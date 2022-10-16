package frechsack.stage.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class UndefinedFactory {

    static final frechsack.stage.util.Undefined<?> UNDEFINED = new UndefinedFactory.Undefined();
    static final frechsack.stage.util.Undefined<?> NULL = new UndefinedFactory.Null();

    static class Present<Type> implements frechsack.stage.util.Undefined<Type> {

        private final Type value;

        Present(Type value) {
            this.value = value;
        }

        @Override
        public Type get() {
            return value;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isUndefined() {
            return false;
        }
        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public <Out> frechsack.stage.util.Undefined<Out> map(Function<Type, Out> mapper) {
            Out value = mapper.apply(this.value);
            return value == null
                    ? frechsack.stage.util.Undefined.nullable()
                    : new Present<>(value);
        }

        @Override
        public <Out> frechsack.stage.util.Undefined<Out> flatMap(Function<Type, frechsack.stage.util.Undefined<Out>> mapper) {
            return Objects.requireNonNull(mapper.apply(this.value));
        }

        @Override
        public Type orElse(Type value) {
            return this.value;
        }

        @Override
        public Type orElseGet(Supplier<Type> supplier) {
            return this.value;
        }

        @Override
        public Type orElseThrow() {
            return this.value;
        }

        @Override
        public <ExceptionType extends Throwable> Type orElseThrow(Supplier<? extends ExceptionType> exceptionSupplier) {
            return this.value;
        }

        @Override
        public frechsack.stage.util.Undefined<Type> or(Supplier<? extends frechsack.stage.util.Undefined<Type>> supplier) {
            return this;
        }

        @Override
        public Stream<Type> stream() {
            return null;
        }

        @Override
        public frechsack.stage.util.Undefined<Type> filter(Predicate<? super Type> filter) {
            return filter.test(get())
                    ? this
                    : frechsack.stage.util.Undefined.undefined();
        }

        @Override
        public Optional<Optional<Type>> toOptional() {
            return Optional.of(Optional.of(value));
        }

        @Override
        public void ifPresent(Consumer<? super Type> consumer) {
            consumer.accept(value);
        }

        @Override
        public void ifPresentOrElse(Consumer<? super Type> consumer, Runnable empty) {
            consumer.accept(value);
        }


        @Override
        public void ifUndefined(Runnable action) {
        }

        @Override
        public void ifNull(Runnable action) {
        }

        @Override
        public frechsack.stage.util.Undefined<Type> ifNullMap(Supplier<frechsack.stage.util.Undefined<Type>> supplier) {
            return this;
        }

        @Override
        public frechsack.stage.util.Undefined<Type> ifUndefinedMap(Supplier<frechsack.stage.util.Undefined<Type>> supplier) {
            return this;
        }

        @Override
        public frechsack.stage.util.Undefined<Type> ifPresentMap(Function<Type, frechsack.stage.util.Undefined<Type>> mapper) {
            return Objects.requireNonNull(mapper.apply(value));
        }

        @Override
        public frechsack.stage.util.Undefined<Type> ifEmptyMap(Supplier<frechsack.stage.util.Undefined<Type>> supplier) {
            return this;
        }

        @Override
        public @NotNull Iterator<Type> iterator() {

            return new Iterator<>() {

                private boolean hasNext = true;

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public Type next() {
                    hasNext = false;
                    return value;
                }
            };
        }


        @Override
        public IntStream intStream() {
            if(value instanceof Number)
                return IntStream.of(((Number) value).intValue());
            throw new IllegalStateException("Value is not a number");
        }

        @Override
        public LongStream longStream() {
            if(value instanceof Number)
                return LongStream.of(((Number) value).longValue());
            throw new IllegalStateException("Value is not a number");
        }

        @Override
        public DoubleStream doubleStream() {
            if(value instanceof Number)
                return DoubleStream.of(((Number) value).doubleValue());
            throw new IllegalStateException("Value is not a number");
        }
    }

    private static class Null implements frechsack.stage.util.Undefined<Object> {

        @Override
        public Object get() {
            throw new NoSuchElementException("Value is null");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <Out> frechsack.stage.util.Undefined<Out> map(Function<Object, Out> mapper) {
            return (frechsack.stage.util.Undefined<Out>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <Out> frechsack.stage.util.Undefined<Out> flatMap(Function<Object, frechsack.stage.util.Undefined<Out>> mapper) {
            return (frechsack.stage.util.Undefined<Out>) this;
        }

        @Override
        public Object orElse(Object value) {
            return value;
        }

        @Override
        public Object orElseGet(Supplier<Object> supplier) {
            return Objects.requireNonNull(supplier).get();
        }

        @Override
        public Object orElseThrow() {
            throw new NoSuchElementException("Value is undefined");
        }

        @Override
        public <ExceptionType extends Throwable> Object orElseThrow(Supplier<? extends ExceptionType> exceptionSupplier) throws ExceptionType {
            throw exceptionSupplier.get();
        }

        @Override
        public frechsack.stage.util.Undefined<Object> or(Supplier<? extends frechsack.stage.util.Undefined<Object>> supplier) {
            return Objects.requireNonNull ((frechsack.stage.util.Undefined<Object>) supplier.get());
        }

        @Override
        public Stream<Object> stream() {
            return Stream.empty();
        }

        @Override
        public frechsack.stage.util.Undefined<Object> filter(Predicate<? super Object> filter) {
            return this;
        }

        @Override
        public Optional<Optional<Object>> toOptional() {
            return Optional.of(Optional.empty());
        }

        @Override
        public void ifPresent(Consumer<? super Object> consumer) {
        }

        @Override
        public void ifPresentOrElse(Consumer<? super Object> consumer, Runnable empty) {
            empty.run();
        }

        @Override
        public void ifUndefined(Runnable action) {
        }

        @Override
        public void ifNull(Runnable action) {
            action.run();
        }

        @Override
        public frechsack.stage.util.Undefined<Object> ifNullMap(Supplier<frechsack.stage.util.Undefined<Object>> supplier) {
            return Objects.requireNonNull(supplier.get());
        }

        @Override
        public frechsack.stage.util.Undefined<Object> ifUndefinedMap(Supplier<frechsack.stage.util.Undefined<Object>> supplier) {
            return this;
        }

        @Override
        public frechsack.stage.util.Undefined<Object> ifPresentMap(Function<Object, frechsack.stage.util.Undefined<Object>> mapper) {
            return this;
        }

        @Override
        public frechsack.stage.util.Undefined<Object> ifEmptyMap(Supplier<frechsack.stage.util.Undefined<Object>> supplier) {
            return Objects.requireNonNull(supplier.get());
        }

        @Override
        public @NotNull Iterator<Object> iterator() {
            return Spliterators.iterator(Spliterators.emptySpliterator());
        }

        @Override
        public IntStream intStream() {
            return IntStream.empty();
        }

        @Override
        public DoubleStream doubleStream() {
            return DoubleStream.empty();
        }

        @Override
        public LongStream longStream() {
            return LongStream.empty();
        }

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public boolean isUndefined() {
            return false;
        }
    }

    private static class Undefined implements frechsack.stage.util.Undefined<Object> {

        @Override
        public Object get() {
            throw new NoSuchElementException("Value is undefined");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public boolean isUndefined() {
            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <Out> frechsack.stage.util.Undefined<Out> map(Function<Object, Out> mapper) {
            return (frechsack.stage.util.Undefined<Out>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <Out> frechsack.stage.util.Undefined<Out> flatMap(Function<Object, frechsack.stage.util.Undefined<Out>> mapper) {
            return (frechsack.stage.util.Undefined<Out>) this;
        }

        @Override
        public Object orElse(Object value) {
            return value;
        }

        @Override
        public Object orElseGet(Supplier<Object> supplier) {
            return supplier.get();
        }

        @Override
        public Object orElseThrow() {
            throw new NoSuchElementException("Value is undefined");
        }

        @Override
        public <ExceptionType extends Throwable> Object orElseThrow(Supplier<? extends ExceptionType> exceptionSupplier) throws ExceptionType {
            throw exceptionSupplier.get();
        }

        @Override
        public frechsack.stage.util.Undefined<Object> or(Supplier<? extends frechsack.stage.util.Undefined<Object>> supplier) {
            return Objects.requireNonNull((frechsack.stage.util.Undefined<Object>) supplier.get());
        }

        @Override
        public Stream<Object> stream() {
            return Stream.empty();
        }

        @Override
        public frechsack.stage.util.Undefined<Object> filter(Predicate<? super Object> filter) {
            return this;
        }

        @Override
        public Optional<Optional<Object>> toOptional() {
            return Optional.empty();
        }

        @Override
        public void ifPresent(Consumer<? super Object> consumer) {
        }

        @Override
        public void ifPresentOrElse(Consumer<? super Object> consumer, Runnable empty) {
            empty.run();
        }

        @Override
        public void ifUndefined(Runnable action) {
            action.run();
        }

        @Override
        public void ifNull(Runnable action) {
        }

        @Override
        public frechsack.stage.util.Undefined<Object> ifNullMap(Supplier<frechsack.stage.util.Undefined<Object>> supplier) {
            return this;
        }

        @Override
        public frechsack.stage.util.Undefined<Object> ifUndefinedMap(Supplier<frechsack.stage.util.Undefined<Object>> supplier) {
            return Objects.requireNonNull(supplier.get());
        }

        @Override
        public frechsack.stage.util.Undefined<Object> ifPresentMap(Function<Object, frechsack.stage.util.Undefined<Object>> mapper) {
            return this;
        }

        @Override
        public frechsack.stage.util.Undefined<Object> ifEmptyMap(Supplier<frechsack.stage.util.Undefined<Object>> supplier) {
            return Objects.requireNonNull(supplier.get());
        }

        @Override
        public @NotNull Iterator<Object> iterator() {
            return Spliterators.iterator(Spliterators.emptySpliterator());
        }

        @Override
        public IntStream intStream() {
            return IntStream.empty();
        }

        @Override
        public DoubleStream doubleStream() {
            return DoubleStream.empty();
        }

        @Override
        public LongStream longStream() {
            return LongStream.empty();
        }
    }
}
