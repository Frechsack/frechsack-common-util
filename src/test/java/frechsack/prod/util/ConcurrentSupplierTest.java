package frechsack.prod.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ConcurrentSupplierTest {

    @Test
    public void concurrent() throws ExecutionException, InterruptedException {
        final int THREAD_COUNT = 60;

        final AtomicInteger callCount = new AtomicInteger(0);
        final Supplier<Integer> supplier = new ConcurrentSupplier<>(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 10;
        });

        final List<CompletableFuture<?>> tests = new ArrayList<>(THREAD_COUNT);
        final ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
        for(int i = 0; i < THREAD_COUNT; i++){
            tests.add(
                    CompletableFuture.runAsync(
                            () -> Assert.assertEquals(10,(int) supplier.get()),exec
                    ));
        }
        CompletableFuture.allOf(tests.toArray(CompletableFuture[]::new)).get();
        exec.shutdown();
        Assert.assertTrue(callCount.get() < THREAD_COUNT);

    }

}
