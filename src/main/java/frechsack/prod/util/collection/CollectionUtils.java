package frechsack.prod.util.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {

    private CollectionUtils() {}

    private static @NotNull List<Map.Entry<Object, Long>> frequencies(@NotNull Collection<?> collection) {
        return collection.stream()
                .map(it -> it == null ? collection : it)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .sorted( Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .toList();
    }

    /**
     * Returns the element with the highest frequency in this collection. This includes null as an element.
     * If two elements have the same frequency, the lower element (according to its natural order) will be returned.
     * @param collection The collection to find the element in.
     * @return Returns the element with the highest frequency.
     * @param <E> The element type.
     */
    @SuppressWarnings("unchecked")
    public static <E> E highestFrequency(@NotNull Collection<? extends E> collection) {
        if (collection.isEmpty())
            throw new NoSuchElementException();
        final var frequencies = frequencies(collection);
        final var firstElement = frequencies.getFirst().getKey();
        if (firstElement == collection)
            return null;
        return (E) firstElement;
    }

    /**
     * Returns the element with the highest frequency in this collection. This does ignore null as an element.
     * If two elements have the same frequency, the lower element (according to its natural order) will be returned.
     * @param collection The collection to find the element in.
     * @return Returns the element with the highest frequency.
     * @param <E> The element type.
     */
    @SuppressWarnings("unchecked")
    public static <E> @NotNull E highestFrequencyExcludeNull(@NotNull Collection<? extends E> collection) {
        if (collection.isEmpty())
            throw new NoSuchElementException();
        final var frequencies = frequencies(collection);
        final var firstElement = frequencies.getFirst().getKey();
        if (firstElement != collection)
            return (E) firstElement;
        if (frequencies.size() == 1)
            throw new NoSuchElementException("The collection only contains null.");
        return (E) frequencies.get(1).getKey();
    }
}
