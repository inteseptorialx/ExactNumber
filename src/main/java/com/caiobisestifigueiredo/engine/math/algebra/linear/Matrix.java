package com.caiobisestifigueiredo.engine.math.algebra.linear;

import com.caiobisestifigueiredo.engine.math.core.ExactNumber;

import java.util.Arrays;

/**
 * Immutable representation of a Mathematical Matrix utilizing ExactNumber Fixed-Point logic.
 */
public final class Matrix {

    private final ExactNumber[][] data;
    private final int rows;
    private final int cols;

    /**
     * Constructs a Matrix. Performs a defensive copy to guarantee thread-safety and immutability.
     */
    public Matrix(ExactNumber[][] inputData) {
        if (inputData == null || inputData.length == 0 || inputData[0].length == 0) {
            throw new IllegalArgumentException("Matrix data cannot be empty.");
        }

        this.rows = inputData.length;
        this.cols = inputData[0].length;
        this.data = new ExactNumber[rows][cols];

        // Defensive Copy
        for (int i = 0; i < rows; i++) {
            if (inputData[i].length != cols) {
                throw new IllegalArgumentException("Matrix must be rectangular. Row " + i + " length mismatch.");
            }
            this.data[i] = Arrays.copyOf(inputData[i], cols);
        }
    }

    /**
     * Builder pattern: Adds another Matrix to this one.
     */
    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new ArithmeticException("Matrix addition requires identical dimensions.");
        }

        ExactNumber[][] resultData = new ExactNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                resultData[i][j] = this.data[i][j].add(other.data[i][j]);
            }
        }
        return new Matrix(resultData);
    }

    /**
     * Builder pattern: Multiplies this Matrix by another Matrix.
     */
    public Matrix multiply(Matrix other) {
        // Validation boundary rule goes here (See Fixation Exercise)
        if (this.cols != other.rows) {
            throw new ArithmeticException("Matrix multiplication dimension mismatch: A.cols must equal B.rows.");
        }

        ExactNumber[][] resultData = new ExactNumber[this.rows][other.cols];

        // The Triple Loop (O(N^3) complexity)
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                // Initialize the cell with ZERO using the scale of the first element
                ExactNumber sum = ExactNumber.fromLong(0, this.data[0][0].getScale());

                for (int k = 0; k < this.cols; k++) {
                    ExactNumber product = this.data[i][k].multiply(other.data[k][j]);
                    sum = sum.add(product);
                }
                resultData[i][j] = sum;
            }
        }

        return new Matrix(resultData);
    }

    /**
     * Safe Getter returning a defensive copy.
     */
    public ExactNumber[][] getData() {
        ExactNumber[][] copy = new ExactNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            copy[i] = Arrays.copyOf(this.data[i], cols);
        }
        return copy;
    }
}