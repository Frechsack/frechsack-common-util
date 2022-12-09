package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.function.BinaryOperator;

public class ConcurrentBinaryOperator<Type>  extends  ConcurrentBiFunction<Type, Type, Type> implements BinaryOperator<Type> {

    public ConcurrentBinaryOperator(@NotNull BinaryOperator<Type> operator) {
        super(operator);
    }
}
