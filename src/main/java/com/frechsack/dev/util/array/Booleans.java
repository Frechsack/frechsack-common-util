package com.frechsack.dev.util.array;

import java.util.Comparator;

/**
 * Booleans is a boolean implementation of an {@link Array}.
 * <p>
 * It allows access to primitive read and write operations with boolean.
 *
 * @author Frechsack
 */
public interface Booleans extends Array<Boolean>
{
    /**
     * Returns the element on the specified index as a boolean.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    boolean getBoolean(int index);

    /**
     * Sets the specified boolean on the given position.
     *
     * @param index   The position.
     * @param element The element.
     */
    void setBoolean(int index, boolean element);

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default boolean getAndSetBoolean(int index, boolean element)
    {
        boolean last = getBoolean(index);
        setBoolean(index, element);
        return last;
    }

    /**
     * Performs a logical-and-operation on every index in this Array and stores the operation result on the iterated index.
     * @param b The operand.
     */
    default void and(boolean b)
    {
        for (int i = 0; i < length(); i++)
        {
            setBoolean(i, b && getBoolean(i));
        }
    }
    /**
     * Performs a logical-or-operation on every index in this Array and stores the operation result on the iterated index.
     * @param b The operand.
     */
    default void or(boolean b)
    {
        for (int i = 0; i < length(); i++)
        {
            setBoolean(i, b || getBoolean(i));
        }
    }

    /**
     * Swaps any element in this Array. Elements who are equal to false will be swapped to true, and elements equal to true will be swapped to false.
     */
    default void swap()
    {
        for (int i = 0; i < length(); i++)
        {
            setBoolean(i, !getBoolean(i));
        }
    }

    /**
     * Returns a copy of this Array´s elements as booleans.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
    boolean[] toBooleanArray();

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    boolean equals(boolean[] array);

    @Override
    default void sort(Comparator<? super Boolean> c)
    {
        throw new UnsupportedOperationException("sort");
    }

    @Override
    default void sort()
    {
        throw new UnsupportedOperationException("sort");
    }
}
