package frechsack.prod.util.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CompletableFutures {

    private CompletableFutures() {}


    private static <Type> @NotNull CompletableFuture<Type> retry(
            @NotNull Supplier<CompletableFuture<Type>> supplier,
            @NotNull Predicate<@NotNull Throwable> tryNext,
            int retryCount,
            int retryCountMax
    ) {
        return supplier.get()
            .exceptionallyCompose(error -> {
                if (!tryNext.test(error) || retryCount >= retryCountMax)
                    return CompletableFuture.failedFuture(error);
                return retry(supplier, tryNext, retryCount + 1, retryCountMax);
            });
    }

    public static <Type> @NotNull CompletableFuture<Type> retry(
            @NotNull Supplier<CompletableFuture<Type>> supplier,
            @NotNull Predicate<@NotNull Throwable> tryNext,
            int retryCountMax
    ) {
        return retry(supplier, tryNext, 0, retryCountMax);
    }

    private static <Type> @NotNull CompletableFuture<Type> retryAsync(
        @NotNull Supplier<CompletableFuture<Type>> supplier,
        @NotNull Predicate<@NotNull Throwable> tryNext,
        int retryCount,
        int retryCountMax,
        @NotNull Executor executor
    ) {
        return supplier.get()
            .exceptionallyComposeAsync(error -> {
                if (!tryNext.test(error) || retryCount >= retryCountMax)
                    return CompletableFuture.failedFuture(error);
                return retryAsync(supplier, tryNext, retryCount + 1, retryCountMax, executor);
            }, executor);
    }

    public static <Type> @NotNull CompletableFuture<Type> retryAsync(
            @NotNull Supplier<CompletableFuture<Type>> supplier,
            @NotNull Predicate<@NotNull Throwable> tryNext,
            int retryCountMax,
            @NotNull Executor executor
    ) {
        return retryAsync(supplier, tryNext, 0, retryCountMax, executor);
    }

    public static <Type> Function<Type, Type> peek(@NotNull Consumer<? super Type> consumer) {
        return value -> {
            consumer.accept(value);
            return value;
        };
    }

    public static <Type> Function<Throwable, Type> peekError(@NotNull Consumer<Throwable> consumer) {
        return error -> {
            consumer.accept(error);
            throw error instanceof RuntimeException re ? re : new RuntimeException(error);
        };
    }
}
