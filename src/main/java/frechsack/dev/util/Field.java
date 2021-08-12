package frechsack.dev.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A Field supplies and creates a value on demand when {@link #get()} is called.
 * @param <E> The value´s class-type.
 * @author frechsack
 */
public abstract class Field<E> implements Supplier<E>
{
    private Reference<E> computedReference;

    /**
     * Recomputes this Field´s value.
     * @return The new value returned by {@link #get()}.
     */
    protected abstract E compute();

    /**
     * Forces this Field to recompute it´s value.
     */
    public void recompute(){
        if(computedReference != null) computedReference.enqueue();
        computedReference = null;
    }

    @Override
    public synchronized E get()
    {
        Reference<E> reference = computedReference;
        if(reference == null || reference.get() == null) {
            computedReference = reference = new SoftReference<>(compute());
        }
        return reference.get();
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        return Objects.equals(get(),o);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(computedReference);
    }

    @Override
    public String toString()
    {

        return "Field{" + "computedReference=" + computedReference + '}';
    }
}
