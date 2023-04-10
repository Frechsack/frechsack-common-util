package frechsack.prod.util.concurrent.flow;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AsyncSubscriber<Type> implements Flow.Subscriber<Type> {
    private final @NotNull Flow.Subscriber<Type> implementation;
    private final @NotNull Executor executor;
    private final @NotNull Queue<Event> events = new LinkedList<>();

    private final @NotNull ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private @Nullable EventConsumerTask eventConsumerTask;

    private @Nullable Flow.Subscription subscription;

    private boolean isEventConsumerRunning = false;

    private boolean isClosed = false;

    public AsyncSubscriber(@NotNull Flow.Subscriber<Type> implementation, @Nullable Executor executor) {
        this.implementation = implementation;
        this.executor = executor == null ? ForkJoinPool.commonPool() : executor;
    }

    public AsyncSubscriber(@NotNull Flow.Subscriber<Type> implementation) {
        this(implementation, null);
    }

    private void tryStartEventConsumer(){
        try {
            lock.readLock().lock();
            if (isEventConsumerRunning || events.isEmpty())
                return;
        }
        finally {
            lock.readLock().unlock();
        }

        try {
            lock.writeLock().lock();
            if (isEventConsumerRunning || events.isEmpty())
                return;
            if (eventConsumerTask == null)
                eventConsumerTask = new EventConsumerTask();
            try {
                this.isEventConsumerRunning = true;
                executor.execute(eventConsumerTask);
            }
            catch (Exception ignored){
                this.isEventConsumerRunning = false;
            }

        }
        finally {
            lock.writeLock().unlock();
        }
    }

    private void queueEvent(@NotNull Event event){
        try {
            lock.readLock().lock();
            if (isClosed)
                return;
        }
        finally {
            lock.readLock().unlock();
        }

        try {
            lock.writeLock().lock();
            if (isClosed)
                return;
            while (!events.offer(event)) {
                final Iterator<Event> iterator = events.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().isNonPriority()) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        finally {
            lock.writeLock().unlock();
        }
        tryStartEventConsumer();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        if (subscription == null)
            throw new NullPointerException("Subscription is null.");

        try {
            lock.writeLock().lock();
            this.isClosed = false;
            this.subscription = subscription;
        }
        finally {
            lock.writeLock().unlock();
        }
        queueEvent(new OnSubscribeEvent(subscription));
    }

    @Override
    public void onNext(Type item) {
        queueEvent(new OnNextEvent(item));
    }

    @Override
    public void onError(Throwable error) {
        if (error == null)
            throw new NullPointerException("Error is null.");

        try {
            lock.writeLock().lock();
            // Only priority events should be processed from here on.
            events.removeIf(Event::isNonPriority);
            queueEvent(new OnErrorEvent(error));
            isClosed = true;
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void onComplete() {
        try {
            lock.writeLock().lock();
            // Only priority events should be processed from here on.
            events.removeIf(Event::isNonPriority);
            queueEvent(new OnCompleteEvent());
            isClosed = true;
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    private final class EventConsumerTask extends ForkJoinTask<Void>
            implements Runnable, CompletableFuture.AsynchronousCompletionTask {

        public Void getRawResult() { return null; }
        public void setRawResult(Void v) {}
        public boolean exec() {
            try {
                lock.writeLock().lock();
                while (!events.isEmpty()){
                    try {
                        Event event = events.poll();
                        lock.writeLock().unlock();
                        event.act();
                    }
                    catch (Exception e){
                        onError(e);
                    }
                    finally {
                        lock.writeLock().lock();
                    }

                }
            }
            finally {
                isEventConsumerRunning = false;
                lock.writeLock().unlock();
            }
            return false;
        }
        public void run() { exec(); }
    }

    private abstract sealed class Event permits OnCompleteEvent, OnErrorEvent, OnNextEvent, OnSubscribeEvent {
        abstract void act();

        abstract boolean isNonPriority();
    }

    private final class OnSubscribeEvent extends Event {

        private final @NotNull Flow.Subscription subscription;

        private OnSubscribeEvent(Flow.@NotNull Subscription subscription) {
            this.subscription = subscription;
        }

        @Override
        void act() {
            implementation.onSubscribe(subscription);
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
            implementation.onError(error);
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
            implementation.onNext(element);
        }

        @Override
        boolean isNonPriority() {
            return true;
        }
    }

    private final class OnCompleteEvent extends Event {

        @Override
        void act() {
            implementation.onComplete();
        }

        @Override
        boolean isNonPriority() {
            return false;
        }
    }

}
