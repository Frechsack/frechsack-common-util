package com.frechsack.dev.util.array;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public interface NumericArray<E extends Number> extends Array<E>
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
