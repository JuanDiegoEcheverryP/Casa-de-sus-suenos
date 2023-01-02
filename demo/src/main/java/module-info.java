module com.example.testvideo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.testvideo to javafx.fxml;
    exports com.example.testvideo;
}