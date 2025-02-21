package com.example.di05_informes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class HelloController {
    private final String urlDb = "jdbc:sqlite:chinook.db";
    public Button customerReportButton;
    public Button artistReportButton;
    public Button closeButton;

    public void initialize() {

    }

    public void generateCustomerReport(ActionEvent actionEvent) {
        try {
            String jasperFile = "customer.jrxml";
            InputStream inputStream = HelloApplication.class.getResourceAsStream(jasperFile);

            System.out.println("Compilando : " + jasperFile);
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            Connection conn = DriverManager.getConnection(urlDb);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);
            JasperViewer.viewReport(jasperPrint, true);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void generateArtistReport(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("informeArtista.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Informe Artistas");
            stage.setScene(scene);
            stage.setWidth(440);
            stage.setHeight(550);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}