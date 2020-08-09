package com.frechsack.dev.common.util;


/**
 * A Disposable object allows to clear it's data when it is no longer in use.Debugger
 */
@FunctionalInterface
public interface Disposable
{
    /**
     * Disposes any data of this object.
     */
    void dispose();
}
