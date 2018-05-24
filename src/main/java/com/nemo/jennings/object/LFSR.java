package com.nemo.jennings.object;

import java.util.Arrays;

public class LFSR {

    private final Polynomial polynomial;
    private int[] y0; // initial state
    private int[] y; // current state
    private final int[][] matrix;

    public LFSR(Polynomial polynomial, int[] y0) {
        this.polynomial = polynomial;
        this.y = Arrays.copyOf(y0, y0.length);
        this.y0 = y.clone();
        this.matrix = polynomial.getMatrix();
    }

    public int[] getY() {
        return y.clone();
    }

    public int[] getY0() {
        return y0.clone();
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }

    boolean shift() {
        int[] temp = new int[y.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < y.length; j++) {
                temp[i] += matrix[i][j] * y[j];
                if (temp[i] % 2 == 0) temp[i] = 0;
            }
        }
        y = temp;
        return Arrays.equals(y, y0);
    }

    public void run(int rounds) {
        for (int i = 0; i < rounds; i++) {
            shift();
        }
    }
}
