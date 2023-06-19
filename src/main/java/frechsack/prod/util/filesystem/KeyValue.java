package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface KeyValue<Key> {

    static KeyValue<String> parseJSON(String text){
        return JSONKeyValueFactoryNew.parseJSON(text);
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

    int getInt(Key key);

    double getDouble(Key key);

    long getLong(Key key);

    @NotNull String getString(Key key);

    boolean getBoolean(Key key);

    <KeyType> KeyValue<KeyType> getValues(Key key, Class<KeyType> keyType);

    KeyValue<Integer> getArray(Key key);

    boolean containsKey(Key key);

    boolean isArray(Key key);

    boolean isKeyValue(Key key);

    Stream<Tuple<Key, Object>> stream();

    Stream<Key> streamKeys();
}
