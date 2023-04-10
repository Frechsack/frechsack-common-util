package frechsack.dev.util.signal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;
import java.util.stream.Stream;

public final class SignalIntPipe {

    public static final boolean DEFAULT_SHARED = false;

    private @NotNull IntSupplier generator;
    private final @NotNull Collection<Signal<?>> parents;
    private final boolean isShared;

    private final ReentrantLock lock = new ReentrantLock();

    public SignalIntPipe(@NotNull IntSupplier generator, @NotNull Collection<Signal<?>> parents, boolean isShared) {
        this.generator = generator;
        this.parents = parents;
        this.isShared = isShared;
    }

    private SignalIntPipe pipe(@NotNull IntSupplier supplier){
        if (isShared)
            return new SignalIntPipe(supplier, parents, true);
        this.generator = supplier;
        return this;
    }

    public SignalIntPipe peek(@NotNull IntConsumer consumer){
        lock.lock();
        final IntSupplier parentGenerator = this.generator;
        Objects.requireNonNull(consumer);
        var result = pipe(() -> {
           int value = parentGenerator.getAsInt();
           consumer.accept(value);
           return value;
        });
        lock.unlock();
        return result;
    }

    public SignalIntPipe filter(@NotNull IntPredicate predicate){
        lock.lock();
        final IntSupplier parentGenerator = this.generator;
        Objects.requireNonNull(predicate);
        IntSupplier generator = new IntSupplier() {
            int lastValidValue = 0;
            @Override
            public int getAsInt() {
                int newValue = parentGenerator.getAsInt();
                if (predicate.test(newValue))
                    lastValidValue = newValue;
                return lastValidValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public SignalIntPipe reduce(@NotNull IntBinaryOperator operator){
        lock.lock();
        final IntSupplier parentGenerator = this.generator;
        Objects.requireNonNull(operator);
        IntSupplier generator = new IntSupplier() {
            int lastValue = 0;
            @Override
            public int getAsInt() {
                lastValue = operator.applyAsInt(lastValue, parentGenerator.getAsInt());
                return lastValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public SignalIntPipe range(int minInclusive, int maxInclusive){
        lock.lock();
        final IntSupplier parentGenerator = this.generator;
        IntSupplier generator = new IntSupplier() {
            int lastValidValue = 0;
            @Override
            public int getAsInt() {
                int returnValue = parentGenerator.getAsInt();
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

    public SignalIntPipe map(@NotNull IntUnaryOperator operator){
        lock.lock();
        Objects.requireNonNull(operator);
        final IntSupplier parentGenerator = this.generator;
        IntSupplier supplier = () -> operator.applyAsInt(parentGenerator.getAsInt());
        var result = pipe(supplier);
        lock.unlock();
        return result;
    }

    @Contract("_ -> new")
    public <Type> @NotNull SignalPipe<Type> mapToObject(@NotNull IntFunction<Type> function){
        lock.lock();
        Objects.requireNonNull(function);
        final IntSupplier parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.getAsInt());
        var result = new SignalPipe<>(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract("_ -> new")
    public <Type extends Number> @NotNull SignalNumberPipe<Type> mapToNumber(@NotNull IntFunction<Type> function){
        lock.lock();
        Objects.requireNonNull(function);
        final IntSupplier parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.getAsInt());
        var result = new SignalNumberPipe<>(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull SignalDoublePipe mapToDouble(){
        lock.lock();
        final IntSupplier parentGenerator = this.generator;
        var result = new SignalDoublePipe(() -> (double) parentGenerator.getAsInt(), parents, isShared);
        lock.unlock();
        return result;
    }

    public SignalIntPipe mapToInt(){
        return this;
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull SignalLongPipe mapToLong(){
        lock.lock();
        final IntSupplier parentGenerator = this.generator;
        var result = new SignalLongPipe(() -> (long) parentGenerator.getAsInt(), parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract(" -> new")
    public @NotNull SignalNumberPipe<Integer> boxed() {
        lock.lock();
        var result = mapToNumber(it -> it);
        lock.unlock();
        return result;
    }

    @Contract(" -> new")
    public Signal.@NotNull Number<Integer> build(){
        lock.lock();
        var result = new DependingIntSignal(generator, parents.stream());
        lock.unlock();
        return result;
    }

    @Contract(" -> new")
    public Signal.@NotNull Number<Double> buildDouble(){
        lock.lock();
        final IntSupplier parentGenerator = this.generator;
        DoubleSupplier generator = () -> (double) parentGenerator.getAsInt();
        var result = new DependingDoubleSignal(generator, parents.stream());
        lock.unlock();
        return result;
    }

    @Contract(" -> new")
    public Signal.@NotNull Number<Long> buildLong(){
        lock.lock();
        final IntSupplier parentGenerator = this.generator;
        LongSupplier generator = () -> (long) parentGenerator.getAsInt();
        var result = new DependingLongSignal(generator, parents.stream());
        lock.unlock();
        return result;
    }

    public SignalIntPipe shared() {
        lock.lock();
        var result = isShared ? this : new SignalIntPipe(generator, parents, true);
        lock.unlock();
        return result;
    }

    public SignalIntPipe exclusive() {
        lock.lock();
        var result = isShared ? new SignalIntPipe(generator, parents, false) : this;
        lock.unlock();
        return result;
    }


}
