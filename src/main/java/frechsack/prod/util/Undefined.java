package frechsack.prod.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * A container object that may contain the state 'undefined', 'null' or 'present'.
 * In case of 'present' a value can be obtained by {@link #get()}.
 * If one of the states is 'undefined' or 'null', this object will have the state 'empty' too.
 * Calling {@link #get()} when this container is empty, will throw an exception.
 * @param <Type> The container value class-type.
 */
public interface Undefined <Type> {

    @SuppressWarnings("unchecked")
    static <Type> Undefined<Type> of(@Nullable Type value){
        return value == null
                ? (Undefined<Type>) UndefinedFactory.Null.GENERIC
                : new UndefinedFactory.Present.Generic<>(value);
    }
    @SuppressWarnings("unchecked")
    static <Type> Undefined<Type> of(){
        return (Undefined<Type>) UndefinedFactory.Undefined.GENERIC;
    }

    static Undefined.@NotNull Boolean ofBoolean(@Nullable java.lang.Boolean value){
        return value == null
                ? UndefinedFactory.Null.BOOLEAN
                : new UndefinedFactory.Present.Boolean(value);
    }

    static Undefined.@NotNull Boolean ofBoolean(boolean value){
        return new UndefinedFactory.Present.Boolean(value);
    }

    static Undefined.@NotNull Boolean ofBoolean(){
        return UndefinedFactory.Undefined.BOOLEAN;
    }
    @SuppressWarnings("unchecked")
    static Undefined.@NotNull Number<Integer> ofInteger(@Nullable Integer value){
        return value == null
                ? (Number<Integer>) UndefinedFactory.Null.NUMBER
                : new UndefinedFactory.Present.Integer(value);
    }

    static Undefined.@NotNull Number<Integer> ofInteger(int value) {
        return new UndefinedFactory.Present.Integer(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.@NotNull Number<Integer> ofInteger(){
        return (Number<Integer>) UndefinedFactory.Undefined.NUMBER;
    }

    @SuppressWarnings("unchecked")
    static Undefined.@NotNull Number<Float> ofFloat(@Nullable Float value){
        return value == null
                ? (Number<Float>) UndefinedFactory.Null.NUMBER
                : new UndefinedFactory.Present.Float(value);
    }

    static Undefined.@NotNull Number<Float> ofFloat(float value) {
        return new UndefinedFactory.Present.Float(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.@NotNull Number<Float> ofFloat(){
        return (Number<Float>) UndefinedFactory.Undefined.NUMBER;
    }

    @SuppressWarnings("unchecked")
    static Undefined.@NotNull Number<Long> ofLong(@Nullable Long value){
        return value == null
                ? (Number<Long>) UndefinedFactory.Null.NUMBER
                : new UndefinedFactory.Present.Long(value);
    }

    static Undefined.@NotNull Number<Long> ofLong(long value) {
        return new UndefinedFactory.Present.Long(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.@NotNull Number<Long> ofLong(){
        return (Number<Long>) UndefinedFactory.Undefined.NUMBER;
    }

    @SuppressWarnings("unchecked")
    static Undefined.@NotNull Number<Double> ofDouble(@Nullable Double value){
        return value == null
                ? (Number<Double>) UndefinedFactory.Null.NUMBER
                : new UndefinedFactory.Present.Double(value);
    }

    static Undefined.@NotNull Number<Double> ofDouble(double value) {
        return new UndefinedFactory.Present.Double(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.@NotNull Number<Double> ofDouble(){
        return (Number<Double>) UndefinedFactory.Undefined.NUMBER;
    }
    @SuppressWarnings("unchecked")
    static <Type extends java.lang.Number> Undefined.@NotNull Number<Type> ofNumber(@Nullable Type value){
        return value == null
                ? (Number<Type>) UndefinedFactory.Null.NUMBER
                : new UndefinedFactory.Present.Number<>(value);
    }
    @SuppressWarnings("unchecked")
    static <Type extends java.lang.Number> Undefined.@NotNull Number<Type> ofNumber(){
        return (Number<Type>) UndefinedFactory.Undefined.NUMBER;
    }

    /**
     * Specialised version of {@link Undefined} with additional numeric operations.
     * @param <Type> The container value class-type.
     */
    interface Number<Type extends java.lang.Number> extends Undefined<Type> {

        /**
         * Returns the value as a long.
         * @throws java.util.NoSuchElementException, if {@link #isEmpty()} is true.
         * @return Returns the value as a long.
         */
        long getAsLong();

        /**
         * Returns the value as a double.
         * @throws java.util.NoSuchElementException, if {@link #isEmpty()} is true.
         * @return Returns the value as a double.
         */
        double getAsDouble();

        /**
         * Returns the value as an integer.
         * @throws java.util.NoSuchElementException, if {@link #isEmpty()} is true.
         * @return Returns the value as an integer.
         */
        default int getAsInt(){
            return (int) getAsLong();
        }

        /**
         * Returns the value as a byte.
         * @throws java.util.NoSuchElementException, if {@link #isEmpty()} is true.
         * @return Returns the value as a byte.
         */
        default byte getAsByte(){
            return (byte) getAsInt();
        }

        /**
         * Returns the value as a short.
         * @throws java.util.NoSuchElementException, if {@link #isEmpty()} is true.
         * @return Returns the value as a short.
         */
        default short getAsShort(){
            return (byte) getAsInt();
        }

        /**
         * Returns the value as a float.
         * @throws java.util.NoSuchElementException, if {@link #isEmpty()} is true.
         * @return Returns the value as a float.
         */
        default float getAsFloat(){
            return (float) getAsDouble();
        }

        /**
         * Returns a Stream with one or zero elements.
         * The Stream will have one element, if this object has the state 'present', otherwise the stream will be empty.
         * @return Returns the Stream.
         */
        default IntStream intStream(){
            return isPresent()
                    ? IntStream.of(getAsInt())
                    : IntStream.empty();
        }

        /**
         * Returns the value in this object, if one is present, otherwise the specified value.
         * @param value The alternative value.
         * @return Returns an integer.
         */
        default int orElse(int value) {
            return isPresent()
                    ? getAsInt()
                    : value;
        }
        /**
         * Returns a Stream with one or zero elements.
         * The Stream will have one element, if this object has the state 'present', otherwise the stream will be empty.
         * @return Returns the Stream.
         */
        default DoubleStream doubleStream(){
            return isPresent()
                    ? DoubleStream.of(getAsDouble())
                    : DoubleStream.empty();
        }

        /**
         * Returns the value in this object, if one is present, otherwise the specified value.
         * @param value The alternative value.
         * @return Returns a double.
         */
        default double orElse(double value) {
            return isPresent()
                    ? getAsDouble()
                    : value;
        }

        /**
         * Returns a Stream with one or zero elements.
         * The Stream will have one element, if this object has the state 'present', otherwise the stream will be empty.
         * @return Returns the Stream.
         */
        default LongStream longStream(){
            return isPresent()
                    ? LongStream.of(getAsLong())
                    : LongStream.empty();
        }

        /**
         * Returns the value in this object, if one is present, otherwise the specified value.
         * @param value The alternative value.
         * @return Returns a long.
         */
        default long orElse(long value) {
            return isPresent()
                    ? getAsLong()
                    : value;
        }

        /**
         * Executes the given Consumer, if a value is present.
         * @param consumer The Consumer to be executed.
         */
        default void ifPresent(@NotNull IntConsumer consumer){
            if(isPresent())
                consumer.accept(getAsInt());
        }

        /**
         * Executes the given Consumer, if a value is present.
         * @param consumer The Consumer to be executed.
         */
        default void ifPresent(@NotNull DoubleConsumer consumer){
            if(isPresent())
                consumer.accept(getAsDouble());
        }

        /**
         * Executes the given Consumer, if a value is present.
         * @param consumer The Consumer to be executed.
         */
        default void ifPresent(@NotNull LongConsumer consumer){
            if (isPresent())
                consumer.accept(getAsLong());
        }

        default OptionalInt toOptionalInt(){
            return isPresent()
                    ? OptionalInt.of(getAsInt())
                    : OptionalInt.empty();
        }

        default OptionalDouble toOptionalDouble(){
            return isPresent()
                    ? OptionalDouble.of(getAsDouble())
                    : OptionalDouble.empty();
        }

        default OptionalLong toOptionalLong(){
            return isPresent()
                    ? OptionalLong.of(getAsLong())
                    : OptionalLong.empty();
        }

    }

    /**
     * Specialised version of {@link Undefined} with booleans.
     */
    interface Boolean extends Undefined<java.lang.Boolean> {

        /**
         * Returns the value as a boolean.
         * @throws java.util.NoSuchElementException, if {@link #isEmpty()} is true.
         * @return Returns the value as a boolean.
         */
        boolean getAsBoolean();

        /**
         * Returns the value in this object, if one is present, otherwise the specified value.
         * @param value The alternative value.
         * @return Returns a boolean.
         */
        default Stream<java.lang.Boolean> orElse(boolean value){
            return isPresent()
                    ? Stream.of(get())
                    : Stream.of(value);
        }
    }

    /**
     * Checks if this object has the state 'undefined'.
     * @return {@code true} if this object has the state 'undefined', else {@code false}.
     */
    boolean isUndefined();

    /**
     * Checks if this object has the state 'null'.
     * @return {@code true} if this object has the state 'null', else {@code false}.
     */
    boolean isNull();

    /**
     * Returns the value of this object.
     * @throws java.util.NoSuchElementException, if this Undefined has not the state 'present'.
     * @return Returns the value or throws an Exception.
     */
    Type get();

    /**
     * Checks if this object has the state 'present'.
     * @return {@code true} if this object has the state 'present', else {@code false}.
     */
    default boolean isPresent(){
        return !isNull() && !isUndefined();
    }

    /**
     * Checks if this object has the state 'empty'.
     * @return {@code true} if this object has the state 'present', else {@code false}.
     */
    default boolean isEmpty(){
        return isNull() || isUndefined();
    }

    /**
     * Returns a {@link Stream} with one or zero elements.
     * The Stream will have one element, if this object has the state 'present', otherwise the stream will be empty.
     * @return Returns a Stream.
     */
    default Stream<Type> stream(){
        return isPresent()
                ? Stream.of(get())
                : Stream.empty();
    }

    /**
     * Returns a {@link Stream} with one or zero elements.
     * This version includes {@code null} as an element.
     * @return Returns a Stream.
     */
    default Stream<Type> streamIncludeNull(){
        return isPresent()
                ? Stream.of(get())
                : isNull()
                    ? Stream.of((Type) null)
                    : Stream.empty();
    }

    /**
     * Returns the value in this object, if one is present, otherwise the specified value.
     * @param value The alternative value.
     * @return Returns a long.
     */
    default Type orElse(Type value) {
        return isPresent()
                ? get()
                : value;
    }

    /**
     * Acts like {@link #get()}, but will throw a customized Exception, if this object is 'empty'.
     * @param exception The Exception that should be thrown, if this object is 'empty'.
     * @return Returns the value or throws an Exception.
     */
    default Type orElseThrow(@NotNull Supplier<? extends RuntimeException> exception){
        if(isEmpty()) throw exception.get();
        return get();
    }

    default void ifPresent(@NotNull Consumer<? super Type> consumer){
        if(isPresent())
            consumer.accept(get());
    }

    default void ifEmpty(@NotNull Runnable runnable){
        if(isEmpty())
            runnable.run();
    }

    default void ifNull(@NotNull Runnable runnable){
        if(isNull())
            runnable.run();
    }

    default void ifUndefined(@NotNull Runnable runnable){
        if(isUndefined())
            runnable.run();
    }

    default Optional<Type> toOptional(){
        return isPresent()
                ? Optional.of(get())
                : Optional.empty();
    }

    default <Target> Target compute(@NotNull Function<Undefined<Type>, Target> function){
        return function.apply(this);
    }

}
