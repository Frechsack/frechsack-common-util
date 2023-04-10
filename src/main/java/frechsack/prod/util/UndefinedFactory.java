package frechsack.prod.util;

import java.util.NoSuchElementException;
import java.util.Objects;

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

            @Override
            public String toString() {
                return "Undefined.Number{" + get() + "}";
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

            @Override
            public String toString() {
                return "Undefined.Double{" + get() + "}";
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

            @Override
            public String toString() {
                return "Undefined.Float{" + get() + "}";
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

            @Override
            public String toString() {
                return "Undefined.Long{" + get() + "}";
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

            @Override
            public String toString() {
                return "Undefined.Integer{" + get() + "}";
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

            @Override
            public String toString() {
                return "Undefined.Boolean{" + get() + "}";
            }
        }

        private Present(){}

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

        @Override
        public String toString() {
            return "Undefined{" + get() + "}";
        }

        @Override
        public int hashCode() {
            return get().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this)
                return true;
            if(obj instanceof frechsack.prod.util.Undefined<?> undefined)
                return Objects.equals(undefined.get(), get());
            return super.equals(obj);
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

            @Override
            public String toString() {
                return "Undefined.Boolean{undefined}";
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

            @Override
            public String toString() {
                return "Undefined.Number{undefined}";
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
        public String toString() {
            return "Undefined{undefined}";
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj instanceof frechsack.prod.util.Undefined<?> undefined)
                return undefined.isUndefined();
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return 1;
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

            @Override
            public String toString() {
                return "Undefined.Boolean{null}";
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

            @Override
            public String toString() {
                return "Undefined.Number{null}";
            }
       }

       private Null(){}
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

        @Override
        public String toString() {
            return "Undefined{null}";
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this)
                return true;
            if (obj instanceof frechsack.prod.util.Undefined<?> undefined)
                return undefined.isNull();
            return super.equals(obj);
        }
    }
}
