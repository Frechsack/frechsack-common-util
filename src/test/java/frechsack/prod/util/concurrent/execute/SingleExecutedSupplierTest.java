package frechsack.prod.util.concurrent.execute;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class SingleExecutedSupplierTest {

    @Test
    public void states() throws InterruptedException {
        Supplier<Integer> action = () -> {
            try {
                Thread.sleep(100);
            }
            catch (Exception ignored){
            }
            return 0;
        };
        SingleExecutedSupplier<Integer> supplier = new SingleExecutedSupplier<>(action);
        new Thread(supplier::get).start();
        Thread.sleep(20);
        Assert.assertTrue(supplier.isStarted());
        Assert.assertTrue(supplier.isRunning());
        Assert.assertFalse(supplier.isFinished());
        Thread.sleep(100);
        Assert.assertTrue(supplier.isStarted());
        Assert.assertFalse(supplier.isRunning());
        Assert.assertTrue(supplier.isFinished());
    }

    @Test
    public void getCallCount() {
        final AtomicInteger counter = new AtomicInteger();
        Supplier<Integer> action = counter::incrementAndGet;
        SingleExecutedSupplier<Integer> runnable = new SingleExecutedSupplier<>(action);

        Assert.assertEquals(0, counter.get());

        IntStream.range(0,1000).parallel().forEach(i-> runnable.get());

        Assert.assertEquals(1, counter.get());
    }
}