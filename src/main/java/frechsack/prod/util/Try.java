package frechsack.prod.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Try<Type> extends Callable<Type> {
  
    @SuppressWarnings("unchecked")
    static <Type> Try<Type> error(@NotNull Exception error) {
      return (Try<Type>) new TryFactory.Error(Objects.requireNonNull(error));
    }

    static <Type> Try<Type> of(Type value) {
      return new TryFactory.Present<>(value);
    }

    static <Type> Try<Type> of(@NotNull Supplier<Type> supplier) {
      try {
        return of(supplier.get());
      }
      catch(Exception e) {
        return error(e);
      }
    }

    static <Type> Try<Type> of(@NotNull Callable<Type> callable) {
      try {
        return of(callable.call());
      }
      catch(Exception e) {
        return error(e);
      }
    }

    Type get();

    Exception error();

    boolean isPresent();

    @Override
    default Type call() throws Exception {
      if(isPresent()) get();
      throw error();
    }

    default boolean isError(){
      return !isPresent();
    }

    default Try<Type> peek(@NotNull Consumer<Type> consumer) {
      if(isPresent()) consumer.accept(get());
      return this;
    }

    @SuppressWarnings("unchecked")
    default <OutType> Try<OutType> map(@NotNull Function<Type, OutType> function) {
      return isPresent()
        ? Try.of(function.apply(get()))
        : (Try<OutType>) this;
    }

    default <OutType> Try<OutType> flatMap(@NotNull Function<Try<Type>, Try<OutType>> function){
      return function.apply(this);
    }

    default Try<Type> orTry(@NotNull Callable<Type> callable){
      return isPresent()
        ? this
        : of(callable);
    }

   default Type orGet(@NotNull Supplier<Type> supplier) {
      return isPresent()
        ? get()
        : supplier.get();
    }

    default Type or(Type type){
      return isPresent()
        ? get()
        : type;
    }
}
