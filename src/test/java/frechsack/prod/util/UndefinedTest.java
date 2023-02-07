package frechsack.prod.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class UndefinedTest {


    @Test
    public void constructor(){
        Assert.assertTrue(Undefined.of().isUndefined());
        Assert.assertFalse(Undefined.of().isNull());
        Assert.assertTrue(Undefined.of().isEmpty());
        Assert.assertFalse(Undefined.of().isPresent());
        Assert.assertThrows(NoSuchElementException.class, () -> Undefined.of().get());

        Assert.assertFalse(Undefined.of(null).isUndefined());
        Assert.assertTrue(Undefined.of(null).isNull());
        Assert.assertTrue(Undefined.of(null).isEmpty());
        Assert.assertFalse(Undefined.of(null).isPresent());

        Assert.assertEquals(1,(int)Undefined.of(1).get());
        Assert.assertFalse(Undefined.of(1).isUndefined());
        Assert.assertFalse(Undefined.of(1).isNull());
        Assert.assertFalse(Undefined.of(1).isEmpty());
        Assert.assertTrue(Undefined.of(1).isPresent());
    }

}
