package frechsack.dev.util.route;

/**
 * Implementing this interface marks an Object as Routable, that means it´s elements can be accessed through a {@link Route}.
 * @param <E> The type of this sequence.
 */
public interface Routable<E> extends Iterable<E>
{
    /**
     * Returns a new Route over the elements in this Object.
     * @return Returns the Route.
     * @see Route
     */
    Route<E> route();
}
