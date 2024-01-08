package homer.vkusmir;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class HelloController {

    @FXML
    private AnchorPane mainKitchenPane, mainOrderPane, mainStartPane, orderCategoriesPane;


    @FXML
    void openMainKitchenPaneFromStart(MouseEvent event) {
        ControlHelper.switchPane(mainStartPane, mainKitchenPane);
    }

    @FXML
    void openMainOrderPainFromOrder(ActionEvent event) {
        ControlHelper.switchPane(orderCategoriesPane, mainOrderPane);
    }

    @FXML
    void openMainOrderPaneFromStart(MouseEvent event) {
        ControlHelper.switchPane(mainStartPane, mainOrderPane);
    }

    @FXML
    void openOrderPaneFromMainOrder(ActionEvent event) {
        ControlHelper.switchPane(mainOrderPane, orderCategoriesPane);
    }

    @FXML
    void openStartPaneFromMainOrder(ActionEvent event) {
        ControlHelper.switchPane(mainOrderPane, mainStartPane);
    }

    @FXML
    void openStartPaneFromMainKitchen(ActionEvent event) {
        ControlHelper.switchPane(mainKitchenPane, mainStartPane);
    }

}
