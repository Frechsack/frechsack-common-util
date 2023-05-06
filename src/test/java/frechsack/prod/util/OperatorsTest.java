package frechsack.prod.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class OperatorsTest {

    @Test
    public void retryRun() {
        AtomicInteger callCountA = new AtomicInteger();
        Runnable actionA = callCountA::incrementAndGet;
        Operators.retryRun(actionA, 2);

        Assert.assertEquals(1, callCountA.get());

        AtomicInteger callCountB = new AtomicInteger();
        Runnable actionB = new Runnable() {
            private boolean isFirstCall = true;
            @Override
            public void run() {
                if (isFirstCall)
                {
                    isFirstCall = false;
                    throw new IllegalArgumentException();
                }
                callCountB.incrementAndGet();
            }
        };
        Operators.retryRun(actionB, 1);
        Assert.assertEquals(1, callCountB.get());

        Runnable actionC = () -> { throw new IllegalArgumentException(); };
        Assert.assertThrows(IllegalArgumentException.class, () -> Operators.retryRun(actionC, 3));
    }

    @Test
    public void retryRunAsync() {
        AtomicInteger callCountA = new AtomicInteger();
        CompletableFuture<Void> actionA = Operators.retryRunAsync(callCountA::incrementAndGet, 2);

        actionA.join();
        Assert.assertEquals(1, callCountA.get());

        AtomicInteger callCountB = new AtomicInteger();
        CompletableFuture<Void> actionB = Operators.retryRunAsync(new Runnable() {
            private boolean isFirstCall = true;
            @Override
            public void run() {
                if (isFirstCall)
                {
                    isFirstCall = false;
                    throw new IllegalArgumentException();
                }
                callCountB.incrementAndGet();
            }
        }, 1);
        actionB.join();
        Assert.assertEquals(1, callCountB.get());

        CompletableFuture<Void> actionC = Operators.retryRunAsync(() -> { throw new IllegalArgumentException(); }, 3);
        Assert.assertThrows(CompletionException.class, actionC::join);

        long start = System.currentTimeMillis();
        CompletableFuture<Void> actionD = Operators.retryRunAsync(() -> { throw new IllegalArgumentException(); }, 3, 200);
        Assert.assertThrows(CompletionException.class, actionD::join);
        long end = System.currentTimeMillis();
        Assert.assertTrue(end - start >= 550);
    }

    @Test
    public void retryGet() {
        AtomicInteger callCountA = new AtomicInteger();
        Supplier<Integer> actionA = () -> { callCountA.incrementAndGet(); return 1;};

        Assert.assertEquals(1, (int) Operators.retryGet(actionA, 2));
        Assert.assertEquals(1, callCountA.get());

        AtomicInteger callCountB = new AtomicInteger();
        Supplier<Integer> actionB = new Supplier<>() {
            private boolean isFirstCall = true;
            @Override
            public Integer get() {
                if (isFirstCall)
                {
                    isFirstCall = false;
                    throw new IllegalArgumentException();
                }
                callCountB.incrementAndGet();
                return 1;
            }
        };
        Assert.assertEquals(1, (int) Operators.retryGet(actionB, 1));
        Assert.assertEquals(1, callCountB.get());

        Supplier<Integer> actionC = () -> { throw new IllegalArgumentException(); };
        Assert.assertThrows(IllegalArgumentException.class, () -> Operators.retryGet(actionC, 3));
    }

    @Test
    public void retryGetAsync() {
        AtomicInteger callCountA = new AtomicInteger();
        CompletableFuture<Integer> actionA = Operators.retryGetAsync(() -> { callCountA.incrementAndGet(); return 1;},2);

        Assert.assertEquals(1, (int) actionA.join());
        Assert.assertEquals(1, callCountA.get());

        AtomicInteger callCountB = new AtomicInteger();
        CompletableFuture<Integer> actionB = Operators.retryGetAsync(new Supplier<>() {
            private boolean isFirstCall = true;

            @Override
            public Integer get() {
                if (isFirstCall) {
                    isFirstCall = false;
                    throw new IllegalArgumentException();
                }
                callCountB.incrementAndGet();
                return 1;
            }
        },1);
        Assert.assertEquals(1, (int) actionB.join());
        Assert.assertEquals(1, callCountB.get());


        CompletableFuture<Integer> actionC = Operators.retryGetAsync(() -> { throw new IllegalArgumentException(); }, 3);
        Assert.assertThrows(CompletionException.class, actionC::join);

        long start = System.currentTimeMillis();
        CompletableFuture<Integer> actionD = Operators.retryGetAsync(() -> { throw new IllegalArgumentException(); }, 3, 200);
        Assert.assertThrows(CompletionException.class, actionD::join);
        long end = System.currentTimeMillis();
        Assert.assertTrue(end - start >= 550);
    }
}