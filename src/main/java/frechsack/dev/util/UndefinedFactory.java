package frechsack.dev.util;

class UndefinedFactory {

    private static RuntimeException nullException(){
        return new IllegalStateException("Is null");
    }

    private static RuntimeException undefinedException(){
        return new IllegalStateException("Is undefined");
    }

    private UndefinedFactory(){}

    private abstract static class Present<Type> implements frechsack.dev.util.Undefined<Type> {

        private static class Generic<Type> extends Present<Type> {

            private final Type value;

            private Generic(Type value) {
                this.value = value;
            }

            @Override
            public Type get() {
                return value;
            }
        }

        private static class Number<Type extends java.lang.Number> extends Present<Type> implements frechsack.dev.util.Undefined.Number<Type> {

            private final Type value;

            private Number(Type value) {
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

        private static class Double extends Present<java.lang.Double> implements frechsack.dev.util.Undefined.Number<java.lang.Double> {

            private final double value;

            private Double(double value) {
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

        private static class Long extends Present<java.lang.Long> implements frechsack.dev.util.Undefined.Number<java.lang.Long> {

            private final long value;

            private Long(long value) {
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

        private static class Integer extends Present<java.lang.Integer> implements frechsack.dev.util.Undefined.Number<java.lang.Integer> {

            private final int value;

            private Integer(int value) {
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

        private static class Boolean extends Present<java.lang.Boolean> implements frechsack.dev.util.Undefined.Boolean {

            private final boolean value;

            private Boolean(boolean value) {
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

    private static class Undefined<Dummy> implements frechsack.dev.util.Undefined<Dummy> {

        static final frechsack.dev.util.Undefined<?> GENERIC = new Undefined<>();
        static final frechsack.dev.util.Undefined.Number<?> NUMBER = new Number();

        static final frechsack.dev.util.Undefined.Boolean BOOLEAN = new Boolean();

        private static class Boolean extends Undefined<java.lang.Boolean> implements frechsack.dev.util.Undefined.Boolean {

            @Override
            public boolean getAsBoolean() {
                throw undefinedException();
            }
        }

        private static class Number extends Undefined<Integer> implements frechsack.dev.util.Undefined.Number<Integer> {

            @Override
            public int getAsInt() {
                throw undefinedException();
            }

            @Override
            public long getAsLong() {
                throw undefinedException();
            }

            @Override
            public double getAsDouble() {
                throw undefinedException();
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
            throw undefinedException();
        }
    }

    private static class Null<Dummy> implements frechsack.dev.util.Undefined<Dummy> {

        static final frechsack.dev.util.Undefined<Object> GENERIC = new Null<>();

        static final frechsack.dev.util.Undefined.Boolean BOOLEAN = new Boolean();

        static final frechsack.dev.util.Undefined.Number<?> NUMBER = new Number();

        private static class Boolean extends Null<java.lang.Boolean> implements frechsack.dev.util.Undefined.Boolean {

           @Override
           public boolean getAsBoolean() {
               throw nullException();
           }
       }

        private static class Number extends Null<Integer> implements frechsack.dev.util.Undefined.Number<Integer> {

           @Override
           public int getAsInt() {
               throw nullException();
           }

           @Override
           public long getAsLong() {
               throw nullException();
           }

           @Override
           public double getAsDouble() {
               throw nullException();
           }
       }

        @Override
        public boolean isNull() {
           return true;
       }

        @Override
        public Dummy get() {
           throw nullException();
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
