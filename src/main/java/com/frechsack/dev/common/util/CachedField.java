package com.frechsack.dev.common.util;

/**
 * Caches a value. A value is created only when it is required.
 * @param <E> The value type.
 */
public abstract class CachedField<E> implements Result<E>,Disposable
{
    private boolean isValue;
    private E value;

    /**
     * Creates a new instance of the required type.
     * @return The value.
     */
    protected abstract E generate();

    /**
     * Called when the value of this {@link CachedField} is accessed.
     * @param value The value. Avoid getting the value with {@link CachedField#obtain()}, because that will resolve in an infinite loop.
     */
    protected void validate(E value){}

    /**
     * Disposes the current value, if it's set. The next time - the value is required - it will be created again.
     */
    @Override
    public void dispose()
    {
        if(!isValue)return;
        if(value instanceof Disposable)
            ((Disposable) value).dispose();
        value=null;
        isValue = false;
    }

    @Override
    public final E obtain()
    {
        if(!isValue)
        {
            value = generate();
            isValue = true;
        }
        validate(value);
        return value;
    }

    /**
     * Checks if the value is created.
     * @return True if the value is created, else false.
     */
    public boolean isValue()
    {
        return isValue;
    }
}
