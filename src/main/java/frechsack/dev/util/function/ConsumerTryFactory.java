package frechsack.dev.util.function;

import java.util.function.Consumer;

public class ConsumerTryFactory
{
    private ConsumerTryFactory(){}

    static class SimpleConsumerTry<E> implements ConsumerTry<E>{

        private final Consumer<E> consumer;

        SimpleConsumerTry(Consumer<E> consumer) {this.consumer = consumer;}

        @Override
        public void accept(E e)
        {
            consumer.accept(e);
        }

        @Override
        public void tryAccept(E e)
        {
            consumer.accept(e);
        }
    }
}
