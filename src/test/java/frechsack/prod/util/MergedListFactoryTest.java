package frechsack.prod.util;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;

public class MergedListFactoryTest {

    @Test
    public void testStructure(){
        List<Integer> sourceA = new ArrayList<>(List.of(1,2,3));
        List<Integer> sourceB = new ArrayList<>(List.of(4,5,6));
        List<Integer> sourceC = new ArrayList<>(List.of(7,8,9));
        List<Integer> sourceD = new ArrayList<>(List.of(10,11,12));

        List<Integer> destination = CollectionUtils.merged(sourceA,sourceB,sourceC, sourceD);
        MatcherAssert.assertThat(destination, instanceOf(RandomAccess.class));

        Assert.assertArrayEquals(destination.toArray(),new Object[] { 1,2, 3,4,5,6,7,8,9,10,11,12 });
        Assert.assertArrayEquals(destination.toArray(new Integer[12]),new Integer[] { 1,2, 3,4,5,6,7,8,9,10,11,12 });

        sourceA = new LinkedList<>(List.of(1,2));
        sourceB = new LinkedList<>(List.of(3,4));
        destination = CollectionUtils.merged(sourceA,sourceB);

        MatcherAssert.assertThat(destination, not(instanceOf(RandomAccess.class)));
    }

    @Test
    public void testModification(){
        List<Integer> sourceA = new ArrayList<>(List.of(1,2,3));
        List<Integer> sourceB = new ArrayList<>(List.of(4,5,6));
        List<Integer> sourceC = new ArrayList<>(List.of(7,8,9));
        List<Integer> destination = CollectionUtils.merged(sourceA,sourceB,sourceC);

        destination.add(10);
        Assert.assertArrayEquals(sourceC.toArray(),new Object[] { 7,8,9,10 });

        destination.remove(2);
        Assert.assertArrayEquals(sourceA.toArray(),new Object[] { 1,2});

        Assert.assertEquals(Integer.valueOf(5), destination.get(3));
    }

    @Test
    public void testIterator(){

        List<Integer> sourceA = new ArrayList<>(List.of(1,2,3));
        List<Integer> sourceB = new ArrayList<>(List.of(4,5,6));
        List<Integer> sourceC = new ArrayList<>(List.of(7,8,9));

        List<Integer> destination = CollectionUtils.merged(sourceA,sourceB,sourceC);

        Iterator<Integer> iterator = destination.iterator();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < destination.size(); i++) {
            Assert.assertEquals(iterator.next(),destination.get(i));
        }

        ListIterator<Integer> listIterator = destination.listIterator();
        Assert.assertEquals(Integer.valueOf(1),listIterator.next());
        Assert.assertEquals(Integer.valueOf(2),listIterator.next());
        Assert.assertEquals(Integer.valueOf(3),listIterator.next());
        Assert.assertEquals(Integer.valueOf(4),listIterator.next());
        Assert.assertEquals(Integer.valueOf(5),listIterator.next());
        Assert.assertEquals(Integer.valueOf(6),listIterator.next());
        Assert.assertEquals(Integer.valueOf(7),listIterator.next());
        Assert.assertEquals(Integer.valueOf(8),listIterator.next());
        Assert.assertEquals(Integer.valueOf(9),listIterator.next());
        Assert.assertEquals(Integer.valueOf(9),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(8),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(7),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(6),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(5),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(4),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(3),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(2),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(1),listIterator.previous());
        Assert.assertEquals(Integer.valueOf(1),listIterator.next());
    }

}
