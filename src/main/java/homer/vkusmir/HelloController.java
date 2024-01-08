package homer.vkusmir;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class HelloController {

    @FXML
    private AnchorPane mainKitchenPane, mainOrderPane, mainStartPane, orderCategoriesPane;


    @FXML
    void openMainKitchenPaneFromStart(MouseEvent event) throws Exception {
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

    @FXML
    void getLogIfFiveClicked(MouseEvent event) {
        if (ControlHelper.canReturnLog()) {
            ControlHelper.alreadyGetLog = true;
            System.out.println("Вернул лог!");  // TODO: paste some secret task
        }
    }

}
