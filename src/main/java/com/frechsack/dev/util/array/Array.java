package com.frechsack.dev.util.array;

import com.frechsack.dev.util.route.Routable;

import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public interface Array<E> extends Iterable<E>, Routable<E>, Function<Integer, E>
{
    static <E> Array<E> of(E... array)
    {
        return new ArrayFactory.GenericArray<>(array, true);
    }

    static <E> Array<E> of(boolean isReference, E... array)
    {
        return new ArrayFactory.GenericArray<>(array, isReference);
    }

    static <E extends Number> NumericArray<E> of(Number[] numbers)
    {
        // TODO Generic Number array?
        return null;
    }

    // TODO: A lot of statics
    static NumericArray<Integer> of(boolean isReference, int... array){
        return new ArrayFactory.IntArray(array,isReference);
    }
    static NumericArray<Integer> of(int... array){
        return new ArrayFactory.IntArray(array,true);
    }
    static NumericArray<Integer> ofInt(int length){
        return new ArrayFactory.IntArray(length);
    }
    static NumericArray<Byte> of(boolean isReference, byte... array){
        return new ArrayFactory.ByteArray(array,isReference);
    }
    static NumericArray<Byte> of(byte... array){
        return new ArrayFactory.ByteArray(array,true);
    }
    static NumericArray<Byte> ofBoolean(int length){
        return new ArrayFactory.ByteArray(length);
    }



    int indexOf(Object element);

    int lastIndexOf(Object element);

    void clear();

    boolean contains(Object element);

    E[] toArray();

    Object asArray();

    int length();

    E get(int index);

    E set(int index, E element);

    Stream<E> stream();

    Stream<E> parallelStream();

    boolean isPrimitive();

    IntFunction<E[]> generator();

    @Override
    Spliterator<E> spliterator();

    boolean equals(E[] array);

    List<E> asList();

    void sort(Comparator<? super E> c);

    @Override
    default E apply(Integer index)
    {
        return get(index);
    }

    void sort();
}
