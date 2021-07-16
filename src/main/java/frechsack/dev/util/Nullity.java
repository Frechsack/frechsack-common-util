package frechsack.dev.util;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class Nullity
{
    private Nullity() {}

    public static <E> E nullIf(E a, E b)
    {
        return Objects.equals(a, b) ? null : a;
    }

    public static <E> E nullIf(Collection<E> collection){
        E prev = null;
        E current;
        for (E e : collection)
        {
            current = e;
            if (Objects.equals(current, prev)) return null;
            prev = current;
        }
        return prev;
    }

    public static  boolean isNull(Object e){
        return e == null;
    }

    public static boolean isNotNull(Object e){
        return e != null;
    }

    public static <E> E nonNull(E a, E b)
    {
        return a == null ? b : a;
    }

    public static <E> E nonNull(Supplier<E> defaultValue,E a, E b )
    {
        return a != null ? a : b != null ? b : defaultValue.get();
    }

    public static <E> E nonNull(Supplier<E> defaultValue,E a, E b, E c)
    {
        return a != null ? a : b != null ? b : c != null ? c : defaultValue.get();
    }

    public static <E> E nonNull(Supplier<E> defaultValue, Collection<E> collection)
    {
       return collection.parallelStream().filter(Nullity::isNotNull).findFirst().orElseGet(defaultValue);
    }

    public static <E> E nonNull(Supplier<E> defaultValue, Iterator<E> iterator)
    {
       return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0),true).filter(Nullity::isNotNull).findFirst().orElseGet(defaultValue);
    }

    @SafeVarargs
    public static <E> E nonNull(Supplier<E> defaultValue, E... values)
    {
        for (int i = 0; i < values.length; i++) if (values[i] != null) return values[i];
        return defaultValue.get();
    }

    public static boolean containsNull(Object... values){
        for (int i = 0; i < values.length; i++)  if(values[i] == null) return true;
        return false;
    }

    public static  boolean containsNull(Collection<?> values){
        return values.parallelStream().anyMatch(Nullity::isNull);
    }

    public static boolean containsNotNull(Collection<?> values){
        return values.parallelStream().noneMatch(Nullity::isNull);
    }

}
