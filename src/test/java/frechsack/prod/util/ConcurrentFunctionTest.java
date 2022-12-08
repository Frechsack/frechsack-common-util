package frechsack.prod.util;

import frechsack.prod.util.concurrent.ConcurrentFunction;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentFunctionTest {

    @Test
    public void concurrentException() throws ExecutionException, InterruptedException {
        final int THREAD_COUNT = 60;
        final AtomicInteger callCount = new AtomicInteger(0);
        final ConcurrentFunction<String, Integer> function = new ConcurrentFunction<>(in -> {
            callCount.incrementAndGet();
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("TestException");
        });

        final ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
        final List<CompletableFuture<?>> tests = new ArrayList<>(THREAD_COUNT);
        final int INPUT_MODULAR = 20;
        for(int i = 0; i < THREAD_COUNT; i++){
            final String input = Integer.toString(i % INPUT_MODULAR);
            tests.add(
                    CompletableFuture.runAsync(
                            () -> Assert.assertThrows(ExecutionException.class, () -> CompletableFuture.runAsync(() -> function.apply(input)).get(1000L, TimeUnit.MILLISECONDS)), exec
                    ));
        }

        CompletableFuture.allOf(tests.toArray(CompletableFuture[]::new)).get();
        exec.shutdown();
        Assert.assertTrue(callCount.get() < THREAD_COUNT);
    }

    @Test
    public void concurrentDifferentArgs() throws ExecutionException, InterruptedException {
        final int THREAD_COUNT = 60;
        final AtomicInteger callCount = new AtomicInteger(0);
        final ConcurrentFunction<String, Integer> function = new ConcurrentFunction<>(in -> {
            callCount.incrementAndGet();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Integer.parseInt(in);
        });

        final List<CompletableFuture<?>> tests = new ArrayList<>(THREAD_COUNT);
        final ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
        final int INPUT_MODULAR = 20;
        for(int i = 0; i < THREAD_COUNT; i++){
            final String input = Integer.toString(i % INPUT_MODULAR);
            final int expected = i % INPUT_MODULAR;
            tests.add(
                    CompletableFuture.runAsync(
                            () -> Assert.assertEquals(expected,(int) function.apply(input)),exec
                    ));
        }

        CompletableFuture.allOf(tests.toArray(CompletableFuture[]::new)).get();
        exec.shutdown();
        Assert.assertTrue(callCount.get() <= THREAD_COUNT);

    }
    @Test
    public void concurrentSameArgs() throws ExecutionException, InterruptedException {
        final int THREAD_COUNT = 20;
        final AtomicInteger callCount = new AtomicInteger(0);
        final ConcurrentFunction<String, Integer> function = new ConcurrentFunction<>(in -> {
            callCount.incrementAndGet();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Integer.parseInt(in);
        });

        final List<CompletableFuture<?>> tests = new ArrayList<>(THREAD_COUNT);
        final ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
        for(int i = 0; i < THREAD_COUNT; i++){
            final String input = Integer.toString(i);
            final int expected = i;
            tests.add(
                    CompletableFuture.runAsync(
                            () -> Assert.assertEquals(expected,(int) function.apply(input)), exec
                    ));
        }

        CompletableFuture.allOf(tests.toArray(CompletableFuture[]::new)).get();
        exec.shutdown();
        Assert.assertEquals(THREAD_COUNT, callCount.get());

    }
}
