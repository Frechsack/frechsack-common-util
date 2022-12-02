package frechsack.prod.util.stream;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class StreamUtils {

    /**
     * Returns a {@link Predicate}, that will return true for each element passed in the first time.
     * The distinctness of the elements is computed by the given {@link BiPredicate}.
     * If the predicate returns true for any element, the element will not be present on the result stream.
     * @param comparator To compare two elements. Should return true for elements, that are logical equal.
     * Those elements will be removed from the stream.
     * @return Returns a Predicate, that can be used in {@link java.util.stream.Stream#filter(Predicate)} to return a Stream with distinct elements.
     * @param <Type> The Streams element type.
     */
    public static <Type> Predicate<Type> distinct(BiPredicate<Type, Type> comparator){
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
    public static <Type> Predicate<Type> distinctBy(Function<Type, ?> extractor){
        final Set<Object> passedElements = Collections.synchronizedSet(new HashSet<>());
        return element -> passedElements.add(extractor.apply(element));
    }
}
