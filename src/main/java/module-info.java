module ma.enset.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens ma.enset.demo to javafx.fxml;
    exports ma.enset.demo;
}