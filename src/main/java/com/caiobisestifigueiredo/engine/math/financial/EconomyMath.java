package com.caiobisestifigueiredo.engine.math.financial;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;

/**
 * Financial module for server economy, banking systems, and inflation control.
 * Prevents floating-point duping vulnerabilities.
 */
public final class EconomyMath {

    private EconomyMath() {
        // Stateless utility class
    }

    /**
     * Calculates Simple Interest (Linear economy growth).
     * Formula: Amount = Principal * (1 + (rate * time))
     */
    public static ExactNumber simpleInterest(ExactNumber principal, ExactNumber rate, ExactNumber time) {
        ExactNumber rateTimeProduct = rate.multiply(time);
        ExactNumber one = ExactNumber.fromLong(1, principal.getScale());

        ExactNumber multiplier = one.add(rateTimeProduct);
        return principal.multiply(multiplier);
    }

    /**
     * Calculates Compound Interest (Exponential economy growth).
     * Formula: Amount = Principal * (1 + rate)^time
     * * @param principal Initial money.
     * @param rate Interest rate per tick/day (e.g., 0.05 for 5%).
     * @param periods Number of compounding periods.
     */
    public static ExactNumber compoundInterest(ExactNumber principal, ExactNumber rate, int periods) {
        if (periods < 0) {
            throw new IllegalArgumentException("Compounding periods cannot be negative.");
        }
        if (periods == 0) {
            return principal;
        }

        ExactNumber one = ExactNumber.fromLong(1, principal.getScale());
        ExactNumber base = one.add(rate); // (1 + r)

        // Fast Exponentiation for O(log N) performance
        ExactNumber multiplier = exactPow(base, periods);

        return principal.multiply(multiplier);
    }

    /**
     * Fast exponentiation by squaring to calculate (base^exp).
     */
    private static ExactNumber exactPow(ExactNumber base, int exp) {
        ExactNumber result = ExactNumber.fromLong(1, base.getScale());
        ExactNumber currentPower = base;
        int currentExp = exp;

        while (currentExp > 0) {
            if (currentExp % 2 == 1) {
                result = result.multiply(currentPower);
            }
            currentPower = currentPower.multiply(currentPower);
            currentExp /= 2;
        }
        return result;
    }
}