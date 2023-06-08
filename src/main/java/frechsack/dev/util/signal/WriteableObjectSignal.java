package frechsack.dev.util.signal;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.Executor;

public sealed class WriteableObjectSignal<Type> extends ObservableSignal<Type> implements WriteableSignal<Type> permits WriteableNumberSignal {

    protected @Nullable Type value;

    public WriteableObjectSignal(@Nullable Type initial, @Nullable Executor executor) {
        super(executor);
        this.value = initial;
    }

    @Override
    public synchronized boolean set(Type newValue) {
        Type lastValue = this.value;
        boolean isChanged = !Objects.equals(lastValue, newValue);
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
