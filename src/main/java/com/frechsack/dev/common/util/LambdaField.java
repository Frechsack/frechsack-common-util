package com.frechsack.dev.common.util;

/**
 * Subtype of {@link CachedField}. Caches a value and creates it with an interface.
 *
 * @param <E> The value type.
 */
public class LambdaField<E> extends CachedField<E>
{
    private final Result<E> generator;

    /**
     * Creates a new instance. The value will be generator by the given {@link Result}.
     *
     * @param generator The {@link Result} that will be generate the value of this {@link LambdaField}.
     */
    public LambdaField(Result<E> generator)
    {
        this.generator = generator;
    }

    @Override
    protected E generate()
    {
        return generator.obtain();
    }
}
