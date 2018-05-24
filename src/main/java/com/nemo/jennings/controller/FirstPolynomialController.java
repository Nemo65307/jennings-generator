package com.nemo.jennings.controller;

import com.nemo.jennings.object.LFSR;
import com.nemo.jennings.object.Polynomial;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FirstPolynomialController extends AbstractPolynomialController implements Initializable {

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
    private GridPane matrixGridPane;
    @FXML
    private TitledPane matrixTitledPane;
    @FXML
    private GridPane initStateGridPane;
    @FXML
    private Button nextButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Integer> listOfN = loadInN(3, 7, 15);
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
            short newN = listOfN.get(newValue.intValue()).shortValue();
            ObservableList<Polynomial> polynomials = selectPolyComboBox.getItems();
            polynomials.clear();
            polynomials.addAll(loadPolynomialsByN(newN));
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

    @FXML
    public void nextButtonPressed(ActionEvent event) throws IOException {
        Polynomial selectedPolynomial = selectPolyComboBox.getSelectionModel().getSelectedItem();
        if (selectedPolynomial == null) {
            setErrorMessage(errorLabel, "Select polynomial first!");
        } else if (!validateInitStatePane(initStateGridPane, initStateErrorLabel)) {
            setErrorMessage(errorLabel, "Y[0] is incorrect!");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/secondPolynomial.fxml"));
            Parent secondPolyParent = fxmlLoader.load();
            Scene secondPolyScene = new Scene(secondPolyParent, nextButton.getScene().getWidth(), nextButton.getScene().getHeight());
            SecondPolynomialController nextController = fxmlLoader.getController();
            int[] selectedInitState = getSelectedInitialState(initStateGridPane, initStateErrorLabel);
            LFSR lfsr = new LFSR(selectedPolynomial, selectedInitState);
            nextController.initData(lfsr);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(secondPolyScene);
            window.show();
        }
    }

}