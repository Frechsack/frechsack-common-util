package frechsack.prod.util;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public interface Try<Type> extends Callable<Type> {
  
  static Try<Type> error(Exception error) {
    return new TryFactory.Error(Objects.requireNonNull(error));
  }
  
  static Try<Type> of(Type value) {
    return new TryFactory.Present(value);
  }
  
  static Try<Type> of(Supplier<Type> supplier) {
    try {
      return of(supplier.get());
    }
    catch(Exception e) {
      return error(e);
    }
  }
  
  static Try<Type> of(Callable<Type> callable) {
    try {
      return of(callable.get());
    }
    catch(Exception e) {
      return error(e);
    }
  }
  
  Type get();

  Exception getError();

  boolean isPresent();

  @Override
  default Type call() throws Exception {
    if(isPresent()) get();
    throw getError();
  }

  default boolean isError(){
    return !isPresent();
  }

  default Try<Type> peek(Consumer<Type> consumer) {
    if(isPresent()) consumer.consume(get());
  }

  default <Outtype> Try<Outtype> map(Function<Type, Outtype> function) {
    return isPresent()
      ? Try.of(function.apply(get())
      : 
  }

  default <Outtype> Try<Outtype> flatMap(Function<Try<Type>, Try<Outtype>> function){
    return function.apply(this);
  }

  default Try<Type> orTry(Callable<Type> callable){
    return isPresent()
      ? this
      : Try.of(callable);
  }
               
 default Type orGet(Supplier<Type> supplier) {
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
