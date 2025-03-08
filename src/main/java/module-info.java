module com.texgen.texgen {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.texgen.main to javafx.fxml;
    exports com.texgen.main;
}