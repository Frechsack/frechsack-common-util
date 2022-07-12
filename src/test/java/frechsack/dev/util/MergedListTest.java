package frechsack.dev.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MergedListTest {

    @Test
    public void testStructure(){
        List<Integer> sourceA = new ArrayList<>(List.of(1,2,3));
        List<Integer> sourceB = new ArrayList<>(List.of(4,5,6));
        List<Integer> sourceC = new ArrayList<>(List.of(7,8,9));
        List<Integer> sourceD = new ArrayList<>(List.of(10,11,12));

        List<Integer> destination = MergedList.of(sourceA,sourceB,sourceC, sourceD);

        Assert.assertArrayEquals(destination.toArray(),new Object[] { 1,2, 3,4,5,6,7,8,9,10,11,12 });
        Assert.assertArrayEquals(destination.toArray(new Integer[12]),new Integer[] { 1,2, 3,4,5,6,7,8,9,10,11,12 });
    }

    @Test
    public void testIterator(){
        List<Integer> sourceA = new ArrayList<>(List.of(1,2,3));
        List<Integer> sourceB = new ArrayList<>(List.of(4,5,6));
        List<Integer> sourceC = new ArrayList<>(List.of(7,8,9));
        List<Integer> destination = MergedList.of(sourceA,sourceB,sourceC);

        Iterator<Integer> iterator = destination.iterator();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < destination.size(); i++) {
            Assert.assertEquals(iterator.next(),destination.get(i));
        }

        ListIterator<Integer> listIterator = destination.listIterator();
        listIterator.next();
        listIterator.next();
        Assert.assertEquals(2,listIterator.nextIndex());
        listIterator.next();
        Assert.assertEquals(3,listIterator.nextIndex());
        listIterator.next();
        Assert.assertEquals(4,listIterator.nextIndex());
        listIterator.next();
        listIterator.next();
        listIterator.next();
        listIterator.next();
        Assert.assertEquals(Integer.valueOf(9),listIterator.next());
        //Assert.assertThrows(IndexOutOfBoundsException.class, listIterator::next);
        listIterator.previous();
        listIterator.previous();
        listIterator.previous();
        Assert.assertEquals(Integer.valueOf(5),listIterator.previous());
        listIterator.previous();
        listIterator.previous();
    }

}
