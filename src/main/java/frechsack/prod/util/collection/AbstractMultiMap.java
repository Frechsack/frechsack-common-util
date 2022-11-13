package frechsack.prod.util.collection;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractMultiMap<Key, Value> implements MultiMap<Key, Value> {

    protected abstract Map<Key, Collection<Value>> getModel();

    private class Accessor extends AbstractCollection<Value> {

        private final Key key;

        private Accessor(Key key) {
            this.key = key;
        }

        @Override
        public boolean add(Value value) {
            Collection<Value> values = getModel().get(key);
            if(values == null)
                getModel().put(key, new ArrayList<>(List.of(value)));
            else
                values.add(value);
            return true;
        }

        @Override
        public Iterator<Value> iterator() {
            Collection<Value> values = getModel().get(key);
            return values == null
                    ? Spliterators.iterator(Spliterators.emptySpliterator())
                    : values.iterator();
        }

        @Override
        public int size() {
            Collection<Value> values = getModel().get(key);
            return values == null ? 0 : values.size();
        }
    }

    private Reference<EntrySet> entrySetRef;

    protected Stream<Value> streamValues(){
        return getModel().values().stream()
                .flatMap(Collection::stream);
    }

    @Override
    public Value getOne(Key key) {
        Collection<Value> values = getModel().get(key);
        if(values == null)
            return null;
        return values.stream().findFirst().orElse(null);
    }

    @Override
    public @NotNull Collection<Value> get(Key key) {
        Collection<Value> values = getModel().get(key);
        return values == null
                ? new Accessor(key)
                : values;
    }

    @Override
    public boolean containsKey(Object key) {
        Collection<Value> values = getModel().get(key);
        return values != null && !values.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return streamValues()
                .anyMatch(it -> Objects.equals(it, value));
    }

    @Override
    public @NotNull Collection<Value> values() {
        return streamValues().toList();
    }

    @Override
    public @NotNull Set<Key> keySet() {
        return getModel().keySet();
    }

    @Override
    public @NotNull Collection<Value> put(Key key, Collection<Value> values) {
        Collection<Value> last = getModel().put(key, new ArrayList<>(values));
        return last == null ? List.of() : last;
    }

    @Override
    public @NotNull Set<MultiMap.Entry<Key, Value>> entrySet() {
        EntrySet entrySet = entrySetRef == null
                ? null
                : entrySetRef.get();
        if(entrySet == null)
            entrySetRef = new SoftReference<>(entrySet = new EntrySet());
        return entrySet;
    }

    @Override
    public @NotNull Collection<Value> remove(Object key) {
        Collection<Value> last = getModel().remove(key);
        return last == null ? List.of() : last;
    }

    class EntrySet extends AbstractSet<MultiMap.Entry<Key, Value>> {

        @Override
        public boolean add(MultiMap.Entry<Key, Value> entry) {
            Key key = Objects.requireNonNull(entry).getKey();
            Collection<Value> values = Objects.requireNonNull(entry.get());
            new Entry(key).add(values);
            return true;
        }

        @Override
        public Iterator<MultiMap.Entry<Key,Value>> iterator() {
            return AbstractMultiMap.this.getModel().keySet().stream()
                    .map(this::ofKey).iterator();
        }

        @Override
        public boolean remove(Object o) {
            return AbstractMultiMap.this.remove(o).size() > 0;
        }

        private MultiMap.Entry<Key, Value> ofKey(Key key){
            return new Entry(key);
        }

        @Override
        public int size() {
            return AbstractMultiMap.this.keySet().size();
        }
    }

    class Entry implements MultiMap.Entry<Key, Value> {

        private final Key key;
        private class Accessor extends AbstractSet<Value> {

            @Override
            public @NotNull Iterator<Value> iterator() {
                Collection<Value> values = AbstractMultiMap.this.getModel().get(key);
                return values == null
                        ? Spliterators.iterator(Spliterators.emptySpliterator())
                        : values.iterator();
            }

            @Override
            public boolean add(Value value) {
                Collection<Value> values = AbstractMultiMap.this.getModel().get(key);
                if (values == null)
                    AbstractMultiMap.this.getModel().put(key, new ArrayList<>(List.of(value)));
                else
                    values.add(value);
                return true;
            }

            @Override
            public int size() {
                Collection<Value> values = AbstractMultiMap.this.getModel().get(key);
                return values == null
                        ? 0
                        : values.size();
            }
        }

        private Entry(Key key) {
            this.key = key;
        }

        @Override
        public Key getKey() {
            return key;
        }

        @Override
        public Value getOne() {
            Collection<Value> values = AbstractMultiMap.this.getModel().get(key);
            return values == null || values.isEmpty()
                    ? null
                    : values.stream().findFirst().orElse(null);
        }

        @Override
        public @NotNull Collection<Value> get() {
            Collection<Value> values = AbstractMultiMap.this.getModel().get(key);
            return values == null
                    ? new Accessor()
                    : values;
        }

        @Override
        public void add(Collection<Value> values) {
            Collection<Value> current = AbstractMultiMap.this.getModel().get(key);
            if(current == null)
                AbstractMultiMap.this.getModel().put(key, new ArrayList<>(Objects.requireNonNull(values)));
            else
                current.addAll(values);
        }

        @Override
        public Collection<Value> set(@NotNull Collection<Value> values) {
            return AbstractMultiMap.this.getModel().put(key, new ArrayList<>(Objects.requireNonNull(values)));
        }
    }

}
