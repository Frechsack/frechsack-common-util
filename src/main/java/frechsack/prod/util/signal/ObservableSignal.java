package frechsack.prod.util.signal;

import frechsack.prod.util.concurrent.flow.AsyncPublisher;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.locks.ReentrantReadWriteLock;

sealed abstract class ObservableSignal<Type> implements Signal<Type> permits DependingSignal, WriteableBooleanSignal, WriteableDoubleSignal, WriteableIntSignal, WriteableLongSignal, WriteableObjectSignal {

    private AsyncPublisher<Type> changePublisher;

    private AsyncPublisher<Signal<?>> invalidationPublisher;

    private final ReentrantReadWriteLock publisherLock = new ReentrantReadWriteLock();

    protected final @Nullable Executor executor;

    ObservableSignal(@Nullable Executor executor) {
        this.executor = executor;
    }

    protected boolean isEagerRequired(){
        try {
            publisherLock.readLock().lock();
            return changePublisher != null && changePublisher.hasSubscribers();
        }
        finally {
            publisherLock.readLock().unlock();
        }

    }

    @Override
    public void close() {
        publisherLock.readLock().lock();
        if (changePublisher != null)
            changePublisher.close();
        if (invalidationPublisher != null)
            invalidationPublisher.close();
        publisherLock.readLock().unlock();
    }

    protected void fireInvalidation(){
        publisherLock.readLock().lock();
        AsyncPublisher<Signal<?>> publisher = this.invalidationPublisher;
        publisherLock.readLock().unlock();
        if (publisher != null)
            publisher.submit(this);
    }

    protected void fireChange(Type newValue){
        publisherLock.readLock().lock();
        AsyncPublisher<Type> publisher = this.changePublisher;
        publisherLock.readLock().unlock();
        if(publisher != null)
            publisher.submit(newValue);
    }

    @Override
    public void subscribeOnChange(Flow.Subscriber<? super Type> subscriber) {
        publisherLock.writeLock().lock();
        if (changePublisher == null)
            changePublisher = new AsyncPublisher<>(executor == null ? AsyncPublisher.defaultExecutor() : executor);
        changePublisher.subscribe(subscriber);
        publisherLock.writeLock().unlock();
    }

    @Override
    public void subscribeOnInvalidate(Flow.Subscriber<? super Signal<?>> subscriber) {
        publisherLock.writeLock().lock();
        if (invalidationPublisher == null)
            invalidationPublisher = new AsyncPublisher<>(executor == null ? AsyncPublisher.defaultExecutor() : executor);
        invalidationPublisher.subscribe(subscriber);
        publisherLock.writeLock().unlock();

    }

}
