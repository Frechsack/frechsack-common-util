package frechsack.prod.util.concurrent.flow;

import java.util.concurrent.Flow;

public class CompactSubscriber<Type> implements Flow.Subscriber<Type> {

    protected Flow.Subscription subscription;

    private final long initialRequest;

    public CompactSubscriber(long initialRequest) {
        this.initialRequest = initialRequest;
    }

    public CompactSubscriber() {
        this(0);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(initialRequest);
    }

    @Override
    public void onNext(Type item) {
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onComplete() {
    }
}
