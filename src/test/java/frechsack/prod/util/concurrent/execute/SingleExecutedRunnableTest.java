package frechsack.prod.util.concurrent.execute;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class SingleExecutedRunnableTest {

    @Test
    public void states() throws InterruptedException {
        Runnable action = () -> {
            try {
                Thread.sleep(100);
            }
            catch (Exception ignored){
            }
        };
        SingleExecutedRunnable runnable = new SingleExecutedRunnable(action);
        new Thread(runnable).start();
        Thread.sleep(20);
        Assert.assertTrue(runnable.isStarted());
        Assert.assertTrue(runnable.isRunning());
        Assert.assertFalse(runnable.isFinished());
        Thread.sleep(100);
        Assert.assertTrue(runnable.isStarted());
        Assert.assertFalse(runnable.isRunning());
        Assert.assertTrue(runnable.isFinished());
    }

    @Test
    public void runCallCount() {
        final AtomicInteger counter = new AtomicInteger();
        Runnable action = counter::incrementAndGet;
        SingleExecutedRunnable runnable = new SingleExecutedRunnable(action);

        Assert.assertEquals(0, counter.get());

        IntStream.range(0,1000).parallel().forEach(i-> runnable.run());

        Assert.assertEquals(1, counter.get());
    }
}