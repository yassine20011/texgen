module com.texgen.main {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.texgen to javafx.fxml;
    exports com.texgen;
}