package frechsack.prod.util.stream;

import org.jetbrains.annotations.NotNull;

import java.util.function.*;
import java.util.stream.IntStream;

public class FBooleanStream extends FIntStream {

    private FBooleanStream(@NotNull IntStream internal) {
        super(internal.map(it -> it == 0 ? 0 : 1));
    }

    public static FBooleanStream of(@NotNull IntStream internal){
        return new FBooleanStream(internal);
    }

    public @NotNull FBooleanStream apply(@NotNull IntStream internal) {
        this.internal = internal;
        return this;
    }

    @Override
    public FBooleanStream filter(@NotNull IntPredicate predicate) {
        return apply(internal.filter(predicate));
    }

    public FBooleanStream filterEqualsTrue() {
        return apply(internal.filter(it -> it != 0));
    }

    public FBooleanStream filterEqualsFalse() {
        return apply(internal.filter(it -> it == 0));
    }

    @Override
    public FBooleanStream map(IntUnaryOperator mapper) {
        return apply(internal.map(mapper));
    }

    @Override
    public <U> FStream<U> mapToObj(IntFunction<? extends U> mapper) {
        return FStream.of(internal.mapToObj(mapper));
    }

    @Override
    public FBooleanStream flatMap(IntFunction<? extends IntStream> mapper) {
        return FBooleanStream.of(internal.flatMap(mapper));
    }

    @Override
    public FBooleanStream distinct() {
        return apply(internal.distinct());
    }

    @Override
    public FBooleanStream sorted() {
        return apply(internal.sorted());
    }

    @Override
    public FBooleanStream peek(IntConsumer action) {
        return apply(internal.peek(action));
    }

    @Override
    public FBooleanStream limit(long maxSize) {
        return apply(internal.limit(maxSize));
    }

    @Override
    public FBooleanStream skip(long n) {
        return apply(internal.skip(n));
    }

    @Override
    public <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return internal.collect(supplier, accumulator, combiner);
    }

    @Override
    public @NotNull FBooleanStream parallel() {
        return apply(internal.parallel());
    }

    @NotNull
    @Override
    public FBooleanStream unordered() {
        return apply(internal.unordered());
    }

    @NotNull
    @Override
    public FBooleanStream onClose(Runnable closeHandler) {
        return apply(internal.onClose(closeHandler));
    }

    public boolean[] toBooleanArray() {
        final var counter = collect(Counter::new, (counter12, value) -> {
            if (value == 0)
                counter12.falseCount++;
            else
                counter12.trueCount++;
        }, (counter1, counter2) -> {
            counter1.trueCount += counter2.trueCount;
            counter1.falseCount += counter2.falseCount;
        });
        final var values = new boolean[counter.falseCount + counter.trueCount];
        for (int i = 0; i < counter.trueCount; i++)
            values[i] = true;
        return values;
    }

    private static class Counter {
        private int trueCount = 0;
        private int falseCount = 0;
    }
}
