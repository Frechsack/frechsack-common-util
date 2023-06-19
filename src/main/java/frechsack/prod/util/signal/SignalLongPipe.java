package frechsack.prod.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

public class SignalLongPipe {

    public static final boolean DEFAULT_SHARED = false;

    private @NotNull LongSupplier generator;
    private final @NotNull Collection<Signal<?>> parents;
    private final boolean isShared;
    private final @Nullable Executor executor;
    private final ReentrantLock lock = new ReentrantLock();

    public SignalLongPipe(@NotNull LongSupplier generator, @NotNull Collection<Signal<?>> parents, boolean isShared, @Nullable Executor executor) {
        this.generator = generator;
        this.parents = parents;
        this.isShared = isShared;
        this.executor = executor;
    }

    private SignalLongPipe pipe(@NotNull LongSupplier supplier){
        if (isShared)
            return new SignalLongPipe(supplier, parents, true, executor);
        this.generator = supplier;
        return this;
    }

    public SignalLongPipe map(@NotNull LongUnaryOperator operator){
        lock.lock();
        Objects.requireNonNull(operator);
        final LongSupplier parentGenerator = this.generator;
        LongSupplier supplier = () -> operator.applyAsLong(parentGenerator.getAsLong());
        SignalLongPipe result = pipe(supplier);
        lock.unlock();
        return result;
    }

    public SignalLongPipe peek(@NotNull LongConsumer consumer){
        lock.lock();
        final LongSupplier parentGenerator = this.generator;
        Objects.requireNonNull(consumer);
        SignalLongPipe result = pipe(() -> {
            long value = parentGenerator.getAsLong();
            consumer.accept(value);
            return value;
        });
        lock.unlock();
        return result;
    }

    public SignalLongPipe filter(@NotNull LongPredicate predicate){
        lock.lock();
        final LongSupplier parentGenerator = this.generator;
        Objects.requireNonNull(predicate);
        LongSupplier generator = new LongSupplier() {
            long lastValidValue = 0;
            @Override
            public long getAsLong() {
                long newValue = parentGenerator.getAsLong();
                if (predicate.test(newValue))
                    lastValidValue = newValue;
                return lastValidValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public SignalLongPipe reduce(@NotNull LongBinaryOperator operator){
        lock.lock();
        final LongSupplier parentGenerator = this.generator;
        Objects.requireNonNull(operator);
        LongSupplier generator = new LongSupplier() {
            long lastValue = 0;
            @Override
            public long getAsLong() {
                lastValue = operator.applyAsLong(lastValue, parentGenerator.getAsLong());
                return lastValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public SignalLongPipe range(int minInclusive, int maxInclusive){
        lock.lock();
        final LongSupplier parentGenerator = this.generator;
        LongSupplier generator = new LongSupplier() {
            long lastValidValue = 0;
            @Override
            public long getAsLong() {
                long returnValue = parentGenerator.getAsLong();
                if(returnValue < minInclusive || returnValue > maxInclusive)
                    returnValue = lastValidValue;
                else
                    lastValidValue = returnValue;
                return returnValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public <Type> SignalPipe<Type> mapToObject(@NotNull LongFunction<Type> function){
        lock.lock();
        Objects.requireNonNull(function);
        final LongSupplier parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.getAsLong());
        var result = new SignalPipe<>(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public <Type extends Number> SignalNumberPipe<Type> mapToNumber(@NotNull LongFunction<Type> function){
        lock.lock();
        Objects.requireNonNull(function);
        final LongSupplier parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.getAsLong());
        var result = new SignalNumberPipe<>(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe mapToDouble(){
        lock.lock();
        final LongSupplier parentGenerator = this.generator;
        var result = new SignalDoublePipe(() -> (double) parentGenerator.getAsLong(), parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalIntPipe mapToInt(){
        lock.lock();
        final LongSupplier parentGenerator = this.generator;
        var result = new SignalIntPipe(() -> (int) parentGenerator.getAsLong(), parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalLongPipe mapToLong(){
        return this;
    }

    public SignalNumberPipe<Long> boxed() {
        lock.lock();
        var result = mapToNumber(it -> it);
        lock.unlock();
        return result;
    }

    public Signal.Number<Long> build(){
        lock.lock();
        var result = new DependingLongSignal(generator, parents, executor);
        lock.unlock();
        return result;
    }

    public Signal.Number<Integer> buildInt(){
        lock.lock();
        final LongSupplier parentGenerator = this.generator;
        IntSupplier generator = () -> (int) parentGenerator.getAsLong();
        var result = new DependingIntSignal(generator, parents, executor);
        lock.unlock();
        return result;
    }

    public Signal.Number<Double> buildDouble(){
        lock.lock();
        final LongSupplier parentGenerator = this.generator;
        DoubleSupplier generator = () -> (double) parentGenerator.getAsLong();
        var result = new DependingDoubleSignal(generator, parents, executor);
        lock.unlock();
        return result;
    }

    public SignalLongPipe shared() {
        lock.lock();
        var result = isShared ? this : new SignalLongPipe(generator, parents, true, executor);
        lock.unlock();
        return result;
    }

    public SignalLongPipe exclusive() {
        lock.lock();
        var result = isShared ? new SignalLongPipe(generator, parents, false, executor) : this;
        lock.unlock();
        return result;
    }
}
