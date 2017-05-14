package edu.BarSU.AI.GUI;

import edu.BarSU.AI.GUI.edu.BarSU.AI.Algorithms.*;

import java.util.ArrayList;

import javafx.fxml.FXML;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {
    @FXML
    private TextField tfOxAdd;
    @FXML
    private TextField tfOyAdd;
    @FXML
    private LineChart<Number, Number> lchGraph;
    @FXML
    private ListView<GraphPoint> lvPoint;
    @FXML
    private TableView<MethodData> tvMethodsData;
    @FXML
    private TableColumn<MethodData, String> tcMethodName;
    @FXML
    private TableColumn<MethodData, Integer> tcCoast;
    @FXML
    private TableColumn<MethodData, String> tcWay;
    @FXML
    private TableColumn<MethodData, Integer> tcIteration;
    @FXML
    private TableColumn<MethodData, Integer> tcTime;

    ObservableList<MethodData> methodDataList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        tcMethodName.setCellValueFactory(
                new PropertyValueFactory<MethodData, String>("nameMethod"));
        tcCoast.setCellValueFactory(
                new PropertyValueFactory<MethodData, Integer>("costMethod"));
        tcWay.setCellValueFactory(
                new PropertyValueFactory<MethodData, String>("wayMethod"));
        tcIteration.setCellValueFactory(
                new PropertyValueFactory<MethodData, Integer>("iterationMethod"));
        tcTime.setCellValueFactory(
                new PropertyValueFactory<MethodData, Integer>("timeMethod"));

        tvMethodsData.setItems(methodDataList);

        lvPoint.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void add(){
        try {
            lvPoint.getItems().add(new GraphPoint(
                    Double.valueOf(tfOxAdd.getText()),
                    Double.valueOf(tfOyAdd.getText())));


        } catch (Exception Ex){
            new Alert(Alert.AlertType.INFORMATION, "Некорректно введены координаты!").show();
            return;
        }
        rebuildGraphPoint();

        tfOxAdd.clear();
        tfOyAdd.clear();
    }

    @FXML
    private void delete(){
        lvPoint.getItems().removeAll(lvPoint.getSelectionModel().getSelectedItems());

        rebuildGraphPoint();
    }

    @FXML
    private void calculate() {
        methodDataList.clear();

        ObservableList<XYChart.Series<Number, Number>> lineChartData = lchGraph.getData();
        lineChartData.clear();
        //
        double[][] sourceMatrix = getMatrixCoast();
        // Genetic method
        long methodTime = System.nanoTime();
        ArrayList<Integer> geneticData = Genetic.StartMethod(sourceMatrix, 17, 100000);
        methodTime = System.nanoTime() - methodTime;
        calculateMethod(geneticData,  "Генетический", methodTime);
        // Ant method
        long methodTime1 = System.nanoTime();
        ArrayList<Integer> antData = AntColony.StartMethod(sourceMatrix, 1000, 0.3, 0.4, 0.6);
        methodTime1 = System.nanoTime() - methodTime1;
        calculateMethod(antData,  "Муравьиный", methodTime1);
    }

    private void calculateMethod(ArrayList<Integer> methodData, String methodName, long methodTime) {
        StringBuilder strRoute = new StringBuilder();

        for (int i = 0; i < methodData.size() - 2; ++i)
            strRoute.append(methodData.get(i) + "-");
        strRoute.append(methodData.get(0));

        methodDataList.add(new MethodData(
                methodName,
                methodData.get(methodData.size() - 2),
                strRoute.toString(),
                methodData.get(methodData.size() - 1),
                methodTime/1000000));

        methodData.remove(methodData.size() - 1);
        methodData.remove(methodData.size() - 1);

        showTheRoute(methodData, methodName);
    }

    private void showTheRoute(ArrayList<Integer> pointData, String lineChartName) {
        ObservableList<XYChart.Data> dataList = FXCollections.observableArrayList();

        ObservableList<GraphPoint> graphPointsList = lvPoint.getItems();

        GraphPoint tempGraphPoint;

        for (int temp: pointData) {
            tempGraphPoint = graphPointsList.get(temp - 1);
            dataList.add(new XYChart.Data(
                    tempGraphPoint.getXPosition(),
                    tempGraphPoint.getYPosition()));
        }

        tempGraphPoint = graphPointsList.get(0);
        dataList.add(new XYChart.Data(
                tempGraphPoint.getXPosition(),
                tempGraphPoint.getYPosition()));

        XYChart.Series route = new XYChart.Series();
        route.setData(dataList);
        route.setName(lineChartName);

        ObservableList<XYChart.Series<Number, Number>> lineChartData = lchGraph.getData();
        lineChartData.add(route);
    }

    private void rebuildGraphPoint() {
        ObservableList<XYChart.Series<Number, Number>> newSeriesList = FXCollections.observableArrayList();

        byte count = 0;

        for (GraphPoint tempPoint: lvPoint.getItems()) {
            ObservableList<XYChart.Data> dataList = FXCollections.observableArrayList();

            dataList.add(new XYChart.Data(tempPoint.getXPosition(), tempPoint.getYPosition()));

            XYChart.Series tempSeries = new XYChart.Series();

            tempSeries.setName(String.valueOf(++count));

            tempSeries.setData(dataList);

            newSeriesList.add(tempSeries);
        }

        lchGraph.setCreateSymbols(false);
        lchGraph.setData(newSeriesList);
    }

    private double[][] getMatrixCoast() {
        int sizeMatrix = lvPoint.getItems().size();
        double[][] sourceMatrix = new double[sizeMatrix][sizeMatrix];

        ObservableList<GraphPoint> pointList = lvPoint.getItems();

        for (int i = 0; i < sizeMatrix; ++i)
            for (int j = 0; j < sizeMatrix; ++j)
                if (i > j) {
                    GraphPoint tempPointOne = pointList.get(i);
                    GraphPoint tempPointTwo = pointList.get(j);

                    double tempA = Math.abs(tempPointOne.getXPosition() - tempPointTwo.getXPosition());
                    double tempB = Math.abs(tempPointOne.getYPosition() - tempPointTwo.getYPosition());

                    try {
                        sourceMatrix[i][j] = sourceMatrix[j][i] =
                                (tempA == 0) ? tempB :
                                        (tempB == 0) ? tempA : find3SideOfTheTriangle(tempA, tempB);
                    } catch (Exception Ex) {
                        return new double[][]{{},{}};
                    }
                } else
                    if (i == j)
                        sourceMatrix[i][j] = 0;


        return sourceMatrix;
    }

    private final static double find3SideOfTheTriangle(double a, double b) throws Exception {
            return Math.sqrt(a * a + b * b);
    }
}