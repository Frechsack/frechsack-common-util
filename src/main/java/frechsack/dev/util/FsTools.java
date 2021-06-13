package frechsack.dev.util;

import frechsack.dev.util.route.Route;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FsTools
{
    private FsTools() {}

    /**
     * Common FileSystem operations.
     */
    public static class FileSystem
    {
        /**
         * Returns the last {@link Path} sibling.
         *
         * @param path The Path
         * @return Return the last sibling. Returns an empty String if the Path is null or empty.
         */
        public static String getLastName(Path path)
        {
            if (path == null) return "";
            return lastSibling(path);
        }

        private static String lastSibling(Path path)
        {
            String lastName = path.getName(path.getNameCount() - 1).toString();
            return lastName == null ? "" : lastName;
        }

        /**
         * Returns the extension of a {@link Path} without the dot.
         *
         * @param path The Path.
         * @return Returns the extension. Returns an empty String if the Path does not contain an extension.
         */
        public static String getExtension(Path path)
        {
            if (path == null) return "";
            String lastSibling = lastSibling(path);
            int dotIndex = lastSibling.lastIndexOf('.');
            if (dotIndex == -1) return "";
            return lastSibling.substring(++dotIndex);
        }

        /**
         * Returns the name of a {@link Path}.
         *
         * @param path The Path.
         * @return Returns the name. Returns an empty String if the Path does not contain a sibling.
         */
        public static String getName(Path path)
        {
            if (path == null) return "";
            String lastName = getLastName(path);
            int dotIndex = lastName.lastIndexOf('.');
            if (dotIndex == -1) return "";
            return lastName.substring(0, dotIndex);
        }
    }

    /**
     * Common static operations for {@link Collection}.
     */
    public static class Collections
    {
        private Collections() {}

        public static <E> Stream<Pair<Integer, E>> indexStream(java.util.Collection<E> clt)
        {
            Iterator<E> iterator = clt.iterator();
            return IntStream.range(0, clt.size()).mapToObj(index -> Pair.of(index, iterator.next()));
        }

        public static <E, V, Z> Stream<Pair<E, V>> pairStream(Collection<Z> clt, Function<Z, E> ze, Function<Z, V> zv)
        {
            return clt.stream().map(z -> Pair.of(ze.apply(z), zv.apply(z)));
        }

        /**
         * Converts a {@link Collection} to a {@link Set}.
         * @param collection The collection.
         * @param <E> The element´s classtype.
         * @return Returns a Set.
         */
        public static <E> Set<E> toSet(java.util.Collection<E> collection)
        {
            if (collection instanceof Set) return (Set<E>) collection;
            return new FsToolsSetWrapper<>(collection);
        }

        /**
         * Adds any element in the specified array to the specified {@link Collection}.
         * @param collection The collection.
         * @param array The array.
         * @param <E> The element´s classtype.
         * @return Returns the passed Collection.
         */
        @SafeVarargs
        public static <E> Collection<E> addAll(Collection<E> collection, E... array)
        {
            if (array == null || array.length == 0) return collection;
            Objects.requireNonNull(collection);
            collection.addAll(new FsStdReadonlyCollection<>(array));
            return collection;
        }


        private static class FsStdReadonlyCollection<E> implements Collection<E>
        {
            private final E[] array;

            private FsStdReadonlyCollection(E[] array) {this.array = array;}

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
                return Route.of(array);
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

        private static class FsToolsSetWrapper<E> implements Set<E>
        {
            private final java.util.Collection<E> collection;

            private FsToolsSetWrapper(java.util.Collection<E> collection) {this.collection = collection;}

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
                return collection.addAll(c);
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
                return "FsToolsSetWrapper{" + "collection=" + collection + '}';
            }

            @Override
            public boolean equals(Object o)
            {
                if (this == o) return true;
                if (Objects.equals(o, collection)) return true;
                if (o == null || getClass() != o.getClass()) return false;
                FsToolsSetWrapper<?> that = (FsToolsSetWrapper<?>) o;
                return Objects.equals(collection, that.collection);
            }

            @Override
            public int hashCode()
            {
                return Objects.hash(collection);
            }
        }

    }
}
