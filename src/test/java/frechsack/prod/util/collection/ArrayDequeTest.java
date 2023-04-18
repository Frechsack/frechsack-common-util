package frechsack.prod.util.collection;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;

public class ArrayDequeTest {

    @Test
    public void size() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        Assert.assertEquals(0, deque.size());

        deque.addLast(1);
        deque.addFirst(2);
        Assert.assertEquals(2, deque.size());

    }

    @Test
    public void isEmpty() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        Assert.assertTrue(deque.isEmpty());

        deque.addFirst(1);
        Assert.assertFalse(deque.isEmpty());
    }

    @Test
    public void addFirst() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        deque.addFirst(1);
        deque.addFirst(2);
        Assert.assertArrayEquals(new Object[]{2,1}, deque.toArray());

        deque.addFirst(3);
        deque.addFirst(4);
        Assert.assertArrayEquals(new Object[]{4,3,2,1}, deque.toArray());
    }

    @Test
    public void addLast() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        Assert.assertArrayEquals(new Object[]{}, deque.toArray());

        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addLast(4);
        Assert.assertArrayEquals(new Object[]{1,2,3,4}, deque.toArray());
    }

    @Test
    public void offerFirst() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        Assert.assertArrayEquals(new Object[]{}, deque.toArray());

        deque.offerFirst(1);
        deque.offerFirst(2);
        deque.offerFirst(3);
        deque.offerFirst(4);
        Assert.assertArrayEquals(new Object[]{4,3,2,1}, deque.toArray());

        deque.offerFirst(5);
        deque.offerFirst(6);
        Assert.assertArrayEquals(new Object[]{6,5,4,3,2,1}, deque.toArray());
    }

    @Test
    public void offerLast() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        Assert.assertArrayEquals(new Object[]{}, deque.toArray());

        deque.offerLast(1);
        deque.offerLast(2);
        deque.offerLast(3);
        deque.offerLast(4);
        Assert.assertArrayEquals(new Object[]{1,2,3,4}, deque.toArray());

        deque.offerLast(5);
        deque.offerLast(6);
        Assert.assertArrayEquals(new Object[]{1,2,3,4,5,6}, deque.toArray());
    }

    @Test
    public void removeFirstOccurrence() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addLast(3);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(5);
        deque.addLast(2);
        deque.removeFirstOccurrence(3);
        Assert.assertArrayEquals(new Object[]{1,2,3,4,5,5,2}, deque.toArray());

        deque.removeFirstOccurrence(5);
        Assert.assertArrayEquals(new Object[]{1,2,3,4,5,2}, deque.toArray());

        deque.removeFirstOccurrence(2);
        Assert.assertArrayEquals(new Object[]{1,3,4,5,2}, deque.toArray());
    }

    @Test
    public void removeLastOccurrence() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addLast(3);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(5);
        deque.addLast(2);
        deque.removeLastOccurrence(5);
        Assert.assertArrayEquals(new Object[]{1,2,3,3,4,5,2}, deque.toArray());

        deque.removeLastOccurrence(2);
        Assert.assertArrayEquals(new Object[]{1,2,3,3,4,5}, deque.toArray());

        deque.removeLastOccurrence(5);
        Assert.assertArrayEquals(new Object[]{1,2,3,3,4}, deque.toArray());
    }

    @Test
    public void cross(){
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        deque.addLast(1);
        deque.addLast(2);
        Assert.assertArrayEquals(new Object[]{1,2}, deque.toArray());

        deque.addLast(3);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);
        Assert.assertArrayEquals(new Object[]{1,2,3,4,5,6}, deque.toArray());

        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        Assert.assertArrayEquals(new Object[]{3,2,1,1,2,3,4,5,6}, deque.toArray());

        deque.addLast(7);
        Assert.assertArrayEquals(new Object[]{3,2,1,1,2,3,4,5,6,7}, deque.toArray());
    }

    @Test
    public void contains() {

    }

    @Test
    public void iterator() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10, 2f);
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addLast(4);

        Iterator<Integer> iterator = deque.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, (int) iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, (int) iterator.next());

        iterator.remove();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, (int) iterator.next());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(4, (int) iterator.next());
        iterator.remove();

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void descendingIterator() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10, 2f);
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addLast(4);

        Iterator<Integer> iterator = deque.descendingIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(4, (int) iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, (int) iterator.next());

        iterator.remove();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, (int) iterator.next());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, (int) iterator.next());
        iterator.remove();

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void trimToSize() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10, 2f);
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addLast(4);

        Assert.assertArrayEquals(new Object[]{1,2,3,4}, deque.toArray());

        deque.addLast(5);
        Assert.assertEquals(8, deque.capacity());

        deque.trimToSize();
        Assert.assertEquals(5, deque.capacity());

        Assert.assertArrayEquals(new Object[]{1,2,3,4,5}, deque.toArray());
    }

    @Test
    public void capacity() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10, 2);
        Assert.assertEquals(0, deque.capacity());

        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addLast(4);
        Assert.assertEquals(4, deque.capacity());

        deque.addLast(5);
        Assert.assertEquals(8, deque.capacity());
    }

    @Test
    public void maximumCapacity() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(10);
        Assert.assertEquals(10, deque.maximumCapacity());
    }

    @Test
    public void offerFirstMany() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(12, 2f);
        deque.offerFirstMany(Arrays.asList(1,2,3,4,5,6));
        Assert.assertArrayEquals(new Object[]{1,2,3,4,5,6}, deque.toArray());

        deque.offerFirstMany(Arrays.asList(1,2,3,4,5,6));
        Assert.assertArrayEquals(new Object[]{1,2,3,4,5,6,1,2,3,4,5,6}, deque.toArray());
    }

    @Test
    public void offerLastMany() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(12, 2f);
        deque.offerLastMany(Arrays.asList(1,2,3,4,5,6));
        Assert.assertArrayEquals(new Object[]{1,2,3,4,5,6}, deque.toArray());

        deque.offerLastMany(Arrays.asList(6,5,4,3,2,1));
        Assert.assertArrayEquals(new Object[]{1,2,3,4,5,6,6,5,4,3,2,1}, deque.toArray());
    }
}