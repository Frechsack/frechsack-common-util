package frechsack.prod.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A {@link Try} may contain in instance of a type (or at least null) or an {@link Exception}.
 * To check if this object contains an object call {@link #isPresent()}. The value can than be received by {@link #value()}.
 * The same procedure works if this instance contains an error. Check if an error is present with {@link #isError()} and get it with {@link #error()}.
 * @param <Type> The value class-type.
 */
public interface Try<Type> extends Callable<Type> {

    /**
     * Creates a new Try with an Exception.
     * @param error The exception. Must not be null.
     * @return Returns the created Try with the given Exception.
     * @param <Type> The value class-type.
     */
    @SuppressWarnings("unchecked")
    static <Type> Try<Type> error(@NotNull Exception error) {
      return (Try<Type>) new TryFactory.Error(Objects.requireNonNull(error));
    }

    /**
     * Creates a new Try with a value.
     * @param value The value.
     * @return Returns the created instance of Try with the given value.
     * @param <Type> The value class-type.
     */
    static <Type> Try<Type> of(Type value) {
      return new TryFactory.Present<>(value);
    }

    /**
     * Creates a new Try with a value received by the given Supplier. If the Supplier throws an Exception during {@link Supplier#get()},
     * the RuntimeException will be used to create an instance of Try with the thrown RuntimeException.
     * @param supplier The Supplier.
     * @return Returns the created instance of Try with the value received from the Supplier or the thrown RuntimeException.
     * @param <Type> The value class-type.
     */
    static <Type> Try<Type> of(@NotNull Supplier<Type> supplier) {
      try {
        return of(supplier.get());
      }
      catch(Exception e) {
        return error(e);
      }
    }

    /**
     * Creates a new Try with a value received by the given Callable. If the Callable throws an Exception during {@link Callable#call()},
     * the Exception will be used to create an instance of Try with the thrown Exception.
     * @param callable The Callable.
     * @return Returns the created instance of Try with the value received from the Callable or the thrown RuntimeException.
     * @param <Type> The value class-type.
     */
    static <Type> Try<Type> of(@NotNull Callable<Type> callable) {
      try {
        return of(callable.call());
      }
      catch(Exception e) {
        return error(e);
      }
    }

    /**
     * Returns the value inside of this Try. If no value is present ({@link #isPresent()} fails), a RuntimeException should be thrown.
     * @return Returns the value.
     */
    Type value();

    /**
     * Returns the Exception hold by this Try. If no Exception is present ({@link #isError()} fails), a RuntimeException should be thrown.
     * @return Returns the Exception.
     */
    @NotNull Exception error();

    /**
     * Checks if this instance contains a value (not an Exception) and calling {@link #value()} is safe.
     * @return Returns true if this instance contains a value, otherwise false.
     */
    boolean isPresent();

    /**
     * If this instance contains a value, the value will be returned. If no value is present, the Exception hold by this instance is thrown.
     * @return Returns the value inside of this Try, if it is present.
     * @throws Exception Throws the Exception inside of this Try, if it is present.
     */
    @Override
    default Type call() throws Exception {
      if(isPresent()) return value();
      throw error();
    }

    /**
     * Returns a Stream with zero or one elements. The Stream contains the value inside of this instance, if it is present, otherwise an empty Stream is returned.
     * @return Returns a Stream.
     */
    default Stream<Type> stream() {
      return isPresent()
              ? Stream.of(value())
              : Stream.empty();
    }

    /**
     * Returns a Stream with zero or one elements. The Stream contains the error inside of this instance, if it is present, otherwise an empty Stream is returned.
     * @return Returns a Stream.
     */
      default Stream<Exception> errorStream() {
        return isPresent()
                ? Stream.empty()
                : Stream.of(error());
      }

    /**
     * Checks if this instance contains an error and calling {@link #error()} is safe.
     * This should be equal to the inverse of {@link #isPresent()}.
     * @return Returns true if this instance contains an error, otherwise false.
     */
    default boolean isError(){
      return !isPresent();
    }

    /**
     * Performs an operation on the value inside of this instance, if it is present.
     * If it is present and an Exception is thrown during the execution of the given Consumer, an instance of Try with the thrown RuntimeException is returned.
     * @param consumer The operation to be performed, if a value is present.
     * @return Returns a reference to this object, or an instance of Try with a thrown Exception during execution of the Consumer.
     */
    default Try<Type> peek(@NotNull Consumer<Type> consumer) {
      Objects.requireNonNull(consumer);
      if(isPresent()) {
        try {
          consumer.accept(value());
        }
        catch (Exception e){
          return error(e);
        }
      }
      return this;
    }

    /**
     * Performs an operation on the exception inside of this instance, if it is present.
     * If it is present and an Exception is thrown during the execution of the given Consumer, an instance of Try with the thrown RuntimeException is returned.
     * @param consumer The operation to be performed, if an exception is present.
     * @return Returns a reference to this object, or an instance of Try with a thrown Exception during execution of the Consumer.
     */
    default Try<Type> peekError(@NotNull Consumer<Exception> consumer){
        try {
            if(isPresent()) return this;
            Objects.requireNonNull(consumer).accept(error());
        }
        catch (Exception e){
            return error(e);
        }
        return this;
    }

    /**
     * Performs a transformation of the value inside of this Try, if it is present.
     * If it is present and an Exception is thrown during the execution of the given Function, an instance of Try with the thrown RuntimeException is returned.
     * @param function The transform function.
     * @return Returns a Try with the transformed value, or an instance of Try with a thrown Exception during execution of the Consumer.
     * @param <OutType> The new value-type.
     */
    @SuppressWarnings("unchecked")
    default <OutType> Try<OutType> map(@NotNull Function<Type, OutType> function) {
      Objects.requireNonNull(function);
      if(isError()) return (Try<OutType>) this;
      try {
        return of(function.apply(value()));
      }
      catch (Exception e){
        return error(e);
      }
    }

    /**
     * Performs a transformation of the exception in this Try, if it contains one.
     * If the transformation itself throws an exception, an instance containing the raised exception will be returned.
     * @param function The mapping function.
     * @return Returns a new Try with the transformed exception, or this instance if it contains a value.
     */
    default Try<Type> mapError(Function<Exception, Exception> function){
        Objects.requireNonNull(function);
        if(isPresent()) return this;
        try {
            return error(function.apply(error()));
        }catch (Exception e){
            return error(e);
        }
    }

    /**
     * Performs a transformation of the exception in this Try, if it contains one.
     * The transformation will only be done, if this Try contains an exception and if the exception has a specific class-type.
     * If the transformation itself throws an exception, an instance containing the raised exception will be returned.
     * @param errorType The exception class-type.
     * @param function The mapping function.
     * @return Returns a new Try with the transformed exception, or this instance if it contains a value.
     */
    @SuppressWarnings("unchecked")
    default <ErrorType extends Exception> Try<Type> mapErrorExact(Class<? extends ErrorType> errorType, Function<ErrorType, Exception> function){
        Objects.requireNonNull(errorType);
        Objects.requireNonNull(function);
        if(isPresent() || error().getClass() != errorType) return this;
        try {
           return error(function.apply((ErrorType) error()));
        }
        catch (Exception e){
            return error(e);
        }
    }

    /**
     * Performs a transformation of the exception in this Try, if it contains one.
     * The transformation will only be done, if this Try contains an exception and if the exception class-type is accessible (extends) the specified class-type.
     * If the transformation itself throws an exception, an instance containing the raised exception will be returned.
     * @param errorType The exception class-type.
     * @param function The mapping function.
     * @return Returns a new Try with the transformed exception, or this instance if it contains a value.
     */
    @SuppressWarnings("unchecked")
    default <ErrorType extends Exception> Try<Type> mapErrorInstance(Class<? extends ErrorType> errorType, Function<ErrorType, Exception> function){
        Objects.requireNonNull(errorType);
        Objects.requireNonNull(function);
        if(isPresent() || !errorType.isAssignableFrom(error().getClass())) return this;
        try {
            return error(function.apply((ErrorType) error()));
        }
        catch (Exception e){
            return error(e);
        }
    }

    /**
     * Performs a transformation of this object.
     * If an Exception is thrown during the execution of the given Function, an instance of Try with the thrown RuntimeException is returned.
     * @param function The transform function.
     * @return Returns a Try with the transformed value, or an instance of Try with a thrown Exception during execution of the Consumer.
     * @param <OutType> The new value-type.
     */
    default <OutType> Try<OutType> flatMap(@NotNull Function<Try<Type>, Try<OutType>> function){
      Objects.requireNonNull(function);
      try {
        return function.apply(this);
      }
      catch (Exception e){
        return error(e);
      }
    }

    /**
     * Returns a Try with a value. If this Try does contain an error, a new Try is created, with the given Callable as it´s value.
     * @param callable The Callable.
     * @return Returns this object, if a value is present, otherwise a new Try is created.
     */
    default Try<Type> orTry(@NotNull Callable<Type> callable){
      return isPresent()
        ? this
        : of(callable);
    }

    /**
     * Returns a value. If this Try does not contain one, the given Supplier is used to get one.
     * @param supplier The Supplier.
     * @return Returns one value.
     */
   default Type orGet(@NotNull Supplier<Type> supplier) {
      return isPresent()
        ? value()
        : supplier.get();
    }
    /**
     * Returns a value. If this Try does not contain one, the given one is returned instead.
     * @param value The alternative Supplier.
     * @return Returns one value.
     */
    default Type or(Type value){
      return isPresent()
        ? value()
        : value;
    }
}
