package frechsack.prod.util;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public interface Tuple<First, Second> {

    static <First, Second> Tuple<First, Second> of(First first, Second second){
        return new TupleFactory.SimpleTuple<>(first, second);
    }

    static <First, Second> Tuple<First, Second> of(Supplier<First> first, Supplier<Second> second){
        return new TupleFactory.SupplierTuple<>(first, second);
    }

    static <First, Second> Tuple<First, Second> of(Map.Entry<First, Second> entry){
        return new TupleFactory.EntryTuple<>(Objects.requireNonNull(entry));
    }

    First first();

    Second second();
}
