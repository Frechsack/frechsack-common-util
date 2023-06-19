package frechsack.prod.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

public class SignalDoublePipe {

    public static final boolean DEFAULT_SHARED = false;

    private @NotNull DoubleSupplier generator;
    private final @NotNull Collection<Signal<?>> parents;
    private final boolean isShared;

    private final @Nullable Executor executor;

    private final ReentrantLock lock = new ReentrantLock();

    public SignalDoublePipe(@NotNull DoubleSupplier generator, @NotNull Collection<Signal<?>> parents, boolean isShared, @Nullable Executor executor) {
        this.generator = generator;
        this.parents = parents;
        this.isShared = isShared;
        this.executor = executor;
    }

    private SignalDoublePipe pipe(@NotNull DoubleSupplier supplier){
        if (isShared)
            return new SignalDoublePipe(supplier, parents, true, executor);
        this.generator = supplier;
        return this;
    }

    public SignalDoublePipe map(@NotNull DoubleUnaryOperator operator){
        lock.lock();
        Objects.requireNonNull(operator);
        final DoubleSupplier parentGenerator = this.generator;
        DoubleSupplier generator = () -> operator.applyAsDouble(parentGenerator.getAsDouble());
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe peek(@NotNull DoubleConsumer consumer){
        lock.lock();
        final DoubleSupplier parentGenerator = this.generator;
        Objects.requireNonNull(consumer);
        var result = pipe(() -> {
            double value = parentGenerator.getAsDouble();
            consumer.accept(value);
            return value;
        });
        lock.unlock();
        return result;
    }

    public SignalDoublePipe filter(@NotNull DoublePredicate predicate){
        lock.lock();
        final DoubleSupplier parentGenerator = this.generator;
        Objects.requireNonNull(predicate);
        DoubleSupplier generator = new DoubleSupplier() {
            double lastValidValue = 0;
            @Override
            public double getAsDouble() {
                double newValue = parentGenerator.getAsDouble();
                if (predicate.test(newValue))
                    lastValidValue = newValue;
                return lastValidValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe reduce(DoubleBinaryOperator operator){
        lock.lock();
        final DoubleSupplier parentGenerator = this.generator;
        Objects.requireNonNull(operator);
        DoubleSupplier generator = new DoubleSupplier() {
            double lastValue = 0;
            @Override
            public double getAsDouble() {
                lastValue = operator.applyAsDouble(lastValue, parentGenerator.getAsDouble());
                return lastValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe range(int minInclusive, int maxInclusive){
        lock.lock();
        final DoubleSupplier parentGenerator = this.generator;
        DoubleSupplier generator = new DoubleSupplier() {
            double lastValidValue = 0;
            @Override
            public double getAsDouble() {
                double returnValue = parentGenerator.getAsDouble();
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

    public <Type> SignalPipe<Type> mapToObject(DoubleFunction<Type> function){
        lock.lock();
        Objects.requireNonNull(function);
        final DoubleSupplier parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.getAsDouble());
        var result = new SignalPipe<>(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public <Type extends Number> SignalNumberPipe<Type> mapToNumber(DoubleFunction<Type> function){
        lock.lock();
        Objects.requireNonNull(function);
        final DoubleSupplier parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.getAsDouble());
        var result = new SignalNumberPipe<>(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe mapToDouble(){
        return this;
    }

    public SignalIntPipe mapToInt(){
        lock.lock();
        final DoubleSupplier parentGenerator = this.generator;
        var result = new SignalIntPipe(() -> (int) parentGenerator.getAsDouble(), parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalLongPipe mapToLong(){
        lock.lock();
        final DoubleSupplier parentGenerator = this.generator;
        var result = new SignalLongPipe(() -> (long) parentGenerator.getAsDouble(), parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalNumberPipe<Double> boxed() {
        lock.lock();
        var result = mapToNumber(it -> it);
        lock.unlock();
        return result;
    }

    public Signal.Number<Double> build(){
        lock.lock();
        var result = new DependingDoubleSignal(generator, parents, executor);
        lock.unlock();
        return result;
    }

    public Signal.Number<Integer> buildInt(){
        lock.lock();
        final DoubleSupplier parentGenerator = this.generator;
        IntSupplier generator = () -> (int) parentGenerator.getAsDouble();
        var result = new DependingIntSignal(generator, parents, executor);
        lock.unlock();
        return result;
    }

    public Signal.Number<Long> buildLong(){
        lock.lock();
        final DoubleSupplier parentGenerator = this.generator;
        LongSupplier generator = () -> (long) parentGenerator.getAsDouble();
        var result = new DependingLongSignal(generator, parents, executor);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe shared() {
        lock.lock();
        var result = isShared ? this : new SignalDoublePipe(generator, parents, true, executor);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe exclusive() {
        lock.lock();
        var result = isShared ? new SignalDoublePipe(generator, parents, false, executor) : this;
        lock.unlock();
        return result;
    }
}
