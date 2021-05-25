package com.frechsack.dev.util.array;

import com.frechsack.dev.util.route.Route;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

class ArrayFactory
{


    private static class GenericArray<E> extends AbstractArray<E>
    {
        private final E[] data;

        private GenericArray(E[] data) {this.data = data;}

        @SuppressWarnings("unchecked")
        private GenericArray(int length, Class<E> type)
        {
            this.data = (E[]) Array.newInstance(type, length);
        }

        @Override
        protected E getVoid()
        {
            return null;
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public E get(int index)
        {
            return data[index];
        }

        @Override
        public E set(int index, E element)
        {
            E last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public boolean isPrimitive()
        {
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public IntFunction<E[]> generator()
        {
            return length -> (E[]) Array.newInstance(data.getClass().getComponentType(), length);
        }

        @Override
        public void sort(Comparator<? super E> c)
        {
            Arrays.sort(data, c);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public Route<E> route()
        {
            return Route.of(data);
        }

        @Override
        public Iterator<E> iterator()
        {
            return Route.of(data);
        }

        @Override
        public String toString()
        {
            return "GenericArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public boolean equals(E[] array)
        {
            return Arrays.equals(array,data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }
    }

    private static class LongArray extends AbstractNumericArray<Long>
    {

        private final long[] data;

        private LongArray(long[] data) {this.data = data;}

        private LongArray(int length) {this.data = new long[length];}

        @Override
        protected Long getVoid()
        {
            return 0L;
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) data[index];
        }

        @Override
        public short getShort(int index)
        {
            return (short) data[index];
        }

        @Override
        public int getInt(int index)
        {
            return (int) data[index];
        }

        @Override
        public float getFloat(int index)
        {
            return data[index];
        }

        @Override
        public double getDouble(int index)
        {
            return data[index];
        }

        @Override
        public long getLong(int index)
        {
            return data[index];
        }

        @Override
        public byte setByte(int index, byte element)
        {
            byte last = (byte) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public short setShort(int index, short element)
        {
            short last = (short) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public int setInt(int index, int element)
        {
            int last = (int) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public float setFloat(int index, float element)
        {
            float last = data[index];
            data[index] = (long) element;
            return last;
        }

        @Override
        public double setDouble(int index, double element)
        {
            double last = data[index];
            data[index] = (long) element;
            return last;
        }

        @Override
        public long setLong(int index, long element)
        {
            long last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public Long get(int index)
        {
            return getLong(index);
        }

        @Override
        public Long set(int index, Long element)
        {
            return setLong(index, element == null ? getVoid() : element);
        }

        @Override
        public boolean isPrimitive()
        {
            return true;
        }

        @Override
        public IntFunction<Long[]> generator()
        {
            return Long[]::new;
        }

        @Override
        public void sort(Comparator<? super Long> c)
        {
            // TODO: Sort -> Sort Stream?
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public LongStream longStream()
        {
            return Arrays.stream(data);
        }

        @Override
        public Route<Long> route()
        {
            return Route.of(data);
        }

        @Override
        public Iterator<Long> iterator()
        {
            return Route.of(data);
        }

        @Override
        public String toString()
        {
            return "LongArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public boolean equals(long[] array)
        {
            return Arrays.equals(array, data);
        }
    }

    private static class DoubleArray extends AbstractNumericArray<Double>
    {
        private final double[] data;

        private DoubleArray(double[] data) {this.data = data;}

        private DoubleArray(int length) {this.data = new double[length];}

        @Override
        protected Double getVoid()
        {
            return 0d;
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) data[index];
        }

        @Override
        public short getShort(int index)
        {
            return (short) data[index];
        }

        @Override
        public int getInt(int index)
        {
            return (int) data[index];
        }

        @Override
        public float getFloat(int index)
        {
            return (float) data[index];
        }

        @Override
        public double getDouble(int index)
        {
            return data[index];
        }

        @Override
        public long getLong(int index)
        {
            return (long) data[index];
        }

        @Override
        public byte setByte(int index, byte element)
        {
            byte last = (byte) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public short setShort(int index, short element)
        {
            short last = (short) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public int setInt(int index, int element)
        {
            int last = (int) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public float setFloat(int index, float element)
        {
            float last = (float) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public double setDouble(int index, double element)
        {
            double last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public long setLong(int index, long element)
        {
            long last = (long) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public Double get(int index)
        {
            return getDouble(index);
        }

        @Override
        public Double set(int index, Double element)
        {
            return setDouble(index, element == null ? getVoid() : element);
        }

        @Override
        public boolean isPrimitive()
        {
            return true;
        }

        @Override
        public IntFunction<Double[]> generator()
        {
            return Double[]::new;
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public void sort(Comparator<? super Double> c)
        {
            // TODO: Sort
        }

        @Override
        public DoubleStream doubleStream()
        {
            return Arrays.stream(data);
        }


        @Override
        public Route<Double> route()
        {
            return Route.of(data);
        }

        @Override
        public String toString()
        {
            return "DoubleArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public Iterator<Double> iterator()
        {
            return Route.of(data);
        }

        @Override
        public boolean equals(double[] array)
        {
            return Arrays.equals(array, data);
        }
    }

    private static class FloatArray extends AbstractNumericArray<Float>
    {
        private final float[] data;

        private FloatArray(float[] data) {this.data = data;}

        private FloatArray(int length) {this.data = new float[length];}

        @Override
        protected Float getVoid()
        {
            return 0f;
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) data[index];
        }

        @Override
        public short getShort(int index)
        {
            return (short) data[index];
        }

        @Override
        public int getInt(int index)
        {
            return (int) data[index];
        }

        @Override
        public float getFloat(int index)
        {
            return data[index];
        }

        @Override
        public double getDouble(int index)
        {
            return data[index];
        }

        @Override
        public long getLong(int index)
        {
            return (long) data[index];
        }

        @Override
        public byte setByte(int index, byte element)
        {
            byte last = (byte) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public short setShort(int index, short element)
        {
            short last = (short) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public int setInt(int index, int element)
        {
            int last = (int) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public float setFloat(int index, float element)
        {
            float last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public double setDouble(int index, double element)
        {
            double last = data[index];
            data[index] = (float) element;
            return last;
        }

        @Override
        public long setLong(int index, long element)
        {
            long last = (long) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public Float get(int index)
        {
            return data[index];
        }

        @Override
        public Float set(int index, Float element)
        {
            return setFloat(index, element == null ? getVoid() : element);
        }

        @Override
        public boolean isPrimitive()
        {
            return true;
        }

        @Override
        public IntFunction<Float[]> generator()
        {
            return Float[]::new;
        }

        @Override
        public Route<Float> route()
        {
            return Route.of(data);
        }

        @Override
        public Iterator<Float> iterator()
        {
            return Route.of(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public void sort(Comparator<? super Float> c)
        {
            // TODO: Sort
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public String toString()
        {
            return "FloatArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public boolean equals(float[] array)
        {
            return Arrays.equals(array, data);
        }
    }

    private static class IntArray extends AbstractNumericArray<Integer>
    {
        private final int[] data;

        private IntArray(int[] data) {this.data = data;}

        private IntArray(int length) {this.data = new int[length];}

        @Override
        protected Integer getVoid()
        {
            return 0;
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) data[index];
        }

        @Override
        public short getShort(int index)
        {
            return (short) data[index];
        }

        @Override
        public int getInt(int index)
        {
            return data[index];
        }

        @Override
        public float getFloat(int index)
        {
            return data[index];
        }

        @Override
        public double getDouble(int index)
        {
            return data[index];
        }

        @Override
        public long getLong(int index)
        {
            return data[index];
        }

        @Override
        public byte setByte(int index, byte element)
        {
            byte last = (byte) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public short setShort(int index, short element)
        {
            short last = (short) data[index];
            data[index] = element;
            return last;
        }

        @Override
        public int setInt(int index, int element)
        {
            int last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public float setFloat(int index, float element)
        {
            float last = data[index];
            data[index] = (int) element;
            return last;
        }

        @Override
        public double setDouble(int index, double element)
        {
            double last = data[index];
            data[index] = (int) element;
            return last;
        }

        @Override
        public long setLong(int index, long element)
        {
            long last = data[index];
            data[index] = (int) element;
            return last;
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public Integer get(int index)
        {
            return data[index];
        }

        @Override
        public Integer set(int index, Integer element)
        {
            return setInt(index, element == null ? getVoid() : element);
        }

        @Override
        public boolean isPrimitive()
        {
            return true;
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public void sort(Comparator<? super Integer> c)
        {
            // TODO: Sort
        }

        @Override
        public IntFunction<Integer[]> generator()
        {
            return Integer[]::new;
        }

        @Override
        public Route<Integer> route()
        {
            return Route.of(data);
        }

        @Override
        public IntStream intStream()
        {
            return Arrays.stream(data);
        }

        @Override
        public Iterator<Integer> iterator()
        {
            return Route.of(data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public String toString()
        {
            return "IntArray{" + Arrays.toString(data) + '}';
        }
    }

    private static class ShortArray extends AbstractNumericArray<Short>
    {

        private final short[] data;

        private ShortArray(short[] data) {this.data = data;}

        private ShortArray(int length) {this.data = new short[length];}

        @Override
        protected Short getVoid()
        {
            return 0;
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) data[index];
        }

        @Override
        public short getShort(int index)
        {
            return data[index];
        }

        @Override
        public int getInt(int index)
        {
            return data[index];
        }

        @Override
        public float getFloat(int index)
        {
            return data[index];
        }

        @Override
        public double getDouble(int index)
        {
            return data[index];
        }

        @Override
        public long getLong(int index)
        {
            return data[index];
        }

        @Override
        public byte setByte(int index, byte element)
        {
            short last = data[index];
            data[index] = element;
            return (byte) last;
        }

        @Override
        public short setShort(int index, short element)
        {
            short last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public int setInt(int index, int element)
        {
            short last = data[index];
            data[index] = (short) element;
            return last;
        }

        @Override
        public float setFloat(int index, float element)
        {
            short last = data[index];
            data[index] = (short) element;
            return last;
        }

        @Override
        public double setDouble(int index, double element)
        {
            short last = data[index];
            data[index] = (short) element;
            return last;
        }

        @Override
        public long setLong(int index, long element)
        {
            short last = data[index];
            data[index] = (short) element;
            return last;
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public Short get(int index)
        {
            return data[index];
        }

        @Override
        public Short set(int index, Short element)
        {
            return setShort(index, element == null ? getVoid() : element);
        }

        @Override
        public boolean isPrimitive()
        {
            return true;
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public void sort(Comparator<? super Short> c)
        {
            // TODO: Sort
        }

        @Override
        public IntFunction<Short[]> generator()
        {
            return Short[]::new;
        }

        @Override
        public Route<Short> route()
        {
            return Route.of(data);
        }

        @Override
        public Iterator<Short> iterator()
        {
            return Route.of(data);
        }

        @Override
        public String toString()
        {
            return "ShortArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public boolean equals(short[] array)
        {
            return Arrays.equals(array, data);
        }
    }

    private static class ByteArray extends AbstractNumericArray<Byte>
    {
        private final byte[] data;

        private ByteArray(byte[] data) {this.data = data;}

        private ByteArray(int length) {this.data = new byte[length];}

        @Override
        protected Byte getVoid()
        {
            return 0;
        }

        @Override
        public byte getByte(int index)
        {
            return data[index];
        }

        @Override
        public short getShort(int index)
        {
            return data[index];
        }

        @Override
        public int getInt(int index)
        {
            return data[index];
        }

        @Override
        public float getFloat(int index)
        {
            return data[index];
        }

        @Override
        public double getDouble(int index)
        {
            return data[index];
        }

        @Override
        public long getLong(int index)
        {
            return data[index];
        }

        @Override
        public byte setByte(int index, byte element)
        {
            byte last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public short setShort(int index, short element)
        {
            byte last = data[index];
            data[index] = (byte) element;
            return last;
        }

        @Override
        public int setInt(int index, int element)
        {
            byte last = data[index];
            data[index] = (byte) element;
            return last;
        }

        @Override
        public float setFloat(int index, float element)
        {
            byte last = data[index];
            data[index] = (byte) element;
            return last;
        }

        @Override
        public double setDouble(int index, double element)
        {
            byte last = data[index];
            data[index] = (byte) element;
            return last;
        }

        @Override
        public long setLong(int index, long element)
        {
            byte last = data[index];
            data[index] = (byte) element;
            return last;
        }

        @Override
        public byte[] toByteArray()
        {
            byte[] clone = new byte[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public Byte get(int index)
        {
            return data[index];
        }

        @Override
        public Byte set(int index, Byte element)
        {
            return setByte(index, element == null ? getVoid() : element);
        }

        @Override
        public boolean isPrimitive()
        {
            return true;
        }

        @Override
        public IntFunction<Byte[]> generator()
        {
            return Byte[]::new;
        }

        @Override
        public Iterator<Byte> iterator()
        {
            return Route.of(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public void sort(Comparator<? super Byte> c)
        {
            // TODO: Sort
        }

        @Override
        public Route<Byte> route()
        {
            return Route.of(data);
        }

        @Override
        public String toString()
        {
            return "ByteArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public boolean equals(byte[] array)
        {
            return Arrays.equals(array, data);
        }
    }

    private static class CharArray extends AbstractArray<Character> implements com.frechsack.dev.util.array.CharArray
    {

        private final char[] data;

        private CharArray(char[] data) {this.data = data;}

        private CharArray(int length) {this.data = new char[length];}

        @Override
        protected Character getVoid()
        {
            // TODO: Java Default Value
            return Character.MIN_VALUE;
        }

        @Override
        public char getChar(int index)
        {
            return data[index];
        }

        @Override
        public char setChar(int index, char element)
        {
            char last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public char[] toCharArray()
        {
            char[] clone = new char[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public boolean equals(char[] array)
        {
            return Arrays.equals(array, data);
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public Character get(int index)
        {
            return getChar(index);
        }

        @Override
        public Character set(int index, Character element)
        {
            return setChar(index, element == null ? getVoid() : element);
        }

        @Override
        public boolean isPrimitive()
        {
            return true;
        }

        @Override
        public IntFunction<Character[]> generator()
        {
            return Character[]::new;
        }

        @Override
        public Iterator<Character> iterator()
        {
            return Route.of(data);
        }

        @Override
        public Route<Character> route()
        {
            return Route.of(data);
        }

        @Override
        public String toString()
        {
            return "CharArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public void sort(Comparator<? super Character> c)
        {
            // TODO: Sort
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

    }

    private static class BooleanArray extends AbstractArray<Boolean> implements com.frechsack.dev.util.array.BooleanArray
    {

        private final boolean[] data;

        private BooleanArray(boolean[] data) {this.data = data;}

        private BooleanArray(int length) {this.data = new boolean[length];}


        @Override
        public boolean getBoolean(int index)
        {
            return data[index];
        }

        @Override
        public boolean setBoolean(int index, boolean element)
        {
            boolean last = data[index];
            data[index] = element;
            return last;
        }

        @Override
        public boolean[] toBooleanArray()
        {
            boolean[] clone = new boolean[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public boolean equals(boolean[] array)
        {
            return Arrays.equals(array, data);
        }

        @Override
        protected Boolean getVoid()
        {
            return false;
        }

        @Override
        public Object asArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public Boolean get(int index)
        {
            return data[index];
        }

        @Override
        public Boolean set(int index, Boolean element)
        {
            return setBoolean(index, element == null ? getVoid() : element);
        }

        @Override
        public boolean isPrimitive()
        {
            return true;
        }

        @Override
        public IntFunction<Boolean[]> generator()
        {
            return Boolean[]::new;
        }

        @Override
        public Iterator<Boolean> iterator()
        {
            return Route.of(data);
        }

        @Override
        public Route<Boolean> route()
        {
            return Route.of(data);
        }

        @Override
        public String toString()
        {
            return "BooleanArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }
    }
}
