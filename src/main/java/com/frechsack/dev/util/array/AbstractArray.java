package com.frechsack.dev.util.array;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractArray<E> implements Array<E>
{
    private Reference<ArrayList> arrayListReference;

    protected abstract E getVoid();

    protected static final int STREAM_PREFERRED_LENGTH = 10;

    @Override
    public int indexOf(Object element)
    {
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (Objects.equals(element, get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length()).parallel().filter(index -> Objects.equals(element, get(index))).findAny().orElse(-1);
    }

    @Override
    public int lastIndexOf(Object element)
    {
        for (int i = length() - 1; i > 0; i--)
        {
            if (Objects.equals(element, get(i))) return i;
        }
        return -1;
    }

    @Override
    public boolean contains(Object element)
    {
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++) if (Objects.equals(element, get(i))) return true;
            return false;
        }
        return IntStream.range(0, length()).parallel().anyMatch(index -> Objects.equals(element, get(index)));
    }

    @Override
    public void clear()
    {
        E voidValue = getVoid();
        for (int i = 0; i < length(); i++) set(i, voidValue);
    }

    @Override
    public Stream<E> stream()
    {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Stream<E> parallelStream()
    {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public boolean equals(E[] array)
    {
        if (array.length != length()) return false;
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++) if (!Objects.equals(array[i], get(i))) return false;
            return true;
        }
        return IntStream.range(0, length()).parallel().allMatch(index -> Objects.equals(array[index], get(index)));
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o instanceof Collection) return asList().equals(o);
        if (!(o instanceof Array)) return false;
        // Compare collection
        return AbstractArray.this.parallelStream().allMatch(((Array<?>) o)::contains);
    }

    @Override
    public Spliterator<E> spliterator()
    {
        return Spliterators.spliterator(iterator(), length(), Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray()
    {
        E[] array = (E[]) java.lang.reflect.Array.newInstance(this.getClass().getComponentType(), length());
        IntStream.range(0, length()).parallel().forEach(index -> array[index] = get(index));
        return array;
    }

    @Override
    public List<E> asList()
    {
        ArrayList arrayList = arrayListReference == null ? null : arrayListReference.get();
        if (arrayList == null) arrayListReference = new SoftReference<>(new ArrayList());
        return arrayList;
    }

    private class ArrayList extends AbstractList<E>
    {
        @Override
        public E set(int index, E element)
        {
            return AbstractArray.this.set(index, element);
        }

        @Override
        public E get(int index)
        {
            return AbstractArray.this.get(index);
        }

        @Override
        public int size()
        {
            return AbstractArray.this.length();
        }

        @Override
        public int indexOf(Object o)
        {
            return AbstractArray.this.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o)
        {
            return AbstractArray.this.lastIndexOf(o);
        }

        @Override
        public void clear()
        {
            AbstractArray.this.clear();
        }

        @Override
        public boolean isEmpty()
        {
            return length() == 0;
        }

        @Override
        public boolean contains(Object o)
        {
            return AbstractArray.this.contains(o);
        }

        @Override
        public Object[] toArray()
        {
            return AbstractArray.this.toArray();
        }

        @Override
        public <T> T[] toArray(IntFunction<T[]> generator)
        {
            return toArray(generator.apply(length()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a)
        {
            final T[] clone = a.length >= length() ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), length());
            // Stream
            IntStream.range(0, length()).parallel().forEach(index -> a[index] = (T) AbstractArray.this.get(index));
            return clone;
        }

        @Override
        public Stream<E> stream()
        {
            return AbstractArray.this.stream();
        }

        @Override
        public Stream<E> parallelStream()
        {
            return AbstractArray.this.parallelStream();
        }

        @Override
        public void sort(Comparator<? super E> c)
        {
            AbstractArray.this.sort(c);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o || AbstractArray.this == o) return true;
            if (o instanceof List) return super.equals(o);
            if (o instanceof Array) return equals(((Array<?>) o).asList());
            if (!(o instanceof Collection)) return false;
            // Compare collection
            return AbstractArray.this.parallelStream().allMatch(((Collection<?>) o)::contains);
        }
    }
}
