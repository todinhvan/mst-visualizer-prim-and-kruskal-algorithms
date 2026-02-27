module com.mst {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.xml.dom;

    opens com.mst to javafx.fxml;
    exports com.mst;
    exports com.mst.controllers;
    exports com.mst.models;

}