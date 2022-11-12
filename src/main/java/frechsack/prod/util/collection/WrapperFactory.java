package frechsack.prod.util.collection;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class WrapperFactory {

    record SetWrapper<E>(Collection<E> collection) implements Set<E> {

        @Override
        public int size() {
            return collection.size();
        }

        @Override
        public boolean isEmpty() {
            return collection.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return collection.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return collection.iterator();
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            collection.forEach(action);
        }

        @Override
        public Object[] toArray() {
            return collection.toArray();
        }

        @SuppressWarnings("SuspiciousToArrayCall")
        @Override
        public <T> T[] toArray(T[] a) {
            return collection.toArray(a);
        }

        @SuppressWarnings("SuspiciousToArrayCall")
        @Override
        public <T> T[] toArray(IntFunction<T[]> generator) {
            return collection.toArray(generator);
        }

        @Override
        public boolean add(E e) {
            if (contains(e)) return false;
            return collection.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return collection.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return collection.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            if (c.isEmpty()) return false;
            return collection.addAll(c.stream().filter(it -> !contains(it)).collect(Collectors.toSet()));
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return collection.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return collection.removeAll(c);
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            return collection.removeIf(filter);
        }

        @Override
        public void clear() {
            collection.clear();
        }

        @Override
        public Spliterator<E> spliterator() {
            return collection.spliterator();
        }

        @Override
        public Stream<E> stream() {
            return collection.stream();
        }

        @Override
        public Stream<E> parallelStream() {
            return collection.parallelStream();
        }

        @Override
        public String toString() {
            return "SetWrapper{" + "collection=" + collection + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (Objects.equals(collection, o)) return true;
            return collection.equals(o);
        }
    }

    static class SynchronizedDequeWrapper<E> extends SynchronizedQueueWrapper<E> implements Deque<E> {

        private final Deque<E> deque;

        SynchronizedDequeWrapper(Deque<E> deque) {
            super(deque);
            this.deque = deque;
        }

        @Override
        public void addFirst(E e) {
            synchronized (mutex) {
                deque.addFirst(e);
            }
        }

        @Override
        public void addLast(E e) {
            synchronized (mutex) {
                deque.addLast(e);
            }
        }

        @Override
        public boolean offerFirst(E e) {
            synchronized (mutex) {
                return deque.offerFirst(e);
            }
        }

        @Override
        public boolean offerLast(E e) {
            synchronized (mutex) {
                return deque.offerLast(e);
            }
        }

        @Override
        public E removeFirst() {
            synchronized (mutex) {
                return deque.removeFirst();
            }
        }

        @Override
        public E removeLast() {
            synchronized (mutex) {
                return deque.removeLast();
            }
        }

        @Override
        public E pollFirst() {
            synchronized (mutex) {
                return deque.pollFirst();
            }
        }

        @Override
        public E pollLast() {
            synchronized (mutex) {
                return deque.pollLast();
            }
        }

        @Override
        public E getFirst() {
            synchronized (mutex) {
                return deque.getFirst();
            }
        }

        @Override
        public E getLast() {
            synchronized (mutex) {
                return deque.getLast();
            }
        }

        @Override
        public E peekFirst() {
            synchronized (mutex) {
                return deque.peekFirst();
            }
        }

        @Override
        public E peekLast() {
            synchronized (mutex) {
                return deque.peekLast();
            }
        }

        @Override
        public boolean removeFirstOccurrence(Object o) {
            synchronized (mutex) {
                return deque.removeFirstOccurrence(o);
            }
        }

        @Override
        public boolean removeLastOccurrence(Object o) {
            synchronized (mutex) {
                return deque.removeLastOccurrence(o);
            }
        }

        @Override
        public void push(E e) {
            synchronized (mutex) {
                deque.push(e);
            }
        }

        @Override
        public E pop() {
            synchronized (mutex) {
                return deque.pop();
            }
        }

        @Override
        public Iterator<E> descendingIterator() {
            synchronized (mutex) {
                return deque.descendingIterator();
            }
        }

        @Override
        public String toString() {
            return "SynchronizedDequeWrapper{" + "deque=" + deque + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (Objects.equals(deque, o)) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            SynchronizedDequeWrapper<?> that = (SynchronizedDequeWrapper<?>) o;
            return Objects.equals(deque, that.deque);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), deque);
        }
    }

    static class SynchronizedQueueWrapper<E> implements Queue<E> {
        protected final Object mutex;
        protected final Queue<E> queue;

        SynchronizedQueueWrapper(Queue<E> queue) {
            this.queue = queue;
            mutex = this;
        }

        @Override
        public int size() {
            synchronized (mutex) {
                return queue.size();
            }
        }

        @Override
        public boolean isEmpty() {
            synchronized (mutex) {
                return queue.isEmpty();
            }
        }

        @Override
        public boolean contains(Object o) {
            synchronized (mutex) {
                return queue.contains(o);
            }
        }

        @Override
        public Iterator<E> iterator() {
            synchronized (mutex) {
                return queue.iterator();
            }
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            synchronized (mutex) {
                queue.forEach(action);
            }
        }

        @Override
        public Object[] toArray() {
            synchronized (mutex) {
                return queue.toArray();
            }
        }

        @SuppressWarnings("SuspiciousToArrayCall")
        @Override
        public <T> T[] toArray(T[] a) {
            synchronized (mutex) {
                return queue.toArray(a);
            }
        }

        @SuppressWarnings("SuspiciousToArrayCall")
        @Override
        public <T> T[] toArray(IntFunction<T[]> generator) {
            synchronized (mutex) {
                return queue.toArray(generator);
            }
        }

        @Override
        public boolean add(E e) {
            synchronized (mutex) {
                return queue.add(e);
            }
        }

        @Override
        public boolean remove(Object o) {
            synchronized (mutex) {
                return queue.remove(o);
            }
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            synchronized (mutex) {
                return queue.containsAll(c);
            }
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            synchronized (mutex) {
                return queue.addAll(c);
            }
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            synchronized (mutex) {
                return queue.removeAll(c);
            }
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            synchronized (mutex) {
                return queue.removeIf(filter);
            }
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            synchronized (mutex) {
                return queue.retainAll(c);
            }
        }

        @Override
        public void clear() {
            synchronized (mutex) {
                queue.clear();
            }
        }

        @Override
        public Spliterator<E> spliterator() {
            synchronized (mutex) {
                return queue.spliterator();
            }
        }

        @Override
        public Stream<E> stream() {
            synchronized (mutex) {
                return queue.stream();
            }
        }

        @Override
        public Stream<E> parallelStream() {
            synchronized (mutex) {
                return queue.parallelStream();
            }
        }

        @Override
        public boolean offer(E e) {
            synchronized (mutex) {
                return queue.offer(e);
            }
        }

        @Override
        public E remove() {
            synchronized (mutex) {
                return queue.remove();
            }
        }

        @Override
        public E poll() {
            synchronized (mutex) {
                return queue.poll();
            }
        }

        @Override
        public E element() {
            synchronized (mutex) {
                return queue.element();
            }
        }

        @Override
        public E peek() {
            synchronized (mutex) {
                return queue.peek();
            }
        }

        @Override
        public String toString() {
            return "SynchronizedQueueWrapper{" + "queue=" + queue + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (Objects.equals(queue, o)) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SynchronizedQueueWrapper<?> that = (SynchronizedQueueWrapper<?>) o;
            return Objects.equals(mutex, that.mutex) && Objects.equals(queue, that.queue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mutex, queue);
        }
    }

    record IteratorWrapper<E>(Iterator<E> iterator) implements Enumeration<E> {

        @Override
        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        @Override
        public E nextElement() {
            return iterator.next();
        }

        @Override
        public Iterator<E> asIterator() {
            return iterator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IteratorWrapper<?> that = (IteratorWrapper<?>) o;
            return Objects.equals(iterator, that.iterator);
        }

        @Override
        public String toString() {
            return "IteratorWrapper{" + "iterator=" + iterator + '}';
        }
    }

}
