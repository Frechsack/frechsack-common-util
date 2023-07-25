package frechsack.prod.util.filesystem;

import frechsack.prod.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

class KeyValueFactory {

    static class SynchronizedKeyValue<Key> implements KeyValue<Key> {

        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private final @NotNull KeyValue<Key> impl;

        SynchronizedKeyValue(@NotNull KeyValue<Key> impl) {
            this.impl = impl;
        }

        @Override
        public void setNull(Key key) {
            lock.writeLock().lock();
            try {
                impl.setNull(key);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void addNull() {
            lock.writeLock().lock();
            try {
                impl.addNull();
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void setInt(Key key, int value) {
            lock.writeLock().lock();
            try {
                impl.setInt(key, value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void addInt(int value) {
            lock.writeLock().lock();
            try {
                impl.addInt(value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void setDouble(Key key, double value) {
            lock.writeLock().lock();
            try {
                impl.setDouble(key, value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void addDouble(double value) {
            lock.writeLock().lock();
            try {
                impl.addDouble(value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void setLong(Key key, long value) {
            lock.writeLock().lock();
            try {
                impl.setLong(key, value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void addLong(long value) {
            lock.writeLock().lock();
            try {
                impl.addLong(value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void setString(Key key, @NotNull String value) {
            lock.writeLock().lock();
            try {
                impl.setString(key, value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void addString(@NotNull String value) {
            lock.writeLock().lock();
            try {
                impl.addString(value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void setBoolean(Key key, boolean value) {
            lock.writeLock().lock();
            try {
                impl.setBoolean(key, value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void addBoolean(boolean value) {
            lock.writeLock().lock();
            try {
                impl.addBoolean(value);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void setValues(Key key, @NotNull KeyValue<?> values) {
            lock.writeLock().lock();
            try {
                impl.setValues(key, values);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void addValues(@NotNull KeyValue<?> values) {
            lock.writeLock().lock();
            try {
                impl.addValues(values);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void setArray(Key key, @NotNull KeyValue<Integer> values) {
            lock.writeLock().lock();
            try {
                impl.setArray(key, values);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void addArray(@NotNull KeyValue<Integer> values) {
            lock.writeLock().lock();
            try {
                impl.addArray(values);
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public int getInt(Key key) {
            lock.readLock().lock();
            try {
                return impl.getInt(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public int getInt(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.getInt(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public double getDouble(Key key) {
            lock.readLock().lock();
            try {
                return impl.getDouble(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public double getDouble(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.getDouble(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public long getLong(Key key) {
            lock.readLock().lock();
            try {
                return impl.getLong(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public long getLong(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.getLong(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public @NotNull String getString(Key key) {
            lock.readLock().lock();
            try {
                return impl.getString(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public @NotNull String getString(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.getString(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean getBoolean(Key key) {
            lock.readLock().lock();
            try {
                return impl.getBoolean(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean getBoolean(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.getBoolean(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public void getNull(Key key) {
            lock.readLock().lock();
            try {
                impl.getNull(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public @NotNull <KeyType> KeyValue<KeyType> getKeyValue(Key key, Class<KeyType> keyType) {
            lock.readLock().lock();
            try {
                return impl.getKeyValue(key, keyType);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public @NotNull KeyValue<Integer> getArray(Key key) {
            lock.readLock().lock();
            try {
                return impl.getArray(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean containsKey(Key key) {
            lock.readLock().lock();
            try {
                return impl.containsKey(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isArray(Key key) {
            lock.readLock().lock();
            try {
                return impl.isArray(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isKeyValue(Key key, Class<?> type) {
            lock.readLock().lock();
            try {
                return impl.isKeyValue(key, type);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isNull(Key key) {
            lock.readLock().lock();
            try {
                return impl.isNull(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isLong(Key key) {
            lock.readLock().lock();
            try {
                return impl.isLong(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isLong(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.isLong(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isDouble(Key key) {
            lock.readLock().lock();
            try {
                return impl.isDouble(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isDouble(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.isDouble(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isNumber(Key key) {
            lock.readLock().lock();
            try {
                return impl.isNumber(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isNumber(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.isNumber(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isString(Key key) {
            lock.readLock().lock();
            try {
                return impl.isString(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isString(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.isString(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isBoolean(Key key) {
            lock.readLock().lock();
            try {
                return impl.isBoolean(key);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isBoolean(Key key, boolean isConversion) {
            lock.readLock().lock();
            try {
                return impl.isBoolean(key, isConversion);
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public Stream<Tuple<Key, Object>> stream() {
            lock.readLock().lock();
            try {
                return impl.stream();
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public Stream<Key> streamKeys() {
            lock.readLock().lock();
            try {
                return impl.streamKeys();
            }
            finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public Set<Key> keys() {
            lock.readLock().lock();
            try {
                return impl.keys();
            }
            finally {
                lock.readLock().unlock();
            }
        }
    }
}
