package frechsack.prod.util.stream;

import frechsack.prod.util.concurrent.SingleRunnable;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    private StreamUtils() {}

    /**
     * Returns a function that adds the elements from the given Stream. This function should be used with {@link Stream#mapMulti(BiConsumer)} to add elements to a Stream.
     * @param stream The elements that should be consumed.
     * @return Returns a function.
     * @param <Type> The elements type.
     */
    public static <Type> BiConsumer<Type,Consumer<Type>>  mapMultiAdd(Stream<Type> stream){
        final AtomicBoolean isDone = new AtomicBoolean(false);
        return (element, consumer) -> {
            consumer.accept(element);
            if (isDone.getAndSet(true))
                return;
            stream.forEachOrdered(consumer);
            stream.close();
        };
    }

    @SuppressWarnings("unchecked")
    public static <Input, Output> Comparator<Input> sortedBy(Function<Input,Output> extractor){
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
    public static <Type> Predicate<Type> filterDistinct(BiPredicate<Type, Type> comparator){
        final Set<Type> passedElements = Collections.synchronizedSet(new HashSet<>());
        return element -> {
            for (Type passedElement : passedElements)
                if(comparator.test(passedElement, element))
                    return false;
            passedElements.add(element);
            return true;
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
    public static <Type> Predicate<Type> filterDistinctBy(Function<Type, ?> extractor){
        final Set<Object> passedElements = Collections.synchronizedSet(new HashSet<>());
        return element -> passedElements.add(extractor.apply(element));
    }
}
