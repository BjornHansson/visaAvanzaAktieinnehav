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
    private static final String OPTION_LARGE = "Show the fourteen largest holdings";
    private static final String OPTION_ALL = "Show all holdings";
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

        // Have several lists to change between depending on options
        ObservableList<PieChart.Data> chartDataAll = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> chartDataLargest = FXCollections.observableArrayList();

        for (Shareholding share : this.shareholdings) {
            chartDataAll.add(new PieChart.Data(share.getLabel(), share.getAmount()));
        }
        for (int i = 0; i < MAX_NR; i++) {
            Shareholding share = this.shareholdings.get(i); // They should already be sorted by size
            chartDataLargest.add(new PieChart.Data(share.getLabel(), share.getAmount()));
        }

        final PieChart chart = new PieChart(chartDataLargest);
        chart.setTitle(TITLE);
        chart.setPrefSize(DEFAULT_SIZE, DEFAULT_SIZE);
        chart.setLabelLineLength(50);

        final ChoiceBox<String> selectBox = new ChoiceBox<>();
        selectBox.setItems(FXCollections.observableArrayList(OPTION_LARGE, OPTION_ALL));
        selectBox.setValue(OPTION_LARGE);
        selectBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String selectedOption = selectBox.getItems().get((Integer) number2);
                if (selectedOption.equals(OPTION_ALL)) {
                    chart.setLabelsVisible(false); // Unable to display labels if there are too many
                    chart.setData(chartDataAll);
                } else {
                    chart.setData(chartDataLargest);
                    chart.setLabelsVisible(true);
                }
                // TODO Add option for horizontal bar chart
            }
        });

        Background bg = new Background(new BackgroundFill(Color.web("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY));
        VBox vBox = new VBox(8); // spacing = 8
        vBox.setFillWidth(true);
        vBox.setBackground(bg);
        vBox.getChildren().add(selectBox);
        vBox.getChildren().add(chart);

        ((Group) scene.getRoot()).getChildren().add(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
