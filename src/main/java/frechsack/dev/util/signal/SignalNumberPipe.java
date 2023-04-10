package frechsack.dev.util.signal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Objects;
import java.util.function.*;

public final class SignalNumberPipe<Type extends Number> extends SignalPipe<Type> {

    public SignalNumberPipe(@NotNull Supplier<Type> generator, @NotNull @UnmodifiableView Collection<Signal<?>> parents, boolean isShared) {
        super(generator, parents, isShared);
    }

    private SignalNumberPipe<Type> pipe(@NotNull Supplier<Type> supplier){
        if (isShared)
            return new SignalNumberPipe<>(supplier, parents, true);
        this.generator = supplier;
        return this;
    }

    @Override
    public SignalNumberPipe<Type> peek(@NotNull Consumer<Type> consumer) {
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        Objects.requireNonNull(consumer);
        var result = pipe(() -> {
            Type value = parentGenerator.get();
            consumer.accept(value);
            return value;
        });
        lock.unlock();
        return result;
    }

    @Override
    public SignalNumberPipe<Type> filter(@NotNull Predicate<Type> predicate){
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        Objects.requireNonNull(predicate);
        Supplier<Type> generator = new Supplier<Type>() {
            Type lastValidValue = null;
            @Override
            public Type get() {
                Type newValue = parentGenerator.get();
                if (predicate.test(newValue))
                    lastValidValue = newValue;
                return lastValidValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    @Override
    public SignalPipe<Type> reduce(BinaryOperator<Type> operator, Type identity) {
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        Objects.requireNonNull(operator);
        Supplier<Type> generator = new Supplier<>() {
            Type lastValue = null;
            boolean isFirstIteration = true;
            @Override
            public Type get() {
                if (isFirstIteration){
                    lastValue = identity;
                    isFirstIteration = false;
                }
                lastValue = operator.apply(lastValue, parentGenerator.get());
                return lastValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    @Override
    public SignalNumberPipe<Type> reduce(BinaryOperator<Type> operator, Supplier<Type> identity){
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        Objects.requireNonNull(operator);
        Supplier<Type> generator = new Supplier<>() {
            Type lastValue = null;

            boolean isFirstIteration = true;

            @Override
            public Type get() {
                if (isFirstIteration){
                    lastValue = identity.get();
                    isFirstIteration = false;
                }
                lastValue = operator.apply(lastValue, parentGenerator.get());
                return lastValue;
            }
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull SignalDoublePipe mapToDouble() {
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        DoubleSupplier generator = () -> {
          Number value = parentGenerator.get();
          return value == null ? 0 : value.doubleValue();
        };
        SignalDoublePipe result = new SignalDoublePipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull SignalIntPipe mapToInt() {
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        IntSupplier generator = () -> {
            Number value = parentGenerator.get();
            return value == null ? 0 : value.intValue();
        };

        SignalIntPipe result = new SignalIntPipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull SignalLongPipe mapToLong() {
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        LongSupplier generator = () -> {
            Number value = parentGenerator.get();
            return value == null ? 0 : value.longValue();
        };
        SignalLongPipe result = new SignalLongPipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract(value = "_ -> new", pure = true)
    @Override
    public <Out> @NotNull SignalPipe<Out> map(@NotNull Function<Type, Out> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        Supplier<Out> generator = () -> function.apply(parentGenerator.get());
        SignalPipe<Out> result = new SignalPipe<>(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract("_ -> new")
    @Override
    public <Out extends Number> @NotNull SignalNumberPipe<Out> mapToNumber(@NotNull Function<Type, Out> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        Supplier<Out> generator = () -> function.apply(parentGenerator.get());
        SignalNumberPipe<Out> result = new SignalNumberPipe<>(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract(value = "_ -> new", pure = true)
    @Override
    public @NotNull SignalIntPipe mapToInt(@NotNull ToIntFunction<Type> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        IntSupplier generator = () -> function.applyAsInt(parentGenerator.get());
        SignalIntPipe result = new SignalIntPipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract(value = "_ -> new", pure = true)
    @Override
    public @NotNull SignalDoublePipe mapToDouble(@NotNull ToDoubleFunction<Type> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        DoubleSupplier generator = () -> function.applyAsDouble(parentGenerator.get());
        SignalDoublePipe result = new SignalDoublePipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Contract(value = "_ -> new", pure = true)
    @Override
    public @NotNull SignalLongPipe mapToLong(@NotNull ToLongFunction<Type> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        LongSupplier generator = () -> function.applyAsLong(parentGenerator.get());
        SignalLongPipe result = new SignalLongPipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    @Override
    public SignalNumberPipe<Type> unary(@NotNull UnaryOperator<Type> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.get());
        SignalNumberPipe<Type> result = pipe(generator);
        lock.unlock();
        return result;
    }

    @Contract(" -> new")
    @Override
    public Signal.@NotNull Number<Type> build() {
        lock.lock();
        Signal.Number<Type> result = new DependingNumberSignal<>(generator, parents.stream());
        lock.unlock();
        return result;
    }

    @Contract(" -> new")
    public Signal.@NotNull Number<Integer> buildInt() {
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        IntSupplier generator = () -> {
            Number value = parentGenerator.get();
            return value == null ? 0 : value.intValue();
        };

        Signal.Number<Integer> result = new DependingIntSignal(generator, parents.stream());
        lock.unlock();
        return result;
    }

    @Contract(" -> new")
    public Signal.@NotNull Number<Double> buildDouble() {
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        DoubleSupplier generator = () -> {
            Number value = parentGenerator.get();
            return value == null ? 0 : value.doubleValue();
        };
        Signal.Number<Double> result = new DependingDoubleSignal(generator, parents.stream());
        lock.unlock();
        return result;
    }

    @Contract(" -> new")
    public Signal.@NotNull Number<Long> buildLong() {
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        LongSupplier generator = () -> {
            Number value = parentGenerator.get();
            return value == null ? 0 : value.longValue();
        };
        Signal.Number<Long> result = new DependingLongSignal(generator, parents.stream());
        lock.unlock();
        return result;
    }

    @Override
    public SignalNumberPipe<Type> shared() {
        lock.lock();
        SignalNumberPipe<Type> result = isShared ? this : new SignalNumberPipe<>(generator, parents, true);
        lock.unlock();
        return result;
    }

    @Override
    public SignalNumberPipe<Type> exclusive() {
        lock.lock();
        SignalNumberPipe<Type> result = isShared ? new SignalNumberPipe<>(generator, parents, false) : this;
        lock.unlock();
        return result;
    }

}
