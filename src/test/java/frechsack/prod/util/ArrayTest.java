package frechsack.prod.util;

import frechsack.prod.util.array.Array;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ArrayTest {

    @Test
    public void test(){
        Array.Number<Integer> ints = Array.ofInt(new int[]{1,2,3,4,5});
        Assert.assertTrue(ints.isPrimitive());
        Assert.assertEquals(5,ints.length());
        Assert.assertEquals(15,ints.streamInt().sum());
        Assert.assertEquals(3d,ints.getDouble(2),0.01);

        Array.Number<BigDecimal> bigDecimals = Array.ofNumber(BigDecimal.ONE, BigDecimal.TEN);
        Assert.assertEquals(2,bigDecimals.length());
        Assert.assertEquals(10d,bigDecimals.replace(1,2),0.1);
        Assert.assertFalse(bigDecimals.isPrimitive());

        Array.Number<BigInteger> bigIntegers = Array.ofNumber(BigInteger.ONE, BigInteger.ZERO);
        Assert.assertArrayEquals(new BigInteger[]{BigInteger.ONE, BigInteger.ZERO},bigIntegers.toArrayBoxed());
    }
}
