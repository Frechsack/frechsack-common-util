package frechsack.prod.util.concurrent.execute;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A {@link Runnable} that is executed exactly one time. This Object is threadsafe. Calling {@link #run()} from different threads is safe.
 */
public class SingleExecutedRunnable implements Runnable {

    private static final short STATE_STARTED = 1;

    private static final short STATE_FINISHED = 2;

    private final Runnable action;

    private short state;

    private RuntimeException error;

    /**
     * Create a new instance.
     * @param action The Runnable that should be executed.
     */
    public SingleExecutedRunnable(@NotNull Runnable action) {
        this.action = Objects.requireNonNull(action);
    }

    public boolean isStarted(){
        return state == STATE_STARTED || state == STATE_FINISHED;
    }

    public boolean isRunning(){
        return isStarted() && !isFinished();
    }

    public boolean isFinished(){
        return state == STATE_FINISHED;
    }

    @Override
    public synchronized void run() {
        if (!isStarted()){
            state = STATE_STARTED;
            try {
                action.run();
            }
            catch (RuntimeException error){
                this.error = error;
            }
            state = STATE_FINISHED;
        }
        if(error != null)
            throw error;
    }
}
