package frechsack.dev.util;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;

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
        Undefined.Number<Integer> a = Undefined.ofInt(null).or(() -> 12);
        Assert.assertEquals(12,a.getInt());
        Assert.assertFalse(a.flatMapBoolean((it) -> Undefined.ofBoolean(false)).getBoolean());
        Assert.assertEquals(12, a.flatMap((it) -> Undefined.of(new Object())).flatMapNumber(it -> Undefined.ofNumber(12)).getDouble(),1);
        Assert.assertTrue(a.flatMap((it) -> Undefined.of()).flatMapNumber(it -> Undefined.ofNumber()).mapInt(it -> 12).isUndefined());
        Assert.assertTrue(a.transform(it -> Undefined.ofNumber(BigInteger.valueOf(100)).filter((Predicate<BigInteger>) b -> b.compareTo(BigInteger.valueOf(200)) > 0)).isUndefined());
    }

}
