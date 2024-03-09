package frechsack.prod.util.stream;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import java.util.stream.Stream;

public class StreamUtils {

    private StreamUtils() {}

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <Type> @NotNull Stream<Type> concat(@NotNull Stream<? extends Type>... streams){
        if(streams.length == 0)
            return Stream.empty();
        if(streams.length == 1)
            return (Stream<Type>) streams[0];

        Stream<Type> concat = (Stream<Type>) streams[0];
        for (int i = 1; i < streams.length; i++)
            concat = Stream.concat(concat, streams[i]);
        return concat;
    }

    /**
     * Returns a function that adds the elements from the given Stream. This function should be used with {@link Stream#mapMulti(BiConsumer)} to add elements to a Stream.
     * @param stream The elements that should be consumed.
     * @return Returns a function.
     * @param <Type> The elements type.
     */
    public static <Type> @NotNull BiConsumer<Type,Consumer<Type>> mapMultiAdd(@NotNull Stream<? extends Type> stream){
        final AtomicBoolean isDone = new AtomicBoolean(false);
        return (element, consumer) -> {
            if (isDone.getAndSet(true))
                consumer.accept(element);
            else {
                stream.forEachOrdered(consumer);
                stream.close();
                consumer.accept(element);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <Input, Output> @NotNull Comparator<Input> sortedBy(@NotNull Function<Input,Output> extractor){
        Objects.requireNonNull(extractor);
        return (lhs, rhs) -> {
            if(lhs == null && rhs == null) return 0;
            if(lhs == null) return 1;
            if(rhs == null) return -1;

            Output cLhs = extractor.apply(lhs);
            Output cRhs = extractor.apply(rhs);

            if(cLhs == null && cRhs == null) return 0;
            if(cLhs == null) return 1;
            if(cRhs == null) return -1;

            if(cLhs instanceof Comparable<?> )
              return ((Comparable<Output>) cLhs).compareTo(cRhs);

            throw new IllegalArgumentException("Class: '" + cLhs.getClass() +  "' does not implement java.lang.Comparable.");
        };
    }



    /**
     * Returns a {@link Predicate}, that will return true for each element passed in the first time.
     * The distinctness of the elements is computed by the given {@link BiPredicate}.
     * If the predicate returns true for any element, the element will not be present on the result stream.
     * @param comparator To compare two elements. Should return true for elements, that are logical equal.
     * Those elements will be removed from the stream.
     * @return Returns a Predicate, that can be used in {@link java.util.stream.Stream#filter(Predicate)} to return a Stream with distinct elements.
     * @param <Type> The Streams element type.
     */
    public static <Type> @NotNull Predicate<Type> filterDistinct(@NotNull BiPredicate<Type, Type> comparator){
        final ConcurrentHashMap<Type, Object> passedElements = new ConcurrentHashMap<>();
        final var keySet = passedElements.keySet();
        final Object dummy = new Object();
        return element -> {
            for (Type passedElement : keySet)
                if(!comparator.test(passedElement, element))
                    return false;
            return passedElements.put(element, dummy) == null;
        };
    }

    /**
     * Returns a {@link Predicate}, that will return true for each element passed in the first time.
     * The distinctness of the elements is chosen by a single attribute. The attribute is extracted by the given {@link Function}.
     * @param extractor The function used to extract an attribute from the element.
     * An element will be removed from the stream, if any element prior had the same attribute returned by this Function.
     * @return Returns a Predicate, that can be used in {@link java.util.stream.Stream#filter(Predicate)} to return a Stream with distinct elements.
     * @param <Type> The Streams element type.
     */
    public static <Type> @NotNull Predicate<Type> filterDistinctBy(@NotNull Function<Type, ?> extractor){
        final Set<Object> passedElements = Collections.synchronizedSet(new HashSet<>());
        return element -> passedElements.add(extractor.apply(element));
    }
}
