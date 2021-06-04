package com.frechsack.dev.util.array;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Numbers are a numeric implementation of an {@link Array}.
 * <p>
 * It allows access to primitive read and write operations and {@link IntStream}.
 *
 * @param <E> The class type of this Array. This may be an Object or primitive type.
 * @author Frechsack
 */
public interface Numbers<E extends Number> extends Array<E>
{
    /**
     * Returns the element on the specified index as a byte.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    byte getByte(int index);

    /**
     * Returns the element on the specified index as a short.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    short getShort(int index);

    /**
     * Returns the element on the specified index as an int.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    int getInt(int index);

    /**
     * Returns the element on the specified index as a float.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    float getFloat(int index);

    /**
     * Returns the element on the specified index as a double.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    double getDouble(int index);

    /**
     * Returns the element on the specified index as a long.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    long getLong(int index);

    /**
     * Sets the specified byte on the given position. It depends on the implementation if a byte can be set. If not the element will be down-casted.
     *
     * @param index   The position.
     * @param element The element.
     */
    void setByte(int index, byte element);

    /**
     * Sets the specified short on the given position. It depends on the implementation if a short can be set. If not the element will be down-casted.
     *
     * @param index   The position.
     * @param element The element.
     */
    void setShort(int index, short element);

    /**
     * Sets the specified int on the given position. It depends on the implementation if an int can be set. If not the element will be down-casted.
     *
     * @param index   The position.
     * @param element The element.
     */
    void setInt(int index, int element);

    /**
     * Sets the specified float on the given position. It depends on the implementation if a float can be set. If not the element will be down-casted.
     *
     * @param index   The position.
     * @param element The element.
     */
    void setFloat(int index, float element);

    /**
     * Sets the specified double on the given position. It depends on the implementation if a double can be set. If not the element will be down-casted.
     *
     * @param index   The position.
     * @param element The element.
     */
    void setDouble(int index, double element);

    /**
     * Sets the specified long on the given position. It depends on the implementation if a long can be set. If not the element will be down-casted.
     *
     * @param index   The position.
     * @param element The element.
     */
    void setLong(int index, long element);

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default byte getAndSetByte(int index, byte element)
    {
        byte last = getByte(index);
        setByte(index, element);
        return last;
    }

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default short getAndSetShort(int index, short element)
    {
        short last = getShort(index);
        setShort(index, element);
        return last;
    }

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default int getAndSetInt(int index, int element)
    {
        int last = getInt(index);
        setInt(index, element);
        return last;
    }

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default float getAndSetFloat(int index, float element)
    {
        float last = getFloat(index);
        setFloat(index, element);
        return last;
    }

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default double getAndSetDouble(int index, double element)
    {
        double last = getDouble(index);
        setDouble(index, element);
        return last;
    }

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default long getAndSetLong(int index, long element)
    {
        long last = getLong(index);
        setLong(index, element);
        return last;
    }

    /**
     * Returns a copy of this Array´s elements as bytes.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    byte[] toByteArray();

    /**
     * Returns a copy of this Array´s elements as shorts.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    short[] toShortArray();

    /**
     * Returns a copy of this Array´s elements as integers.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    int[] toIntArray();

    /**
     * Returns a copy of this Array´s elements as doubles.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    double[] toDoubleArray();

    /**
     * Returns a copy of this Array´s elements as longs.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    long[] toLongArray();

    /**
     * Returns a copy of this Array´s elements as floats.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    float[] toFloatArray();

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    boolean equals(int[] array);

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    boolean equals(float[] array);

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    boolean equals(double[] array);

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    boolean equals(long[] array);

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    boolean equals(short[] array);

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    boolean equals(byte[] array);

    /**
     * Creates an {@link IntStream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    IntStream intStream();
    /**
     * Creates a {@link DoubleStream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    DoubleStream doubleStream();
    /**
     * Creates a {@link LongStream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    LongStream longStream();
}
