package frechsack.prod.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtilsTest {

    @Test
    public void removeDuplicates(){

        List<Integer> list = new ArrayList<>(List.of(1,2,3,3,4,4,5,6,7,8,8));
        CollectionUtils.removeDuplicates(list);
        Assert.assertArrayEquals(list.toArray(), List.of(1,2,3,4,5,6,7,8).toArray());

        list = new ArrayList<>(List.of(1,2,1,2,3,4,5,4,6,7,10));
        CollectionUtils.removeDuplicates(list);
        Assert.assertArrayEquals(list.toArray(), List.of(1,2,3,4,5,6,7,10).toArray());
    }
}
