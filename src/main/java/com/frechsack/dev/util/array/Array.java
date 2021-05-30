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
    // TODO: Cached Array wrappers.

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
    static <E extends Number> Numbers<E> of(Function<Number, E> converter, E... array)
    {
        return new Factory.GenericNumbers<>(array, true, converter);
    }

    static <E extends Number> Numbers<E> of(boolean isReference, Function<Number, E> converter, E... array)
    {
        return new Factory.GenericNumbers<>(array, isReference, converter);
    }

    static Numbers<Number> of(boolean isReference, Number... array)
    {
        return new Factory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number);
    }

    static Numbers<Number> ofNumber(int length)
    {
        return new Factory.GenericNumbers<>(length, Number.class, number -> number == null ? 0 : number);
    }

    static <E extends Number> Numbers<E> ofNumber(int length, Class<E> type, Function<Number, E> converter)
    {
        return new Factory.GenericNumbers<>(length, type, converter);
    }

    static Booleans of(Boolean[] array){
        return new Factory.GenericBooleans(array, true);
    }

    static Booleans of(boolean isReference, Boolean[] array){
        return new Factory.GenericBooleans(array, isReference);
    }

    static Booleans ofGenericBoolean(int length){
        return new Factory.GenericBooleans(length);
    }

    static Characters of(Character[] array){
        return new Factory.GenericCharacters(array,true);
    }

    static Characters of(boolean isReference,Character[] array){
        return new Factory.GenericCharacters(array,isReference);
    }

    static Characters ofGenericCharacter(int length){
        return new Factory.GenericCharacters(length);
    }

    static Numbers<Integer> of(boolean isReference, int... array)
    {
        return new Factory.IntArray(array, isReference);
    }

    static Numbers<Integer> of(int... array)
    {
        return new Factory.IntArray(array, true);
    }

    static Numbers<Integer> ofInt(int length)
    {
        return new Factory.IntArray(length);
    }

    static Numbers<Byte> of(boolean isReference, byte... array)
    {
        return new Factory.ByteArray(array, isReference);
    }

    static Numbers<Byte> of(byte... array)
    {
        return new Factory.ByteArray(array, true);
    }

    static Numbers<Byte> ofByte(int length)
    {
        return new Factory.ByteArray(length);
    }

    static Numbers<Short> of(boolean isReference, short... array)
    {
        return new Factory.ShortArray(array, isReference);
    }

    static Numbers<Short> of(short... array)
    {
        return new Factory.ShortArray(array, true);
    }

    static Numbers<Short> ofShort(int length)
    {
        return new Factory.ShortArray(length);
    }

    static Numbers<Float> of(boolean isReference, float... array)
    {
        return new Factory.FloatArray(array, isReference);
    }

    static Numbers<Float> of(float... array)
    {
        return new Factory.FloatArray(array, true);
    }

    static Numbers<Float> ofFloat(int length)
    {
        return new Factory.FloatArray(length);
    }

    static Numbers<Double> of(boolean isReference, double... array)
    {
        return new Factory.DoubleArray(array, isReference);
    }

    static Numbers<Double> of(double... array)
    {
        return new Factory.DoubleArray(array, true);
    }

    static Numbers<Double> ofDouble(int length)
    {
        return new Factory.DoubleArray(length);
    }

    static Numbers<Long> of(boolean isReference, long... array)
    {
        return new Factory.LongArray(array, isReference);
    }

    static Numbers<Long> of(long... array)
    {
        return new Factory.LongArray(array, true);
    }

    static Numbers<Long> ofLong(int length)
    {
        return new Factory.LongArray(length);
    }

    static Booleans of(boolean isReference, boolean... array)
    {
        return new Factory.Booleans(array, isReference);
    }

    static Booleans of(boolean... array)
    {
        return new Factory.Booleans(array, true);
    }

    static Factory.Booleans ofBoolean(int length)
    {
        return new Factory.Booleans(length);
    }

    static Array<Character> of(boolean isReference, char... array)
    {
        return new Factory.Characters(array, isReference);
    }

    static Array<Character> of(char... array)
    {
        return new Factory.Characters(array, true);
    }

    static Array<Character> of(int length)
    {
        return new Factory.Characters(length);
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
