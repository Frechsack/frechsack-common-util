package com.frechsack.dev.util.array;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public interface NumArray<E extends Number> extends Array<E>
{
    byte getByte(int index);

    short getShort(int index);

    int getInt(int index);

    float getFloat(int index);

    double getDouble(int index);

    long getLong(int index);

    void setByte(int index, byte element);

    void setShort(int index, short element);

    void setInt(int index, int element);

    void setFloat(int index, float element);

    void setDouble(int index, double element);

    void setLong(int index, long element);

    default byte getAndSetByte(int index, byte element)
    {
        byte last = getByte(index);
        setByte(index, element);
        return last;
    }

    default short getAndSetShort(int index, short element)
    {
        short last = getShort(index);
        setShort(index, element);
        return last;
    }

    default int getAndSetInt(int index, int element)
    {
        int last = getInt(index);
        setInt(index, element);
        return last;
    }

    default float getAndSetFloat(int index, float element)
    {
        float last = getFloat(index);
        setFloat(index, element);
        return last;
    }
    default double getAndSetDouble(int index, double element)
    {
        double last = getDouble(index);
        setDouble(index, element);
        return last;
    }
    default long getAndSetLong(int index, long element)
    {
        long last = getLong(index);
        setLong(index, element);
        return last;
    }

    byte[] toByteArray();

    short[] toShortArray();

    int[] toIntArray();

    double[] toDoubleArray();

    long[] toLongArray();

    float[] toFloatArray();

    boolean equals(int[] array);

    boolean equals(float[] array);

    boolean equals(double[] array);

    boolean equals(long[] array);

    boolean equals(short[] array);

    boolean equals(byte[] array);

    IntStream intStream();

    DoubleStream doubleStream();

    LongStream longStream();
}
