package com.frechsack.dev.util.array;

import java.util.Comparator;

public interface BooleanArray extends Array<Boolean>
{
    boolean getBoolean(int index);

    boolean setBoolean(int index, boolean element);

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
