package frechsack.dev.util.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface FunctionTry<E,V> extends Function<E,V>
{
    @Override
    default V apply(E e){
        try
        {
            return tryApply(e);
        }catch (Exception ex){
            return null;
        }
    }

    V tryApply(E e) throws Exception;

    default <T> FunctionTry<T, V> tryCompose(Function<? super T, ? extends E> before) {
        Objects.requireNonNull(before);
        return (T t) -> tryApply(before.apply(t));
    }

    default <T> FunctionTry<E, T> tryAndThen(Function<? super V, ? extends T> after) {
        Objects.requireNonNull(after);
        return (E e) -> after.apply(tryApply(e));
    }

    static  <E,V> FunctionTry<E,V> of(Function<E,V> function){
        return function == null ? null : new FunctionTryFactory.SimpleFunctionTry<>(function);
    }
}
