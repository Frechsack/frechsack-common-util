package frechsack.prod.util;

import org.jetbrains.annotations.NotNull;

class TryFactory {

   static class Present<Type> implements Try<Type> {

        private final Type value;

        public Present(Type value) {
            this.value = value;
        }

        @Override
        public Type value(){
            return value;
        }

        @Override
        public @NotNull Exception error() {
            throw new IllegalStateException(); 
        }

        @Override
        public boolean isPresent() {
            return true;
        }
   }

    record Error(Exception error) implements Try<Object> {

        @Override
        public Object value() {
            throw new IllegalStateException();
        }

        @Override
        public boolean isPresent() {
                return false;
            }

    }
}
