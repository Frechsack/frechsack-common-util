package frechsack.prod.util.concurrent;

import frechsack.prod.util.Tuple;

import java.util.Objects;
import java.util.function.BiFunction;

public class ConcurrentBiFunction<InputA, InputB, Output> implements BiFunction<InputA, InputB, Output> {

    private final ConcurrentFunction<Tuple<InputA, InputB>, Output> impl;

    public ConcurrentBiFunction(BiFunction<InputA, InputB, Output> function) {
        Objects.requireNonNull(function);
        this.impl = new ConcurrentFunction<>(args -> function.apply(args.first(), args.second()));
    }

    @Override
    public Output apply(InputA inputA, InputB inputB) {
        return impl.apply(Tuple.of(inputA, inputB));
    }
}
