package com.nemo.jennings.object;

import com.nemo.jennings.dto.PolynomialDTO;
import com.nemo.jennings.Util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Polynomial {

    private final int octal;
    private final char letter;
    private final String binary;
    private final int j;
    private final int n;
    private final int period;
    private final String fxPolynomial;
    private final int[] controlIndexes;
    private final int[][] matrix;

    public Polynomial(PolynomialDTO dto) {
        this((int) dto.getOctal(), dto.getJ(), dto.getLetter());
    }

    public Polynomial(int octal, int j, char letter) {
        this.octal = octal;
        this.letter = letter;
        this.j = j;
        this.binary = Util.convertOctToBin(octal);
        this.n = binary.length() - 1;
        this.fxPolynomial = buildFxPolynomial();
        this.period = calculatePeriod();
        this.controlIndexes = findControlIndexes();
        this.matrix = buildMatrix();
    }

    private int[] findControlIndexes() {
        List<Integer> indexesOf1 = new LinkedList<>();
        for (int i = 1; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                indexesOf1.add(i - 1);
            }
        }
        return indexesOf1.stream().mapToInt(i -> i).toArray(); //converts list to int array
    }

    private int calculatePeriod() {
        return (int) ((Math.pow(2, n) - 1) / (Util.gcd((int) (Math.pow(2, n) - 1), j)));
    }

    private String buildFxPolynomial() {
        String reverseBinary = new StringBuilder(binary).reverse().toString();
        List<Integer> indexesOf1 = new LinkedList<>();
        StringBuilder polynomial = new StringBuilder();
        for (int i = 0; i < binary.length(); i++) {
            if (reverseBinary.charAt(i) == '1') {
                indexesOf1.add(i);
            }
        }
        Collections.reverse(indexesOf1);
        for (Integer i : indexesOf1) {
            if (i.equals(0)) {
                polynomial.append("1");
            } else {
                polynomial.append("x^").append(i).append(" + ");
            }
        }
        return polynomial.toString();
    }

    private int[][] buildMatrix() {
        int[][] matrix = new int[n][n];
        int[] firstLine = Util.convertStringToBinArr(binary.substring(1)); // leading digit is removed (n*n)
        matrix[0] = firstLine;
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if ((i - j) == 1) {
                    matrix[i][j] = 1;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
        return matrix;
    }

    public int getOctal() {
        return octal;
    }

    public String getBinary() {
        return binary;
    }

    public int getJ() {
        return j;
    }

    public int getN() {
        return n;
    }

    public int getPeriod() {
        return period;
    }

    public String getFxPolynomial() {
        return fxPolynomial;
    }

    public int[] getControlIndexes() {
        return controlIndexes.clone();
    }

    public int[][] getMatrix() {
        return matrix.clone();
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public String toString() {
        return n + " " + octal + " " + letter + " j" + j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polynomial that = (Polynomial) o;
        return octal == that.octal &&
                letter == that.letter &&
                j == that.j &&
                n == that.n &&
                period == that.period &&
                Objects.equals(binary, that.binary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(octal, letter, binary, j, n, period);
    }
}
