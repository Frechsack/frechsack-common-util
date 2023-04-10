package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.DoubleFunction;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

public final class WriteableNumberSignal<Type extends Number> extends WriteableObjectSignal<Type> implements WriteableSignal.Number<Type> {

    private final @NotNull DoubleFunction<Type> doubleFunction;

    private final @NotNull IntFunction<Type> intFunction;

    private final @NotNull LongFunction<Type> longFunction;

    public WriteableNumberSignal(@NotNull DoubleFunction<Type> doubleFunction, @NotNull IntFunction<Type> intFunction, @NotNull LongFunction<Type> longFunction, @Nullable Type initial) {
        this.doubleFunction = Objects.requireNonNull(doubleFunction);
        this.intFunction = Objects.requireNonNull(intFunction);
        this.longFunction = Objects.requireNonNull(longFunction);
        this.value = initial;
    }

    public WriteableNumberSignal(@NotNull DoubleFunction<Type> doubleFunction, @NotNull IntFunction<Type> intFunction, @NotNull LongFunction<Type> longFunction) {
        this(doubleFunction, intFunction, longFunction, null);
    }

    @Override
    public boolean setInt(int value) {
        return set(intFunction.apply(value));
    }

    @Override
    public boolean setDouble(double value) {
        return set(doubleFunction.apply(value));
    }

    @Override
    public boolean setLong(long value) {
        return set(longFunction.apply(value));
    }
}
