module utcn.ug.queuesystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens ug.queuesystem to javafx.fxml;
    exports ug.queuesystem;
    exports ug.queuesystem.GUI;
    opens ug.queuesystem.GUI to javafx.fxml;
}