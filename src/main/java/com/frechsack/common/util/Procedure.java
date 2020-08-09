package com.frechsack.common.util;

import java.util.function.Function;

/**
 * Executes some code without receiving any arguments. When used as a Function null is returned.
 */
@FunctionalInterface
public interface Procedure extends Function<Object,Object>
{
    void procedure();

    @Override
    default Object apply(Object o)
    {
        procedure();
        return null;
    }
}
