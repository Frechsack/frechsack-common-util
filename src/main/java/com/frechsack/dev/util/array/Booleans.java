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
