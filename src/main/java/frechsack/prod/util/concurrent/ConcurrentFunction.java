package frechsack.prod.util.concurrent;

import frechsack.prod.util.concurrent.execute.SingleExecutedSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class ConcurrentFunction<In, Out> implements Function<In, Out> {

    private final Function<In, Out> function;

    private final Map<In, SingleExecutedSupplier<Out>> values = new WeakHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public ConcurrentFunction(@NotNull Function<In, Out> function) {
        this.function = function;
    }

    @Override
    public Out apply(final In in) {
        lock.lock();
        SingleExecutedSupplier<Out> supplier = values.get(in);
        if(supplier == null || supplier.isFinished()) {
            supplier = new SingleExecutedSupplier<>(() -> function.apply(in));
            values.put(in, supplier);
        }
        lock.unlock();
        return supplier.get();
    }
}
