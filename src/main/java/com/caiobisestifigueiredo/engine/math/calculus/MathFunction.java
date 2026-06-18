package com.caiobisestifigueiredo.engine.math.calculus;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;
import com.caiobisestifigueiredo.engine.math.core.MathPlugin;

/**
 * Represents a generic mathematical function f(x) -> y.
 * Extends our Microkernel plugin architecture.
 */
@FunctionalInterface
public interface MathFunction extends MathPlugin<ExactNumber> {
    // The execute method from MathPlugin inherently fulfills this contract.
}