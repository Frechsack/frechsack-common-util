package frechsack.dev.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Provides static functions to handle null values.
 * @author frechsack
 */
public class Nullity
{
    private Nullity() {}

    /**
     * Performs an action if the specified value is not null.
     * @param value The value.
     * @param onAction The action that will be performed if the passed value is not null.
     * @param <E> The value class-type.
     * @return Returns the passed value.
     */
    public static <E> E ifPresent(final E value,final Consumer<E> onAction){
        if(value != null) onAction.accept(value);
        return value;
    }

    /**
     * Returns null if the given arguments are equal, otherwise the first not null argument is returned.
     *
     * @param a   The first argument.
     * @param b   The second argument.
     * @param <E> The arguments class-type.
     * @return Returns null if both Objects are equal, otherwise the first not null argument.
     */
    public static <E> E nullIfEqual(final E a,final E b)
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
    public static <E> E nullIfEqual(final Collection<E> collection)
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
     * Returns null if the Objects returned by the iterator are equal, otherwise the first not null element is returned.
     *
     * @param iterator The specified iterator.
     * @param <E>        The elements class-type.
     * @return Returns null if the Objects in the specified iterator are equal, otherwise the first not null element is returned.
     */
    public static <E> E nullIfEqual(final Iterator<E> iterator)
    {
        E prev = null;
        E notNull = null;
        E current;
        while (iterator.hasNext())
        {
            current = iterator.next();
            if (Objects.equals(current, prev)) return null;
            prev = current;
            if (notNull == null) notNull = prev;
        }
        return notNull;
    }

    /**
     * Returns null if the Objects returned by the Stream are equal, otherwise the first not null element is returned.
     *
     * @param stream The specified Stream.
     * @param <E>        The elements class-type.
     * @return Returns null if the Objects in the specified Stream are equal, otherwise the first not null element is returned.
     */
    public static <E> E nullIfEqual(final Stream<E> stream)
    {
        E element = nullIfEqual(stream.iterator());
        stream.close();
        return element;
    }

    /**
     * Checks if the specified value is null.
     *
     * @param e The element.
     * @return Returns true if the element is null, else false.
     */
    public static boolean isNull(final Object e)
    {
        return e == null;
    }

    /**
     * Checks if the specified value is not null.
     *
     * @param e The element.
     * @return Returns true if the element is not null, else false.
     */
    public static boolean isNonNull(final Object e)
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
    public static <E> Optional<E> nonNull(final E a,final E b)
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
    public static <E> E nonNull(final Supplier<E> defaultValue,final E a,final E b)
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
    public static <E> E nonNull(final Supplier<E> defaultValue,final E a, final E b,final E c)
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
    public static <E> E nonNull(final Supplier<E> defaultValue,final Collection<E> collection)
    {
        return Objects.requireNonNull(Objects.requireNonNull(collection)
                                             .parallelStream()
                                             .filter(Nullity::isNonNull)
                                             .findFirst()
                                             .orElseGet(defaultValue), "Supplier must return a non null value.");
    }

    /**
     * Returns the first not null element. If every element in the specified Stream is null, a default value is requested.
     *
     * @param defaultValue A Supplier that returns a default value.
     * @param stream   The Stream.
     * @param <E>          The elements class-type.
     * @return Returns a non null element.
     */
    public static <E> E nonNull(final Supplier<E> defaultValue,final Stream<E> stream)
    {
        E value= Objects.requireNonNull(Objects.requireNonNull(stream)
                                             .filter(Nullity::isNonNull)
                                             .findFirst()
                                             .orElseGet(defaultValue), "Supplier must return a non null value.");
        stream.close();
        return value;
    }

    /**
     * Returns the first not null element. If every element returned by the specified Iterator is null, a default value is requested.
     *
     * @param defaultValue A Supplier that returns a default value.
     * @param iterator     The Iterator.
     * @param <E>          The elements class-type.
     * @return Returns a non null element.
     */
    public static <E> E nonNull(final Supplier<E> defaultValue,final Iterator<E> iterator)
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
    public static <E> E nonNull(final Supplier<E> defaultValue,final E... values)
    {
        Objects.requireNonNull(values);
        for (int i = 0; i < values.length; i++) if (values[i] != null) return values[i];
        return Objects.requireNonNull(defaultValue.get(),"Supplier must return a non null value.");
    }
}
