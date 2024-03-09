package frechsack.prod.util.concurrent.execute;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class OnceExecutedFunction<InputType, OutputType> implements Function<InputType, OutputType> {

    private final @NotNull ConcurrentHashMap<InstanceHolder<InputType>, InstanceHolder<OutputType>> values = new ConcurrentHashMap<>();
    private final @NotNull Function<InputType, OutputType> function;

    public OnceExecutedFunction(@NotNull Function<InputType, OutputType> function) {
        this.function = Objects.requireNonNull(function);
    }

    public boolean isExecuted(InputType input) {
        return values.containsKey(input);
    }

    public int size() {
        return values.size();
    }

    public void reset() {
        values.clear();
    }

    @Override
    public OutputType apply(InputType input) {
        final var inputHolder = new InstanceHolder<>(input);
        return values.computeIfAbsent(inputHolder, __ -> new InstanceHolder<>(function.apply(inputHolder.value))).value;
    }

    private record InstanceHolder<OutputType>(OutputType value) {
        @Override
        public int hashCode() {
            return value == null ? 0 : value.hashCode();
        }
    }
}
