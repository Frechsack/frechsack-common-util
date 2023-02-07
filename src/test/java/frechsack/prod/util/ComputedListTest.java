package frechsack.prod.util;

import frechsack.prod.util.collection.ComputedList;
import org.junit.Assert;
import org.junit.Test;

public class ComputedListTest {

    @Test
    public void test(){
        Assert.assertEquals(0L, (long) new ComputedList<>(i -> i).get(0));
    }
}
