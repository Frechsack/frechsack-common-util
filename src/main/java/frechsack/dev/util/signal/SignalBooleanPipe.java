package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

public class SignalBooleanPipe {

    public static final boolean DEFAULT_SHARED = false;

    private @NotNull BooleanSupplier generator;
    private final @NotNull Collection<Signal<?>> parents;
    private final boolean isShared;

    private final @Nullable Executor executor;
    private final ReentrantLock lock = new ReentrantLock();

    public SignalBooleanPipe(@NotNull BooleanSupplier generator, @NotNull Collection<Signal<?>> parents, boolean isShared, @Nullable Executor executor) {
        this.generator = generator;
        this.parents = parents;
        this.isShared = isShared;
        this.executor = executor;
    }

    private SignalBooleanPipe pipe(@NotNull BooleanSupplier supplier){
        if (isShared)
            return new SignalBooleanPipe(supplier, parents, true, executor);
        this.generator = supplier;
        return this;
    }

    public SignalBooleanPipe map(@NotNull UnaryOperator<Boolean> operator){
        lock.lock();
        Objects.requireNonNull(operator);
        final BooleanSupplier parentGenerator = this.generator;
        BooleanSupplier generator = () -> {
            Boolean value = operator.apply(parentGenerator.getAsBoolean());
            return value != null && value;
        };
        var result = pipe(generator);
        lock.unlock();
        return result;
    }

    public SignalBooleanPipe peek(@NotNull Consumer<Boolean> consumer){
        lock.lock();
        final BooleanSupplier parentGenerator = this.generator;
        Objects.requireNonNull(consumer);
        var result = pipe(() -> {
            boolean value = parentGenerator.getAsBoolean();
            consumer.accept(value);
            return value;
        });
        lock.unlock();
        return result;
    }

    public <Type> SignalPipe<Type> mapToObject(Function<Boolean, Type> function){
        lock.lock();
        Objects.requireNonNull(function);
        final BooleanSupplier parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.getAsBoolean());
        var result = new SignalPipe<>(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public <Type extends Number> SignalNumberPipe<Type> mapToNumber(Function<Boolean, Type> function){
        lock.lock();
        Objects.requireNonNull(function);
        final BooleanSupplier parentGenerator = this.generator;
        Supplier<Type> generator = () -> function.apply(parentGenerator.getAsBoolean());
        var result = new SignalNumberPipe<>(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalIntPipe mapToInt(@NotNull ToIntFunction<Boolean> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final BooleanSupplier parentGenerator = this.generator;
        IntSupplier generator = () -> function.applyAsInt(parentGenerator.getAsBoolean());
        var result = new SignalIntPipe(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalDoublePipe mapToDouble(@NotNull ToDoubleFunction<Boolean> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final BooleanSupplier parentGenerator = this.generator;
        DoubleSupplier generator = () -> function.applyAsDouble(parentGenerator.getAsBoolean());
        var result = new SignalDoublePipe(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public SignalLongPipe mapToLong(@NotNull ToLongFunction<Boolean> function) {
        lock.lock();
        Objects.requireNonNull(function);
        final BooleanSupplier parentGenerator = this.generator;
        LongSupplier generator = () -> function.applyAsLong(parentGenerator.getAsBoolean());
        var result = new SignalLongPipe(generator, parents, isShared, executor);
        lock.unlock();
        return result;
    }

    public DependingBooleanSignal build(){
        lock.lock();
        var result = new DependingBooleanSignal(generator, parents, executor);
        lock.unlock();
        return result;
    }

    public SignalBooleanPipe shared() {
        lock.lock();
        var result = isShared ? this : new SignalBooleanPipe(generator, parents, true, executor);
        lock.unlock();
        return result;
    }

    public SignalBooleanPipe exclusive() {
        lock.lock();
        var result = isShared ? new SignalBooleanPipe(generator, parents, false, executor) : this;
        lock.unlock();
        return result;
    }
}
