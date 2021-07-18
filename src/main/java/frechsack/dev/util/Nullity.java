package frechsack.dev.util;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 * Provides static functions to handle null values.
 * @author frechsack
 */
public class Nullity
{
    private Nullity() {}

    /**
     * Returns null if the given arguments are equal, otherwise the first not null argument is returned.
     *
     * @param a   The first argument.
     * @param b   The second argument.
     * @param <E> The arguments class-type.
     * @return Returns null if both Objects are equal, otherwise the first not null argument.
     */
    public static <E> E nullIf(E a, E b)
    {
        return Objects.equals(a, b) ? null : a == null ? b : a;
    }

    /**
     * Returns null if the Objects in the specified collection are equal, otherwise the first not null element is returned.
     *
     * @param collection The specified collection.
     * @param <E>        The elements class-type.
     * @return Returns null if the Objects in the specified collection are equal, otherwise the first not null element is returned.
     */
    public static <E> E nullIf(Collection<E> collection)
    {
        E prev = null;
        E notNull = null;
        E current;
        for (E e : collection)
        {
            current = e;
            if (Objects.equals(current, prev)) return null;
            prev = current;
            if (notNull == null) notNull = prev;
        }
        return notNull;
    }

    /**
     * Checks if the specified value is null.
     *
     * @param e The element.
     * @return Returns true if the element is null, else false.
     */
    public static boolean isNull(Object e)
    {
        return e == null;
    }

    /**
     * Checks if the specified value is not null.
     *
     * @param e The element.
     * @return Returns true if the element is not null, else false.
     */
    public static boolean isNonNull(Object e)
    {
        return e != null;
    }

    /**
     * Returns the first not null value. If both elements are null, an empty {@link Optional} is returned.
     *
     * @param a The first element.
     * @param b The second element.
     * @return Returns the first not null element.
     */
    public static <E> Optional<E> nonNull(E a, E b)
    {
        // Are both null?
        if (a == null && b == null) return Optional.empty();
        return a == null ? Optional.of(b) : Optional.of(a);
    }

    /**
     * Returns the first not null element. If both elements are null, a default value is requested.
     *
     * @param defaultValue A Supplier that returns a default value.
     * @param a            The first element.
     * @param b            The second element.
     * @param <E>          The elements class-type.
     * @return Returns a non null element.
     */
    public static <E> E nonNull(Supplier<E> defaultValue, E a, E b)
    {
        return a != null ? a : b != null ? b : Objects.requireNonNull(defaultValue.get(), "Supplier must return a non null value.");
    }

    /**
     * Returns the first not null element. If all elements are null, a default value is requested.
     *
     * @param defaultValue A Supplier that returns a default value.
     * @param a            The first element.
     * @param b            The second element.
     * @param c            The third element.
     * @param <E>          The elements class-type.
     * @return Returns a non null element.
     */
    public static <E> E nonNull(Supplier<E> defaultValue, E a, E b, E c)
    {
        return a != null ? a : b != null ? b : c != null ? c : Objects.requireNonNull(defaultValue.get(), "Supplier must return a non null value.");
    }

    /**
     * Returns the first not null element. If every element in the specified Collection is null, a default value is requested.
     *
     * @param defaultValue A Supplier that returns a default value.
     * @param collection   The Collection.
     * @param <E>          The elements class-type.
     * @return Returns a non null element.
     */
    public static <E> E nonNull(Supplier<E> defaultValue, Collection<E> collection)
    {
        return Objects.requireNonNull(Objects.requireNonNull(collection)
                                             .parallelStream()
                                             .filter(Nullity::isNonNull)
                                             .findFirst()
                                             .orElseGet(defaultValue), "Supplier must return a non null value.");
    }

    /**
     * Returns the first not null element. If every element returned by the specified Iterator is null, a default value is requested.
     *
     * @param defaultValue A Supplier that returns a default value.
     * @param iterator     The Iterator.
     * @param <E>          The elements class-type.
     * @return Returns a non null element.
     */
    public static <E> E nonNull(Supplier<E> defaultValue, Iterator<E> iterator)
    {
        return Objects.requireNonNull(StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), true)
                                                   .filter(Nullity::isNonNull)
                                                   .findFirst()
                                                   .orElseGet(defaultValue), "Supplier must return a non null value.");
    }

    /**
     * Returns the first not null element. If every element in the specified array is null, a default value is returned.
     *
     * @param defaultValue A Supplier that returns a default value.
     * @param values       The array.
     * @param <E>          The elements class-type.
     * @return Returns a non null element.
     */
    @SafeVarargs
    public static <E> E nonNull(Supplier<E> defaultValue, E... values)
    {
        Objects.requireNonNull(values);
        for (int i = 0; i < values.length; i++) if (values[i] != null) return values[i];
        return Objects.requireNonNull(defaultValue.get(),"Supplier must return a non null value.");
    }

    /**
     * Checks if the specified array contains a null element.
     * If the specified array is null, false is returned.
     * @param values The array.
     * @return Returns true if the array contains null, else false.
     */
    public static boolean containsNull(Object... values)
    {
        if(values == null) return false;
        for (int i = 0; i < values.length; i++) if (values[i] == null) return true;
        return false;
    }

    /**
     * Checks if the specified array contains a non null element.
     * If the specified array is null, false is returned.
     * @param values The array.
     * @return Returns true if the array contains non null, else false.
     */
    public static boolean containsNonNull(Object... values)
    {
        if(values == null) return false;
        for (int i = 0; i < values.length; i++) if (values[i] != null) return true;
        return false;
    }

    /**
     * Checks if the specified Collection contains a null element.
     * If the specified Collection is null, false is returned.
     * @param values The array.
     * @return Returns true if the array contains null, else false.
     */
    public static boolean containsNull(Collection<?> values)
    {
        if(values == null) return false;
        return values.parallelStream().anyMatch(Nullity::isNull);
    }

    /**
     * Checks if the specified Collection contains a non null element.
     * If the specified Collection is null, false is returned.
     * @param values The array.
     * @return Returns true if the array contains non null, else false.
     */
    public static boolean containsNonNull(Collection<?> values)
    {
        if(values == null) return false;
        return values.parallelStream().noneMatch(Nullity::isNull);
    }

}
