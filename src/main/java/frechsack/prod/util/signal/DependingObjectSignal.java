package frechsack.prod.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

sealed class DependingObjectSignal<Type> extends DependingSignal<Type> permits DependingNumberSignal{

    private Type value;

    private final @NotNull Supplier<Type> generator;

    public DependingObjectSignal(@NotNull Supplier<Type> generator, @NotNull @UnmodifiableView Collection<Signal<?>> parents, @Nullable Executor executor) {
        super(parents, executor);
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
