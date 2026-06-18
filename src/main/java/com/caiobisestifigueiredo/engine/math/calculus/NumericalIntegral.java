package com.caiobisestifigueiredo.engine.math.calculus;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin;

/**
 * Calculates the Definite Integral of a mathematical function
 * using the Numerical Trapezoidal Rule.
 */
public final class NumericalIntegral implements MathPlugin<ExactNumber> {

    private final MathFunction function;
    private final ExactNumber startA; // The lower limit of integration
    private final int slices;         // Number of trapezoids (precision)

    /**
     * @param function The function to integrate (e.g., a Polynomial).
     * @param startA The starting point 'a' on the x-axis.
     * @param slices Higher slices = more precision, but more CPU cost.
     */
    public NumericalIntegral(MathFunction function, ExactNumber startA, int slices) {
        if (slices <= 0) {
            throw new IllegalArgumentException("Slices must be greater than zero.");
        }
        this.function = function;
        this.startA = startA;
        this.slices = slices;
    }

    /**
     * Executes the definite integral from 'startA' up to 'endB' (passed as x).
     */
    @Override
    public ExactNumber execute(ExactNumber endB) {
        // Step 1: Calculate the width of each slice (h = (b - a) / slices)
        ExactNumber exactSlices = ExactNumber.fromLong(slices, startA.getScale());
        ExactNumber totalWidth = endB.subtract(startA);
        ExactNumber h = totalWidth.divide(exactSlices);

        // Step 2: Calculate the ends of the whole area -> (f(a) + f(b)) / 2
        ExactNumber fA = function.execute(startA);
        ExactNumber fB = function.execute(endB);
        ExactNumber endsSum = fA.add(fB);
        ExactNumber two = ExactNumber.fromLong(2, startA.getScale());

        ExactNumber area = endsSum.divide(two);

        // Step 3: Loop through the inner slices and accumulate f(x)
        ExactNumber sumInner = ExactNumber.fromLong(0, startA.getScale());

        for (int i = 1; i < slices; i++) {
            // currentX = a + (i * h)
            ExactNumber currentStep = ExactNumber.fromLong(i, startA.getScale()).multiply(h);
            ExactNumber currentX = startA.add(currentStep);

            sumInner = sumInner.add(function.execute(currentX));
        }

        // Step 4: Final Formula -> h * ( (f(a)+f(b))/2 + sumInner )
        return h.multiply(area.add(sumInner));
    }
}