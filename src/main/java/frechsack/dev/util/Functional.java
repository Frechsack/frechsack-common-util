package frechsack.dev.util;

import frechsack.dev.util.function.ConsumerTry;
import frechsack.dev.util.function.FunctionTry;
import frechsack.dev.util.function.RunnableTry;
import frechsack.dev.util.function.SupplierTry;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides static functions for functional programming.
 *
 * @author frechsack
 */
public class Functional
{
    private Functional() {}

    /**
     * Performs an operation that may throw an Exception. The Exception will be ignored.
     *
     * @param toCall The action that will be called.
     * @return Returns true if the operation completed, when an Exception is thrown false is returned.
     */
    public static boolean tryRun(RunnableTry toCall)
    {
       return tryRun(toCall,null);
    }

    /**
     * Performs an operation. In case an Exception is thrown an optional error handler will be called.
     *
     * @param toCall  The action that will be called.
     * @param onError An optional operation that is called in case an Exception is thrown.
     * @return Returns true if the operation finished successfully.
     */
    public static boolean tryRun(RunnableTry toCall, Consumer<Exception> onError)
    {
        try
        {
            Objects.requireNonNull(toCall).tryRun();
            return true;
        }
        catch (Exception e)
        {
            if (onError != null) onError.accept(e);
        }
        return false;
    }

    /**
     * Performs an operation and returns it´s value. In case an Exception is thrown an optional error handler will be called. The Exception handler will return the value then.
     *
     * @param toCall  The action that will be called.
     * @param onError An optional operation that is called in case an Exception is thrown.
     * @return Returns a value. If the handler is not specified in case of an Exception, null is returned.
     */
    public static <E> E tryGet(SupplierTry<E> toCall, Function<Exception, E> onError)
    {
        try
        {
            return Objects.requireNonNull(toCall).tryGet();
        }
        catch (Exception e)
        {
            return onError == null ? null : onError.apply(e);
        }
    }

    /**
     * Performs an operation on a resource that will be automatically closed.
     *
     * @param resource The resource that should be used.
     * @param onAction The action will be called when the resource was successfully obtained.
     * @param onError  An optional operation that is called in case an Exception is thrown.
     * @param <E>      The resource´s class-type.
     */
    public static <E extends AutoCloseable> boolean tryRunWith(SupplierTry<E> resource, ConsumerTry<E> onAction, Consumer<Exception> onError)
    {
        try
        {
            try (E res = resource.tryGet())
            {
                Objects.requireNonNull(onAction).accept(res);
                return true;
            }
            catch (Exception e)
            {
                if (onError != null) onError.accept(e);
            }
        }
        catch (Exception ignored)
        {
        }
        return false;
    }

    /**
     * Performs an operation and returns it´s value on a resource that will be automatically closed . In case an Exception is thrown an optional error handler will be called. The Exception handler will return the value then.
     *
     * @param resource The resource that should be used.
     * @param onAction The action will be called when the resource was successfully obtained.
     * @param onError  An optional operation that is called in case an Exception is thrown.
     * @param <E>      The resource´s class-type.
     * @return Returns a value. If the handler is not specified in case of an Exception, null is returned.
     */
    public static <E extends AutoCloseable, V> V tryGetWith(SupplierTry<E> resource, FunctionTry<E, V> onAction, Function<Exception, V> onError)
    {
        try (E res = resource.tryGet())
        {
            return Objects.requireNonNull(onAction).apply(res);
        }
        catch (Exception e)
        {
            return onError == null ? null : onError.apply(e);
        }
    }

}
