package frechsack.prod.util.concurrent.execute;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.function.Supplier;

public class CachedExecutedSupplier<Out> implements Supplier<Out> {

    private Reference<Holder<Out>> reference;

    private final Supplier<Out> supplier;

    public CachedExecutedSupplier(@NotNull Supplier<Out> supplier) {
        this.supplier = supplier;
    }

    protected Reference<Holder<Out>> referenceOf(Holder<Out> holder){
        return new SoftReference<>(holder);
    }

    @Override
    public synchronized Out get() {
        final Reference<Holder<Out>> reference = this.reference;
        Holder<Out> holder = reference == null ? null : reference.get();

        if(holder == null){
            try {
                holder = new Holder<>(supplier.get(), null);
            }
            catch (RuntimeException error){
                holder = new Holder<>(null, error);
            }
            this.reference = referenceOf(holder);
        }

        if(holder.error != null)
            throw holder.error;
        return holder.value;
    }

    protected record Holder<Type> (Type value, RuntimeException error){}
}
