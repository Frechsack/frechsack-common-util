package frechsack.prod.util.concurrent;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class ConcurrentFunctionTest {

    @Test
    public void applyTestResults() {
        final AtomicInteger counter = new AtomicInteger();
        final ConcurrentFunction<Integer, String> function = new ConcurrentFunction<>(in -> {
            counter.incrementAndGet();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
            return in.toString();
        });

        final int COUNT = 40;
        final int MOD = 2;

        List<String> actualElements = IntStream.range(0,COUNT).parallel().map(it -> it % MOD).mapToObj(function::apply).toList();
        List<String> expectedElements = IntStream.range(0,COUNT).map(it -> it % MOD).mapToObj(Integer::toString).toList();
        Assert.assertEquals(expectedElements, actualElements);
    }

    @Test
    public void applyTestCallCount() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger();
        final ConcurrentFunction<Integer, String> function = new ConcurrentFunction<>(in -> {
            counter.incrementAndGet();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
            return in.toString();
        });

        final int COUNT = 40;
        final int MOD = 2;

        ExecutorService exec = Executors.newFixedThreadPool(COUNT);
        exec.invokeAll(IntStream.range(0,COUNT).parallel().map(it -> it % MOD).mapToObj(it -> (Callable<String>) () -> function.apply(it)).toList());
        exec.shutdown();
        Assert.assertEquals(MOD, counter.get());
    }
}