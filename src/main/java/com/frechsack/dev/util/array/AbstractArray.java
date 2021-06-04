package com.frechsack.dev.util.array;

import com.frechsack.dev.util.Pair;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractArray<E> implements Array<E>
{
    private Reference<ArrayList> arrayListReference;

    protected abstract E getVoid();

    protected static final int STREAM_PREFERRED_LENGTH = 4096;

    protected AbstractArray() {}

    @Override
    public E getAndSet(int index, E element)
    {
        E last = get(index);
        set(index, element);
        return last;
    }

    @Override
    public int firstIndexOf(Object element)
    {
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (Objects.equals(element, get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length()).parallel().filter(index -> Objects.equals(element, get(index))).findFirst().orElse(-1);
    }

    @Override
    public int lastIndexOf(Object element)
    {
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = length() - 1; i > 0; i--)
            {
                if (Objects.equals(element, get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length())
                        .parallel()
                        .map(index -> length() - index - 1)
                        .filter(index -> Objects.equals(element, get(index)))
                        .findFirst()
                        .orElse(-1);
    }

    @Override
    public int indexOf(int start, int end, Object element)
    {
        if (end - start < STREAM_PREFERRED_LENGTH)
        {
            for (int i = start; i < end; i++)
            {
                if (Objects.equals(element, get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(start, end).parallel().filter(index -> Objects.equals(element, get(index))).findAny().orElse(-1);
    }

    @Override
    public int indexOf(int start, int end, Predicate<E> predicate)
    {
        if (end - start < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (predicate.test(get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(start, end).parallel().filter(index -> predicate.test(get(index))).findAny().orElse(-1);
    }

    @Override
    public int lastIndexOf(Predicate<E> predicate)
    {
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = length() - 1; i > 0; i--)
            {
                if (predicate.test(get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length()).parallel().map(index -> length() - index - 1).filter(index -> predicate.test(get(index))).findFirst().orElse(-1);
    }

    @Override
    public int firstIndexOf(Predicate<E> predicate)
    {
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                if (predicate.test(get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length()).parallel().filter(index -> predicate.test(get(index))).findFirst().orElse(-1);
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
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++) set(i, voidValue);
        }
        else
        {
            IntStream.range(0, length()).parallel().forEach(index -> set(index, voidValue));
        }
    }

    @Override
    public void forEach(Consumer<? super E> action)
    {
        if (length() < STREAM_PREFERRED_LENGTH) for (int i = 0; i < length(); i++) action.accept(get(i));
        else stream().forEach(action);
    }

    @Override
    public Stream<E> stream()
    {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Stream<Pair<Integer, E>> streamIndices()
    {
        return IntStream.range(0, length()).mapToObj(index -> Pair.of(index, get(index)));
    }

    @Override
    public Stream<Pair<Integer, E>> parallelStreamIndices()
    {
        return IntStream.range(0, length()).parallel().mapToObj(index -> Pair.of(index, get(index)));
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
    public final boolean equals(Object o)
    {
        if (this == o) return true;
        if (o instanceof Collection) return asList().equals(o);
        if (asArray() == o) return true;
        if (!(o instanceof Array)) return false;
        // Compare collection
        Array<?> oArray = (Array<?>) o;
        if (oArray.length() != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if (!Objects.equals(oArray.get(i), get(i))) return false;
        }
        return true;
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
        E[] array = (E[]) java.lang.reflect.Array.newInstance(asArray().getClass().getComponentType(), length());
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                array[i] = get(i);
            }
        }
        else IntStream.range(0, length()).parallel().forEach(index -> array[index] = get(index));
        return array;
    }

    @Override
    public List<E> asList()
    {
        ArrayList arrayList = arrayListReference == null ? null : arrayListReference.get();
        if (arrayList == null) arrayListReference = new SoftReference<>(arrayList = new ArrayList());
        return arrayList;
    }

    @Override
    public void sort(Comparator<? super E> c)
    {
        java.util.ArrayList<E> ls = new java.util.ArrayList<>(length());
        stream().sorted(c).collect(Collectors.toCollection(() -> ls));
        // Replace
        IntStream.range(0, ls.size()).parallel().forEach(index -> set(index, ls.get(index)));
    }

    @Override
    public void sortReverse()
    {
        sort();
        reverse();
    }

    @Override
    public void sortReverse(Comparator<? super E> c)
    {
        java.util.ArrayList<E> ls = new java.util.ArrayList<>(length());
        stream().sorted(c).collect(Collectors.toCollection(() -> ls));
        // Replace
        IntStream.range(0, ls.size()).parallel().forEach(index -> set(length() - 1 - index, ls.get(index)));
    }

    @Override
    public void fill(E element)
    {
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                set(i, element);
            }
        }
        else IntStream.range(0, length()).parallel().forEach(index -> set(index, element));
    }

    @Override
    public void reverse()
    {
        IntStream.range(0, length() / 2).parallel().forEach(index ->
                                                            {
                                                                int swapIndex = length() - 1 - index;
                                                                E swap = get(index);
                                                                set(index, getAndSet(swapIndex, swap));
                                                            });

    }

    @Override
    public void transform(Function<E, E> mapper)
    {
        if (length() < STREAM_PREFERRED_LENGTH)
        {
            for (int i = 0; i < length(); i++)
            {
                set(i, mapper.apply(get(i)));
            }
        }
        else IntStream.range(0, length()).parallel().forEach(index -> set(index, mapper.apply(get(index))));
    }

    @Override
    public final Iterator<E> iterator()
    {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<E>
    {
        private int index = -1;

        @Override
        public boolean hasNext()
        {
            return index + 1 < length();
        }

        @Override
        public E next()
        {
            return get(++index);
        }

        @Override
        public void remove()
        {
            set(index, getVoid());
        }
    }

    private class ArrayList extends AbstractList<E>
    {
        @Override
        public E set(int index, E element)
        {
            return AbstractArray.this.getAndSet(index, element);
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
