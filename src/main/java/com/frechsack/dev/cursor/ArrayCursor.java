package com.frechsack.dev.cursor;

import java.util.function.Consumer;

public class ArrayCursor<E> implements Cursor<E>
{
    private E[] array;
    private int index;

    public ArrayCursor(E[] array)
    {
        setArray(array);
    }

    public void setArray(E[] array)
    {
        this.array = array;
        index = -1;
    }

    public E[] getArray()
    {
        return array;
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action)
    {
        for (int i = index; i < array.length; i++) action.accept(array[i]);
    }

    @Override
    public boolean hasNext()
    {
        return index < array.length - 1;
    }

    @Override
    public boolean hasPrevious()
    {
        return index > 0;
    }

    @Override
    public E next()
    {
        return array[++index];
    }

    @Override
    public E previous()
    {
        return array[--index];
    }

    @Override
    public E last()
    {
        return array[index = array.length - 1];
    }

    @Override
    public E first()
    {
        return array[index = 0];
    }
}
