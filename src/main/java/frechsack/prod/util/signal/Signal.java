package frechsack.prod.util.signal;

import frechsack.prod.util.collection.ArrayDeque;
import frechsack.prod.util.collection.CollectionUtils;
import frechsack.prod.util.concurrent.flow.AutoUnsubscribeSubscriber;
import frechsack.prod.util.concurrent.flow.CompactSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.function.*;

public interface Signal<Type> extends Supplier<Type>, Flow.Publisher<Type>, Closeable, AutoCloseable {

    static <Type extends java.lang.Number> WriteableNumberSignal<Type> ofNumber(@NotNull DoubleFunction<Type> doubleFunction, @NotNull IntFunction<Type> intFunction, @NotNull LongFunction<Type> longFunction, @Nullable Type initial, @Nullable Executor executor){
        return new WriteableNumberSignal<>(doubleFunction, intFunction, longFunction, initial, executor);
    }

    static <Type extends java.lang.Number> WriteableNumberSignal<Type> ofNumber(@NotNull DoubleFunction<Type> doubleFunction, @NotNull IntFunction<Type> intFunction, @NotNull LongFunction<Type> longFunction, @Nullable Type initial){
        return new WriteableNumberSignal<>(doubleFunction, intFunction, longFunction, initial, null);
    }

    static <Type> SignalPipe<Type> pipeOf(@NotNull Supplier<Type> generator, @Nullable Executor executor, Signal<?>... parents){
        return new SignalPipe<>(generator, Arrays.asList(parents), SignalPipe.DEFAULT_SHARED, executor);
    }

    static <Type> SignalPipe<Type> pipeOf(@NotNull Supplier<Type> generator, Signal<?>... parents){
        return new SignalPipe<>(generator, Arrays.asList(parents), SignalPipe.DEFAULT_SHARED, null);
    }

    static <Type> WriteableObjectSignal<Type> of(@Nullable Type initial, @Nullable Executor executor){
        return new WriteableObjectSignal<>(initial, executor);
    }

    static <Type> WriteableObjectSignal<Type> of(@Nullable Type initial){
        return new WriteableObjectSignal<>(initial, null);
    }

    static SignalIntPipe pipeOfInt(@NotNull IntSupplier generator, @Nullable Executor executor, @NotNull Signal<?>... parents){
        return new SignalIntPipe(generator, Arrays.asList(parents), SignalPipe.DEFAULT_SHARED, executor);
    }

    static SignalIntPipe pipeOfInt(@NotNull IntSupplier generator, @NotNull Signal<?>... parents){
        return new SignalIntPipe(generator, Arrays.asList(parents), SignalPipe.DEFAULT_SHARED, null);
    }

    static WriteableIntSignal ofInt(int initial, @Nullable Executor executor){
        return new WriteableIntSignal(initial, executor);
    }

    static WriteableIntSignal ofInt(int initial){
        return new WriteableIntSignal(initial, null);
    }

    static SignalDoublePipe pipeOfDouble(@NotNull DoubleSupplier generator, @Nullable Executor executor, @NotNull Signal<?>... parents){
        return new SignalDoublePipe(generator, Arrays.asList(parents), SignalPipe.DEFAULT_SHARED, executor);
    }

    static SignalDoublePipe pipeOfDouble(@NotNull DoubleSupplier generator, @NotNull Signal<?>... parents){
        return new SignalDoublePipe(generator, Arrays.asList(parents), SignalPipe.DEFAULT_SHARED, null);
    }

    static WriteableDoubleSignal ofDouble(double initial, @Nullable Executor executor){
        return new WriteableDoubleSignal(initial, executor);
    }

    static WriteableDoubleSignal ofDouble(double initial){
        return new WriteableDoubleSignal(initial, null);
    }

    static SignalLongPipe pipeOfLong(@NotNull LongSupplier generator, @Nullable Executor executor, @NotNull Signal<?>... parents){
        return new SignalLongPipe(generator, Arrays.asList(parents), SignalPipe.DEFAULT_SHARED, executor);
    }

    static SignalLongPipe pipeOfLong(@NotNull LongSupplier generator, @NotNull Signal<?>... parents){
        return new SignalLongPipe(generator, Arrays.asList(parents), SignalPipe.DEFAULT_SHARED, null);
    }

    static WriteableLongSignal ofLong(long initial, @Nullable Executor executor){
        return new WriteableLongSignal(initial, executor);
    }

    static WriteableLongSignal ofLong(long initial){
        return new WriteableLongSignal(initial, null);
    }

    /**
     * Creates a pipe to build new signals, that depend on this signal.
     * @return Returns a new pipe.
     */
    default SignalPipe<Type> pipe(){
        Executor executor = this instanceof ObservableSignal<Type> obs ? obs.executor : null;
        return new SignalPipe<>(this, List.of(this), SignalPipe.DEFAULT_SHARED, executor);
    }

    /**
     * Subscribes to this signal and receives events when this signal becomes invalid.
     * @param subscriber The subscriber to be notified.
     */
    void subscribeOnInvalidate(Flow.Subscriber<? super Signal<?>> subscriber);

    /**
     * Subscribes to this signal and receives the current value of this signal.
     * @param subscriber The subscriber to be notified.
     */
    void subscribeOnChange(Flow.Subscriber<? super Type> subscriber);

    @Override
    @Deprecated
    default void subscribe(Flow.Subscriber<? super Type> subscriber){
        subscribeOnChange(subscriber);
    }

    /**
     * Returns a queue that contains the elements submitted by this signal.
     * @param capacity The capacity of the underlying queue.
     * @return Returns a queue with the elements.
     */
    default Queue<Type> elements(int capacity){
        Queue<Type> queue = CollectionUtils.synchronizedQueue(new ArrayDeque<>(capacity));
        subscribeOnChange(new AutoUnsubscribeSubscriber<>(new WeakReference<>(queue), new CompactSubscriber<>(Long.MAX_VALUE) {
            @Override
            public void onNext(Type item) {
                if(!queue.offer(item))
                    queue.poll();
            }
        }));
        return queue;
    }

    interface Boolean extends Signal<java.lang.Boolean>, BooleanSupplier {

        @Override
        default boolean getAsBoolean(){
            java.lang.Boolean value = get();
            return value != null && value;
        }

        default SignalBooleanPipe pipeBoolean(){
            Executor executor = this instanceof ObservableSignal<?> obs ? obs.executor : null;
            return new SignalBooleanPipe(this, List.of(this), SignalBooleanPipe.DEFAULT_SHARED, executor);
        }
    }

    interface Number<Type extends java.lang.Number> extends Signal<Type>, IntSupplier, DoubleSupplier, LongSupplier {

        @Override
        default double getAsDouble() {
            Type value = get();
            return value == null ? 0 : value.doubleValue();
        }

        @Override
        default int getAsInt() {
            Type value = get();
            return value == null ? 0 : value.intValue();
        }

        @Override
        default long getAsLong() {
            Type value = get();
            return value == null ? 0 : value.longValue();
        }

        default SignalIntPipe pipeInt(){
            Executor executor = this instanceof ObservableSignal<?> obs ? obs.executor : null;
            return new SignalIntPipe(this, List.of(this), SignalIntPipe.DEFAULT_SHARED, executor);
        }

        default SignalDoublePipe pipeDouble(){
            Executor executor = this instanceof ObservableSignal<?> obs ? obs.executor : null;
            return new SignalDoublePipe(this, List.of(this), SignalDoublePipe.DEFAULT_SHARED, executor);
        }

        default SignalLongPipe pipeLong(){
            Executor executor = this instanceof ObservableSignal<?> obs ? obs.executor : null;
            return new SignalLongPipe(this, List.of(this), SignalLongPipe.DEFAULT_SHARED, executor);
        }

        @Override
        default SignalNumberPipe<Type> pipe(){
            Executor executor = this instanceof ObservableSignal<?> obs ? obs.executor : null;
            return new SignalNumberPipe<>(this, List.of(this), SignalNumberPipe.DEFAULT_SHARED, executor);
        }
    }
}
