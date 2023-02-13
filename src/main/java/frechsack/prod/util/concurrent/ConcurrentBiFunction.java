package frechsack.prod.util.concurrent;

import frechsack.prod.util.concurrent.execute.SingleExecutedSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

public class ConcurrentBiFunction<U, R, T> implements BiFunction<U, R, T> {

    private final BiFunction<U, R, T> function;

    private final Map<Arguments<U, R>, SingleExecutedSupplier<T>> values = new WeakHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public ConcurrentBiFunction(@NotNull BiFunction<U, R, T> function) {
        this.function = function;
    }

    @Override
    public T apply(U u, R r) {
        lock.lock();
        final Arguments<U, R> arguments = new Arguments<>(u, r);
        SingleExecutedSupplier<T> supplier = values.get(arguments);
        if(supplier == null || supplier.isFinished()) {
            supplier = new SingleExecutedSupplier<>(() -> function.apply(u, r));
            values.put(arguments, supplier);
        }
        lock.unlock();
        return supplier.get();
    }

    private record Arguments<U, R>(U u, R r) {}

}
