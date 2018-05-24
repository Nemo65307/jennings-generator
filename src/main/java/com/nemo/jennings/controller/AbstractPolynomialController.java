package com.nemo.jennings.controller;

import com.nemo.jennings.dao.PolynomialDAO;
import com.nemo.jennings.dao.impl.DBPolynomialDAO;
import com.nemo.jennings.dto.PolynomialDTO;
import com.nemo.jennings.object.Polynomial;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;

import static com.nemo.jennings.Util.addStateToGridPane;

abstract class AbstractPolynomialController {

    private final PolynomialDAO loader;

    AbstractPolynomialController() {
        loader = new DBPolynomialDAO();
    }

    List<Integer> loadAllN() {
        Set<Integer> ns = new HashSet<>();
        List<PolynomialDTO> dtos = loader.loadAll();
        for (PolynomialDTO dto : dtos) {
            ns.add((int) dto.getN());
        }
        return new ArrayList<>(ns);
    }

    List<Integer> loadInN(int n1, int n2, int n3) {
        Set<Integer> ns = new HashSet<>();
        List<PolynomialDTO> dtos = loader.loadInN((short) n1, (short) n2, (short) n3);
        for (PolynomialDTO dto : dtos) {
            ns.add((int) dto.getN());
        }
        return new ArrayList<>(ns);
    }

    List<Integer> loadByN(int n) {
        Set<Integer> ns = new HashSet<>();
        List<PolynomialDTO> dtos = loader.loadByN((short) n);
        for (PolynomialDTO dto : dtos) {
            int currentN = (int) dto.getN();
            ns.add(currentN);
        }
        return new ArrayList<>(ns);
    }

    List<Polynomial> loadPolynomialsByN(short n) {
        List<PolynomialDTO> dtos = loader.loadByN(n);
        List<Polynomial> entities = new ArrayList<>(dtos.size());
        for (PolynomialDTO dto : dtos) {
            Polynomial entity = new Polynomial(dto);
            entities.add(entity);
        }
        return entities;
    }

    void addMatrixToGridPane(TitledPane matrixTitledPane, GridPane gridPane, int[][] matrix) {
        gridPane.getChildren().clear();
        if (!matrixTitledPane.isVisible()) {
            matrixTitledPane.setVisible(true);
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                Label cell = new Label();
                cell.setPrefHeight(16);
                cell.setPrefWidth(16);
                cell.setAlignment(Pos.CENTER);
                cell.setText(String.valueOf(matrix[i][j]));
                gridPane.add(cell, j, i);
            }
        }
    }

    void setupInitialState(GridPane gridPane, Polynomial polynomial) {
        gridPane.getChildren().clear();
        if (!gridPane.isVisible()) {
            gridPane.setVisible(true);
        }
        Integer[] y0 = new Integer[polynomial.getN()];
        Arrays.fill(y0, 0);
        y0[y0.length - 1] = 1;
        addStateToGridPane(gridPane, y0, true);
    }

    void setLabelsVisible(Label... labels) {
        for (Label label : labels) {
            label.setVisible(true);
        }
    }

    void setErrorMessage(Label label, String message) {
        if (!label.isVisible()) {
            label.setVisible(true);
        }
        label.setText(message);
        label.setTextFill(Color.web("red"));
    }

    boolean validateInitStatePane(GridPane initStateGridPane, Label initStateErrorLabel) {
        if (initStateGridPane == null) return false;
        for (Node node : initStateGridPane.getChildren()) {
            if (node instanceof TextField) {
                String currentValue = ((TextField) node).getText();
                if (!(currentValue.equals("0") || currentValue.equals("1"))) {
                    setErrorMessage(initStateErrorLabel, "Only 0 or 1");
                    return false;
                }
            }
        }
        return true;
    }

    int[] getSelectedInitialState(GridPane initStateGridPane, Label initStateErrorLabel) {
        if (!validateInitStatePane(initStateGridPane, initStateErrorLabel)) {
            return null;
        }
        List<Integer> initStateList = new ArrayList<>();
        for (Node node : initStateGridPane.getChildren()) {
            if (node instanceof TextField) {
                Integer currentValue = Integer.valueOf(((TextField) node).getText());
                initStateList.add(currentValue);
            }
        }
        return initStateList.stream().mapToInt(i -> i).toArray();
    }

}