package frechsack.prod.util.concurrent.execute;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SingleExecutedSupplier<Out> implements Supplier<Out> {

    private static final short STATE_STARTED = 1;

    private static final short STATE_FINISHED = 2;

    private Out value;

    private RuntimeException error;

    private short state;

    private final Supplier<Out> supplier;

    public SingleExecutedSupplier(@NotNull Supplier<Out> supplier) {
        this.supplier = supplier;
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
    public synchronized Out get() {
        if (!isStarted()) {
            state = STATE_STARTED;
            try {
                value = supplier.get();
            }
            catch (RuntimeException error){
                this.error = error;
            }
            state = STATE_FINISHED;
        }
        if(error != null)
            throw error;
        return value;
    }
}
