package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyDoubleWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Formula1StatsApp extends Application {

    private ComboBox<Integer> yearComboBox;
    private TableView<DriverSeason> tableView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Formula 1 Statistics");

        // Crear ComboBox para seleccionar el año
        yearComboBox = new ComboBox<>();
        yearComboBox.setPromptText("Select Year");
        yearComboBox.setOnAction(e -> updateTableView());

        // Crear HBox para contener la etiqueta "Año:" y el ComboBox
        HBox yearSelectionBox = new HBox(10);
        yearSelectionBox.setAlignment(Pos.CENTER);
        Label yearLabel = new Label("Año:");
        yearSelectionBox.getChildren().addAll(yearLabel, yearComboBox);

        // Inicializar TableView
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Crear columnas para el TableView
        TableColumn<DriverSeason, String> nameCol = new TableColumn<>("Driver Name");
        nameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFullName()));

        TableColumn<DriverSeason, Integer> winsCol = new TableColumn<>("Wins");
        winsCol.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getWins()).asObject());

        TableColumn<DriverSeason, Double> pointsCol = new TableColumn<>("Total Points");
        pointsCol.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getTotalPoints()).asObject());

        TableColumn<DriverSeason, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getSeasonRank()).asObject());

        tableView.getColumns().addAll(nameCol, winsCol, pointsCol, rankCol);

        // Crear layout
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(yearSelectionBox, tableView);

        // Crear escena
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Cargar años disponibles
        loadYears();
    }
    
    private void loadYears() {
        List<Integer> years = new ArrayList<>();
        // Asumiendo que los años van desde 1950 hasta 2023
        for (int year = 2009; year <= 2023; year++) {
            years.add(year);
        }
        yearComboBox.setItems(FXCollections.observableArrayList(years));
    }

    private void updateTableView() {
        Integer selectedYear = yearComboBox.getValue();
        if (selectedYear == null) return;

        String query = "SELECT r.year, d.forename, d.surname, " +
                       "COUNT(CASE WHEN res.position = 1 THEN 1 END) AS wins, " +
                       "SUM(res.points) AS total_points, " +
                       "RANK() OVER (PARTITION BY r.year ORDER BY SUM(res.points) DESC) AS season_rank " +
                       "FROM results res " +
                       "JOIN races r ON res.race_id = r.race_id " +
                       "JOIN drivers d ON res.driver_id = d.driver_id " +
                       "WHERE r.year = ? " +
                       "GROUP BY r.year, d.driver_id, d.forename, d.surname " +
                       "ORDER BY r.year, season_rank";

        ObservableList<DriverSeason> data = FXCollections.observableArrayList();

        try (Connection conn = Main.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selectedYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                data.add(new DriverSeason(
                    rs.getInt("year"),
                    rs.getString("forename"),
                    rs.getString("surname"),
                    rs.getInt("wins"),
                    rs.getDouble("total_points"),
                    rs.getInt("season_rank")
                ));
            }

            tableView.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error updating table: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}