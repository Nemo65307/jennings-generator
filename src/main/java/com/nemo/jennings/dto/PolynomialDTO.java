package com.nemo.jennings.dto;

import java.io.Serializable;

public class PolynomialDTO implements Serializable {

    private short n;
    private short j;
    private long octal;
    private char letter;

    public PolynomialDTO() {
    }

    public PolynomialDTO(short n, short j, long octal, char letter) {
        this.n = n;
        this.j = j;
        this.octal = octal;
        this.letter = letter;
    }

    public short getN() {
        return n;
    }

    public void setN(short n) {
        this.n = n;
    }

    public short getJ() {
        return j;
    }

    public void setJ(short j) {
        this.j = j;
    }

    public long getOctal() {
        return octal;
    }

    public void setOctal(long octal) {
        this.octal = octal;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "PolynomialDTO{" +
                "n=" + n +
                ", j=" + j +
                ", octal=" + octal +
                ", letter=" + letter +
                '}';
    }
}
