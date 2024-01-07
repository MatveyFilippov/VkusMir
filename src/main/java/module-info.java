module homer.vkusmir {
    requires javafx.controls;
    requires javafx.fxml;


    opens homer.vkusmir to javafx.fxml;
    exports homer.vkusmir;
}