package frechsack.prod.util.collection;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Provides static functions for Collection operations.
 *
 * @author frechsack
 */
public class CollectionUtils {
    private CollectionUtils() {
    }


    /**
     * Merges some lists together into one. Changes in the returned list will be reflected in the others.
     * If every source list implements {@link RandomAccess}, the returned list will implement it as well.
     * @param lists The source lists.
     * @return Returns the merged list. In case no lists are passed, an empty {@link ArrayList} will be returned. If only a single list is passed, that list will be returned.
     * @param <E> The element type.
     */
    @SafeVarargs
    public static <E> List<E> merged(List<E>... lists){
        if(lists.length == 0) return new ArrayList<>();
        if(lists.length == 1) return lists[0];

        boolean isRandomAccess = true;
        for (List<E> list : lists) if(!(list instanceof RandomAccess)) {
            isRandomAccess = false;
            break;
        }

        return isRandomAccess ? new MergedListFactory.MergedRandomAccessList<>(lists) : new MergedListFactory.MergedLinkedList<>(lists);
    }

    /**
     * Removes any duplicate elements from a Collection.
     * @param collection The Collection.
     * @return Returns the input Collection.
     * @param <Type> The Collections elements type.
     * @param <Clt> The Collection-type.
     */
    public static <Type, Clt extends Collection<Type>> Clt removeDuplicates(Clt collection){
        Set<Type> duplicateHandle = new HashSet<>();
        Type element;
        for(Iterator<Type> iterator = collection.iterator(); iterator.hasNext(); ){
            element = iterator.next();
            if(duplicateHandle.contains(element))
                iterator.remove();
            else
                duplicateHandle.add(element);
        }
        return collection;
    }

    /**
     * Creates a Stream over all elements contained in the specified Collection. The elements will be mapped to their index in the collection. The order of the elements depends on the implementation of {@link Collection#iterator()}.
     *
     * @param clt The Collection.
     * @param <E> The Stream´s elements class-type.
     * @return The Stream.
     */
    public static <E> Stream<Tuple<Integer, E>> indexStream(java.util.Collection<E> clt) {
        Iterator<E> iterator = clt.iterator();
        return IntStream.range(0, clt.size()).mapToObj(index -> Tuple.of(index, iterator.next()));
    }


    /**
     * Creates a Stream with an undefined amount of elements from the specified Iterator. This operation is similar to {@link StreamSupport#stream(Spliterator, boolean)}.
     *
     * @param iterator The Iterator that supplies elements for the Stream.
     * @param isParallel Indicates if the stream will be parallel.
     * @param <E>      The Stream´s element class-type.
     * @return Returns a Stream with the elements from the specified Iterator.
     */
    public static <E> Stream<E> stream(Iterator<E> iterator, boolean isParallel) {
        return stream(iterator, isParallel,-1);
    }

    /**
     * Creates a Stream with an undefined amount of elements from the specified Iterator. This operation is similar to {@link StreamSupport#stream(Spliterator, boolean)}.
     *
     * @param iterator The Iterator that supplies elements for the Stream.
     * @param isParallel Indicates if the stream will be parallel.
     * @param size     The size specifies the amount of elements returned by the Iterator. If set to -1 this property is ignored.
     * @param <E>      The Stream´s element class-type.
     * @return Returns a Stream with the elements from the specified Iterator.
     */
    public static <E> Stream<E> stream(Iterator<E> iterator, boolean isParallel, int size) {
        return StreamSupport.stream(size < 0 ? Spliterators.spliteratorUnknownSize(iterator, 0) : Spliterators.spliterator(iterator, size, Spliterator.SIZED),
                isParallel);
    }

    /**
     * Adds any element that is returned by the Iterator to the specified Collection.
     *
     * @param collection The Collection.
     * @param iterator   The Iterator.
     * @param <E>        The element´s class-type.
     * @return Return the Collection.
     */
    public static <E> Collection<E> addAll(Collection<E> collection, Iterator<E> iterator) {
        return addAll(collection, iterator, 0);
    }

    /**
     * Adds any element that is returned by the Iterator to the specified Collection.
     *
     * @param collection The Collection.
     * @param iterator   The Iterator.
     * @param size       An optional size. May specify the amount of elements that will be returned by the iterator.
     * @param <E>        The element´s class-type.
     * @return Return the Collection.
     */
    public static <E> Collection<E> addAll(Collection<E> collection, Iterator<? extends E> iterator, int size) {
        Objects.requireNonNull(collection);
        if (iterator == null) return collection;

        // known size
        if (size > 0 && collection instanceof ArrayList)
            ((ArrayList<E>) collection).ensureCapacity(collection.size() + size);
        while (iterator.hasNext()) collection.add(iterator.next());

        return collection;
    }

    /**
     * Returns the key of the specified element in the Map.
     *
     * @param map   The Map.
     * @param value The value.
     * @param <E>   The Map´s key class-type.
     * @param <V>   The Map´s value class-type.
     * @return The Key.
     */
    public static <E, V> Optional<E> getKey(Map<E, V> map, V value) {
        if (map == null) return Optional.empty();
        return map.entrySet().parallelStream().filter(it -> Objects.equals(it.getValue(), value)).map(Map.Entry::getKey).findAny();
    }

    /**
     * Converts a {@link Collection} to a {@link Set}.
     *
     * @param collection The collection.
     * @param <E>        The element´s class-type.
     * @return Returns a Set.
     */
    public static <E> Set<E> toSet(java.util.Collection<E> collection) {
        if (collection instanceof Set) return (Set<E>) collection;
        return new WrapperFactory.SetWrapper<>(Objects.requireNonNull(collection));
    }

    /**
     * Adds any element in the specified array to the specified {@link Collection}.
     *
     * @param collection The collection.
     * @param array      The array.
     * @param <E>        The element´s class-type.
     * @return Returns the passed Collection.
     */
    @SafeVarargs
    public static <E> Collection<E> addAll(Collection<E> collection, E... array) {
        if (array == null || array.length == 0) return collection;
        Objects.requireNonNull(collection);
        collection.addAll(List.of(array));
        return collection;
    }

    /**
     * Returns an Enumeration based on an Iterator.
     *
     * @param iterator The Iterator.
     * @param <E>      The elements class-type.
     * @return Returns an Enumeration.
     */
    public static <E> Enumeration<E> toEnumeration(Iterator<E> iterator) {
        return new WrapperFactory.IteratorWrapper<>(Objects.requireNonNull(iterator));
    }

    /**
     * Returns a synchronized version of the specified Queue.
     *
     * @param queue The Queue.
     * @param <E>   The Queue elements type.
     * @return Returns  a synchronized version of the specified Queue.
     */
    public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
        if (queue instanceof SynchronousQueue || queue instanceof WrapperFactory.SynchronizedQueueWrapper) return queue;
        return new WrapperFactory.SynchronizedQueueWrapper<>(Objects.requireNonNull(queue));
    }

    /**
     * Returns a synchronized version of the specified Deque.
     *
     * @param deque The Deque.
     * @param <E>   The Deque elements type.
     * @return Returns a synchronized version of the specified Deque.
     */
    public <E> Queue<E> synchronizedDeque(Deque<E> deque) {
        if (deque instanceof SynchronousQueue) return deque;
        return new WrapperFactory.SynchronizedDequeWrapper<>(Objects.requireNonNull(deque));
    }


}
