package com.caiobisestifigueiredo.engine.math.geometry;

import com.caiobisestifigueiredo.engine.math.constants.MathConstants;
import com.caiobisestifigueiredo.engine.math.core.ExactNumber;

/**
 * Spatial and Plane Geometry Module.
 */
public final class Geometry {

    private Geometry() {
        // Utility class, do not instantiate
    }

    /**
     * Calculates the area of a circle: A = PI * r^2
     */
    public static ExactNumber areaOfCircle(ExactNumber radius) {
        ExactNumber radiusSquared = radius.multiply(radius);
        return MathConstants.getInstance().PI.multiply(radiusSquared);
    }

    /**
     * Calculates the volume of a sphere: V = (4/3) * PI * r^3
     */
    public static ExactNumber volumeOfSphere(ExactNumber radius) {
        int scale = radius.getScale();
        ExactNumber four = ExactNumber.fromLong(4, scale);
        ExactNumber three = ExactNumber.fromLong(3, scale);

        ExactNumber radiusCubed = radius.multiply(radius).multiply(radius);

        // V = 4 * PI * r^3 / 3
        return four.multiply(MathConstants.getInstance().PI)
                .multiply(radiusCubed)
                .divide(three);
    }
}