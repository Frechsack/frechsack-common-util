package frechsack.dev.util.signal;

import frechsack.prod.util.concurrent.flow.AsyncOnDemandSubscriber;
import frechsack.prod.util.concurrent.flow.AutoUnsubscribeSubscriber;
import frechsack.prod.util.concurrent.flow.CompactSubscriber;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.stream.Stream;

abstract sealed class DependingSignal<Type> extends ObservableSignal<Type> implements Signal<Type> permits DependingBooleanSignal, DependingDoubleSignal, DependingIntSignal, DependingLongSignal, DependingObjectSignal {

    boolean isValid = false;

    public DependingSignal(@NotNull Stream<Signal<?>> parents){
        parents.forEach(it -> it.subscribeOnInvalidate(
                new AutoUnsubscribeSubscriber<>(new WeakReference<>(this),
                        new CompactSubscriber<>(Long.MAX_VALUE) {
                            @Override
                            public void onNext(Signal<?> item) {
                                onParentInvalidated();
                            }
                        }
                )));
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
