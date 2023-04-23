package frechsack.dev.util.signal;

import frechsack.prod.util.concurrent.flow.AsyncPublisher;

import java.util.concurrent.Flow;
import java.util.concurrent.locks.ReentrantReadWriteLock;

sealed abstract class ObservableSignal<Type> implements Signal<Type> permits DependingSignal, WriteableBooleanSignal, WriteableDoubleSignal, WriteableIntSignal, WriteableLongSignal, WriteableObjectSignal {

    private AsyncPublisher<Type> changePublisher;

    private AsyncPublisher<Signal<?>> invalidationPublisher;

    private final ReentrantReadWriteLock publisherLock = new ReentrantReadWriteLock();

    protected boolean isEagerRequired(){
        return changePublisher != null && changePublisher.hasSubscribers();
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
            changePublisher = new AsyncPublisher<>();
        changePublisher.subscribe(subscriber);
        publisherLock.writeLock().unlock();
    }

    @Override
    public void subscribeOnInvalidate(Flow.Subscriber<Signal<?>> subscriber) {
        publisherLock.writeLock().lock();
        if (invalidationPublisher == null)
            invalidationPublisher = new AsyncPublisher<>();
        invalidationPublisher.subscribe(subscriber);
        publisherLock.writeLock().unlock();

    }

}
