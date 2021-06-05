package com.frechsack.dev.util.array;

import com.frechsack.dev.util.Pair;
import com.frechsack.dev.util.route.Routable;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
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
 * @param <E> The element type of this Array. This may be an Object or primitive type.
 * @author Frechsack
 */
public interface Array<E> extends Iterable<E>, Routable<E>, Function<Integer, E>, IntFunction<E>
{
    /**
     * Creates a new Array with the specified elements. The returned Array will write through the specified array.
     *
     * @param array The elements.
     * @param <E>   The Array´s element type.
     * @return Returns the Array.
     */
    @SafeVarargs
    static <E> Array<E> of(E... array)
    {
        return new ArrayFactory.GenericArray<>(array, true);

    }

    /**
     * Creates a new Array with the specified elements.
     *
     * @param isReference Indicates if the Array should write through the specified elements.
     * @param array       The elements.
     * @param <E>         The Array´s element type.
     * @return Returns the Array.
     */
    @SafeVarargs
    static <E> Array<E> of(boolean isReference, E... array)
    {
        return new ArrayFactory.GenericArray<>(array, isReference);
    }

    /**
     * Creates a new Array with the specified class type and length.
     *
     * @param length The Array´s length.
     * @param type   The Array´s class type.
     * @param <E>    The Array´s class type.
     * @return Returns the Array.
     */
    static <E> Array<E> of(int length, Class<E> type)
    {
        return new ArrayFactory.GenericArray<>(length, type);
    }

    /**
     * Creates a new number Array with the specified elements. The returned Array will write through the specified array.
     *
     * @param array     The content.
     * @param converter The function will convert {@link Number} to a type of {@code E}. When {@code null} is passed, the function must return a default value that is equal to {@code 0}.
     * @param <E>       The Array´s element type.
     * @return Returns the Array.
     */
    @SafeVarargs
    static <E extends Number> Numbers<E> ofTypedNumber(Function<Number, E> converter, E... array)
    {
        checkNumberConverter(converter);
        return new ArrayFactory.GenericNumbers<>(array, true, converter);
    }

    /**
     * Creates a new number Array with the specified elements.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param converter   The function will convert {@link Number} to a type of {@code E}. When {@code null} is passed, the function must return a default value that is equal to {@code 0}.
     * @param array       The content.
     * @param <E>         The Array´s element type.
     * @return Returns the Array.
     */
    @SafeVarargs
    static <E extends Number> Numbers<E> ofTypedNumber(boolean isReference, Function<Number, E> converter, E... array)
    {
        checkNumberConverter(converter);
        return new ArrayFactory.GenericNumbers<>(array, isReference, converter);
    }

    /**
     * Creates a new number Array with the specified length.
     *
     * @param length    The Array´s length.
     * @param type      The Array´s element type.
     * @param converter The function will convert {@link Number} to a type of {@code E}. When {@code null} is passed, the function must return a default value that is equal to {@code 0}.
     * @param <E>       The Array´s element type.
     * @return Returns the Array.
     */
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

    /**
     * Creates an Array of {@code Number} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Number> ofNumber(boolean isReference, Number... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number);
    }

    /**
     * Creates an Array of {@code Number} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Number> ofNumber(Number... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number);
    }

    /**
     * Creates an Array of {@code Number} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Number> ofNumber(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Number.class, number -> number == null ? 0 : number);
    }

    /**
     * Creates an Array of {@code int} with the specified content. The Array will use a primitive int model.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Integer> ofInt(boolean isReference, int... array)
    {
        return new ArrayFactory.IntArray(array, isReference);
    }

    /**
     * Creates an Array of {@code int} with the specified content. The returned Array will write through the specified array. The Array will use a primitive int model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Integer> ofInt(int... array)
    {
        return new ArrayFactory.IntArray(array, true);
    }

    /**
     * Creates an Array of {@code int} with the specified length. The Array will use a primitive int model.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Integer> ofInt(int length)
    {
        return new ArrayFactory.IntArray(length);
    }

    /**
     * Creates an Array of {@code Integer} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Integer> ofGenericInt(Integer... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.intValue());
    }

    /**
     * Creates an Array of {@code Integer} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Integer> ofGenericInt(boolean isReference, Integer... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.intValue());
    }

    /**
     * Creates an Array of {@code Integer} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Integer> ofGenericInt(int length)
    {
        return new ArrayFactory.GenericNumbers<Integer>(length, Integer.class, number -> number == null ? 0 : number.intValue());
    }

    /**
     * Creates an Array of {@code byte} with the specified content. The Array will use a primitive byte model.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Byte> ofByte(boolean isReference, byte... array)
    {
        return new ArrayFactory.ByteArray(array, isReference);
    }

    /**
     * Creates an Array of {@code byte} with the specified content. The returned Array will write through the specified array. The Array will use a primitive byte model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Byte> ofByte(byte... array)
    {
        return new ArrayFactory.ByteArray(array, true);
    }

    /**
     * Creates an Array of {@code byte} with the specified length. The Array will use a primitive byte model.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Byte> ofByte(int length)
    {
        return new ArrayFactory.ByteArray(length);
    }

    /**
     * Creates an Array of {@code Byte} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Byte> ofGenericByte(Byte... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.byteValue());
    }

    /**
     * Creates an Array of {@code Byte} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Byte> ofGenericByte(boolean isReference, Byte... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.byteValue());
    }

    /**
     * Creates an Array of {@code Byte} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Byte> ofGenericByte(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Byte.class, number -> number == null ? 0 : number.byteValue());
    }

    /**
     * Creates an Array of {@code short} with the specified content. The Array will use a primitive short model.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Short> ofShort(boolean isReference, short... array)
    {
        return new ArrayFactory.ShortArray(array, isReference);
    }

    /**
     * Creates an Array of {@code short} with the specified content. The returned Array will write through the specified array. The Array will use a primitive short model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Short> ofShort(short... array)
    {
        return new ArrayFactory.ShortArray(array, true);
    }

    /**
     * Creates an Array of {@code short} with the specified length. The Array will use a primitive short model.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Short> ofShort(int length)
    {
        return new ArrayFactory.ShortArray(length);
    }

    /**
     * Creates an Array of {@code Short} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Short> ofGenericShort(Short... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.shortValue());
    }

    /**
     * Creates an Array of {@code Short} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Short> ofGenericShort(boolean isReference, Short... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.shortValue());
    }

    /**
     * Creates an Array of {@code Short} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Short> ofGenericShort(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Short.class, number -> number == null ? 0 : number.shortValue());
    }

    /**
     * Creates an Array of {@code float} with the specified content. The Array will use a primitive float model.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Float> ofFloat(boolean isReference, float... array)
    {
        return new ArrayFactory.FloatArray(array, isReference);
    }

    /**
     * Creates an Array of {@code float} with the specified content. The returned Array will write through the specified array. The Array will use a primitive float model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Float> ofFloat(float... array)
    {
        return new ArrayFactory.FloatArray(array, true);
    }

    /**
     * Creates an Array of {@code float} with the specified length. The Array will use a primitive float model.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Float> ofFloat(int length)
    {
        return new ArrayFactory.FloatArray(length);
    }

    /**
     * Creates an Array of {@code Float} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Float> ofGenericFloat(Float... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.floatValue());
    }

    /**
     * Creates an Array of {@code Float} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Float> ofGenericFloat(boolean isReference, Float... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.floatValue());
    }

    /**
     * Creates an Array of {@code Float} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Float> ofGenericFloat(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Float.class, number -> number == null ? 0 : number.floatValue());
    }

    /**
     * Creates an Array of {@code double} with the specified content. The Array will use a primitive double model.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Double> ofDouble(boolean isReference, double... array)
    {
        return new ArrayFactory.DoubleArray(array, isReference);
    }

    /**
     * Creates an Array of {@code double} with the specified content. The returned Array will write through the specified array. The Array will use a primitive double model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Double> ofDouble(double... array)
    {
        return new ArrayFactory.DoubleArray(array, true);
    }

    /**
     * Creates an Array of {@code double} with the specified length. The Array will use a primitive double model.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Double> ofDouble(int length)
    {
        return new ArrayFactory.DoubleArray(length);
    }

    /**
     * Creates an Array of {@code Double} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Double> ofGenericDouble(Double... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.doubleValue());
    }

    /**
     * Creates an Array of {@code Double} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Double> ofGenericDouble(boolean isReference, Double... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.doubleValue());
    }

    /**
     * Creates an Array of {@code Double} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Double> ofGenericDouble(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Double.class, number -> number == null ? 0 : number.doubleValue());
    }

    /**
     * Creates an Array of {@code long} with the specified content. The Array will use a primitive long model.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Long> ofLong(boolean isReference, long... array)
    {
        return new ArrayFactory.LongArray(array, isReference);
    }

    /**
     * Creates an Array of {@code long} with the specified content. The returned Array will write through the specified array. The Array will use a primitive long model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Long> ofLong(long... array)
    {
        return new ArrayFactory.LongArray(array, true);
    }

    /**
     * Creates an Array of {@code long} with the specified length. The Array will use a primitive long model.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Long> ofLong(int length)
    {
        return new ArrayFactory.LongArray(length);
    }

    /**
     * Creates an Array of {@code Long} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Long> ofGenericLong(Long... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.longValue());
    }

    /**
     * Creates an Array of {@code Long} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Numbers<Long> ofGenericLong(boolean isReference, Long... array)
    {
        return new ArrayFactory.GenericNumbers<>(array, true, number -> number == null ? 0 : number.longValue());
    }

    /**
     * Creates an Array of {@code Long} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Long> ofGenericLong(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Long.class, number -> number == null ? 0 : number.longValue());
    }

    /**
     * Creates an Array of {@code boolean} with the specified content. The Array will use a primitive boolean model.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Booleans ofBoolean(boolean isReference, boolean... array)
    {
        return new ArrayFactory.Booleans(array, isReference);
    }

    /**
     * Creates an Array of {@code boolean} with the specified content. The returned Array will write through the specified array. The Array will use a primitive boolean model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Booleans ofBoolean(boolean... array)
    {
        return new ArrayFactory.Booleans(array, true);
    }

    /**
     * Creates an Array of {@code boolean} with the specified length. The Array will use a primitive boolean model.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Booleans ofBoolean(int length)
    {
        return new ArrayFactory.Booleans(length);
    }

    /**
     * Creates an Array of {@code Boolean} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Booleans ofGenericBoolean(Boolean... array)
    {
        return new ArrayFactory.GenericBooleans(array, true);
    }

    /**
     * Creates an Array of {@code Boolean} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Booleans ofGenericBoolean(boolean isReference, Boolean... array)
    {
        return new ArrayFactory.GenericBooleans(array, isReference);
    }

    /**
     * Creates an Array of {@code Boolean} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Booleans ofGenericBoolean(int length)
    {
        return new ArrayFactory.GenericBooleans(length);
    }

    /**
     * Creates an Array of {@code char} with the specified content. The Array will use a primitive char model.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Characters ofChar(boolean isReference, char... array)
    {
        return new ArrayFactory.Characters(array, isReference);
    }

    /**
     * Creates an Array of {@code char} with the specified content. The returned Array will write through the specified array. The Array will use a primitive char model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Characters ofChar(char... array)
    {
        return new ArrayFactory.Characters(Objects.requireNonNull(array), true);
    }

    /**
     * Creates an Array of {@code char} with the specified length. The Array will use a primitive char model.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Characters ofChar(int length)
    {
        return new ArrayFactory.Characters(length);
    }

    /**
     * Creates an Array of {@code Character} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Characters ofGenericChar(Character... array)
    {
        return new ArrayFactory.GenericCharacters(array, true);
    }

    /**
     * Creates an Array of {@code Character} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Characters ofGenericChar(boolean isReference, Character... array)
    {
        return new ArrayFactory.GenericCharacters(array, isReference);
    }

    /**
     * Creates an Array of {@code Character} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
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
    default E apply(int index)
    {
        return get(index);
    }

    /**
     * Sorts this Array. This Array´s elements have to implement {@link Comparable}.
     */
    void sort();
}
