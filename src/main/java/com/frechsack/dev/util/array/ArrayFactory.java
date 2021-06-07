package com.frechsack.dev.util.array;

import com.frechsack.dev.util.route.Route;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Package-private implementations of {@link com.frechsack.dev.util.array.Array}.
 *
 * @author Frechsack
 */
class ArrayFactory
{

    static class ArrayList<E> extends AbstractList<E> implements RandomAccess
    {
        private final com.frechsack.dev.util.array.Array<E> array;

        ArrayList(com.frechsack.dev.util.array.Array<E> array) {this.array = array;}

        @Override
        public E set(int index, E element)
        {
            return array.getAndSet(index, element);
        }

        @Override
        public E get(int index)
        {
            return array.get(index);
        }

        @Override
        public int size()
        {
            return array.length();
        }

        @Override
        public int indexOf(Object o)
        {
            return array.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o)
        {
            return array.lastIndexOf(o);
        }

        @Override
        public void clear()
        {
            array.clear();
        }

        @Override
        public boolean isEmpty()
        {
            return array.length() == 0;
        }

        @Override
        public boolean contains(Object o)
        {
            return array.contains(o);
        }

        @Override
        public Object[] toArray()
        {
            return array.toArray();
        }

        @Override
        public <T> T[] toArray(IntFunction<T[]> generator)
        {
            return toArray(generator.apply(array.length()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a)
        {
            final T[] clone = a.length >= array.length() ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), array.length());
            // Stream
            for (int i = 0; i < array.length(); i++)
            {
                clone[i] = (T) array.get(i);
            }
            return clone;
        }

        @Override
        public Stream<E> stream()
        {
            return array.stream();
        }

        @Override
        public Stream<E> parallelStream()
        {
            return array.parallelStream();
        }

        @Override
        public void sort(Comparator<? super E> c)
        {
            array.sort(c);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o || array == o) return true;
            if (o instanceof List) return super.equals(o);
            if (o instanceof com.frechsack.dev.util.array.Array) return equals(((com.frechsack.dev.util.array.Array<?>) o).asList());
            if (!(o instanceof Collection)) return false;
            // Compare collection
            return array.parallelStream().allMatch(((Collection<?>) o)::contains);
        }
    }

    static class ArrayIterator<E> implements Iterator<E>
    {
        private final com.frechsack.dev.util.array.Array<E> array;


        private int index = -1;

        ArrayIterator(com.frechsack.dev.util.array.Array<E> array) {this.array = array;}

        @Override
        public boolean hasNext()
        {
            return index + 1 < array.length();
        }

        @Override
        public E next()
        {
            return array.get(++index);
        }

        @Override
        public void remove()
        {
            try
            {
                array.set(index, null);
            }
            catch (Exception ignored)
            {
            }
        }
    }


    static class GenericCharacters extends AbstractArray<Character> implements com.frechsack.dev.util.array.Characters
    {
        private final Character[] data;

        GenericCharacters(Character[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new Character[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        GenericCharacters(int length)
        {
            this.data = new Character[length];
        }


        @Override
        protected Character getVoid()
        {
            return '\u0000';
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
            Character last = data[index];
            return last == null ? getVoid() : last;
        }

        @Override
        public void set(int index, Character element)
        {
            data[index] = element == null ? getVoid() : element;
        }

        @Override
        public boolean isPrimitive()
        {
            return false;
        }

        @Override
        public IntFunction<Character[]> generator()
        {
            return Character[]::new;
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public Route<Character> route()
        {
            return Route.of(data);
        }

        @Override
        public char getChar(int index)
        {
            Character last = data[index];
            return last == null ? getVoid() : last;
        }

        @Override
        public void setChar(int index, char element)
        {
            data[index] = element;
        }

        @Override
        public char[] toCharArray()
        {
            char[] clone = new char[data.length];
            IntStream.range(0, length()).parallel().forEach(index -> clone[index] = get(index));
            return clone;
        }

        @Override
        public boolean equals(char[] array)
        {
            if (data.length != array.length) return false;

            if (length() < STREAM_PREFERRED_LENGTH)
            {
                for (int i = 0; i < length(); i++)
                {
                    if (getChar(i) != array[i]) return false;
                }
                return true;
            }
            return IntStream.range(0, length()).allMatch(index -> getChar(index) == array[index]);
        }

        @Override
        public String toString()
        {
            return "GenericCharacters{" + Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }
    }

    static class GenericBooleans extends AbstractArray<Boolean> implements com.frechsack.dev.util.array.Booleans
    {
        private final Boolean[] data;

        GenericBooleans(Boolean[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new Boolean[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        GenericBooleans(int length) {this.data = new Boolean[length];}

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
        public void setBoolean(int index, boolean element)
        {
            data[index] = element;
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
        public String toString()
        {
            return "GenericBooleanArray{" + Arrays.toString(data) + '}';
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }
    }

    static class GenericNumbers<E extends Number> extends AbstractNumbers<E>
    {
        private final E[] data;
        private final Function<Number, E> converter;

        @SuppressWarnings("unchecked")
        GenericNumbers(E[] data, boolean isReference, Function<Number, E> converter)
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
        GenericNumbers(int length, Class<E> type, Function<Number, E> converter)
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

    static class LongArray extends AbstractNumbers<Long>
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

    static class DoubleArray extends AbstractNumbers<Double>
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
        public boolean equals(double[] array)
        {
            return Arrays.equals(array, data);
        }
    }

    static class FloatArray extends AbstractNumbers<Float>
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

    static class IntArray extends AbstractNumbers<Integer>
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

    static class ShortArray extends AbstractNumbers<Short>
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

    static class ByteArray extends AbstractNumbers<Byte>
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

    static class Characters extends AbstractArray<Character> implements com.frechsack.dev.util.array.Characters
    {
        private final char[] data;

        Characters(char[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new char[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        Characters(int length) {this.data = new char[length];}

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
        public void setChar(int index, char element)
        {
            data[index] = element;
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

    static class Booleans extends AbstractArray<Boolean> implements com.frechsack.dev.util.array.Booleans
    {

        private final boolean[] data;

        Booleans(boolean[] data, boolean isReference)
        {
            if (isReference) this.data = data;
            else
            {
                this.data = new boolean[data.length];
                System.arraycopy(data, 0, this.data, 0, data.length);
            }
        }

        Booleans(int length) {this.data = new boolean[length];}


        @Override
        public boolean getBoolean(int index)
        {
            return data[index];
        }

        @Override
        public void setBoolean(int index, boolean element)
        {
            data[index] = element;
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
