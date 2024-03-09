package frechsack.prod.util.concurrent.execute;

import frechsack.prod.util.concurrent.BooleanLatch;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class OnceExecutedRunnable implements Runnable {
    private final @NotNull ReentrantLock lock = new ReentrantLock();
    private final @NotNull Runnable action;
    private boolean isExecuted = false;

    public OnceExecutedRunnable(@NotNull Runnable action) {
        this.action = Objects.requireNonNull(action);
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public boolean isExecuting() {
        return lock.isLocked();
    }

    public void reset() {
        try {
            lock.lock();
            isExecuted = false;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        if (isExecuted) return;
        try {
            lock.lock();
            if (!isExecuted) {
                action.run();
                isExecuted = true;
            }
        }
        finally {
            lock.unlock();
        }
    }
}
