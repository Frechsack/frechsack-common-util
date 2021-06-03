package com.frechsack.dev.util.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link Iterator} based approach of {@link Route}.
 * <p>
 * This implementation will take an Iterator and store previous iterated elements.
 *
 * @param <E> The type of this sequence.
 * @author Frechsack
 */
public class IteratorRoute<E> implements Route<E>
{
    private final Iterator<E> elementIterator;
    private final ArrayList<E> passedElementLs = new ArrayList<>();
    private int passedIndex = -1;

    /**
     * Creates a new IteratorRoute based on the specified Iterator.
     *
     * @param elementIterator The Iterator.
     * @throws NullPointerException Thrown when the Iterator is null.
     */
    public IteratorRoute(Iterator<E> elementIterator)
    {
        Objects.requireNonNull(elementIterator);
        this.elementIterator = elementIterator;
    }

    @Override
    public void remove()
    {
        try
        {
            elementIterator.remove();
            passedElementLs.remove(passedIndex--);
        }
        catch (Exception ignored)
        {
        }

    }

    @Override
    public final boolean hasNext()
    {
        if (passedIndex == passedElementLs.size() - 1) return elementIterator.hasNext();
        return passedElementLs.size() > passedIndex;
    }

    @Override
    public final boolean hasPrevious()
    {
        return passedIndex > 0;
    }

    @Override
    public final E next()
    {
        // Check if next element is in list
        if (passedIndex != -1 && passedIndex < passedElementLs.size() - 1)
        {
            return passedElementLs.get(++passedIndex);
        }
        E next = elementIterator.next();
        passedElementLs.add(next);
        passedIndex++;
        return next;
    }

    @Override
    public final E previous()
    {
        return passedElementLs.get(--passedIndex);
    }

    @Override
    public final E last()
    {
        while (elementIterator.hasNext()) passedElementLs.add(elementIterator.next());
        return passedElementLs.get(passedIndex = passedElementLs.size() - 1);
    }

    @Override
    public final E first()
    {
        if (passedElementLs.isEmpty()) Objects.checkIndex(-1, 0);
        return passedElementLs.get(passedIndex = 0);
    }

    @Override
    public String toString()
    {
        return "IteratorRoute{" + "elementIterator=" + elementIterator + ", passedElementLs=" + passedElementLs + ", passedIndex=" + passedIndex + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IteratorRoute<?> that = (IteratorRoute<?>) o;
        return passedIndex == that.passedIndex &&
               Objects.equals(elementIterator, that.elementIterator) &&
               Objects.equals(passedElementLs, that.passedElementLs);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(elementIterator, passedElementLs, passedIndex);
    }
}
