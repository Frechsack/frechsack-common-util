package frechsack.prod.util.array;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.DoubleFunction;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface Array<E> extends Iterable<E> {

    @SuppressWarnings("unchecked")
    static <E extends java.lang.Number> Array.Number<E> ofNumber(E... array){
        Class<?> componentType = Objects.requireNonNull(array).getClass().componentType();
        if(componentType == BigInteger.class)
            return (Number<E>) new ArrayFactory.GenericNumber<>((BigInteger[]) array, BigInteger::valueOf, it -> BigInteger.valueOf((long) it), BigInteger::valueOf);
        if(componentType == BigDecimal.class)
            return (Number<E>) new ArrayFactory.GenericNumber<>((BigDecimal[]) array, BigDecimal::valueOf, BigDecimal::valueOf, BigDecimal::valueOf);
        if(componentType == Integer.class)
            return (Number<E>) new ArrayFactory.GenericNumber<>((Integer[])array, Integer::valueOf, it -> (int) it, it -> (int) it);
        if(componentType == Float.class)
            return (Number<E>) new ArrayFactory.GenericNumber<>((Float[])array, it -> (float) it, it -> (float) it, it -> (float) it);
        if(componentType == Double.class)
            return (Number<E>) new ArrayFactory.GenericNumber<>((Double[])array, it -> (double) it, it -> it, it -> (double) it);
        if(componentType == Long.class)
            return (Number<E>) new ArrayFactory.GenericNumber<>((Long[])array, it -> (long) it, it -> (long) it, it -> it);
        if(componentType == Byte.class)
            return (Number<E>) new ArrayFactory.GenericNumber<>((Byte[])array, it -> (byte) it, it -> (byte) it, it -> (byte) it);
        if(componentType == Short.class)
            return (Number<E>) new ArrayFactory.GenericNumber<>((Short[])array, it -> (short) it, it -> (short) it, it -> (short) it);
        throw new IllegalArgumentException("Unknown class-type: " + componentType + ". Please provide some custom converters for this type by: " + "ofNumber(array,intTransformer,doubleTransformer,longTransformer)");
    }

    static <E extends java.lang.Number> Array.Number<E> ofNumber(E[] array, IntFunction<E> intTransformer, DoubleFunction<E> doubleTransformer, LongFunction<E> longTransformer) {
        return new ArrayFactory.GenericNumber<>(Objects.requireNonNull(array),intTransformer,doubleTransformer,longTransformer);
    }

    @SafeVarargs
    static <E> Array<E> of(E... array) {
        return new ArrayFactory.Generic<>(Objects.requireNonNull(array));
    }

    static Array.Boolean ofBoolean(boolean... array){
        return new ArrayFactory.PrimitiveBoolean(Objects.requireNonNull(array));
    }

    static Array.Boolean ofBoolean(java.lang.Boolean... array){
        return new ArrayFactory.GenericBoolean(Objects.requireNonNull(array));
    }

    static Array.Number<Integer> ofInt(Integer... array){
        return new ArrayFactory.GenericNumber<>(Objects.requireNonNull(array), it -> it, it -> (int) it, it -> (int) it);
    }

    static Array.Number<Float> ofFloat(Float... array){
        return new ArrayFactory.GenericNumber<>(Objects.requireNonNull(array), it -> (float) it, it -> (float) it, it -> (float) it);
    }

    static Array.Number<Double> ofDouble(Double... array){
        return new ArrayFactory.GenericNumber<>(Objects.requireNonNull(array), it -> (double) it, it -> it, it -> (double) it);
    }

    static Array.Number<Long> ofLong(Long... array){
        return new ArrayFactory.GenericNumber<>(Objects.requireNonNull(array), it -> (long) it, it -> (long) it, it -> it);
    }

    static Array.Number<Integer> ofInt(int... array) {
        return new ArrayFactory.PrimitiveInt(Objects.requireNonNull(array));
    }

    static Array.Number<Float> ofFloat(float... array) {
        return new ArrayFactory.PrimitiveFloat(Objects.requireNonNull(array));
    }

    static Array.Number<Double> ofDouble(double... array) {
        return new ArrayFactory.PrimitiveDouble(Objects.requireNonNull(array));
    }

    static Array.Number<Long> ofLong(long... array) {
        return new ArrayFactory.PrimitiveLong(Objects.requireNonNull(array));
    }

    E get(int index);

    void set(int index, E value);

    default E replace(int index, E value){
        E oldValue = get(index);
        set(index,value);
        return oldValue;
    }

    Stream<E> stream();

    int length();

    boolean isPrimitive();

    Object nativeArray();

    default E[] toArrayBoxed(){
        return toArrayBoxed(0,length());
    }

    default E[] toArrayBoxed(int start){
        return toArrayBoxed(start,length()-start);
    }
    E[] toArrayBoxed(int start, int length);

    default Object toArray(){
        return toArray(0,length());
    }

    default Object toArray(int start){
        return toArray(start,length()-start);
    }
    Object toArray(int start, int length);

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    default Array<E> clone() {
        return new ArrayFactory.Generic<>(toArrayBoxed());
    }

    default boolean contains(Object element){
        return indexOf(element) > -1;
    }

    default boolean containsAll(Collection<?> clt){
        for (var element : clt)
            if(!contains(element)) return false;
        return true;
    }

    default int indexOf(Object element){
        for (int i = 0; i < length(); i++)
            if(Objects.equals(element,get(i))) return i;
        return -1;
    }

    @Override
    default Iterator<E> iterator(){
        return new ArrayFactory.Iterators.Iterator<>(this);
    }

    List<E> toList();

    default int lastIndexOf(Object element){
        for(int i = length() - 1 ; i > 0 ; i--)
            if(Objects.equals(element,get(i))) return i;
        return -1;
    }

    interface Number<E extends java.lang.Number> extends Array<E> {

        default PrimitiveIterator.OfInt iteratorInt(){
            return new ArrayFactory.Iterators.PrimitiveIntIterator(this);
        }

        default PrimitiveIterator.OfDouble iteratorDouble() {
            return new ArrayFactory.Iterators.PrimitiveDoubleIterator(this);
        }

        default PrimitiveIterator.OfLong iteratorLong(){
            return new ArrayFactory.Iterators.PrimitiveLongIterator(this);
        }

        default IntStream streamInt(){
            return stream().filter(Objects::nonNull).mapToInt(java.lang.Number::intValue);
        }

        default DoubleStream streamDouble(){
            return stream().filter(Objects::nonNull).mapToDouble(java.lang.Number::doubleValue);
        }

        default LongStream streamLong(){
            return stream().filter(Objects::nonNull).mapToLong(java.lang.Number::longValue);
        }

        @Override
        Array.Number<E> clone();

        int getInt(int index);

        double getDouble(int index);

        long getLong(int index);

        void setInt(int index, int value);

        void setDouble(int index, double value);

        void setLong(int index, long value);

        default int replace(int index, int value){
            var oldValue = getInt(index);
            setInt(index,value);
            return oldValue;
        }

        default double replace(int index, double value){
            var oldValue = getDouble(index);
            setDouble(index,value);
            return oldValue;
        }

        default long replace(int index, long value){
            var oldValue = getLong(index);
            setLong(index,value);
            return oldValue;
        }
    }

    interface Boolean extends Array<java.lang.Boolean> {

        boolean getBoolean(int index);

        void setBoolean(int index, boolean value);

        @Override
        Array.Boolean clone();

        default boolean replace(int index, boolean value){
            var oldValue = getBoolean(index);
            setBoolean(index, value);
            return oldValue;
        }
    }
}
