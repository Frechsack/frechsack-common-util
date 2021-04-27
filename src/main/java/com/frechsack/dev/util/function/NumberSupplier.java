package com.frechsack.dev.util.function;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * Represents a Supplier of {@link Number}s.
 * <p>
 * The values could be returned as any primitive numerical type.
 *
 * @param <E> The value base type.
 * @author Frechsack
 */
@FunctionalInterface
public interface NumberSupplier<E extends Number> extends Supplier<E>, DoubleSupplier, IntSupplier, LongSupplier
{
    /**
     * Gets the value of this Supplier as a byte.
     *
     * @return Returns this Supplier´s value.
     * @implNote The default implementation will convert the value returned by {@link #get()} to a byte, if {@link #get()} returns null, zero is returned.
     */
    default byte getAsByte()
    {
        Number n = get();
        return n == null ? 0 : n.byteValue();
    }

    /**
     * Gets the value of this Supplier as a short.
     *
     * @return Returns this Supplier´s value.
     * @implNote The default implementation will convert the value returned by {@link #get()} to a short, if {@link #get()} returns null, zero is returned.
     */
    default short getAsShort()
    {
        Number n = get();
        return n == null ? 0 : n.shortValue();
    }

    /**
     * Gets the value of this Supplier as an integer.
     *
     * @return Returns this Supplier´s value.
     * @implNote The default implementation will convert the value returned by {@link #get()} to an integer, if {@link #get()} returns null, zero is returned.
     */
    @Override
    default int getAsInt()
    {
        Number n = get();
        return n == null ? 0 : n.intValue();
    }

    /**
     * Gets the value of this Supplier as a float.
     *
     * @return Returns this Supplier´s value.
     * @implNote The default implementation will convert the value returned by {@link #get()} to a float, if {@link #get()} returns null, zero is returned.
     */
    default float getFloat()
    {
        Number n = get();
        return n == null ? 0 : n.floatValue();
    }

    /**
     * Gets the value of this Supplier as a double.
     *
     * @return Returns this Supplier´s value.
     * @implNote The default implementation will convert the value returned by {@link #get()} to a double, if {@link #get()} returns null, zero is returned.
     */
    @Override
    default double getAsDouble()
    {
        Number n = get();
        return n == null ? 0 : n.doubleValue();
    }

    /**
     * Gets the value of this Supplier as a long.
     *
     * @return Returns this Supplier´s value.
     * @implNote The default implementation will convert the value returned by {@link #get()} to a long, if {@link #get()} returns null, zero is returned.
     */
    @Override
    default long getAsLong()
    {
        Number n = get();
        return n == null ? 0 : n.longValue();
    }

    /**
     * Gets the value of this Supplier as a {@link BigDecimal}.
     *
     * @return Returns this Supplier´s value.
     * @implNote The default implementation will convert the value returned by {@link #get()} to a BigDecimal, if {@link #get()} returns null, zero is returned.
     */
    default BigDecimal getAsBigDecimal()
    {
        Number n = get();
        if (n == null) return BigDecimal.ZERO;
        if (n instanceof BigDecimal) return (BigDecimal) n;
        if (n instanceof BigInteger) return new BigDecimal((BigInteger) n);
        if (n instanceof Long) return BigDecimal.valueOf(n.longValue());
        return BigDecimal.valueOf(n.doubleValue());
    }

    /**
     * Gets the value of this Supplier as a {@link BigInteger}.
     *
     * @return Returns this Supplier´s value.
     * @implNote The default implementation will convert the value returned by {@link #get()} to a BigInteger, if {@link #get()} returns null, zero is returned.
     */
    default BigInteger getAsBigInteger()
    {
        Number n = get();
        if (n == null) return BigInteger.ZERO;
        if (n instanceof BigDecimal) return ((BigDecimal) n).toBigInteger();
        if (n instanceof BigInteger) return (BigInteger) n;
        return BigInteger.valueOf(n.longValue());
    }
}
