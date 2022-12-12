package frechsack.prod.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.function.*;

public class SupplierUtils {

    private SupplierUtils(){}

    public static <Out> Supplier<Out> buffer(Supplier<Out> supplier){
        return Objects.requireNonNull(supplier) instanceof Buffer
                ? supplier
                : new Buffer<>(supplier);
    }

    public static BooleanSupplier toBoolean(Predicate<?> predicate){
        Objects.requireNonNull(predicate);
        return () -> predicate.test(null);
    }

    public static Predicate<?> toNoArgPredicate(BooleanSupplier supplier){
        Objects.requireNonNull(supplier);
        return (v) -> supplier.getAsBoolean();
    }

    public static IntSupplier toInt(Supplier<? extends Number> supplier){
        Objects.requireNonNull(supplier);
        return () -> {
            Number n = supplier.get();
            return n == null ? 0 : n.intValue();
        };
    }

    public static LongSupplier toLong(Supplier<? extends Number> supplier){
        Objects.requireNonNull(supplier);
        return () -> {
            Number n = supplier.get();
            return n == null ? 0 : n.longValue();
        };
    }

    public static DoubleSupplier toDouble(Supplier<? extends Number> supplier){
        Objects.requireNonNull(supplier);
        return () -> {
            Number n = supplier.get();
            return n == null ? 0 : n.doubleValue();
        };
    }

    private static class Buffer<Type> implements Supplier<Type> {
        private record Holder<Type>(Type value){}
        private Reference<Holder<Type>> reference;
        private final Supplier<Type> generator;

        private Buffer(Supplier<Type> generator) {
            this.generator = generator;
        }

        @Override
        public Type get() {
            Holder<Type> holder = this.reference == null
                    ? null
                    : this.reference.get();

            if(holder == null)
                synchronized (this){
                    holder = this.reference == null
                            ? null
                            : this.reference.get();
                    if(holder == null){
                        holder = new Holder<>(generator.get());
                        reference = new SoftReference<>(holder);
                    }
                }

            return holder.value;
        }
    }
}
