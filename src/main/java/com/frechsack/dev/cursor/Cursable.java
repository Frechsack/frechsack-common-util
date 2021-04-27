package com.frechsack.dev.cursor;

public interface Cursable<E> extends Iterable<E>
{
    Cursor<E> cursor();
    
}
