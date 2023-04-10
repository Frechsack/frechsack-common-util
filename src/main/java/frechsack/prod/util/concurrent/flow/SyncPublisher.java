package frechsack.prod.util.concurrent.flow;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.Flow;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SyncPublisher<Type> implements Flow.Publisher<Type>, AutoCloseable {

    private static final int DEFAULT_BUFFER_SIZE = 10;

    private final ArrayList<Subscription> subscriptions = new ArrayList<>(0);

    private final int bufferSize;

    public SyncPublisher(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public SyncPublisher() {
        this(DEFAULT_BUFFER_SIZE);
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Type> subscriber) {
        if(subscriptions.stream().anyMatch(it -> Objects.equals(it.subscriber, subscriber)))
            return;
            subscriptions.add(new Subscription(subscriber));
    }

    @Override
    public void close(){
        subscriptions.forEach(Subscription::cancel);
    }

    public void submit(Type element){
        subscriptions.forEach(it -> it.queueElement(element));
    }

    public boolean hasSubscribers(){
        return !subscriptions.isEmpty();
    }

    private class Subscription implements Flow.Subscription {

        private final @NotNull Flow.Subscriber<? super Type> subscriber;

        private final @NotNull Queue<Type> elements = new LinkedList<>();

        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

        private @NotNull State state = State.OPENING;

        private long demand;

        private boolean isConsuming = false;

        private Subscription(Flow.@NotNull Subscriber<? super Type> subscriber) {
            this.subscriber = subscriber;
            consume();
            // TODO: Custom Queue
        }

        private boolean isDemand(){
            try {
                lock.readLock().lock();
                return demand > 0;
            }
            finally {
                lock.readLock().unlock();
            }
        }

        private boolean isElementsAvailable(){
            try {
                lock.readLock().lock();
                return !elements.isEmpty();
            }
            finally {
                lock.readLock().unlock();
            }
        }

        private void consume(){
            try {
                lock.readLock().lock();
                if (isConsuming)
                    return;
            }
            finally {
                lock.readLock().unlock();
            }

            try {
                lock.writeLock().lock();
                if (isConsuming)
                    return;
                isConsuming = true;
                try {
                    if (state == State.OPENING){
                        state = State.OPENED;
                        lock.writeLock().unlock();
                        subscriber.onSubscribe(this);
                        lock.writeLock().lock();
                    }
                    if (state == State.OPENED){
                        while (isDemand() && isElementsAvailable() && state != State.COMPLETED && state != State.ERROR){
                            Type element = elements.poll();
                            demand--;
                            lock.writeLock().unlock();
                            subscriber.onNext(element);
                            lock.writeLock().lock();

                        }
                    }
                    if (state == State.COMPLETING){
                        state = State.COMPLETED;
                        lock.writeLock().unlock();
                        subscriber.onComplete();
                        lock.writeLock().lock();
                    }
                }
                catch (Exception e) {
                    state = State.ERROR;
                    subscriber.onError(e);
                }
            }
            finally {
                isConsuming = false;
                lock.writeLock().unlock();
            }

        }

        private void queueElement(Type element){
            lock.writeLock().lock();
            if (state == State.ERROR || state == State.COMPLETED)
                return;
            while (!elements.offer(element))
                elements.poll();
            lock.writeLock().unlock();
            consume();
        }

        @Override
        public void request(long n) {
            if (n <= 0){
                lock.writeLock().lock();
                state = State.ERROR;
                subscriber.onError(new IllegalArgumentException("Demand must be greater than zero."));
                lock.writeLock().unlock();
            }
            else {
                lock.writeLock().lock();
                demand = Long.max(demand, demand+n);
                lock.writeLock().unlock();
                consume();
            }
        }

        @Override
        public void cancel() {
            try {
                lock.writeLock().lock();
                if (state == State.ERROR)
                    return;

                state = State.COMPLETING;
                // Will never be passed again
                elements.clear();
                consume();
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        private enum State {

            OPENING,

            OPENED,

            ERROR,

            COMPLETING,

            COMPLETED,
        }
    }
}
