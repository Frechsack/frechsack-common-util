package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SingleRunnable implements Runnable {

    private final Runnable action;

    private final AtomicBoolean isExecuted = new AtomicBoolean(false);

    public SingleRunnable(@NotNull Runnable action) {
        this.action = Objects.requireNonNull(action);
    }

    @Override
    public void run() {
        if(!isExecuted.getAndSet(true))
            action.run();
    }
}
