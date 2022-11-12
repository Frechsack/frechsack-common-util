package frechsack.prod.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * An Undefined behaves like an {@link Optional}, that supports undefined, null and a present state.
 *
 * @param <Type>
 */
public interface Undefined<Type> extends Iterable<Type> {

    /**
     * Returns an Undefined, that contains a {@link null} value.
     * @return Returns an Undefined, that contains a {@link null} value.
     * @param <Out> The typed instance type.
     */
    @SuppressWarnings("unchecked")
    static <Out> Undefined<Out> nullable(){
     return (Undefined<Out>) UndefinedFactory.NULL;
    }

    /**
     * Returns an Undefined, that contains an undefined value.
     * @return Returns an Undefined, that contains an undefined value.
     * @param <Out> The typed instance type.
     */
    @SuppressWarnings("unchecked")
    static <Out> Undefined<Out> undefined(){
        return (Undefined<Out>) UndefinedFactory.UNDEFINED;
    }

    /**
     * Returns an Undefined, that contains an undefined value.
     * This function is analog to {@link #undefined()}.
     * This function exists convenience wise, to supply a non argument variation of {@link #of(Object)}.
     * @return Returns an Undefined, that contains an undefined value.
     * @param <Out> The typed instance type.
     */
    static <Out> Undefined<Out> of(){
        return undefined();
    }

    /**
     * Returns an Undefined, that may contain a {@link null} or non-null value.
     * If a null argument is supplied, then an Undefined with a null is returned, otherwise an Undefined with the supplier non-null value.
     * @param value The value inside the returned Undefined.
     * @return Returns an Undefined depending on the argument.
     * @param <Out> The typed instance type.
     */
    static <Out> Undefined<Out> of(Out value){
        return value == null
                ? Undefined.nullable()
                : new UndefinedFactory.Present<>(value);
    }

    /**
     * Returns the value inside this Undefined. This function will throw an error, if {@link #isPresent()} returns {@code false}.
     * @return Returns this Undefined value.
     */
    Type get();

    /**
     * Checks if this Undefined contains a value. In that case calling {@link  #get()} is safe and will return a non-null value.
     * @return Returns {@code true} if this Undefined contains an undefined or null value.
     */
    boolean isEmpty();

    /**
     * Checks if this Undefined contains a null value.
     * @return Returns {@code true} if this Undefined contains null value.
     */
    boolean isNull();

    /**
     * Checks if this Undefined contains an undefined value.
     * @return Returns {@code true} if this Undefined contains an undefined value.
     */
    boolean isUndefined();

    /**
     * Checks if this Undefined contains a value. Calling {@link #get()} is safe, when this function returns {@link true}.
     * @return Returns {@link true} if this Undefined contains a not undefined and non-null value.
     */
    default boolean isPresent(){
        return !isEmpty();
    }

    /**
     * Converts the value inside this Undefined into a different value.
     * The mapping is only performed, if this Undefined contains a present value. If this Undefined contains undefined or null, then this function has no effect.
     * @return Returns a new Undefined with the mapped value. The function may return {@link null}.
     * If this Undefined is empty, a null or undefined containing Undefined will be returned.
     * @param <Out> The typed instance type.
     */
    default <Out> Undefined<Out> map(Function<Type, Out> mapper){
        if (isUndefined()) return Undefined.undefined();
        if (isNull()) return Undefined.nullable();
        return Undefined.of(mapper.apply(get()));
    }

    /**
     * Converts the value inside this Undefined into a different value.
     * The mapping is only performed, if this Undefined contains a present value. If this Undefined contains undefined or null, then this function has no effect.
     * @param mapper The mapping function. The function is not allowed to return a null value.
     * @return Returns a new Undefined with the mapped value.  If this Undefined is empty, a null or undefined containing Undefined will be returned.
     * @param <Out> The typed instance type.
     */
    default <Out> Undefined<Out> flatMap(Function<Type, Undefined<Out>> mapper){
        if (isUndefined()) return Undefined.undefined();
        if (isNull()) return Undefined.nullable();
        return Objects.requireNonNull(mapper.apply(get()));
    }

    /**
     * Returns the current value of this Undefined, if this Undefined is empty, the argument is returned.
     * @param value The alternative value, if this Undefined is empty.
     * @return Returns this Undefined value or the argument.
     */
    default Type orElse(Type value){
        return isPresent()
                ? get()
                : value;
    }

    /**
     * Returns the current value of this Undefined, if this Undefined is empty, the value supplied by the argument is returned.
     * @param supplier The alternative value, if this Undefined is empty.
     * @return Returns this Undefined value or the value supplied by the argument.
     */
    default Type orElseGet(Supplier<Type> supplier) {
        return isPresent()
                ? get()
                : supplier.get();
    }

    /**
     * Returns this Undefined value or throws an exception if it is empty.
     * This function behaves like {@link #get()}.
     * @return Returns the value.
     */
    default Type orElseThrow(){
        if(isPresent()) return get();
        throw new NoSuchElementException("No value present");
    }

    /**
     * Returns this Undefined value or throws the exception, supplied by the argument, if it is empty.
     * This function behaves like {@link #get()}.
     * @return Returns the value.
     */
    default <ExceptionType extends Throwable> Type orElseThrow(Supplier<? extends ExceptionType> exceptionSupplier) throws ExceptionType {
        if(isPresent()) return get();
        throw exceptionSupplier.get();
    }

    /**
     * Returns this Undefined, if it is not empty, otherwise the Undefined supplier by the argument.
     * @param supplier Supplies an alternative Undefined, if this Undefined is empty.
     * @return Returns this or the supplied Undefined.
     */
    default Undefined<Type> or(Supplier<? extends Undefined<Type>> supplier){
        return isPresent()
                ? this
                : supplier.get();
    }

    /**
     * Streams one or zero elements, depending on if this Undefined is empty or not.
     * @return Returns a {@link Stream}.
     */
    default Stream<Type> stream(){
        return isPresent()
                ? Stream.of(get())
                : Stream.empty();
    }

    /**
     * Streams one or zero numbers, depending on if this Undefined is empty or not.
     * if this Undefined contains a value, that is not of type {@link Number}, an exception is thrown.
     * @return Returns an {@link IntStream}.
     */
    default IntStream intStream(){
        if (isEmpty())
            return IntStream.empty();
        Object value = get();
        if(value instanceof Number)
            return IntStream.of(((Number) value).intValue());

        throw new IllegalStateException("Value is not a number");
    }

    /**
     * Streams one or zero numbers, depending on if this Undefined is empty or not.
     * if this Undefined contains a value, that is not of type {@link Number}, an exception is thrown.
     * @return Returns a {@link DoubleStream}.
     */
    default DoubleStream doubleStream(){
        if (isEmpty())
            return DoubleStream.empty();
        Object value = get();
        if(value instanceof Number)
            return DoubleStream.of(((Number) value).doubleValue());

        throw new IllegalStateException("Value is not a number");
    }

    /**
     * Streams one or zero numbers, depending on if this Undefined is empty or not.
     * if this Undefined contains a value, that is not of type {@link Number}, an exception is thrown.
     * @return Returns a {@link LongStream}.
     */
    default LongStream longStream(){
        if (isEmpty())
            return LongStream.empty();
        Object value = get();
        if(value instanceof Number)
            return LongStream.of(((Number) value).longValue());

        throw new IllegalStateException("Value is not a number");
    }

    /**
     * Filters this Undefined value. If this Undefined is empty, this operation has no effect.
     * @param filter The filter function.
     * @return Returns an Undefined.
     */
    default Undefined<Type> filter(Predicate<? super Type> filter){
        if (isEmpty()) return this;
        return filter.test(get())
                ? this
                : Undefined.undefined();
    }

    /**
     * Converts this Undefined into an {@link Optional}.
     * @return If this Undefined is empty, an empty Optional is returned. Otherwise, a non-empty Optional.
     */
    default Optional<Type> toOptional(){
        if(isEmpty()) return Optional.empty();
        return Optional.of(get());
    }

    /**
     * Performs an action, if this Undefined contains a value.
     * @param consumer The action to be performed.
     */
    default void ifPresent(Consumer<? super Type> consumer){
        if(isPresent())
            consumer.accept(get());
    }

    /**
     * Performs an action, if this Undefined contains a value. Otherwise, an empty function is executed.
     * @param consumer The action to be performed, if this Undefined contains a value.
     * @param empty The action to be performed, if this Undefined is empty.
     */
    default void ifPresentOrElse(Consumer<? super Type> consumer, Runnable empty){
        if(isPresent())
            consumer.accept(get());
        else
            empty.run();
    }

    /**
     * Performs an action, if this Undefined is undefined.
     * @param action The action to be performed.
     */
    default void ifUndefined(Runnable action){
        if (isUndefined())
            action.run();
    }

    /**
     * Performs an action, if this Undefined contains null.
     * @param action The action to be performed.
     */
    default void ifNull(Runnable action){
        if(isNull())
            action.run();
    }

    /**
     * Performs a value mapping, if this Undefined contains null.
     * @param supplier The supplier function, that returns an Undefined, if this Undefined contains null.
     * @return Returns this or the supplied Undefined.
     */
    default Undefined<Type> ifNullMap(Supplier<Undefined<Type>> supplier){
        return isNull()
                ? Objects.requireNonNull(supplier.get())
                : this;
    }

    /**
     * Performs a value mapping, if this Undefined is undefined.
     * @param supplier The supplier function, that returns an Undefined, if this Undefined is undefined.
     * @return Returns this or the supplied Undefined.
     */
    default Undefined<Type> ifUndefinedMap(Supplier<Undefined<Type>> supplier){
        return isUndefined()
                ? Objects.requireNonNull(supplier.get())
                : this;
    }

    /**
     * Performs a value mapping, if this Undefined contains a value.
     * @param mapper The mapper function, that returns an Undefined, if this Undefined contains a value.
     * @return Returns this or the supplied Undefined.
     */
    default Undefined<Type> ifPresentMap(Function<Type,Undefined<Type>> mapper){
        return isPresent()
                ? Objects.requireNonNull(mapper.apply(get()))
                : this;
    }

    /**
     * Performs a value mapping, if this Undefined is empty.
     * @param supplier The supplier function, that returns an Undefined, if this Undefined is empty.
     * @return Returns this or the supplied Undefined.
     */
    default Undefined<Type> ifEmptyMap(Supplier<Undefined<Type>> supplier){
        return isEmpty()
                ? Objects.requireNonNull(supplier.get())
                : this;
    }

    /**
     * Performs a transformation of an Undefined.
     * @param mapper The mapping function. This function is not allowed to return null.
     * @return Returns an Undefined.
     * @param <Out> The typed instance type.
     */
    default <Out> Undefined<Out> transform(Function<Undefined<Type>,Undefined<Out>> mapper){
        return Objects.requireNonNull(mapper.apply(this));
    }

    /**
     * Returns an iterator, that returns one or zero elements.
     * @return Returns an iterator.
     */
    @NotNull
    @Override
    default Iterator<Type> iterator(){
        if(isPresent())
            return new Iterator<>() {

                private boolean hasNext = true;

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public Type next() {
                    hasNext = false;
                    return get();
                }
            };
        else
            return Spliterators.iterator(Spliterators.emptySpliterator());
    }


}
