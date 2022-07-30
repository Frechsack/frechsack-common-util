package frechsack.dev.util;

import java.util.Objects;

class UndefinedFactory {

    private UndefinedFactory(){}
    private enum State {
        UNDEFINED, NULL
    }

    static Undefined<?> UNDEFINED = new Empty<>(State.UNDEFINED);
    static Undefined.Number<?> UNDEFINED_NUMBER = new EmptyNumber(State.UNDEFINED);
    static Undefined.Boolean UNDEFINED_BOOLEAN = new EmptyBoolean(State.UNDEFINED);
    static Undefined<?> NULL = new Empty<>(State.NULL);
    static Undefined.Number<?> NULL_NUMBER = new EmptyNumber(State.NULL);
    static Undefined.Boolean NULL_BOOLEAN = new EmptyBoolean(State.NULL);

    @SuppressWarnings("unchecked")
    static Undefined.Number<Integer> pipeInteger(Undefined.Number<?> undefined){
        if(undefined instanceof Int) return (Undefined.Number<Integer>) undefined;
        if(undefined.isUndefined()) return Undefined.ofInt();
        return Undefined.ofInt(undefined.isNull() ? null : undefined.getInt());
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<java.lang.Double> pipeDouble(Undefined.Number<?> undefined){
        if(undefined instanceof Double) return (Undefined.Number<java.lang.Double>) undefined;
        if(undefined.isUndefined()) return Undefined.ofDouble();
        return Undefined.ofDouble(undefined.isNull() ? null : undefined.getDouble());
    }

    @SuppressWarnings("unchecked")
    static Undefined.Number<java.lang.Long> pipeLong(Undefined.Number<?> undefined){
        if(undefined instanceof Long) return (Undefined.Number<java.lang.Long>) undefined;
        if(undefined.isUndefined()) return Undefined.ofLong();
        return Undefined.ofLong(undefined.isNull() ? null : undefined.getLong());
    }

    static Undefined.Number<?> pipeNumber(Undefined.Number<?> undefined){
        if(undefined instanceof Number<?>) return undefined;
        if(undefined.isUndefined()) return Undefined.ofNumber();
        return Undefined.ofNumber(undefined.isNull() ? null : undefined.get());
    }

    static class Generic<E> extends NonEmpty<E> implements Undefined<E> {

        private final E value;

        Generic(E value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public E get() {
            return value;
        }
    }

    static class Boolean extends NonEmpty<java.lang.Boolean> implements Undefined.Boolean {

        private final boolean value;

        Boolean(boolean value) {
            this.value = value;
        }

        @Override
        public java.lang.Boolean get() {
            return value;
        }

        @Override
        public boolean getBoolean() {
            return value;
        }
    }

    static class Int extends NonEmpty<java.lang.Integer> implements Undefined.Number<java.lang.Integer> {

        private final int value;

        Int(int value){
            this.value = value;
        }

        @Override
        public java.lang.Integer get() {
            return value;
        }

        @Override
        public int getInt() {
            return value;
        }

        @Override
        public double getDouble() {
            return value;
        }

        @Override
        public long getLong() {
            return value;
        }
    }

    static class Long extends NonEmpty<java.lang.Long> implements Undefined.Number<java.lang.Long> {

        private final long value;

        Long(long value){
            this.value = value;
        }

        @Override
        public java.lang.Long get() {
            return value;
        }

        @Override
        public int getInt() {
            return (int) value;
        }

        @Override
        public double getDouble() {
            return value;
        }

        @Override
        public long getLong() {
            return value;
        }
    }

    static class Double extends NonEmpty<java.lang.Double> implements Undefined.Number<java.lang.Double> {

        private final double value;

        Double(double value){
            this.value = value;
        }

        @Override
        public java.lang.Double get() {
            return value;
        }

        @Override
        public int getInt() {
            return (int) value;
        }

        @Override
        public double getDouble() {
            return value;
        }

        @Override
        public long getLong() {
            return (long) value;
        }
    }

    static class Number<E extends java.lang.Number> extends NonEmpty<E> implements Undefined.Number<E> {

        private final E value;

        Number(E value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public E get() {
            return value;
        }

        @Override
        public int getInt() {
            return value.intValue();
        }

        @Override
        public double getDouble() {
            return value.doubleValue();
        }

        @Override
        public long getLong() {
            return value.longValue();
        }
    }

    abstract static class NonEmpty<T> implements Undefined<T> {

        @Override
        public boolean isUndefined() {
            return false;
        }

        @Override
        public boolean isNull() {
            return false;
        }
    }

    private static class Empty<T> implements Undefined<T> {

        private final State state;

        private Empty(State state) {
            this.state = state;
        }

        @Override
        public T get() {
            if(state == State.NULL)
                throw new IllegalStateException("Value is null");
            else if(state == State.UNDEFINED)
                throw new IllegalStateException("Value is undefined");
            return null;
        }

        @Override
        public boolean isUndefined() {
            return state == State.UNDEFINED;
        }

        @Override
        public boolean isNull() {
            return state == State.NULL;
        }
    }

    private static class EmptyNumber extends Empty<java.lang.Number> implements Generic.Number<java.lang.Number> {

        private EmptyNumber(State state) {
            super(state);
        }

        @Override
        public int getInt() {
            get();
            return -1;
        }

        @Override
        public double getDouble() {
            get();
            return -1;
        }

        @Override
        public long getLong() {
            get();
            return -1;
        }
    }

    private static class EmptyBoolean extends Empty<java.lang.Boolean> implements Generic.Boolean {

        private EmptyBoolean(State state) {
            super(state);
        }

        @Override
        public boolean getBoolean() {
            get();
            return false;
        }
    }
}