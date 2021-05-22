package com.frechsack.dev.util.route;

import java.util.function.Consumer;

public class SingleRoute<E> implements Route<E>
{
    private E single;
    private boolean isInitial = true;

    public SingleRoute(E value)
    {
        setSingle(value);
    }

    public void setSingle(E single)
    {
        this.single = single;
        isInitial = true;
    }

    @Override
    public boolean hasNext()
    {
        return isInitial;
    }

    @Override
    public boolean hasPrevious()
    {
        return false;
    }

    @Override
    public E next()
    {
        if (isInitial)
        {
            isInitial = false;
            return single;
        }
        throw new ArrayIndexOutOfBoundsException(1);
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action)
    {
        if (isInitial) action.accept(single);
    }

    @Override
    public E previous()
    {
        throw new ArrayIndexOutOfBoundsException(1);
    }

    @Override
    public E last()
    {
        isInitial = false;
        return single;
    }

    @Override
    public E first()
    {
        return last();
    }
}
