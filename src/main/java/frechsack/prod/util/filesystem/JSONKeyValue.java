package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class JSONKeyValue implements KeyValue<String> {

    private final Map<String, Object> values = new HashMap<>();

    private static RuntimeException unmatchedTypeException(Class<?> requested, Class<?> actual){
        return new IllegalArgumentException();
    }

    @Override
    public void setNull(@NotNull String key) {
        values.put(key, null);
    }

    @Override
    public void addNull() {
        throw new UnsupportedOperationException("JSON requires a key.");
    }

    @Override
    public void setInt(@NotNull String key, int value) {
        values.put(key, value);
    }

    @Override
    public void addInt(int value) {
        throw new UnsupportedOperationException("JSON requires a key.");
    }

    @Override
    public void setDouble(@NotNull String key, double value) {
        values.put(key, value);
    }

    @Override
    public void addDouble(double value) {
        throw new UnsupportedOperationException("JSON requires a key.");
    }

    @Override
    public void setLong(@NotNull String key, long value) {
        values.put(key, value);
    }

    @Override
    public void addLong(long value) {
        throw new UnsupportedOperationException("JSON requires a key.");
    }

    @Override
    public void setString(@NotNull String key, @NotNull String value) {
        values.put(key, value);
    }

    @Override
    public void addString(@NotNull String value) {
        throw new UnsupportedOperationException("JSON requires a key.");
    }

    @Override
    public void setBoolean(@NotNull String key, boolean value) {
        values.put(key, value);
    }

    @Override
    public void addBoolean(boolean value) {
        throw new UnsupportedOperationException("JSON requires a key.");
    }

    @Override
    public void setValues(@NotNull String key, @NotNull KeyValue<?> values) {
        this.values.put(key, values);
    }

    @Override
    public void addValues(@NotNull KeyValue<?> values) {
        throw new UnsupportedOperationException("JSON requires a key.");
    }

    @Override
    public void setArray(@NotNull String key, @NotNull KeyValue<Integer> values) {
        this.values.put(key, values);
    }

    @Override
    public void addArray(@NotNull KeyValue<Integer> values) {
        throw new UnsupportedOperationException("JSON requires a key.");
    }

    @Override
    public int getInt(String s) {
        if (!containsKey(s))
            throw new NoSuchElementException();

        final var value = this.values.get(s);
        if (this.values.get(s) instanceof Number i)
            return i.intValue();

        throw unmatchedTypeException(Number.class, value.getClass());
    }

    @Override
    public double getDouble(String s) {
        if (!containsKey(s))
            throw new NoSuchElementException();
        final var value = this.values.get(s);
        if (value instanceof Number i)
            return i.doubleValue();

        throw unmatchedTypeException(Number.class, value.getClass());
    }

    @Override
    public long getLong(String s) {
        if (!containsKey(s))
            throw new NoSuchElementException();
        final var value = this.values.get(s);
        if (value instanceof Number i)
            return i.longValue();

        throw unmatchedTypeException(Number.class, value.getClass());
    }

    @Override
    public @NotNull String getString(String s) {
        if (!containsKey(s))
            throw new NoSuchElementException();
        return this.values.get(s).toString();
    }

    @Override
    public boolean getBoolean(String s) {
        if (!containsKey(s))
            throw new NoSuchElementException();
        final var value = this.values.get(s);
        if (value instanceof Boolean b)
            return b;
        if (value == null)
            throw new NoSuchElementException();

        final var possibleBoolean = value.toString().toLowerCase();
        if ("true".equals(possibleBoolean))
            return true;
        if ("false".equals(possibleBoolean))
            return false;

        throw unmatchedTypeException(Number.class, value.getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KeyType> KeyValue<KeyType> getValues(@NotNull String key, @NotNull Class<KeyType> keyType) {
        if (!(keyType == String.class || keyType == Integer.class))
            throw new IllegalArgumentException("JSON allows String or Integer as keys only.");
        if (!containsKey(key))
            throw new NoSuchElementException();
        final var values = this.values.get(key);
        try {
            return (KeyValue<KeyType>) values;
        }
        catch (Exception e){
            throw unmatchedTypeException(KeyValue.class, values.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public KeyValue<Integer> getArray(@NotNull String key) {
        if (!containsKey(key))
            throw new NoSuchElementException();
        final var values = this.values.get(key);
        try {
            return (KeyValue<Integer>) values;
        }
        catch (Exception e){
            throw new IllegalArgumentException("Associated key is not an array.");
        }
    }

    @Override
    public boolean containsKey(String key) {
        return values.containsKey(key);
    }

    @Override
    public boolean isArray(String key) {
        return getArray(key) != null;
    }

    @Override
    public boolean isKeyValue(String key) {
        return getValues(key, String.class) != null;
    }

    @Override
    public Stream<Tuple<String, Object>> stream() {
        return values.entrySet().stream()
                .map(it -> Tuple.of(it.getKey(), it.getValue()));
    }

    @Override
    public Stream<String> streamKeys() {
        return values.keySet().stream();
    }

}
