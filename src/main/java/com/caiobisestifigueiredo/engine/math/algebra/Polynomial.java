package com.caiobisestifigueiredo.engine.math.algebra;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a mathematical polynomial expression.
 * Evaluates the expression for a given 'x' using Horner's Method.
 */
public final class Polynomial implements MathPlugin<ExactNumber> {

    // Coefficients ordered from lowest degree (index 0) to highest degree (index n)
    // Example for f(x) = 5 + 2x + 3x^2: coefficients = [5, 2, 3]
    private final List<ExactNumber> coefficients;

    /**
     * Builder/Constructor using varargs for fluent instantiation.
     */
    public Polynomial(ExactNumber... coeffs) {
        if (coeffs == null || coeffs.length == 0) {
            throw new IllegalArgumentException("Polynomial must have at least one coefficient.");
        }
        // Immutable list to guarantee thread-safety
        this.coefficients = Collections.unmodifiableList(Arrays.asList(coeffs));
    }

    /**
     * Evaluates the polynomial at the given point 'x'.
     * This acts as the Strategy execution for our Microkernel hook.
     */
    @Override
    public ExactNumber execute(ExactNumber x) {
        int degree = coefficients.size() - 1;

        // Horner's Method Algorithm
        ExactNumber result = coefficients.get(degree);

        for (int i = degree - 1; i >= 0; i--) {
            // result = result * x + coefficient[i]
            result = result.multiply(x).add(coefficients.get(i));
        }

        return result;
    }

    /**
     * Retrieves the degree of the polynomial.
     */
    public int getDegree() {
        return coefficients.size() - 1;
    }
}