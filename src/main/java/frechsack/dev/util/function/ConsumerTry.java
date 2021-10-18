package frechsack.dev.util.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * An implementation of {@link Consumer} that my throw an Exception during execution.
 * @param <E> The Consumer´s element class-type.
 */
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

    /**
     * Consumes the specified element. This operation my throw an Exception.
     * @param e The element that will be consumed.
     * @throws Exception An Exception that may be thrown during consuming.
     */
    void tryAccept(E e) throws Exception;

    /**
     * Creates a new Consumer that calls this Consumer first and then applies the specified Consumer afterwards. In case this Consumer throws an Exception, the specified Consumer will not be invoked.
     * @param after The second Consumer, that will be invoked after this.
     * @return Returns the Consumer.
     */
    default ConsumerTry<E> tryAndThen(Consumer<? super E> after) {
        Objects.requireNonNull(after);
        return (E e) ->
        {
                tryAccept(e);
                after.accept(e);
        };
    }

    /**
     * Creates a ConsumerTry based on the specified Consumer.
     * @param consumer The Consumer.
     * @param <E> The Consumumer´s element class-type.
     * @return Returns the wrapped Consumer.
     */
   static <E> ConsumerTry<E> of(Consumer<E> consumer){
       return new ConsumerTryFactory.SimpleConsumerTry<>(consumer);
   }
}
