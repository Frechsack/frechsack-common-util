package com.frechsack.dev.cursor;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * A Cursor acts like an {@link Iterator}.
 * <p>
 * Unlike an Iterator it allows to move forward and backwards.
 * When a Cursor is initialized, it´s position is placed before the first element in it´s sequence.
 *
 * @param <E> The type of this sequence.
 */
public interface Cursor<E> extends Iterator<E>, Enumeration<E>
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
    E last();

    /**
     * Moves this cursor to the first valid position.
     *
     * @return The firsst available element in this sequence.
     */
    E first();

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
}
