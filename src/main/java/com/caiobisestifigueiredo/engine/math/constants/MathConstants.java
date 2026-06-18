package com.caiobisestifigueiredo.engine.math.constants;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;

/**
 * Singleton pattern managing universal mathematical constants.
 * Prevents memory allocation overhead during heavy game ticks.
 */
public final class MathConstants {

    // Eager initialization: created immediately when the class is loaded.
    private static final MathConstants INSTANCE = new MathConstants();

    // Universal Constants pooled in memory
    public final ExactNumber PI;
    public final ExactNumber EULER;
    public final ExactNumber GOLDEN_RATIO;

    /**
     * Private constructor prevents external instantiation.
     */
    private MathConstants() {
        // Assuming the engine uses a default scale of 6 (10^6)
        this.PI = ExactNumber.fromDouble(3.141592, 6);
        this.EULER = ExactNumber.fromDouble(2.718281, 6);
        this.GOLDEN_RATIO = ExactNumber.fromDouble(1.618033, 6);
    }

    /**
     * Global access point to the constants pool.
     */
    public static MathConstants getInstance() {
        return INSTANCE;
    }
}