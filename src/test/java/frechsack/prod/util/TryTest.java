package frechsack.prod.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

public class TryTest {

    @Test
    public void stream(){
        Try<Integer> t = Try.of(12);
        Try<Integer> f = Try.error(new RuntimeException());
        Assert.assertEquals(1, t.stream().count());
        Assert.assertEquals(0, t.errorStream().count());
        Assert.assertEquals(0, f.stream().count());
        Assert.assertEquals(1, f.errorStream().count());

        Function<Integer, Integer> fun = integer -> {
            if(integer % 3 == 0) throw new IllegalArgumentException();
            return integer;
        };

        Assert.assertEquals(13,IntStream.range(0, 20)
                .boxed()
                .map(Try::of)
                .map(it -> it.map(fun))
                .filter(Try::isPresent)
                .count());
    }

    @Test
    public void operators(){
        Try<Integer> t = Try.of(12);
        Assert.assertEquals(12, (int) t.value());
        Assert.assertEquals(10, (int) t.flatMap(it -> Try.of(10)).value());
        Assert.assertTrue(t.isPresent());

        Try<Integer> f = Try.error(new RuntimeException());
        Assert.assertThrows(IllegalStateException.class, f::value);
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
