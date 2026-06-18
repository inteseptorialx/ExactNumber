package com.caiobisestifigueiredo.engine.math.core;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Core immutable representation of a Fixed-Point number with dynamic Type Promotion.
 */
public final class ExactNumber implements Comparable<ExactNumber> {

    // Internal state
    private final long longValue;
    private final BigInteger bigValue;
    private final boolean isBig;
    private final int scale;

    /**
     * Private constructor to enforce the Factory Pattern.
     */
    private ExactNumber(long longValue, BigInteger bigValue, boolean isBig, int scale) {
        this.longValue = longValue;
        this.bigValue = bigValue;
        this.isBig = isBig;
        this.scale = scale;
    }

    // --- FACTORY METHODS (Abstract Factory entry points) ---

    public static ExactNumber fromLong(long value, int scale) {
        return new ExactNumber(value, null, false, scale);
    }

    public static ExactNumber fromBigInteger(BigInteger value, int scale) {
        // Down-promotion check: If BigInteger fits in a long, downgrade it immediately.
        if (value.bitLength() <= 63) {
            return new ExactNumber(value.longValueExact(), null, false, scale);
        }
        return new ExactNumber(0L, value, true, scale);
    }

    public static ExactNumber fromDouble(double value, int scale) {
        BigDecimal bd = BigDecimal.valueOf(value);
        BigDecimal scaled = bd.scaleByPowerOfTen(scale).setScale(0, RoundingMode.HALF_UP);
        return fromBigInteger(scaled.toBigInteger(), scale);
    }

    // --- CORE ARITHMETIC (Builder Pattern friendly) ---

    public ExactNumber add(ExactNumber other) {
        alignScalesGuard(other);

        if (!this.isBig && !other.isBig) {
            try {
                // TDD Boundary: This exact method must be tested to ensure overflow triggers the catch.
                long result = Math.addExact(this.longValue, other.longValue);
                return ExactNumber.fromLong(result, this.scale);
            } catch (ArithmeticException e) {
                // Type Promotion up to BigInteger
                BigInteger bigResult = BigInteger.valueOf(this.longValue)
                        .add(BigInteger.valueOf(other.longValue));
                return ExactNumber.fromBigInteger(bigResult, this.scale);
            }
        }

        // Both or one is BigInteger
        BigInteger val1 = this.isBig ? this.bigValue : BigInteger.valueOf(this.longValue);
        BigInteger val2 = other.isBig ? other.bigValue : BigInteger.valueOf(other.longValue);
        return ExactNumber.fromBigInteger(val1.add(val2), this.scale);
    }

    // --- MICROKERNEL PLUGIN ARCHITECTURE ---

    /**
     * The Microkernel hook. Applies external mathematical algorithms to this core instance.
     */
    public <R> R apply(MathPlugin<R> plugin) {
        return plugin.execute(this);
    }

    // --- UTILITY & GETTERS ---

    private void alignScalesGuard(ExactNumber other) {
        if (this.scale != other.scale) {
            throw new IllegalArgumentException("Scales must match for core operations.");
        }
    }

    public String toFormattedString() {
        if (isBig) {
            BigDecimal bd = new BigDecimal(this.bigValue).scaleByPowerOfTen(-this.scale);
            return bd.toPlainString();
        }
        BigDecimal bd = BigDecimal.valueOf(this.longValue).scaleByPowerOfTen(-this.scale);
        return bd.toPlainString();
    }

    /**
     * Subtracts another ExactNumber using the Builder pattern.
     */
    public ExactNumber subtract(ExactNumber other) {
        alignScalesGuard(other);

        if (!this.isBig && !other.isBig) {
            try {
                long result = Math.subtractExact(this.longValue, other.longValue);
                return ExactNumber.fromLong(result, this.scale);
            } catch (ArithmeticException e) {
                BigInteger bigResult = BigInteger.valueOf(this.longValue)
                        .subtract(BigInteger.valueOf(other.longValue));
                return ExactNumber.fromBigInteger(bigResult, this.scale);
            }
        }

        BigInteger val1 = this.isBig ? this.bigValue : BigInteger.valueOf(this.longValue);
        BigInteger val2 = other.isBig ? other.bigValue : BigInteger.valueOf(other.longValue);
        return ExactNumber.fromBigInteger(val1.subtract(val2), this.scale);
    }

    /**
     * Multiplies two ExactNumbers. Handles scale normalization automatically.
     */
    public ExactNumber multiply(ExactNumber other) {
        alignScalesGuard(other);

        // Security: Multiplying two 64-bit numbers easily overflows.
        // We promote to BigInteger strictly for the calculation to ensure safety,
        // relying on the Factory's down-promotion to return to 'long' if possible.
        BigInteger val1 = this.isBig ? this.bigValue : BigInteger.valueOf(this.longValue);
        BigInteger val2 = other.isBig ? other.bigValue : BigInteger.valueOf(other.longValue);

        // (A * B) -> scale is doubled
        BigInteger rawResult = val1.multiply(val2);

        // Normalize back to base scale
        BigInteger scaleFactor = BigInteger.TEN.pow(this.scale);
        BigInteger normalizedResult = rawResult.divide(scaleFactor);

        return ExactNumber.fromBigInteger(normalizedResult, this.scale);
    }

    /**
     * Divides by another ExactNumber. Handles scale preservation.
     */
    public ExactNumber divide(ExactNumber other) {
        alignScalesGuard(other);

        // Check for division by zero to prevent game crashes
        if ((!other.isBig && other.longValue == 0) ||
                (other.isBig && other.bigValue.equals(BigInteger.ZERO))) {
            throw new ArithmeticException("Engine Error: Division by zero.");
        }

        BigInteger val1 = this.isBig ? this.bigValue : BigInteger.valueOf(this.longValue);
        BigInteger val2 = other.isBig ? other.bigValue : BigInteger.valueOf(other.longValue);

        // Multiply numerator by scale factor BEFORE division to preserve precision decimals
        BigInteger scaleFactor = BigInteger.TEN.pow(this.scale);
        BigInteger scaledNumerator = val1.multiply(scaleFactor);
        BigInteger result = scaledNumerator.divide(val2);

        return ExactNumber.fromBigInteger(result, this.scale);
    }

    public int getScale() {
        return this.scale;
    }

    /**
     * Calculates the remainder (modulo) of division by another ExactNumber.
     */
    public ExactNumber remainder(ExactNumber other) {
        alignScalesGuard(other);

        // Security Check: Modulo by zero is mathematically undefined and crashes the JVM
        if ((!other.isBig && other.longValue == 0) ||
                (other.isBig && other.bigValue.equals(BigInteger.ZERO))) {
            throw new ArithmeticException("Engine Error: Modulo by zero.");
        }

        if (!this.isBig && !other.isBig) {
            // Native primitive modulo operation
            long result = this.longValue % other.longValue;
            return ExactNumber.fromLong(result, this.scale);
        }

        BigInteger val1 = this.isBig ? this.bigValue : BigInteger.valueOf(this.longValue);
        BigInteger val2 = other.isBig ? other.bigValue : BigInteger.valueOf(other.longValue);

        return ExactNumber.fromBigInteger(val1.remainder(val2), this.scale);
    }

    @Override
    public int compareTo(ExactNumber other) {
        alignScalesGuard(other);
        if (!this.isBig && !other.isBig) {
            return Long.compare(this.longValue, other.longValue);
        }
        BigInteger val1 = this.isBig ? this.bigValue : BigInteger.valueOf(this.longValue);
        BigInteger val2 = other.isBig ? other.bigValue : BigInteger.valueOf(other.longValue);
        return val1.compareTo(val2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExactNumber that = (ExactNumber) o;
        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        return isBig ? Objects.hash(bigValue, scale) : Objects.hash(longValue, scale);
    }
}