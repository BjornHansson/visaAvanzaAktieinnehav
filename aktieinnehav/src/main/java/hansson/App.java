package hansson;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Group;

public class App extends Application {
    private static final String TITLE = "Portfolio";
    private static final int DEFAULT_SIZE = 900;

    public static void main(String[] args) {
        System.out.println("Starts the application");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle(TITLE);
        // scene.setFill(Paint.valueOf("#000000"));
        // stage.setMaximized(true);

        final String filePath = System.getenv("BANK_HTML_FILE");
        System.out.println("Parsing the file: " + filePath);
        HtmlFile htmlFile = new HtmlFile(filePath);
        List<Shareholding> shareholdings = htmlFile.getShareholdings();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Shareholding share : shareholdings) {
            pieChartData.add(new PieChart.Data(share.getLabel(), share.getAmount()));
        }

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle(TITLE);
        chart.setPrefSize(DEFAULT_SIZE, DEFAULT_SIZE);

        Background bg = new Background(new BackgroundFill(Color.web("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY));
        VBox box = new VBox(8); // spacing = 8
        box.setFillWidth(true);
        box.setBackground(bg);
        box.getChildren().add(chart);

        ((Group) scene.getRoot()).getChildren().add(box);
        stage.setScene(scene);
        stage.show();
    }
}
