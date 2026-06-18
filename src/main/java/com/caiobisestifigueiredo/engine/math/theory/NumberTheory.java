package com.caiobisestifigueiredo.engine.math.theory;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.algebra.linear.Matrix;

/**
 * Number Theory utilities: Progressions, Sequences, GCD, and LCM.
 */
public final class NumberTheory {

    private NumberTheory() {} // Stateless utility class

    /**
     * Calculates the N-th Fibonacci number using O(log N) Matrix Exponentiation.
     * Prevents CPU starvation on massively large game ticks.
     */
    public static ExactNumber fibonacci(int n, int scale) {
        if (n <= 0) return ExactNumber.fromLong(0, scale);
        if (n == 1) return ExactNumber.fromLong(1, scale);

        ExactNumber one = ExactNumber.fromLong(1, scale);
        ExactNumber zero = ExactNumber.fromLong(0, scale);

        // Transformation Matrix: [[1, 1], [1, 0]]
        ExactNumber[][] baseData = {
                {one, one},
                {one, zero}
        };
        Matrix baseMatrix = new Matrix(baseData);
        Matrix resultMatrix = matrixPower(baseMatrix, n - 1);

        // The answer is located at M[0][0] after N-1 multiplications
        return resultMatrix.getData()[0][0];
    }

    // Exponentiation by squaring for O(log N) performance
    private static Matrix matrixPower(Matrix base, int exp) {
        Matrix result = null; // Identity matrix will be formed implicitly
        Matrix currentPower = base;

        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result == null) ? currentPower : result.multiply(currentPower);
            }
            currentPower = currentPower.multiply(currentPower);
            exp /= 2;
        }
        return result;
    }

    /**
     * Greatest Common Divisor (GCD) using the Euclidean Algorithm.
     */
    public static ExactNumber gcd(ExactNumber a, ExactNumber b) {
        ExactNumber zero = ExactNumber.fromLong(0, a.getScale());
        ExactNumber currentA = a;
        ExactNumber currentB = b;

        while (!currentB.equals(zero)) {
            ExactNumber temp = currentB;
            // remainder (modulo) operation required in ExactNumber
            currentB = currentA.remainder(currentB);
            currentA = temp;
        }
        return currentA;
    }

    /**
     * Lowest Common Multiple (LCM) -> Formula: (|a * b|) / gcd(a, b)
     */
    public static ExactNumber lcm(ExactNumber a, ExactNumber b) {
        ExactNumber zero = ExactNumber.fromLong(0, a.getScale());
        if (a.equals(zero) || b.equals(zero)) return zero;

        ExactNumber absoluteProduct = a.multiply(b); // Assuming positive inputs for simplicity
        return absoluteProduct.divide(gcd(a, b));
    }
}