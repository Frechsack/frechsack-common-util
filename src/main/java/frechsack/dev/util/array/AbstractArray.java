package frechsack.dev.util.array;

import frechsack.dev.util.route.BiIterator;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Package-private implementations base of {@link Array}.
 *
 * @param <E> The Array´s element type.
 * @author Frechsack
 */
public abstract class AbstractArray<E> implements Array<E>, Serializable, RandomAccess {
    private Reference<ArrayFactory.ArrayList<E>> arrayListReference;

    protected abstract E getVoid();

    protected static final int STREAM_PREFERRED_LENGTH = 4096;

    protected AbstractArray() {
    }

    @Override
    public int firstIndexOf(Object element) {
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) {
                if (Objects.equals(element, get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length()).parallel().filter(index -> Objects.equals(element, get(index))).findFirst().orElse(-1);
    }

    @Override
    public int lastIndexOf(Object element) {
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = length() - 1; i > 0; i--) {
                if (Objects.equals(element, get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length())
                .parallel()
                .map(index -> length() - index - 1)
                .filter(index -> Objects.equals(element, get(index)))
                .findFirst()
                .orElse(-1);
    }

    @Override
    public int indexOf(int start, int end, Object element) {
        if (end - start < STREAM_PREFERRED_LENGTH) {
            for (int i = start; i < end; i++) {
                if (Objects.equals(element, get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(start, end).parallel().filter(index -> Objects.equals(element, get(index))).findAny().orElse(-1);
    }

    @Override
    public int indexOf(int start, int end, Predicate<E> predicate) {
        Objects.requireNonNull(predicate);
        if (end - start < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) {
                if (predicate.test(get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(start, end).parallel().filter(index -> predicate.test(get(index))).findAny().orElse(-1);
    }

    @Override
    public int lastIndexOf(Predicate<E> predicate) {
        Objects.requireNonNull(predicate);
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = length() - 1; i > 0; i--) {
                if (predicate.test(get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length()).parallel().map(index -> length() - index - 1).filter(index -> predicate.test(get(index))).findFirst().orElse(-1);
    }

    @Override
    public int firstIndexOf(Predicate<E> predicate) {
        Objects.requireNonNull(predicate);
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) {
                if (predicate.test(get(i))) return i;
            }
            return -1;
        }
        return IntStream.range(0, length()).parallel().filter(index -> predicate.test(get(index))).findFirst().orElse(-1);
    }

    @Override
    public boolean contains(Object element) {
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) if (Objects.equals(element, get(i))) return true;
            return false;
        }
        return IntStream.range(0, length()).parallel().anyMatch(index -> Objects.equals(element, get(index)));
    }

    @Override
    public void clear() {
        E voidValue = getVoid();
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) set(i, voidValue);
        } else {
            IntStream.range(0, length()).parallel().forEach(index -> set(index, voidValue));
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        if (length() < STREAM_PREFERRED_LENGTH) for (int i = 0; i < length(); i++) action.accept(get(i));
        else stream().forEach(action);
    }

    @Override
    public boolean equals(E[] array) {
        if (array == null) return false;
        if (array.length != length()) return false;
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) if (!Objects.equals(array[i], get(i))) return false;
            return true;
        }
        return IntStream.range(0, length()).parallel().allMatch(index -> Objects.equals(array[index], get(index)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Collection) return asList().equals(o);
        if (asArray() == o) return true;
        if (!(o instanceof Array)) return false;
        // Compare collection
        Array<?> oArray = (Array<?>) o;
        if (oArray.length() != length()) return false;
        for (int i = 0; i < length(); i++) {
            if (!Objects.equals(oArray.get(i), get(i))) return false;
        }
        return true;
    }

    @Override
    public E[] toArray() {
        E[] array = generator().apply(length());
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) {
                array[i] = get(i);
            }
        } else IntStream.range(0, length()).parallel().forEach(index -> array[index] = get(index));
        return array;
    }

    @Override
    public List<E> asList() {
        ArrayFactory.ArrayList<E> arrayList = arrayListReference == null ? null : arrayListReference.get();
        if (arrayList == null) arrayListReference = new SoftReference<>(arrayList = new ArrayFactory.ArrayList<>(this));
        return arrayList;
    }


    @Override
    public void fill(E element) {
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) {
                set(i, element);
            }
        } else IntStream.range(0, length()).parallel().forEach(index -> set(index, element));
    }

    @Override
    public void reverse() {
        if (length() < STREAM_PREFERRED_LENGTH) {
            int swapIndex;
            for (int i = 0; i < length() / 2; i++) {
                swapIndex = length() - 1 - i;
                set(i, getAndSet(swapIndex, get(i)));
            }
        } else IntStream.range(0, length() / 2).parallel().forEach(index ->
        {
            int swapIndex = length() - 1 - index;
            E swap = get(index);
            set(index, getAndSet(swapIndex, swap));
        });

    }


    @Override
    public void transform(Function<E, E> mapper) {
        Objects.requireNonNull(mapper);
        if (length() < STREAM_PREFERRED_LENGTH) {
            for (int i = 0; i < length(); i++) {
                set(i, mapper.apply(get(i)));
            }
        } else IntStream.range(0, length()).parallel().forEach(index -> set(index, mapper.apply(get(index))));
    }

    @Override
    public final Iterator<E> iterator() {
        return new ArrayFactory.ArrayIterator<>(this);
    }

    @Override
    public BiIterator<E> biIterator() {
        return BiIterator.of(this::get, length(), i -> set(i, getVoid()));
    }

}
