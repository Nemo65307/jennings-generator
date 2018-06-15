package com.nemo.jennings.controller;

import com.nemo.jennings.Util;
import com.nemo.jennings.object.LFSR;
import com.nemo.jennings.object.Multiplexer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nemo.jennings.Util.addStateToGridPane;
import static com.nemo.jennings.Util.mulAndAddSequence;
import static com.nemo.jennings.Util.shiftSequence;

public class ShiftAnalysisController {

    @FXML
    private GridPane currentStateGridPane1;
    @FXML
    private GridPane currentStateGridPane2;
    @FXML
    private Label polynomialLabel1;
    @FXML
    private Label polynomialLabel2;
    @FXML
    private Label tLabel;
    @FXML
    private Label tLabel1;
    @FXML
    private Label tLabel2;
    @FXML
    private Label experimentalTLabel;
    @FXML
    private Label hammingLabel;
    @FXML
    private TextArea outputTextArea;
    @FXML
    private TextField roundsTextField;
    @FXML
    private Button backButton;

    private Multiplexer multiplexer;
    private LFSR lfsr1;
    private LFSR lfsr2;
    private StringBuilder outputBuffer;
    private long hammingWeight;
    private final String endOfPeriodMark = " End of period\n";

    void initData(LFSR lfsr1, LFSR lfsr2) {
        this.lfsr1 = lfsr1;
        this.lfsr2 = lfsr2;
        this.multiplexer = new Multiplexer(lfsr1, lfsr2);
        setupLfsrViews();
        outputBuffer = new StringBuilder();
        addRoundsTextFieldListener();
    }

    private void setupLfsrViews() {
        updateViews();
        polynomialLabel1.setText(lfsr1.getPolynomial().toString());
        polynomialLabel2.setText(lfsr2.getPolynomial().toString());
        tLabel1.setText("T=" + String.valueOf(lfsr1.getPolynomial().getPeriod()));
        tLabel2.setText("T=" + String.valueOf(lfsr2.getPolynomial().getPeriod()));
        tLabel.setText("T=" + multiplexer.getPeriod());
    }

    private void updateViews() {
        Integer[] y1 = Arrays.stream(lfsr1.getY()).boxed().toArray(Integer[]::new);
        Integer[] y2 = Arrays.stream(lfsr2.getY()).boxed().toArray(Integer[]::new);
        addStateToGridPane(currentStateGridPane1, y1, false);
        addStateToGridPane(currentStateGridPane2, y2, false);
        calculateHammingWeight();
        experimentalTLabel.setText("Ex. T=" + outputTextArea.getText().
                replaceAll(endOfPeriodMark, "").
                length());
        hammingLabel.setText("Hamming=" + String.valueOf(hammingWeight));
    }

    private void calculateHammingWeight() {
        this.hammingWeight = 0;
        String seq = outputTextArea.getText().replaceAll(endOfPeriodMark, "");
        int[] binSeq = Util.convertStringToBinArr(seq);
        for (int i = 0; i < binSeq.length; i++) {
            if (binSeq[i] == 1) {
                hammingWeight++;
            }
        }
    }

    private double[] calculateAutocorrelation() {
        String seq = outputTextArea.getText().replaceAll(endOfPeriodMark, "");
        int[] firstBinSeq = Util.convertStringToBinArr(seq);
        Util.invertBinArr(firstBinSeq);
        int[] seqNumbers = new int[firstBinSeq.length];
        int[] currentBinSeq = firstBinSeq.clone();
        for (int i = 0; i < firstBinSeq.length; i++) {
            seqNumbers[i] = mulAndAddSequence(firstBinSeq, currentBinSeq);
            shiftSequence(currentBinSeq);
        }
        double[] normalizedSeqNumbers = new double[seqNumbers.length];
        for (int i = 0; i < seqNumbers.length; i++) {
            normalizedSeqNumbers[i] = (double) seqNumbers[i] / firstBinSeq.length;
        }
        return normalizedSeqNumbers;
    }

    @FXML
    public void shiftButtonPressed() {
        String roundsString = roundsTextField.getText();
        if (!roundsString.equals("")) {
            Long rounds = Long.parseLong(roundsString);
            outputBuffer.append(multiplexer.run(rounds));
            outputTextArea.setText(outputBuffer.toString());
            updateViews();
        }
    }

    @FXML
    public void shiftPeriodButtonPressed() {
        outputBuffer.append(multiplexer.run(multiplexer.getPeriod()));
        outputTextArea.setText(outputBuffer.toString());
        updateViews();
    }

    private void addRoundsTextFieldListener() {
        roundsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                roundsTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    public void clearButtonPressed() {
        outputTextArea.clear();
    }

    @FXML
    public void restartButtonPressed() {
        outputTextArea.clear();
        hammingWeight = 0;
        hammingLabel.setText("");
        experimentalTLabel.setText("");
        outputBuffer = new StringBuilder();
    }

    @FXML
    public void acfButtonPressed() {
        Stage stage = new Stage();
        stage.setTitle("ACF Chart");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Step");
        yAxis.setLabel("Value");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("ACF");
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        XYChart.Series series = new XYChart.Series();
        List<XYChart.Data> chartPoints = new ArrayList<>();
        double[] values = calculateAutocorrelation();
        for (int i = 0; i < values.length; i++) {
            chartPoints.add(new XYChart.Data(i, values[i]));
        }
        series.getData().addAll(chartPoints);
        Scene scene = new Scene(lineChart, 650.0, 500.0);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/secondPolynomial.fxml"));
        Parent secondPolyParent = fxmlLoader.load();
        Scene secondPolyScene = new Scene(secondPolyParent, backButton.getScene().getWidth(), backButton.getScene().getHeight());
        SecondPolynomialController prevController = fxmlLoader.getController();
        prevController.initData(lfsr1);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(secondPolyScene);
        window.show();
    }

}