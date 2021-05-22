package com.frechsack.dev.util.collection;

import com.frechsack.dev.util.Pair;
import com.frechsack.dev.util.route.IndexRoute;
import com.frechsack.dev.util.route.Routable;
import com.frechsack.dev.util.route.Route;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class wraps a native Java Array to an Object.
 * <p>
 * Primitive arrays and generic Object arrays are allowed. If a primitive or an Object type will be used is specified by the constructor.
 * <p>
 * The "native" array can be obtained by {@link #array()}. The returned array must be casted to the requested type.
 * <p>
 * This Array is compatible with the Java Collections Framework by {@link #list()}. This Array will be wrapped into a List. Changes in it will be reflected in this Array.
 * <p>
 * Common Array access operations are supported by {@link #set(int, Object)}, {@link #get(int)} and {@link #length()}. In case this Array uses a primitive type model, values can be set and obtained by special operations like {@link #setInt(int, int)}.
 *
 * @param <E> This Array´s class type.
 */
public final class Array<E> implements Function<Integer, E>, IntFunction<E>, Iterable<E>, Routable<E>, Cloneable
{
    private final Model<E> model;
    private Reference<List<E>> toListRef;

    private Array(E[] array, boolean isReference, Class<E> componentType)
    {
        Objects.requireNonNull(componentType);
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = modelForLength(componentType, array.length);
            for (int i = 0; i < array.length; i++) model.set(i, array[i]);
        }
        else
        {
            this.model = modelForArray(componentType, array);
        }
    }

    /**
     * Creates a new Array with the given array.
     *
     * @param array       The array.
     * @param isReference Specifies how the passed array should be used. If true is passed, the passed array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the passed array.
     */
    public Array(E[] array, boolean isReference)
    {
        this(array, isReference, getComponentType(array));
    }

    /**
     * Creates a new Array with the given array
     * The given array will be used as a reference, changes in this Array class will be reflected in the passed array.
     *
     * @param array The array.
     */
    public Array(E[] array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive boolean array.
     * The given primitive boolean array will be used as a reference, changes in this Array class will be reflected in the passed primitive array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Boolean}.
     *
     * @param array The primitive array.
     */
    public Array(boolean... array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive boolean array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Boolean}.
     *
     * @param array       The primitive array.
     * @param isReference Specifies how the passed primitive array should be used. If true is passed, the primitive array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the primitive array.
     */
    @SuppressWarnings("unchecked")
    public Array(boolean[] array, boolean isReference)
    {
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = (Model<E>) new BooleanModel(array.length);
            for (int i = 0; i < array.length; i++) set(i, (E) Boolean.valueOf(array[i]));
        }
        else this.model = (Model<E>) new BooleanModel(array);
    }

    /**
     * Creates a new Array with the given primitive byte array.
     * The given primitive byte array will be used as a reference, changes in this Array class will be reflected in the passed primitive array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Byte}.
     *
     * @param array The primitive array.
     */
    public Array(byte... array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive byte array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Byte}.
     *
     * @param array       The primitive array.
     * @param isReference Specifies how the passed primitive array should be used. If true is passed, the primitive array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the primitive array.
     */
    @SuppressWarnings("unchecked")
    public Array(byte[] array, boolean isReference)
    {
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = (Model<E>) new ByteModel(array.length);
            for (int i = 0; i < array.length; i++) set(i, (E) Byte.valueOf(array[i]));
        }
        else this.model = (Model<E>) new ByteModel(array);
    }

    /**
     * Creates a new Array with the given primitive short array.
     * The given primitive short array will be used as a reference, changes in this Array class will be reflected in the passed primitive array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Short}.
     *
     * @param array The primitive array.
     */
    public Array(short... array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive short array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Short}.
     *
     * @param array       The primitive array.
     * @param isReference Specifies how the passed primitive array should be used. If true is passed, the primitive array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the primitive array.
     */
    @SuppressWarnings("unchecked")
    public Array(short[] array, boolean isReference)
    {
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = (Model<E>) new ShortModel(array.length);
            for (int i = 0; i < array.length; i++) set(i, (E) Short.valueOf(array[i]));
        }
        else this.model = (Model<E>) new ShortModel(array);
    }

    /**
     * Creates a new Array with the given primitive int array.
     * The given primitive int array will be used as a reference, changes in this Array class will be reflected in the passed primitive array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Integer}.
     *
     * @param array The primitive array.
     */
    public Array(int... array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive int array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Integer}.
     *
     * @param array       The primitive array.
     * @param isReference Specifies how the passed primitive array should be used. If true is passed, the primitive array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the primitive array.
     */
    @SuppressWarnings("unchecked")
    public Array(int[] array, boolean isReference)
    {
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = (Model<E>) new IntModel(array.length);
            for (int i = 0; i < array.length; i++) set(i, (E) Integer.valueOf(array[i]));
        }
        else this.model = (Model<E>) new IntModel(array);
    }

    /**
     * Creates a new Array with the given primitive char array.
     * The given primitive char array will be used as a reference, changes in this Array class will be reflected in the passed primitive array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Character}.
     *
     * @param array The primitive array.
     */
    public Array(char... array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive char array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Character}.
     *
     * @param array       The primitive array.
     * @param isReference Specifies how the passed primitive array should be used. If true is passed, the primitive array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the primitive array.
     */
    @SuppressWarnings("unchecked")
    public Array(char[] array, boolean isReference)
    {
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = (Model<E>) new CharModel(array.length);
            for (int i = 0; i < array.length; i++) set(i, (E) Character.valueOf(array[i]));
        }
        else this.model = (Model<E>) new CharModel(array);
    }

    /**
     * Creates a new Array with the given primitive float array.
     * The given primitive float array will be used as a reference, changes in this Array class will be reflected in the passed primitive array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Float}.
     *
     * @param array The primitive array.
     */
    public Array(float... array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive float array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Float}.
     *
     * @param array       The primitive array.
     * @param isReference Specifies how the passed primitive array should be used. If true is passed, the primitive array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the primitive array.
     */
    @SuppressWarnings("unchecked")
    public Array(float[] array, boolean isReference)
    {
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = (Model<E>) new FloatModel(array.length);
            for (int i = 0; i < array.length; i++) set(i, (E) Float.valueOf(array[i]));
        }
        else this.model = (Model<E>) new FloatModel(array);
    }

    /**
     * Creates a new Array with the given primitive double array.
     * The given primitive double array will be used as a reference, changes in this Array class will be reflected in the passed primitive array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Integer}.
     *
     * @param array The primitive array.
     */
    public Array(double... array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive double array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Double}.
     *
     * @param array       The primitive array.
     * @param isReference Specifies how the passed primitive array should be used. If true is passed, the primitive array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the primitive array.
     */
    @SuppressWarnings("unchecked")
    public Array(double[] array, boolean isReference)
    {
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = (Model<E>) new DoubleModel(array.length);
            for (int i = 0; i < array.length; i++) set(i, (E) Double.valueOf(array[i]));
        }
        else this.model = (Model<E>) new DoubleModel(array);
    }

    /**
     * Creates a new Array with the given primitive long array.
     * The given primitive long array will be used as a reference, changes in this Array class will be reflected in the passed primitive array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Long}.
     *
     * @param array The primitive array.
     */
    public Array(long... array)
    {
        this(array, true);
    }

    /**
     * Creates a new Array with the given primitive long array.
     * This constructor should only be used, if this Array´s generic type is of type {@link Long}.
     *
     * @param array       The primitive array.
     * @param isReference Specifies how the passed primitive array should be used. If true is passed, the primitive array will be used as a reference. <p>
     *                    Changes in this Array class will be reflected in the primitive array.
     */
    @SuppressWarnings("unchecked")
    public Array(long[] array, boolean isReference)
    {
        Objects.requireNonNull(array);
        if (!isReference)
        {
            this.model = (Model<E>) new LongModel(array.length);
            for (int i = 0; i < array.length; i++) set(i, (E) Long.valueOf(array[i]));
        }
        else this.model = (Model<E>) new LongModel(array);
    }

    /**
     * Creates a new Array of the given type and length.
     *
     * @param length        The length of this array.
     * @param componentType The type of this array.<p>If the passed class-type is a primitive ({@link Class#isPrimitive()} returns true) an primitive model will be used.
     */
    public Array(int length, Class<E> componentType)
    {
        Objects.requireNonNull(componentType);
        Objects.checkIndex(0, length);
        this.model = modelForLength(componentType, length);
    }

    /**
     * Fills the whole Array with the specified value.
     *
     * @param e The value.
     */
    public void fill(E e)
    {
        fill(e, 0, length());
    }

    /**
     * Fills the whole Array with the specified value.
     *
     * @param e     The value.
     * @param start The inclusive start index.
     */
    public void fill(E e, int start)
    {
        fill(e, start, length());
    }

    /**
     * Fills the whole Array with the specified value.
     *
     * @param e     The value.
     * @param start The inclusive start index.
     * @param end   The exclusive end index.
     */
    public void fill(E e, int start, int end)
    {
        Objects.checkFromToIndex(start, end, length());
        IntStream.range(start, end).parallel().forEach(index -> set(index, e));
    }

    /**
     * Checks if this Array contains any Object that equals the specified Object.
     *
     * @param o The Object.
     * @return Returns true if the specified Object is found in this Array, else false.
     */
    public boolean contains(Object o)
    {
        if (length() < 8)
        {
            for (int i = 0; i < length(); i++) if (Objects.equals(o, get(i))) return true;
            return false;
        }
        return streamParallel().anyMatch(e -> Objects.equals(o, e));
    }

    /**
     * Returns the index of the specified Object in this Array. If the specified Object is not present in this Array, -1 is returned.
     *
     * @param o The Object.
     * @return Returns the index of the specified Object, else -1.
     */
    public int indexOf(Object o)
    {
        int length = length();
        for (int i = 0; i < length; i++) if (Objects.equals(get(i), o)) return i;
        return -1;
    }

    /**
     * Returns an {@link Spliterator} for this Array through the whole range of this Array.
     *
     * @return The Spliterator.
     */
    public Spliterator<E> spliterator()
    {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.SIZED);
    }

    /**
     * Streams the whole content of this Array.
     *
     * @return The Stream.
     * @see Stream
     */
    public Stream<E> stream()
    {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns an index based Stream of this Array.
     *
     * @return The Stream.
     * @see Stream
     */
    public Stream<Pair<Integer, E>> indexStream()
    {
        return IntStream.range(0, length()).mapToObj(i -> Pair.of(i, get(i)));
    }

    /**
     * Streams the whole content of this Array parallel.
     *
     * @return The Stream.
     * @see Stream
     */
    public Stream<E> streamParallel()
    {
        return StreamSupport.stream(spliterator(), true);
    }

    /**
     * Returns an {@link IntFunction} that acts like a generator for this Array´s type.
     *
     * @return The generator.
     */
    public IntFunction<E[]> generator()
    {
        return model.generator();
    }

    /**
     * Returns this Array´s value at the specified index.
     *
     * @param index The index.
     * @return The value at the specified index.
     */
    public E get(int index)
    {
        return model.get(index);
    }

    /**
     * Returns this Array´s value at the specified index. This will work only, if this Array used a primitive boolean Array model.
     *
     * @param index The index.
     * @return The primitive boolean at the specified index.
     */
    public boolean getBoolean(int index)
    {
        return model.getBoolean(index);
    }

    /**
     * Returns this Array´s value at the specified index. This will work only, if this Array used a primitive char Array model.
     *
     * @param index The index.
     * @return The primitive char at the specified index.
     */
    public char getChar(int index)
    {
        return model.getChar(index);
    }

    /**
     * Returns this Array´s value at the specified index. This will work only, if this Array used a primitive byte Array model.
     *
     * @param index The index.
     * @return The primitive byte at the specified index.
     */
    public byte getByte(int index)
    {
        return model.getByte(index);
    }

    /**
     * Returns this Array´s value at the specified index. This will work only, if this Array used a primitive short Array model.
     *
     * @param index The index.
     * @return The primitive short at the specified index.
     */
    public short getShort(int index)
    {
        return model.getShort(index);
    }

    /**
     * Returns this Array´s value at the specified index. This will work only, if this Array used a primitive int Array model.
     *
     * @param index The index.
     * @return The primitive int at the specified index.
     */
    public int getInt(int index)
    {
        return model.getInt(index);
    }

    /**
     * Returns this Array´s value at the specified index. This will work only, if this Array used a primitive float Array model.
     *
     * @param index The index.
     * @return The primitive float at the specified index.
     */
    public float getFloat(int index)
    {
        return model.getFloat(index);
    }

    /**
     * Returns this Array´s value at the specified index. This will work only, if this Array used a primitive double Array model.
     *
     * @param index The index.
     * @return The primitive double at the specified index.
     */
    public double getDouble(int index)
    {
        return model.getDouble(index);
    }

    /**
     * Returns this Array´s value at the specified index. This will work only, if this Array used a primitive long Array model.
     *
     * @param index The index.
     * @return The primitive long at the specified index.
     */
    public long getLong(int index)
    {
        return model.getLong(index);
    }

    /**
     * Assigns the specified value at the given index in this Array.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public E set(int index, E value)
    {
        return model.set(index, value);
    }

    /**
     * Assigns the specified primitive boolean at the given index in this Array. This will work only, if the Array used a primitive boolean Array model.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public boolean setBoolean(int index, boolean value)
    {
        return model.setBoolean(index, value);
    }

    /**
     * Assigns the specified primitive char at the given index in this Array. This will work only, if the Array used a primitive char Array model.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public char setChar(int index, char value)
    {
        return model.setChar(index, value);
    }

    /**
     * Assigns the specified primitive short at the given index in this Array. This will work only, if the Array used a primitive short Array model.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public short setShort(int index, short value)
    {
        return model.setShort(index, value);
    }

    /**
     * Assigns the specified primitive byte at the given index in this Array. This will work only, if the Array used a primitive byte Array model.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public byte setByte(int index, byte value)
    {
        return model.setByte(index, value);
    }

    /**
     * Assigns the specified primitive int at the given index in this Array. This will work only, if the Array used a primitive int Array model.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public int setInt(int index, int value)
    {
        return model.setInt(index, value);
    }

    /**
     * Assigns the specified primitive float at the given index in this Array. This will work only, if the Array used a primitive float Array model.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public float setFloat(int index, float value)
    {
        return model.setFloat(index, value);
    }

    /**
     * Assigns the specified primitive double at the given index in this Array. This will work only, if the Array used a primitive double Array model.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public double setDouble(int index, double value)
    {
        return model.setDouble(index, value);
    }

    /**
     * Assigns the specified primitive long at the given index in this Array. This will work only, if the Array used a primitive long Array model.
     *
     * @param index The index.
     * @param value The value.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public long setLong(int index, long value)
    {
        return model.setLong(index, value);
    }

    /**
     * Returns this Array´s value at the specified index.
     *
     * @param index The index.
     * @return The value at the specified index.
     */
    @Override
    public E apply(Integer index)
    {
        return get(index == null ? 0 : index);
    }

    /**
     * Returns this Array´s value at the specified index.
     *
     * @param index The index.
     * @return The value at the specified index.
     */
    @Override
    public E apply(int index)
    {
        return null;
    }

    /**
     * Assigns an unspecified value at the given index in this Array. In case of a primitive model, the primitive default value will be used, otherwise null.
     *
     * @param index The index.
     * @return Returns the previous value, that was assigned to the specified index.
     */
    public E setVoid(int index)
    {
        return model.set(index, null);
    }

    /**
     * Returns the length of this Array.
     *
     * @return The length
     */
    public int length()
    {
        return model.length();
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    protected Object clone()
    {
        // Create from Scratch
        Array<E> clone = new Array<>(length(), getComponentType(array().getClass()));
        int length = length();
        for (int i = 0; i < length; i++) clone.set(i, get(i));
        return clone;
    }

    /**
     * Returns the native model of this array as a primitive int array. This will work only, if this Array uses a primitive int model.
     *
     * @return The model.
     */
    public int[] intArray()
    {
        return model.intArray();
    }

    /**
     * Returns a copy of the primitive int model from this array. This will work only, if this Array uses a primitive int model.
     *
     * @return The model.
     */
    public int[] toIntArray()
    {
        return model.toIntArray();
    }

    /**
     * Returns the native model of this array as a primitive boolean array. This will work only, if this Array uses a primitive boolean model.
     *
     * @return The model.
     */
    public boolean[] booleanArray()
    {
        return model.booleanArray();
    }

    /**
     * Returns a copy of the primitive boolean model from this array. This will work only, if this Array uses a primitive boolean model.
     *
     * @return The model.
     */
    public boolean[] toBooleanArray()
    {
        return model.toBooleanArray();
    }

    /**
     * Returns the native model of this array as a primitive byte array. This will work only, if this Array uses a primitive byte model.
     *
     * @return The model.
     */
    public byte[] byteArray()
    {
        return model.byteArray();
    }

    /**
     * Returns a copy of the primitive byte model from this array. This will work only, if this Array uses a primitive byte model.
     *
     * @return The model.
     */
    public byte[] toByteArray()
    {
        return model.toByteArray();
    }

    /**
     * Returns the native model of this array as a primitive short array. This will work only, if this Array uses a primitive short model.
     *
     * @return The model.
     */
    public short[] shortArray()
    {
        return model.shortArray();
    }

    /**
     * Returns a copy of the primitive short model from this array. This will work only, if this Array uses a primitive short model.
     *
     * @return The model.
     */
    public short[] toShortArray()
    {
        return model.toShortArray();
    }

    /**
     * Returns the native model of this array as a primitive float array. This will work only, if this Array uses a primitive float model.
     *
     * @return The model.
     */
    public float[] floatArray()
    {
        return model.floatArray();
    }

    /**
     * Returns a copy of the primitive float model from this array. This will work only, if this Array uses a primitive float model.
     *
     * @return The model.
     */
    public float[] toFloatArray()
    {
        return model.toFloatArray();
    }

    /**
     * Returns the native model of this array as a primitive double array. This will work only, if this Array uses a primitive double model.
     *
     * @return The model.
     */
    public double[] doubleArray()
    {
        return model.doubleArray();
    }

    /**
     * Returns a copy of the primitive double model from this array. This will work only, if this Array uses a primitive double model.
     *
     * @return The model.
     */
    public double[] toDoubleArray()
    {
        return model.toDoubleArray();
    }

    /**
     * Returns the native model of this array as a primitive long array. This will work only, if this Array uses a primitive long model.
     *
     * @return The model.
     */
    public long[] longArray()
    {
        return model.longArray();
    }

    /**
     * Returns a copy of the primitive long model from this array. This will work only, if this Array uses a primitive long model.
     *
     * @return The model.
     */
    public long[] toLongArray()
    {
        return model.toLongArray();
    }

    /**
     * Returns the native model of this array as a primitive char array. This will work only, if this Array uses a primitive char model.
     *
     * @return The model.
     */
    public char[] charArray()
    {
        return model.charArray();
    }

    /**
     * Returns a copy of the primitive char model from this array. This will work only, if this Array uses a primitive char model.
     *
     * @return The model.
     */
    public char[] toCharArray()
    {
        return model.toCharArray();
    }

    /**
     * Returns the native model of this array.
     *
     * @return The model.
     */
    public Object array()
    {
        return model.array();
    }

    /**
     * Returns a copy of the native model of this array.
     *
     * @return The model.
     */
    public Object toArray()
    {
        return model.toArray();
    }

    /**
     * Returns a generic copy of this arrays model. This will be different from {@link #toArray()}, if this array uses a primitive type model.
     *
     * @return The generic copy.
     */
    public E[] toGenericArray()
    {
        return stream().toArray(generator());
    }

    /**
     * Checks if this Array uses a primitive array type.
     *
     * @return True if this Array uses a primitive array type, else false.
     */
    public boolean isPrimitive()
    {
        return model.isBoolean() ||
               model.isByte() ||
               model.isShort() ||
               model.isInt() ||
               model.isDouble() ||
               model.isLong() ||
               model.isChar() ||
               model.isFloat();
    }

    @Override
    public Route<E> route()
    {
        return new IndexRoute<>(this::get, this::length)
        {
            @Override
            protected int remove(int index)
            {
                setVoid(index);
                return index;
            }
        };
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Array<?> array = (Array<?>) o;
        return Objects.equals(model, array.model);
    }

    @SuppressWarnings("unchecked")
    public boolean equals(E[] array)
    {
        if (isPrimitive()) return Arrays.equals(array, toGenericArray());
        return Arrays.equals(array, (E[]) model.array());
    }

    /**
     * Compares this arrays primitive boolean array to the specified array. This will work only, if this Array uses a primitive boolean array model.
     *
     * @param array The array that will be compared with this Array.
     * @return Returns true if the passed array content equals this Array model, else false.
     */
    public boolean equals(boolean[] array)
    {
        return Arrays.equals(array, booleanArray());
    }

    /**
     * Compares this arrays primitive short array to the specified array. This will work only, if this Array uses a primitive short array model.
     *
     * @param array The array that will be compared with this Array.
     * @return Returns true if the passed array content equals this Array model, else false.
     */
    public boolean equals(short[] array)
    {
        return Arrays.equals(array, shortArray());
    }

    /**
     * Compares this arrays primitive byte array to the specified array. This will work only, if this Array uses a primitive byte array model.
     *
     * @param array The array that will be compared with this Array.
     * @return Returns true if the passed array content equals this Array model, else false.
     */
    public boolean equals(byte[] array)
    {
        return Arrays.equals(array, byteArray());
    }

    /**
     * Compares this arrays primitive int array to the specified array. This will work only, if this Array uses a primitive int array model.
     *
     * @param array The array that will be compared with this Array.
     * @return Returns true if the passed array content equals this Array model, else false.
     */
    public boolean equals(int[] array)
    {
        return Arrays.equals(array, intArray());
    }

    /**
     * Compares this arrays primitive double array to the specified array. This will work only, if this Array uses a primitive double array model.
     *
     * @param array The array that will be compared with this Array.
     * @return Returns true if the passed array content equals this Array model, else false.
     */
    public boolean equals(double[] array)
    {
        return Arrays.equals(array, doubleArray());
    }

    /**
     * Compares this arrays primitive long array to the specified array. This will work only, if this Array uses a primitive long array model.
     *
     * @param array The array that will be compared with this Array.
     * @return Returns true if the passed array content equals this Array model, else false.
     */
    public boolean equals(long[] array)
    {
        return Arrays.equals(array, longArray());
    }

    /**
     * Compares this arrays primitive char array to the specified array. This will work only, if this Array uses a primitive char array model.
     *
     * @param array The array that will be compared with this Array.
     * @return Returns true if the passed array content equals this Array model, else false.
     */
    public boolean equals(char[] array)
    {
        return Arrays.equals(array, charArray());
    }

    /**
     * Compares this arrays primitive float array to the specified array. This will work only, if this Array uses a primitive float array model.
     *
     * @param array The array that will be compared with this Array.
     * @return Returns true if the passed array content equals this Array model, else false.
     */
    public boolean equals(float[] array)
    {
        return Arrays.equals(array, floatArray());
    }

    /**
     * Sorts this array. It is mandatory that this Array´s generic type implements {@link Comparable} or is a primitive type.
     */
    public void sort()
    {
        model.sort();
    }

    /**
     * Sorts this array with the specified comparator. It is mandatory that this Array´s generic type is not a primitive. Primitives should be sorted by their natural order with {@link #sort()}.
     *
     * @param comparator The comparator.
     * @see Arrays#sort(E[])
     */
    public void sort(Comparator<? super E> comparator)
    {
        model.sort(comparator);
    }

    /**
     * Returns a {@link List} representation of this Array. Changes in the returned List will be reflected in this array.
     *
     * @return Returns a List representation of this Array.
     */
    public List<E> list()
    {
        List<E> toList = toListRef == null ? null : toListRef.get();
        if (toList == null) toListRef = new SoftReference<>(toList = new ArrayAsList());
        return toList;
    }

    @Override
    public String toString()
    {
        return "Array{type=" + getComponentType(array().getClass()) + " array=" + model + '}';
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(model);
    }

    @Override
    public Iterator<E> iterator()
    {
        return route();
    }

    @SuppressWarnings("unchecked")
    private static <E> Model<E> modelForArray(Class<E> componentType, Object array)
    {
        // Primitive model
        if (componentType.isPrimitive())
        {
            if (componentType == Boolean.TYPE) return (Model<E>) new BooleanModel((boolean[]) array);
            else if (componentType == Byte.TYPE) return (Model<E>) new ByteModel((byte[]) array);
            else if (componentType == Short.TYPE) return (Model<E>) new ShortModel((short[]) array);
            else if (componentType == Integer.TYPE) return (Model<E>) new IntModel((int[]) array);
            else if (componentType == Float.TYPE) return (Model<E>) new FloatModel((float[]) array);
            else if (componentType == Character.TYPE) return (Model<E>) new CharModel((char[]) array);
            else if (componentType == Double.TYPE) return (Model<E>) new DoubleModel((double[]) array);
            else if (componentType == Long.TYPE) return (Model<E>) new LongModel((long[]) array);
            else throw new IllegalArgumentException("Can not create a Model for the primitive type: " + componentType.getName());
        }
        // Class model
        else
        {
            /*
            if (type == Boolean.class) return (Model<E>) new BooleanModel((boolean[]) array);
            else if (type == Byte.class) return (Model<E>) new ByteModel((byte[]) array);
            else if (type == Short.class) return (Model<E>) new ShortModel((short[]) array);
            else if (type == Integer.class) return (Model<E>) new IntModel((int[]) array);
            else if (type == Float.class) return (Model<E>) new FloatModel((float[]) array);
            else if (type == Character.class) return (Model<E>) new CharModel((char[]) array);
            else if (type == Double.class) return (Model<E>) new DoubleModel((double[]) array);
            else if (type == Long.class) return (Model<E>) new LongModel((long[]) array);*/
            return new GenericModel<>((E[]) array);
        }
    }

    @SuppressWarnings("unchecked")
    private static <E> Model<E> modelForLength(Class<E> componentType, int length)
    {
        // Primitive model
        if (componentType.isPrimitive())
        {
            if (componentType == Boolean.TYPE) return (Model<E>) new BooleanModel(length);
            else if (componentType == Byte.TYPE) return (Model<E>) new ByteModel(length);
            else if (componentType == Short.TYPE) return (Model<E>) new ShortModel(length);
            else if (componentType == Integer.TYPE) return (Model<E>) new IntModel(length);
            else if (componentType == Float.TYPE) return (Model<E>) new FloatModel(length);
            else if (componentType == Character.TYPE) return (Model<E>) new CharModel(length);
            else if (componentType == Double.TYPE) return (Model<E>) new DoubleModel(length);
            else if (componentType == Long.TYPE) return (Model<E>) new LongModel(length);
            else throw new IllegalArgumentException("Can not create a Model for the primitive type: " + componentType.getName());
        }
        // Class model
        else
        {
            // Use the generic one
            /*
            if (type == Boolean.class) return (Model<E>) new BooleanModel(length);
            else if (type == Byte.class) return (Model<E>) new ByteModel(length);
            else if (type == Short.class) return (Model<E>) new ShortModel(length);
            else if (type == Integer.class) return (Model<E>) new IntModel(length);
            else if (type == Float.class) return (Model<E>) new FloatModel(length);
            else if (type == Character.class) return (Model<E>) new CharModel(length);
            else if (type == Double.class) return (Model<E>) new DoubleModel(length);
            else if (type == Long.class) return (Model<E>) new LongModel(length);*/
            return new GenericModel<>(componentType, length);
        }
    }

    private static <E> Class<E> getComponentType(E[] array)
    {
        Objects.requireNonNull(array);
        return getComponentType(array.getClass());
    }

    @SuppressWarnings("unchecked")
    private static <E> Class<E> getComponentType(Class<?> type)
    {
        while (type.isArray()) type = type.getComponentType();
        return (Class<E>) type;
    }

    @SuppressWarnings("unchecked")
    private static class GenericModel<E> extends Model<E>
    {
        private final E[] data;

        @Override
        public Object toArray()
        {
            E[] clone = (E[]) java.lang.reflect.Array.newInstance(getComponentType(data), data.length);
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        private GenericModel(E[] data)
        {
            this.data = data;
        }

        private GenericModel(Class<E> type, int length)
        {
            this.data = (E[]) java.lang.reflect.Array.newInstance(getComponentType(type), length);
        }

        @Override
        public boolean setBoolean(int index, boolean item)
        {
            if (!Objects.equals(getComponentType(data), Boolean.class)) super.setBoolean(index, item);
            boolean last = (Boolean) data[index];
            data[index] = (E) Boolean.valueOf(item);
            return last;
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public E get(int i)
        {
            return data[i];
        }

        @Override
        public E set(int i, E e)
        {
            E last = data[i];
            data[i] = e;
            return last;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<E[]> generator()
        {
            return value -> (E[]) java.lang.reflect.Array.newInstance(getComponentType(data), data.length);
        }

        @Override
        public boolean allowsVoid()
        {
            return true;
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o instanceof Array)
            {
                return Arrays.equals(data, (E[]) ((Array<?>) o).array());
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }

        @Override
        public void sort(Comparator<? super E> c)
        {
            Arrays.sort(data, c);
        }
    }

    private static class ByteModel extends Model<Byte>
    {
        private final byte[] data;

        @Override
        public Object toArray()
        {
            return toByteArray();
        }

        @Override
        public byte[] byteArray()
        {
            return data;
        }

        @Override
        public byte[] toByteArray()
        {
            byte[] clone = new byte[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public double[] toDoubleArray()
        {
            double[] clone = new double[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public int[] toIntArray()
        {
            int[] clone = new int[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public short[] toShortArray()
        {
            short[] clone = new short[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public float[] toFloatArray()
        {
            float[] clone = new float[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public long[] toLongArray()
        {
            long[] clone = new long[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        private ByteModel(int length)
        {
            this.data = new byte[length];
        }

        private ByteModel(byte[] data)
        {
            this.data = data;
        }

        @Override
        public boolean isByte()
        {
            return true;
        }

        @Override
        public Byte get(int i)
        {
            return getByte(i);
        }

        @Override
        public Byte set(int i, Byte e)
        {
            return setByte(i, e == null ? 0 : e);
        }

        @Override
        public byte setByte(int index, byte item)
        {
            byte b = data[index];
            data[index] = item;
            return b;
        }

        @Override
        public double setDouble(int index, double item)
        {
            return setByte(index, (byte) item);
        }

        @Override
        public int setInt(int index, int item)
        {
            return setByte(index, (byte) item);
        }

        @Override
        public short setShort(int index, short item)
        {
            return setByte(index, (byte) item);
        }

        @Override
        public float setFloat(int index, float item)
        {
            return setByte(index, (byte) item);
        }

        @Override
        public long setLong(int index, long item)
        {
            return setByte(index, (byte) item);
        }

        @Override
        public byte getByte(int index)
        {
            return data[index];
        }

        @Override
        public double getDouble(int index)
        {
            return getByte(index);
        }

        @Override
        public int getInt(int index)
        {
            return getByte(index);
        }

        @Override
        public short getShort(int index)
        {
            return getByte(index);
        }

        @Override
        public float getFloat(int index)
        {
            return getByte(index);
        }

        @Override
        public long getLong(int index)
        {
            return getByte(index);
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<Byte[]> generator()
        {
            return value -> new Byte[data.length];
        }

        @Override
        public boolean allowsVoid()
        {
            return false;
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ByteModel byteModel = (ByteModel) o;
            return Arrays.equals(data, byteModel.data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }
    }

    private static class LongModel extends Model<Long>
    {
        private final long[] data;

        @Override
        public Object toArray()
        {
            return toLongArray();
        }

        @Override
        public long[] longArray()
        {
            return data;
        }

        @Override
        public long[] toLongArray()
        {
            long[] clone = new long[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public float[] toFloatArray()
        {
            float[] clone = new float[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public short[] toShortArray()
        {
            short[] clone = new short[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (short) data[i]);
            return clone;
        }

        @Override
        public byte[] toByteArray()
        {
            byte[] clone = new byte[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (byte) data[i]);
            return clone;
        }

        @Override
        public int[] toIntArray()
        {
            int[] clone = new int[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (int) data[i]);
            return clone;
        }

        @Override
        public double[] toDoubleArray()
        {
            double[] clone = new double[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        private LongModel(int length)
        {
            this.data = new long[length];
        }

        private LongModel(long[] data)
        {
            this.data = data;
        }

        @Override
        public boolean isLong()
        {
            return true;
        }

        @Override
        public long getLong(int index)
        {
            return data[index];
        }

        @Override
        public float getFloat(int index)
        {
            return getLong(index);
        }

        @Override
        public short getShort(int index)
        {
            return (short) getLong(index);
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) getLong(index);
        }

        @Override
        public int getInt(int index)
        {
            return (int) getLong(index);
        }

        @Override
        public double getDouble(int index)
        {
            return getLong(index);
        }

        @Override
        public Long get(int i)
        {
            return getLong(i);
        }

        @Override
        public long setLong(int index, long item)
        {
            long l = data[index];
            data[index] = item;
            return l;
        }

        @Override
        public float setFloat(int index, float item)
        {
            return setLong(index, (long) item);
        }

        @Override
        public short setShort(int index, short item)
        {
            return (short) setLong(index, item);
        }

        @Override
        public byte setByte(int index, byte item)
        {
            return (byte) setLong(index, item);
        }

        @Override
        public int setInt(int index, int item)
        {
            return (int) setLong(index, item);
        }

        @Override
        public double setDouble(int index, double item)
        {
            return setLong(index, (long) item);
        }

        @Override
        public Long set(int i, Long e)
        {
            return setLong(i, e == null ? 0 : e);
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<Long[]> generator()
        {
            return value -> new Long[data.length];
        }

        @Override
        public boolean allowsVoid()
        {
            return false;
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LongModel longModel = (LongModel) o;
            return Arrays.equals(data, longModel.data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }
    }

    private static class DoubleModel extends Model<Double>
    {
        private final double[] data;

        @Override
        public Object toArray()
        {
            return toDoubleArray();
        }

        @Override
        public double[] toDoubleArray()
        {
            double[] clone = new double[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public int[] toIntArray()
        {
            int[] clone = new int[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (int) data[i]);
            return clone;
        }

        @Override
        public long[] toLongArray()
        {
            long[] clone = new long[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (long) data[i]);
            return clone;
        }

        @Override
        public byte[] toByteArray()
        {
            byte[] clone = new byte[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (byte) data[i]);
            return clone;
        }

        @Override
        public short[] toShortArray()
        {
            short[] clone = new short[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (short) data[i]);
            return clone;
        }

        @Override
        public float[] toFloatArray()
        {
            float[] clone = new float[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (float) data[i]);
            return clone;
        }

        @Override
        public double[] doubleArray()
        {
            return data;
        }

        private DoubleModel(int length)
        {
            this.data = new double[length];
        }

        private DoubleModel(double[] data)
        {
            this.data = data;
        }

        @Override
        public boolean isDouble()
        {
            return true;
        }

        @Override
        public Double get(int i)
        {
            return getDouble(i);
        }

        @Override
        public double getDouble(int index)
        {
            return data[index];
        }

        @Override
        public int getInt(int index)
        {
            return (int) getDouble(index);
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) getDouble(index);
        }

        @Override
        public short getShort(int index)
        {
            return (short) getDouble(index);
        }

        @Override
        public long getLong(int index)
        {
            return (long) getDouble(index);
        }

        @Override
        public float getFloat(int index)
        {
            return (float) getDouble(index);
        }

        @Override
        public Double set(int i, Double e)
        {
            return setDouble(i, e == null ? 0 : e);
        }

        @Override
        public double setDouble(int index, double item)
        {
            double d = data[index];
            data[index] = item;
            return d;
        }

        @Override
        public int setInt(int index, int item)
        {
            return (int) setDouble(index, item);
        }

        @Override
        public byte setByte(int index, byte item)
        {
            return (byte) setDouble(index, item);
        }

        @Override
        public short setShort(int index, short item)
        {
            return (short) setDouble(index, item);
        }

        @Override
        public long setLong(int index, long item)
        {
            return (long) setDouble(index, item);
        }

        @Override
        public float setFloat(int index, float item)
        {
            return (float) setDouble(index, item);
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<Double[]> generator()
        {
            return value -> new Double[data.length];
        }

        @Override
        public boolean allowsVoid()
        {
            return false;
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DoubleModel that = (DoubleModel) o;
            return Arrays.equals(data, that.data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }
    }

    private static class CharModel extends Model<Character>
    {
        private final char[] data;

        @Override
        public Object toArray()
        {
            return toCharArray();
        }

        @Override
        public char[] toCharArray()
        {
            char[] clone = new char[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public char[] charArray()
        {
            return data;
        }

        private CharModel(int length)
        {
            this.data = new char[length];
        }

        private CharModel(char[] data)
        {
            this.data = data;
        }

        @Override
        public boolean isChar()
        {
            return true;
        }

        @Override
        public Character get(int i)
        {
            return getChar(i);
        }

        @Override
        public Character set(int i, Character e)
        {
            return setChar(i, e == null ? ' ' : e);
        }

        @Override
        public char setChar(int index, char item)
        {
            char c = data[index];
            data[index] = item;
            return c;
        }

        @Override
        public char getChar(int index)
        {
            return data[index];
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<Character[]> generator()
        {
            return value -> new Character[data.length];
        }

        @Override
        public boolean allowsVoid()
        {
            return false;
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CharModel charModel = (CharModel) o;
            return Arrays.equals(data, charModel.data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }
    }

    private static class FloatModel extends Model<Float>
    {
        private final float[] data;

        @Override
        public Object toArray()
        {
            return toFloatArray();
        }

        @Override
        public float[] toFloatArray()
        {
            float[] clone = new float[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public short[] toShortArray()
        {
            short[] clone = new short[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (short) data[i]);
            return clone;
        }

        @Override
        public byte[] toByteArray()
        {
            byte[] clone = new byte[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (byte) data[i]);
            return clone;
        }

        @Override
        public double[] toDoubleArray()
        {
            double[] clone = new double[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public long[] toLongArray()
        {
            long[] clone = new long[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (long) data[i]);
            return clone;
        }

        @Override
        public int[] toIntArray()
        {
            int[] clone = new int[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (int) data[i]);
            return clone;
        }

        private FloatModel(int length)
        {
            this.data = new float[length];
        }

        private FloatModel(float[] data)
        {
            this.data = data;
        }

        @Override
        public boolean isFloat()
        {
            return true;
        }

        @Override
        public Float get(int i)
        {
            return getFloat(i);
        }

        @Override
        public Float set(int i, Float e)
        {
            return setFloat(i, e == null ? 0 : e);
        }

        @Override
        public float setFloat(int index, float item)
        {
            float f = data[index];
            data[index] = item;
            return f;
        }

        @Override
        public double setDouble(int index, double item)
        {
            return setFloat(index, (float) item);
        }

        @Override
        public long setLong(int index, long item)
        {
            return (long) setFloat(index, item);
        }

        @Override
        public short setShort(int index, short item)
        {
            return (short) setFloat(index, item);
        }

        @Override
        public byte setByte(int index, byte item)
        {
            return (byte) setFloat(index, item);
        }

        @Override
        public int setInt(int index, int item)
        {
            return (int) setFloat(index, item);
        }

        @Override
        public float getFloat(int index)
        {
            return data[index];
        }

        @Override
        public long getLong(int index)
        {
            return (long) getFloat(index);
        }

        @Override
        public double getDouble(int index)
        {
            return getFloat(index);
        }

        @Override
        public short getShort(int index)
        {
            return (short) getFloat(index);
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) getFloat(index);
        }

        @Override
        public int getInt(int index)
        {
            return (int) getFloat(index);
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public float[] floatArray()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<Float[]> generator()
        {
            return value -> new Float[data.length];
        }

        @Override
        public boolean allowsVoid()
        {
            return false;
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FloatModel that = (FloatModel) o;
            return Arrays.equals(data, that.data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }
    }

    private static class IntModel extends Model<Integer>
    {
        private final int[] data;

        private IntModel(int length)
        {
            this.data = new int[length];
        }

        private IntModel(int[] data)
        {
            this.data = data;
        }

        @Override
        public boolean isInt()
        {
            return true;
        }

        @Override
        public int setInt(int index, int item)
        {
            int i = data[index];
            data[index] = item;
            return i;
        }

        @Override
        public byte setByte(int index, byte item)
        {
            return (byte) setInt(index, item);
        }

        @Override
        public short setShort(int index, short item)
        {
            return (short) setInt(index, item);
        }

        @Override
        public float setFloat(int index, float item)
        {
            return setInt(index, (int) item);
        }

        @Override
        public long setLong(int index, long item)
        {
            return setInt(index, (int) item);
        }

        @Override
        public double setDouble(int index, double item)
        {
            return setInt(index, (int) item);
        }

        @Override
        public int getInt(int index)
        {
            return data[index];
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) getInt(index);
        }

        @Override
        public short getShort(int index)
        {
            return (short) getInt(index);
        }

        @Override
        public float getFloat(int index)
        {
            return getInt(index);
        }

        @Override
        public double getDouble(int index)
        {
            return getInt(index);
        }

        @Override
        public long getLong(int index)
        {
            return get(index);
        }

        @Override
        public Integer get(int i)
        {
            return getInt(i);
        }

        @Override
        public Integer set(int i, Integer e)
        {
            return setInt(i, e == null ? 0 : e);
        }

        @Override
        public int[] intArray()
        {
            return data;
        }

        @Override
        public int[] toIntArray()
        {
            int[] clone = new int[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public long[] toLongArray()
        {
            long[] clone = new long[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public float[] toFloatArray()
        {
            float[] clone = new float[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public double[] toDoubleArray()
        {
            double[] clone = new double[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public byte[] toByteArray()
        {
            byte[] clone = new byte[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (byte) data[i]);
            return clone;
        }

        @Override
        public short[] toShortArray()
        {
            short[] clone = new short[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (short) data[i]);
            return clone;
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public Object toArray()
        {
            return toIntArray();
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<Integer[]> generator()
        {
            return value -> new Integer[data.length];
        }

        @Override
        public boolean allowsVoid()
        {
            return false;
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntModel intModel = (IntModel) o;
            return Arrays.equals(data, intModel.data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }
    }

    private static class ShortModel extends Model<Short>
    {
        private final short[] data;

        private ShortModel(int length)
        {
            this.data = new short[length];
        }

        private ShortModel(short[] data)
        {
            this.data = data;
        }

        @Override
        public boolean isShort()
        {
            return true;
        }

        @Override
        public short[] shortArray()
        {
            return data;
        }

        @Override
        public short[] toShortArray()
        {
            short[] clone = new short[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public byte[] toByteArray()
        {
            byte[] clone = new byte[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = (byte) data[i]);
            return clone;
        }

        @Override
        public double[] toDoubleArray()
        {
            double[] clone = new double[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public float[] toFloatArray()
        {
            float[] clone = new float[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public int[] toIntArray()
        {
            int[] clone = new int[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public long[] toLongArray()
        {
            long[] clone = new long[data.length];
            IntStream.range(0, data.length).parallel().forEach(i -> clone[i] = data[i]);
            return clone;
        }

        @Override
        public Short get(int i)
        {
            return getShort(i);
        }

        @Override
        public short getShort(int index)
        {
            return data[index];
        }

        @Override
        public byte getByte(int index)
        {
            return (byte) getShort(index);
        }

        @Override
        public int getInt(int index)
        {
            return getShort(index);
        }

        @Override
        public float getFloat(int index)
        {
            return getShort(index);
        }

        @Override
        public double getDouble(int index)
        {
            return getShort(index);
        }

        @Override
        public long getLong(int index)
        {
            return getShort(index);
        }

        @Override
        public short setShort(int index, short item)
        {
            short s = data[index];
            data[index] = item;
            return s;
        }

        @Override
        public byte setByte(int index, byte item)
        {
            return (byte) setShort(index, item);
        }

        @Override
        public int setInt(int index, int item)
        {
            return setShort(index, (short) item);
        }

        @Override
        public float setFloat(int index, float item)
        {
            return setShort(index, (short) item);
        }

        @Override
        public double setDouble(int index, double item)
        {
            return setShort(index, (short) item);
        }

        @Override
        public long setLong(int index, long item)
        {
            return setShort(index, (short) item);
        }

        @Override
        public Short set(int i, Short e)
        {
            return setShort(i, e == null ? 0 : e);
        }

        @Override
        public Object toArray()
        {
            return toShortArray();
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<Short[]> generator()
        {
            return value -> new Short[data.length];
        }

        @Override
        public boolean allowsVoid()
        {
            return false;
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ShortModel that = (ShortModel) o;
            return Arrays.equals(data, that.data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }

        @Override
        public void sort()
        {
            Arrays.sort(data);
        }
    }

    private static class BooleanModel extends Model<Boolean>
    {
        private final boolean[] data;

        private BooleanModel(int length)
        {
            this.data = new boolean[length];
        }

        private BooleanModel(boolean[] data)
        {
            this.data = data;
        }

        @Override
        public Object array()
        {
            return data;
        }

        @Override
        public Object toArray()
        {
            return toBooleanArray();
        }

        @Override
        public boolean[] booleanArray()
        {
            return data;
        }

        @Override
        public boolean[] toBooleanArray()
        {
            boolean[] clone = new boolean[data.length];
            System.arraycopy(data, 0, clone, 0, data.length);
            return clone;
        }

        @Override
        public boolean isBoolean()
        {
            return true;
        }

        @Override
        public boolean allowsVoid()
        {
            return false;
        }

        @Override
        public Boolean get(int i)
        {
            return getBoolean(i);
        }

        @Override
        public boolean getBoolean(int index)
        {
            return data[index];
        }

        @Override
        public boolean setBoolean(int index, boolean item)
        {
            boolean b = data[index];
            data[index] = item;
            return b;
        }

        @Override
        public Boolean set(int i, Boolean e)
        {
            return setBoolean(i, e != null && e);
        }

        @Override
        public int length()
        {
            return data.length;
        }

        @Override
        public IntFunction<Boolean[]> generator()
        {
            return value -> new Boolean[data.length];
        }

        @Override
        public String toString()
        {
            return Arrays.toString(data);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BooleanModel that = (BooleanModel) o;
            return Arrays.equals(data, that.data);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(data);
        }
    }

    private abstract static class Model<E>
    {
        boolean[] booleanArray()
        {
            throw new UnsupportedOperationException("booleanArray");
        }

        boolean[] toBooleanArray()
        {
            throw new UnsupportedOperationException("toBooleanArray");
        }

        byte[] byteArray()
        {
            throw new UnsupportedOperationException("byteArray");
        }

        byte[] toByteArray()
        {
            throw new UnsupportedOperationException("toByteArray");
        }

        short[] shortArray()
        {
            throw new UnsupportedOperationException("shortArray");
        }

        short[] toShortArray()
        {
            throw new UnsupportedOperationException("toShortArray");
        }

        int[] intArray()
        {
            throw new UnsupportedOperationException("intArray");
        }

        int[] toIntArray()
        {
            throw new UnsupportedOperationException("toIntArray");
        }

        float[] floatArray()
        {
            throw new UnsupportedOperationException("floatArray");
        }

        float[] toFloatArray()
        {
            throw new UnsupportedOperationException("toFloatArray");
        }

        double[] doubleArray()
        {
            throw new UnsupportedOperationException("doubleArray");
        }

        double[] toDoubleArray()
        {
            throw new UnsupportedOperationException("toDoubleArray");
        }

        long[] longArray()
        {
            throw new UnsupportedOperationException("longArray");
        }

        long[] toLongArray()
        {
            throw new UnsupportedOperationException("toLongArray");
        }

        char[] charArray()
        {
            throw new UnsupportedOperationException("charArray");
        }

        char[] toCharArray()
        {
            throw new UnsupportedOperationException("toCharArray");
        }

        boolean isBoolean()
        {
            return false;
        }

        boolean isShort()
        {
            return false;
        }

        boolean isByte()
        {
            return false;
        }

        boolean isChar()
        {
            return false;
        }

        boolean isInt()
        {
            return false;
        }

        boolean isDouble()
        {
            return false;
        }

        boolean isLong()
        {
            return false;
        }

        boolean isFloat()
        {
            return false;
        }

        int getInt(int index)
        {
            throw new UnsupportedOperationException("getInt");
        }

        int setInt(int index, int item)
        {
            throw new UnsupportedOperationException("setInt");
        }

        boolean getBoolean(int index)
        {
            throw new UnsupportedOperationException("getBoolean");
        }

        boolean setBoolean(int index, boolean item)
        {
            throw new UnsupportedOperationException("setBoolean");
        }

        short getShort(int index)
        {
            throw new UnsupportedOperationException("getShort");
        }

        short setShort(int index, short item)
        {
            throw new UnsupportedOperationException("setShort");
        }

        float getFloat(int index)
        {
            throw new UnsupportedOperationException("getFloat");
        }

        float setFloat(int index, float item)
        {
            throw new UnsupportedOperationException("setFloat");
        }

        double getDouble(int index)
        {
            throw new UnsupportedOperationException("getDouble");
        }

        double setDouble(int index, double item)
        {
            throw new UnsupportedOperationException("setDouble");
        }

        char getChar(int index)
        {
            throw new UnsupportedOperationException("getChar");
        }

        char setChar(int index, char item)
        {
            throw new UnsupportedOperationException("setChar");
        }

        byte getByte(int index)
        {
            throw new UnsupportedOperationException("getByte");
        }

        byte setByte(int index, byte item)
        {
            throw new UnsupportedOperationException("setByte");
        }

        long getLong(int index)
        {
            throw new UnsupportedOperationException("getLong");
        }

        long setLong(int index, long item)
        {
            throw new UnsupportedOperationException("setLong");
        }

        void sort(Comparator<? super E> c) {throw new UnsupportedOperationException("sort");}

        void sort() {throw new UnsupportedOperationException("sort");}

        abstract E get(int i);

        abstract E set(int i, E e);

        abstract Object array();

        abstract Object toArray();

        abstract int length();

        abstract IntFunction<E[]> generator();

        abstract boolean allowsVoid();
    }


    private class ArrayAsList extends AbstractList<E> implements List<E>
    {
        @Override
        public int size()
        {
            return length();
        }

        @Override
        public boolean isEmpty()
        {
            return length() == 0;
        }

        @Override
        public boolean contains(Object o)
        {
            return Array.this.contains(o);
        }

        @Override
        public E get(int index)
        {
            return Array.this.get(index);
        }

        @Override
        public E set(int index, E element)
        {
            return Array.this.set(index, element);
        }

        @Override
        public int indexOf(Object o)
        {
            return Array.this.indexOf(o);
        }

        @Override
        public Iterator<E> iterator()
        {
            return Array.this.iterator();
        }

        @Override
        public Object[] toArray()
        {
            return toGenericArray();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a)
        {
            if (a.length < length())
                // Create Array
                a = (T[]) java.lang.reflect.Array.newInstance(a.getClass(), length());

            final T[] clone = a;
            // Copy data
            IntStream.range(0, length()).parallel().forEach(i -> clone[i] = (T) get(i));
            // Fill null
            IntStream.range(length(), clone.length).parallel().forEach(i -> clone[i] = null);
            return a;
        }

        @Override
        public Spliterator<E> spliterator()
        {
            return Array.this.spliterator();
        }

        @Override
        public void forEach(Consumer<? super E> action)
        {
            Array.this.forEach(action);
        }

        @Override
        public void sort(Comparator<? super E> c)
        {
            Array.this.sort(c);
        }
    }
}
