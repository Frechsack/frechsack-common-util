package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public interface KeyValue<Key> {

    static KeyValue<String> parseJSON(String text){
        return JSONKeyValueFactory.parseObject(text);
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

    @NotNull <KeyType> KeyValue<KeyType> getValues(Key key, Class<KeyType> keyType);

    @NotNull KeyValue<Integer> getArray(Key key);

    boolean containsKey(Key key);

    boolean isArray(Key key);

    boolean isKeyValue(Key key);

    boolean isNull(Key key);

    default boolean isLong(Key key) {
        return isLong(key, true);
    }

    boolean isLong(Key key, boolean isConversion);

    default boolean isDouble(Key key) {
        return isDouble(key, true);
    }

    boolean isDouble(Key key, boolean isConversion);

    default boolean isNumber(Key key) {
        return isNumber(key, true);
    }

    boolean isNumber(Key key, boolean isConversion);

    default boolean isString(Key key) {
        return isString(key, true);
    }

    boolean isString(Key key, boolean isConversion);

    default boolean isBoolean(Key key) {
        return isBoolean(key, true);
    }

    boolean isBoolean(Key key, boolean isConversion);

    Stream<Tuple<Key, Object>> stream();

    Stream<Key> streamKeys();

    Set<Key> keys();
}
