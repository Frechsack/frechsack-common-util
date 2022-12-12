package frechsack.prod.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TryTest {

    @Test
    public void operators(){
        Try<Integer> t = Try.of(12);
        Assert.assertEquals(12, (int) t.get());
        Assert.assertEquals(10, (int) t.flatMap(it -> Try.of(10)).get());
        Assert.assertTrue(t.isPresent());

        Try<Integer> f = Try.error(new RuntimeException());
        Assert.assertThrows(IllegalStateException.class, f::get);
        Assert.assertThrows(RuntimeException.class, f::call);
        Assert.assertEquals(12, (int) f.or(12));
        Assert.assertTrue(f.isError());
        Assert.assertEquals(RuntimeException.class, f.error().getClass());
        Assert.assertEquals(10, (int) f.orGet(() -> 10));
        Assert.assertEquals(IndexOutOfBoundsException.class, f.orTry(() -> {
            throw new IndexOutOfBoundsException();
        }).error().getClass());
    }
}
