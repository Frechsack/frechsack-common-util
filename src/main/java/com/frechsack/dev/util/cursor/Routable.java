package com.frechsack.dev.util.cursor;

public interface Routable<E> extends Iterable<E>
{
    Route<E> route();
}
