package com.frechsack.dev.util.array;

public interface Characters extends Array<Character>
{
    char getChar(int index);

    void setChar(int index, char element);

    char[] toCharArray();

    boolean equals(char[] array);
}
