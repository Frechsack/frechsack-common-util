package frechsack.prod.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TupleTest {

    @Test
    public void constructor(){
        Tuple.of(1, "HelloWorld");
        Tuple.of(() -> 1, () -> "HelloWorld");
        Tuple.of(new Map.Entry<>() {
            @Override
            public Object getKey() {
                return null;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object setValue(Object value) {
                return null;
            }
        });
        Assert.assertThrows(NullPointerException.class, () -> Tuple.of(null));

    }

    @Test
    public void first(){
        Assert.assertEquals(1, (int) Tuple.of(1, null).first());
        Assert.assertEquals(1, (int) Tuple.of(() -> 1, null).first());
        Assert.assertEquals(1, Tuple.of(new Map.Entry<>() {
            @Override
            public Object getKey() {
                return 1;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object setValue(Object value) {
                return null;
            }
        }).first());
    }

    @Test
    public void second(){
        Assert.assertEquals(1, (int) Tuple.of(null, 1).second());
        Assert.assertEquals(1, (int) Tuple.of(() -> null, () -> 1).second());
        Assert.assertEquals(1, Tuple.of(new Map.Entry<>() {
            @Override
            public Object getKey() {
                return 1;
            }

            @Override
            public Object getValue() {
                return 1;
            }

            @Override
            public Object setValue(Object value) {
                return null;
            }
        }).second());
    }

    @Test
    public void equals(){
        Assert.assertEquals(Tuple.of(1,2),Tuple.of(1,2));
        Assert.assertEquals(Tuple.of(1,2), Tuple.of(() -> 1, () -> 2));
        Assert.assertEquals(Tuple.of(1,2), Tuple.of(new Map.Entry<>() {
            @Override
            public Object getKey() {
                return 1;
            }

            @Override
            public Object getValue() {
                return 2;
            }

            @Override
            public Object setValue(Object value) {
                return null;
            }
        }));
    }
}
