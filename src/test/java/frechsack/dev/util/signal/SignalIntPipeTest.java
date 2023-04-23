package frechsack.dev.util.signal;

import org.junit.Assert;
import org.junit.Test;

public class SignalIntPipeTest {

    @Test
    public void filter() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(5);
        Signal.Number<Integer> dep = root.pipeInt()
                .peek(System.out::println)
                .filter(it -> it == 10)
                .build();
        root.setInt(10);
        Thread.sleep(5);
        Assert.assertEquals(10, dep.getAsInt());

        root.setInt(15);
        Thread.sleep(5);
        Assert.assertEquals(10, dep.getAsInt());

    }

    @Test
    public void reduce() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Integer> dep = root.pipeInt()
                .reduce(Integer::sum)
                .build();

        root.setInt(5);
        Thread.sleep(15);
        Assert.assertEquals(5, dep.getAsInt());

        root.setInt(10);
        Thread.sleep(15);
        Assert.assertEquals(15, dep.getAsInt());
    }

    @Test
    public void range() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Integer> dep = root.pipeInt()
                .range(0,10)
                .build();
        root.setInt(5);
        Thread.sleep(15);
        Assert.assertEquals(5, dep.getAsInt());

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(5, dep.getAsInt());

        root.setInt(1);
        Thread.sleep(15);
        Assert.assertEquals(1, dep.getAsInt());

        root.setInt(-1);
        Thread.sleep(15);
        Assert.assertEquals(1, dep.getAsInt());

    }

    @Test
    public void map() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Integer> dep = root.pipeInt()
                .map(it -> it * 2)
                .build();

        root.setInt(1);
        Thread.sleep(15);
        Assert.assertEquals(2, dep.getAsInt());

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(22, dep.getAsInt());
    }

    @Test
    public void mapToObject() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal<String> dep = root.pipeInt()
                .mapToObject(Integer::toString)
                .build();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals("11", dep.get());
    }

    @Test
    public void mapToNumber() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Double> dep = root.pipeInt()
                .mapToNumber(it -> (double)(it * 2))
                .build();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(22d, dep.getAsDouble(), 0.1);
    }

    @Test
    public void mapToDouble() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Double> dep = root.pipeInt()
                .mapToDouble()
                .build();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(11d, dep.getAsDouble(), 0.1);
    }

    @Test
    public void mapToInt() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Integer> dep = root.pipeInt()
                .mapToInt()
                .build();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(11, dep.getAsInt());
    }

    @Test
    public void mapToLong() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Long> dep = root.pipeInt()
                .mapToLong()
                .build();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(11, dep.getAsInt());
    }

    @Test
    public void boxed() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Integer> dep = root.pipeInt()
                .boxed()
                .build();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(11, dep.getAsInt());
    }

    @Test
    public void build() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Integer> dep = root.pipeInt()
                .boxed()
                .build();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(11, dep.getAsInt());
    }

    @Test
    public void buildDouble() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Double> dep = root.pipeInt()
                .boxed()
                .buildDouble();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(11, dep.getAsInt());
    }

    @Test
    public void buildLong() throws InterruptedException {
        WriteableSignal.Number<Integer> root = Signal.ofInt(0);
        Signal.Number<Long> dep = root.pipeInt()
                .boxed()
                .buildLong();

        root.setInt(11);
        Thread.sleep(15);
        Assert.assertEquals(11, dep.getAsInt());
    }
}