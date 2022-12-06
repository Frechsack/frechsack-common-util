package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
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
                    holder = new Holder();
                    handler.put(input, holder);
                }
            }
        return holder.getSync(input);
    }

    private class Holder {

        private Output value;

        private CountDownLatch synchronizer;


        private synchronized void compute(Input input){
            if(synchronizer != null) return;
            synchronizer = new CountDownLatch(1);
            value = function.apply(input);
            synchronizer.countDown();
        }

        private Output getSync(Input input) {
            if(synchronizer != null)
                try {
                    synchronizer.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            else
                compute(input);
            return value;
        }
    }
}
