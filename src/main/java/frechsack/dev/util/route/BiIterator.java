package frechsack.dev.util.route;

import frechsack.dev.util.array.Array;

import java.util.*;
import java.util.function.*;

/**
 * A BiIterator acts like an {@link Iterator}.
 * <p>
 * Unlike an Iterator it allows to move forward and backwards.
 * When a BiIterator is initialized, it´s position is placed before the first element in it´s sequence.
 *
 * @param <E> The type of this sequence.
 */
public interface BiIterator<E> extends Iterator<E>, Enumeration<E>
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

    /**
     * Creates a dynamic sized BiIterator for the specified {@link IntFunction} and {@link IntSupplier}.
     * @param items This function is used to access elements on the passed position.
     * @param size This function is used to read the length. The value returned by this function must not be constant.
     * @param <E> The BiIterator´s element class type.
     * @return Returns a BiIterator for the specified items.
     * @see IntFunction
     * @see IntSupplier
     */
    static <E> BiIterator<E> of(IntFunction<E> items, IntSupplier size){
        Objects.requireNonNull(items);
        Objects.requireNonNull(size);
        return new BiIteratorFactory.DynamicIndexBiIterator<>(items, size, null);
    }

    /**
     * Creates a fixed sized BiIterator for the specified {@link IntFunction}.
     * @param items This function is used to access elements on the passed position.
     * @param size The size of the Route.
     * @param <E> The BiIterator´s element class type.
     * @return Returns a BiIterator for the specified items.
     * @see IntFunction
     */
    static <E> BiIterator<E> of(IntFunction<E> items, int size){
        Objects.requireNonNull(items);
        return new BiIteratorFactory.FixedIndexBiIterator<>(items, size, null);
    }
    /**
     * Creates a dynamic sized BiIterator for the specified {@link IntFunction} and {@link IntSupplier}.
     * @param items This function is used to access elements on the passed position.
     * @param size This function is used to read the length. The value returned by this function must not be constant.
     * @param remove This function is used to remove element. The returned int by {@link IntUnaryOperator#applyAsInt(int)} is used as the BiIterator´s new index.
     * @param <E> The BiIterator´s element class type.
     * @return Returns a Route for the specified items.
     * @see IntFunction
     * @see IntSupplier
     * @see IntUnaryOperator
     */
    static <E> BiIterator<E> of(IntFunction<E> items, IntSupplier size, IntUnaryOperator remove){
        Objects.requireNonNull(items);
        Objects.requireNonNull(size);
        return new BiIteratorFactory.DynamicIndexBiIterator<>(items, size, remove);
    }
    /**
     * Creates a fixed sized BiIterator for the specified {@link IntFunction} and {@link IntSupplier}.
     * @param items This function is used to access elements on the passed position.
     * @param size The size of the Route.
     * @param remove This function is used to remove element. The returned int by {@link IntUnaryOperator#applyAsInt(int)} is used as the BiIterator´s new index.
     * @param <E> The BiIterator´s element class type.
     * @return Returns a BiIterator for the specified items.
     * @see IntFunction
     * @see IntSupplier
     * @see IntUnaryOperator
     */
    static <E> BiIterator<E> of(IntFunction<E> items, int size, IntConsumer remove){
        Objects.requireNonNull(items);
        return new BiIteratorFactory.FixedIndexBiIterator<>(items, size, remove);
    }

    /**
     * Creates a BiIterator for the whole range of the specified Array. The BiIterator supports the {@link #remove()} operation.
     *
     * @param array The Array.
     * @param <E>   The Array´s element type.
     * @return Returns a BiIterator for the specified Array.
     */
    static <E> BiIterator<E> of(Array<E> array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>(array::get, array.length(), i -> array.set(i, null));
    }

    /**
     * Creates a BiIterator for the whole {@link Collection}. The Route will use the Collection´s {@link Iterator} to travel threw the elements.
     *
     * @param collection The Collection.
     * @param <E>        The Collection´s element type.
     * @return Returns a BiIterator for the specified Collection.
     */
    static <E> BiIterator<E> of(Collection<E> collection)
    {
        if (collection instanceof List)
        {
            ListIterator<E> iterator = ((List<E>) collection).listIterator();
            //noinspection ConstantConditions
            if (iterator != null) return of(iterator);
        }
        return of(collection.iterator());
    }

    /**
     * Returns a BiIterator that wraps the specified {@link ListIterator} into a Route.
     *
     * @param iterator The ListIterator.
     * @param <E>      The Route´s element type.
     * @return Returns a BiIterator for the specified ListIterator.
     */
    static <E> BiIterator<E> of(ListIterator<E> iterator)
    {
        return new BiIteratorFactory.ListIteratorBiIterator<>(iterator);
    }

    /**
     * Returns a BiIterator that wraps the specified {@link Iterator} into a Route.
     *
     * @param iterator The Iterator.
     * @param <E>      The Route´s element type.
     * @return Returns a BiIterator for the specified Iterator.
     */
    static <E> BiIterator<E> of(Iterator<E> iterator)
    {
        if (iterator instanceof ListIterator) return of((ListIterator<E>) iterator);
        return new BiIteratorFactory.IteratorBiIterator<>(iterator);
    }

    /**
     * Returns a BiIterator for the specified array.
     *
     * @param array The array.
     * @param <E>   The array´s element type.
     * @return Returns a BiIterator for the specified array.
     */
    static <E> BiIterator<E> of(E[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>(i -> array[i], array.length, i -> array[i] = null);
    }

    /**
     * Returns a BiIterator for the specified array.
     *
     * @param array           The array.
     * @param remove This Operation is performed, when an element should be removed. The operation should return the new index after removing.
     * @param <E>             The array´s element type.
     * @return Returns a BiIterator for the specified array.
     */
    static <E> BiIterator<E> of(E[] array, IntUnaryOperator remove)
    {
        return new BiIteratorFactory.DynamicIndexBiIterator<>(i -> array[i], () -> array.length, remove);
    }

    /**
     * Returns a BiIterator for the specified element.
     *
     * @param e   The element. The element may be null.
     * @param <E> The element´s class type.
     * @return Returns a BiIterator for the specified element.
     */
    static <E> BiIterator<E> of(E e)
    {
        return new BiIteratorFactory.SingleBiIterator<>(e);
    }

    /**
     * Returns a BiIterator for the specified {@code boolean}.
     *
     * @param v The {@code boolean}.
     * @return Returns a BiIterator for the specified {@code boolean}.
     */
    static BiIterator<Boolean> of(boolean v)
    {
        return new BiIteratorFactory.SingleBiIterator<>(v);
    }

    /**
     * Returns a BiIterator for the specified {@code boolean} array.
     *
     * @param array The array.
     * @return Returns a BiIterator for the specified {@code boolean} array.
     */
    static BiIterator<Boolean> of(boolean[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>(i -> array[i], array.length, i -> array[i] = false);
    }

    /**
     * Returns a BiIterator for the specified {@code char}.
     *
     * @param v The {@code char}.
     * @return Returns a BiIterator for the specified {@code char}.
     */
    static BiIterator<Character> of(char v)
    {
        return new BiIteratorFactory.SingleBiIterator<>(v);
    }

    /**
     * Returns a BiIterator for the specified {@code char} array.
     *
     * @param array The array.
     * @return Returns a BiIterator for the specified {@code char} array.
     */
    static BiIterator<Character> of(char[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>(i -> array[i], array.length, i -> array[i] = '\u0000');
    }

    /**
     * Returns a BiIterator for the specified {@code byte}.
     *
     * @param v The {@code byte}.
     * @return Returns a BiIterator for the specified {@code byte}.
     */
    static BiIterator<Byte> of(byte v)
    {
        return new BiIteratorFactory.SingleBiIterator<>(v);
    }

    /**
     * Returns a BiIterator for the specified {@code byte} array.
     *
     * @param array The array.
     * @return Returns a BiIterator for the specified {@code byte}  array.
     */
    static BiIterator<Byte> of(byte[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    /**
     * Returns a BiIterator for the specified {@code short}.
     *
     * @param v The {@code short}.
     * @return Returns a BiIterator for the specified {@code short}.
     */
    static BiIterator<Short> of(short v)
    {
        return new BiIteratorFactory.SingleBiIterator<>(v);
    }

    /**
     * Returns a BiIterator for the specified {@code short} array.
     *
     * @param array The array.
     * @return Returns a BiIterator for the specified {@code short} array.
     */
    static BiIterator<Short> of(short[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    /**
     * Returns a BiIterator for the specified {@code int}.
     *
     * @param v The {@code int}.
     * @return Returns a BiIterator for the specified {@code int}.
     */
    static BiIterator<Integer> of(int v)
    {
        return new BiIteratorFactory.SingleBiIterator<>(v);
    }

    /**
     * Returns a BiIterator for the specified {@code int} array.
     *
     * @param array The array.
     * @return Returns a BiIterator for the specified {@code int} array.
     */
    static BiIterator<Integer> of(int[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    /**
     * Returns a BiIterator for the specified {@code float}.
     *
     * @param v The {@code float}.
     * @return Returns a BiIterator for the specified {@code float}.
     */
    static BiIterator<Float> of(float v)
    {
        return new BiIteratorFactory.SingleBiIterator<>(v);
    }

    /**
     * Returns a BiIterator for the specified {@code float} array.
     *
     * @param array The array.
     * @return Returns a BiIterator for the specified {@code float} array.
     */
    static BiIterator<Float> of(float[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    /**
     * Returns a BiIterator for the specified {@code double}.
     *
     * @param v The {@code double}.
     * @return Returns a BiIterator for the specified {@code double}.
     */
    static BiIterator<Double> of(double v)
    {
        return new BiIteratorFactory.SingleBiIterator<>(v);
    }

    /**
     * Returns a BiIterator for the specified {@code double} array.
     *
     * @param array The array.
     * @return Returns a BiIterator for the specified {@code double} array.
     */
    static BiIterator<Double> of(double[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

    /**
     * Returns a BiIterator for the specified {@code long}.
     *
     * @param v The {@code long}.
     * @return Returns a BiIterator for the specified {@code long}.
     */
    static BiIterator<Long> of(long v)
    {
        return new BiIteratorFactory.SingleBiIterator<>(v);
    }

    /**
     * Returns a BiIterator for the specified {@code long} array.
     *
     * @param array The array.
     * @return Returns a BiIterator for the specified {@code long} array.
     */
    static BiIterator<Long> of(long[] array)
    {
        return new BiIteratorFactory.FixedIndexBiIterator<>((i) -> array[i], array.length, i -> array[i] = 0);
    }

}
