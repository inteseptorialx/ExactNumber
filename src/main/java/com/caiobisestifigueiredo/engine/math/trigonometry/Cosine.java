package com.caiobisestifigueiredo.engine.math.trigonometry;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin;

/**
 * Calculates the Cosine of an angle (in radians) using an optimized Taylor Series.
 */
public final class Cosine implements MathPlugin<ExactNumber> {

    private final int terms;

    public Cosine(int terms) {
        if (terms <= 0) {
            throw new IllegalArgumentException("Terms must be positive.");
        }
        this.terms = terms;
    }

    @Override
    public ExactNumber execute(ExactNumber x) {
        ExactNumber result = ExactNumber.fromLong(1, x.getScale());
        ExactNumber currentTerm = ExactNumber.fromLong(1, x.getScale());
        ExactNumber xSquared = x.multiply(x);

        for (int i = 1; i < terms; i++) {
            // Factorial denominators optimized dynamically: (2i) * (2i - 1)
            long val1 = 2L * i;
            long val2 = val1 - 1L;
            ExactNumber denominator = ExactNumber.fromLong(val1 * val2, x.getScale());

            // next_term = current_term * x^2 / denominator
            currentTerm = currentTerm.multiply(xSquared).divide(denominator);

            // Alternate signs: - + - +
            if (i % 2 != 0) {
                result = result.subtract(currentTerm);
            } else {
                result = result.add(currentTerm);
            }
        }

        return result;
    }
}