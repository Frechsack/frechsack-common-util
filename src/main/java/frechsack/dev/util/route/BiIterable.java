package frechsack.dev.util.route;

/**
 * Implementing this interface marks an Object as BiIterable, that means it´s elements can be accessed through a {@link BiIterator}.
 * @param <E> The type of this sequence.
 */
public interface BiIterable<E> extends Iterable<E>
{
    /**
     * Returns a new BiIterator over the elements in this Object.
     * @return Returns the BiIterator.
     * @see BiIterator
     */
    BiIterator<E> biIterator();
}
