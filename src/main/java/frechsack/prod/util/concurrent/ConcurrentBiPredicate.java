package frechsack.prod.util.concurrent;

import frechsack.prod.util.Tuple;

import java.util.Objects;
import java.util.function.BiPredicate;

public class ConcurrentBiPredicate<A, B> implements BiPredicate<A, B> {

    private final ConcurrentFunction<Tuple<A, B>, Boolean> impl;


    public ConcurrentBiPredicate(BiPredicate<A, B> predicate){
        Objects.requireNonNull(predicate);
        impl = new ConcurrentFunction<>(in -> predicate.test(in.first(), in.second()));
    }

    @Override
    public boolean test(A a, B b) {
        return impl.apply(Tuple.of(a, b));
    }
}
