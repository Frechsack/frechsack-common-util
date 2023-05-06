package frechsack.prod.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

public class Operators {

    private Operators() {}

    @SafeVarargs
    public static <Type> Type coalesce(Type... values){
        return coalesce(Objects::nonNull, values);
    }

    @SafeVarargs
    public static <Type> Type coalesce(Predicate<Type> predicate, Type... values){
        for (Type value : values)
            if (predicate.test(value))
                return value;
        return null;
    }

    @SafeVarargs
    public static <Out> Out coalesce(Supplier<Out>... suppliers){
        Out value = null;
        for (Supplier<Out> supplier : suppliers)
            if(supplier != null)
                if((value = supplier.get()) != null)
                    break;
        return value;
    }

    public static int coalesce(int notToMatch, IntSupplier... suppliers){
        int value = notToMatch;
        for (IntSupplier supplier : suppliers)
            if(supplier != null)
                if((value = supplier.getAsInt()) != notToMatch)
                    break;
        return value;
    }

    public static int coalesce(IntSupplier... suppliers){
        return coalesce(0,suppliers);
    }

    public static double coalesce(double notToMatch, DoubleSupplier... suppliers){
        double value = notToMatch;
        for (DoubleSupplier supplier : suppliers)
            if(supplier != null)
                if((value = supplier.getAsDouble()) != notToMatch)
                    break;
        return value;
    }

    public static double coalesce(DoubleSupplier... suppliers){
        return coalesce(0,suppliers);
    }

    public static long coalesce(long notToMatch, LongSupplier... suppliers){
        long value = notToMatch;
        for (LongSupplier supplier : suppliers)
            if(supplier != null)
                if((value = supplier.getAsLong()) != notToMatch)
                    break;
        return value;
    }

    public static long coalesce(LongSupplier... suppliers){
        return coalesce(0,suppliers);
    }

    /**
     * Executes the given action. If the action throws an exception, the action will be called again the given amount of times.
     * This function will throw the last thrown exception raised by the action.
     * @param action The action to be called.
     * @param retry The amount of retries.
     */
    public static void retryRun(@NotNull Runnable action, int retry) {
        do {
            try {
                action.run();
                return;
            }
            catch (RuntimeException e){
                if (retry-- == 0)
                    throw e;
            }

        } while (true);
    }

    /**
     * Executes the given action asynchronous. If the action throws an exception, the action will be called again the given amount of times.
     * This function will throw the last thrown exception raised by the action.
     * @param action The action to be called.
     * @param retry The amount of retries.
     */
    public static CompletableFuture<Void> retryRunAsync(@NotNull Runnable action, int retry) {
        CompletableFuture<Void> actionFuture = CompletableFuture.runAsync(action);
        for (int i = 0; i < retry; i++)
            actionFuture = actionFuture.exceptionallyAsync(__ -> { action.run(); return null; });
        return actionFuture;
    }

    /**
     * Executes the given action asynchronous. If the action throws an exception, the action will be called again the given amount of times.
     * This function will throw the last thrown exception raised by the action.
     * @param action The action to be called.
     * @param retry The amount of retries.
     * @param delayMs The delay between the retries.
     */
    public static CompletableFuture<Void> retryRunAsync(@NotNull Runnable action, int retry, int delayMs) {
        CompletableFuture<Void> actionFuture = CompletableFuture.runAsync(action);
        final Executor delayedExecutor = CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS);
        for (int i = 0; i < retry; i++)
            actionFuture = actionFuture.exceptionallyAsync(__ -> {
                action.run();
                return null;
                }, delayedExecutor);
        return actionFuture;
    }

    /**
     * Executes the given supplier. If the action throws an exception, the action will be called again the given amount of times.
     * This function will throw the last thrown exception raised by the action.
     * @param action The action to be called.
     * @param retry The amount of retries.
     */
    public static <E> E retryGet(@NotNull Supplier<E> action, int retry){
        do {
            try {
                return action.get();
            }
            catch (RuntimeException e){
                if (retry-- == 0)
                    throw e;
            }

        } while (true);
    }

    /**
     * Executes the given action asynchronous. If the action throws an exception, the action will be called again the given amount of times.
     * This function will throw the last thrown exception raised by the action.
     * @param action The action to be called.
     * @param retry The amount of retries.
     */
    public static <E> CompletableFuture<E> retryGetAsync(@NotNull Supplier<E> action, int retry){
        CompletableFuture<E> actionFuture = CompletableFuture.supplyAsync(action);
        for (int i = 0; i < retry; i++)
            actionFuture = actionFuture.exceptionallyAsync(__ -> action.get());
        return actionFuture;
    }

    /**
     * Executes the given action asynchronous. If the action throws an exception, the action will be called again the given amount of times.
     * This function will throw the last thrown exception raised by the action.
     * @param action The action to be called.
     * @param retry The amount of retries.
     * @param delayMs The delay between the retries.
     */
    public static <E> CompletableFuture<E> retryGetAsync(@NotNull Supplier<E> action, int retry, int delayMs) {
        CompletableFuture<E> actionFuture = CompletableFuture.supplyAsync(action);
        final Executor delayedExecutor = CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS);
        for (int i = 0; i < retry; i++)
            actionFuture = actionFuture.exceptionallyAsync(__ -> action.get(), delayedExecutor);
        return actionFuture;
    }
}
