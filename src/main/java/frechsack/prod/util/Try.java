// TODO: Extend Callable
public interface Try<Type> {
  
  static Try<Type> error(Exception error) {
    // TODO: Null-check
    // TOOD: Impl.
    return null;
  }
  
  static Try<Type> error() {
    // TOOD: Impl.
    return error(null);
  }

  static Try<Type> of(Type value) {
    // TOOD: Impl.
    return null;
  }
  
  static Try<Type> of(Supplier<Type> supplier) {
    // TODO: Null-check
    // TODO: Impl.
    return null;
  }
  
  static Try<Type> of(Callable<Type> callable) {
    // TODO: Null-check
    // TODO: Impl.
    return null;
  }
  
  Type get();

  Exception getError();

  default boolean isError(){
    return getError() != null;
  }
  
  default boolean isPresent() {
    return getError() == null;
  }
  
  default <Outtype> Try<Outtype> map(Function<Type, Outtype> function) {
    return isPresent()
      ? Try.of(function.apply(get())
      : 
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
