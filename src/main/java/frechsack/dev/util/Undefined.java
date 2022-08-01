package frechsack.dev.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * An {@link Undefined} may contain a value, is null or is undefined.
 * This type behaves like {@link java.util.Optional} but allows more than two states ('empty' and 'present').
 * An Undefined has three possible states: 'undefined', 'null' or 'present'.
 * To check if this instance represents 'undefined' use {@link #isUndefined()} and {@link  #isNull()} to check if it is 'null'.
 * In contexts where this class type is used, it should never be considered, that an instance if this type is null. Instead, an instance of {@link #of()} should be passed.
 * @param <E> The element-type.
 */
public interface Undefined<E> extends Iterable<E> {

    @SuppressWarnings("unchecked")
    static <E> Undefined<E> of(E value){
        return value == null ? (Undefined<E>) UndefinedFactory.NULL : new UndefinedFactory.Generic<>(value);
    }

    @SuppressWarnings("unchecked")
    static <E> Undefined<E> of(){
        return (Undefined<E>) UndefinedFactory.UNDEFINED;
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Integer> ofInt(Integer value){
        return value == null ? (Number<Integer>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Int(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Integer> ofInt(){
        return (Number<Integer>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Number<Integer> ofInt(int value){
        return new UndefinedFactory.Int(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Float> ofFloat(Float value){
        return value == null ? (Number<Float>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Float(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Float> ofFloat(){
        return (Number<Float>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Number<Float> ofFloat(float value){
        return new UndefinedFactory.Float(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Double> ofDouble(Double value){
        return value == null ? (Number<Double>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Double(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Double> ofDouble(){
        return (Number<Double>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Number<Double> ofDouble(int value){
        return new UndefinedFactory.Double(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Long> ofLong(Long value){
        return value == null ? (Number<Long>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Long(value);
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<Long> ofLong(){
        return (Number<Long>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Number<Long> ofLong(long value){
        return new UndefinedFactory.Long(value);
    }

    @SuppressWarnings("unchecked")
    static <E extends java.lang.Number> Undefined.Number<E> ofNumber(E value){
        return value == null ? (Number<E>) UndefinedFactory.NULL_NUMBER : new UndefinedFactory.Number<>(value);
    }

    @SuppressWarnings("unchecked")
    static <E extends java.lang.Number>  Undefined.Number<E> ofNumber(){
        return (Number<E>) UndefinedFactory.UNDEFINED_NUMBER;
    }

    static Undefined.Boolean ofBoolean(java.lang.Boolean value){
        return value == null ?  UndefinedFactory.NULL_BOOLEAN : new UndefinedFactory.Boolean(value);
    }

    static Undefined.Boolean ofBoolean(){
        return UndefinedFactory.UNDEFINED_BOOLEAN;
    }

    static Undefined.Boolean ofBoolean(boolean value){
        return new UndefinedFactory.Boolean(value);
    }


    /**
     * Returns the value, if this instance is not empty. May throws an exception in case it is empty.
     * @return The value wrapped by this instance.
     */
    E get();

    /**
     * Checks if this instance is of state 'undefined'.
     * @return True if this instance represents 'undefined', otherwise false.
     */
    boolean isUndefined();

    /**
     * Checks if this instance is of state 'null'.
     * @return True if this instance represents 'null', otherwise false.
     */
    boolean isNull();

    /**
     * Checks if this instance represents 'null' or 'undefined'.
     * @return True if this instance is 'null' or 'undefined', otherwise false.
     */
    default boolean isEmpty(){
        return isUndefined() || isNull();
    }

    /**
     * Checks if this instance contains an actual value.
     * Such an instance will be considered as 'present'. Behaves like the opposite of {@link #isEmpty()}.
     * @return True if this instance is not empty, otherwise false.
     */
    default boolean isPresent(){
        return !isUndefined() && !isNull();
    }

    /**
     * If this instance contains a value, that value is returned, otherwise the passed argument is returned.
     * @param value The value, that is returned in case this instance contains no value.
     * @return This instance´s value or the passed one.
     */
    default E orElse(E value) {
        return isEmpty() ? value : get();
    }

    /**
     * Returns a new {@link Undefined}, that contains the value of this instance if the specified test is passed. If not an {@link Undefined} with the state 'undefined' is returned.
     * @param predicate The test.
     * @return An instance of {@link Undefined}.
     */
    default Undefined<E> filter(Predicate<E> predicate) {
        if(isEmpty()) return this;
        if(predicate == null) return Undefined.of();
        boolean isPassed = predicate.test(get());
        return isPassed ? this : Undefined.of();
    }

    default E orElseGet(Supplier<E> supplier) {
        return isPresent() ? get() : Objects.requireNonNull(supplier).get();
    }

    default Undefined<E> or(Supplier<E> supplier) {
        if(isPresent()) return this;
        if(supplier == null) return of();
        return of(supplier.get());
    }

    @SuppressWarnings("unchecked")
    default <T> Undefined<T> map(Function<E, T> map) {
        if(isEmpty())
            return (Undefined<T>) this;
        if(map == null) return of();
        return of(map.apply(get()));
    }

    @SuppressWarnings("unchecked")
    default <T extends java.lang.Number> Undefined.Number<T> mapNumber(Function<E,T> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Number)
                return (Number<T>) this;
            else{
                if(isNull()) return (Number<T>) UndefinedFactory.NULL_NUMBER;
                else return (Number<T>)  UndefinedFactory.UNDEFINED_NUMBER;
            }
        }
        if(map == null) return ofNumber();
        return ofNumber(map.apply(get()));
    }

    @SuppressWarnings("unchecked")
    default Undefined.Number<Integer> mapInt(Function<E, Integer> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Number)
                return (Number<Integer>) this;
            else{
                if(isNull()) return (Number<Integer>) UndefinedFactory.NULL_NUMBER;
                else return (Number<Integer>)  UndefinedFactory.UNDEFINED_NUMBER;
            }
        }
        if(map == null) return ofInt();
        return ofInt(map.apply(get()));
    }

    @SuppressWarnings("unchecked")
    default Undefined.Number<Long> mapLong(Function<E, Long> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Number)
                return (Number<Long>) this;
            else{
                if(isNull()) return (Number<Long>) UndefinedFactory.NULL_NUMBER;
                else return (Number<Long>)  UndefinedFactory.UNDEFINED_NUMBER;
            }
        }
        if(map == null) return ofLong();
        return ofLong(map.apply(get()));
    }

    @SuppressWarnings("unchecked")
    default Undefined.Number<Double> mapDouble(Function<E, Double> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Number)
                return (Number<Double>) this;
            else{
                if(isNull()) return (Number<Double>) UndefinedFactory.NULL_NUMBER;
                else return (Number<Double>)  UndefinedFactory.UNDEFINED_NUMBER;
            }
        }
        if(map == null) return ofDouble();
        return ofDouble(map.apply(get()));
    }

    default Undefined.Boolean mapBoolean(Function<E, java.lang.Boolean> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Boolean)
                return (Boolean) this;
            else{
                if(isNull()) return UndefinedFactory.NULL_BOOLEAN;
                else return UndefinedFactory.UNDEFINED_BOOLEAN;
            }
        }
        if(map == null) return ofBoolean();
        return ofBoolean(map.apply(get()));
    }
    @SuppressWarnings("unchecked")
    default <T> Undefined<T> flatMap(Function<E, Undefined<T>> map){
        if(isEmpty()) return (Undefined<T>) this;
        if(map == null) return of();
        return map.apply(get());
    }

    @SuppressWarnings("unchecked")
    default <T extends java.lang.Number> Undefined.Number<T> flatMapNumber(Function<E, Undefined.Number<T>> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Number)
                return (Number<T>) this;
            else{
                if(isNull()) return (Number<T>) UndefinedFactory.NULL_NUMBER;
                else return (Number<T>)  UndefinedFactory.UNDEFINED_NUMBER;
            }
        }
        if(map == null) return ofNumber();
        return map.apply(get());
    }

    @SuppressWarnings("unchecked")
    default Undefined.Number<Integer> flatMapInt(Function<E, Undefined.Number<Integer>> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Number)
                return (Number<Integer>) this;
            else{
                if(isNull()) return (Number<Integer>) UndefinedFactory.NULL_NUMBER;
                else return (Number<Integer>)  UndefinedFactory.UNDEFINED_NUMBER;
            }
        }
        if(map == null) return ofInt();
        return map.apply(get());
    }

    @SuppressWarnings("unchecked")
    default Undefined.Number<Long> flatMapLong(Function<E, Undefined.Number<Long>> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Number)
                return (Number<Long>) this;
            else{
                if(isNull()) return (Number<Long>) UndefinedFactory.NULL_NUMBER;
                else return (Number<Long>)  UndefinedFactory.UNDEFINED_NUMBER;
            }
        }
        if(map == null) return ofLong();
        return map.apply(get());
    }

    @SuppressWarnings("unchecked")
    default Undefined.Number<Double> flatMapDouble(Function<E, Undefined.Number<Double>> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Number)
                return (Number<Double>) this;
            else{
                if(isNull()) return (Number<Double>) UndefinedFactory.NULL_NUMBER;
                else return (Number<Double>)  UndefinedFactory.UNDEFINED_NUMBER;
            }
        }
        if(map == null) return ofDouble();
        return map.apply(get());
    }

    default Undefined.Boolean flatMapBoolean(Function<E, Undefined.Boolean> map){
        if(isEmpty()) {
            if(this instanceof Undefined.Boolean)
                return (Boolean) this;
            else{
                if(isNull()) return UndefinedFactory.NULL_BOOLEAN;
                else return UndefinedFactory.UNDEFINED_BOOLEAN;
            }
        }
        if(map == null) return ofBoolean();
        return map.apply(get());
    }

    default <T> Undefined<T> transform(Function<Undefined<E>, Undefined<T>> map){
        if(map == null) return of();
        return map.apply(this);
    }

    default <T extends java.lang.Number> Undefined.Number<T> transformToNumber(Function<Undefined<E>, Undefined.Number<T>> map){
        if(map == null) return ofNumber();
        return map.apply(this);
    }

    default Undefined.Boolean transformToBoolean(Function<Undefined<E>, Undefined.Boolean> map){
        if(map == null) return ofBoolean();
        return map.apply(this);
    }

    default Stream<E> stream(){
        return isEmpty() ? Stream.of() : Stream.of(get());
    }

    default void ifPresent(Consumer<E> consumer){
        if(isPresent()) consumer.accept(get());
    }

    default void ifPresentOrElse(Consumer<E> consumer, Runnable empty){
        if(isPresent()) consumer.accept(get());
        else empty.run();
    }

    default <T> Undefined<E> ifMatches(Predicate<Undefined<E>> condition, Function<Undefined<E>, T> extractor, Consumer<T> function) {
        if(condition.test(this))
            function.accept(extractor.apply(this));
        return this;
    }

    @Override
    default Iterator<E> iterator(){
        return new Iterator<>() {
            private boolean isInitial = true;

            @Override
            public boolean hasNext() {
                return isInitial && isPresent();
            }

            @Override
            public E next() {
                isInitial = false;
                return get();
            }
        };
    }

    interface Number<E extends java.lang.Number> extends Undefined<E>{

        int getInt();

        double getDouble();

        long getLong();

        default int orElse(int value){
            return isEmpty() ? value : getInt();
        }

        default long orElse(long value){
            return isEmpty() ? value : getLong();
        }

        default double orElse(double value){
            return isEmpty() ? value : getDouble();
        }

        default int orElseGet(IntSupplier supplier) {
            return isPresent() ? getInt() : Objects.requireNonNull(supplier).getAsInt();
        }

        default double orElseGet(DoubleSupplier supplier) {
            return isPresent() ? getDouble() : Objects.requireNonNull(supplier).getAsDouble();
        }

        default long orElseGet(LongSupplier supplier) {
            return isPresent() ? getLong() : Objects.requireNonNull(supplier).getAsLong();
        }

        default Undefined.Number<Integer> or(IntSupplier supplier){
            if(isPresent()) return UndefinedFactory.pipeInteger(this);
            if(supplier == null) return ofInt();
            return ofInt(supplier.getAsInt());
        }

        default Undefined.Number<Double> or(DoubleSupplier supplier){
            if(isPresent()) return UndefinedFactory.pipeDouble(this);
            if(supplier == null) return ofDouble();
            return ofDouble(supplier.getAsDouble());
        }

        default Undefined.Number<Long> or(LongSupplier supplier){
            if(isPresent()) return UndefinedFactory.pipeLong(this);
            if(supplier == null) return ofLong();
            return ofLong(supplier.getAsLong());
        }

        default Undefined.Number<Integer> filter(IntPredicate predicate){
            if(isEmpty()) return UndefinedFactory.pipeInteger(this);
            if(predicate == null) return ofInt();
            boolean isPassed = predicate.test(getInt());
            return isPassed ? UndefinedFactory.pipeInteger(this) : Undefined.ofInt();
        }

        default Undefined.Number<Double> filter(DoublePredicate predicate){
            if(isEmpty()) return UndefinedFactory.pipeDouble(this);
            if(predicate == null) return ofDouble();
            boolean isPassed = predicate.test(getDouble());
            return isPassed ? UndefinedFactory.pipeDouble(this) : Undefined.ofDouble();
        }

        default Undefined.Number<Long> filter(LongPredicate predicate){
            if(isEmpty()) return UndefinedFactory.pipeLong(this);
            if(predicate == null) return ofLong();
            boolean isPassed = predicate.test(getLong());
            return isPassed ? UndefinedFactory.pipeLong(this) : Undefined.ofLong();
        }

        @Override
        default Undefined.Number<Integer> mapInt(Function<E, Integer> map) {
            if(isEmpty()) return UndefinedFactory.pipeInteger(this);
            if(map == null) return ofInt();
            return ofInt(map.apply(get()));
        }

        @Override
        default Undefined.Number<Long> mapLong(Function<E, Long> map) {
            if(isEmpty()) return UndefinedFactory.pipeLong(this);
            if(map == null) return ofLong();
            return ofLong(map.apply(get()));
        }

        @Override
        default Undefined.Number<Double> mapDouble(Function<E, Double> map) {
            if(isEmpty()) return UndefinedFactory.pipeDouble(this);
            if(map == null) return ofDouble();
            return ofDouble(map.apply(get()));
        }

        default Undefined.Number<Integer> flatMapInt(IntFunction<Undefined.Number<Integer>> map){
            if(isEmpty()) return UndefinedFactory.pipeInteger(this);
            if(map == null) return ofInt();
            return map.apply(getInt());
        }

        default Undefined.Number<Long> flatMapLong(LongFunction<Undefined.Number<Long>> map){
            if(isEmpty()) return UndefinedFactory.pipeLong(this);
            if(map == null) return ofLong();
            return map.apply(getLong());
        }

        default Undefined.Number<Double> flatMapDouble(DoubleFunction<Undefined.Number<Double>> map){
            if(isEmpty()) return UndefinedFactory.pipeDouble(this);
            if(map == null) return ofDouble();
            return map.apply(getDouble());
        }

        default IntStream streamInt(){
            return isEmpty() ? IntStream.empty() : IntStream.of(getInt());
        }

        default DoubleStream streamDouble(){
            return isEmpty() ? DoubleStream.empty() : DoubleStream.of(getDouble());
        }

        default LongStream streamLong(){
            return isEmpty() ? LongStream.empty() : LongStream.of(getLong());
        }

        default void ifPresent(IntConsumer consumer){
            if(isPresent()) consumer.accept(getInt());
        }

        default void ifPresentOrElse(IntConsumer consumer, Runnable empty){
            if(isPresent()) consumer.accept(getInt());
            else empty.run();
        }

        default void ifPresent(DoubleConsumer consumer){
            if(isPresent()) consumer.accept(getDouble());
        }

        default void ifPresentOrElse(DoubleConsumer consumer, Runnable empty){
            if(isPresent()) consumer.accept(getDouble());
            else empty.run();
        }

        default void ifPresent(LongConsumer consumer){
            if(isPresent()) consumer.accept(getLong());
        }

        default void ifPresentOrElse(LongConsumer consumer, Runnable empty){
            if(isPresent()) consumer.accept(getLong());
            else empty.run();
        }

        default Undefined.Number<E> ifMatches(Predicate<Number<E>> condition, IntConsumer function) {
            if(condition.test(this))
                function.accept(getInt());
            return this;
        }

        default Undefined.Number<E> ifMatches(Predicate<Number<E>> condition, DoubleConsumer function) {
            if(condition.test(this))
                function.accept(getDouble());
            return this;
        }

        default Undefined.Number<E> ifMatches(Predicate<Number<E>> condition, LongConsumer function) {
            if(condition.test(this))
                function.accept(getLong());
            return this;
        }
    }

    interface Boolean extends Undefined<java.lang.Boolean> {

        boolean getBoolean();

        default boolean orElse(boolean value){
            return isEmpty() ? value : getBoolean();
        }

        default Undefined.Boolean or(BooleanSupplier supplier) {
            if(isPresent()) return this;
            if(supplier == null) return ofBoolean();
            return ofBoolean(supplier.getAsBoolean());
        }

        default boolean orElseGet(BooleanSupplier supplier) {
            return isPresent() ? getBoolean() : Objects.requireNonNull(supplier).getAsBoolean();
        }
    }
}
