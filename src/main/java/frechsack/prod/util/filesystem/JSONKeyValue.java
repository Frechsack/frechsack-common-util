package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSONKeyValue implements KeyValue<String> {

    private final Map<String, Object> values = new HashMap<>();

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
    public int getInt(String key, boolean isConversion) {
        final var value = values.get(key);
        if (value == null)
            throw new NoSuchElementException();
        if (value instanceof Integer i)
            return i;
        if (value instanceof Long l)
            return l.intValue();
        if (!isConversion)
            throw new IllegalArgumentException();
        if (value instanceof Number n)
            return n.intValue();
        final var str = value.toString();

        if (str.indexOf('.') == -1)
            try {
                return Integer.parseInt(str);
            }
            catch (NumberFormatException ignored){
            }

        throw new IllegalArgumentException();
    }

    @Override
    public double getDouble(String key, boolean isConversion) {
        final var value = values.get(key);
        if (value == null)
            throw new NoSuchElementException();
        if (value instanceof Double d)
            return d;
        if (!isConversion)
            throw new IllegalArgumentException();
        if (value instanceof Number n)
            return n.doubleValue();
        final var str = value.toString();

        try {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException ignored){
        }

        throw new IllegalArgumentException();
    }

    @Override
    public long getLong(String key, boolean isConversion) {
        final var value = values.get(key);
        if (value == null)
            throw new NoSuchElementException();
        if (value instanceof Long l)
            return l;
        if (value instanceof Integer i)
            return i.longValue();
        if (!isConversion)
            throw new IllegalArgumentException();
        if (value instanceof Number n)
            return n.intValue();
        final var str = value.toString();

        if (str.indexOf('.') == -1)
            try {
                return Integer.parseInt(str);
            }
            catch (NumberFormatException ignored){
            }

        throw new IllegalArgumentException();
    }

    @Override
    public @NotNull String getString(String key, boolean isConversion) {
        final var value = values.get(key);
        if (value == null)
            throw new NoSuchElementException();
        if (value instanceof String s)
            return s;
        if (!isConversion)
            throw new IllegalArgumentException();
       return value.toString();
    }

    @Override
    public boolean getBoolean(String key, boolean isConversion) {
        final var value = values.get(key);
        if (value == null)
            throw new NoSuchElementException();
        if (value instanceof Boolean b)
            return b;
        if (!isConversion)
            throw new IllegalArgumentException();
        if (value instanceof String s){
            if (s.equalsIgnoreCase("false"))
                return false;
            else if (s.equalsIgnoreCase("true"))
                return true;
        }
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <KeyType> @NotNull KeyValue<KeyType> getValues(@NotNull String key, @NotNull Class<KeyType> keyType) {
        if (!(keyType == String.class || keyType == Integer.class))
            throw new IllegalArgumentException("JSON allows String or Integer as keys only.");
        final var values = this.values.get(key);
        if (values == null)
            throw new NoSuchElementException();
        try {
            return (KeyValue<KeyType>) values;
        }
        catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull KeyValue<Integer> getArray(@NotNull String key) {
        final var values = this.values.get(key);
        if (values == null)
            throw new NoSuchElementException();
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
        final var values = this.values.get(key);
        if (values == null)
            return false;
        try {
            @SuppressWarnings("unchecked")
            final var e = (KeyValue<Integer>) values;
            return true;
        }
        catch (ClassCastException ignored){
            return false;
        }

    }

    @Override
    public boolean isKeyValue(String key) {
        final var values = this.values.get(key);
        return values instanceof KeyValue<?>;
    }

    @Override
    public boolean isNull(String key) {
        final var values = this.values.get(key);
        return values == null;
    }

    @Override
    public boolean isLong(String key, boolean isConversion) {
        try {
            getLong(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean isDouble(String key, boolean isConversion) {
        try {
            getDouble(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean isNumber(String key, boolean isConversion) {
        try {
            return isLong(key, isConversion) || isDouble(key, isConversion);
        }
        catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean isString(String key, boolean isConversion) {
        try {
           getString(key, isConversion);
           return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean isBoolean(String key, boolean isConversion) {
        try {
            getString(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
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

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(values.keySet());
    }

    @Override
    public String toString() {
        final var entries = streamKeys().sorted().map(key -> {
            final var entryBuilder = new StringBuilder();
            entryBuilder.append('"').append(key).append('"').append(':');
            if (isNull(key))
                entryBuilder.append("null");
            else if(isDouble(key, false))
                entryBuilder.append(getDouble(key, true));
            else if (isLong(key, true))
                entryBuilder.append(getLong(key, true));
            else if (isString(key, false))
                entryBuilder.append('"').append(getString(key, false)).append('"');
            else if (isArray(key))
                entryBuilder.append(getArray(key));
            else if (isKeyValue(key))
                entryBuilder.append(getArray(key));
            else if (isBoolean(key))
                entryBuilder.append(getBoolean(key));
            else
                throw new IllegalStateException();
            return entryBuilder.toString();
        }).collect(Collectors.joining(","));
        return '{' +
                entries +
                '}';
    }
}
