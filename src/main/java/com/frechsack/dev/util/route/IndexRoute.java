package com.frechsack.dev.util.route;

import com.frechsack.dev.util.function.NumberSupplier;

import java.util.Objects;
import java.util.function.IntFunction;

/**
 * An indexed based approach of {@link Route}.
 * <p>
 * This implementation allows a dynamic resizing of the underlying model.
 * @param <E> The type of this sequence.
 * @author Frechsack
 */
public class IndexRoute<E> implements Route<E>
{
    private final IntFunction<E> items;
    private final NumberSupplier<?> size;
    private int index = -1;

    /**
     * Creates a new IndexRoute.
     *
     * @param items A function that returns the given value on the specified position.
     * @param size Returns the current size of the underlying model.
     * @apiNote This constructor is visible, because user may want to override {@link #remove(int)} to modify the remove operation.
     * @throws NullPointerException Thrown when one of the arguments is null.
     */
    public IndexRoute(IntFunction<E> items, NumberSupplier<?> size)
    {
        Objects.requireNonNull(items);
        Objects.requireNonNull(size);
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

    @Override
    public String toString()
    {
        return "IndexRoute{" + "items=" + items + ", size=" + size + ", index=" + index + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexRoute<?> that = (IndexRoute<?>) o;
        return index == that.index && Objects.equals(items, that.items) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(items, size, index);
    }
}
