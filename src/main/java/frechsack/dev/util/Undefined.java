package frechsack.dev.util;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface Undefined <Type> {

    interface Number<Type extends java.lang.Number> extends Undefined<Type> {

        int getAsInt();

        long getAsLong();

        double getAsDouble();

        default IntStream intStream(){
            return isPresent()
                    ? IntStream.of(getAsInt())
                    : IntStream.empty();
        }

        default IntStream orElse(int value) {
            return isPresent()
                    ? IntStream.of(getAsInt())
                    : IntStream.of(value);
        }

        default DoubleStream doubleStream(){
            return isPresent()
                    ? DoubleStream.of(getAsDouble())
                    : DoubleStream.empty();
        }

        default DoubleStream orElse(double value) {
            return isPresent()
                    ? DoubleStream.of(getAsDouble())
                    : DoubleStream.of(value);
        }

        default LongStream longStream(){
            return isPresent()
                    ? LongStream.of(getAsLong())
                    : LongStream.empty();
        }

        default LongStream orElse(long value) {
            return isPresent()
                    ? LongStream.of(getAsLong())
                    : LongStream.of(value);
        }
    }

    interface Boolean extends Undefined<java.lang.Boolean> {

        boolean getAsBoolean();

        default Stream<java.lang.Boolean> orElse(boolean value){
            return isPresent()
                    ? Stream.of(get())
                    : Stream.of(value);
        }
    }

    boolean isUndefined();

    boolean isNull();

    Type get();

    default boolean isPresent(){
        return !isNull() && !isUndefined();
    }

    default Stream<Type> stream(){
        return isPresent()
                ? Stream.of(get())
                : Stream.empty();
    }

    default Stream<Type> orElse(Type value) {
        return isPresent()
                ? Stream.of(get())
                : Stream.of(value);
    }

}
