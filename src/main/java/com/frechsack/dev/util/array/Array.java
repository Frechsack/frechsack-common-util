package com.frechsack.dev.util.array;

import com.frechsack.dev.util.Pair;
import com.frechsack.dev.util.route.Routable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Array<E> extends Iterable<E>, Routable<E>, Function<Integer, E>
{
    static <E> Array<E> of(E... array)
    {
        return new ArrayFactory.GenericArray<>(array, true);

    }

    static <E> Array<E> of(boolean isReference, E... array)
    {
        return new ArrayFactory.GenericArray<>(array, isReference);
    }

    static <E> Array<E> of(int length, Class<E> type)
    {
        return new ArrayFactory.GenericArray<>(length, type);
    }

    /**
     * Converter: Null passed: Default
     *
     * @param array
     * @param converter
     * @param <E>
     * @return
     */
    static <E extends Number> Numbers<E> ofTypedNumber(Function<Number, E> converter, E... array)
    {
        checkNumberConverter(converter);
        return new ArrayFactory.GenericNumbers<>(array, true, converter);
    }

    static <E extends Number> Numbers<E> ofTypedNumber(boolean isReference, Function<Number, E> converter, E... array)
    {
        checkNumberConverter(converter);
        return new ArrayFactory.GenericNumbers<>(array, isReference, converter);
    }

    static <E extends Number> Numbers<E> ofTypedNumber(int length, Class<E> type, Function<Number, E> converter)
    {
        checkNumberConverter(converter);
        return new ArrayFactory.GenericNumbers<>(length, type, converter);
    }

    private static void checkNumberConverter(Function<Number, ?> converter)
    {
        Objects.requireNonNull(converter);
        if (converter.apply(null) == null)
            throw new IllegalArgumentException("The converter returned null when null was passed. But null is not a valid number");
    }

    static Numbers<Number> ofNumber(boolean isReference, Number... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number);
    }

    static Numbers<Number> ofNumber(Number... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number);
    }

    static Numbers<Number> ofNumber(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Number.class, number -> number == null ? 0 : number);
    }


    static Numbers<Integer> ofInt(boolean isReference, int... array)
    {
        return new ArrayFactory.IntArray(array, isReference);
    }

    static Numbers<Integer> ofInt(int... array)
    {
        return new ArrayFactory.IntArray(array, true);
    }

    static Numbers<Integer> ofInt(int length)
    {
        return new ArrayFactory.IntArray(length);
    }

    static Numbers<Integer> ofGenericInt(Integer... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.intValue());
    }

    static Numbers<Integer> ofGenericInt(boolean isReference, Integer... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.intValue());
    }

    static Numbers<Integer> ofGenericInt(int length)
    {
        return new ArrayFactory.GenericNumbers<Integer>(length, Integer.class, number -> number == null ? 0 : number.intValue());
    }

    static Numbers<Byte> ofByte(boolean isReference, byte... array)
    {
        return new ArrayFactory.ByteArray(array, isReference);
    }

    static Numbers<Byte> ofByte(byte... array)
    {
        return new ArrayFactory.ByteArray(array, true);
    }

    static Numbers<Byte> ofByte(int length)
    {
        return new ArrayFactory.ByteArray(length);
    }

    static Numbers<Byte> ofGenericByte(Byte... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.byteValue());
    }

    static Numbers<Byte> ofGenericByte(boolean isReference, Byte... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.byteValue());
    }

    static Numbers<Byte> ofGenericByte(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Byte.class, number -> number == null ? 0 : number.byteValue());
    }

    static Numbers<Short> ofShort(boolean isReference, short... array)
    {
        return new ArrayFactory.ShortArray(array, isReference);
    }

    static Numbers<Short> ofShort(short... array)
    {
        return new ArrayFactory.ShortArray(array, true);
    }

    static Numbers<Short> ofShort(int length)
    {
        return new ArrayFactory.ShortArray(length);
    }

    static Numbers<Short> ofGenericShort(Short... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.shortValue());
    }

    static Numbers<Short> ofGenericShort(boolean isReference, Short... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.shortValue());
    }

    static Numbers<Short> ofGenericShort(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Short.class, number -> number == null ? 0 : number.shortValue());
    }

    static Numbers<Float> ofFloat(boolean isReference, float... array)
    {
        return new ArrayFactory.FloatArray(array, isReference);
    }

    static Numbers<Float> ofFloat(float... array)
    {
        return new ArrayFactory.FloatArray(array, true);
    }

    static Numbers<Float> ofFloat(int length)
    {
        return new ArrayFactory.FloatArray(length);
    }

    static Numbers<Float> ofGenericFloat(Float... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.floatValue());
    }

    static Numbers<Float> ofGenericFloat(boolean isReference, Float... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.floatValue());
    }

    static Numbers<Float> ofGenericFloat(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Float.class, number -> number == null ? 0 : number.floatValue());
    }

    static Numbers<Double> ofDouble(boolean isReference, double... array)
    {
        return new ArrayFactory.DoubleArray(array, isReference);
    }

    static Numbers<Double> ofDouble(double... array)
    {
        return new ArrayFactory.DoubleArray(array, true);
    }

    static Numbers<Double> ofDouble(int length)
    {
        return new ArrayFactory.DoubleArray(length);
    }

    static Numbers<Double> ofGenericDouble(Double... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.doubleValue());
    }

    static Numbers<Double> ofGenericDouble(boolean isReference, Double... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.doubleValue());
    }

    static Numbers<Double> ofGenericDouble(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Double.class, number -> number == null ? 0 : number.doubleValue());
    }

    static Numbers<Long> ofLong(boolean isReference, long... array)
    {
        return new ArrayFactory.LongArray(array, isReference);
    }

    static Numbers<Long> ofLong(long... array)
    {
        return new ArrayFactory.LongArray(array, true);
    }

    static Numbers<Long> ofLong(int length)
    {
        return new ArrayFactory.LongArray(length);
    }

    static Numbers<Long> ofGenericLong(Long... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.longValue());
    }

    static Numbers<Long> ofGenericLong(boolean isReference, Long... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.longValue());
    }

    static Numbers<Long> ofGenericLong(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Long.class, number -> number == null ? 0 : number.longValue());
    }

    static Booleans ofBoolean(boolean isReference, boolean... array)
    {
        return new ArrayFactory.Booleans(array, isReference);
    }

    static Booleans ofBoolean(boolean... array)
    {
        return new ArrayFactory.Booleans(array, true);
    }

    static Booleans ofBoolean(int length)
    {
        return new ArrayFactory.Booleans(length);
    }

    static Booleans ofGenericBoolean(Boolean... array)
    {
        return new ArrayFactory.GenericBooleans(array, true);
    }

    static Booleans ofGenericBoolean(boolean isReference, Boolean... array)
    {
        return new ArrayFactory.GenericBooleans(array, isReference);
    }

    static Booleans ofGenericBoolean(int length)
    {
        return new ArrayFactory.GenericBooleans(length);
    }

    static Characters ofChar(boolean isReference, char... array)
    {
        return new ArrayFactory.Characters(array, isReference);
    }

    static Characters ofChar(char... array)
    {
        return new ArrayFactory.Characters(array, true);
    }

    static Characters ofChar(int length)
    {
        return new ArrayFactory.Characters(length);
    }

    static Characters ofGenericChar(Character... array)
    {
        return new ArrayFactory.GenericCharacters(array, true);
    }

    static Characters ofGenericChar(boolean isReference, Character... array)
    {
        return new ArrayFactory.GenericCharacters(array, isReference);
    }

    static Characters ofGenericChar(int length)
    {
        return new ArrayFactory.GenericCharacters(length);
    }

    default int indexOf(Object element){
        return indexOf(0, length(), element);
    }

    default int indexOf(int start, Object element){
        return indexOf(start, length(), element);
    }

    int indexOf(int start, int end, Object element);

    default int indexOf(Predicate<E> predicate){
        return indexOf(0,length(),predicate);
    }

    default int indexOf(int start, Predicate<E> predicate){
        return indexOf(start,length(),predicate);
    }

    int indexOf(int start, int end, Predicate<E> predicate);

    int lastIndexOf(Object element);

    int lastIndexOf(Predicate<E> predicate);

    int firstIndexOf(Object element);

    int firstIndexOf(Predicate<E> predicate);

    void clear();

    boolean contains(Object element);

    E[] toArray();

    Object asArray();

    int length();

    E get(int index);

    void set(int index, E element);

    default E getAndSet(int index, E element)
    {
        E last = get(index);
        set(index, element);
        return last;
    }

    default void transform(Function<E,E> mapper){
        for (int i = 0; i < length(); i++) set(i,mapper.apply(get(i)));
    }

    Stream<Pair<Integer,E>> streamIndices();

    Stream<Pair<Integer,E>> parallelStreamIndices();

    Stream<E> stream();

    Stream<E> parallelStream();

    boolean isPrimitive();

    IntFunction<E[]> generator();

    @Override
    Spliterator<E> spliterator();

    boolean equals(E[] array);

    List<E> asList();

    void sort(Comparator<? super E> c);

    void fill(E element);

    void sortReverse();

    void sortReverse(Comparator<? super E> c);

    void reverse();

    @Override
    default E apply(Integer index)
    {
        return get(index);
    }

    void sort();
}
