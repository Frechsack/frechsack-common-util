package frechsack.dev.util.function;

import java.util.function.Function;

public class FunctionTryFactory
{
    private FunctionTryFactory(){}

    static class SimpleFunctionTry<E,V> implements FunctionTry<E,V>
    {
        private final Function<E,V> function;

        SimpleFunctionTry(Function<E, V> function) {this.function = function;}

        @Override
        public V apply(E e)
        {
            return function.apply(e);
        }

        @Override
        public V tryApply(E e)
        {
            return function.apply(e);
        }
    }
}
