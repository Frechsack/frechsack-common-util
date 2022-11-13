package frechsack.prod.util.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MultiMap<Key, Value> {

    Value getOne(Key key);

    @NotNull Collection<Value> get(Key key);

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    @NotNull Collection<Value> remove(Object key);

    @NotNull Collection<Value> values();

    @NotNull Collection<Value> put(Key key, Collection<Value> value);

    @NotNull Set<Key> keySet();

    @NotNull Set<Entry<Key, Value>> entrySet();

    interface Entry<Key, Value> {

        Key getKey();

        default Value getOne(){
            Collection<Value> values = get();
            return values.stream().findFirst().orElse(null);
        }

        @NotNull Collection<Value> get();

        default void add(Collection<Value> values){
            get().addAll(values);
        }

        default Collection<Value> set(Collection<Value> value){
            Collection<Value> removed = new ArrayList<>(get());
            get().clear();
            get().addAll(value);
            return removed;
        }
    }

}
