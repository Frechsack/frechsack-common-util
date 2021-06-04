package com.frechsack.dev.util.array;

import com.frechsack.dev.util.Pair;
import com.frechsack.dev.util.route.Routable;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * An Array behaves like a native array.
 * It allows typical read and write operations by {@link #get(int)} and {@link #set(int, Object)}.
 * The Array is fixed sized, the length can be obtained by {@link #length()}.
 * <p>
 * This Object allows various stream, iterator and sort operations.
 * <p>
 * Typical an Array is backed by a native array. Direct access to the underlying array can be obtained by {@link #asArray()}.
 * <p>
 * To be compatible with {@link java.util.Collection} an Array allows to wrap itself into a {@link List} by {@link #asList()}.
 * The returned List allows modification of this Array.
 * <p>
 * An Array is considered as equal, when it´s elements are equal to a second array in the same order.
 * <p>
 * This Object acts like a bridge between the Java-Collections-Framework and native arrays.
 * <p>
 * This interface provides static factories to create an Array of any class-type.
 *
 * @param <E> The class type of this Array. This may be an Object or primitive type.
 * @author Frechsack
 */
public interface Array<E> extends Iterable<E>, Routable<E>, Function<Integer, E>, IntFunction<E>
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

    /**
     * Returns the index of the specified element in this Array.
     * It is not granted, that the specified index is the first one that matches the given value.
     *
     * @param element The value.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    default int indexOf(Object element)
    {
        return indexOf(0, length(), element);
    }

    /**
     * Returns the index of the specified element in this Array.
     * It is not granted, that the specified index is the first one that matches the given value.
     *
     * @param start   The first inclusive index, where the search will start.
     * @param element The value.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    default int indexOf(int start, Object element)
    {
        return indexOf(start, length(), element);
    }

    /**
     * Returns the index of the specified element in this Array.
     * It is not granted, that the specified index is the first one that matches the given value.
     *
     * @param start   The first inclusive index, where the search will start.
     * @param end     The last exclusive index, where the search will end.
     * @param element The value.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    int indexOf(int start, int end, Object element);

    /**
     * Returns an index, where the specified {@link Predicate} returns true.
     * It is not granted, that the specified index is the first one that matches the given value.
     *
     * @param predicate The predicate.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    default int indexOf(Predicate<E> predicate)
    {
        return indexOf(0, length(), predicate);
    }

    /**
     * Returns an index, where the specified {@link Predicate} returns true.
     * It is not granted, that the specified index is the first one that matches the given value.
     *
     * @param start     The first inclusive index, where the search will start.
     * @param predicate The predicate.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    default int indexOf(int start, Predicate<E> predicate)
    {
        return indexOf(start, length(), predicate);
    }

    /**
     * Returns an index, where the specified {@link Predicate} returns true.
     * It is not granted, that the specified index is the first one that matches the given value.
     *
     * @param start     The first inclusive index, where the search will start.
     * @param end       The last exclusive index, where the search will end.
     * @param predicate The predicate.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    int indexOf(int start, int end, Predicate<E> predicate);

    /**
     * Returns the last index of the specified element in this Array.
     *
     * @param element The value.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    int lastIndexOf(Object element);

    /**
     * Returns the last index, where the specified {@link Predicate} returns true.
     *
     * @param predicate The predicate.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    int lastIndexOf(Predicate<E> predicate);

    /**
     * Returns the first index of the specified element in this Array.
     *
     * @param element The value.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    int firstIndexOf(Object element);

    /**
     * Returns the first index, where the specified {@link Predicate} returns true.
     *
     * @param predicate The predicate.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    int firstIndexOf(Predicate<E> predicate);

    /**
     * Try's to set any value in this Array to a default value. In case this Array contains primitive types, they will be set to their default value otherwise they will be set to null.
     */
    void clear();

    /**
     * Checks if this Array contains the specified element.
     *
     * @param element The element.
     * @return Returns true if this Array contains the specified element, else false.
     */
    boolean contains(Object element);

    /**
     * Returns a copy of this Array´s element.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    E[] toArray();

    /**
     * Returns the underlying native array of this Array. Changes in the returned array will be reflected in this Object.
     *
     * @return Returns the underlying native array.
     */
    Object asArray();

    /**
     * Returns the length of this Array.
     *
     * @return Returns the length.
     */
    int length();

    /**
     * Returns the element on the specified index.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    E get(int index);

    /**
     * Sets the specified element on the given position.
     *
     * @param index   The position.
     * @param element The element.
     */
    void set(int index, E element);

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default E getAndSet(int index, E element)
    {
        E last = get(index);
        set(index, element);
        return last;
    }

    /**
     * Applies the specified {@link Function} to any position in this Array.
     *
     * @param mapper The transformation Function.
     */
    default void transform(Function<E, E> mapper)
    {
        for (int i = 0; i < length(); i++) set(i, mapper.apply(get(i)));
    }

    /**
     * Creates an Index based {@link Stream}. Each index in relation to it´s current element will be streamed in a {@link Pair}.
     *
     * @return Returns a Stream.
     */
    Stream<Pair<Integer, E>> streamIndices();

    /**
     * Creates an Index based parallel {@link Stream}. Each index in relation to it´s current element will be streamed in a {@link Pair}.
     *
     * @return Returns a Stream.
     */
    Stream<Pair<Integer, E>> parallelStreamIndices();

    /**
     * Creates a {@link Stream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    Stream<E> stream();

    /**
     * Creates a parallel {@link Stream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    Stream<E> parallelStream();

    /**
     * Checks if this Array´s elements are primitives.
     *
     * @return Returns true if this Array contains primitives, else false.
     */
    boolean isPrimitive();

    /**
     * Creates an {@link IntFunction} that creates an generic array of this Array´s type.
     *
     * @return Returns a generator.
     */
    IntFunction<E[]> generator();

    @Override
    Spliterator<E> spliterator();

    /**
     * Checks if this Array is equal to the specified array. They are considered as equal, when they contains the same elements in the same order.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    boolean equals(E[] array);

    /**
     * Returns a {@link List} that wraps this Array. Modifications in the returned List will be reflected in this Array.
     *
     * @return Returns a List.
     */
    List<E> asList();

    /**
     * Sorts this Array with the given {@link Comparator}.
     *
     * @param c The Comparator.
     */
    void sort(Comparator<? super E> c);

    /**
     * Replaces any position in this Array with the specified element.
     *
     * @param element The element.
     */
    void fill(E element);

    /**
     * Sorts this Array in the opposite order.  This Array´s elements have to implement {@link Comparable}.
     */
    void sortReverse();

    /**
     * Sorts this Array with the given {@link Comparator} in the opposite order.
     *
     * @param c The Comparator.
     */
    void sortReverse(Comparator<? super E> c);

    /**
     * Changes the order of this Array. The previous first element will now equal the last element.
     */
    void reverse();

    /**
     * Rotates the elements in this Array by the specified distance.
     *
     * @param distance The distance. The elements current position will differ by the specified distance after the call.
     * @implNote To rotate the elements in This Array, {@link Collections#rotate(List, int)} is used, to take advantage of the default optimizations.
     */
    default void rotate(int distance)
    {
        Collections.rotate(asList(), distance);
    }

    @Override
    default E apply(Integer index)
    {
        return get(index == null ? 0 : index);
    }


    @Override
    default E apply(int index){
        return get(index);
    }

    /**
     * Sorts this Array. This Array´s elements have to implement {@link Comparable}.
     */
    void sort();
}
