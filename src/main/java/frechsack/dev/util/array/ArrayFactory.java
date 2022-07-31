package frechsack.dev.util.array;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.DoubleFunction;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class ArrayFactory {

    private ArrayFactory() {}

    static class PrimitiveBoolean extends Primitive<java.lang.Boolean> implements Array.Boolean {

        private final boolean[] array;

        PrimitiveBoolean(boolean[] array) {
            this.array = array;
        }

        @Override
        public java.lang.Boolean get(int index) {
            return array[index];
        }

        @Override
        public void set(int index, java.lang.Boolean value) {
            array[index] = Objects.requireNonNull(value);
        }

        @Override
        public Stream<java.lang.Boolean> stream() {
            Stream.Builder<java.lang.Boolean> builder = Stream.builder();
            for(boolean item : array) builder.accept(item);
            return builder.build();
        }

        @Override
        public int length() {
            return array.length;
        }

        @Override
        public boolean getBoolean(int index) {
            return array[index];
        }

        @Override
        public void setBoolean(int index, boolean value) {
            array[index] = value;
        }

        @Override
        public Object nativeArray() {
            return array;
        }

        @Override
        public java.lang.Boolean[] copyBoxed(int start, int length) {
            Objects.checkFromToIndex(start,start+length,length());
            java.lang.Boolean[] array = new java.lang.Boolean[length];
            for (int i = 0; i < length; i++) {
                array[i] = this.array[i + start];
            }
            return array;
        }

        @Override
        public Object copy(int start, int length) {
            return Arrays.copyOfRange(array,start,start + length);
        }
    }

    static class PrimitiveFloat extends Primitive<java.lang.Float> implements Array.Number<java.lang.Float> {

        private final float[] array;

        PrimitiveFloat(float[] array) {
            this.array = array;
        }

        @Override
        public java.lang.Float get(int index) {
            return array[index];
        }

        @Override
        public void set(int index, java.lang.Float value) {
            array[index] = Objects.requireNonNull(value);
        }

        @Override
        public Stream<java.lang.Float> stream() {
            Stream.Builder<java.lang.Float> builder = Stream.builder();
            for(float item : array) builder.accept(item);
            return builder.build();
        }

        @Override
        public LongStream streamLong() {
            return stream().mapToLong(java.lang.Float::longValue);
        }

        @Override
        public IntStream streamInt() {
            return stream().mapToInt(java.lang.Float::intValue);
        }

        @Override
        public DoubleStream streamDouble() {
            return stream().mapToDouble(java.lang.Float::doubleValue);
        }

        @Override
        public int length() {
            return array.length;
        }

        @Override
        public int getInt(int index) {
            return (int) array[index];
        }

        @Override
        public double getDouble(int index) {
            return array[index];
        }

        @Override
        public long getLong(int index) {
            return (long) array[index];
        }

        @Override
        public void setInt(int index, int value) {
            array[index] = value;
        }

        @Override
        public void setDouble(int index, double value) {
            array[index] = (float) value;
        }

        @Override
        public void setLong(int index, long value) {
            array[index] = value;
        }

        @Override
        public Object nativeArray() {
            return array;
        }

        @Override
        public Float[] copyBoxed(int start, int length) {
            Objects.checkFromToIndex(start,start+length,length());
            Float[] array = new Float[length];
            for (int i = 0; i < length; i++) {
                array[i] = this.array[i + start];
            }
            return array;
        }

        @Override
        public Object copy(int start, int length) {
            return Arrays.copyOfRange(array,start,start + length);
        }
    }

    static class PrimitiveLong extends Primitive<java.lang.Long> implements Array.Number<java.lang.Long> {

        private final long[] array;


        PrimitiveLong(long[] array) {
            this.array = array;
        }

        @Override
        public java.lang.Long get(int index) {
            return array[index];
        }

        @Override
        public void set(int index, java.lang.Long value) {
            array[index] = Objects.requireNonNull(value);
        }

        @Override
        public Stream<java.lang.Long> stream() {
            return LongStream.of(array).boxed();
        }

        @Override
        public LongStream streamLong() {
            return LongStream.of(array);
        }

        @Override
        public IntStream streamInt() {
            return streamLong().mapToInt(it -> (int) it);
        }

        @Override
        public DoubleStream streamDouble() {
            return streamLong().mapToDouble(it -> (double) it);
        }

        @Override
        public int length() {
            return array.length;
        }

        @Override
        public int getInt(int index) {
            return (int) array[index];
        }

        @Override
        public double getDouble(int index) {
            return array[index];
        }

        @Override
        public long getLong(int index) {
            return array[index];
        }

        @Override
        public void setInt(int index, int value) {
            array[index] = value;
        }

        @Override
        public void setDouble(int index, double value) {
            array[index] = (long) value;
        }

        @Override
        public void setLong(int index, long value) {
            array[index] = value;
        }

        @Override
        public Object nativeArray() {
            return array;
        }

        @Override
        public Long[] copyBoxed(int start, int length) {
            Objects.checkFromToIndex(start,start+length,length());
            Long[] array = new Long[length];
            for (int i = 0; i < length; i++) {
                array[i] = this.array[i + start];
            }
            return array;
        }

        @Override
        public Object copy(int start, int length) {
            return Arrays.copyOfRange(array,start,start + length);
        }
    }

    static class PrimitiveDouble extends Primitive<java.lang.Double> implements Array.Number<java.lang.Double> {

        private final double[] array;

        PrimitiveDouble(double[] array) {
            this.array = array;
        }

        @Override
        public java.lang.Double get(int index) {
            return array[index];
        }

        @Override
        public void set(int index, java.lang.Double value) {
            array[index] = Objects.requireNonNull(value);
        }

        @Override
        public Stream<java.lang.Double> stream() {
            return DoubleStream.of(array).boxed();
        }

        @Override
        public DoubleStream streamDouble() {
            return DoubleStream.of(array);
        }

        @Override
        public LongStream streamLong() {
            return DoubleStream.of(array).mapToLong(it -> (long)it);
        }

        @Override
        public IntStream streamInt() {
            return DoubleStream.of(array).mapToInt(it -> (int)it);
        }

        @Override
        public int length() {
            return array.length;
        }

        @Override
        public int getInt(int index) {
            return (int) array[index];
        }

        @Override
        public double getDouble(int index) {
            return array[index];
        }

        @Override
        public long getLong(int index) {
            return (long) array[index];
        }

        @Override
        public void setInt(int index, int value) {
            array[index] = value;
        }

        @Override
        public void setDouble(int index, double value) {
            array[index] = value;
        }

        @Override
        public void setLong(int index, long value) {
            array[index] = value;
        }

        @Override
        public Object nativeArray() {
            return array;
        }

        @Override
        public Double[] copyBoxed(int start, int length) {
            Objects.checkFromToIndex(start,start+length,length());
            Double[] array = new Double[length];
            for (int i = 0; i < length; i++) {
                array[i] = this.array[i + start];
            }
            return array;
        }

        @Override
        public Object copy(int start, int length) {
            return Arrays.copyOfRange(array,start,start + length);
        }
    }

    static class PrimitiveInt extends Primitive<Integer> implements Array.Number<Integer> {

        private final int[] array;

        PrimitiveInt(int[] array) {
            this.array = array;
        }

        @Override
        public Integer get(int index) {
            return array[index];
        }

        @Override
        public void set(int index, Integer value) {
            array[index] = Objects.requireNonNull(value);
        }

        @Override
        public Stream<Integer> stream() {
            return IntStream.of(array).boxed();
        }

        @Override
        public int length() {
            return array.length;
        }

        @Override
        public Object nativeArray() {
            return array;
        }

        @Override
        public Integer[] copyBoxed(int start, int length) {
            Objects.checkFromToIndex(start,start+length,length());
            Integer[] array = new Integer[length];
            for (int i = 0; i < length; i++) {
                array[i] = this.array[i + start];
            }
            return array;
        }

        @Override
        public Object copy(int start, int length) {
            return Arrays.copyOfRange(array,start,start + length);
        }

        @Override
        public IntStream streamInt() {
            return IntStream.of(array);
        }

        @Override
        public DoubleStream streamDouble() {
            return streamInt().asDoubleStream();
        }

        @Override
        public LongStream streamLong(){
            return streamInt().asLongStream();
        }

        @Override
        public int getInt(int index) {
            return array[index];
        }

        @Override
        public double getDouble(int index) {
            return array[index];
        }

        @Override
        public long getLong(int index) {
            return array[index];
        }

        @Override
        public void setInt(int index, int value) {
            array[index] = value;
        }

        @Override
        public void setDouble(int index, double value) {
            array[index] = (int) value;
        }

        @Override
        public void setLong(int index, long value) {
            array[index] = (int) value;
        }
    }

    static class GenericBoolean extends Generic<Boolean> implements Array.Boolean{

        GenericBoolean(java.lang.Boolean[] array) {
            super(array);
        }

        @Override
        public boolean getBoolean(int index) {
            var value = array[index];
            return value != null && value;
        }

        @Override
        public void setBoolean(int index, boolean value) {
            array[index] = value;
        }
    }

    static class GenericNumber<E extends java.lang.Number> extends Generic<E> implements Array.Number<E> {
        private final IntFunction<E> intTransformer;
        private final DoubleFunction<E> doubleTransformer;
        private final LongFunction<E> longTransformer;

        GenericNumber(E[] array, IntFunction<E> intTransformer, DoubleFunction<E> doubleTransformer, LongFunction<E> longTransformer) {
            super(array);
            this.intTransformer = intTransformer;
            this.doubleTransformer = doubleTransformer;
            this.longTransformer = longTransformer;
        }

        @Override
        public E get(int index) {
            return array[index];
        }

        @Override
        public void set(int index, E value) {
            array[index] = value;
        }

        @Override
        public Stream<E> stream() {
            return Stream.of(array);
        }

        @Override
        public int length() {
            return array.length;
        }

        @Override
        public int getInt(int index) {
            E value = array[index];
            return value == null ? 0 : value.intValue();
        }

        @Override
        public double getDouble(int index) {
            E value = array[index];
            return value == null ? 0 : value.doubleValue();
        }

        @Override
        public long getLong(int index) {
            E value = array[index];
            return value == null ? 0 : value.longValue();
        }

        @Override
        public void setInt(int index, int value) {
            array[index] = intTransformer.apply(value);
        }

        @Override
        public void setDouble(int index, double value) {
            array[index] = doubleTransformer.apply(value);
        }

        @Override
        public void setLong(int index, long value) {
            array[index] = longTransformer.apply(value);
        }
    }

    static class Generic<E> extends AbstractArray<E> implements Array<E>{
        final E[] array;

        Generic(E[] array) {
            this.array = array;
        }

        @Override
        public E get(int index) {
            return array[index];
        }

        @Override
        public void set(int index, E value) {
            array[index] = value;
        }

        @Override
        public Stream<E> stream() {
            return Stream.of(array);
        }

        @Override
        public int length() {
            return array.length;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }

        @Override
        public Object nativeArray() {
            return array;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E[] copyBoxed(int start, int length) {
            Objects.checkFromToIndex(start,start+length,length());
            E[] array = (E[]) java.lang.reflect.Array.newInstance(this.array.getClass().componentType(),length);
            System.arraycopy(this.array,start,array,0,length);
            return array;
        }

        @Override
        public Object copy(int start, int length) {
            return copyBoxed(start,length);
        }
    }

    private abstract static class Primitive<E> extends AbstractArray<E> implements Array<E> {

        @Override
        public boolean isPrimitive() {
            return true;
        }
    }

    private abstract static class AbstractArray<E> implements Array<E> {

        private Reference<List<E>> listReference;

        @Override
        public List<E> toList() {
            List<E> list = listReference == null ? null : listReference.get();
            if(list == null)
                listReference = new SoftReference<>(list = new ArrayFactory.ArrayList<>(this));
            return list;
        }
    }

    private static class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess {
        private final Array<E> array;

        private ArrayList(Array<E> array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return array.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return array.iterator();
        }

        @Override
        public Object[] toArray() {
            return array.copyBoxed();
        }

        @Override
        public boolean add(E e) {
            throw new UnsupportedOperationException("add");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("remove");
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return array.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException("addAll");
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            throw new UnsupportedOperationException("addAll");
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException("removeAll");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException("retainAll");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("clear");
        }

        @Override
        public E get(int index) {
            return array.get(index);
        }

        @Override
        public E set(int index, E element) {
            return array.replace(index,element);
        }

        @Override
        public void add(int index, E element) {
            throw new UnsupportedOperationException("add");
        }

        @Override
        public E remove(int index) {
            throw new UnsupportedOperationException("remove");
        }

        @Override
        public int indexOf(Object o) {
            return array.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return array.lastIndexOf(o);
        }
    }


    static class Iterators {

        static class Iterator<E> implements java.util.Iterator<E> {

            private final Array<E> array;
            private int index = -1;

            Iterator(Array<E> array) {
                this.array = array;
            }

            @Override
            public boolean hasNext() {
                return index < array.length();
            }

            @Override
            public E next() {
                return array.get(++index);
            }
        }

        private static class PrimitiveIterator {
            final Array.Number<?> array;
            int index = -1;

            private PrimitiveIterator(Array.Number<?> array) {
                this.array = array;
            }

            public boolean hasNext() {
                return index < array.length();
            }
        }

        static class PrimitiveIntIterator extends Iterators.PrimitiveIterator implements java.util.PrimitiveIterator.OfInt {

            PrimitiveIntIterator(Array.Number<?> array) {
                super(array);
            }

            @Override
            public int nextInt() {
                return array.getInt(++index);
            }
        }
        static class PrimitiveDoubleIterator extends Iterators.PrimitiveIterator implements java.util.PrimitiveIterator.OfDouble {


            PrimitiveDoubleIterator(Array.Number<?> array) {
                super(array);
            }

            @Override
            public double nextDouble() {
                return array.getDouble(++index);
            }
        }
        static class PrimitiveLongIterator extends Iterators.PrimitiveIterator implements java.util.PrimitiveIterator.OfLong {

            PrimitiveLongIterator(Array.Number<?> array) {
                super(array);
            }

            @Override
            public long nextLong() {
                return array.getLong(++index);
            }
        }
    }
}
