package frechsack.dev.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.DoublePredicate;

public class UndefinedTest {

    @Test
    public void test(){


        Assert.assertTrue(Undefined.ofBoolean(null).isNull());
        Assert.assertFalse(Undefined.ofBoolean().isNull());
        Assert.assertTrue(Undefined.ofBoolean().isUndefined());
        Assert.assertFalse(Undefined.ofBoolean(true).isUndefined());
        Assert.assertFalse(Undefined.ofBoolean(false).getBoolean());

        Assert.assertTrue(Undefined.ofInt(null).isNull());
        Assert.assertFalse(Undefined.ofInt().isNull());
        Assert.assertTrue(Undefined.ofInt().isUndefined());
        Assert.assertFalse(Undefined.ofInt(12).isUndefined());
        Assert.assertEquals(12,Undefined.ofInt(12).getInt());

        Assert.assertTrue(Undefined.ofDouble(null).isNull());
        Assert.assertFalse(Undefined.ofDouble().isNull());
        Assert.assertTrue(Undefined.ofDouble().isUndefined());
        Assert.assertFalse(Undefined.ofDouble(12).isUndefined());
        Assert.assertEquals(12,Undefined.ofDouble(12).getInt());

        Assert.assertTrue(Undefined.ofLong(null).isNull());
        Assert.assertFalse(Undefined.ofLong().isNull());
        Assert.assertTrue(Undefined.ofLong().isUndefined());
        Assert.assertFalse(Undefined.ofLong(12).isUndefined());
        Assert.assertEquals(12,Undefined.ofLong(12).getInt());
    }

    @Test
    public void testModify(){


        Undefined.Number<Integer> in = Undefined.ofInt(null).or(() -> 12);
        Undefined.Number<Long> d = in.mapDouble(Integer::doubleValue).filter((DoublePredicate) value -> value == 10).mapLong(Double::longValue);

        System.out.println(d.getClass());

        System.out.println(d.isUndefined());
       // System.out.println(d.get());

    }

}
