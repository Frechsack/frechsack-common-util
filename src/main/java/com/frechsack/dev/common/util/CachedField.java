package com.frechsack.dev.common.util;

/**
 * A {@code CachedField} buffers a value and just creates it, if it is required.
 * @param <E> The type of this field.
 */
public class CachedField<E> implements Result<E>
{
    private       E         field;
    private final Result<E> generator;

    /**
     * Creates a new CachedField.
     * @param generator The generator that will generate the value of the {@code CachedField}.
     */
    public CachedField(Result<E> generator)
    {
        this.generator = generator;
    }

    /**
     * Creates a new CachedField. If this constructor is used the function {@link #generate()} must be overridden.
     */
    public CachedField()
    {
        generator = null;
    }

    /**
     * Generates the value of this {@code CachedField}. This function must be overridden if no function is given at the constructor.
     * @return The value.
     */
    protected E generate()
    {
        return generator.obtain();
    }


    @Override
    public E obtain()
    {
        if(field==null) field = generate();
        return field;
    }
}
