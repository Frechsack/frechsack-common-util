package com.frechsack.common.util;


import java.util.function.Function;

/**
 * Returns an object of a given type.
 * @param <E> The type of this result.
 */
@FunctionalInterface
public interface Result<E> extends Function<Object,E>
{
    /**
     * Returns the result of this Function.
     * @return The result.
     */
    E obtain();

    @Override
    default E apply(Object o)
    {
        return obtain();
    }
}
