package frechsack.dev.util.signal;

import frechsack.prod.util.concurrent.flow.SyncPublisher;

import java.io.IOException;
import java.util.concurrent.Flow;
import java.util.concurrent.locks.ReentrantReadWriteLock;

sealed abstract class ObservableSignal<Type> implements Signal<Type> permits DependingSignal, WriteableBooleanSignal, WriteableDoubleSignal, WriteableIntSignal, WriteableLongSignal, WriteableObjectSignal {

    private SyncPublisher<Type> changePublisher;

    private SyncPublisher<Signal<?>> invalidationPublisher;

    private final ReentrantReadWriteLock publisherLock = new ReentrantReadWriteLock();

    protected boolean isEagerRequired(){
        return changePublisher != null && changePublisher.hasSubscribers();
    }

    protected boolean hasSubscribers(){
        try {
            publisherLock.readLock().lock();
            return (changePublisher != null && changePublisher.hasSubscribers())
                    || (invalidationPublisher != null && invalidationPublisher.hasSubscribers());
        }
        finally {
            publisherLock.readLock().unlock();
        }
    }

    @Override
    public void close() throws IOException {
        publisherLock.writeLock().lock();
        if (changePublisher != null)
            changePublisher.close();
        if (invalidationPublisher != null)
            invalidationPublisher.close();
        publisherLock.writeLock().unlock();
    }

    protected void fireInvalidation(){
        publisherLock.readLock().lock();
        SyncPublisher<Signal<?>> publisher = this.invalidationPublisher;
        publisherLock.readLock().unlock();
        if (publisher != null)
            publisher.submit(this);
    }

    protected void fireChange(Type newValue){
        publisherLock.readLock().lock();
        SyncPublisher<Type> publisher = this.changePublisher;
        publisherLock.readLock().unlock();
        if(publisher != null)
            publisher.submit(newValue);
    }

    @Override
    public void subscribeOnChange(Flow.Subscriber<? super Type> subscriber) {
        publisherLock.writeLock().lock();
        if (changePublisher == null)
            changePublisher = new SyncPublisher<>();
        publisherLock.writeLock().unlock();
        changePublisher.subscribe(subscriber);
    }

    @Override
    public void subscribeOnInvalidate(Flow.Subscriber<Signal<?>> subscriber) {
        publisherLock.writeLock().lock();
        if (invalidationPublisher == null)
            invalidationPublisher = new SyncPublisher<>();
        publisherLock.writeLock().unlock();
        invalidationPublisher.subscribe(subscriber);
    }

}
