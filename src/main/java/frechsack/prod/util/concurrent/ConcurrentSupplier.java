package frechsack.prod.util.concurrent;

import frechsack.prod.util.concurrent.execute.SingleExecutedSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class ConcurrentSupplier<Out> implements Supplier<Out> {

    private final Supplier<Out> supplier;

    private SingleExecutedSupplier<Out> task;

    private final ReentrantLock lock = new ReentrantLock();

    public ConcurrentSupplier(@NotNull Supplier<Out> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Out get() {
        lock.lock();
        SingleExecutedSupplier<Out> task = this.task;
        if(task == null || task.isFinished())
            task = (this.task = new SingleExecutedSupplier<>(supplier));
        lock.unlock();
        return task.get();
    }
}
