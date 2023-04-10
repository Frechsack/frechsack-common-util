package frechsack.dev.util.signal;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public interface WriteableSignal<Type> extends Signal<Type>, Consumer<Type> {

    boolean set(Type value);

    @Override
    default void accept(Type type){
        set(type);
    }

    interface Boolean extends WriteableSignal<java.lang.Boolean>, Signal.Boolean {

        boolean setBoolean(boolean value);

        default void accept(boolean value){
            setBoolean(value);
        }
    }

    interface Number<Type extends java.lang.Number> extends WriteableSignal<Type>, Signal.Number<Type>, DoubleConsumer, IntConsumer, LongConsumer {

        default boolean setInt(int value){
            return setLong(value);
        }

        boolean setDouble(double value);

        boolean setLong(long value);

        @Override
        default void accept(int value){
            setInt(value);
        }

        @Override
        default void accept(long value){
            setLong(value);
        }

        @Override
        default void accept(double value){
            setDouble(value);
        }
    }
}
