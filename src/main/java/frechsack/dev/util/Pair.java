package frechsack.dev.util;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A Pair stores two Objects. It´s main propose is to provide a link between two related Objects.
 * <p>
 * Two Pairs are considered as equal, when their first and second value are equal.
 * <p>
 * This interface provides common implementations of Pair.
 *
 * @param <E> The first Object´s type.
 * @param <V> The second Object´s type.
 * @author Frechsack
 */
public interface Pair<E, V>
{
    /**
     * Returns the first Object of this Pair.
     *
     * @return The first Object.
     */
    E first();

    /**
     * Returns the second Object of this Pair.
     *
     * @return The second Object.
     */
    V second();

    /**
     * Creates a new Pair with the specified content.
     *
     * @param e   The first Object.
     * @param v   The second Object.
     * @param <E> The first Object´s type.
     * @param <V> The second Object´s type.
     * @return Returns a new Pair with the content of e and v.
     */
    static <E, V> Pair<E, V> of(E e, V v)
    {
        return new Pairs.SimplePair<>(e, v);
    }

    /**
     * Creates a new Pair with the specified content. The values returned by this Pair is equal to {@link Supplier#get()}.
     *
     * @param e      The first Supplier. When {@link #first()} is invoked, the Supplier´s {@link Supplier#get()} function is invoked.
     * @param v      The second Supplier. When {@link #second()} is invoked, the Supplier´s {@link Supplier#get()} function is invoked.
     * @param <E>    The first Object´s type.
     * @param <V>The second Object´s type.
     * @return Returns a new Pair with the content of the specified Suppliers.
     * @throws NullPointerException Thrown when one of the Suppliers is null.
     * @see Supplier
     */
    static <E, V> Pair<E, V> of(Supplier<E> e, Supplier<V> v)
    {
        Objects.requireNonNull(e);
        Objects.requireNonNull(v);
        return new Pairs.SupplierPair<>(e, v);
    }

    /**
     * Creates a new Pair with the specified content. This Pair will act like a mirror to the specified {@link java.util.Map.Entry}. This Pair´s {@link #first()} function will be return the Entry´s key, and {@link #second()} returns the Entry´s value.
     *
     * @param entry  The entry that should be mirrored.
     * @param <E>    The first Object´s type.
     * @param <V>The second Object´s type.
     * @return Returns a new Pair with the same content as the specified entry.
     * @throws NullPointerException Thrown when the specified entry is null.
     */
    static <E, V> Pair<E, V> of(Map.Entry<E, V> entry)
    {
        Objects.requireNonNull(entry);
        return new Pairs.PairEntry<>(entry);
    }

    /**
     * Creates a Pair with the three Objects.
     *
     * @param e   The first Object.
     * @param v   The second Object. Obtainable by get {{@link #second()} and then {@link #first()}.
     * @param k   The third Object. Obtainable by get {{@link #second()} and then {@link #second()}.
     * @param <E> The first Object´s type.
     * @param <V> The second Object´s type.
     * @param <K> The third Object´s type.
     * @return Returns a new Pair with the specified content.
     */
    static <E, V, K> Pair<E, Pair<V, K>> of(E e, V v, K k)
    {
        return of(e, of(v, k));
    }

}
