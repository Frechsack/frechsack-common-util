package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.stream.Stream;

final class DependingNumberSignal<Type extends Number> extends DependingObjectSignal<Type> implements Signal.Number<Type> {

    public DependingNumberSignal(@NotNull Supplier<Type> generator, @NotNull Stream<Signal<?>> parents) {
        super(generator, parents);
    }
}
