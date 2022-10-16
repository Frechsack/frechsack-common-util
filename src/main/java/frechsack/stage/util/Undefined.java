package frechsack.stage.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface Undefined<Type> extends Iterable<Type> {

    @SuppressWarnings("unchecked")
    static <Out> Undefined<Out> nullable(){
     return (Undefined<Out>) UndefinedFactory.NULL;
    }

    @SuppressWarnings("unchecked")
    static <Out> Undefined<Out> undefined(){
        return (Undefined<Out>) UndefinedFactory.UNDEFINED;
    }

    static <Out> Undefined<Out> of(){
        return Undefined.undefined();
    }

    static <Out> Undefined<Out> of(Out value){
        return value == null
                ? Undefined.nullable()
                : new UndefinedFactory.Present<>(value);
    }

    Type get();

    boolean isEmpty();

    boolean isNull();

    boolean isUndefined();

    default boolean isPresent(){
        return !isEmpty();
    }

    default <Out> Undefined<Out> map(Function<Type, Out> mapper){
        if (isUndefined()) return Undefined.undefined();
        if (isNull()) return Undefined.nullable();
        return Undefined.of(mapper.apply(get()));
    }

    default <Out> Undefined<Out> flatMap(Function<Type, Undefined<Out>> mapper){
        if (isUndefined()) return Undefined.undefined();
        if (isNull()) return Undefined.nullable();
        return Objects.requireNonNull(mapper.apply(get()));
    }

    default Type orElse(Type value){
        return isPresent()
                ? get()
                : value;
    }

    default Type orElseGet(Supplier<Type> supplier) {
        return isPresent()
                ? get()
                : supplier.get();
    }

    default Type orElseThrow(){
        if(isPresent()) return get();
        throw new NoSuchElementException("No value present");
    }

    default <ExceptionType extends Throwable> Type orElseThrow(Supplier<? extends ExceptionType> exceptionSupplier) throws ExceptionType {
        if(isPresent()) return get();
        throw exceptionSupplier.get();
    }

    default Undefined<Type> or(Supplier<? extends Undefined<Type>> supplier){
        return isPresent()
                ? this
                : supplier.get();
    }

    default Stream<Type> stream(){
        return isPresent()
                ? Stream.of(get())
                : Stream.empty();
    }

    default IntStream intStream(){
        if (isEmpty())
            return IntStream.empty();
        Object value = get();
        if(value instanceof Number)
            return IntStream.of(((Number) value).intValue());

        throw new IllegalStateException("Value is not a number");
    }

    default DoubleStream doubleStream(){
        if (isEmpty())
            return DoubleStream.empty();
        Object value = get();
        if(value instanceof Number)
            return DoubleStream.of(((Number) value).doubleValue());

        throw new IllegalStateException("Value is not a number");
    }

    default LongStream longStream(){
        if (isEmpty())
            return LongStream.empty();
        Object value = get();
        if(value instanceof Number)
            return LongStream.of(((Number) value).longValue());

        throw new IllegalStateException("Value is not a number");
    }

    default Undefined<Type> filter(Predicate<? super Type> filter){
        if (isEmpty()) return this;
        return filter.test(get())
                ? this
                : Undefined.undefined();
    }

    default Optional<Optional<Type>> toOptional(){
        if(isUndefined()) return Optional.empty();
        if(isNull()) return Optional.of(Optional.empty());
        return Optional.of(Optional.of(get()));
    }

    default void ifPresent(Consumer<? super Type> consumer){
        if(isPresent())
            consumer.accept(get());
    }

    default void ifPresentOrElse(Consumer<? super Type> consumer, Runnable empty){
        if(isPresent())
            consumer.accept(get());
        else
            empty.run();
    }

    default void ifUndefined(Runnable action){
        if (isUndefined())
            action.run();
    }

    default void ifNull(Runnable action){
        if(isNull())
            action.run();
    }

    default Undefined<Type> ifNullMap(Supplier<Undefined<Type>> supplier){
        return isNull()
                ? Objects.requireNonNull(supplier.get())
                : this;
    }

    default Undefined<Type> ifUndefinedMap(Supplier<Undefined<Type>> supplier){
        return isUndefined()
                ? Objects.requireNonNull(supplier.get())
                : this;
    }

    default Undefined<Type> ifPresentMap(Function<Type,Undefined<Type>> mapper){
        return isPresent()
                ? Objects.requireNonNull(mapper.apply(get()))
                : this;
    }

    default Undefined<Type> ifEmptyMap(Supplier<Undefined<Type>> supplier){
        return isEmpty()
                ? Objects.requireNonNull(supplier.get())
                : this;
    }

    default <Out> Undefined<Out> transform(Function<Undefined<Type>,Undefined<Out>> mapper){
        return Objects.requireNonNull(mapper.apply(this));
    }

    @NotNull
    @Override
    Iterator<Type> iterator();
}
