package frechsack.dev.util.signal;

import org.jetbrains.annotations.Nullable;

public sealed class WriteableObjectSignal<Type> extends ObservableSignal<Type> implements WriteableSignal<Type> permits WriteableNumberSignal {

    protected @Nullable Type value;

    public WriteableObjectSignal(@Nullable Type initial) {
        this.value = initial;
    }

    public WriteableObjectSignal() {
    }

    @Override
    public synchronized boolean set(Type newValue) {
        Type lastValue = this.value;
        boolean isChanged = lastValue != newValue;
        this.value = newValue;
        if(isChanged){
            fireInvalidation();
            fireChange(newValue);
        }
        return isChanged;
    }

    @Override
    public Type get() {
        return value;
    }
}
