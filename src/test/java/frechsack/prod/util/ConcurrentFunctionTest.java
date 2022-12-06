package frechsack.prod.util;

import frechsack.prod.util.concurrent.ConcurrentFunction;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConcurrentFunctionTest {


    @Test
    public void concurrent(){
        final AtomicInteger operationsCounter = new AtomicInteger(0);
        final Function<String, Integer> toInt = s -> {
            operationsCounter.addAndGet(1);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Integer.parseInt(s);
        };

        final ConcurrentFunction<String, Integer> toIntConcurrent = new ConcurrentFunction<>(toInt);

        Object[] result = Stream.concat(
                IntStream.range(0, 19).boxed(),
                IntStream.range(10, 19).boxed()
            ).parallel().map(Object::toString).map(toIntConcurrent).distinct().sorted().toArray();

        Assert.assertArrayEquals(
                IntStream.range(0,19).boxed().toArray(),
                result
        );

        Assert.assertEquals(operationsCounter.get(), 19);
    }
}
