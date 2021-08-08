package frechsack.dev.util.function;

import java.util.function.Supplier;

@FunctionalInterface
public interface SupplierTry<E> extends Supplier<E>
{
    @Override
    default E get()
    {
        try
        {
            return tryGet();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    E tryGet() throws Exception;


    static <E> SupplierTry<E> of(Supplier<E> supplier){
         return supplier == null ? null :   new SupplierTryFactory.SimpleSupplierTry<>(supplier);
    }
}
