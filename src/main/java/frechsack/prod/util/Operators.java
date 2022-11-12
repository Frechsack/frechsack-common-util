package frechsack.prod.util;

import java.util.Objects;
import java.util.function.*;

public class Operators {

    private Operators() {}

    @SafeVarargs
    public static <Type> Type coalesce(Type... values){
        return coalesce(Objects::nonNull, values);
    }

    @SafeVarargs
    public static <Type> Type coalesce(Predicate<Type> predicate, Type... values){
        for (Type value : values)
            if (predicate.test(value))
                return value;
        return null;
    }

    @SafeVarargs
    public static <Out> Out coalesce(Supplier<Out>... suppliers){
        Out value = null;
        for (Supplier<Out> supplier : suppliers)
            if(supplier != null)
                if((value = supplier.get()) != null)
                    break;
        return value;
    }

    public static int coalesce(int notToMatch, IntSupplier... suppliers){
        int value = notToMatch;
        for (IntSupplier supplier : suppliers)
            if(supplier != null)
                if((value = supplier.getAsInt()) != notToMatch)
                    break;
        return value;
    }

    public static int coalesce(IntSupplier... suppliers){
        return coalesce(0,suppliers);
    }

    public static double coalesce(double notToMatch, DoubleSupplier... suppliers){
        double value = notToMatch;
        for (DoubleSupplier supplier : suppliers)
            if(supplier != null)
                if((value = supplier.getAsDouble()) != notToMatch)
                    break;
        return value;
    }

    public static double coalesce(DoubleSupplier... suppliers){
        return coalesce(0,suppliers);
    }

    public static long coalesce(long notToMatch, LongSupplier... suppliers){
        long value = notToMatch;
        for (LongSupplier supplier : suppliers)
            if(supplier != null)
                if((value = supplier.getAsLong()) != notToMatch)
                    break;
        return value;
    }

    public static long coalesce(LongSupplier... suppliers){
        return coalesce(0,suppliers);
    }


}
