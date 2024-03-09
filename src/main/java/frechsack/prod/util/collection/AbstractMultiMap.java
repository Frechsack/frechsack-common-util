package frechsack.prod.util.collection;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractMultiMap<Key, Value> implements MultiMap<Key, Value> {

    protected abstract Map<Key, Collection<Value>> getModel();
    private Reference<EntrySet> entrySetRef;

    public @NotNull Stream<Value> streamValues(){
        return getModel().values().stream()
                .flatMap(Collection::stream);
    }

    @Override
    public @NotNull Stream<Key> streamKeys() {
        return getModel().keySet().stream();
    }

    @Override
    public @NotNull Stream<Value> stream(Key key) {
        Collection<Value> values = getModel().get(key);
        return values == null
                ? Stream.empty()
                : values.stream();
    }

    @Override
    public @NotNull Collection<Value> get(Key key) {
        Collection<Value> values = getModel().get(key);
        return values == null
                ? new AbsentKey(key)
                : values;
    }

    @Override
    public Value getOne(Key key) {
        Collection<Value> values = getModel().get(key);
        return values == null
                ? null
                : values.stream().findFirst().orElse(null);
    }

    @Override
    public boolean containsKey(Object key) {
        return getModel().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return streamValues().anyMatch(it -> Objects.equals(it, value));
    }

    @Override
    public @NotNull Set<Key> keySet() {
        return getModel().keySet();
    }

    @Override
    public @NotNull Collection<Value> values() {
        return new Values();
    }

    @Override
    public @NotNull Collection<Value> put(Key key, Collection<Value> values) {
        Collection<Value> current = getModel().get(key);
        getModel().put(key, new ArrayList<>(values));
        return current == null
                ? List.of()
                : current;
    }

    @Override
    public void addAll(Key key, Collection<Value> values) {
        Collection<Value> current = getModel().get(key);
        if(current == null)
            getModel().put(key, new ArrayList<>(values));
        else
            current.addAll(values);
    }

    @Override
    public void add(Key key, Value value) {
        Collection<Value> current = getModel().get(key);
        if(current == null)
            getModel().put(key, new ArrayList<>(List.of(value)));
        else
            current.add(value);
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
        Collection<Value> values = getModel().remove(key);
        return values == null
                ? List.of()
                : values;
    }

    @Override
    public void clear() {
        getModel().clear();
    }

    @Override
    public int size() {
        return getModel().size();
    }

    @Override
    public int valueCount() {
        return (int) streamValues().count();
    }

    private class Values extends AbstractCollection<Value> {

        @Override
        public @NotNull Iterator<Value> iterator() {
            return new ValuesIterator();
        }

        @Override
        public int size() {
            return AbstractMultiMap.this.valueCount();
        }

        private class ValuesIterator implements Iterator<Value> {

            private final int expectedKeyCount;
            private final @NotNull Iterator<Key> keyIterator;

            private Iterator<Value> valueIterator;

            private ValuesIterator() {
                this.keyIterator = AbstractMultiMap.this.keySet().iterator();
                this.expectedKeyCount = AbstractMultiMap.this.size();

            }

            private void checkModification(){
                if(expectedKeyCount != AbstractMultiMap.this.size())
                    throw new ConcurrentModificationException();
            }

            @Override
            public void remove() {
                if(valueIterator == null)
                    throw new IllegalStateException("The cursor is offset, call next() first.");
                valueIterator.remove();
            }

            private void moveCursor(){
                if (valueIterator != null && valueIterator.hasNext())
                    return;
                while (keyIterator.hasNext()){
                    var currentKey = keyIterator.next();
                    valueIterator = AbstractMultiMap.this.get(currentKey).iterator();
                    if(valueIterator.hasNext()) return;
                }
            }

            @Override
            public boolean hasNext() {
                checkModification();
                moveCursor();
                return valueIterator.hasNext();
            }

            @Override
            public Value next() {
                checkModification();
                moveCursor();
                return valueIterator.next();

            }
        }
    }

    private class EntrySet extends AbstractSet<MultiMap.Entry<Key, Value>> {

        private class EntrySetIterator implements java.util.Iterator<MultiMap.Entry<Key, Value>> {

            private final Iterator<Key> iterator;

            private EntrySetIterator(Iterator<Key> iterator) {
                this.iterator = iterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public MultiMap.Entry<Key, Value> next() {
                return new EntrySetItem(iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        }

        private class EntrySetItem implements MultiMap.Entry<Key, Value> {

            private final Key key;

            private EntrySetItem(Key key) {
                this.key = key;
            }

            @Override
            public Key getKey() {
                return key;
            }

            @Override
            public @NotNull Collection<Value> get() {
                return AbstractMultiMap.this.get(key);
            }

            @Override
            public @NotNull Collection<Value> remove() {
                return AbstractMultiMap.this.remove(key);
            }

            @Override
            public Value getOne() {
                return AbstractMultiMap.this.getOne(key);
            }

            @Override
            public void addAll(Collection<Value> values) {
                AbstractMultiMap.this.addAll(key, values);
            }

            @Override
            public void add(Value value) {
                AbstractMultiMap.this.add(key, value);
            }

            @Override
            public @NotNull Collection<Value> put(Collection<Value> value) {
                return AbstractMultiMap.this.put(key, value);
            }
        }

        @Override
        public boolean add(MultiMap.Entry<Key, Value> entry) {
            Key key = Objects.requireNonNull(Objects.requireNonNull(entry).getKey());
            Collection<Value> values = Objects.requireNonNull(entry.get());
            AbstractMultiMap.this.addAll(key, values);
            return true;
        }

        @Override
        public @NotNull java.util.Iterator<MultiMap.Entry<Key,Value>> iterator() {
            return new EntrySetIterator(AbstractMultiMap.this.keySet().iterator());
        }

        @Override
        public boolean remove(Object o) {
            if(AbstractMultiMap.this.getModel().containsKey(o))
                AbstractMultiMap.this.getModel().remove(o);
            else if(o instanceof MultiMap.Entry<?,?> entry && AbstractMultiMap.this.getModel().containsKey(entry.getKey())){
                AbstractMultiMap.this.getModel().remove(entry.getKey());
            }
            else
                return false;
            return true;
        }

        @Override
        public int size() {
            return AbstractMultiMap.this.keySet().size();
        }
    }

    private class AbsentKey extends AbstractSet<Value> {

        private final Key key;

        private AbsentKey(Key key) {
            this.key = key;
        }

        @Override
        public @NotNull java.util.Iterator<Value> iterator() {
            return AbstractMultiMap.this.stream(key).iterator();
        }

        @Override
        public boolean add(Value value) {
            AbstractMultiMap.this.add(key, value);
            return true;
        }

        @Override
        public int size() {
            Collection<Value> values = AbstractMultiMap.this.getModel().get(key);
            return values == null ? 0 : values.size();
        }
    }
}
