package frechsack.prod.util.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public interface MultiMap<Key, Value> {

    /**
     * Returns every value associated with the specified key. Modification of the returned Collection will modify this Map.
     * If the key is not present an empty Collection will be returned, which in case of modification will modify this Map.
     * @param key The key.
     * @return Returns a Collection with the elements associated with the key.
     */
    @NotNull Collection<Value> get(Key key);

    /**
     * Returns a Set with the key-value mappings in this Map.
     * Modification of this Set will modify this Map.
     * @return Returns the Set.
     */
    @NotNull Set<Entry<Key, Value>> entrySet();

    /**
     * Removes a key from this Map.
     * @param key The key.
     * @return Returns the associated elements with the key.
     */
    @NotNull Collection<Value> remove(Object key);

    /**
     * Removes any mapping from this Map.
     */
    void clear();

    /**
     * Returns the count of mappings (amount of keys) in this Map.
     * @return Returns the amount of keys.
     */
    default int size(){
        return (int) streamKeys().count();
    }

    /**
     * Returns the amount of elements in this Map.
     * @return Returns the amount of elements.
     */
    default int valueCount(){
        return (int) streamValues().count();
    }

    /**
     * Returns a Collection with any element in this Map.
     * The Collection allows to remove elements from this Map but not adding them.
     * @return Returns the Collection.
     */
    @NotNull Collection<Value> values();

    /**
     * Returns a Set with any key in this Map.
     * The Set allows to remove keys from this Map but not adding them.
     * @return Returns the Set.
     */
    @NotNull Set<Key> keySet();

    default @NotNull Stream<Value> stream(Key key){
        return get(key).stream();
    }

    default @NotNull Stream<Value> streamValues(){
        return entrySet().stream().flatMap(it -> it.get().stream());
    }

    default @NotNull Stream<Key> streamKeys(){
        return entrySet().stream().map(Entry::getKey);
    }

    default boolean containsKey(Object key){
        return keySet().contains(key);
    }

    default boolean containsValue(Object value){
        return values().contains(value);
    }

    default Value getOne(Key key){
        return get(key).stream().findFirst().orElse(null);
    }

    default void addAll(Key key, Collection<Value> values){
        get(key).addAll(Objects.requireNonNull(values));
    }

    default void add(Key key, Value value){
        get(key).add(value);
    }

    default @NotNull Collection<Value> put(Key key, Collection<Value> value){
        Collection<Value> current = get(key);
        Collection<Value> last = new ArrayList<>(current);
        current.clear();
        current.addAll(value);
        return last;
    }

    interface Entry<Key, Value> {

        Key getKey();

        @NotNull Collection<Value> get();

        @NotNull Collection<Value> remove();

        default Value getOne(){
            Collection<Value> values = get();
            return values.stream().findFirst().orElse(null);
        }

        default void addAll(Collection<Value> values){
            get().addAll(values);
        }

        default void add(Value value){
            get().add(value);
        }

        default @NotNull Collection<Value> put(Collection<Value> value){
            Collection<Value> removed = new ArrayList<>(get());
            get().clear();
            get().addAll(value);
            return removed;
        }
    }

}
