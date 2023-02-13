package frechsack.prod.util.concurrent;

import frechsack.prod.util.concurrent.execute.SingleExecutedRunnable;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ConcurrentRunnableTest {

    @Test
    public void runCallCount() throws Exception {
        final AtomicInteger counter = new AtomicInteger();
        final Runnable action = () -> {
            try {
                Thread.sleep(10);
                counter.incrementAndGet();
            }
            catch (Exception ignored){}
        };
        final SingleExecutedRunnable runnable = new SingleExecutedRunnable(action);
        IntStream.range(0,2).parallel().forEach(i -> runnable.run());
        Assert.assertEquals(1, counter.get());

        Thread.sleep(15);
        action.run();
        Assert.assertEquals(2, counter.get());

    }

}