package com.frechsack.dev.util.array;

import java.util.Comparator;

public interface Booleans extends Array<Boolean>
{
    boolean getBoolean(int index);

    void setBoolean(int index, boolean element);

    default boolean getAndSetBoolean(int index, boolean element){
        boolean last = getBoolean(index);
        setBoolean(index,element);
        return last;
    }

    default void and(boolean b){
        for (int i = 0; i < length(); i++)
        {
            setBoolean(i,b && getBoolean(i));
        }
    }

    default void or(boolean b){
        for (int i = 0; i < length(); i++)
        {
            setBoolean(i,b || getBoolean(i));
        }
    }

    default void swap(){
        for (int i = 0; i < length(); i++)
        {
            setBoolean(i,!getBoolean(i));
        }
    }

    boolean[] toBooleanArray();

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
