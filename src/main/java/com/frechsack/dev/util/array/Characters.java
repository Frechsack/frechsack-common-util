package com.frechsack.dev.util.array;

public interface Characters extends Array<Character>
{
    char getChar(int index);

    void setChar(int index, char element);

    default char getAndSetChar(int index, char element)
    {
        char last = getChar(index);
        setChar(index, element);
        return last;
    }

    char[] toCharArray();

    boolean equals(char[] array);
}
