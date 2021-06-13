package com.frechsack.dev.util.array;

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
   default boolean getBoolean(int index){
       Boolean b = get(index);
       return b != null && b;
   }

    /**
     * Sets the specified boolean on the given position.
     *
     * @param index   The position.
     * @param element The element.
     */
    default void setBoolean(int index, boolean element){
        set(index,element);
    }

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
     *
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
     *
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
   default boolean[] toBooleanArray(){
       boolean[] clone = new boolean[length()];
       for (int i = 0; i < length(); i++)
           clone[i] = getBoolean(i);
       return clone;
   }

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     *
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(boolean[] array){
        if(array == null) return false;
        if(array.length != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if(array[i] != getBoolean(i)) return false;
        }
        return true;
    }

    /**
     * When sorting a {@code boolean} Array, it is assumed that {@code false} is less than {@code true}.
     */
    @Override
    default void sort()
    {
        // false is less than true
        // Get amount of false
        int trueCount = 0;
        for (int i = 0; i < length(); i++) if (getBoolean(i)) trueCount++;
        for (int i = 0; i < length() - trueCount; i++)   setBoolean(i, false);
        for (int i = length()-trueCount; i < length(); i++)    setBoolean(i, true);

    }

    @Override
    default Booleans subArray(int fromIndex, int toIndex)
    {
        return new ArrayFactory.SubBooleans(this,fromIndex,toIndex);
    }
}
