package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;

public class ConcurrentFunction<Input, Output> implements Function<Input, Output> {

    private final Function<Input, Output> function;

    private final WeakHashMap<Input, Holder> handler =  new WeakHashMap<>();

    public ConcurrentFunction(@NotNull Function<Input, Output> function) {
        this.function = Objects.requireNonNull(function);
    }

    @Override
    public Output apply(Input input) {
        Holder holder = handler.get(input);
        if(holder == null)
            synchronized (handler) {
                if((holder = handler.get(input)) == null){
                    holder = new Holder(input);
                    handler.put(input, holder);
                }
            }
        return holder.get();
    }

    private class Holder {
        private final ConcurrentRunnable runnable;
        private Output value;

        private Holder(Input input) {
            runnable = new ConcurrentRunnable(() -> value = ConcurrentFunction.this.function.apply(input));
        }

        public Output get(){
            runnable.run();
            return value;
        }
    }
}
