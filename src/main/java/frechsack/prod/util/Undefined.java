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

    interface Number<Type extends java.lang.Number> extends Undefined<Type> {

        long getAsLong();

        double getAsDouble();

        default int getAsInt(){
            return (int) getAsLong();
        }
        default byte getAsByte(){
            return (byte) getAsInt();
        }

        default short getAsShort(){
            return (byte) getAsInt();
        }

        default float getAsFloat(){
            return (float) getAsDouble();
        }

        default IntStream intStream(){
            return isPresent()
                    ? IntStream.of(getAsInt())
                    : IntStream.empty();
        }

        default int orElse(int value) {
            return isPresent()
                    ? getAsInt()
                    : value;
        }

        default DoubleStream doubleStream(){
            return isPresent()
                    ? DoubleStream.of(getAsDouble())
                    : DoubleStream.empty();
        }

        default double orElse(double value) {
            return isPresent()
                    ? getAsDouble()
                    : value;
        }

        default LongStream longStream(){
            return isPresent()
                    ? LongStream.of(getAsLong())
                    : LongStream.empty();
        }

        default long orElse(long value) {
            return isPresent()
                    ? getAsLong()
                    : value;
        }

        default void ifPresent(IntConsumer consumer){
            if(isPresent())
                consumer.accept(getAsInt());
        }

        default void ifPresent(DoubleConsumer consumer){
            if(isPresent())
                consumer.accept(getAsDouble());
        }

        default void ifPresent(LongConsumer consumer){
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

    interface Boolean extends Undefined<java.lang.Boolean> {

        boolean getAsBoolean();

        default Stream<java.lang.Boolean> orElse(boolean value){
            return isPresent()
                    ? Stream.of(get())
                    : Stream.of(value);
        }
    }

    boolean isUndefined();

    boolean isNull();

    Type get();

    default boolean isPresent(){
        return !isNull() && !isUndefined();
    }

    default boolean isEmpty(){
        return isNull() || isUndefined();
    }

    default Stream<Type> stream(){
        return isPresent()
                ? Stream.of(get())
                : Stream.empty();
    }

    default Type orElse(Type value) {
        return isPresent()
                ? get()
                : value;
    }

    default Type orElseThrow(Supplier<? extends RuntimeException> exception){
        if(isEmpty()) throw exception.get();
        return get();
    }

    default void ifPresent(Consumer<? super Type> consumer){
        if(isPresent())
            consumer.accept(get());
    }

    default void ifEmpty(Runnable runnable){
        if(isEmpty())
            runnable.run();
    }

    default void ifNull(Runnable runnable){
        if(isNull())
            runnable.run();
    }

    default void ifUndefined(Runnable runnable){
        if(isUndefined())
            runnable.run();
    }

    default Optional<Type> toOptional(){
        return isPresent()
                ? Optional.of(get())
                : Optional.empty();
    }

}
