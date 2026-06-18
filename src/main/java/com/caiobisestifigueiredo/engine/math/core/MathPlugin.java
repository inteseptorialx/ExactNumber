package com.caiobisestifigueiredo.engine.math.core;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;

/**
 * Strategy/Microkernel interface for external module integration.
 * @param <R> The return type of the plugin operation.
 */
@FunctionalInterface
public interface MathPlugin<R> {
    R execute(ExactNumber context);
}