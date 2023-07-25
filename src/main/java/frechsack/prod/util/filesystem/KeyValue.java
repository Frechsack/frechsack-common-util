package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public interface KeyValue<Key> {

    static KeyValue<String> parseJSON(String text, boolean isAsync){
        return JSONKeyValueFactory.parseObject(text, isAsync);
    }

    static <Key> KeyValue<Key> synchronizedKeyValue(@NotNull KeyValue<Key> keyValue){
        return keyValue instanceof KeyValueFactory.SynchronizedKeyValue<Key>
                ? keyValue
                : new KeyValueFactory.SynchronizedKeyValue<>(keyValue);
    }


    void setNull(Key key);

    void addNull();

    void setInt(Key key, int value);

    void addInt(int value);

    void setDouble(Key key, double value);

    void addDouble(double value);

    void setLong(Key key, long value);

    void addLong(long value);

    void setString(Key key, @NotNull String value);

    void addString(@NotNull String value);

    void setBoolean(Key key, boolean value);

    void addBoolean(boolean value);

    void setValues(Key key, @NotNull KeyValue<?> values);

    void addValues(@NotNull KeyValue<?> values);

    void setArray(Key key, @NotNull KeyValue<Integer> values);

    void addArray(@NotNull KeyValue<Integer> values);

    default int getInt(Key key) {
        return getInt(key, true);
    }

    int getInt(Key key, boolean isConversion);

    default double getDouble(Key key) {
        return getDouble(key, true);
    }

    double getDouble(Key key, boolean isConversion);

    default long getLong(Key key) {
        return getLong(key, true);
    }

    long getLong(Key key, boolean isConversion);

    default @NotNull String getString(Key key) {
        return this.getString(key, true);
    }

    @NotNull String getString(Key key, boolean isConversion);

    default boolean getBoolean(Key key) {
        return this.getBoolean(key, true);
    }

    boolean getBoolean(Key key, boolean isConversion);

    void getNull(Key key);

    @NotNull <KeyType> KeyValue<KeyType> getKeyValue(Key key, Class<KeyType> keyType);

    default @NotNull KeyValue<Integer> getArray(Key key) {
        return getKeyValue(key, Integer.class);
    }

    boolean containsKey(Key key);

    default boolean isArray(Key key) {
        try {
            getArray(key);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    default boolean isKeyValue(Key key, Class<?> type) {
        try {
            getKeyValue(key, type);
            return true;
        }
        catch (Exception ignored) {
            return false;
        }
    }

    default boolean isNull(Key key) {
        try {
            getNull(key);
            return true;
        }
        catch (Exception ignored) {
            return false;
        }
    }

    default boolean isLong(Key key) {
        return isLong(key, true);
    }

    default boolean isLong(Key key, boolean isConversion) {
        try {
            getLong(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    default boolean isDouble(Key key) {
        return isDouble(key, true);
    }

    default boolean isDouble(Key key, boolean isConversion) {
        try {
            getDouble(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    default boolean isNumber(Key key) {
        return isNumber(key, true);
    }

    default boolean isNumber(Key key, boolean isConversion) {
        try {
            getLong(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    default boolean isString(Key key) {
        return isString(key, true);
    }

    default boolean isString(Key key, boolean isConversion) {
        try {
            getString(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    default boolean isBoolean(Key key) {
        return isBoolean(key, true);
    }

    default boolean isBoolean(Key key, boolean isConversion) {
        try {
            getBoolean(key, isConversion);
            return true;
        }
        catch (Exception ignored){
            return false;
        }
    }

    Stream<Tuple<Key, Object>> stream();

    Stream<Key> streamKeys();

    Set<Key> keys();
}
