package frechsack.prod.util.concurrent.execute;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class OnceExecutedSupplier<ReturnType> implements Supplier<ReturnType> {

    private final @NotNull ReentrantLock lock = new ReentrantLock();
    private final @NotNull Supplier<ReturnType> supplier;

    private ReturnType value;
    private boolean isExecuted = false;

    public OnceExecutedSupplier(@NotNull Supplier<ReturnType> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
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
    public ReturnType get() {
        if (isExecuted) return value;
        try {
            lock.lock();
            if (isExecuted) return value;
            value = supplier.get();
            isExecuted = true;
            return value;
        }
        finally {
            lock.unlock();
        }
    }
}
