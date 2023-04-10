package frechsack.dev.util.signal;

public final class WriteableLongSignal extends ObservableSignal<Long> implements WriteableSignal.Number<Long> {

    private long value;

    public WriteableLongSignal(long value) {
        this.value = value;
    }

    public WriteableLongSignal() {
    }

    @Override
    public boolean set(Long value) {
        return setLong(value == null ? 0 : value);
    }

    @Override
    public synchronized boolean setLong(long newValue) {
        long lastValue = this.value;
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
        return setLong((long) value);
    }

    @Override
    public boolean setInt(int value) {
        return setLong(value);
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
        return value;
    }

    @Override
    public Long get() {
        return value;
    }
}
