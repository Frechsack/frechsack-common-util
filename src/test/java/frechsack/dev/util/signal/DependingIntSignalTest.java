package frechsack.dev.util.signal;

import frechsack.prod.util.concurrent.flow.CompactSubscriber;
import org.junit.Assert;
import org.junit.Test;

public class DependingIntSignalTest {

    @Test
    public void changeListener() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(10);
        Signal.Number<Integer> dep = root.pipeInt().build();
        WriteableSignal.Number<Integer> listenerCall = Signal.ofInt(0);

        dep.subscribeOnChange(new CompactSubscriber<>(Long.MAX_VALUE) {
            @Override
            public void onNext(Integer newValue) {
                listenerCall.setInt(listenerCall.getAsInt() + 1);
                Assert.assertTrue(newValue == 5 || newValue == 15 || newValue == 1);
            }
        });

        root.setInt(5);
        Thread.sleep(5);
        root.setInt(15);
        Thread.sleep(5);
        root.setInt(15);
        Thread.sleep(5);
        root.setInt(1);
        Thread.sleep(5);
        Assert.assertEquals(3, listenerCall.getAsInt());
    }

    @Test
    public void invalidationListener() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(10);
        Signal.Number<Integer> dep = root.pipeInt().build();
        WriteableSignal.Number<Integer> listenerCall = Signal.ofInt(0);

        dep.subscribeOnInvalidate(new CompactSubscriber<>(Long.MAX_VALUE) {
            @Override
            public void onNext(Signal<?> item) {
                listenerCall.setLong(listenerCall.getAsLong() + 1);
            }
        });
        // Validieren
        dep.get();
        // Invalid
        root.setInt(5);
        Thread.sleep(5);
        root.setInt(15);
        Thread.sleep(5);
        Assert.assertEquals(1, listenerCall.getAsInt());
    }

    @Test
    public void getAsDouble() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(10);
        Signal.Number<Integer> dep = root.pipeInt().build();
        Assert.assertEquals(dep.getAsDouble(), root.getAsDouble(), 0.1);

        root.setDouble(15);
        Thread.sleep(5);
        Assert.assertEquals(dep.getAsDouble(), root.getAsDouble(), 0.1);
    }

    @Test
    public void getAsInt() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(10);
        Signal.Number<Integer> dep = root.pipeInt().build();
        Assert.assertEquals(dep.getAsInt(), root.getAsInt(), 1);

        root.setDouble(15);
        Thread.sleep(1000);
        Assert.assertEquals(15, dep.getAsInt());
    }

    @Test
    public void getAsLong() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(10);
        Signal.Number<Integer> dep = root.pipeInt().build();
        Assert.assertEquals(dep.getAsLong(), root.getAsLong());

        root.setDouble(15);
        Thread.sleep(200);
        Assert.assertEquals(15, dep.getAsLong());
    }

    @Test
    public void get() {
        Signal.Number<Integer> root = Signal.ofInt(10);
        Signal.Number<Integer> dep = root.pipeInt().build();
        Assert.assertEquals(dep.get(), root.get());
    }
}