package com.frechsack.dev.util.array;

import com.frechsack.dev.util.route.Routable;

import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.IntFunction;
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

    static <E> Array<E> ofGeneric(Class<E> type, int length)
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
    static <E extends Number> NumericArray<E> of(Function<Number, E> converter, E... array)
    {
        return new ArrayFactory.GenericNumericArray<>(array, true, converter);
    }

    static <E extends Number> NumericArray<E> of(boolean isReference, Function<Number, E> converter, E... array)
    {
        return new ArrayFactory.GenericNumericArray<>(array, isReference, converter);
    }

    static NumericArray<Number> of(boolean isReference, Number... array)
    {
        return new ArrayFactory.GenericNumericArray<>(array, isReference, number -> number == null ? 0 : number);
    }

    static NumericArray<Number> ofGenericNumber(int length)
    {
        return new ArrayFactory.GenericNumericArray<>(length, Number.class, number -> number == null ? 0 : number);
    }

    static <E extends Number> NumericArray<E> ofGenericNumber(int length, Class<E> type, Function<Number, E> converter)
    {
        return new ArrayFactory.GenericNumericArray<>(length, type, converter);
    }

    static BooleanArray of(Boolean[] array){
        return new ArrayFactory.GenericBooleanArray(array,true);
    }
    static BooleanArray of(boolean isReference,Boolean[] array){
        return new ArrayFactory.GenericBooleanArray(array,isReference);
    }
    static BooleanArray ofGenericBoolean(int length){
        return new ArrayFactory.GenericBooleanArray(length);
    }

    static NumericArray<Integer> of(boolean isReference, int... array)
    {
        return new ArrayFactory.IntArray(array, isReference);
    }

    static NumericArray<Integer> of(int... array)
    {
        return new ArrayFactory.IntArray(array, true);
    }

    static NumericArray<Integer> ofInt(int length)
    {
        return new ArrayFactory.IntArray(length);
    }

    static NumericArray<Byte> of(boolean isReference, byte... array)
    {
        return new ArrayFactory.ByteArray(array, isReference);
    }

    static NumericArray<Byte> of(byte... array)
    {
        return new ArrayFactory.ByteArray(array, true);
    }

    static NumericArray<Byte> ofByte(int length)
    {
        return new ArrayFactory.ByteArray(length);
    }

    static NumericArray<Short> of(boolean isReference, short... array)
    {
        return new ArrayFactory.ShortArray(array, isReference);
    }

    static NumericArray<Short> of(short... array)
    {
        return new ArrayFactory.ShortArray(array, true);
    }

    static NumericArray<Short> ofShort(int length)
    {
        return new ArrayFactory.ShortArray(length);
    }

    static NumericArray<Float> of(boolean isReference, float... array)
    {
        return new ArrayFactory.FloatArray(array, isReference);
    }

    static NumericArray<Float> of(float... array)
    {
        return new ArrayFactory.FloatArray(array, true);
    }

    static NumericArray<Float> ofFloat(int length)
    {
        return new ArrayFactory.FloatArray(length);
    }

    static NumericArray<Double> of(boolean isReference, double... array)
    {
        return new ArrayFactory.DoubleArray(array, isReference);
    }

    static NumericArray<Double> of(double... array)
    {
        return new ArrayFactory.DoubleArray(array, true);
    }

    static NumericArray<Double> ofDouble(int length)
    {
        return new ArrayFactory.DoubleArray(length);
    }

    static NumericArray<Long> of(boolean isReference, long... array)
    {
        return new ArrayFactory.LongArray(array, isReference);
    }

    static NumericArray<Long> of(long... array)
    {
        return new ArrayFactory.LongArray(array, true);
    }

    static NumericArray<Long> ofLong(int length)
    {
        return new ArrayFactory.LongArray(length);
    }

    static BooleanArray of(boolean isReference, boolean... array)
    {
        return new ArrayFactory.BooleanArray(array, isReference);
    }

    static BooleanArray of(boolean... array)
    {
        return new ArrayFactory.BooleanArray(array, true);
    }

    static ArrayFactory.BooleanArray ofBoolean(int length)
    {
        return new ArrayFactory.BooleanArray(length);
    }

    static Array<Character> of(boolean isReference, char... array)
    {
        return new ArrayFactory.CharArray(array, isReference);
    }

    static Array<Character> of(char... array)
    {
        return new ArrayFactory.CharArray(array, true);
    }

    static Array<Character> of(int length)
    {
        return new ArrayFactory.CharArray(length);
    }

    int indexOf(Object element);

    int lastIndexOf(Object element);

    void clear();

    boolean contains(Object element);

    E[] toArray();

    Object asArray();

    int length();

    E get(int index);

    void set(int index, E element);

    E getAndSet(int index, E element);

    Stream<E> stream();

    Stream<E> parallelStream();

    boolean isPrimitive();

    IntFunction<E[]> generator();

    @Override
    Spliterator<E> spliterator();

    boolean equals(E[] array);

    List<E> asList();

    void sort(Comparator<? super E> c);

    @Override
    default E apply(Integer index)
    {
        return get(index);
    }

    void sort();
}
