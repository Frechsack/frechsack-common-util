package frechsack.prod.util.concurrent.flow;

import frechsack.prod.util.collection.ArrayDeque;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A simple implementation of an async {@link java.util.concurrent.Flow.Publisher)}.
 * Each subscription will run in its own thread.
 * If elements are delivered faster than they are consumed, they are buffered.
 * You can deliver elements by {@link #submit(Object)}.
 * Every external available operation on this object is synchronized.
 * @param <Type> The elements type.
 */
public class AsyncPublisher<Type> implements Flow.Publisher<Type> {

    /**
     * The default maximum amount of elements per subscription that will be buffered.
     */
    public static final int DEFAULT_BUFFER_SIZE = 20;

    /**
     * The executor of subscription tasks.
     */
    @NotNull
    private final Executor executor;

    /**
     * A list with active subscriptions.
     */
    @NotNull
    private final ArrayList<@NotNull Subscription> subscriptions = new ArrayList<>();

    /**
     * The buffer size for subscriptions.
     */
    private final int bufferSize;

    /**
     * The lock for synchronized access to the subscriptions.
     */
    @NotNull
    private final ReentrantReadWriteLock subscriptionsLock = new ReentrantReadWriteLock();

    /**
     * Creates a new Object with a custom executor and buffer size.
     * @param executor The executor.
     * @param bufferSize The buffer size.
     */
    public AsyncPublisher(@NotNull Executor executor, int bufferSize){
        this.executor = executor;
        this.bufferSize = bufferSize;
    }

    /**
     * Creates a new Object with a custom executor.
     * @param executor The custom executor. If this executor is null, the default one is used
     */
    public AsyncPublisher(@NotNull Executor executor) {
        this(executor, DEFAULT_BUFFER_SIZE);
    }


    /**
     * Creates a new Object with a {@link ForkJoinPool#commonPool()} executor and the default {@link #DEFAULT_BUFFER_SIZE} buffer size.
     */
    public AsyncPublisher() {
        this(defaultExecutor(), DEFAULT_BUFFER_SIZE);
    }

    public static Executor defaultExecutor(){
        return ForkJoinPool.commonPool();
    }

    /**
     * Checks if any subscriber is subscribed to this publisher.
     * @return Returns true if an active subscriber is subscribed, otherwise false.
     */
    public boolean hasSubscribers(){
        try {
            subscriptionsLock.readLock().lock();
            return !subscriptions.isEmpty();
        }
        finally {
            subscriptionsLock.readLock().unlock();
        }
    }

    /**
     * @see #hasSubscribers()
     */
    public boolean isSubscribed(){
        return hasSubscribers();
    }

    /**
     * Adds an elements to each active subscriber.
     * @param element The element to be passed to the subscribers.
     */
    public void submit(Type element){
        try {
            subscriptionsLock.readLock().lock();
            this.subscriptions.forEach(it -> it.submitElement(element));
        }
        finally {
            subscriptionsLock.readLock().unlock();
        }

    }

    /**
     * Cancels any active subscription. More subscribers can subscribe again to this publisher afterwords.
     */
    public void close() {
        try {
            subscriptionsLock.readLock().lock();
            subscriptions.forEach(Subscription::cancel);
        }
        finally {
            subscriptionsLock.readLock().unlock();
        }

    }

    @Override
    public void subscribe(Flow.Subscriber<? super Type> subscriber) {
        try {
            subscriptionsLock.writeLock().lock();
            subscriptionsLock.readLock().lock();
            for (int i = 0; i < subscriptions.size(); i++) {
                var asyncSubscription = subscriptions.get(i);
                if (Objects.equals(asyncSubscription.subscriber, subscriber)) {
                    if (asyncSubscription.isCompleted())
                        break;
                    else
                        return;
                }
            }
            subscriptionsLock.readLock().unlock();
            subscriptions.add(new Subscription(subscriber, bufferSize));
        }
        finally {
            subscriptionsLock.writeLock().unlock();
        }
    }

    /**
     * The subscriptions used by a {@link AsyncPublisher}.
     * Can be used for detailed monitoring.
     * Every external available operation on this object is synchronized.
     */
    public class Subscription implements Flow.Subscription {

        private static final short NONE = 0;
        private static final short OPENED = 1;
        private static final short COMPLETED_GRACEFULLY = 2;
        private static final short COMPLETED_EXCEPTIONALLY = 3;

        private short state = NONE;
        private boolean isEventConsumerRunning;
        private long demand;

        private EventConsumerTask eventConsumerTask;

        @NotNull
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        @NotNull
        private final Deque<@NotNull Event> events;

        @NotNull
        private final Flow.Subscriber<? super Type> subscriber;

        private Subscription(@NotNull Flow.Subscriber<? super Type> subscriber, int bufferSize) {
            this.subscriber = subscriber;
            // Add 2 elements for a Start & Finish Event.
            this.events = new ArrayDeque<>(bufferSize + 2);
            this.open();
        }

        /**
         * Returns the amount of elements in the queue.
         * @return The amount of elements.
         */
        public int getElementsInQueue(){
            try {
                lock.readLock().lock();
                return (int) events.stream().filter(Event::isNonPriority).count();
            }
            finally {
                lock.readLock().unlock();
            }
        }

        /**
         * Returns the current demand for elements.
         * @return The current demand.
         */
        public long getDemand(){
            try {
                lock.readLock().lock();
                return demand;
            }
            finally {
                lock.readLock().unlock();
            }
        }

        /**
         * Checks if there is currently a demand.
         * @return Returns true if there is a demand, otherwise false.
         */
        public boolean isDemand(){
            try {
                lock.readLock().lock();
                return demand > 0;
            }
            finally {
                lock.readLock().unlock();
            }
        }

        /**
         * Checks if this subscription can receive elements.
         * @return Returns true if this subscription can receive elements, otherwise false.
         */
        public boolean isOpened() {
            try {
                lock.readLock().lock();
                return state == OPENED;
            }
            finally {
                lock.readLock().unlock();
            }
        }

        /**
         * Returns true if this subscription is completed.
         * @return Returns true if this subscription is completed.
         */
        public boolean isCompleted() {
            try {
                lock.readLock().lock();
                return state == COMPLETED_GRACEFULLY || state == COMPLETED_EXCEPTIONALLY;
            }
            finally {
                lock.readLock().unlock();
            }
        }

        /**
         * Returns true if this subscription is completed gracefully - without throwing an exception.
         * @return Returns true if this subscription is completed.
         */
        public boolean isCompletedGracefully() {
            try {
                lock.readLock().lock();
                return state == COMPLETED_GRACEFULLY;
            }
            finally {
                lock.readLock().unlock();
            }
        }

        /**
         * Returns true if this subscription is completed exceptionally - with throwing an exception.
         * @return Returns true if this subscription is completed.
         */
        public boolean isCompletedExceptionally() {
            try {
                lock.readLock().lock();
                return state == COMPLETED_EXCEPTIONALLY;
            }
            finally {
                lock.readLock().unlock();
            }
        }

        private void open() {
            if (isCompleted() || isOpened())
                return;

            try {
                lock.writeLock().lock();
                if (isCompleted() || isOpened())
                    return;
                events.addLast(new OnSubscribeEvent(this));
                state = OPENED;
                processElements();
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        private void completeGracefully() {
            if (isCompleted())
                return;

            try {
                lock.writeLock().lock();
                if (isCompleted())
                    return;
                events.removeIf(Event::isNonPriority);
                events.addLast(new OnCompleteEvent());
                state = COMPLETED_GRACEFULLY;
                processElements();
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        private void completeExceptionally(@NotNull Throwable error) {
            if (isCompleted())
                return;

            try {
                lock.writeLock().lock();
                if (isCompleted())
                    return;
                events.removeIf(Event::isNonPriority);
                events.addLast(new OnErrorEvent(error));
                state = COMPLETED_EXCEPTIONALLY;
                processElements();
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        private void submitElement(Type element) {
            if (isCompleted())
                return;

            try {
                lock.writeLock().lock();
                if (isCompleted())
                    return;
                final OnNextEvent event = new OnNextEvent(element);
                while (!events.offerLast(event))
                    events.pollFirst();
                processElements();
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void request(long n) {
            if (isCompleted())
                return;

            try {
                lock.writeLock().lock();
                if (isCompleted())
                    return;
                if (n <= 0){
                    completeExceptionally(new IllegalArgumentException());
                    return;
                }
                demand = Long.max(n, demand + n);
                processElements();
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void cancel() {
            completeGracefully();
        }

        private void processElements(){
            try {
                lock.readLock().lock();
                if (isEventConsumerRunning)
                    return;
            }
            finally {
                lock.readLock().unlock();
            }
            try {
                lock.writeLock().lock();
                if (isEventConsumerRunning)
                    return;
                isEventConsumerRunning = true;
                if (eventConsumerTask == null)
                    eventConsumerTask = new EventConsumerTask();
                try {
                    executor.execute(eventConsumerTask);
                }
                catch (Exception e){
                    // In case executor does not work -> complete exceptionally synchronous.
                    isEventConsumerRunning = false;
                    subscriber.onError(e);
                    state = COMPLETED_EXCEPTIONALLY;
                    events.clear();
                }
            }
            finally {
                lock.writeLock().unlock();
            }

        }

        private final class EventConsumerTask extends ForkJoinTask<Void>
                implements Runnable, CompletableFuture.AsynchronousCompletionTask {

            public Void getRawResult() { return null; }
            public void setRawResult(Void v) {}

            private boolean isLoop(){
                return !events.isEmpty() && (events.peekLast().isPriority() || demand > 0);
            }

            public boolean exec(){
                try {
                    lock.readLock().lock();
                    while (isLoop()){
                        try {
                            lock.readLock().unlock();
                            lock.writeLock().lock();
                            if (!isLoop())
                                return false;

                            Event event = events.pollFirst();

                            try {
                                lock.writeLock().unlock();
                                //noinspection DataFlowIssue
                                event.act();
                            } finally {
                                lock.writeLock().lock();
                            }


                            if (event.isNonPriority())
                                demand--;
                        }
                        catch (Exception e){
                            Subscription.this.completeExceptionally(e);
                        }
                        finally {
                            lock.writeLock().unlock();
                            lock.readLock().lock();
                        }
                    }
                }
                finally {
                    lock.readLock().unlock();
                    lock.writeLock().lock();
                    isEventConsumerRunning = false;
                    lock.writeLock().unlock();
                }
                return false;
            }

            public void run() { exec(); }
        }

        private abstract sealed class Event permits AsyncPublisher.Subscription.OnCompleteEvent, AsyncPublisher.Subscription.OnErrorEvent, AsyncPublisher.Subscription.OnNextEvent, AsyncPublisher.Subscription.OnSubscribeEvent {
            abstract void act();

            abstract boolean isNonPriority();

            final boolean isPriority(){
                return !isNonPriority();
            }
        }

        private final class OnSubscribeEvent extends Event {

            private final @NotNull Flow.Subscription subscription;

            private OnSubscribeEvent(@NotNull Flow.Subscription subscription) {
                this.subscription = subscription;
            }

            @Override
            void act() {
                subscriber.onSubscribe(subscription);
            }

            @Override
            boolean isNonPriority() {
                return false;
            }
        }

        private final class OnErrorEvent extends Event {

            private final @NotNull Throwable error;

            private OnErrorEvent(@NotNull Throwable error) {
                this.error = error;
            }

            @Override
            void act() {
                subscriber.onError(error);

                // Remove this subscription from AsyncPublisher
                try {
                    AsyncPublisher.this.subscriptionsLock.writeLock().lock();
                    AsyncPublisher.this.subscriptions.remove(AsyncPublisher.Subscription.this);
                }
                finally {
                    AsyncPublisher.this.subscriptionsLock.writeLock().unlock();
                }
            }

            @Override
            boolean isNonPriority() {
                return false;
            }
        }

        private final class OnNextEvent extends Event {

            private final @Nullable Type element;

            private OnNextEvent(@Nullable Type element) {
                this.element = element;
            }

            @Override
            void act() {
                subscriber.onNext(element);
            }

            @Override
            boolean isNonPriority() {
                return true;
            }
        }

        private final class OnCompleteEvent extends Event {

            @Override
            void act() {
                subscriber.onComplete();
                // Remove this subscription from AsyncPublisher
                try {
                    AsyncPublisher.this.subscriptionsLock.writeLock().lock();
                    AsyncPublisher.this.subscriptions.remove(AsyncPublisher.Subscription.this);
                }
                finally {
                    AsyncPublisher.this.subscriptionsLock.writeLock().unlock();
                }
            }

            @Override
            boolean isNonPriority() {
                return false;
            }
        }

    }
}
