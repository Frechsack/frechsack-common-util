package frechsack.prod.util;

import java.util.NoSuchElementException;

class UndefinedFactory {

    private static NoSuchElementException noSuchElementException(){
        return new NoSuchElementException("Undefined is empty");
    }

    private UndefinedFactory(){}

    abstract static class Present<Type> implements frechsack.prod.util.Undefined<Type> {

        static class Generic<Type> extends Present<Type> {

            private final Type value;

            Generic(Type value) {
                this.value = value;
            }

            @Override
            public Type get() {
                return value;
            }
        }

        static class Number<Type extends java.lang.Number> extends Present<Type> implements frechsack.prod.util.Undefined.Number<Type> {

            private final Type value;

            Number(Type value) {
                this.value = value;
            }

            @Override
            public int getAsInt() {
                return value.intValue();
            }

            @Override
            public long getAsLong() {
                return value.longValue();
            }

            @Override
            public double getAsDouble() {
                return value.doubleValue();
            }

            @Override
            public Type get() {
                return value;
            }
        }

        static class Double extends Present<java.lang.Double> implements frechsack.prod.util.Undefined.Number<java.lang.Double> {

            private final double value;

            Double(double value) {
                this.value = value;
            }

            @Override
            public int getAsInt() {
                return (int) value;
            }

            @Override
            public long getAsLong() {
                return (long) value;
            }

            @Override
            public double getAsDouble() {
                return value;
            }

            @Override
            public java.lang.Double get() {
                return value;
            }
        }

        static class Float extends Present<java.lang.Float> implements frechsack.prod.util.Undefined.Number<java.lang.Float> {

            private final float value;

            Float(float value) {
                this.value = value;
            }

            @Override
            public int getAsInt() {
                return (int) value;
            }

            @Override
            public long getAsLong() {
                return (long) value;
            }

            @Override
            public double getAsDouble() {
                return value;
            }

            @Override
            public float getAsFloat() {
                return value;
            }

            @Override
            public java.lang.Float get() {
                return value;
            }
        }

        static class Long extends Present<java.lang.Long> implements frechsack.prod.util.Undefined.Number<java.lang.Long> {

            private final long value;

            Long(long value) {
                this.value = value;
            }

            @Override
            public int getAsInt() {
                return (int) value;
            }

            @Override
            public long getAsLong() {
                return value;
            }

            @Override
            public double getAsDouble() {
                return value;
            }

            @Override
            public java.lang.Long get() {
                return value;
            }
        }

        static class Integer extends Present<java.lang.Integer> implements frechsack.prod.util.Undefined.Number<java.lang.Integer> {

            private final int value;

            Integer(int value) {
                this.value = value;
            }

            @Override
            public int getAsInt() {
                return value;
            }

            @Override
            public long getAsLong() {
                return value;
            }

            @Override
            public double getAsDouble() {
                return value;
            }

            @Override
            public java.lang.Integer get() {
                return value;
            }
        }

        static class Boolean extends Present<java.lang.Boolean> implements frechsack.prod.util.Undefined.Boolean {

            private final boolean value;

            Boolean(boolean value) {
                this.value = value;
            }

            @Override
            public boolean getAsBoolean() {
                return value;
            }

            @Override
            public java.lang.Boolean get() {
                return value;
            }
        }

        @Override
        public boolean isUndefined() {
            return false;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isPresent() {
            return true;
        }
    }

    static class Undefined<Dummy> implements frechsack.prod.util.Undefined<Dummy> {

        static final frechsack.prod.util.Undefined<?> GENERIC = new Undefined<>();
        static final frechsack.prod.util.Undefined.Number<?> NUMBER = new Number();

        static final frechsack.prod.util.Undefined.Boolean BOOLEAN = new Boolean();

        private static class Boolean extends Undefined<java.lang.Boolean> implements frechsack.prod.util.Undefined.Boolean {

            @Override
            public boolean getAsBoolean() {
                throw noSuchElementException();
            }
        }

        private static class Number extends Undefined<Integer> implements frechsack.prod.util.Undefined.Number<Integer> {

            @Override
            public long getAsLong() {
                throw noSuchElementException();
            }

            @Override
            public double getAsDouble() {
                throw noSuchElementException();
            }
        }

        @Override
        public boolean isUndefined() {
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
        public Dummy get() {
            throw noSuchElementException();
        }
    }

    static class Null<Dummy> implements frechsack.prod.util.Undefined<Dummy> {

        static final frechsack.prod.util.Undefined<Object> GENERIC = new Null<>();

        static final frechsack.prod.util.Undefined.Boolean BOOLEAN = new Boolean();

        static final frechsack.prod.util.Undefined.Number<?> NUMBER = new Number();

        private static class Boolean extends Null<java.lang.Boolean> implements frechsack.prod.util.Undefined.Boolean {

           @Override
           public boolean getAsBoolean() {
               throw noSuchElementException();
           }
       }

        private static class Number extends Null<Integer> implements frechsack.prod.util.Undefined.Number<Integer> {

           @Override
           public long getAsLong() {
               throw noSuchElementException();
           }

           @Override
           public double getAsDouble() {
               throw noSuchElementException();
           }
       }

        @Override
        public boolean isNull() {
           return true;
       }

        @Override
        public Dummy get() {
           throw noSuchElementException();
       }

        @Override
        public boolean isUndefined() {
           return false;
       }

        @Override
        public boolean isPresent() {
            return false;
        }
    }
}
