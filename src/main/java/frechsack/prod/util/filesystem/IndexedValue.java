package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
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
    public int getInt(Integer key, boolean isConversion) {
        Objects.checkIndex(key, elements.size());
        final var value = this.elements.get(key);
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
    public double getDouble(Integer key, boolean isConversion) {
        Objects.checkIndex(key, elements.size());
        final var value = this.elements.get(key);
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
    public long getLong(Integer key, boolean isConversion) {
        Objects.checkIndex(key, elements.size());
        final var value = this.elements.get(key);
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
    public @NotNull String getString(Integer key, boolean isConversion) {
        Objects.checkIndex(key, elements.size());
        final var value = this.elements.get(key);
        if (value == null)
            throw new NoSuchElementException();
        if (value instanceof String s)
            return s;
        if (!isConversion)
            throw new IllegalArgumentException();
        return value.toString();
    }

    @Override
    public boolean getBoolean(Integer key, boolean isConversion) {
        Objects.checkIndex(key, elements.size());
        final var value = this.elements.get(key);
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
    public <KeyType> @NotNull KeyValue<KeyType> getValues(@NotNull Integer key, @NotNull Class<KeyType> keyType) {
        if (!(keyType == String.class || keyType == Integer.class))
            throw new IllegalArgumentException("JSON allows String or Integer as keys only.");
        Objects.checkIndex(key, elements.size());
        final var values = this.elements.get(key);
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
    public @NotNull KeyValue<Integer> getArray(@NotNull Integer key) {
        Objects.checkIndex(key, elements.size());
        final var values = this.elements.get(key);
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
    public boolean containsKey(Integer key) {
        return key < elements.size();
    }

    @Override
    public boolean isArray(Integer key) {
        final var values = elements.get(key);
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
    public boolean isKeyValue(Integer key) {
        final var values = elements.get(key);
        return values instanceof KeyValue<?>;
    }

    @Override
    public boolean isNull(Integer key) {
        final var values = elements.get(key);
        return values == null;
    }

    @Override
    public boolean isLong(Integer key, boolean isConversion) {
        try {
            getLong(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean isDouble(Integer key, boolean isConversion) {
        try {
            getDouble(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean isNumber(Integer key, boolean isConversion) {
        try {
            return isLong(key, isConversion) || isDouble(key, isConversion);
        }
        catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean isString(Integer key, boolean isConversion) {
        try {
            getString(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }


    @Override
    public boolean isBoolean(Integer key, boolean isConversion) {
        try {
            getBoolean(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
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

    @Override
    public Set<Integer> keys() {
        return IntStream.range(0, elements.size()).boxed().collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        final var entries = streamKeys().sorted().map(key -> {
            final var entryBuilder = new StringBuilder();
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
        return '[' +
                entries +
                ']';
    }
}
