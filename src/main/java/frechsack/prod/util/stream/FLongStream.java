package frechsack.prod.util.stream;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

public class FLongStream implements LongStream {

    private @NotNull LongStream internal;

    private FLongStream(@NotNull LongStream internal) {
        this.internal = internal;
    }

    public static FLongStream of(@NotNull LongStream internal){
        return new FLongStream(internal);
    }

    private FLongStream apply(LongStream internal) {
        this.internal = internal;
        return this;
    }

    @Override
    public FLongStream filter(LongPredicate predicate) {
        return apply(internal.filter(predicate));
    }

    @Override
    public FLongStream map(LongUnaryOperator mapper) {
        return apply(internal.map(mapper));
    }

    @Override
    public <U> FStream<U> mapToObj(LongFunction<? extends U> mapper) {
        return FStream.of(internal.mapToObj(mapper));
    }

    @Override
    public FIntStream mapToInt(LongToIntFunction mapper) {
        return FIntStream.of(internal.mapToInt(mapper));
    }

    @Override
    public FDoubleStream mapToDouble(LongToDoubleFunction mapper) {
        return FDoubleStream.of(internal.mapToDouble(mapper));
    }

    @Override
    public FLongStream flatMap(LongFunction<? extends LongStream> mapper) {
        return apply(internal.flatMap(mapper));
    }

    @Override
    public FLongStream mapMulti(LongMapMultiConsumer mapper) {
        return apply(internal.mapMulti(mapper));
    }

    @Override
    public FLongStream distinct() {
        return apply(internal.distinct());
    }

    @Override
    public FLongStream sorted() {
        return apply(internal.sorted());
    }

    public FLongStream sortedReversed() {
        return apply(internal.boxed().sorted(Comparator.reverseOrder()).mapToLong(Long::valueOf));
    }

    @Override
    public FLongStream peek(LongConsumer action) {
        return apply(internal.peek(action));
    }

    @Override
    public FLongStream limit(long maxSize) {
        return apply(internal.limit(maxSize));
    }

    @Override
    public FLongStream skip(long n) {
        return apply(internal.skip(n));
    }

    @Override
    public FLongStream takeWhile(LongPredicate predicate) {
        return apply(internal.takeWhile(predicate));
    }

    @Override
    public FLongStream dropWhile(LongPredicate predicate) {
        return apply(internal.dropWhile(predicate));
    }

    @Override
    public void forEach(LongConsumer action) {
        internal.forEach(action);
    }

    @Override
    public void forEachOrdered(LongConsumer action) {
        internal.forEachOrdered(action);
    }

    @Override
    public long[] toArray() {
        return internal.toArray();
    }

    @Override
    public long reduce(long identity, LongBinaryOperator op) {
        return internal.reduce(identity, op);
    }

    @Override
    public OptionalLong reduce(LongBinaryOperator op) {
        return internal.reduce(op);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return internal.collect(supplier, accumulator, combiner);
    }

    @Override
    public long sum() {
        return internal.sum();
    }

    @Override
    public OptionalLong min() {
        return internal.min();
    }

    @Override
    public OptionalLong max() {
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
    public LongSummaryStatistics summaryStatistics() {
        return internal.summaryStatistics();
    }

    @Override
    public boolean anyMatch(LongPredicate predicate) {
        return internal.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(LongPredicate predicate) {
        return internal.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(LongPredicate predicate) {
        return internal.noneMatch(predicate);
    }

    @Override
    public OptionalLong findFirst() {
        return internal.findFirst();
    }

    @Override
    public OptionalLong findAny() {
        return internal.findAny();
    }

    @Override
    public DoubleStream asDoubleStream() {
        return internal.asDoubleStream();
    }

    @Override
    public FStream<Long> boxed() {
        return FStream.of(internal.boxed());
    }

    @Override
    public FLongStream sequential() {
        return apply(internal.sequential());
    }

    @Override
    public FLongStream parallel() {
        return apply(internal.parallel());
    }

    @NotNull
    @Override
    public FLongStream unordered() {
        return apply(internal.unordered());
    }

    @NotNull
    @Override
    public FLongStream onClose(Runnable closeHandler) {
        return apply(internal.onClose(closeHandler));
    }

    @Override
    public void close() {
        internal.close();
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return internal.iterator();
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return internal.spliterator();
    }

    @Override
    public boolean isParallel() {
        return internal.isParallel();
    }
}
