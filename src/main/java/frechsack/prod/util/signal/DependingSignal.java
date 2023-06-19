package frechsack.prod.util.signal;

import frechsack.prod.util.concurrent.flow.AutoUnsubscribeSubscriber;
import frechsack.prod.util.concurrent.flow.CompactSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;

abstract sealed class DependingSignal<Type> extends ObservableSignal<Type> implements Signal<Type> permits DependingBooleanSignal, DependingDoubleSignal, DependingIntSignal, DependingLongSignal, DependingObjectSignal {

    boolean isValid = false;

    private final WeakReference<Flow.Subscription>[] parentSubscriptionReferences;

    public DependingSignal(@NotNull @UnmodifiableView Collection<Signal<?>> parents, @Nullable Executor executor){
        super(executor);
        //noinspection unchecked
        this.parentSubscriptionReferences = new WeakReference[parents.size()];
        final var parentInvalidationSubscriber = new AutoUnsubscribeSubscriber<>(new WeakReference<>(this), new CompactSubscriber<>(Long.MAX_VALUE) {
            @Override
            public void onNext(Object item) {
                onParentInvalidated();
            }

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                super.onSubscribe(subscription);
                synchronized (parentSubscriptionReferences) {
                    for (int i = 0; i < parentSubscriptionReferences.length; i++) {
                        if (parentSubscriptionReferences[i] == null || parentSubscriptionReferences[i].refersTo(null)) {
                            parentSubscriptionReferences[i] = new WeakReference<>(subscription);
                            break;
                        }
                    }
                }
            }
        });
        parents.forEach(it -> it.subscribeOnInvalidate(parentInvalidationSubscriber));

    }

    @Override
    public synchronized void close() {
        for (Reference<Flow.Subscription> parentSubscriptionReference : parentSubscriptionReferences) {
            if (parentSubscriptionReference != null) {
                Flow.Subscription subscription = parentSubscriptionReference.get();
                if (subscription != null)
                    subscription.cancel();
            }
        }
    }

    private synchronized void onParentInvalidated(){
        boolean isFireInvalidationListener = isValid;
        this.isValid = false;
        if (isFireInvalidationListener)
            fireInvalidation();
        if(isEagerRequired())
            revalidate();
    }

    protected abstract void revalidate();
}
