package frechsack.dev.util.signal;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

public final class WriteableIntSignal extends ObservableSignal<Integer> implements WriteableSignal.Number<Integer> {

    private int value;

    public WriteableIntSignal(int value, @Nullable Executor executor) {
        super(executor);
        this.value = value;
    }
    @Override
    public boolean set(Integer value) {
        return setInt(value == null ? 0 : value);
    }

    @Override
    public synchronized boolean setInt(int newValue) {
        int lastValue = this.value;
        boolean isChanged = lastValue != newValue;
        this.value = newValue;
        if(isChanged){
            fireInvalidation();
            fireChange(newValue);
        }
        return isChanged;
    }

    @Override
    public boolean setDouble(double value) {
        return setInt((int) value);
    }

    @Override
    public boolean setLong(long value) {
        return setInt((int) value);
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public long getAsLong() {
        return value;
    }

    @Override
    public Integer get() {
        return value;
    }
}
