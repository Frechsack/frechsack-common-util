package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IndexedValue implements KeyValue<Integer> {

    private final ArrayList<Object> elements = new ArrayList<>();

    private static RuntimeException unmatchedTypeException(Class<?> actual){
        return new IllegalArgumentException();
    }

    @Override
    public void setNull(@NotNull Integer integer) {
        elements.set(integer, null);
    }

    @Override
    public void addNull() {
        elements.add(null);
    }

    @Override
    public void setInt(@NotNull Integer integer, int value) {
        elements.set(integer, value);
    }

    @Override
    public void addInt(int value) {
        elements.add(value);
    }

    @Override
    public void setDouble(@NotNull Integer integer, double value) {
        elements.set(integer, value);
    }

    @Override
    public void addDouble(double value) {
        elements.add(value);
    }

    @Override
    public void setLong(@NotNull Integer integer, long value) {
        elements.set(integer, value);
    }

    @Override
    public void addLong(long value) {
        elements.add(value);
    }

    @Override
    public void setString(@NotNull Integer integer, @Nullable String value) {
        elements.set(integer, value);
    }

    @Override
    public void addString(@NotNull String value) {
        elements.add(value);
    }

    @Override
    public void setBoolean(@NotNull Integer integer, boolean value) {
        elements.set(integer, value);
    }

    @Override
    public void addBoolean(boolean value) {
        elements.add(value);
    }

    @Override
    public void setValues(@NotNull Integer integer, @NotNull KeyValue<?> values) {
        elements.set(integer, values);
    }

    @Override
    public void addValues(@NotNull KeyValue<?> values) {
        elements.add(values);
    }

    @Override
    public void setArray(@NotNull Integer integer, @NotNull KeyValue<Integer> values) {
        elements.set(integer, values);
    }

    @Override
    public void addArray(@NotNull KeyValue<Integer> values) {
        elements.add(values);
    }

    @Override
    public int getInt(Integer integer) {
        Objects.checkIndex(integer, elements.size());
        final var value = elements.get(integer);
        if (value instanceof Number n)
            return n.intValue();
        throw unmatchedTypeException(value.getClass());
    }

    @Override
    public double getDouble(Integer integer) {
        Objects.checkIndex(integer, elements.size());
        final var value = elements.get(integer);
        if (value instanceof Number n)
            return n.doubleValue();
        throw unmatchedTypeException(value.getClass());
    }

    @Override
    public long getLong(Integer integer) {
        Objects.checkIndex(integer, elements.size());
        final var value = elements.get(integer);
        if (value instanceof Number n)
            return n.longValue();
        throw unmatchedTypeException(value.getClass());
    }

    @Override
    public @NotNull String getString(Integer integer) {
        Objects.checkIndex(integer, elements.size());
        final var value = elements.get(integer);
        if (value != null)
            return value.toString();
        throw new NoSuchElementException();
    }

    @Override
    public boolean getBoolean(Integer integer) {
        Objects.checkIndex(integer, elements.size());
        final var value = this.elements.get(integer);
        if (value instanceof Boolean b)
            return b;
        if (value == null)
            throw new NoSuchElementException();

        final var possibleBoolean = value.toString().toLowerCase();
        if ("true".equals(possibleBoolean))
            return true;
        if ("false".equals(possibleBoolean))
            return false;

        throw unmatchedTypeException(value.getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KeyType> KeyValue<KeyType> getValues(Integer integer, Class<KeyType> keyType) {
        Objects.checkIndex(integer, elements.size());
        final var value = elements.get(integer);
        if (value instanceof KeyValue<?> kv)
            return (KeyValue<KeyType>) kv;
        throw unmatchedTypeException(value.getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public KeyValue<Integer> getArray(Integer integer) {
        Objects.checkIndex(integer, elements.size());
        final var value = elements.get(integer);
        if (value instanceof KeyValue<?> kv)
            return (KeyValue<Integer>) kv;
        throw unmatchedTypeException(value.getClass());
    }


    @Override
    public boolean containsKey(@NotNull Integer integer) {
        return elements.size() > integer;
    }

    @Override
    public boolean isArray(@NotNull Integer integer) {
        Objects.checkIndex(integer, elements.size());
        final var value = elements.get(integer);
        try {
            @SuppressWarnings("unchecked")
            final var toCheck = (KeyValue<Integer>) value;
            return true;
        }
        catch (ClassCastException e){
            return false;
        }
    }

    @Override
    public boolean isKeyValue(@NotNull Integer integer) {
        Objects.checkIndex(integer, elements.size());
        final var type = elements.get(integer).getClass();
        return !(type == Boolean.class || type == String.class || type == Double.class
                || type == Long.class || type == Integer.class || type == Float.class
                || type == Short.class || type == Byte.class);
    }

    @Override
    public Stream<Tuple<Integer, Object>> stream() {
        final IntSupplier index = new IntSupplier() {
            int index = 0;
            @Override
            public int getAsInt() {
                return index++;
            }
        };
        return elements.stream().map(element -> Tuple.of(index.getAsInt(), element));
    }

    @Override
    public Stream<Integer> streamKeys() {
       return IntStream.range(0, elements.size()).boxed();
    }
}
