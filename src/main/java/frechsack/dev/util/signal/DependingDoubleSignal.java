package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.function.DoubleSupplier;
import java.util.stream.Stream;

final class DependingDoubleSignal extends DependingSignal<Double> implements Signal.Number<Double>{

    private double value;
    private final @NotNull DoubleSupplier generator;

    public DependingDoubleSignal(@NotNull DoubleSupplier generator, @NotNull @UnmodifiableView Collection<Signal<?>> parents, @Nullable Executor executor) {
        super(parents, executor);
        this.generator = generator;
    }

    @Override
    protected synchronized void revalidate() {
        if (isValid) return;
        double lastValue = value;
        value = generator.getAsDouble();
        isValid = true;
        if (value != lastValue)
            fireChange(value);
    }

    @Override
    public synchronized double getAsDouble() {
        if (!isValid)
            revalidate();
        return value;
    }

    @Override
    public int getAsInt() {
        return (int) getAsDouble();
    }

    @Override
    public long getAsLong() {
        return (int) getAsDouble();
    }

    @Override
    public Double get() {
        return getAsDouble();
    }
}
