package frechsack.prod.util;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

class TupleFactory {

    private abstract static class AbstractTuple<First, Second> implements Tuple<First, Second> {

        @Override
        public String toString() {
            return "{first: " + first() + ", " + "second: " + second() + "}";
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj == null) return false;
            if(!(obj instanceof Tuple<?,?> bTuple))
                return false;

            if(!Objects.equals(first(),bTuple.first())) return false;
            return Objects.equals(second(), bTuple.second());
        }

        @Override
        public int hashCode() {
            return Objects.hash(first(), second());
        }
    }

    static class SimpleTuple<First, Second> extends AbstractTuple<First, Second> {

        private final First first;
        private final Second second;

        SimpleTuple(First first, Second second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public First first() {
            return first;
        }

        @Override
        public Second second() {
            return second;
        }
    }

    static class SupplierTuple<First, Second> extends AbstractTuple<First, Second>{

        private final Supplier<First> first;
        private final Supplier<Second> second;

        SupplierTuple(Supplier<First> first, Supplier<Second> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public First first() {
            return first == null ? null : first.get();
        }

        @Override
        public Second second() {
            return second == null ? null : second.get();
        }
    }

    static class EntryTuple<First, Second> extends AbstractTuple<First, Second>{

        private final Map.Entry<First, Second> entry;

        EntryTuple(Map.Entry<First, Second> entry) {
            this.entry = entry;
        }

        @Override
        public First first() {
            return entry.getKey();
        }

        @Override
        public Second second() {
            return entry.getValue();
        }
    }
}
