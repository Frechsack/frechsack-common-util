package frechsack.dev.util.signal;

import frechsack.prod.util.concurrent.flow.AsyncSubscriber;
import frechsack.prod.util.concurrent.flow.SyncPublisher;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.locks.ReentrantReadWriteLock;

sealed abstract class ObservableSignal<Type> implements Signal<Type> permits DependingSignal, WriteableBooleanSignal, WriteableDoubleSignal, WriteableIntSignal, WriteableLongSignal, WriteableObjectSignal {

    // TODO: SubmissionPublisher does not allow null elements.
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
    public void close() {
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
        changePublisher.subscribe(new AsyncSubscriber<>(subscriber));
        publisherLock.writeLock().unlock();
    }

    @Override
    public void subscribeOnInvalidate(Flow.Subscriber<Signal<?>> subscriber) {
        publisherLock.writeLock().lock();
        if (invalidationPublisher == null)
            invalidationPublisher = new SyncPublisher<>();
        invalidationPublisher.subscribe(new AsyncSubscriber<>(subscriber));
        publisherLock.writeLock().unlock();

    }

}
