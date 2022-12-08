package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class ConcurrentRunnable implements Runnable {

    private final Runnable action;
    private RuntimeException actionError;
    private CountDownLatch sync;

    public ConcurrentRunnable(@NotNull  Runnable action) {
        this.action = Objects.requireNonNull(action);
    }

    private synchronized void startAction() {
        if(isActionRunning()) {
            awaitActionFinished();
        }
        sync = new CountDownLatch(1);
        try {
            action.run();
            actionError = null;
            sync.countDown();
            sync = null;
        }
        catch (RuntimeException e){
            actionError = e;
            sync.countDown();
            sync = null;
            throw e;
        }
    }

    private void awaitActionFinished(){
        CountDownLatch sync = this.sync;
        if(sync != null)
            try {
                sync.await();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        if(actionError != null)
            throw actionError;
    }

    private boolean isActionRunning(){
        CountDownLatch sync = this.sync;
        return sync != null && sync.getCount() > 0;
    }

    @Override
    public void run() {
        if(isActionRunning())
            awaitActionFinished();
        else
            startAction();
    }
}
