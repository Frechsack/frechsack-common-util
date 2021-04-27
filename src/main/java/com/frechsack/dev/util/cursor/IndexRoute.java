package com.frechsack.dev.util.cursor;

import com.frechsack.dev.util.function.NumberSupplier;

import java.util.Objects;
import java.util.function.Function;

public class IndexRoute<E> implements Route<E>
{
    private final Function<Integer, E> items;
    private final NumberSupplier<?> size;
    private int index = -1;

    public IndexRoute(Function<Integer, E> items, NumberSupplier<?> size)
    {
        this.items = items;
        this.size = size;
    }

    @Override
    public boolean hasNext()
    {
        return size.getAsInt() > index + 1;
    }

    @Override
    public boolean hasPrevious()
    {
        return index - 1 >= 0;
    }

    @Override
    public E next()
    {
        int index = this.index+1;
        Objects.checkIndex(index, size.getAsInt());
        this.index = index;
        return items.apply(index);
    }

    @Override
    public E previous()
    {
        int index = this.index-1;
        Objects.checkIndex(index , size.getAsInt());
        this.index = index;
        return items.apply(index);
    }

    @Override
    public E last()
    {
        this.index = size.getAsInt()-2;
        return next();
    }

    @Override
    public E first()
    {
        this.index = -1;
        return next();
    }
}
