package frechsack.prod.util.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ArrayDeque<Type> extends AbstractQueue<Type> implements Deque<Type> {

    private int firstIndex = -1;
    private int lastIndex = -1;

    private final float growFactor;

    private Object[] array;

    private final int maximumCapacity;

    public ArrayDeque(int maximumCapacity, Collection<Type> collection, float growFactor){
        array = new Object[collection.size()];
        this.maximumCapacity = maximumCapacity;
        for (Type type : collection) addLast(type);
        this.growFactor = growFactor;
    }

    public ArrayDeque(int maximumCapacity, Collection<Type> collection){
        this(maximumCapacity, collection, 2f);
    }

    public ArrayDeque(int maximumCapacity, float growFactor, int initialCapacity) {
        this.array = new Object[initialCapacity];
        this.maximumCapacity = maximumCapacity;
        this.growFactor = growFactor;
    }

    public ArrayDeque(int maximumCapacity, float growFactor) {
      this(maximumCapacity, growFactor, 0);
    }

    public ArrayDeque(int maximumCapacity) {
        this(maximumCapacity, 2f);
    }

    public ArrayDeque(float growFactor) {
        this(Integer.MAX_VALUE, growFactor);
    }

    public ArrayDeque() {
        this(Integer.MAX_VALUE);
    }

    private boolean isMaximumCapacityNotSufficient(int minCapacity){
        return maximumCapacity < minCapacity;
    }

    private RuntimeException capacityExceededException(){
        throw new IllegalStateException();
    }

    private void requireCapacity(int minCapacity){
        if (minCapacity <= capacity())
            return;

        if (isEmpty()){
            array = new Object[minCapacity];
        }
        else {
            int size = size();
            Object[] newArray = new Object[Math.max((int)((float)size * growFactor), minCapacity)];
            // does left-shift
            System.arraycopy(array,firstIndex, newArray, 0, array.length);
            firstIndex = 0;
            lastIndex = size - 1;
            array = newArray;
        }
    }

    private void shiftElementsInRangeBy(int startIndex, int endIndex, int shiftBy){
        if (shiftBy > 0) {
            for (int i = endIndex + shiftBy; i >= startIndex + shiftBy; i-- )
                array[i] = array[i - shiftBy];
            if (endIndex + shiftBy > lastIndex)
                lastIndex = endIndex + shiftBy;
            if (startIndex == firstIndex)
                firstIndex += shiftBy;
        }
        else if (shiftBy < 0) {
            for (int i = startIndex + shiftBy; i <= endIndex + shiftBy; i++)
                array[i] = array[i - shiftBy];
            if (endIndex == lastIndex)
                lastIndex += shiftBy;
            if (startIndex + shiftBy < firstIndex)
                firstIndex = startIndex + shiftBy;
        }

        for (int i = 0; i < firstIndex; i++)
            array[i] = null;
        for (int i = lastIndex + 1; i < array.length; i++)
            array[i] = null;
    }

    public void trimToSize(){
        int size = size();
        if (size == 0 || array.length == size)
            return;

        Object[] newArray = new Object[size];
        System.arraycopy(array, firstIndex, newArray, 0, newArray.length);
        array = newArray;
        firstIndex = 0;
        lastIndex = size -1;
    }

    public int capacity(){
        return array.length;
    }

    public int maximumCapacity(){
        return maximumCapacity;
    }

    @Override
    public int size() {
        if (lastIndex == -1 && firstIndex == -1)
            return 0;
        return lastIndex - firstIndex + 1;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void addFirst(Type value) {
        if (!offerFirst(value))
            throw capacityExceededException();
    }

    @Override
    public void addLast(Type value) {
        if (!offerLast(value))
            throw capacityExceededException();
    }

    @Override
    public boolean offerFirst(Type value) {
        int capacity = size() + 1;

        if(isMaximumCapacityNotSufficient(capacity))
            return false;

        requireCapacity(capacity);

        if (isEmpty()) {
            firstIndex = lastIndex = array.length - 1;
            array[firstIndex] = value;
        }
        else {
            if (firstIndex == 0) {
                int shiftBy = array.length - lastIndex - 1;
                shiftElementsInRangeBy(firstIndex,lastIndex, shiftBy);
            }
            array[--firstIndex] = value;
        }
        return true;
    }

    @Override
    public boolean offerLast(Type value) {
        int capacity = size() + 1;

        if(isMaximumCapacityNotSufficient(capacity))
            return false;

        requireCapacity(capacity);

        if (isEmpty()){
            firstIndex = lastIndex = 0;
            array[firstIndex] = value;
        }
        else {
            if (lastIndex == array.length - 1) {
                int shiftBy = -firstIndex;
                shiftElementsInRangeBy(firstIndex, lastIndex, shiftBy);
            }
            array[++lastIndex] = value;
        }
        return true;
    }

    @Override
    public Type removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        @SuppressWarnings("unchecked")
        Type value = (Type) array[firstIndex];
        array[firstIndex] = null;
        firstIndex++;
        return value;
    }

    @Override
    public Type removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        @SuppressWarnings("unchecked")
        Type value = (Type) array[lastIndex];
        array[lastIndex] = null;
        lastIndex--;
        return value;
    }

    @Override
    public Type pollFirst() {
        if (isEmpty())
            return null;
        @SuppressWarnings("unchecked") Type value = (Type) array[firstIndex];
        array[firstIndex] = null;
        firstIndex++;
        return value;
    }

    @Override
    public Type pollLast() {
        if (isEmpty())
            return null;
        @SuppressWarnings("unchecked") Type value = (Type) array[lastIndex];
        array[lastIndex] = null;
        lastIndex--;
        return value;
    }

    @Override
    public Type getFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        //noinspection unchecked
        return  (Type) array[firstIndex];
    }

    @Override
    public Type getLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        //noinspection unchecked
        return  (Type) array[lastIndex];
    }

    @Override
    public Type peekFirst() {
        if (isEmpty())
            return null;
        //noinspection unchecked
        return  (Type) array[firstIndex];
    }

    @Override
    public Type peekLast() {
        if (isEmpty())
            return null;
        //noinspection unchecked
        return  (Type) array[lastIndex];
    }

    private void removeAt(int index){
        int size = size();
        if (size == 0)
            return;
        if (size == 1){
            array[index] = null;
            firstIndex = -1;
            lastIndex = -1;
        }
        else if (index == firstIndex)
            shiftElementsInRangeBy(firstIndex + 1, lastIndex, -1);
        else
            shiftElementsInRangeBy(firstIndex, index - 1, 1);

    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (isEmpty())
            return false;
        for (int i = firstIndex; i <= lastIndex; i++)
            if (Objects.equals(array[i], o)){
                removeAt(i);
                return true;
            }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (isEmpty())
            return false;
        for (int i = lastIndex; i >= firstIndex; i--)
            if (Objects.equals(array[i], o)) {
                removeAt(i);
                return true;
            }
        return false;
    }

    @Override
    public void push(Type value) {
        addLast(value);
    }

    @Override
    public Type pop() {
        return removeFirst();
    }

    @Override
    public boolean contains(Object o) {
        if (isEmpty())
            return false;
        for (int i = firstIndex; i <= lastIndex; i++)
            if (Objects.equals(array[i], o))
                return true;
        return false;
    }

    private class Iterator implements java.util.Iterator<Type> {
        private int indexOffset = -1;

        private int initialFirstIndex = firstIndex;

        private int initialLastIndex = lastIndex;

        private final boolean isDescending;

        private Iterator(boolean isDescending) {
            this.isDescending = isDescending;
        }

        private void requireUnmodified(){
            if (!(initialFirstIndex == firstIndex && initialLastIndex == lastIndex))
                throw new ConcurrentModificationException();
        }

        @Override
        public boolean hasNext() {
            requireUnmodified();
            return indexOffset + 1 < size();
        }

        @Override
        public void remove() {
            requireUnmodified();
            if (indexOffset == -1)
                throw new UnsupportedOperationException();
            int index = isDescending ? lastIndex - indexOffset : firstIndex + indexOffset;
            ArrayDeque.this.removeAt(index);
            indexOffset--;
            initialFirstIndex = firstIndex;
            initialLastIndex = lastIndex;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Type next() {
            requireUnmodified();
            indexOffset++;
            int index = isDescending ? lastIndex - indexOffset : firstIndex + indexOffset;
            return (Type) array[index];
        }
    }

    @NotNull
    @Override
    public java.util.Iterator<Type> iterator() {
        return new Iterator(false);
    }

    @NotNull
    @Override
    public java.util.Iterator<Type> descendingIterator() {
        return new Iterator(true);
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        if (isEmpty())
            return new Object[0];

        return Arrays.copyOfRange(array, firstIndex, lastIndex + 1);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        if (isEmpty())
            return a;

        if (a.length < size())
            return (T[]) Arrays.copyOfRange(array, firstIndex, lastIndex, a.getClass());

        System.arraycopy(a, firstIndex, a, 0, size());
        return a;
    }


    @Override
    public boolean offer(Type value) {
        return offerLast(value);
    }

    @Override
    public Type poll() {
        return pollFirst();
    }

    @Override
    public Type peek() {
        return peekFirst();
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
