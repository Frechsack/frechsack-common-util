package frechsack.dev.util.function;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface ConsumerTry<E> extends Consumer<E>
{
    @Override
    default void accept(E e)
    {
        try
        {
            tryAccept(e);
        }
        catch (Exception ignored)
        {
        }
    }

    void tryAccept(E e) throws Exception;

    default ConsumerTry<E> tryAndThen(Consumer<? super E> after) {
        Objects.requireNonNull(after);
        return (E e) ->
        {
                tryAccept(e);
                after.accept(e);
        };
    }

   static <E> ConsumerTry<E> of(Consumer<E> consumer){
       return new ConsumerTryFactory.SimpleConsumerTry<>(consumer);
   }
}
