package com.frechsack.dev.cursor;

import java.util.List;
import java.util.function.Consumer;

public class ListCursor<E> implements com.frechsack.dev.cursor.Cursor<E>
{
    private List<E> list;
    private int index = -1;

    public ListCursor(List<E> list)
    {
        setList(list);
    }

    public void setList(List<E> list)
    {
        this.list = list;
        index = -1;
    }

    public List<E> getList()
    {
        return list;
    }

    @Override
    public boolean hasNext()
    {
        return index < list.size()-1;
    }

    @Override
    public boolean hasPrevious()
    {
        return index > 0;
    }

    @Override
    public E next()
    {
        return list.get(++index);
    }

    @Override
    public E previous()
    {
        return list.get(--index);
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action)
    {
        for (int i = index;i < list.size();i++ ) action.accept(list.get(i));
    }

    @Override
    public void remove()
    {
        if(index == -1) return;
        list.remove(index);
        index--;
    }

    @Override
    public E last()
    {
        return list.get(index = list.size()-1);
    }

    @Override
    public E first()
    {
        return list.get(index = 0);
    }
}
