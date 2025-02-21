module com.example.di05_informes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;


    opens com.example.di05_informes to javafx.fxml;
    exports com.example.di05_informes;
}