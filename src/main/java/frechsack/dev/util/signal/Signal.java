package frechsack.dev.util.signal;

import frechsack.prod.util.concurrent.flow.AsyncSubscriber;
import frechsack.prod.util.concurrent.flow.AutoUnsubscribeSubscriber;
import frechsack.prod.util.concurrent.flow.CompactSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Flow;
import java.util.function.*;

public interface Signal<Type> extends Supplier<Type>, Flow.Publisher<Type>, Closeable, AutoCloseable {

    static <Type extends java.lang.Number> WriteableNumberSignal<Type> ofNumber(@NotNull DoubleFunction<Type> doubleFunction, @NotNull IntFunction<Type> intFunction, @NotNull LongFunction<Type> longFunction,@Nullable Type initial){
        return new WriteableNumberSignal<>(doubleFunction, intFunction, longFunction, initial);
    }

    static <Type> WriteableObjectSignal<Type> of(Type initial){
        return new WriteableObjectSignal<>(initial);
    }

    static WriteableIntSignal ofInt(int initial){
        return new WriteableIntSignal(initial);
    }

    static WriteableDoubleSignal ofDouble(double initial){
        return new WriteableDoubleSignal(initial);
    }

    static WriteableLongSignal ofLong(long initial){
        return new WriteableLongSignal(initial);
    }

    default SignalPipe<Type> pipe(){
        return new SignalPipe<>(this, List.of(this), SignalPipe.DEFAULT_SHARED);
    }

    void subscribeOnInvalidate(Flow.Subscriber<Signal<?>> subscriber);

    void subscribeOnChange(Flow.Subscriber<? super Type> subscriber);

    @Override
    @Deprecated
    default void subscribe(Flow.Subscriber<? super Type> subscriber){
        subscribeOnChange(subscriber);
    }

    default Queue<Type> elements(int capacity){
        ArrayBlockingQueue<Type> queue = new ArrayBlockingQueue<>(capacity);
        subscribeOnChange(new AutoUnsubscribeSubscriber<>(new WeakReference<>(queue), new CompactSubscriber<>(Long.MAX_VALUE) {
            @Override
            public void onNext(Type item) {
                queue.offer(item);
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
            return new SignalBooleanPipe(this, List.of(this), SignalBooleanPipe.DEFAULT_SHARED);
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
            return new SignalIntPipe(this, List.of(this), SignalIntPipe.DEFAULT_SHARED);
        }

        default SignalDoublePipe pipeDouble(){
            return new SignalDoublePipe(this, List.of(this), SignalDoublePipe.DEFAULT_SHARED);
        }

        default SignalLongPipe pipeLong(){
            return new SignalLongPipe(this, List.of(this), SignalLongPipe.DEFAULT_SHARED);
        }

        @Override
        default SignalNumberPipe<Type> pipe(){
            return new SignalNumberPipe<>(this, List.of(this), SignalNumberPipe.DEFAULT_SHARED);
        }
    }
}
