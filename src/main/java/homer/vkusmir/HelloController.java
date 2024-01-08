package homer.vkusmir;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class HelloController {

    @FXML
    private Text helloText;

    @FXML
    void startKitchen(MouseEvent event) {
        helloText.setText("Кухня");
    }

    @FXML
    void startOrder(MouseEvent event) {
        helloText.setText("Заказ");
    }

}
