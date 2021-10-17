package frechsack.dev.util.array;

import frechsack.dev.util.Pair;
import frechsack.dev.util.route.BiIterable;
import frechsack.dev.util.route.BiIterator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.*;

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
public interface Array<E> extends Iterable<E>, BiIterable<E>, Function<Integer, E>, IntFunction<E>
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(type);
        return new ArrayFactory.GenericArray<>(length, type);
    }

    /**
     * Creates a new number Array with the specified elements. How the Array will proceed with the specified numbers depends on the specified classtype.
     *
     * @param type  The Array´s element classtype.
     * @param array The Array´s contents. The content may contain null elements.
     * @param <E>   The Array´s element´s classtype.
     * @return Returns the Array.
     */
    @SuppressWarnings("unchecked")
    static <E extends Number> Numbers<E> ofTypedNumber(Class<E> type, Number... array)
    {
        Numbers<? extends Number> arr;
        if (type.isPrimitive())
        {
            if (type.equals(Byte.TYPE))
            {
                arr = ofByte(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setByte(i, array[i] == null ? 0 : array[i].byteValue());
            }
            else if (type.equals(Short.TYPE))
            {
                arr = ofShort(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setShort(i, array[i] == null ? 0 : array[i].shortValue());
            }
            else if (type.equals(Integer.TYPE))
            {
                arr = ofInt(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setInt(i, array[i] == null ? 0 : array[i].intValue());
            }
            else if (type.equals(Float.TYPE))
            {
                arr = ofFloat(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setFloat(i, array[i] == null ? 0 : array[i].floatValue());
            }
            else if (type.equals(Double.TYPE))
            {
                arr = ofDouble(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setDouble(i, array[i] == null ? 0 : array[i].doubleValue());
            }
            else if (type.equals(Long.TYPE))
            {
                arr = ofLong(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setLong(i, array[i] == null ? 0 : array[i].longValue());
            }
            else throw new IllegalArgumentException("The classtype must be of type number");
        }
        else
        {
            if (type.equals(Byte.class))
            {
                arr = ofGenericByte(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setByte(i, array[i] == null ? 0 : array[i].byteValue());

            }
            else if (type.equals(Short.class))
            {
                arr = ofGenericShort(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setShort(i, array[i] == null ? 0 : array[i].shortValue());

            }
            else if (type.equals(Integer.class))
            {
                arr = ofGenericInt(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setInt(i, array[i] == null ? 0 : array[i].intValue());

            }
            else if (type.equals(Float.class))
            {
                arr = ofGenericFloat(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setFloat(i, array[i] == null ? 0 : array[i].floatValue());

            }
            else if (type.equals(Double.class))
            {
                arr = ofGenericDouble(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setDouble(i, array[i] == null ? 0 : array[i].doubleValue());

            }
            else if (type.equals(Long.class))
            {
                arr = ofGenericLong(array.length);
                for (int i = 0; i < arr.length(); i++) arr.setLong(i, array[i] == null ? 0 : array[i].longValue());

            }
            else if (type.equals(BigInteger.class))
            {
                Numbers<BigInteger> bigIntegers;
                Function<Number, BigInteger> converter = number ->
                {
                    if (number == null) return BigInteger.ZERO;
                    if (number instanceof BigInteger) return (BigInteger) number;
                    return BigInteger.valueOf(number.longValue());
                };
                bigIntegers = ofTypedNumber(array.length, BigInteger.class, converter);
                for (int i = 0; i < bigIntegers.length(); i++)
                {
                    bigIntegers.set(i, converter.apply(array[i]));
                }
                return (Numbers<E>) bigIntegers;
            }
            else if (type.equals(BigDecimal.class))
            {
                Numbers<BigDecimal> bigDecimals;
                Function<Number, BigDecimal> converter = number ->
                {
                    if (number == null) return BigDecimal.ZERO;
                    if (number instanceof BigDecimal) return (BigDecimal) number;
                    return BigDecimal.valueOf(number.doubleValue());
                };
                bigDecimals = ofTypedNumber(array.length, BigDecimal.class, converter);
                for (int i = 0; i < bigDecimals.length(); i++)
                {
                    bigDecimals.set(i, converter.apply(array[i]));
                }
                return (Numbers<E>) bigDecimals;
            }
            else throw new IllegalArgumentException("Unsupported classtype. Provide a custom converter by ofTypeNumber(Function<Number, E>,E... array)");
        }
        return (Numbers<E>) arr;
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
        Objects.requireNonNull(type);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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

    static Numbers<Integer> ofInt(int length, IntStream stream){
        Numbers<Integer> array = new ArrayFactory.IntArray(length);
        PrimitiveIterator.OfInt iterator = stream.iterator();
        int dI = 0;
        while (dI < array.length() && iterator.hasNext())
            array.setInt(dI++,iterator.nextInt());
        stream.close();
        return array;
    }

    static Numbers<Integer> ofGenericInt(int length, IntStream stream){
        Numbers<Integer> array = new ArrayFactory.GenericNumbers<>(length,Integer.class, number -> number == null ? 0 : number.intValue());
        PrimitiveIterator.OfInt iterator = stream.iterator();
        int dI = 0;
        while (dI < array.length() && iterator.hasNext())
            array.setInt(dI++,iterator.nextInt());
        stream.close();
        return array;
    }

    /**
     * Creates an Array of {@code Integer} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Integer> ofGenericInt(Integer... array)
    {
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
        return new ArrayFactory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number.intValue());
    }

    /**
     * Creates an Array of {@code Integer} with the specified length.
     *
     * @param length The Array´s length.
     * @return Returns the Array.
     */
    static Numbers<Integer> ofGenericInt(int length)
    {
        return new ArrayFactory.GenericNumbers<>(length, Integer.class, number -> number == null ? 0 : number.intValue());
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
        return new ArrayFactory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number.byteValue());
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
        return new ArrayFactory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number.shortValue());
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
        return new ArrayFactory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number.floatValue());
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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

    static Numbers<Double> ofDouble(int length, DoubleStream stream){
        Numbers<Double> array = new ArrayFactory.DoubleArray(length);
        PrimitiveIterator.OfDouble iterator = stream.iterator();
        int dI = 0;
        while (dI < array.length() && iterator.hasNext())
            array.setDouble(dI++,iterator.nextDouble());
        stream.close();
        return array;
    }

    static Numbers<Double> ofGenericDouble(int length, DoubleStream stream){
        Numbers<Double> array = new ArrayFactory.GenericNumbers<>(length,Double.class, number -> number == null ? 0 : number.doubleValue());
        PrimitiveIterator.OfDouble iterator = stream.iterator();
        int dI = 0;
        while (dI < array.length() && iterator.hasNext())
            array.setDouble(dI++,iterator.nextDouble());
        stream.close();
        return array;
    }

    /**
     * Creates an Array of {@code Double} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Double> ofGenericDouble(Double... array)
    {
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
        return new ArrayFactory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number.doubleValue());
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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

    static Numbers<Long> ofLong(int length, LongStream stream){
        Numbers<Long> array = new ArrayFactory.LongArray(length);
        PrimitiveIterator.OfLong iterator = stream.iterator();
        int dI = 0;
        while (dI < array.length() && iterator.hasNext())
            array.setLong(dI++,iterator.nextLong());
        stream.close();
        return array;
    }

    static Numbers<Long> ofGenericLong(int length, LongStream stream){
        Numbers<Long> array = new ArrayFactory.GenericNumbers<>(length,Long.class, number -> number == null ? 0 : number.longValue());
        PrimitiveIterator.OfLong iterator = stream.iterator();
        int dI = 0;
        while (dI < array.length() && iterator.hasNext())
            array.setLong(dI++,iterator.nextLong());
        stream.close();
        return array;
    }

    /**
     * Creates an Array of {@code Long} with the specified content. The returned Array will write through the specified array.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Numbers<Long> ofGenericLong(Long... array)
    {
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
        return new ArrayFactory.GenericNumbers<>(array, isReference, number -> number == null ? 0 : number.longValue());
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
     * @param isReference Indicates if the Array should write through the specified array. If set to zero, a reference is used.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Booleans ofBoolean(byte isReference, boolean... array)
    {
        Objects.requireNonNull(array);
        return new ArrayFactory.Booleans(array, isReference == 0);
    }

    /**
     * Creates an Array of {@code boolean} with the specified content. The returned Array will write through the specified array. The Array will use a primitive boolean model.
     *
     * @param array The content.
     * @return Returns the Array.
     */
    static Booleans ofBoolean(boolean... array)
    {
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
        return new ArrayFactory.GenericBooleans(array, true);
    }

    /**
     * Creates an Array of {@code Boolean} with the specified content.
     *
     * @param isReference Indicates if the Array should write through the specified array. If set to zero, a reference is used.
     * @param array       The content.
     * @return Returns the Array.
     */
    static Booleans ofGenericBoolean(byte isReference, Boolean... array)
    {
        Objects.requireNonNull(array);
        return new ArrayFactory.GenericBooleans(array, isReference == 0);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
        Objects.requireNonNull(array);
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
    default int indexOf(int start, int end, Object element)
    {
        for (int i = start; i < end; i++)
        {
            if (Objects.equals(element, get(i))) return i;
        }
        return -1;
    }

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
    default int indexOf(int start, int end, Predicate<E> predicate)
    {
        Objects.requireNonNull(predicate);
        for (int i = 0; i < length(); i++)
        {
            if (predicate.test(get(i))) return i;
        }
        return -1;
    }

    /**
     * Returns the last index of the specified element in this Array.
     *
     * @param element The value.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    default int lastIndexOf(Object element)
    {
        for (int i = length() - 1; i > 0; i--)
        {
            if (Objects.equals(element, get(i))) return i;
        }
        return -1;
    }

    /**
     * Returns the last index, where the specified {@link Predicate} returns true.
     *
     * @param predicate The predicate.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    default int lastIndexOf(Predicate<E> predicate)
    {
        Objects.requireNonNull(predicate);
        for (int i = length() - 1; i > 0; i--)
        {
            if (predicate.test(get(i))) return i;
        }
        return -1;
    }

    /**
     * Returns the first index of the specified element in this Array.
     *
     * @param element The value.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    default int firstIndexOf(Object element)
    {
        for (int i = 0; i < length(); i++)
        {
            if (Objects.equals(element, get(i))) return i;
        }
        return -1;
    }

    /**
     * Returns the first index, where the specified {@link Predicate} returns true.
     *
     * @param predicate The predicate.
     * @return Returns an index, that matches the given value. If the value is not present in this Array -1 is returned.
     */
    default int firstIndexOf(Predicate<E> predicate)
    {
        Objects.requireNonNull(predicate);
        for (int i = 0; i < length(); i++)
        {
            if (predicate.test(get(i))) return i;
        }
        return -1;
    }

    /**
     * Try's to set any value in this Array to a default value.
     * In case this Array contains primitive types, they will be set to their default value otherwise they will be set to null.
     *
     * @implNote The default implementation will set any element in this Array to null.
     */
    default void clear()
    {
        for (int i = 0; i < length(); i++)
        {
            set(i, null);
        }
    }

    /**
     * Checks if this Array contains the specified element.
     *
     * @param element The element.
     * @return Returns true if this Array contains the specified element, else false.
     */
    default boolean contains(Object element)
    {
        for (int i = 0; i < length(); i++) if (Objects.equals(element, get(i))) return true;
        return false;
    }

    /**
     * Returns a copy of this Array´s element.
     *
     * @return Returns a copy with the same size and content as this Array.
     * @implNote The default implementation will create an clone with {@link #generator()} and then copy any element by reference.
     */
    default E[] toArray()
    {
        E[] clone = generator().apply(length());
        for (int i = 0; i < length(); i++)
        {
            clone[i] = get(i);
        }
        return clone;
    }

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
    default Stream<Pair<Integer, E>> streamIndices()
    {
        return IntStream.range(0, length()).mapToObj(index -> Pair.of(index, get(index)));
    }

    /**
     * Creates an Index based parallel {@link Stream}. Each index in relation to it´s current element will be streamed in a {@link Pair}.
     *
     * @return Returns a Stream.
     */
    default Stream<Pair<Integer, E>> parallelStreamIndices()
    {
        return IntStream.range(0, length()).parallel().mapToObj(index -> Pair.of(index, get(index)));
    }

    /**
     * Creates a {@link Stream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    default Stream<E> stream()
    {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Creates a parallel {@link Stream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    default Stream<E> parallelStream()
    {
        return StreamSupport.stream(spliterator(), true);
    }

    /**
     * Checks if this Array´s elements are primitives.
     *
     * @return Returns true if this Array contains primitives, else false.
     */
    boolean isPrimitive();

    @Override
    default Iterator<E> iterator()
    {
        return new ArrayFactory.ArrayIterator<>(this);
    }

    @Override
    default BiIterator<E> biIterator()
    {
        return BiIterator.of(this::get, length(), i -> set(i, null));
    }

    /**
     * Creates an {@link IntFunction} that creates an generic array of this Array´s type.
     *
     * @return Returns a generator.
     */
    IntFunction<E[]> generator();

    @Override
    default Spliterator<E> spliterator()
    {
        return Spliterators.spliterator(iterator(), length(), Spliterator.SIZED | Spliterator.SUBSIZED);
    }

    /**
     * Checks if this Array is equal to the specified array. They are considered as equal, when they contains the same elements in the same order.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(E[] array)
    {
        if (array == null) return false;
        if (array.length != length()) return false;
        for (int i = 0; i < length(); i++) if (!Objects.equals(array[i], get(i))) return false;
        return true;
    }

    /**
     * Returns a {@link List} that wraps this Array. Modifications in the returned List will be reflected in this Array.
     *
     * @return Returns a List.
     */
    default List<E> asList()
    {
        return new ArrayFactory.ArrayList<>(this);
    }

    /**
     * Sorts this Array with the given {@link Comparator}.
     *
     * @param c The Comparator.
     * @implNote The default implementation will sort a copy of this Array and then reassign any element in it to this Array.
     */
    default void sort(Comparator<? super E> c)
    {
        Objects.requireNonNull(c);
        E[] array = toArray();
        Arrays.sort(array, c);
        for (int i = 0; i < length(); i++)
        {
            set(i, array[i]);
        }
    }

    /**
     * Replaces any position in this Array with the specified element.
     *
     * @param element The element.
     */
    default void fill(E element)
    {
        for (int i = 0; i < length(); i++) set(i, element);
    }

    /**
     * Sorts this Array in the opposite order.  This Array´s elements have to implement {@link Comparable}.
     */
    default void sortReverse()
    {
        sort();
        reverse();
    }

    /**
     * Sorts this Array with the given {@link Comparator} in the opposite order.
     *
     * @param c The Comparator.
     */
    default void sortReverse(Comparator<? super E> c)
    {
        sort(c);
        reverse();
    }

    /**
     * Changes the order of this Array. The previous first element will now equal the last element.
     */
    default void reverse()
    {
        int swapIndex;
        for (int i = 0; i < length() / 2; i++)
        {
            swapIndex = length() - 1 - i;
            set(i, getAndSet(swapIndex, get(i)));
        }
    }

    /**
     * Rotates the elements in this Array by the specified distance.
     *
     * @param distance The distance. The elements current position will differ by the specified distance after the call.
     * @implNote To rotate the elements in This Array, {@link Collections#rotate(List, int)} is used, to take advantage of the default optimizations.
     */
    default void rotate(int distance)
    {
        Collections.rotate(asList(), distance * -1);
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

    @Override
    default void forEach(Consumer<? super E> action)
    {
        Objects.requireNonNull(action);
        for (int i = 0; i < length(); i++) action.accept(get(i));
    }

    /**
     * Sorts this Array. This Array´s elements have to implement {@link Comparable}.
     *
     * @implNote The default implementation will sort a copy of this Array and then reassign any element in it to this Array.
     */
    default void sort()
    {
        E[] array = toArray();
        Arrays.sort(array);
        for (int i = 0; i < length(); i++)
        {
            set(i, array[i]);
        }
    }

    /**
     * A SubArray will reflect any operations on it into this Array.
     * Basically a SubArray is a view of a certain range in this Array.
     * The length of the SubArray will be equal to {@code toIndex - fromIndex}.
     *
     * @param fromIndex The inclusive first index.
     * @param toIndex   The exclusive last index.
     * @return Returns a view of this Array with the specified range.
     */
    default Array<E> subArray(int fromIndex, int toIndex)
    {
        Objects.checkFromToIndex(fromIndex, toIndex, length());
        return new ArrayFactory.SubArray<>(this, fromIndex, toIndex);
    }

    /**
     * Creates a new Array, that retains as many elements as possible from this Array.
     * @param length The length of the Array.
     * @return Returns a new Array.
     */
    @SuppressWarnings("unchecked")
    default Array<E> resized(int length){
        // Create a new instance of this guy
        Array<E> resized = (Array<E>) Array.of(length, asArray().getClass().getComponentType());
        for (int i = 0; i < length && i < this.length(); i++) resized.set(i,get(i));
        return resized;
    }

    /**
     * Fills the specified Array with as many elements as possible with this Array´s contents.
     * @param array The target array.
     * @param <V> The target array´s type.
     * @return Return the input array.
     */
    default <V extends Array<? super E>> V copyTo(V array){
        for (int i = 0; i < length() && i < array.length(); i++)  array.set(i,get(i));
        return array;
    }

    default Array<E> copy(){
        return this.resized(length());
    }

    default Array<E> append(int arraySize,Stream<? extends E> stream){
        return combine(this,arraySize,stream);
    }

    static <E> Array<E> combine(Array<E> array,int arraySize, Stream<? extends E> stream){
        Objects.checkIndex(array.length()-1,arraySize);
        Array<E>  destination = array.resized(arraySize);
        int dI = array.length();
        Iterator<? extends E> iterator = stream.iterator();
        while (dI < destination.length() && iterator.hasNext())
            destination.set(dI++,iterator.next());
        stream.close();
        return destination;
    }

    static <E extends Number> Numbers<E> combine(Numbers<E> array,int arraySize, Stream<? extends E> stream){
        Objects.checkIndex(array.length()-1,arraySize);
        Numbers<E>  destination = array.resized(arraySize);
        int dI = array.length();
        Iterator<? extends E> iterator = stream.iterator();
        while (dI < destination.length() && iterator.hasNext())
            destination.set(dI++,iterator.next());
        stream.close();
        return destination;
    }

    static <E extends Number> Numbers<E> combine(Numbers<E> array,int arraySize, IntStream stream){
        Objects.checkIndex(array.length()-1,arraySize);
        Numbers<E>  destination = array.resized(arraySize);
        int dI = array.length();
        PrimitiveIterator.OfInt iterator= stream.iterator();
        while (dI < destination.length() && iterator.hasNext())
            destination.setInt(dI++,iterator.nextInt());
        stream.close();
        return destination;
    }

    static <E extends Number> Numbers<E> combine(Numbers<E> array,int arraySize, DoubleStream stream){
        Objects.checkIndex(array.length()-1,arraySize);
        Numbers<E>  destination = array.resized(arraySize);
        int dI = array.length();
        PrimitiveIterator.OfDouble iterator= stream.iterator();
        while (dI < destination.length() && iterator.hasNext())
            destination.setDouble(dI++,iterator.nextDouble());
        stream.close();
        return destination;
    }

    static <E extends Number> Numbers<E> combine( Numbers<E> array,int arraySize, LongStream stream){
        Objects.checkIndex(array.length()-1,arraySize);
        Numbers<E>  destination = array.resized(arraySize);
        int dI = array.length();
        PrimitiveIterator.OfLong iterator= stream.iterator();
        while (dI < destination.length() && iterator.hasNext())
            destination.setDouble(dI++,iterator.nextLong());
        stream.close();
        return destination;
    }


}
