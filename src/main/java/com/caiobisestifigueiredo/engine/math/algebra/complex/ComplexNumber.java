package com.caiobisestifigueiredo.engine.math.algebra.complex;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;

import java.util.Objects;

/**
 * Immutable representation of a Complex Number (a + bi) on the Argand-Gauss plane.
 * Powered internally by the ExactNumber architecture.
 */
public final class ComplexNumber {

    private final ExactNumber real;
    private final ExactNumber imaginary;

    /**
     * Builder/Constructor for the Complex Number.
     */
    public ComplexNumber(ExactNumber real, ExactNumber imaginary) {
        if (real.getScale() != imaginary.getScale()) {
            throw new IllegalArgumentException("Scales of Real and Imaginary parts must match.");
        }
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Adds another Complex Number.
     * (a + bi) + (c + di) = (a + c) + (b + d)i
     */
    public ComplexNumber add(ComplexNumber other) {
        ExactNumber newReal = this.real.add(other.real);
        ExactNumber newImaginary = this.imaginary.add(other.imaginary);
        return new ComplexNumber(newReal, newImaginary);
    }

    /**
     * Subtracts another Complex Number.
     * (a + bi) - (c + di) = (a - c) + (b - d)i
     */
    public ComplexNumber subtract(ComplexNumber other) {
        ExactNumber newReal = this.real.subtract(other.real);
        ExactNumber newImaginary = this.imaginary.subtract(other.imaginary);
        return new ComplexNumber(newReal, newImaginary);
    }

    /**
     * Multiplies by another Complex Number using FOIL distribution.
     * (a + bi) * (c + di) = (ac - bd) + (ad + bc)i
     */
    public ComplexNumber multiply(ComplexNumber other) {
        // ac
        ExactNumber ac = this.real.multiply(other.real);
        // bd
        ExactNumber bd = this.imaginary.multiply(other.imaginary);
        // ad
        ExactNumber ad = this.real.multiply(other.imaginary);
        // bc
        ExactNumber bc = this.imaginary.multiply(other.real);

        // Real Part: ac - bd (Notice the subtraction because i^2 = -1)
        ExactNumber newReal = ac.subtract(bd);

        // Imaginary Part: ad + bc
        ExactNumber newImaginary = ad.add(bc);

        return new ComplexNumber(newReal, newImaginary);
    }

    // --- GETTERS & UTILITIES ---

    public ExactNumber getReal() {
        return real;
    }

    public ExactNumber getImaginary() {
        return imaginary;
    }

    /**
     * Formats the complex number into a readable mathematical string.
     */
    public String toFormattedString() {
        String realStr = real.toFormattedString();
        String imagStr = imaginary.toFormattedString();

        // Handle the sign elegantly for display
        if (imagStr.startsWith("-")) {
            return realStr + " - " + imagStr.substring(1) + "i";
        }
        return realStr + " + " + imagStr + "i";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return real.equals(that.real) && imaginary.equals(that.imaginary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }
}