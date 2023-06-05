package frechsack.prod.util.stream;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;

public class FIntStream implements IntStream {

    private @NotNull IntStream internal;

    private FIntStream(@NotNull IntStream internal) {
        this.internal = internal;
    }

    public static FIntStream of(@NotNull IntStream internal){
        return new FIntStream(internal);
    }

    private FIntStream apply(IntStream internal) {
        this.internal = internal;
        return this;
    }

    @Override
    public FIntStream filter(IntPredicate predicate) {
        return apply(internal.filter(predicate));
    }

    @Override
    public FIntStream map(IntUnaryOperator mapper) {
        return apply(internal.map(mapper));
    }

    @Override
    public <U> FStream<U> mapToObj(IntFunction<? extends U> mapper) {
        return FStream.of(internal.mapToObj(mapper));
    }

    @Override
    public FLongStream mapToLong(IntToLongFunction mapper) {
        return FLongStream.of(internal.mapToLong(mapper));
    }

    @Override
    public FDoubleStream mapToDouble(IntToDoubleFunction mapper) {
        return FDoubleStream.of(internal.mapToDouble(mapper));
    }

    @Override
    public FIntStream flatMap(IntFunction<? extends IntStream> mapper) {
        return apply(internal.flatMap(mapper));
    }

    @Override
    public FIntStream mapMulti(IntMapMultiConsumer mapper) {
        return apply(internal.mapMulti(mapper));
    }

    @Override
    public FIntStream distinct() {
        return apply(internal.distinct());
    }

    @Override
    public FIntStream sorted() {
        return apply(internal.sorted());
    }

    public FIntStream sortedReversed() {
        return apply(internal.boxed().sorted(Comparator.reverseOrder()).mapToInt(Integer::valueOf));
    }

    @Override
    public FIntStream peek(IntConsumer action) {
        return apply(internal.peek(action));
    }

    @Override
    public FIntStream limit(long maxSize) {
        return apply(internal.limit(maxSize));
    }

    @Override
    public FIntStream skip(long n) {
        return apply(internal.skip(n));
    }

    @Override
    public FIntStream takeWhile(IntPredicate predicate) {
        return apply(internal.takeWhile(predicate));
    }

    @Override
    public FIntStream dropWhile(IntPredicate predicate) {
        return apply(internal.dropWhile(predicate));
    }

    @Override
    public void forEach(IntConsumer action) {
        internal.forEach(action);
    }

    @Override
    public void forEachOrdered(IntConsumer action) {
        internal.forEachOrdered(action);
    }

    @Override
    public int[] toArray() {
        return internal.toArray();
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        return internal.reduce(identity, op);
    }

    @Override
    public OptionalInt reduce(IntBinaryOperator op) {
        return internal.reduce(op);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return internal.collect(supplier, accumulator, combiner);
    }

    @Override
    public int sum() {
        return internal.sum();
    }

    @Override
    public OptionalInt min() {
        return internal.min();
    }

    @Override
    public OptionalInt max() {
        return internal.max();
    }

    @Override
    public long count() {
        return internal.count();
    }

    @Override
    public OptionalDouble average() {
        return internal.average();
    }

    @Override
    public IntSummaryStatistics summaryStatistics() {
        return internal.summaryStatistics();
    }

    @Override
    public boolean anyMatch(IntPredicate predicate) {
        return internal.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(IntPredicate predicate) {
        return internal.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(IntPredicate predicate) {
        return internal.noneMatch(predicate);
    }

    @Override
    public OptionalInt findFirst() {
        return internal.findFirst();
    }

    @Override
    public OptionalInt findAny() {
        return internal.findAny();
    }

    @Override
    public FLongStream asLongStream() {
        return FLongStream.of(internal.asLongStream());
    }

    @Override
    public FDoubleStream asDoubleStream() {
        return FDoubleStream.of(internal.asDoubleStream());
    }

    @Override
    public FStream<Integer> boxed() {
        return FStream.of(internal.boxed());
    }

    @Override
    public FIntStream sequential() {
        return apply(internal.sequential());
    }

    @Override
    public FIntStream parallel() {
        return apply(internal.parallel());
    }

    @NotNull
    @Override
    public FIntStream unordered() {
        return apply(internal.unordered());
    }

    @NotNull
    @Override
    public FIntStream onClose(Runnable closeHandler) {
        return apply(internal.onClose(closeHandler));
    }

    @Override
    public void close() {
        internal.close();
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return internal.iterator();
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return internal.spliterator();
    }

    @Override
    public boolean isParallel() {
        return internal.isParallel();
    }
}
