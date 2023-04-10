package frechsack.prod.util.concurrent.flow;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Flow;
import java.util.function.BooleanSupplier;

public class AsyncOnDemandSubscriber<Type> implements Flow.Subscriber<Type> {

    private final @NotNull Flow.Subscriber<Type> syncImplementation;
    private final @NotNull BooleanSupplier isHandleAsync;

    private @Nullable AsyncSubscriber<Type> asyncImplementation;

    public AsyncOnDemandSubscriber(@NotNull Flow.Subscriber<Type> implementation, @NotNull BooleanSupplier isHandleAsync) {
        this.syncImplementation = implementation;
        this.isHandleAsync = isHandleAsync;
    }

    private synchronized AsyncSubscriber<Type> requireAsyncImplementation(){
        if (asyncImplementation == null)
            asyncImplementation = new AsyncSubscriber<>(syncImplementation);
        return asyncImplementation;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        if (isHandleAsync.getAsBoolean())
            requireAsyncImplementation().onSubscribe(subscription);
        else
            syncImplementation.onSubscribe(subscription);
    }

    @Override
    public void onNext(Type item) {
        if (isHandleAsync.getAsBoolean())
            requireAsyncImplementation().onNext(item);
        else
            syncImplementation.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        if (isHandleAsync.getAsBoolean())
            requireAsyncImplementation().onError(throwable);
        else
            syncImplementation.onError(throwable);
    }

    @Override
    public void onComplete() {
        if (isHandleAsync.getAsBoolean())
            requireAsyncImplementation().onComplete();
        else
            syncImplementation.onComplete();
    }
}
