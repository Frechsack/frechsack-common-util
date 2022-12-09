package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class ConcurrentConsumer<Input> implements Consumer<Input> {

    private final ConcurrentFunction<Input, Void> impl;

    public ConcurrentConsumer(@NotNull Consumer<Input> consumer){
        Objects.requireNonNull(consumer);
        impl = new ConcurrentFunction<>(in -> {
            consumer.accept(in);
            return null;
        });
    }


    @Override
    public void accept(Input input) {
        impl.apply(input);
    }
}
