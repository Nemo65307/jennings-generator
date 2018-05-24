package com.nemo.jennings.controller;

import com.nemo.jennings.Util;
import com.nemo.jennings.object.LFSR;
import com.nemo.jennings.object.Polynomial;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class SecondPolynomialController extends AbstractPolynomialController {

    @FXML
    private ComboBox<Integer> selectNComboBox;
    @FXML
    private ComboBox<Polynomial> selectPolyComboBox;
    @FXML
    private Label loadingLabel;
    @FXML
    private Label nLabel;
    @FXML
    private Label tLabel;
    @FXML
    private Label fxLabel;
    @FXML
    private Label initStateLabel;
    @FXML
    private Label initStateErrorLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label isFullLabel;
    @FXML
    private GridPane matrixGridPane;
    @FXML
    private TitledPane matrixTitledPane;
    @FXML
    private GridPane initStateGridPane;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;

    private LFSR prevLfsr;

    void initData(LFSR prevLfsr) {
        this.prevLfsr = prevLfsr;
        Polynomial prevPolynomial = prevLfsr.getPolynomial();
        int thisN = Util.log2(prevPolynomial.getN()) + 1;
        List<Integer> listOfN = loadByN(thisN);
        selectNComboBox.getItems().addAll(listOfN);
        addSelectedNListener(listOfN);
        addSelectedPolyListener();
        loadingLabel.setVisible(false);
    }

    @SuppressWarnings("Duplicates")
    private void addSelectedNListener(List<Integer> listOfN) {
        selectNComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
        {
            if (selectPolyComboBox.isDisabled()) {
                selectPolyComboBox.setDisable(false);
            }
            isFullLabel.setVisible(false);
            short newN = listOfN.get(newValue.intValue()).shortValue();
            if (((int) (Math.pow(2, newN)) - 1) != prevLfsr.getPolynomial().getN()) {
                isFullLabel.setText("Multiplexer will be incomplete");
                isFullLabel.setVisible(true);
            }
            ObservableList<Polynomial> polynomials = selectPolyComboBox.getItems();
            polynomials.clear();
            List<Polynomial> loadedPolys = loadPolynomialsByN(newN);
            if (loadedPolys.size() != 0) {
                errorLabel.setVisible(false);
                polynomials.addAll(loadedPolys);
            } else {
                setErrorMessage(errorLabel, "No polynomials found!");
            }
        });
    }

    @SuppressWarnings("Duplicates")
    private void addSelectedPolyListener() {
        selectPolyComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setLabelsVisible(nLabel, tLabel, fxLabel, initStateLabel);
                nLabel.setText("n=" + newValue.getN());
                tLabel.setText("T=" + newValue.getPeriod());
                fxLabel.setText("f(x)=" + newValue.getFxPolynomial());
                initStateLabel.setText("Y[0]=");
                setupInitialState(initStateGridPane, newValue);
                addMatrixToGridPane(matrixTitledPane, matrixGridPane, newValue.getMatrix());
                nextButton.setDisable(false);
            }
        });
    }

    @Override
    List<Polynomial> loadPolynomialsByN(short n) {
        List<Polynomial> polynomials = super.loadPolynomialsByN(n);
        Polynomial prevPolynomial = prevLfsr.getPolynomial();
        polynomials.removeIf(polynomial -> Util.gcd(prevPolynomial.getPeriod(), polynomial.getPeriod()) != 1);
        return polynomials;
    }

    @FXML
    public void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/firstPolynomial.fxml"));
        Parent firstPolyParent = fxmlLoader.load();
        Scene firstPolyScene = new Scene(firstPolyParent, backButton.getScene().getWidth(), backButton.getScene().getHeight());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(firstPolyScene);
        window.show();
    }

    @FXML
    public void nextButtonPressed(ActionEvent event) throws IOException {
        Polynomial selectedPolynomial = selectPolyComboBox.getSelectionModel().getSelectedItem();
        if (selectedPolynomial == null) {
            setErrorMessage(errorLabel, "Select polynomial first!");
        } else if (!validateInitStatePane(initStateGridPane, initStateErrorLabel)) {
            setErrorMessage(errorLabel, "Y[0] is incorrect!");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/shiftAnalysis.fxml"));
            Parent shiftAnalysisParent = fxmlLoader.load();
            Scene shiftAnalysisScene = new Scene(shiftAnalysisParent, nextButton.getScene().getWidth(), nextButton.getScene().getHeight());
            ShiftAnalysisController nextController = fxmlLoader.getController();
            int[] selectedInitState = getSelectedInitialState(initStateGridPane, initStateErrorLabel);
            LFSR thisLfsr = new LFSR(selectedPolynomial, selectedInitState);
            nextController.initData(prevLfsr, thisLfsr);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(shiftAnalysisScene);
            window.show();
        }
    }

}