package com.nemo.jennings;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public final class Util {

    public static int[] convertStringToBinArr(String binString) {
        int[] binArray = new int[binString.length()];
        for (int i = 0; i < binString.length(); i++) {
            binArray[i] = Integer.parseInt(String.valueOf(binString.charAt(i)));
        }
        return binArray;
    }

    public static void invertBinArr(int[] binArr) {
        for (int i = 0; i < binArr.length; i++) {
            if (binArr[i] == 1) {
                binArr[i] = -1;
            } else {
                binArr[i] = 1;
            }
        }
    }

    public static String convertOctToBin(int octal) {
        String binaryStr = "";
        String octalString = String.valueOf(octal);
        try {
            int bin = Integer.parseInt(octalString, 8);
            binaryStr = Integer.toBinaryString(bin);
        } catch (NumberFormatException e) {
            System.out.println("Cannot convert this octal number!");
        }
        return binaryStr;
    }

    public static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public static int convertBinArrayToDecimal(int[] binArray) {
        int decimal = 0;
        for (int i = binArray.length - 1; i >= 0; i--) {
            if (binArray[i] == 1) {
                decimal += Math.pow(2, binArray.length - i - 1);
            }
        }
        return decimal;
    }

    private static int logb(int a, int b) {
        if (a == 0 || b == 0) throw new ArithmeticException("Can't calculate logarithm of 0");
        return (int) (Math.log(a) / Math.log(b));
    }

    public static int log2(int a) {
        return logb(a, 2);
    }

    public static int mulAndAddSequence(int[] a, int[] b) {
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    public static void shiftSequence(int[] y) {
        int yLast = y[y.length - 1];
        for (int i = y.length - 2; i >= 0; i--) {
            y[i + 1] = y[i];
        }
        y[0] = yLast;
    }

    public static boolean isRemainderZero(double x) {
        String[] splitter = String.valueOf(x).split("\\.");
        int i = splitter[1].length();
        if (i == 1 && splitter[1].equals("0")) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param isTextField
     * true - textfields, false - labels
     */
    public static void addStateToGridPane(GridPane gridPane, Integer[] state, boolean isTextField) {
        gridPane.getChildren().clear();
        if (!gridPane.isVisible()) {
            gridPane.setVisible(true);
        }
        if (isTextField) {
            for (int i = 0; i < state.length; i++) {
                TextField cell = new TextField();
                cell.setPrefHeight(25);
                cell.setPrefWidth(25);
                cell.setAlignment(Pos.CENTER);
                cell.setText(String.valueOf(state[i]));
                gridPane.add(cell, i, 0);
            }
        } else {
            for (int i = 0; i < state.length; i++) {
                Label cell = new Label();
                cell.setPrefHeight(15);
                cell.setPrefWidth(15);
                cell.setAlignment(Pos.CENTER);
                cell.setText(String.valueOf(state[i]));
                gridPane.add(cell, i, 0);
            }
        }
    }

}
