package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

final class DependingIntSignal extends DependingSignal<Integer> implements Signal.Number<Integer> {

    private int value;
    private final @NotNull IntSupplier generator;
    public DependingIntSignal(@NotNull IntSupplier generator, @NotNull @UnmodifiableView Collection<Signal<?>> parents) {
        super(parents);
        this.generator = generator;
    }

    @Override
    protected synchronized void revalidate() {
        if (isValid) return;
        int lastValue = value;
        value = generator.getAsInt();
        isValid = true;
        if (value != lastValue)
            fireChange(value);
    }

    @Override
    public double getAsDouble() {
        return getAsInt();
    }

    @Override
    public synchronized int getAsInt() {
        if (!isValid)
            revalidate();
        return value;
    }

    @Override
    public long getAsLong() {
        return getAsInt();
    }

    @Override
    public synchronized Integer get() {
        return getAsInt();
    }
}
