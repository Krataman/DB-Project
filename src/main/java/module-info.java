module org.dbprj.dbproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.opencsv;


    opens org.dbprj.dbproject to javafx.fxml;
    exports org.dbprj.dbproject;
}