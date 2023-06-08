package frechsack.dev.util.signal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

public final class WriteableBooleanSignal extends ObservableSignal<Boolean> implements WriteableSignal.Boolean {

    private boolean value;

    public WriteableBooleanSignal(boolean initial, @Nullable Executor executor) {
        super(executor);
        this.value = initial;
    }

    public WriteableBooleanSignal(@Nullable Executor executor) {
        super(executor);
    }

    @Override
    public boolean set(java.lang.Boolean value) {
        return setBoolean(value != null && value);
    }

    @Override
    public synchronized boolean setBoolean(boolean newValue) {
        boolean lastValue = this.value;
        boolean isChanged = lastValue != newValue;
        this.value = newValue;
        if(isChanged){
            fireInvalidation();
            fireChange(newValue);
        }
        return isChanged;
    }

    @Override
    public java.lang.Boolean get() {
        return value;
    }
}
