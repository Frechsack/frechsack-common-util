package frechsack.prod.util.concurrent;

import frechsack.prod.util.concurrent.execute.SingleExecutedRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentRunnable implements Runnable {

    private final Runnable action;

    private volatile SingleExecutedRunnable task;

    private final ReentrantLock lock = new ReentrantLock();

    public ConcurrentRunnable(@NotNull Runnable action) {
        this.action = Objects.requireNonNull(action);
    }

    @Override
    public void run() {
        lock.lock();
        SingleExecutedRunnable task = this.task;
        if(task == null || task.isFinished())
            task = (this.task = new SingleExecutedRunnable(action));
        lock.unlock();
        task.run();
    }
}
