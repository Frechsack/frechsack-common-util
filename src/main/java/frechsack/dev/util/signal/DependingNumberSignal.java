package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Stream;

final class DependingNumberSignal<Type extends Number> extends DependingObjectSignal<Type> implements Signal.Number<Type> {

    public DependingNumberSignal(@NotNull Supplier<Type> generator, @NotNull @UnmodifiableView Collection<Signal<?>> parents, @Nullable Executor executor) {
        super(generator, parents, executor);
    }
}
