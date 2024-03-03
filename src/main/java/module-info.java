module com.example.test_javafx2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires twilio;
    requires java.mail;
    requires javafx.swing;
    requires javafx.media;
    //requires libusb4java;

    opens entities to javafx.base;
    opens com.example.test_javafx2 to javafx.fxml;
    exports com.example.test_javafx2;
}