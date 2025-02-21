package com.example.di05_informes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class artistasController {

    private final String urlDb = "jdbc:sqlite:chinook.db";
    public ListView<String> artistasListView;

    @FXML
    public void initialize() {
        cargarArtistas();
        artistasListView.setCellFactory(param -> new ListCell<String>() {
            private final Text text = new Text();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    text.setText(item);
                    text.setWrappingWidth(artistasListView.getWidth() - 18);
                    setGraphic(text);
                }
            }
        });
        artistasListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmacion");
                alert.setHeaderText("Se ha seleccionado un artista");
                alert.setContentText("Quiere generar el informe de: " + newValue.toString());
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            String jasperFile = "artista.jrxml";
                            InputStream inputStream = HelloApplication.class.getResourceAsStream(jasperFile);

                            System.out.println("Compilando : " + jasperFile);
                            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                            Connection conn = DriverManager.getConnection(urlDb);

                            Map<String, Object> params = new HashMap<>();
                            params.put("artista_name", newValue.toString());

                            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
                            JasperViewer.viewReport(jasperPrint, true);
                        } catch (JRException e){
                            e.printStackTrace();
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                    } else {
                        artistasListView.getSelectionModel().clearSelection();
                    }
                });
            }
        });
    }

    private void cargarArtistas() {
        ObservableList<String> artistas = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(urlDb);
             PreparedStatement stmt = conn.prepareStatement("SELECT Name FROM artists");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                artistas.add(rs.getString("Name"));
            }
            artistasListView.setItems(artistas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
