module mod1.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j;
    requires spring.context;

    opens pachet.service to javafx.fxml, javafx.base;
    exports pachet.service;
    opens pachet.gui to javafx.fxml, javafx.base;
    exports pachet.gui;
    opens pachet to javafx.fxml, javafx.base;
    exports pachet;
}