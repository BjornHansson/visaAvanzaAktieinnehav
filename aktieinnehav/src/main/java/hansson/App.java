package hansson;

import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Group;

public class App extends Application {
    private static final String TITLE = "Portfolio";
    private static final String OPTION_LARGE = "Fourteen largest holdings";
    private static final String OPTION_ALL_PIE = "All holdings in pie chart";
    private static final String OPTION_ALL_BAR = "All holdings in bar chart";
    private static final int DEFAULT_SIZE = 900;
    private static final int MAX_NR = 14;
    private List<Shareholding> shareholdings;

    public static void main(String[] args) {
        System.out.println("Starts the application");
        launch(args);
    }

    public App() {
        final String filePath = System.getenv("BANK_HTML_FILE");
        System.out.println("Parsing the file: " + filePath);
        HtmlFile htmlFile = new HtmlFile(filePath);
        this.shareholdings = htmlFile.getShareholdings();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle(TITLE);
        scene.getStylesheets().add("styles.css");

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();
        final BarChart<Number, String> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(TITLE);
        barChart.setPrefSize(DEFAULT_SIZE, DEFAULT_SIZE);
        xAxis.setLabel("Amount");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Holding");

        // Have several lists to change between depending on options
        ObservableList<PieChart.Data> chartDataAll = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> chartDataLargest = FXCollections.observableArrayList();

        int indexShare = 0;
        for (Shareholding share : this.shareholdings) {
            String label = share.getLabel();
            double amount = share.getAmount();

            chartDataAll.add(new PieChart.Data(label, amount));
            if (indexShare < MAX_NR) {
                chartDataLargest.add(new PieChart.Data(label, amount));
            }

            XYChart.Series<Number, String> series = new XYChart.Series<Number, String>();
            series.setName(share.getLabel());
            series.getData().add(new XYChart.Data<Number, String>(amount, label));
            barChart.getData().add(series);
            indexShare++;
        }

        final PieChart chart = new PieChart(chartDataLargest);
        chart.setTitle(TITLE);
        chart.setPrefSize(DEFAULT_SIZE, DEFAULT_SIZE);
        chart.setLabelLineLength(50);

        final Background bg = new Background(new BackgroundFill(Color.web("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY));
        final VBox vBox = new VBox(8); // spacing = 8
        vBox.setFillWidth(true);
        vBox.setBackground(bg);

        final ChoiceBox<String> selectBox = new ChoiceBox<>();
        selectBox.setItems(FXCollections.observableArrayList(OPTION_LARGE, OPTION_ALL_PIE, OPTION_ALL_BAR));
        selectBox.setValue(OPTION_LARGE);

        selectBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                vBox.getChildren().clear(); // Remove all of the elements
                vBox.getChildren().add(selectBox); // Always add the select box
                
                String selectedOption = selectBox.getItems().get((Integer) number2);
                switch (selectedOption) {
                case OPTION_ALL_PIE:
                    vBox.getChildren().add(chart);
                    chart.setLabelsVisible(false); // Unable to display labels if there are too many
                    chart.setData(chartDataAll);
                    break;
                case OPTION_LARGE:
                    vBox.getChildren().add(chart);
                    chart.setData(chartDataLargest);
                    chart.setLabelsVisible(true);
                    break;
                case OPTION_ALL_BAR:
                    vBox.getChildren().add(barChart);
                    break;
                }
            }
        });

        vBox.getChildren().add(selectBox);
        vBox.getChildren().add(chart);

        ((Group) scene.getRoot()).getChildren().add(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
