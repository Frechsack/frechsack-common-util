package com.frechsack.dev.util;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Package-private implementations of {@link Pair}.
 * @author Frechsack
 */
class PairFactory
{
    private PairFactory(){}

    static class PairEntry<E, V> implements Pair<E, V>
    {
        private final Map.Entry<E, V> entry;

        PairEntry(Map.Entry<E, V> entry)
        {
            this.entry = entry;
        }

        @Override
        public E first()
        {
            return entry.getKey();
        }

        @Override
        public V second()
        {
            return entry.getValue();
        }

        @Override
        public String toString()
        {
            return "PairEntry{" + "first=" + first() + ", second=" + second() + " entry=" + entry + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> that = (Pair<?, ?>) o;
            return Objects.equals(first(), that.first()) && Objects.equals(second(), that.second());
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(entry);
        }
    }

    static class SupplierPair<E, V> implements Pair<E, V>
    {

        private final Supplier<E> eSupplier;
        private final Supplier<V> vSupplier;

        SupplierPair(Supplier<E> eSupplier, Supplier<V> vSupplier)
        {
            this.eSupplier = eSupplier;
            this.vSupplier = vSupplier;
        }

        @Override
        public E first()
        {
            return eSupplier.get();
        }

        @Override
        public V second()
        {
            return vSupplier.get();
        }

        @Override
        public String toString()
        {
            return "SupplierPair{" + "eSupplier=" + eSupplier + ", vSupplier=" + vSupplier + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> that = (Pair<?, ?>) o;
            return Objects.equals(first(), that.first()) && Objects.equals(second(), that.second());
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(eSupplier, vSupplier);
        }
    }

    static class SimplePair<E, V> implements Pair<E, V>
    {
        private final E first;
        private final V second;

        SimplePair(E first, V second)
        {
            this.first = first;
            this.second = second;
        }

        @Override
        public E first()
        {
            return first;
        }

        @Override
        public V second()
        {
            return second;
        }

        @Override
        public String toString()
        {
            return "SimplePair{" + "first=" + first + ", second=" + second + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> that = (Pair<?, ?>) o;
            return Objects.equals(first, that.first()) && Objects.equals(second, that.second());
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(first, second);
        }
    }
}
