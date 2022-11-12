package frechsack.prod.util.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;


class ComputedListFactory {

    static class ComputedRandomAccessList<Type> extends AbstractList<Type> implements List<Type>, RandomAccess {

        private final IntFunction<Type> getter;

        private final IntSupplier size;


        public ComputedRandomAccessList(@NotNull IntFunction<Type> getter) {
            this(getter, true);
        }

        public ComputedRandomAccessList(@NotNull IntFunction<Type> getter, boolean isBuffered) {
            this(getter, () -> Integer.MAX_VALUE, isBuffered);
        }

        public ComputedRandomAccessList(@NotNull IntFunction<Type> getter, @NotNull IntSupplier size, boolean isBuffered) {
            this.size = Objects.requireNonNull(size);
            this.getter = isBuffered
                    ? new BufferedFunction(Objects.requireNonNull(getter))
                    : Objects.requireNonNull(getter);
        }

        @Override
        public Type get(int index) {
            return getter.apply(index);
        }

        @Override
        public int size() {
            return size.getAsInt();
        }

        private class BufferedFunction implements IntFunction<Type> {

            private BufferedFunction(@NotNull IntFunction<Type> getter) {
                this.getter = getter;
            }

            @Override
            public Type apply(int index) {
                Holder<Type> holder = getHolder(index);
                return holder == null ? compute(index) : holder.value;
            }

            private record Holder<Out>(Out value) {}

            private final Map<Integer, Reference<Holder<Type>>> buffer = new HashMap<>();

            private final IntFunction<Type> getter;

            private @Nullable Holder<Type> getHolder(int index){
                Reference<Holder<Type>> reference = buffer.get(index);
                return reference != null && !reference.refersTo(null)
                        ? reference.get()
                        : null;
            }

            private synchronized Type compute(int index){
                // Check again, a different Thread may have accessed the Function already
                Holder<Type> holder = getHolder(index);
                if(holder != null)
                    return holder.value;

                Type value = getter.apply(index);
                buffer.put(index,new SoftReference<>(new Holder<>(value)));
                return value;
            }
        }
    }
}
