package com.caiobisestifigueiredo.engine.math.calculus;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin;

/**
 * Calculates the numerical derivative of a given function at point 'x'
 * using the Central Finite Difference method.
 */
public final class NumericalDerivative implements MathPlugin<ExactNumber> {

    private final MathFunction function;
    private final ExactNumber h; // The infinitesimal step (e.g., 0.001)

    /**
     * @param function The mathematical function to be derivated.
     * @param h The step size. Smaller is more accurate, up to the fixed-point limit.
     */
    public NumericalDerivative(MathFunction function, ExactNumber h) {
        this.function = function;
        this.h = h;
    }

    @Override
    public ExactNumber execute(ExactNumber x) {
        // Step 1: Calculate f(x + h)
        ExactNumber xPlusH = x.add(h);
        ExactNumber fPlus = function.execute(xPlusH);

        // Step 2: Calculate f(x - h)
        ExactNumber xMinusH = x.subtract(h);
        ExactNumber fMinus = function.execute(xMinusH);

        // Step 3: Numerator -> f(x + h) - f(x - h)
        ExactNumber numerator = fPlus.subtract(fMinus);

        // Step 4: Denominator -> 2 * h
        // Optimization Trick: Instead of costly multiplication, h + h is natively 2h!
        ExactNumber denominator = h.add(h);

        // Step 5: Result -> Numerator / Denominator
        return numerator.divide(denominator);
    }
}