package frechsack.prod.util;

import java.util.Objects;
import java.util.function.*;

public final class SupplierUtils {

    private SupplierUtils(){}

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
}
