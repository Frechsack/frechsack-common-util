package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

/**
 * A threadsafe implementation of {@link Supplier} that will block multiple parallel calls to {@link #get()}.
 * If multiple threads call {@link #get()} in parallel, only one thread will execute the underlying {@link Supplier}.
 * Possible other threads will wait until the execution by the first thread is done.
 * They will take the result of the first thread.
 * If the {@link Supplier} throws an Exception during the execution, the other threads will throw the same Exception too.
 * @param <Output>
 */
public class ConcurrentSupplier<Output> implements Supplier<Output> {

    /**
     * The underlying implementation of Supplier.
     */
    private final Supplier<Output> supplier;

    /**
     * A reference to the result of the supplier.
     */
    private Reference<Optional<Output>> valueReference;

    /**
     * An exception that may be thrown during the execution of the underlying Supplier.
     */
    private RuntimeException actionError;

    /**
     * A clock used to synchronize the access at the underlying Supplier.
     */
    private CountDownLatch sync;

    public ConcurrentSupplier(@NotNull Supplier<Output> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    /**
     * Accesses the value stored in {@link #valueReference}.
     * If the reference is garbage-collected, the value will be recomputed.
     * @return Returns the computed value.
     */
    @SuppressWarnings("OptionalAssignedToNull")
    private Output valueFromReference(){
        Reference<Optional<Output>> valueReference = this.valueReference;
        Optional<Output> output = valueReference == null || valueReference.refersTo(null) ? null : valueReference.get();
        if(output == null)
            return startAction();
        return output.orElse(null);
    }

    /**
     * Starts the computation of the value. This function is threadsafe and can be called anytime.
     * This function will throw an Exception, if the underlying Supplier´s computations thrown an Exception.
     * @return Returns the computed value returned by the underlying Supplier.
     */
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

    /**
     * Awaits the computation of the underlying Supplier. If no computation is going on, the computation will be started.
     * This function will throw an Exception, if the underlying Supplier´s computations thrown an Exception.
     * @return Returns the computed value.
     */
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

    /**
     * Checks if the computation is currently performed.
     * @return Returns true if the computation is running, otherwise false.
     */
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
