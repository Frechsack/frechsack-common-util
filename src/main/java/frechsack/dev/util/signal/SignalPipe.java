package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

public sealed class SignalPipe<Type> permits SignalNumberPipe {

    public static final boolean DEFAULT_SHARED = false;

    protected @NotNull Supplier<Type> generator;
    protected final @NotNull @UnmodifiableView Collection<Signal<?>> parents;
    protected final boolean isShared;

    protected final ReentrantLock lock = new ReentrantLock();

    public SignalPipe(@NotNull Supplier<Type> generator, @NotNull @UnmodifiableView Collection<Signal<?>> parents, boolean isShared) {
        this.generator = generator;
        this.parents = parents;
        this.isShared = isShared;
    }

    private SignalPipe<Type> pipe(@NotNull Supplier<Type> supplier){
        if (isShared)
            return new SignalPipe<>(supplier, parents, true);
        this.generator = supplier;
        return this;
    }

    public SignalPipe<Type> peek(@NotNull Consumer<Type> consumer){
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

    public SignalPipe<Type> filter(@NotNull Predicate<Type> predicate){
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        Objects.requireNonNull(predicate);
        Supplier<Type> generator = new Supplier<>() {
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

    public SignalPipe<Type> reduce(BinaryOperator<Type> operator, Type identity){
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        Objects.requireNonNull(operator);
        Supplier<Type> generator = new Supplier<>() {
            Type lastValue = null;
            boolean isFirstIteration = true;

            @Override
            public Type get() {
                if (isFirstIteration) {
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

    public SignalPipe<Type> reduce(BinaryOperator<Type> operator, Supplier<Type> identity){
        lock.lock();
        final Supplier<Type> parentGenerator = this.generator;
        Objects.requireNonNull(operator);
        Supplier<Type> generator = new Supplier<>() {
            Type lastValue = null;
            boolean isFirstIteration = true;

            @Override
            public Type get() {
                if (isFirstIteration) {
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

    public <Out> SignalPipe<Out> map(@NotNull Function<Type, Out> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        Supplier<Out> generator = () -> function.apply(parentGenerator.get());
        SignalPipe<Out> result = new SignalPipe<>(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    public <Out extends Number> SignalNumberPipe<Out> mapToNumber(@NotNull Function<Type, Out> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        Supplier<Out> generator = () -> function.apply(parentGenerator.get());
        SignalNumberPipe<Out> result = new SignalNumberPipe<>(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    public SignalIntPipe mapToInt(@NotNull ToIntFunction<Type> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        IntSupplier generator = () -> function.applyAsInt(parentGenerator.get());
        SignalIntPipe result = new SignalIntPipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe mapToDouble(@NotNull ToDoubleFunction<Type> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        DoubleSupplier generator = () -> function.applyAsDouble(parentGenerator.get());
        SignalDoublePipe result =  new SignalDoublePipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    public SignalLongPipe mapToLong(@NotNull ToLongFunction<Type> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        LongSupplier generator = () -> function.applyAsLong(parentGenerator.get());
        SignalLongPipe result = new SignalLongPipe(generator, parents, isShared);
        lock.unlock();
        return result;
    }

    public SignalPipe<Type> unary(@NotNull UnaryOperator<Type> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final Supplier<Type> parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.get());
        SignalPipe<Type> result = pipe(generator);
        lock.unlock();
        return result;
    }

    public Signal<Type> build() {
        lock.lock();
        Signal<Type> result = new DependingObjectSignal<>(generator, parents);
        lock.unlock();
        return result;
    }

    public SignalPipe<Type> shared() {
        lock.lock();
        SignalPipe<Type> result = isShared ? this : new SignalPipe<>(generator, parents, true);
        lock.unlock();
        return result;
    }

    public SignalPipe<Type> exclusive() {
        lock.lock();
        SignalPipe<Type> result = isShared ? new SignalPipe<>(generator, parents, false) : this;
        lock.unlock();
        return result;
    }
}
