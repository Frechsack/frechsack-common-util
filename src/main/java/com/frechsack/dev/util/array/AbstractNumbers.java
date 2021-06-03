package com.frechsack.dev.util.array;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public abstract class AbstractNumbers<E extends Number> extends AbstractArray<E> implements Numbers<E>
{
    protected AbstractNumbers(){
        super();
    }

    @Override
    public IntStream intStream()
    {
        return IntStream.range(0,length()).map(this::getInt);
    }

    @Override
    public DoubleStream doubleStream()
    {
        return IntStream.range(0,length()).mapToDouble(this::getDouble);
    }

    @Override
    public LongStream longStream()
    {
        return IntStream.range(0,length()).mapToLong(this::getLong);
    }

    @Override
    public byte[] toByteArray()
    {
        byte[] clone = new byte[length()];
        IntStream.range(0, clone.length).parallel().forEach(index -> clone[index] = getByte(index));
        return clone;
    }

    @Override
    public short[] toShortArray()
    {
        short[] clone = new short[length()];
        IntStream.range(0, clone.length).parallel().forEach(index -> clone[index] = getShort(index));
        return clone;
    }

    @Override
    public int[] toIntArray()
    {
        int[] clone = new int[length()];
        IntStream.range(0, clone.length).parallel().forEach(index -> clone[index] = getInt(index));
        return clone;
    }

    @Override
    public double[] toDoubleArray()
    {
        double[] clone = new double[length()];
        IntStream.range(0, clone.length).parallel().forEach(index -> clone[index] = getDouble(index));
        return clone;
    }

    @Override
    public long[] toLongArray()
    {
        long[] clone = new long[length()];
        IntStream.range(0, clone.length).parallel().forEach(index -> clone[index] = getLong(index));
        return clone;
    }

    @Override
    public float[] toFloatArray()
    {
        float[] clone = new float[length()];
        IntStream.range(0, clone.length).parallel().forEach(index -> clone[index] = getFloat(index));
        return clone;
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
