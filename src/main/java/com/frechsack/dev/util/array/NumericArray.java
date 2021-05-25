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

    byte setByte(int index, byte element);

    short setShort(int index, short element);

    int setInt(int index, int element);

    float setFloat(int index, float element);

    double setDouble(int index, double element);

    long setLong(int index, long element);

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
