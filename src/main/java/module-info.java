module com.vperse.sasa {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vperse.sasa to javafx.fxml;
    exports com.vperse.sasa;
    exports com.vperse.sasa.mainmenu;
    opens com.vperse.sasa.mainmenu to javafx.fxml;
    exports com.vperse.sasa.editor;
    opens com.vperse.sasa.editor to javafx.fxml;
    exports com.vperse.sasa.simulation;
    opens com.vperse.sasa.simulation to javafx.fxml;

    exports com.vperse.sasa.logic;
    exports com.vperse.sasa.util;
}