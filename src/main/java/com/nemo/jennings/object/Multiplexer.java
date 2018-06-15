package com.nemo.jennings.object;

import com.nemo.jennings.Util;
import com.nemo.jennings.exception.ApplicationException;

public class Multiplexer {

    private LFSR input;
    private LFSR selector;
    private long period;
    private boolean isFull;
    private int inputN;

    public Multiplexer(LFSR input, LFSR selector) {
        int inputN = input.getPolynomial().getN();
        int selectorN = selector.getPolynomial().getN();
        if (inputN > Math.pow(2, selectorN)) {
            throw new ApplicationException("Selector should have pins no less than log2 of input pins!");
        }
        if (Util.gcd(input.getPolynomial().getPeriod(), selector.getPolynomial().getPeriod()) != 1) {
            throw new ApplicationException("Periods aren't coprime!");
        }
        this.input = input;
        this.selector = selector;
        isFull = !(inputN < Math.pow(2, selectorN));
        this.inputN = inputN;
        this.period = calculatePeriod();
    }

    public String run(long rounds) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < rounds; i++) {
            int nextBit = selectInput();
            output.append(nextBit);
            boolean isT1 = input.shift();
            boolean isT2 = selector.shift();
            if (isT1 && isT2) {
                output.append(" End of period\n");
            }
        }
        return String.valueOf(output);
    }

    private int selectInput() {
        int[] yInput = input.getY();
        int[] ySelector = selector.getY();
        int inputIndex;
        inputIndex = Util.convertBinArrayToDecimal(ySelector);
        if (!isFull && inputIndex > inputN) {
            inputIndex = inputN;
        }
        return yInput[inputIndex - 1];
    }

    private long calculatePeriod() {
        return input.getPolynomial().getPeriod() * selector.getPolynomial().getPeriod();
    }

    public long getPeriod() {
        return period;
    }

}