package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class ConcurrentSupplier<Output> implements Supplier<Output> {

    private final Supplier<Output> supplier;

    private Reference<Optional<Output>> valueReference;

    private RuntimeException actionError;
    private CountDownLatch sync;

    public ConcurrentSupplier(@NotNull Supplier<Output> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    @SuppressWarnings("OptionalAssignedToNull")
    private Output valueFromReference(){
        Reference<Optional<Output>> valueReference = this.valueReference;
        Optional<Output> output = valueReference == null || valueReference.refersTo(null) ? null : valueReference.get();
        if(output == null)
            return startAction();
        return output.orElse(null);
    }

    private synchronized Output startAction() {
        if(isActionRunning()) {
            awaitActionFinished();
        }
        sync = new CountDownLatch(1);
        try {
            Output value = supplier.get();
            valueReference = new SoftReference<>(Optional.ofNullable(value));
            actionError = null;
            sync.countDown();
            sync = null;
            return value;
        }
        catch (RuntimeException e){
            if(valueReference != null)
                valueReference.clear();
            valueReference = null;
            actionError = e;
            sync.countDown();
            sync = null;
            throw e;
        }
    }

    private Output awaitActionFinished(){
        CountDownLatch sync = this.sync;
        if(sync != null)
            try {
                sync.await();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        if(actionError != null)
            throw actionError;

        return valueFromReference();
    }

    private boolean isActionRunning(){
        CountDownLatch sync = this.sync;
        return sync != null && sync.getCount() > 0;
    }

    @Override
    public Output get() {
        if(isActionRunning())
            return awaitActionFinished();
        else
            return startAction();
    }
}
