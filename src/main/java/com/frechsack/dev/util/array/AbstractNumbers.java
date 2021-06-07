package com.frechsack.dev.util.array;

import java.util.stream.IntStream;

/**
 * Package-private implementations base of {@link Numbers}.
 *
 * @param <E> The Array´s element type.
 * @author Frechsack
 */
abstract class AbstractNumbers<E extends Number> extends AbstractArray<E> implements Numbers<E>
{
    protected AbstractNumbers()
    {
        super();
    }

    @Override
    public boolean equals(int[] array)
    {
        if (length() != array.length) return false;
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (getInt(i) != array[i]) return false;
            }
            return true;
        }
        return IntStream.range(0, length()).allMatch(index -> getInt(index) == array[index]);
    }

    @Override
    public boolean equals(float[] array)
    {
        if (length() != array.length) return false;
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (getFloat(i) != array[i]) return false;
            }
            return true;
        }
        return IntStream.range(0, length()).allMatch(index -> getFloat(index) == array[index]);
    }

    @Override
    public boolean equals(double[] array)
    {
        if (length() != array.length) return false;
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (getDouble(i) != array[i]) return false;
            }
            return true;
        }
        return IntStream.range(0, length()).allMatch(index -> getDouble(index) == array[index]);
    }

    @Override
    public boolean equals(long[] array)
    {
        if (length() != array.length) return false;
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (getLong(i) != array[i]) return false;
            }
            return true;
        }
        return IntStream.range(0, length()).allMatch(index -> getLong(index) == array[index]);
    }

    @Override
    public boolean equals(short[] array)
    {
        if (length() != array.length) return false;
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (getShort(i) != array[i]) return false;
            }
            return true;
        }
        return IntStream.range(0, length()).allMatch(index -> getShort(index) == array[index]);
    }

    @Override
    public boolean equals(byte[] array)
    {
        if (length() != array.length) return false;
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (getByte(i) != array[i]) return false;
            }
            return true;
        }
        return IntStream.range(0, length()).allMatch(index -> getByte(index) == array[index]);
    }
}
