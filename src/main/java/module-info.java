module com.example.fpoe_50zo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fpoe_50zo.controller to javafx.fxml;
    exports com.example.fpoe_50zo;
}