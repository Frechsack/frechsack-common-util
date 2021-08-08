package frechsack.dev.util.collection;

import frechsack.dev.util.Pair;
import frechsack.dev.util.array.Array;
import frechsack.dev.util.route.BiIterator;

import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Provides static functions for Collection operations.
 *
 * @author frechsack
 */
public class CollectionUtils
{
    private CollectionUtils() {}

    public static <E> Stream<Pair<Integer, E>> indexStream(java.util.Collection<E> clt)
    {
        Iterator<E> iterator = clt.iterator();
        return IntStream.range(0, clt.size()).mapToObj(index -> Pair.of(index, iterator.next()));
    }

    public static <E, V, Z> Stream<Pair<E, V>> pairStream(Collection<Z> clt, Function<Z, E> ze, Function<Z, V> zv)
    {
        return clt.stream().map(z -> Pair.of(ze.apply(z), zv.apply(z)));
    }

    public static <E> Collection<E> addAll(Collection<E> collection, Iterator<E> iterator)
    {
        return addAll(collection, iterator, 0);
    }

    public static <E> Collection<E> addAll(Collection<E> collection, Iterator<? extends E> iterator, int size)
    {
        Objects.requireNonNull(collection);
        if (iterator == null) return collection;

        // known size
        if (size > 0 && collection instanceof ArrayList) ((ArrayList<E>) collection).ensureCapacity(collection.size() + size);
        while (iterator.hasNext()) collection.add(iterator.next());

        return collection;
    }

    public static <E> Collection<E> addAll(Collection<E> collection, Array<? extends E> array)
    {
        if (array == null || array.length() == 0) return collection;

        if (collection instanceof ArrayList) ((ArrayList<E>) collection).ensureCapacity(collection.size() + array.length());
        for (int i = 0; i < array.length(); i++)
            collection.add(array.get(i));
        return collection;
    }

    /**
     * Converts a {@link Collection} to a {@link Set}.
     *
     * @param collection The collection.
     * @param <E>        The element´s class-type.
     * @return Returns a Set.
     */
    public static <E> Set<E> toSet(java.util.Collection<E> collection)
    {
        if (collection instanceof Set) return (Set<E>) collection;
        return new SetWrapper<>(Objects.requireNonNull(collection));
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
    public static <E> Collection<E> addAll(Collection<E> collection, E... array)
    {
        if (array == null || array.length == 0) return collection;
        Objects.requireNonNull(collection);
        collection.addAll(new ArrayWrapper<>(Objects.requireNonNull(array)));
        return collection;
    }

    /**
     * Returns an Enumeration based on a Iterator.
     * @param iterator The Iterator.
     * @param <E> The elements class-type.
     * @return Returns an Enumeration.
     */
    public static <E> Enumeration<E> toEnumeration(Iterator<E> iterator){
        return new IteratorWrapper<>(Objects.requireNonNull(iterator));
    }

    /**
     * Returns a synchronized version of the specified Queue.
     *
     * @param queue The Queue.
     * @param <E>   The Queue elements type.
     * @return Returns  a synchronized version of the specified Queue.
     */
    public static <E> Queue<E> synchronizedQueue(Queue<E> queue)
    {
        if (queue instanceof SynchronousQueue || queue instanceof SynchronizedQueue) return queue;
        return new SynchronizedQueue<>(Objects.requireNonNull(queue));
    }

    /**
     * Returns a synchronized version of the specified Deque.
     *
     * @param deque The Deque.
     * @param <E>   The Deque elements type.
     * @return Returns a synchronized version of the specified Deque.
     */
    public static <E> Queue<E> synchronizedDeque(Deque<E> deque)
    {
        if (deque instanceof SynchronousQueue) return deque;
        return new SynchronizedDeque<>(Objects.requireNonNull(deque));
    }

    private static class SynchronizedDeque<E> extends SynchronizedQueue<E> implements Deque<E>
    {

        private final Deque<E> deque;

        private SynchronizedDeque(Deque<E> deque)
        {
            super(deque);
            this.deque = deque;
        }

        @Override
        public void addFirst(E e)
        {
            synchronized (mutex)
            {
                deque.addFirst(e);
            }
        }

        @Override
        public void addLast(E e)
        {
            synchronized (mutex)
            {
                deque.addLast(e);
            }
        }

        @Override
        public boolean offerFirst(E e)
        {
            synchronized (mutex)
            {
                return deque.offerFirst(e);
            }
        }

        @Override
        public boolean offerLast(E e)
        {
            synchronized (mutex)
            {
                return deque.offerLast(e);
            }
        }

        @Override
        public E removeFirst()
        {
            synchronized (mutex)
            {
                return deque.removeFirst();
            }
        }

        @Override
        public E removeLast()
        {
            synchronized (mutex)
            {
                return deque.removeLast();
            }
        }

        @Override
        public E pollFirst()
        {
            synchronized (mutex)
            {
                return deque.pollFirst();
            }
        }

        @Override
        public E pollLast()
        {
            synchronized (mutex)
            {
                return deque.pollLast();
            }
        }

        @Override
        public E getFirst()
        {
            synchronized (mutex)
            {
                return deque.getFirst();
            }
        }

        @Override
        public E getLast()
        {
            synchronized (mutex)
            {
                return deque.getLast();
            }
        }

        @Override
        public E peekFirst()
        {
            synchronized (mutex)
            {
                return deque.peekFirst();
            }
        }

        @Override
        public E peekLast()
        {
            synchronized (mutex)
            {
                return deque.peekLast();
            }
        }

        @Override
        public boolean removeFirstOccurrence(Object o)
        {
            synchronized (mutex)
            {
                return deque.removeFirstOccurrence(o);
            }
        }

        @Override
        public boolean removeLastOccurrence(Object o)
        {
            synchronized (mutex)
            {
                return deque.removeLastOccurrence(o);
            }
        }

        @Override
        public void push(E e)
        {
            synchronized (mutex)
            {
                deque.push(e);
            }
        }

        @Override
        public E pop()
        {
            synchronized (mutex)
            {
                return deque.pop();
            }
        }

        @Override
        public Iterator<E> descendingIterator()
        {
            synchronized (mutex)
            {
                return deque.descendingIterator();
            }
        }

        @Override
        public String toString()
        {
            return "SynchronizedDeque{" + "deque=" + deque + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (Objects.equals(deque, o)) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            SynchronizedDeque<?> that = (SynchronizedDeque<?>) o;
            return Objects.equals(deque, that.deque);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(super.hashCode(), deque);
        }
    }

    private static class SynchronizedQueue<E> implements Queue<E>
    {
        protected final Object mutex;
        protected final Queue<E> queue;

        private SynchronizedQueue(Queue<E> queue)
        {
            this.queue = queue;
            mutex = this;
        }

        @Override
        public int size()
        {
            synchronized (mutex)
            {
                return queue.size();
            }
        }

        @Override
        public boolean isEmpty()
        {
            synchronized (mutex)
            {
                return queue.isEmpty();
            }
        }

        @Override
        public boolean contains(Object o)
        {
            synchronized (mutex)
            {
                return queue.contains(o);
            }
        }

        @Override
        public Iterator<E> iterator()
        {
            synchronized (mutex)
            {
                return queue.iterator();
            }
        }

        @Override
        public void forEach(Consumer<? super E> action)
        {
            synchronized (mutex)
            {
                queue.forEach(action);
            }
        }

        @Override
        public Object[] toArray()
        {
            synchronized (mutex)
            {
                return queue.toArray();
            }
        }

        @SuppressWarnings("SuspiciousToArrayCall")
        @Override
        public <T> T[] toArray(T[] a)
        {
            synchronized (mutex)
            {
                return queue.toArray(a);
            }
        }

        @SuppressWarnings("SuspiciousToArrayCall")
        @Override
        public <T> T[] toArray(IntFunction<T[]> generator)
        {
            synchronized (mutex)
            {
                return queue.toArray(generator);
            }
        }

        @Override
        public boolean add(E e)
        {
            synchronized (mutex)
            {
                return queue.add(e);
            }
        }

        @Override
        public boolean remove(Object o)
        {
            synchronized (mutex)
            {
                return queue.remove(o);
            }
        }

        @Override
        public boolean containsAll(Collection<?> c)
        {
            synchronized (mutex)
            {
                return queue.containsAll(c);
            }
        }

        @Override
        public boolean addAll(Collection<? extends E> c)
        {
            synchronized (mutex)
            {
                return queue.addAll(c);
            }
        }

        @Override
        public boolean removeAll(Collection<?> c)
        {
            synchronized (mutex)
            {
                return queue.removeAll(c);
            }
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter)
        {
            synchronized (mutex)
            {
                return queue.removeIf(filter);
            }
        }

        @Override
        public boolean retainAll(Collection<?> c)
        {
            synchronized (mutex)
            {
                return queue.retainAll(c);
            }
        }

        @Override
        public void clear()
        {
            synchronized (mutex)
            {
                queue.clear();
            }
        }

        @Override
        public Spliterator<E> spliterator()
        {
            synchronized (mutex)
            {
                return queue.spliterator();
            }
        }

        @Override
        public Stream<E> stream()
        {
            synchronized (mutex)
            {
                return queue.stream();
            }
        }

        @Override
        public Stream<E> parallelStream()
        {
            synchronized (mutex)
            {
                return queue.parallelStream();
            }
        }

        @Override
        public boolean offer(E e)
        {
            synchronized (mutex)
            {
                return queue.offer(e);
            }
        }

        @Override
        public E remove()
        {
            synchronized (mutex)
            {
                return queue.remove();
            }
        }

        @Override
        public E poll()
        {
            synchronized (mutex)
            {
                return queue.poll();
            }
        }

        @Override
        public E element()
        {
            synchronized (mutex)
            {
                return queue.element();
            }
        }

        @Override
        public E peek()
        {
            synchronized (mutex)
            {
                return queue.peek();
            }
        }

        @Override
        public String toString()
        {
            return "SynchronizedQueue{" + "queue=" + queue + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (Objects.equals(queue, o)) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SynchronizedQueue<?> that = (SynchronizedQueue<?>) o;
            return Objects.equals(mutex, that.mutex) && Objects.equals(queue, that.queue);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(mutex, queue);
        }
    }

    private static class ArrayWrapper<E> implements Collection<E>
    {
        private final E[] array;

        private ArrayWrapper(E[] array) {this.array = array;}

        @Override
        public int size()
        {
            return array.length;
        }

        @Override
        public boolean isEmpty()
        {
            return array.length == 0;
        }

        @Override
        public boolean contains(Object o)
        {
            return parallelStream().anyMatch(element -> Objects.equals(o, element));
        }

        @Override
        public Iterator<E> iterator()
        {
            return BiIterator.of(array);
        }

        @Override
        public void forEach(Consumer<? super E> action)
        {
            parallelStream().forEach(action);
        }

        @Override
        public Object[] toArray()
        {
            return array;
        }

        @Override
        public <T> T[] toArray(T[] a)
        {
            //noinspection unchecked
            return (T[]) array;
        }

        @Override
        public <T> T[] toArray(IntFunction<T[]> generator)
        {
            //noinspection unchecked
            return (T[]) array;
        }

        @Override
        public boolean add(E e)
        {
            uoe("add");
            return false;
        }

        @Override
        public boolean remove(Object o)
        {
            uoe("remove");
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c)
        {
            uoe("containsAll");
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends E> c)
        {
            uoe("addAll");
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c)
        {
            uoe("removeAll");
            return false;
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter)
        {
            uoe("removeIf");
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c)
        {
            uoe("retainAll");
            return false;
        }

        @Override
        public void clear()
        {
            uoe("clear");
        }

        @Override
        public Spliterator<E> spliterator()
        {
            return Arrays.spliterator(array);
        }

        @Override
        public Stream<E> stream()
        {
            return StreamSupport.stream(Arrays.spliterator(array), false);
        }

        @Override
        public Stream<E> parallelStream()
        {
            return StreamSupport.stream(Arrays.spliterator(array), true);
        }

        private void uoe(String op)
        {
            throw new UnsupportedOperationException(op);
        }
    }

    private static class IteratorWrapper<E> implements Enumeration<E>
    {
        private final Iterator<E> iterator;

        private IteratorWrapper(Iterator<E> iterator) {this.iterator = iterator;}

        @Override
        public boolean hasMoreElements()
        {
            return iterator.hasNext();
        }

        @Override
        public E nextElement()
        {
            return iterator.next();
        }

        @Override
        public Iterator<E> asIterator()
        {
            return iterator;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IteratorWrapper<?> that = (IteratorWrapper<?>) o;
            return Objects.equals(iterator, that.iterator);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(iterator);
        }

        @Override
        public String toString()
        {
            return "IteratorWrapper{" + "iterator=" + iterator + '}';
        }
    }

    private static class SetWrapper<E> implements Set<E>
    {
        private final java.util.Collection<E> collection;

        private SetWrapper(java.util.Collection<E> collection) {this.collection = collection;}

        @Override
        public int size()
        {
            return collection.size();
        }

        @Override
        public boolean isEmpty()
        {
            return collection.isEmpty();
        }

        @Override
        public boolean contains(Object o)
        {
            return collection.contains(o);
        }

        @Override
        public Iterator<E> iterator()
        {
            return collection.iterator();
        }

        @Override
        public void forEach(Consumer<? super E> action)
        {
            collection.forEach(action);
        }

        @Override
        public Object[] toArray()
        {
            return collection.toArray();
        }

        @SuppressWarnings("SuspiciousToArrayCall")
        @Override
        public <T> T[] toArray(T[] a)
        {
            return collection.toArray(a);
        }

        @SuppressWarnings("SuspiciousToArrayCall")
        @Override
        public <T> T[] toArray(IntFunction<T[]> generator)
        {
            return collection.toArray(generator);
        }

        @Override
        public boolean add(E e)
        {
            if (contains(e)) return false;
            return collection.add(e);
        }

        @Override
        public boolean remove(Object o)
        {
            return collection.remove(o);
        }

        @Override
        public boolean containsAll(java.util.Collection<?> c)
        {
            return collection.containsAll(c);
        }

        @Override
        public boolean addAll(java.util.Collection<? extends E> c)
        {
            if (c.isEmpty()) return false;
            return collection.addAll(c.stream().filter(it -> !contains(it)).collect(Collectors.toSet()));
        }

        @Override
        public boolean retainAll(java.util.Collection<?> c)
        {
            return collection.retainAll(c);
        }

        @Override
        public boolean removeAll(java.util.Collection<?> c)
        {
            return collection.removeAll(c);
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter)
        {
            return collection.removeIf(filter);
        }

        @Override
        public void clear()
        {
            collection.clear();
        }

        @Override
        public Spliterator<E> spliterator()
        {
            return collection.spliterator();
        }

        @Override
        public Stream<E> stream()
        {
            return collection.stream();
        }

        @Override
        public Stream<E> parallelStream()
        {
            return collection.parallelStream();
        }

        @Override
        public String toString()
        {
            return "SetWrapper{" + "collection=" + collection + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (Objects.equals(collection, o)) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SetWrapper<?> that = (SetWrapper<?>) o;
            return Objects.equals(collection, that.collection);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(collection);
        }
    }

}
