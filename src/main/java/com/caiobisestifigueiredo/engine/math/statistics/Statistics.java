package com.caiobisestifigueiredo.engine.math.statistics;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Statistical analysis module for game economy, mob behavior, and damage calculation.
 */
public final class Statistics {

    private Statistics() {}

    /**
     * Arithmetic Mean (Average).
     */
    public static ExactNumber mean(ExactNumber... data) {
        if (data == null || data.length == 0) throw new IllegalArgumentException("Empty dataset.");

        ExactNumber sum = ExactNumber.fromLong(0, data[0].getScale());
        for (ExactNumber num : data) {
            sum = sum.add(num);
        }

        ExactNumber count = ExactNumber.fromLong(data.length, data[0].getScale());
        return sum.divide(count);
    }

    /**
     * Median (Middle value). Performs a Defensive Copy to protect original arrays.
     */
    public static ExactNumber median(ExactNumber... data) {
        if (data == null || data.length == 0) throw new IllegalArgumentException("Empty dataset.");

        // Defensive Copy to prevent Side-Effects
        ExactNumber[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);

        int mid = copy.length / 2;
        if (copy.length % 2 == 0) {
            // Even number of elements: average of the two middle ones
            ExactNumber two = ExactNumber.fromLong(2, copy[0].getScale());
            return copy[mid - 1].add(copy[mid]).divide(two);
        } else {
            // Odd number of elements
            return copy[mid];
        }
    }

    /**
     * Mode (Most frequent value).
     */
    public static ExactNumber mode(ExactNumber... data) {
        if (data == null || data.length == 0) throw new IllegalArgumentException("Empty dataset.");

        Map<ExactNumber, Integer> frequencyMap = new HashMap<>();
        int maxFreq = 0;
        ExactNumber modeValue = data[0];

        for (ExactNumber num : data) {
            int freq = frequencyMap.getOrDefault(num, 0) + 1;
            frequencyMap.put(num, freq);

            if (freq > maxFreq) {
                maxFreq = freq;
                modeValue = num;
            }
        }
        return modeValue;
    }
}