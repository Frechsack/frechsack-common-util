package frechsack.prod.util.stream;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.DoubleStream;

public class FDoubleStream implements DoubleStream {

    private @NotNull DoubleStream internal;

    private FDoubleStream(@NotNull DoubleStream internal) {
        this.internal = internal;
    }

    public static @NotNull FDoubleStream of(@NotNull DoubleStream internal){
        return new FDoubleStream(internal);
    }

    private FDoubleStream apply(DoubleStream internal) {
        this.internal = internal;
        return this;
    }

    @Override
    public FDoubleStream filter(DoublePredicate predicate) {
        return apply(internal.filter(predicate));
    }

    @Override
    public FDoubleStream map(DoubleUnaryOperator mapper) {
        return apply(internal.map(mapper));
    }

    @Override
    public <U> FStream<U> mapToObj(DoubleFunction<? extends U> mapper) {
        return FStream.of(internal.mapToObj(mapper));
    }

    @Override
    public FIntStream mapToInt(DoubleToIntFunction mapper) {
        return FIntStream.of(internal.mapToInt(mapper));
    }

    @Override
    public FLongStream mapToLong(DoubleToLongFunction mapper) {
        return FLongStream.of(internal.mapToLong(mapper));
    }

    @Override
    public FDoubleStream flatMap(DoubleFunction<? extends DoubleStream> mapper) {
        return apply(internal.flatMap(mapper));
    }

    @Override
    public FDoubleStream mapMulti(DoubleMapMultiConsumer mapper) {
        return apply(internal.mapMulti(mapper));
    }

    @Override
    public FDoubleStream distinct() {
        return apply(internal.distinct());
    }

    @Override
    public FDoubleStream sorted() {
        return apply(internal.sorted());
    }

    public FDoubleStream sortedReversed() {
        return apply(internal.boxed().sorted(Comparator.reverseOrder()).mapToDouble(Double::valueOf));
    }

    @Override
    public FDoubleStream peek(DoubleConsumer action) {
        return apply(internal.peek(action));
    }

    @Override
    public FDoubleStream limit(long maxSize) {
        return apply(internal.limit(maxSize));
    }

    @Override
    public FDoubleStream skip(long n) {
        return apply(internal.skip(n));
    }

    @Override
    public FDoubleStream takeWhile(DoublePredicate predicate) {
        return apply(internal.takeWhile(predicate));
    }

    @Override
    public FDoubleStream dropWhile(DoublePredicate predicate) {
        return apply(internal.dropWhile(predicate));
    }

    @Override
    public void forEach(DoubleConsumer action) {
        internal.forEach(action);
    }

    @Override
    public void forEachOrdered(DoubleConsumer action) {
        internal.forEachOrdered(action);
    }

    @Override
    public double[] toArray() {
        return internal.toArray();
    }

    @Override
    public double reduce(double identity, DoubleBinaryOperator op) {
        return internal.reduce(identity, op);
    }

    @Override
    public OptionalDouble reduce(DoubleBinaryOperator op) {
        return internal.reduce(op);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return internal.collect(supplier, accumulator, combiner);
    }

    @Override
    public double sum() {
        return internal.sum();
    }

    @Override
    public OptionalDouble min() {
        return internal.min();
    }

    @Override
    public OptionalDouble max() {
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
    public DoubleSummaryStatistics summaryStatistics() {
        return internal.summaryStatistics();
    }

    @Override
    public boolean anyMatch(DoublePredicate predicate) {
        return internal.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(DoublePredicate predicate) {
        return internal.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(DoublePredicate predicate) {
        return internal.noneMatch(predicate);
    }

    @Override
    public OptionalDouble findFirst() {
        return internal.findFirst();
    }

    @Override
    public OptionalDouble findAny() {
        return internal.findAny();
    }

    @Override
    public FStream<Double> boxed() {
        return FStream.of(internal.boxed());
    }

    @Override
    public FDoubleStream sequential() {
        return apply(internal.sequential());
    }

    @Override
    public FDoubleStream parallel() {
        return apply(internal.parallel());
    }

    @NotNull
    @Override
    public FDoubleStream unordered() {
        return apply(internal.unordered());
    }

    @NotNull
    @Override
    public FDoubleStream onClose(Runnable closeHandler) {
        return apply(internal.onClose(closeHandler));
    }

    @Override
    public void close() {
        internal.close();
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return internal.iterator();
    }

    @Override
    public Spliterator.OfDouble spliterator() {
        return internal.spliterator();
    }

    @Override
    public boolean isParallel() {
        return internal.isParallel();
    }
}
