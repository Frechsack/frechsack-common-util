package frechsack.dev.util.signal;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

public final class WriteableDoubleSignal extends ObservableSignal<Double> implements WriteableSignal.Number<Double> {

    private double value;

    public WriteableDoubleSignal(double initial, @Nullable Executor executor) {
        super(executor);
        this.value = initial;
    }

    @Override
    public boolean set(Double value) {
        return setDouble(value == null ? 0 : value);
    }

    @Override
    public synchronized boolean setDouble(double newValue) {
        double lastValue = this.value;
        boolean isChanged = lastValue != newValue;
        this.value = newValue;
        if(isChanged){
            fireInvalidation();
            fireChange(newValue);
        }
        return isChanged;
    }

    @Override
    public boolean setLong(long value) {
        return setDouble(value);
    }

    @Override
    public boolean setInt(int value) {
        return setDouble(value);
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public int getAsInt() {
        return (int) value;
    }

    @Override
    public long getAsLong() {
        return (long) value;
    }

    @Override
    public Double get() {
        return value;
    }
}
