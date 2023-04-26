package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.function.LongSupplier;
import java.util.stream.Stream;

final class DependingLongSignal extends DependingSignal<Long> implements Signal.Number<Long> {

    private long value;
    private final @NotNull LongSupplier generator;
    public DependingLongSignal(@NotNull LongSupplier generator, @NotNull @UnmodifiableView Collection<Signal<?>> parents) {
        super(parents);
        this.generator = generator;
    }

    @Override
    protected synchronized void revalidate() {
        if (isValid) return;
        long lastValue = value;
        value = generator.getAsLong();
        isValid = true;
        if (value != lastValue)
            fireChange(value);
    }

    @Override
    public double getAsDouble() {
        return (int) getAsLong();
    }

    @Override
    public int getAsInt() {
        return (int) getAsLong();
    }

    @Override
    public synchronized long getAsLong() {
        if (!isValid)
            revalidate();
        return value;
    }

    @Override
    public Long get() {
        return getAsLong();
    }
}
