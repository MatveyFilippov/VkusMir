module homer.vkusmir {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens homer.vkusmir to javafx.fxml;
    exports homer.vkusmir;
}