package frechsack.prod.util;

import frechsack.prod.util.collection.ComputedList;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ComputedListTest {

    @Test
    public void test(){
        List<Integer> list = new ComputedList<>(i -> i);
        Assert.assertEquals(0L, (long) list.get(0));
    }
}
