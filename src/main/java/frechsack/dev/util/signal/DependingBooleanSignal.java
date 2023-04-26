package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public final class DependingBooleanSignal extends DependingSignal<Boolean> implements Signal.Boolean {

    private boolean value;
    private final @NotNull BooleanSupplier generator;

    public DependingBooleanSignal(@NotNull BooleanSupplier generator, @NotNull @UnmodifiableView Collection<Signal<?>> parents) {
        super(parents);
        this.generator = generator;
    }

    @Override
    protected synchronized void revalidate() {
        if (isValid) return;
        boolean lastValue = value;
        value = generator.getAsBoolean();
        isValid = true;
        if (value != lastValue)
            fireChange(value);
    }

    @Override
    public java.lang.Boolean get() {
        return getAsBoolean();
    }

    @Override
    public synchronized boolean getAsBoolean() {
        if (!isValid)
            revalidate();
        return value;
    }
}
