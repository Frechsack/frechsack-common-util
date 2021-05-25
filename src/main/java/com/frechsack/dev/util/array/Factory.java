package com.frechsack.dev.util.array;

import com.frechsack.dev.util.route.Route;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

class Factory
{

    static class GenericBoolArray extends AbstractArray<Boolean> implements com.frechsack.dev.util.array.BoolArray
    {
        private final Boolean[] data;

        GenericBoolArray(Boolean[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new Boolean[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        GenericBoolArray(int length) {this.data = new Boolean[length];}

        @Override
        protected Boolean getVoid()
        {
            return false;
        }

        @Override
        public boolean getBoolean(int index)
        {
            Boolean value = data[index];
            return value != null && value;
        }

        @Override
        public boolean setBoolean(int index, boolean element)
        {
            boolean last = getBoolean(index);
            data[index] = element;
            return last;
        }

        @Override
        public boolean[] toBooleanArray()
        {
            boolean[] clone = new boolean[data.length];
            IntStream.range(0, data.length).parallel().forEach(index -> clone[index] = getBoolean(index));
            return clone;
        }

        @Override
        public boolean equals(boolean[] array)
        {
            if (length() != array.length) return false;
            if (length() < STREAM_PREFERRED_LENGTH)
            {
                for (int i = 0; i < length(); i++)
                {
                    if (getBoolean(i) != array[i]) return false;
                }
                return true;
            }
            return IntStream.range(0, length()).allMatch(index -> getBoolean(index) == array[index]);
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
            Boolean value = data[index];
            return value != null && value;
        }

        @Override
        public void set(int index, Boolean element)
        {
            data[index] = element != null && element;
        }

        @Override
        public boolean isPrimitive()
        {
            return false;
        }

        @Override
        public IntFunction<Boolean[]> generator()
        {
            return Boolean[]::new;
        }

        @Override
        public Route<Boolean> route()
        {
            return Route.of(data);
        }

        @Override
        public Iterator<Boolean> iterator()
        {
            return Route.of(data);
        }

        @Override
        public String toString()
        {
            return "GenericBooleanArray{" +  Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }
    }

    static class GenericNumArray<E extends Number> extends AbstractNumArray<E>
    {
        private final E[] data;
        private final Function<Number, E> converter;

        @SuppressWarnings("unchecked")
        GenericNumArray(E[] data, boolean isReference, Function<Number, E> converter)
        {
            this.converter = converter;
            if (isReference) this.data = data;
            else
            {
                this.data = (E[]) Array.newInstance(data.getClass().getComponentType(), data.length);
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        @SuppressWarnings("unchecked")
        GenericNumArray(int length, Class<E> type, Function<Number, E> converter)
        {
            this.converter = converter;
            this.data = (E[]) Array.newInstance(type, length);
            // Fill with void
            E voidValue = getVoid();
            IntStream.range(0, length).parallel().forEach(index -> data[index] = voidValue);
        }

        @Override
        protected E getVoid()
        {
            return converter.apply(0);
        }

        @Override
        public byte getByte(int index)
        {
            return get(index).byteValue();
        }

        @Override
        public short getShort(int index)
        {
            return get(index).shortValue();
        }

        @Override
        public int getInt(int index)
        {
            return get(index).intValue();
        }

        @Override
        public float getFloat(int index)
        {
            return get(index).floatValue();
        }

        @Override
        public double getDouble(int index)
        {
            return get(index).doubleValue();
        }

        @Override
        public long getLong(int index)
        {
            return get(index).longValue();
        }

        @Override
        public void setByte(int index, byte element)
        {
            data[index] = converter.apply(element);
        }

        @Override
        public void setShort(int index, short element)
        {
            data[index] = converter.apply(element);
        }

        @Override
        public void setInt(int index, int element)
        {
            data[index] = converter.apply(element);
        }

        @Override
        public void setFloat(int index, float element)
        {
            data[index] = converter.apply(element);
        }

        @Override
        public void setDouble(int index, double element)
        {
            data[index] = converter.apply(element);
        }

        @Override
        public void setLong(int index, long element)
        {
            data[index] = converter.apply(element);
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
            E value = data[index];
            return value == null ? getVoid() : value;
        }

        @Override
        public void set(int index, E element)
        {
            data[index] = element == null ? getVoid() : element;
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
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public void sort(Comparator<? super E> c)
        {
            Arrays.sort(data, c);
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
            return "GenericNumericArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            int result = Objects.hash(converter);
            result = 31 * result + Arrays.hashCode(data);
            return result;
        }
    }


    static class GenericArray<E> extends AbstractArray<E>
    {
        private final E[] data;

        @SuppressWarnings("unchecked")
        GenericArray(E[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = (E[]) Array.newInstance(data.getClass().getComponentType(), data.length);
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        @SuppressWarnings("unchecked")
        GenericArray(int length, Class<E> type)
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
        public void set(int index, E element)
        {
            data[index] = element;
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
            return Arrays.equals(array, data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }
    }

    static class LongArray extends AbstractNumArray<Long>
    {

        private final long[] data;

        LongArray(long[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new long[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        LongArray(int length) {this.data = new long[length];}

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
        public void setByte(int index, byte element)
        {
            data[index] = element;
        }

        @Override
        public void setShort(int index, short element)
        {
            data[index] = element;
        }

        @Override
        public void setInt(int index, int element)
        {
            data[index] = element;
        }

        @Override
        public void setFloat(int index, float element)
        {
            data[index] = (long) element;
        }

        @Override
        public void setDouble(int index, double element)
        {
            data[index] = (long) element;
        }

        @Override
        public void setLong(int index, long element)
        {
            data[index] = element;
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
        public void set(int index, Long element)
        {
            setLong(index, element == null ? getVoid() : element);
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

    static class DoubleArray extends AbstractNumArray<Double>
    {
        private final double[] data;

        DoubleArray(double[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new double[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        DoubleArray(int length) {this.data = new double[length];}

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
        public void setByte(int index, byte element)
        {
            data[index] = element;
        }

        @Override
        public void setShort(int index, short element)
        {
            data[index] = element;
        }

        @Override
        public void setInt(int index, int element)
        {
            data[index] = element;
        }

        @Override
        public void setFloat(int index, float element)
        {
            data[index] = element;
        }

        @Override
        public void setDouble(int index, double element)
        {
            data[index] = element;
        }

        @Override
        public void setLong(int index, long element)
        {
            data[index] = element;
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
        public void set(int index, Double element)
        {
            setDouble(index, element == null ? getVoid() : element);
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

    static class FloatArray extends AbstractNumArray<Float>
    {
        private final float[] data;

        FloatArray(float[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new float[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        FloatArray(int length) {this.data = new float[length];}

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
        public void setByte(int index, byte element)
        {
            data[index] = element;
        }

        @Override
        public void setShort(int index, short element)
        {
            data[index] = element;
        }

        @Override
        public void setInt(int index, int element)
        {
            data[index] = element;
        }

        @Override
        public void setFloat(int index, float element)
        {
            data[index] = element;
        }

        @Override
        public void setDouble(int index, double element)
        {
            data[index] = (float) element;
        }

        @Override
        public void setLong(int index, long element)
        {
            data[index] = element;
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
        public void set(int index, Float element)
        {
             setFloat(index, element == null ? getVoid() : element);
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

    static class IntArray extends AbstractNumArray<Integer>
    {
        private final int[] data;

        IntArray(int[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new int[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        IntArray(int length) {this.data = new int[length];}

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
        public void setByte(int index, byte element)
        {
            data[index] = element;
        }

        @Override
        public void setShort(int index, short element)
        {
            data[index] = element;
        }

        @Override
        public void setInt(int index, int element)
        {
            data[index] = element;
        }

        @Override
        public void setFloat(int index, float element)
        {
            data[index] = (int) element;
        }

        @Override
        public void setDouble(int index, double element)
        {
            data[index] = (int) element;
        }

        @Override
        public void setLong(int index, long element)
        {
            data[index] = (int) element;
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
        public void set(int index, Integer element)
        {
             setInt(index, element == null ? getVoid() : element);
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

    static class ShortArray extends AbstractNumArray<Short>
    {

        private final short[] data;

        ShortArray(short[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new short[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        ShortArray(int length) {this.data = new short[length];}

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
        public void setByte(int index, byte element)
        {
            data[index] = element;
        }

        @Override
        public void setShort(int index, short element)
        {
            data[index] = element;
        }

        @Override
        public void setInt(int index, int element)
        {
            data[index] = (short) element;
        }

        @Override
        public void setFloat(int index, float element)
        {
            data[index] = (short) element;
        }

        @Override
        public void setDouble(int index, double element)
        {
            data[index] = (short) element;
        }

        @Override
        public void setLong(int index, long element)
        {
            data[index] = (short) element;
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
        public void set(int index, Short element)
        {
             setShort(index, element == null ? getVoid() : element);
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

    static class ByteArray extends AbstractNumArray<Byte>
    {
        private final byte[] data;

        ByteArray(byte[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new byte[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        ByteArray(int length) {this.data = new byte[length];}

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
        public void setByte(int index, byte element)
        {
            data[index] = element;
        }

        @Override
        public void setShort(int index, short element)
        {
            data[index] = (byte) element;
        }

        @Override
        public void setInt(int index, int element)
        {
            data[index] = (byte) element;
        }

        @Override
        public void setFloat(int index, float element)
        {
            data[index] = (byte) element;
        }

        @Override
        public void setDouble(int index, double element)
        {
            data[index] = (byte) element;
        }

        @Override
        public void setLong(int index, long element)
        {
            data[index] = (byte) element;
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
        public void set(int index, Byte element)
        {
             setByte(index, element == null ? getVoid() : element);
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

    static class CharArray extends AbstractArray<Character> implements com.frechsack.dev.util.array.CharArray
    {
        private final char[] data;

        CharArray(char[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new char[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        CharArray(int length) {this.data = new char[length];}

        @Override
        protected Character getVoid()
        {
            return '\u0000';
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
        public void set(int index, Character element)
        {
             setChar(index, element == null ? getVoid() : element);
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
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

    }

    static class BoolArray extends AbstractArray<Boolean> implements com.frechsack.dev.util.array.BoolArray
    {

        private final boolean[] data;

        BoolArray(boolean[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new boolean[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        BoolArray(int length) {this.data = new boolean[length];}


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
        public void set(int index, Boolean element)
        {
             setBoolean(index, element == null ? getVoid() : element);
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
