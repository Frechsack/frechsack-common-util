package frechsack.dev.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class UndefinedTest {

    @Test
    public void nil() {
        Assert.assertTrue(Undefined.nil().isNull());
        Assert.assertTrue(Undefined.nil().isEmpty());
        Assert.assertFalse(Undefined.nil().isUndefined());
        Assert.assertFalse(Undefined.nil().isPresent());
    }

    @Test
    public void undefined() {
        Assert.assertTrue(Undefined.undefined().isUndefined());
        Assert.assertTrue(Undefined.undefined().isEmpty());
        Assert.assertFalse(Undefined.undefined().isNull());
        Assert.assertFalse(Undefined.undefined().isPresent());
    }

    @Test
    public void empty() {
        Assert.assertTrue(Undefined.empty().isEmpty());
        Assert.assertFalse(Undefined.empty().isPresent());
    }

    @Test
    public void of() {
        Assert.assertTrue(Undefined.of().isEmpty());
        Assert.assertTrue(Undefined.of().isUndefined());
        Assert.assertFalse(Undefined.of().isNull());
        Assert.assertFalse(Undefined.of().isPresent());
    }

    @Test
    public void testOf() {
        Assert.assertTrue(Undefined.of(1).isPresent());
        Assert.assertFalse(Undefined.of(1).isEmpty());
        Assert.assertFalse(Undefined.of(1).isUndefined());
        Assert.assertFalse(Undefined.of(1).isNull());
        Assert.assertThrows(NullPointerException.class,() -> Undefined.of(null));
    }

    @Test
    public void ofNullable() {
        Assert.assertTrue(Undefined.ofNullable(1).isPresent());
        Assert.assertFalse(Undefined.ofNullable(1).isEmpty());
        Assert.assertFalse(Undefined.ofNullable(1).isUndefined());
        Assert.assertFalse(Undefined.ofNullable(1).isNull());

        Assert.assertFalse(Undefined.ofNullable(null).isPresent());
        Assert.assertTrue(Undefined.ofNullable(null).isEmpty());
        Assert.assertFalse(Undefined.ofNullable(null).isUndefined());
        Assert.assertTrue(Undefined.ofNullable(null).isNull());
    }

    @Test
    public void orElse() {
        Assert.assertEquals(1, Undefined.ofNullable(null).orElse(1));
        Assert.assertEquals(1, (int) Undefined.ofNullable(1).get());
        Assert.assertEquals(1, (int) Undefined.of().orElse(1));
    }

    @Test
    public void testOrElse() {
        Assert.assertEquals(1, Undefined.ofNullable(null).orElse(1, 2));
        Assert.assertEquals(1, (int) Undefined.ofNullable(1).orElse(1,2));
        Assert.assertEquals(1, (int) Undefined.of().orElse(2,1));
    }

    @Test
    public void orElseThrow() {
        Assert.assertEquals(1, (int) Undefined.ofNullable(1).orElseThrow());
        Assert.assertThrows(NoSuchElementException.class, () -> Undefined.undefined().orElseThrow());
        Assert.assertThrows(NoSuchElementException.class, () -> Undefined.nil().orElseThrow());
        Assert.assertThrows(NoSuchElementException.class, () -> Undefined.empty().orElseThrow());
    }

    @Test
    public void testOrElseThrow() {
    }

    @Test
    public void orElseGet() {
    }

    @Test
    public void testOrElseGet() {
    }

    @Test
    public void get() {
    }

    @Test
    public void getIncludeNull() {
    }

    @Test
    public void getIncludeUndefined() {
    }

    @Test
    public void getIncludeAny() {
    }

    @Test
    public void ifPresent() {
    }

    @Test
    public void ifEmpty() {
    }

    @Test
    public void ifNull() {
    }

    @Test
    public void ifUndefined() {
    }

    @Test
    public void ifPresentOrElse() {
    }

    @Test
    public void act() {
    }

    @Test
    public void filter() {
    }

    @Test
    public void map() {
    }

    @Test
    public void mapUndefined() {
    }

    @Test
    public void mapNull() {
    }

    @Test
    public void mapEmpty() {
    }

    @Test
    public void transform() {
    }

    @Test
    public void toOptional() {
    }

    @Test
    public void flatMap() {
    }

    @Test
    public void flatMapUndefined() {
    }

    @Test
    public void flatMapNull() {
    }

    @Test
    public void flatMapEmpty() {
    }

    @Test
    public void or() {
    }

    @Test
    public void stream() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testHashCode() {
    }
}