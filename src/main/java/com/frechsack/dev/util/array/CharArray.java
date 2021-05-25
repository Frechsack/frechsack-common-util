package com.frechsack.dev.util.array;

public interface CharArray extends Array<Character>
{
    char getChar(int index);

    char setChar(int index, char element);

    char[] toCharArray();

    boolean equals(char[] array);
}
