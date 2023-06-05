package frechsack.prod.util.stream;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.*;

public class FStream<Type> implements Stream<Type> {

    private @NotNull Stream<Type> internal;

    private FStream(@NotNull Stream<Type> internal) {
        this.internal = internal;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <Type> @NotNull FStream<Type> of(@NotNull Stream<Type> internal){
        return new FStream<>(internal);
    }

    private FStream<Type> apply(Stream<Type> internal) {
        this.internal = internal;
        return this;
    }

    @Override
    public <R> FStream<R> map(Function<? super Type, ? extends R> mapper) {
        return FStream.of(internal.map(mapper));
    }

    @Override
    public FStream<Type> filter(Predicate<? super Type> predicate) {
        return apply(internal.filter(predicate));
    }

    public FStream<Type> distinctBy(Function<Type, ?> extractor){
        final Set<Object> passedElements = Collections.synchronizedSet(new HashSet<>());
        return apply(internal.filter(element -> passedElements.add(extractor.apply(element))).onClose(passedElements::clear));
    }

    public FStream<Type> distinct(BiPredicate<Type, Type> comparator){
        final ConcurrentHashMap<Type, Object> passedElements = new ConcurrentHashMap<>();
        final var keySet = passedElements.keySet();
        final Object dummy = new Object();
        return apply(internal.filter(element -> {
            for (Type passedElement : keySet)
                if(!comparator.test(passedElement, element))
                    return false;
            return passedElements.put(element, dummy) == null;
        }).onClose(passedElements::clear));
    }

    public FStream<Type> sortedBy(Function<? super Type, ?> extractor){
        return apply(internal.sorted(StreamUtils.sortedBy(extractor)));
    }

    public FStream<Type> add(Stream<? extends Type> elements){
        return apply(internal.mapMulti(StreamUtils.mapMultiAdd(elements)));
    }

    @Override
    public FIntStream mapToInt(ToIntFunction<? super Type> mapper) {
        return FIntStream.of(internal.mapToInt(mapper));
    }

    @Override
    public FLongStream mapToLong(ToLongFunction<? super Type> mapper) {
        return FLongStream.of(internal.mapToLong(mapper));
    }

    @Override
    public FDoubleStream mapToDouble(ToDoubleFunction<? super Type> mapper) {
        return FDoubleStream.of(internal.mapToDouble(mapper));
    }

    @Override
    public <R> FStream<R> flatMap(Function<? super Type, ? extends Stream<? extends R>> mapper) {
        return FStream.of(internal.flatMap(mapper));
    }

    @Override
    public FIntStream flatMapToInt(Function<? super Type, ? extends IntStream> mapper) {
        return FIntStream.of(internal.flatMapToInt(mapper));
    }

    @Override
    public FLongStream flatMapToLong(Function<? super Type, ? extends LongStream> mapper) {
        return FLongStream.of(internal.flatMapToLong(mapper));
    }

    @Override
    public FDoubleStream flatMapToDouble(Function<? super Type, ? extends DoubleStream> mapper) {
        return FDoubleStream.of(internal.flatMapToDouble(mapper));
    }

    @Override
    public <R> FStream<R> mapMulti(BiConsumer<? super Type, ? super Consumer<R>> mapper) {
        return FStream.of(internal.mapMulti(mapper));
    }

    @Override
    public FIntStream mapMultiToInt(BiConsumer<? super Type, ? super IntConsumer> mapper) {
        return FIntStream.of(internal.mapMultiToInt(mapper));
    }

    @Override
    public FLongStream mapMultiToLong(BiConsumer<? super Type, ? super LongConsumer> mapper) {
        return FLongStream.of(internal.mapMultiToLong(mapper));
    }

    @Override
    public FDoubleStream mapMultiToDouble(BiConsumer<? super Type, ? super DoubleConsumer> mapper) {
        return FDoubleStream.of(internal.mapMultiToDouble(mapper));
    }

    @Override
    public FStream<Type> distinct() {
        return apply(internal.distinct());
    }

    @Override
    public FStream<Type> sorted() {
        return apply(internal.sorted());
    }

    public FStream<Type> sortedReversed(){
        //noinspection unchecked
        return apply(internal.sorted((Comparator<? super Type>) Comparator.reverseOrder()));
    }

    @Override
    public FStream<Type> sorted(Comparator<? super Type> comparator) {
        return apply(internal.sorted(comparator));
    }

    public FStream<Type> sortedReversed(Comparator<? super Type> comparator){
        return apply(internal.sorted(comparator.reversed()));
    }

    @Override
    public FStream<Type> peek(Consumer<? super Type> action) {
        return apply(internal.peek(action));
    }

    @Override
    public FStream<Type> limit(long maxSize) {
        return apply(internal.limit(maxSize));
    }

    @Override
    public FStream<Type> skip(long n) {
        return apply(internal.skip(n));
    }

    @Override
    public FStream<Type> takeWhile(Predicate<? super Type> predicate) {
        return apply(internal.takeWhile(predicate));
    }

    @Override
    public FStream<Type> dropWhile(Predicate<? super Type> predicate) {
        return apply(internal.dropWhile(predicate));
    }

    @Override
    public void forEach(Consumer<? super Type> action) {
        internal.forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super Type> action) {
        internal.forEachOrdered(action);
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return internal.toArray();
    }

    @NotNull
    @Override
    public <A> A @NotNull [] toArray(IntFunction<A[]> generator) {
        return internal.toArray(generator);
    }

    @Override
    public Type reduce(Type identity, BinaryOperator<Type> accumulator) {
        return internal.reduce(identity, accumulator);
    }

    @NotNull
    @Override
    public Optional<Type> reduce(BinaryOperator<Type> accumulator) {
        return internal.reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super Type, U> accumulator, BinaryOperator<U> combiner) {
        return internal.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Type> accumulator, BiConsumer<R, R> combiner) {
        return internal.collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super Type, A, R> collector) {
        return internal.collect(collector);
    }

    @Override
    public List<Type> toList() {
        return internal.toList();
    }

    @NotNull
    @Override
    public Optional<Type> min(Comparator<? super Type> comparator) {
        return internal.min(comparator);
    }

    @NotNull
    @Override
    public Optional<Type> max(Comparator<? super Type> comparator) {
        return internal.max(comparator);
    }

    @Override
    public long count() {
        return internal.count();
    }

    @Override
    public boolean anyMatch(Predicate<? super Type> predicate) {
        return internal.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super Type> predicate) {
        return internal.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super Type> predicate) {
        return internal.noneMatch(predicate);
    }

    @NotNull
    @Override
    public Optional<Type> findFirst() {
        return internal.findFirst();
    }

    @NotNull
    @Override
    public Optional<Type> findAny() {
        return internal.findAny();
    }

    @NotNull
    @Override
    public Iterator<Type> iterator() {
        return internal.iterator();
    }

    @NotNull
    @Override
    public Spliterator<Type> spliterator() {
        return internal.spliterator();
    }

    @Override
    public boolean isParallel() {
        return internal.isParallel();
    }

    @NotNull
    @Override
    public FStream<Type> sequential() {
        return apply(internal.sequential());
    }

    @NotNull
    @Override
    public FStream<Type> parallel() {
        return apply(internal.parallel());
    }

    @NotNull
    @Override
    public FStream<Type> unordered() {
        return apply(internal.unordered());
    }

    @NotNull
    @Override
    public FStream<Type> onClose(Runnable closeHandler) {
        return apply(internal.onClose(closeHandler));
    }

    @Override
    public void close() {
        internal.close();
    }
}
