package frechsack.dev.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

public interface Undefined<E> {

    @SuppressWarnings("unchecked")
    static <E> Undefined<E> of(E value){
        return value == null ? (Undefined<E>) UndefinedFactory.NULL : new UndefinedFactory.Generic<>(value);
    }

    @SuppressWarnings("unchecked")
    static <E> Undefined<E> of(){
        return (Undefined<E>) UndefinedFactory.UNDEFINED;
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Integer> ofInt(Integer value){
        return value == null ? (Number<Integer>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Int(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Integer> ofInt(){
        return (Number<Integer>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Number<Integer> ofInt(int value){
        return new UndefinedFactory.Int(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Double> ofDouble(Double value){
        return value == null ? (Number<Double>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Double(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Double> ofDouble(){
        return (Number<Double>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Number<Double> ofDouble(int value){
        return new UndefinedFactory.Double(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Long> ofLong(Long value){
        return value == null ? (Number<Long>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Long(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Long> ofLong(){
        return (Number<Long>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Number<Long> ofLong(long value){
        return new UndefinedFactory.Long(value);
    }

    @SuppressWarnings("unchecked")
    static <E extends java.lang.Number> Undefined.Number<E> ofNumber(E value){
        return value == null ? (Number<E>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Number<E>(value);
    }

    @SuppressWarnings("unchecked")
    static <E extends java.lang.Number>  Undefined.Number<E> ofNumber(){
        return (Number<E>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Boolean ofBoolean(java.lang.Boolean value){
        return value == null ?  UndefinedFactory.NULL_BOOLEAN : new UndefinedFactory.Boolean(value);
    }

    static Undefined.Boolean ofBoolean(){
        return UndefinedFactory.UNDEFINED_BOOLEAN;
    }

    static Undefined.Boolean ofBoolean(boolean value){
        return new UndefinedFactory.Boolean(value);
    }


    E get();

    boolean isUndefined();

    boolean isNull();

    default boolean isEmpty(){
        return isUndefined() || isNull();
    }

    default boolean isPresent(){
        return !isUndefined() && !isNull();
    }

    default E orElse(E value) {
        return isEmpty() ? value : get();
    }

    default Undefined<E> filter(Predicate<E> predicate) {
        if(isEmpty()) return this;
        if(predicate == null) return Undefined.of();
        boolean isPassed = predicate.test(get());
        return isPassed ? this : Undefined.of();
    }

    default E orElseGet(Supplier<E> supplier) {
        return isPresent() ? get() : Objects.requireNonNull(supplier).get();
    }

    default Undefined<E> or(Supplier<E> supplier) {
        if(isPresent()) return this;
        if(supplier == null) return of();
        return of(supplier.get());
    }

    @SuppressWarnings("unchecked")
    default <T> Undefined<T> map(Function<E, T> map) {
        if(isEmpty())
            return (Undefined<T>) this;
        if(map == null) return of();
        return of(map.apply(get()));
    }

    default <T extends java.lang.Number> Undefined.Number<T> mapNumber(Function<E,T> map){
        if(isEmpty()) return this instanceof Undefined.Number ? (Number<T>) this : ofNumber();
        if(map == null) return ofNumber();
        return ofNumber(map.apply(get()));
    }

    default Undefined.Number<Integer> mapInt(Function<E, Integer> map){
        if(isEmpty()) return this instanceof Undefined.Number ? (Number<Integer>) this : ofInt();
        if(map == null) return ofInt();
        return ofInt(map.apply(get()));
    }

    default Undefined.Number<Long> mapLong(Function<E, Long> map){
        if(isEmpty()) return this instanceof Undefined.Number ? (Number<Long>) this : ofLong();
        if(map == null) return ofLong();
        return ofLong(map.apply(get()));
    }

    default Undefined.Number<Double> mapDouble(Function<E, Double> map){
        if(isEmpty()) return this instanceof Undefined.Number ? (Number<Double>) this : ofDouble();
        if(map == null) return ofDouble();
        return ofDouble(map.apply(get()));
    }

    default Undefined.Boolean mapBoolean(Function<E, java.lang.Boolean> map){
        if(isEmpty()) return this instanceof Undefined.Boolean ? (Boolean) this : ofBoolean();
        if(map == null) return ofBoolean();
        return ofBoolean(map.apply(get()));
    }

    default Undefined<E> mapNull(){
        if(isNull()) return this;
        return of();
    }

    // TODO: Flat-map
    // TODO: transform

    interface Number<E extends java.lang.Number> extends Undefined<E>{

        int getInt();

        double getDouble();

        long getLong();

        default int orElse(int value){
            return isEmpty() ? value : getInt();
        }

        default long orElse(long value){
            return isEmpty() ? value : getLong();
        }

        default double orElse(double value){
            return isEmpty() ? value : getDouble();
        }

        default int orElseGet(IntSupplier supplier) {
            return isPresent() ? getInt() : Objects.requireNonNull(supplier).getAsInt();
        }

        default double orElseGet(DoubleSupplier supplier) {
            return isPresent() ? getDouble() : Objects.requireNonNull(supplier).getAsDouble();
        }

        default long orElseGet(LongSupplier supplier) {
            return isPresent() ? getLong() : Objects.requireNonNull(supplier).getAsLong();
        }

        default Undefined.Number<Integer> or(IntSupplier supplier){
            if(isPresent()) return UndefinedFactory.pipeInteger(this);
            if(supplier == null) return ofInt();
            return ofInt(supplier.getAsInt());
        }

        default Undefined.Number<Double> or(DoubleSupplier supplier){
            if(isPresent()) return UndefinedFactory.pipeDouble(this);
            if(supplier == null) return ofDouble();
            return ofDouble(supplier.getAsDouble());
        }

        default Undefined.Number<Long> or(LongSupplier supplier){
            if(isPresent()) return UndefinedFactory.pipeLong(this);
            if(supplier == null) return ofLong();
            return ofLong(supplier.getAsLong());
        }

        default Undefined.Number<Integer> filter(IntPredicate predicate){
            if(isEmpty()) return UndefinedFactory.pipeInteger(this);
            if(predicate == null) return ofInt();
            boolean isPassed = predicate.test(getInt());
            return isPassed ? UndefinedFactory.pipeInteger(this) : Undefined.ofInt();
        }

        default Undefined.Number<Double> filter(DoublePredicate predicate){
            if(isEmpty()) return UndefinedFactory.pipeDouble(this);
            if(predicate == null) return ofDouble();
            boolean isPassed = predicate.test(getDouble());
            return isPassed ? UndefinedFactory.pipeDouble(this) : Undefined.ofDouble();
        }

        default Undefined.Number<Long> filter(LongPredicate predicate){
            if(isEmpty()) return UndefinedFactory.pipeLong(this);
            if(predicate == null) return ofLong();
            boolean isPassed = predicate.test(getLong());
            return isPassed ? UndefinedFactory.pipeLong(this) : Undefined.ofLong();
        }

        @Override
        default Undefined.Number<Integer> mapInt(Function<E, Integer> map) {
            if(isEmpty()) return UndefinedFactory.pipeInteger(this);
            if(map == null) return ofInt();
            return ofInt(map.apply(get()));
        }

        @Override
        default Undefined.Number<Long> mapLong(Function<E, Long> map) {
            if(isEmpty()) return UndefinedFactory.pipeLong(this);
            if(map == null) return ofLong();
            return ofLong(map.apply(get()));
        }

        @Override
        default Undefined.Number<Double> mapDouble(Function<E, Double> map) {
            if(isEmpty()) return UndefinedFactory.pipeDouble(this);
            if(map == null) return ofDouble();
            return ofDouble(map.apply(get()));
        }


    }

    interface Boolean extends Undefined<java.lang.Boolean> {

        boolean getBoolean();

        default boolean orElse(boolean value){
            return isEmpty() ? value : getBoolean();
        }

        default Undefined.Boolean or(BooleanSupplier supplier) {
            if(isPresent()) return this;
            if(supplier == null) return ofBoolean();
            return ofBoolean(supplier.getAsBoolean());
        }

        default boolean orElseGet(BooleanSupplier supplier) {
            return isPresent() ? getBoolean() : Objects.requireNonNull(supplier).getAsBoolean();
        }
    }
}
