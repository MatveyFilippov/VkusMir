package homer.vkusmir;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class HelloController {

    private int score = 0;

    @FXML
    private TextField myString;

    @FXML
    void plus_click(ActionEvent event) {
        score++;
        String text = "Нажатий: " + score;
        myString.setText(text);
    }

}
