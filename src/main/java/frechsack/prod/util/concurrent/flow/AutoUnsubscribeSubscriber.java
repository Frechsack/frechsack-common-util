package frechsack.prod.util.concurrent.flow;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.util.concurrent.Flow;

public final class AutoUnsubscribeSubscriber<Type> implements Flow.Subscriber<Type> {

    private final @NotNull Reference<?> referent;
    private final @NotNull Flow.Subscriber<Type> subscriber;
    private Flow.Subscription subscription;

    public AutoUnsubscribeSubscriber(@NotNull Reference<?> referent, @NotNull Flow.Subscriber<Type> subscriber) {
        this.referent = referent;
        this.subscriber = subscriber;
    }

    private synchronized void cancel(){
        if (subscription == null) return;
        subscription.cancel();
        subscription = null;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        if (this.referent.refersTo(null))
            cancel();
        else
            subscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(Type item) {
        if (this.referent.refersTo(null))
            cancel();
        else
            subscriber.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        if (!referent.refersTo(null))
            subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        if (!referent.refersTo(null))
            subscriber.onComplete();
    }
}
