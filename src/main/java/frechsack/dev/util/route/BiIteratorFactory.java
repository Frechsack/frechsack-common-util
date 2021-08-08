package frechsack.dev.util.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.*;

/**
 * Package-private implementations of {@link BiIterator}.
 *
 * @author Frechsack
 */
public class BiIteratorFactory
{
    static class FixedIndexBiIterator<E> implements BiIterator<E>
    {
        private final IntFunction<E> items;
        private final int size;
        private final IntConsumer remove;
        private int index = -1;

        FixedIndexBiIterator(IntFunction<E> items, int size, IntConsumer remove)
        {
            this.items = items;
            this.size = size;
            this.remove = remove;
        }

        @Override
        public final void remove()
        {
            if (remove == null) throw new UnsupportedOperationException("remove");
            remove.accept(index);
        }


        @Override
        public final boolean hasNext()
        {
            return size > index + 1;
        }

        @Override
        public final boolean hasPrevious()
        {
            return index - 1 >= 0;
        }

        @Override
        public final E next()
        {
            int index = this.index + 1;
            Objects.checkIndex(index, size);
            this.index = index;
            return items.apply(index);
        }

        @Override
        public final E previous()
        {
            int index = this.index - 1;
            Objects.checkIndex(index, size);
            this.index = index;
            return items.apply(index);
        }

        @Override
        public final E last()
        {
            this.index = size - 2;
            return next();
        }

        @Override
        public final E first()
        {
            this.index = -1;
            return next();
        }

        @Override
        public String toString()
        {
            return "FixedIndexRoute{" + "items=" + items + ", size=" + size + ", removeFunction=" + remove + ", index=" + index + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FixedIndexBiIterator<?> that = (FixedIndexBiIterator<?>) o;
            return index == that.index && Objects.equals(items, that.items) && Objects.equals(size, that.size) && Objects.equals(remove, that.remove);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(items, size, remove, index);
        }
    }

    static class ListIteratorBiIterator<E> implements BiIterator<E>
    {
        private final ListIterator<E> listIterator;

        ListIteratorBiIterator(ListIterator<E> listIterator)
        {
            this.listIterator = listIterator;
        }

        @Override
        public boolean hasNext()
        {
            return listIterator.hasNext();
        }

        @Override
        public boolean hasPrevious()
        {
            return listIterator.hasPrevious();
        }

        @Override
        public E next()
        {
            return listIterator.next();
        }

        @Override
        public void remove()
        {
            listIterator.remove();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action)
        {
            listIterator.forEachRemaining(action);
        }

        @Override
        public E previous()
        {
            return listIterator.previous();
        }
    }

    static class IteratorBiIterator<E> implements BiIterator<E>
    {
        private final Iterator<E> elementIterator;
        private final ArrayList<E> passedElementLs = new ArrayList<>();
        private int passedIndex = -1;

        IteratorBiIterator(Iterator<E> elementIterator)
        {
            this.elementIterator = elementIterator;
        }

        @Override
        public void remove()
        {
            try
            {
                elementIterator.remove();
                passedElementLs.remove(passedIndex--);
            }
            catch (Exception ignored)
            {
            }

        }

        @Override
        public final boolean hasNext()
        {
            if (passedIndex == passedElementLs.size() - 1) return elementIterator.hasNext();
            return passedElementLs.size() > passedIndex;
        }

        @Override
        public final boolean hasPrevious()
        {
            return passedIndex > 0;
        }

        @Override
        public final E next()
        {
            // Check if next element is in list
            if (passedIndex != -1 && passedIndex < passedElementLs.size() - 1)
            {
                return passedElementLs.get(++passedIndex);
            }
            E next = elementIterator.next();
            passedElementLs.add(next);
            passedIndex++;
            return next;
        }

        @Override
        public final E previous()
        {
            return passedElementLs.get(--passedIndex);
        }

        @Override
        public final E last()
        {
            while (elementIterator.hasNext()) passedElementLs.add(elementIterator.next());
            return passedElementLs.get(passedIndex = passedElementLs.size() - 1);
        }

        @Override
        public final E first()
        {
            if (passedElementLs.isEmpty()) Objects.checkIndex(-1, 0);
            return passedElementLs.get(passedIndex = 0);
        }

        @Override
        public String toString()
        {
            return "IteratorRoute{" + "elementIterator=" + elementIterator + ", passedElementLs=" + passedElementLs + ", passedIndex=" + passedIndex + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IteratorBiIterator<?> that = (IteratorBiIterator<?>) o;
            return passedIndex == that.passedIndex &&
                   Objects.equals(elementIterator, that.elementIterator) &&
                   Objects.equals(passedElementLs, that.passedElementLs);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(elementIterator, passedElementLs, passedIndex);
        }
    }

    static class SingleBiIterator<E> implements BiIterator<E>
    {
        private E single;
        private boolean isInitial = true;

        /**
         * Creates a new SingleRoute.
         *
         * @param value The element.
         */
        SingleBiIterator(E value)
        {
            setSingle(value);
        }

        /**
         * Resets this Route and reapplies a new value to it.
         *
         * @param single The new value.
         */
        public void setSingle(E single)
        {
            this.single = single;
            isInitial = true;
        }

        @Override
        public boolean hasNext()
        {
            return isInitial;
        }

        @Override
        public boolean hasPrevious()
        {
            return false;
        }

        @Override
        public E next()
        {
            if (isInitial)
            {
                isInitial = false;
                return single;
            }
            throw new ArrayIndexOutOfBoundsException(1);
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action)
        {
            if (isInitial) action.accept(single);
        }

        @Override
        public E previous()
        {
            throw new ArrayIndexOutOfBoundsException(1);
        }

        @Override
        public E last()
        {
            isInitial = false;
            return single;
        }

        @Override
        public E first()
        {
            return last();
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SingleBiIterator<?> that = (SingleBiIterator<?>) o;
            return isInitial == that.isInitial && Objects.equals(single, that.single);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(single, isInitial);
        }

        @Override
        public String toString()
        {
            return "SingleRoute{" + "single=" + single + ", isInitial=" + isInitial + '}';
        }
    }

    static class DynamicIndexBiIterator<E> implements BiIterator<E>
    {
        private final IntFunction<E> items;
        private final IntSupplier size;
        private final IntUnaryOperator removeFunction;
        private int index = -1;

        DynamicIndexBiIterator(IntFunction<E> items, IntSupplier size, IntUnaryOperator removeOperation)
        {
            this.items = items;
            this.size = size;
            this.removeFunction = removeOperation;
        }

        @Override
        public final void remove()
        {
            if (removeFunction == null) throw new UnsupportedOperationException("remove");
            int newIndex = removeFunction.applyAsInt(index);
            if (newIndex == -1) return;
            index = newIndex;
        }


        @Override
        public final boolean hasNext()
        {
            return size.getAsInt() > index + 1;
        }

        @Override
        public final boolean hasPrevious()
        {
            return index - 1 >= 0;
        }

        @Override
        public final E next()
        {
            int index = this.index + 1;
            Objects.checkIndex(index, size.getAsInt());
            this.index = index;
            return items.apply(index);
        }

        @Override
        public final E previous()
        {
            int index = this.index - 1;
            // Size may changed
            int size = this.size.getAsInt();
            if (index >= size) index = size - 1;
            else Objects.checkIndex(index, size);
            this.index = index;
            return items.apply(index);
        }

        @Override
        public final E last()
        {
            this.index = size.getAsInt() - 2;
            return next();
        }

        @Override
        public final E first()
        {
            this.index = -1;
            return next();
        }

        @Override
        public String toString()
        {
            return "DynamicIndexRoute{" + "items=" + items + ", size=" + size + ", removeFunction=" + removeFunction + ", index=" + index + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DynamicIndexBiIterator<?> that = (DynamicIndexBiIterator<?>) o;
            return index == that.index &&
                   Objects.equals(items, that.items) &&
                   Objects.equals(size, that.size) &&
                   Objects.equals(removeFunction, that.removeFunction);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(items, size, removeFunction, index);
        }
    }
}
