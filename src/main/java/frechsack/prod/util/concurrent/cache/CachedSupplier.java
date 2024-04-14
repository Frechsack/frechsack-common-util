package frechsack.prod.util.concurrent.cache;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class CachedSupplier<Type> implements Supplier<Type> {

    private final Supplier<Type> supplier;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Type result;
    private long resultExpireAtMillis = -1;
    private final long cacheDurationMillis;

    public CachedSupplier(@NotNull Supplier<Type> supplier) {
        this(supplier, 1000);
    }

    public CachedSupplier(@NotNull Supplier<Type> supplier, @NotNull Duration duration) {
        this(supplier, duration.toMillis());
    }

    public CachedSupplier(@NotNull Supplier<Type> supplier, long millis) {
        this.supplier = Objects.requireNonNull(supplier);
        this.cacheDurationMillis = millis;
    }

    public boolean isExpired() {
        return resultExpireAtMillis == -1 || (resultExpireAtMillis - System.currentTimeMillis()) >= resultExpireAtMillis;
    }

    public void clear() {
        resultExpireAtMillis = -1;
        result = null;
    }

    public void clearIfExpired() {
        if (isExpired())
            clear();
    }

    @Override
    public Type get() {
        lock.readLock().lock();
        try {
            if (!isExpired())
                return result;
        }
        finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        try {
            if (!isExpired())
                return result;
            result = supplier.get();
            resultExpireAtMillis = System.nanoTime() + cacheDurationMillis;
            return result;
        }
        finally {
            lock.writeLock().unlock();
        }
    }
}
