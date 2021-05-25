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
    // TODO: Generic char array

    static <E> Array<E> of(E... array)
    {
        return new Factory.GenericArray<>(array, true);
    }

    static <E> Array<E> of(boolean isReference, E... array)
    {
        return new Factory.GenericArray<>(array, isReference);
    }

    static <E> Array<E> ofGeneric(Class<E> type, int length)
    {
        return new Factory.GenericArray<>(length, type);
    }

    /**
     * Converter: Null passed: Default
     *
     * @param array
     * @param converter
     * @param <E>
     * @return
     */
    static <E extends Number> NumArray<E> of(Function<Number, E> converter, E... array)
    {
        return new Factory.GenericNumArray<>(array, true, converter);
    }

    static <E extends Number> NumArray<E> of(boolean isReference, Function<Number, E> converter, E... array)
    {
        return new Factory.GenericNumArray<>(array, isReference, converter);
    }

    static NumArray<Number> of(boolean isReference, Number... array)
    {
        return new Factory.GenericNumArray<>(array, isReference, number -> number == null ? 0 : number);
    }

    static NumArray<Number> ofGenericNumber(int length)
    {
        return new Factory.GenericNumArray<>(length, Number.class, number -> number == null ? 0 : number);
    }

    static <E extends Number> NumArray<E> ofGenericNumber(int length, Class<E> type, Function<Number, E> converter)
    {
        return new Factory.GenericNumArray<>(length, type, converter);
    }

    static BoolArray of(Boolean[] array){
        return new Factory.GenericBoolArray(array, true);
    }
    static BoolArray of(boolean isReference, Boolean[] array){
        return new Factory.GenericBoolArray(array, isReference);
    }
    static BoolArray ofGenericBoolean(int length){
        return new Factory.GenericBoolArray(length);
    }

    static NumArray<Integer> of(boolean isReference, int... array)
    {
        return new Factory.IntArray(array, isReference);
    }

    static NumArray<Integer> of(int... array)
    {
        return new Factory.IntArray(array, true);
    }

    static NumArray<Integer> ofInt(int length)
    {
        return new Factory.IntArray(length);
    }

    static NumArray<Byte> of(boolean isReference, byte... array)
    {
        return new Factory.ByteArray(array, isReference);
    }

    static NumArray<Byte> of(byte... array)
    {
        return new Factory.ByteArray(array, true);
    }

    static NumArray<Byte> ofByte(int length)
    {
        return new Factory.ByteArray(length);
    }

    static NumArray<Short> of(boolean isReference, short... array)
    {
        return new Factory.ShortArray(array, isReference);
    }

    static NumArray<Short> of(short... array)
    {
        return new Factory.ShortArray(array, true);
    }

    static NumArray<Short> ofShort(int length)
    {
        return new Factory.ShortArray(length);
    }

    static NumArray<Float> of(boolean isReference, float... array)
    {
        return new Factory.FloatArray(array, isReference);
    }

    static NumArray<Float> of(float... array)
    {
        return new Factory.FloatArray(array, true);
    }

    static NumArray<Float> ofFloat(int length)
    {
        return new Factory.FloatArray(length);
    }

    static NumArray<Double> of(boolean isReference, double... array)
    {
        return new Factory.DoubleArray(array, isReference);
    }

    static NumArray<Double> of(double... array)
    {
        return new Factory.DoubleArray(array, true);
    }

    static NumArray<Double> ofDouble(int length)
    {
        return new Factory.DoubleArray(length);
    }

    static NumArray<Long> of(boolean isReference, long... array)
    {
        return new Factory.LongArray(array, isReference);
    }

    static NumArray<Long> of(long... array)
    {
        return new Factory.LongArray(array, true);
    }

    static NumArray<Long> ofLong(int length)
    {
        return new Factory.LongArray(length);
    }

    static BoolArray of(boolean isReference, boolean... array)
    {
        return new Factory.BoolArray(array, isReference);
    }

    static BoolArray of(boolean... array)
    {
        return new Factory.BoolArray(array, true);
    }

    static Factory.BoolArray ofBoolean(int length)
    {
        return new Factory.BoolArray(length);
    }

    static Array<Character> of(boolean isReference, char... array)
    {
        return new Factory.CharArray(array, isReference);
    }

    static Array<Character> of(char... array)
    {
        return new Factory.CharArray(array, true);
    }

    static Array<Character> of(int length)
    {
        return new Factory.CharArray(length);
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
