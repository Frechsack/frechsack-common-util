package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public class ConcurrentPredicate<Test> implements Predicate<Test> {

    private final ConcurrentFunction<Test, Boolean> impl;

    public ConcurrentPredicate(@NotNull Predicate<Test> predicate){
        Objects.requireNonNull(predicate);
        impl = new ConcurrentFunction<>(predicate::test);
    }

    @Override
    public boolean test(Test test) {
        return impl.apply(test);
    }
}
