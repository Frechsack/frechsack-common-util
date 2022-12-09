package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@link Runnable} that is executed exactly one time. This Object is threadsafe. Calling {@link #run()} from different threads is safe.
 */
public class SingleExecutedRunnable implements Runnable {

    private final Runnable action;

    private final AtomicBoolean isExecuted = new AtomicBoolean(false);

    /**
     * Create a new instance.
     * @param action The Runnable that should be executed.
     */
    public SingleExecutedRunnable(@NotNull Runnable action) {
        this.action = Objects.requireNonNull(action);
    }

    @Override
    public void run() {
        if(!isExecuted.getAndSet(true))
            action.run();
    }
}
