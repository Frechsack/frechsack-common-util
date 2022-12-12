package frechsack.prod.util;

class TryFactory {

   static class Present<Type> implements Try<Type> {

        private final Type value;

        public Present(Type value) {
            this.value = value;
        }

        @Override
        public Type get(){
            return value;
        }

        @Override
        public Exception error() {
            throw new IllegalStateException(); 
        }

        @Override
        public boolean isPresent() {
            return true;
        }
   }

    record Error(Exception error) implements Try<Object> {

        @Override
        public Object get() {
            throw new IllegalStateException();
        }

        @Override
        public boolean isPresent() {
                return false;
            }

    }
}
