package com.frechsack.dev.util.route;

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
    public final void remove()
    {
        index = remove(index);
    }

    /**
     * Attempt to remove an element from the underlying model.
     * @param index The index that should be removed.
     * @return Returns the new index. List based models should modify the index after removing an element.
     */
    protected int remove(int index)
    {
        throw new UnsupportedOperationException("remove");
    }

    @Override
    public final boolean hasNext()
    {
        return size.getAsInt() > index + 1;
    }

    @Override
    public final boolean hasPrevious()
    {
        return index - 1 >= 0;
    }

    @Override
    public final E next()
    {
        int index = this.index + 1;
        Objects.checkIndex(index, size.getAsInt());
        this.index = index;
        return items.apply(index);
    }

    @Override
    public final E previous()
    {
        int index = this.index - 1;
        Objects.checkIndex(index, size.getAsInt());
        this.index = index;
        return items.apply(index);
    }

    @Override
    public final E last()
    {
        this.index = size.getAsInt() - 2;
        return next();
    }

    @Override
    public final E first()
    {
        this.index = -1;
        return next();
    }
}
