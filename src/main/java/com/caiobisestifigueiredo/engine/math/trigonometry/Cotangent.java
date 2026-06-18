package com.caiobisestifigueiredo.engine.math.trigonometry;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin;

/**
 * Calculates Cotangent (Cos / Sin)
 */
public final class Cotangent implements MathPlugin<ExactNumber> {
    private final int terms;

    public Cotangent(int terms) {
        this.terms = terms;
    }

    @Override
    public ExactNumber execute(ExactNumber x) {
        ExactNumber sin = x.apply(new Sine(terms));
        if (sin.equals(ExactNumber.fromLong(0, x.getScale()))) {
            throw new ArithmeticException("Engine Error: Cotangent undefined. Sine is zero.");
        }
        ExactNumber cos = x.apply(new Cosine(terms));
        return cos.divide(sin);
    }
}
