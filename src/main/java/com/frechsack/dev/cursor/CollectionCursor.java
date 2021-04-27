package com.frechsack.dev.cursor;

import java.util.Collection;
import java.util.function.Consumer;

public class CollectionCursor<E> implements Cursor<E>
{
    private Collection<? extends E> collection;
    private Object[] array;
    private int index = -1;

    public CollectionCursor(Collection<? extends E> collection)
    {
        setCollection(collection);
    }

    public void setCollection(Collection<? extends E> collection)
    {
        this.collection = collection;
        array = collection.toArray();
        index = -1;
    }

    public Collection<? extends E> getCollection()
    {
        return collection;
    }

    private void requireArray()
    {
        boolean isRemoved = array.length > collection.size();
        if (array.length != collection.size()) array = collection.toArray();
        // Check if index is still in bounds
        if (isRemoved && index >= collection.size()) index = collection.size() - 1;
    }

    @Override
    public void remove()
    {
        requireArray();
        collection.remove(array[index]);
        index--;
        requireArray();
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action)
    {
        requireArray();
        for (int i = index; i < array.length; i++) //noinspection unchecked
            action.accept((E)array[i]);

    }

    @Override
    public boolean hasNext()
    {
        requireArray();
        return index < array.length - 1;
    }

    @Override
    public boolean hasPrevious()
    {
        requireArray();
        return index > 0;
    }

    @Override
    public E next()
    {
        requireArray();
        //noinspection unchecked
        return (E) array[++index];
    }

    @Override
    public E previous()
    {
        requireArray();
        //noinspection unchecked
        return (E) array[--index];
    }

    @Override
    public E last()
    {
        requireArray();
        //noinspection unchecked
        return (E) array[index = array.length - 1];
    }

    @Override
    public E first()
    {
        requireArray();
        //noinspection unchecked
        return (E) array[index = 0];
    }
}
