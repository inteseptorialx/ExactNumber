package com.caiobisestifigueiredo.engine.math.trigonometry;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin;

/**
 * Calculates Cosecant (1 / Sin)
 */
public final class Cosecant implements MathPlugin<ExactNumber> {
    private final int terms;

    public Cosecant(int terms) {
        this.terms = terms;
    }

    @Override
    public ExactNumber execute(ExactNumber x) {
        ExactNumber sin = x.apply(new Sine(terms));
        if (sin.equals(ExactNumber.fromLong(0, x.getScale()))) {
            throw new ArithmeticException("Engine Error: Cosecant undefined. Sine is zero.");
        }
        return ExactNumber.fromLong(1, x.getScale()).divide(sin);
    }
}
