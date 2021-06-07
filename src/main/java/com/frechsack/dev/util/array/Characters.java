package com.frechsack.dev.util.array;

/**
 * Characters is a character implementation of an {@link Array}.
 * <p>
 * It allows access to primitive read and write operations with char.
 *
 * @author Frechsack
 */
public interface Characters extends Array<Character>
{
    /**
     * Returns the element on the specified index as a char.
     *
     * @param index The element´s index.
     * @return Returns the element.
     */
    default char getChar(int index){
        Character ch = get(index);
        return ch == null ? '\u0000' : ch;
    }

    /**
     * Sets the specified char on the given position.
     *
     * @param index   The position.
     * @param element The element.
     */
    default void setChar(int index, char element){
        set(index,element);
    }

    /**
     * Sets the specified element on the given position and returns the previous value.
     *
     * @param index   The position.
     * @param element The new element.
     * @return Returns the previous element.
     */
    default char getAndSetChar(int index, char element)
    {
        char last = getChar(index);
        setChar(index, element);
        return last;
    }

    /**
     * Returns a copy of this Array´s elements as chars.
     *
     * @return Returns a copy with the same size and content as this Array.
     */
   default char[] toCharArray(){
       char[] clone = new char[length()];
       for (int i = 0; i < length(); i++)
           clone[i] = getChar(i);
       return clone;
   }

    /**
     * Checks if this Array´s contents and the specified array are equal. Those two array´s are considered as equal if their elements and order is equal.
     * @param array The comparison array.
     * @return Returns true if this Array and the specified one are equal, else false.
     */
    default boolean equals(char[] array){
        if(array == null) return false;
        if(array.length != length()) return false;
        for (int i = 0; i < length(); i++)
        {
            if(array[i] != getChar(i)) return false;
        }
        return true;
    }
}
