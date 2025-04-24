module com.texgen.main {
    requires javafx.fxml;
    requires javafx.web;
    requires  java.naming;

    requires org.controlsfx.controls;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.apache.logging.log4j;
    requires jdk.jfr;
    requires jbcrypt;
    requires org.fxmisc.richtext;
    requires jdk.httpserver;
    opens com.texgen.controllers to javafx.fxml;


    opens com.texgen to javafx.fxml;
    exports com.texgen;
}