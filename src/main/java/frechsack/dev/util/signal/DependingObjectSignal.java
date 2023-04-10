package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

sealed class DependingObjectSignal<Type> extends DependingSignal<Type> permits DependingNumberSignal{

    private Type value;

    private final @NotNull Supplier<Type> generator;

    public DependingObjectSignal(@NotNull Supplier<Type> generator, @NotNull Stream<Signal<?>> parents) {
        super(parents);
        this.generator = generator;
    }

    @Override
    protected synchronized void revalidate() {
        if (isValid) return;
        Type lastValue = value;
        value = generator.get();
        isValid = true;
        if (!Objects.equals(lastValue, value))
            fireChange(value);
    }

    @Override
    public synchronized Type get() {
        if (!isValid)
            revalidate();
        return value;
    }
}
