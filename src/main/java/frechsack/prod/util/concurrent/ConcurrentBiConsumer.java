package frechsack.prod.util.concurrent;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;

public class ConcurrentBiConsumer<A, B> implements BiConsumer<A, B> {

    private final ConcurrentFunction<Tuple<A, B>, Void> impl;

    public ConcurrentBiConsumer(@NotNull BiConsumer<A, B> consumer) {
        Objects.requireNonNull(consumer);
        impl = new ConcurrentFunction<>(in -> {
            consumer.accept(in.first(), in.second());
            return null;
        });
    }

    @Override
    public void accept(A a, B b) {
        impl.apply(Tuple.of(a, b));
    }
}
