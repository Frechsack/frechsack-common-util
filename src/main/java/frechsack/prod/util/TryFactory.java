package frechsack.prod.util;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

class TryFactory {

   static class Present<Type> implements Try<Type> {

        private final Type value;

        public Present(Type type) {
            this.value = value;
        }

        @Override
        public Type get(){
            return value;
        }

        @Override
        public Exception getError() {
            throw new IllegalStateException(); 
        }

        @Override
        public boolean isPresent() {
            return true;
        }
   }

   static class Error implements Try<Type> {

        private final Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        @Override
        public Type get(){
            throw new IllegalStateException(); 
        }

        @Override
        public Exception getError() {
            return error;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

   }
}
