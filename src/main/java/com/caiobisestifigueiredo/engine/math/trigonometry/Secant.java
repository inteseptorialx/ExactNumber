package com.caiobisestifigueiredo.engine.math.trigonometry;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin; /**
 * Calculates Secant (1 / Cos)
 */
public final class Secant implements MathPlugin<ExactNumber> {
    private final int terms;

    public Secant(int terms) { this.terms = terms; }

    @Override
    public ExactNumber execute(ExactNumber x) {
        ExactNumber cos = x.apply(new Cosine(terms));
        if (cos.equals(ExactNumber.fromLong(0, x.getScale()))) {
            throw new ArithmeticException("Engine Error: Secant undefined. Cosine is zero.");
        }
        return ExactNumber.fromLong(1, x.getScale()).divide(cos);
    }
}
