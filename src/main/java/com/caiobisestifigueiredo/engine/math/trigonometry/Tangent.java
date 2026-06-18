package com.caiobisestifigueiredo.engine.math.trigonometry;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin;

/**
 * Calculates Tangent (Sin / Cos)
 */
public final class Tangent implements MathPlugin<ExactNumber> {
    private final int terms;

    public Tangent(int terms) { this.terms = terms; }

    @Override
    public ExactNumber execute(ExactNumber x) {
        ExactNumber sin = x.apply(new Sine(terms));
        ExactNumber cos = x.apply(new Cosine(terms));

        // Security check: Tangent of 90 degrees (PI/2) has Cosine = 0.
        if (cos.equals(ExactNumber.fromLong(0, x.getScale()))) {
            throw new ArithmeticException("Engine Error: Tangent undefined. Cosine is zero.");
        }
        return sin.divide(cos);
    }
}

