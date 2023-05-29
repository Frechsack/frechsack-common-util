package frechsack.dev.util;

import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Undefined<Type> {

    private static class Constants {

        private static final Undefined<?> UNDEFINED = new Undefined<>();
        private static final Undefined<?> NULL = new Undefined<>(null);
    }

    private final boolean isUndefined;

    private final Type value;

    private Undefined(){
        isUndefined = true;
        this.value = null;
    }

    private Undefined(Type value){
        isUndefined = false;
        this.value = value;

    }

    public static <Type> Undefined<Type> nil(){
        //noinspection unchecked
        return (Undefined<Type>) Constants.NULL;
    }

    public static <Type> Undefined<Type> undefined(){
        //noinspection unchecked
        return (Undefined<Type>) Constants.UNDEFINED;
    }

    public static <Type> Undefined<Type> empty(){
        return undefined();
    }

    public static <Type> Undefined<Type> of(){
        return undefined();
    }

    public static <Type> Undefined<Type> of(Type value){
        return new Undefined<>(Objects.requireNonNull(value));
    }

    public static <Type> Undefined<Type> ofNullable(Type value){
        return value == null
            ? nil()
            : new Undefined<>(value);
    }

    public boolean isUndefined(){
        return isUndefined;
    }

    public boolean isNull(){
        return !isUndefined && value == null;
    }

    public boolean isEmpty(){
        return value == null;
    }

    public boolean isPresent(){
        return value != null;
    }

    public Type orElse(Type other){
        return isPresent()
                ? value
                : other;
    }

    public Type orElse(Type nullValue, Type undefinedValue){
        if (isPresent())
            return value;
        if (isNull())
            return nullValue;
        return undefinedValue;
    }

    public <ExceptionType extends Throwable> Type orElseThrow(Supplier<? extends ExceptionType> exception) throws ExceptionType {
        if (isPresent())
            return value;
        throw exception.get();
    }

    public Type orElseThrow() {
        if (isPresent())
            return value;
        throw new NoSuchElementException("No value present");
    }

    public Type orElseGet(Supplier<? extends Type> other){
        return isPresent()
                ? value
                : other.get();
    }

    public Type orElseGet(Supplier<? extends Type> nullValue, Supplier<? extends Type> undefinedValue){
        if (isPresent())
            return value;
        if (isNull())
            return nullValue.get();
        return undefinedValue.get();
    }


    public Type get(){
        return orElseThrow();
    }

    public Type getIncludeNull(){
        if (isPresent())
            return value;
        if (isNull())
            return null;
        throw new NoSuchElementException("Undefined");
    }

    public Type getIncludeUndefined(){
        if (isPresent())
            return value;
        if (isUndefined())
            return null;
        throw new NoSuchElementException("Null");
    }

    public Type getAny(){
        if (isPresent())
            return value;
        return null;
    }

    public void ifPresent(Consumer<? super Type> action) {
        if (isPresent())
            action.accept(value);
    }

    public void ifEmpty(Runnable action) {
        if (isEmpty())
            action.run();
    }

    public void ifNull(Runnable action) {
        if (isNull())
            action.run();
    }

    public void ifUndefined(Runnable action) {
        if (isUndefined())
            action.run();
    }

    public void ifPresentOrElse(Consumer<? super Type> action, Runnable emptyAction) {
        if (isPresent())
            action.accept(value);
        else
            emptyAction.run();
    }

    public void act(Consumer<? super Type> presentAction, Runnable nullAction, Runnable undefinedAction){
        if (isPresent() && presentAction != null)
            presentAction.accept(value);
        else if (isNull() && nullAction != null)
            nullAction.run();
        else if (isUndefined() && undefinedAction != null)
            undefinedAction.run();
    }

    public Undefined<Type> filter(Predicate<? super Type> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent())
            return this;
        return predicate.test(value) ? this : undefined();
    }

    public <U> Undefined<U> map(Function<? super Type, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();

        return Undefined.ofNullable(mapper.apply(value));
    }

    public <U> Undefined<U> flatMap(Function<? super Type, ? extends Undefined<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();

        @SuppressWarnings("unchecked")
        Undefined<U> r = (Undefined<U>) mapper.apply(value);
        return Objects.requireNonNull(r);
    }

    public Undefined<Type> or(Supplier<? extends Undefined<? extends Type>> supplier) {
        Objects.requireNonNull(supplier);
        if (isPresent())
            return this;

        @SuppressWarnings("unchecked")
        Undefined<Type> r = (Undefined<Type>) supplier.get();
        return Objects.requireNonNull(r);
    }

    public Stream<Type> stream() {
        if (!isPresent())
            return Stream.empty();

        return Stream.of(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Undefined<?> other){
            if (isUndefined() && other.isUndefined())
                return true;
            if (isNull() && other.isNull())
                return true;
            if (isPresent() && other.isPresent())
                return Objects.equals(value, other.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (isPresent())
            return Objects.hashCode(value) + 1;
        if (isNull())
            return 1;
        return 0;
    }
}
