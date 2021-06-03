package com.frechsack.dev.util.route;

import com.frechsack.dev.util.array.Array;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

/**
 * A Route acts like an {@link Iterator}.
 * <p>
 * Unlike an Iterator it allows to move forward and backwards.
 * When a Cursor is initialized, it´s position is placed before the first element in it´s sequence.
 *
 * @param <E> The type of this sequence.
 */
public interface Route<E> extends Iterator<E>, Enumeration<E>
{
    /**
     * {@inheritDoc}
     */
    boolean hasNext();

    /**
     * Returns {@code true} if the iteration has already traveled through an element.
     * When this returns true,{@link #previous()} will return a valid object.
     *
     * @return {@code true} if the iteration has already traveled an element.
     */
    boolean hasPrevious();

    /**
     * {@inheritDoc}
     */
    E next();

    /**
     * Returns the previous element in this sequence.
     *
     * @return the previous element in the iteration.
     */
    E previous();

    /**
     * Moves this cursor to the last valid position.
     *
     * @return The last available element in this sequence.
     */
    default E last()
    {
        E last = null;
        while (hasNext()) last = next();
        return last;
    }

    /**
     * Moves this cursor to the first valid position.
     *
     * @return The first available element in this sequence.
     */
    default E first()
    {
        E first = null;
        while (hasPrevious()) first = previous();
        return first;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean hasMoreElements()
    {
        return hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default E nextElement()
    {
        return next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Iterator<E> asIterator()
    {
        return this;
    }

    default void forEachPrevious(Consumer<? super E> action)
    {
        Objects.requireNonNull(action);
        while (hasPrevious()) action.accept(previous());
    }

    static <E> Route<E> of(Array<E> array)
    {
        return new RouteFactory.FixedIndexRoute<>(array::get, array.length(), i -> array.set(i, null));
    }

    static <E> Route<E> of(Collection<E> collection)
    {
        if (collection instanceof List)
        {
            ListIterator<E> iterator = ((List<E>) collection).listIterator();
            //noinspection ConstantConditions
            if (iterator != null) return of(iterator);
        }
        return of(collection.iterator());
    }

    static <E> Route<E> of(ListIterator<E> iterator)
    {
        return new RouteFactory.ListIteratorRoute<>(iterator);
    }

    static <E> Route<E> of(Iterator<E> iterator)
    {
        if (iterator instanceof ListIterator) return of((ListIterator<E>) iterator);
        return new RouteFactory.IteratorRoute<>(iterator);
    }

    static <E> Route<E> of(E[] array)
    {
        return new RouteFactory.FixedIndexRoute<>(i -> array[i], array.length, i -> array[i] = null);
    }

    static <E> Route<E> of(E[] array, IntUnaryOperator removeOperation)
    {
        return new RouteFactory.DynamicIndexRoute<>(i -> array[i], () -> array.length, removeOperation);
    }

    static <E> Route<E> of(E e)
    {
        return new RouteFactory.SingleRoute<>(e);
    }

    static Route<Boolean> of(boolean v)
    {
        return new RouteFactory.SingleRoute<>(v);
    }

    static Route<Boolean> of(boolean[] array)
    {
        return new RouteFactory.FixedIndexRoute<>(i -> array[i], array.length, i -> array[i] = false);
    }

    static Route<Character> of(char v)
    {
        return new RouteFactory.SingleRoute<>(v);
    }

    static Route<Character> of(char[] array)
    {
        return new RouteFactory.FixedIndexRoute<>(i -> array[i], array.length, i -> array[i] = '\u0000');
    }

    static Route<Byte> of(byte v)
    {
        return new RouteFactory.SingleRoute<>(v);
    }

    static Route<Byte> of(byte[] array)
    {
        return new RouteFactory.FixedIndexRoute<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    static Route<Short> of(short v)
    {
        return new RouteFactory.SingleRoute<>(v);
    }

    static Route<Short> of(short[] array)
    {
        return new RouteFactory.FixedIndexRoute<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    static Route<Integer> of(int v)
    {
        return new RouteFactory.SingleRoute<>(v);
    }

    static Route<Integer> of(int[] array)
    {
        return new RouteFactory.FixedIndexRoute<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    static Route<Float> of(float v)
    {
        return new RouteFactory.SingleRoute<>(v);
    }

    static Route<Float> of(float[] array)
    {
        return new RouteFactory.FixedIndexRoute<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    static Route<Double> of(double v)
    {
        return new RouteFactory.SingleRoute<>(v);
    }

    static Route<Double> of(double[] array)
    {
        return new RouteFactory.FixedIndexRoute<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    static Route<Long> of(long v)
    {
        return new RouteFactory.SingleRoute<>(v);
    }

    static Route<Long> of(long[] array)
    {
        return new RouteFactory.FixedIndexRoute<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

}
