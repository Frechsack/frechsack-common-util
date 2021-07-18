package frechsack.dev.util;

import java.util.function.Supplier;

/**
 * A Field supplies and creates a value on demand when {@link #get()} is called.
 * @param <E> The value´s class-type.
 * @author frechsack
 */
public abstract class Field<E> implements Supplier<E>
{
    private boolean isComputed;
    private E computed;

    /**
     * Recomputes this Field´s value.
     * @return The new value returned by {@link #get()}.
     */
    protected abstract E compute();

    /**
     * Forces this Field to recompute it´s value.
     */
    public void recompute(){
        isComputed = false;
    }

    @Override
    public synchronized E get()
    {
        if(!isComputed) {
            isComputed = true;
            computed = compute();
        }
        return computed;
    }
}
