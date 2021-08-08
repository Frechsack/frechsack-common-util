package frechsack.dev.util.function;

import java.util.function.Supplier;

class SupplierTryFactory
{
    private SupplierTryFactory(){}

    static class SimpleSupplierTry<E> implements SupplierTry<E>{

        private final Supplier<E> supplier;

        SimpleSupplierTry(Supplier<E> supplier) {this.supplier = supplier;}

        @Override
        public E get()
        {
            return supplier.get();
        }

        @Override
        public E tryGet()
        {
            return supplier.get();
        }
    }

}
