package frechsack.prod.util;

import frechsack.prod.util.concurrent.ConcurrentRunnable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ConcurrentRunnableTest {

    @Test
    public void concurrent() throws InterruptedException {
        AtomicInteger callCount = new AtomicInteger(0);
        Runnable count = () -> {
            callCount.incrementAndGet();
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        final Runnable concurrentRunnable = new ConcurrentRunnable(count);
        IntStream.range(0,20).boxed().map(it -> new Thread(concurrentRunnable)).forEach(Thread::start);
        Thread.sleep(2000L);
        Assert.assertTrue(callCount.get() < 20);
    }

    @Test
    public void concurrentException() throws ExecutionException, InterruptedException {
        final int THREAD_COUNT = 20;
        AtomicInteger callCount = new AtomicInteger(0);
        Runnable action = new ConcurrentRunnable(() -> {
            callCount.incrementAndGet();
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("TestError");
        });

        final List<CompletableFuture<?>> tests = new ArrayList<>(THREAD_COUNT);
        for(int i = 0; i < THREAD_COUNT; i++){
            tests.add(
                CompletableFuture.runAsync(
                        () -> Assert.assertThrows(ExecutionException.class, () -> CompletableFuture.runAsync(action).get(1000L, TimeUnit.MILLISECONDS))
                ));
        }

        CompletableFuture.allOf(tests.toArray(CompletableFuture[]::new)).get();
        Assert.assertTrue(callCount.get() < THREAD_COUNT);
    }
}
