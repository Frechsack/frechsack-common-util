package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertiesKeyValue implements KeyValue<String> {

    private final Properties properties;

    public PropertiesKeyValue(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void setNull(@NotNull String o) {
        throw new UnsupportedOperationException("Properties do not support null values.");
    }

    @Override
    public void addNull() {
        throw new UnsupportedOperationException("Properties do not support null values.");
    }

    @Override
    public void setInt(@NotNull String o, int value) {
        properties.setProperty(o, Integer.toString(value));
    }

    @Override
    public void addInt(int value) {
        throw new UnsupportedOperationException("Properties require a key.");
    }

    @Override
    public void setDouble(@NotNull String o, double value) {
        properties.setProperty(o, Double.toString(value));
    }

    @Override
    public void addDouble(double value) {
        throw new UnsupportedOperationException("Properties require a key.");
    }

    @Override
    public void setLong(@NotNull String o, long value) {
        properties.setProperty(o, Long.toString(value));
    }

    @Override
    public void addLong(long value) {
        throw new UnsupportedOperationException("Properties require a key.");
    }

    @Override
    public void setString(@NotNull String o, @NotNull String value) {
        properties.setProperty(o, value);
    }

    @Override
    public void addString(@NotNull String value) {
        throw new UnsupportedOperationException("Properties require a key.");
    }

    @Override
    public void setBoolean(@NotNull String o, boolean value) {
        properties.setProperty(o, Boolean.toString(value));
    }

    @Override
    public void addBoolean(boolean value) {
        throw new UnsupportedOperationException("Properties require a key.");
    }

    @Override
    public void setValues(@NotNull String o, @NotNull KeyValue<?> values) {
        throw new UnsupportedOperationException("Properties do not allow nested values.");
    }

    @Override
    public void addValues(@NotNull KeyValue<?> values) {
        throw new UnsupportedOperationException("Properties require a key.");
    }

    @Override
    public void setArray(@NotNull String o, @NotNull KeyValue<Integer> values) {
        throw new UnsupportedOperationException("Properties do not allow nested values.");
    }

    @Override
    public void addArray(@NotNull KeyValue<Integer> values) {
        throw new UnsupportedOperationException("Properties require a key.");
    }

    @Override
    public int getInt(String key, boolean isConversion) {
        final var value = properties.getProperty(key);
        if (value == null)
            throw new NoSuchElementException();
        try {
            return Integer.parseInt(value);
        }
        catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public double getDouble(String key, boolean isConversion) {
        final var value = properties.getProperty(key);
        if (value == null)
            throw new NoSuchElementException();
        try {
            return Double.parseDouble(value);
        }
        catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public long getLong(String key, boolean isConversion) {
        final var value = properties.getProperty(key);
        if (value == null)
            throw new NoSuchElementException();
        try {
            return Long.parseLong(value);
        }
        catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public @NotNull String getString(String key, boolean isConversion) {
        final var value = properties.getProperty(key);
        if (value == null)
            throw new NoSuchElementException();
        return value;
    }

    @Override
    public boolean getBoolean(String key, boolean isConversion) {
        final var value = properties.getProperty(key);
        if (value == null)
            throw new NoSuchElementException();
        try {
            return Boolean.parseBoolean(value);
        }
        catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void getNull(String key) {
        throw new UnsupportedOperationException("Properties do not support null values.");
    }

    @Override
    public @NotNull <KeyType> KeyValue<KeyType> getKeyValue(String key, Class<KeyType> keyType) {
        throw new UnsupportedOperationException("Properties do not allow nested values.");
    }

    @Override
    public boolean containsKey(String key) {
        return properties.getProperty(key) != null;
    }

    @Override
    public Stream<Tuple<String, Object>> stream() {
        return properties.entrySet().stream().map(entry -> Tuple.of((String) entry.getKey(), entry.getValue()));
    }

    @Override
    public Stream<String> streamKeys() {
        return properties.keySet().stream().map(it -> (String) it);
    }

    @Override
    public Set<String> keys() {
        return streamKeys().collect(Collectors.toSet());
    }

}
