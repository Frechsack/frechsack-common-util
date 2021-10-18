package frechsack.dev.util.array;

import java.util.Objects;
import java.util.PrimitiveIterator;
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
    default byte getByte(int index)
    {
        Number n = get(index);
        return n == null ? 0 : n.byteValue();
    }

    /**
     * Returns the element on the specified index as a short.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    default short getShort(int index)
    {
        Number n = get(index);
        return n == null ? 0 : n.shortValue();
    }

    /**
     * Returns the element on the specified index as an int.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    default int getInt(int index)
    {
        Number n = get(index);
        return n == null ? 0 : n.intValue();
    }

    /**
     * Returns the element on the specified index as a float.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    default float getFloat(int index)
    {
        Number n = get(index);
        return n == null ? 0 : n.floatValue();
    }

    /**
     * Returns the element on the specified index as a double.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    default double getDouble(int index)
    {
        Number n = get(index);
        return n == null ? 0 : n.doubleValue();
    }

    /**
     * Returns the element on the specified index as a long.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    default long getLong(int index)
    {
        Number n = get(index);
        return n == null ? 0 : n.longValue();
    }

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
    default byte[] toByteArray()
    {
        byte[] clone = new byte[length()];
        for (int i = 0; i < length(); i++)
        {
            clone[i] = getByte(i);
        }
        return clone;
    }

    /**
     * Returns a copy of this Array´s elements as shorts.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    default short[] toShortArray()
    {
        short[] clone = new short[length()];
        for (int i = 0; i < length(); i++)
        {
            clone[i] = getShort(i);
        }
        return clone;
    }

    /**
     * Returns a copy of this Array´s elements as integers.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    default int[] toIntArray()
    {
        int[] clone = new int[length()];
        for (int i = 0; i < length(); i++)
        {
            clone[i] = getInt(i);
        }
        return clone;
    }

    /**
     * Returns a copy of this Array´s elements as doubles.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    default double[] toDoubleArray()
    {
        double[] clone = new double[length()];
        for (int i = 0; i < length(); i++)
        {
            clone[i] = getDouble(i);
        }
        return clone;
    }

    /**
     * Returns a copy of this Array´s elements as longs.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    default long[] toLongArray()
    {
        long[] clone = new long[length()];
        for (int i = 0; i < length(); i++)
        {
            clone[i] = getLong(i);
        }
        return clone;
    }

    /**
     * Returns a copy of this Array´s elements as floats.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    default float[] toFloatArray()
    {
        float[] clone = new float[length()];
        for (int i = 0; i < length(); i++)
        {
            clone[i] = getFloat(i);
        }
        return clone;
    }

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(int[] array)
    {
        if (array == null) return false;
        if (array.length != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if (array[i] != getInt(i)) return false;
        }
        return true;
    }

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(float[] array)
    {

        if (array == null) return false;
        if (array.length != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if (array[i] != getFloat(i)) return false;
        }
        return true;
    }

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(double[] array)
    {
        if (array == null) return false;
        if (array.length != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if (array[i] != getDouble(i)) return false;
        }
        return true;
    }

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(long[] array)
    {
        if (array == null) return false;
        if (array.length != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if (array[i] != getLong(i)) return false;
        }
        return true;
    }

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(short[] array)
    {
        if (array == null) return false;
        if (array.length != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if (array[i] != getShort(i)) return false;
        }
        return true;
    }

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(byte[] array)
    {
        if (array == null) return false;
        if (array.length != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if (array[i] != getByte(i)) return false;
        }
        return true;
    }

    @Override
    default Numbers<E> subArray(int fromIndex, int toIndex)
    {
        Objects.checkFromToIndex(fromIndex, toIndex, length());
        return new ArrayFactory.SubNumbers<>(this, fromIndex, toIndex);
    }

    /**
     * Creates an {@link IntStream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    default IntStream intStream()
    {
        return IntStream.range(0, length()).map(this::getInt);
    }

    /**
     * Creates a {@link DoubleStream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    default DoubleStream doubleStream()
    {
        return IntStream.range(0, length()).mapToDouble(this::getDouble);
    }

    /**
     * Creates a {@link LongStream} with the whole content of this Array.
     *
     * @return Returns a Stream.
     */
    default LongStream longStream()
    {
        return IntStream.range(0, length()).mapToLong(this::getLong);
    }

    @Override
    Numbers<E> resized(int length);

    default Numbers<E> append(int arraySize, IntStream stream)
    {
        return Array.combine(this, arraySize, stream);
    }

    default Numbers<E> append(int arraySize, DoubleStream stream)
    {
        return Array.combine(this, arraySize, stream);
    }

    default Numbers<E> append(int arraySize, LongStream stream)
    {
        return Array.combine(this, arraySize, stream);
    }



    @Override
    default Numbers<E> copy()
    {
        return resized(length());
    }

    default PrimitiveIterator.OfInt intIterator(){
        return new PrimitiveIterator.OfInt() {
            int index = 0;
            @Override
            public int nextInt()
            {
                return getInt(index++);
            }

            @Override
            public boolean hasNext()
            {
                return index < length();
            }
        };
    }

    default PrimitiveIterator.OfLong longIterator(){
        return new PrimitiveIterator.OfLong() {
            int index = 0;
            @Override
            public long nextLong()
            {
                return getLong(index++);
            }

            @Override
            public boolean hasNext()
            {
                return index < length();
            }
        };
    }

    default PrimitiveIterator.OfDouble doubleIterator(){
        return new PrimitiveIterator.OfDouble() {
            int index = 0;
            @Override
            public double nextDouble()
            {
                return getDouble(index++);
            }

            @Override
            public boolean hasNext()
            {
                return index < length();
            }
        };
    }
}
