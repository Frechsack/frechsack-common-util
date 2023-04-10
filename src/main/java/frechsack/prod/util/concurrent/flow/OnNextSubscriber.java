package frechsack.prod.util.concurrent.flow;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Flow;

public abstract class OnNextSubscriber<Type> implements Flow.Subscriber<Type> {

    private final long initialRequest;

    private Flow.Subscription subscription;

    public OnNextSubscriber(long initialRequest) {
        this.initialRequest = initialRequest;
    }

    @Override
    public final void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(initialRequest);
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onComplete() {
        subscription = null;
    }

    public Flow.Subscription getSubscription() {
        return subscription;
    }
}
