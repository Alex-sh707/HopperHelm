module com.example.hopperhelm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hopperhelm to javafx.fxml;
    exports com.example.hopperhelm;
}