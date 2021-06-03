package com.frechsack.dev.util.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link Iterator} based approach of {@link Route}.
 * <p>
 * This implementation will take an Iterator and store previous iterated elements.
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
     * @param elementIterator The Iterator.
     * @throws NullPointerException Thrown when the Iterator is null.
     */
    public IteratorRoute(Iterator<E> elementIterator)
    {
        Objects.requireNonNull(elementIterator);
        this.elementIterator = elementIterator;
    }

    // TODO: Remove operation

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
}
