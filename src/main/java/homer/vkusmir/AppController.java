package homer.vkusmir;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    @FXML
    private AnchorPane mainKitchenPane, mainOrderPane, mainStartPane, orderCategoriesPane;

    @FXML
    private Text tempKitchenOutput;


    @FXML
    void openMainKitchenPaneFromStart(MouseEvent event) {
        try {
            Corridor2Talk.initKitchen(tempKitchenOutput);
        } catch (IOException ex) {
            // TODO: do smt (can't up server)
        }
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
        Corridor2Talk.killKitchen();
        ControlHelper.switchPane(mainKitchenPane, mainStartPane);
    }

    @FXML
    void runSecretTask(MouseEvent event) {
        if (ControlHelper.canReturnLog()) {
            ControlHelper.alreadyGetLog = true;
            System.out.println("Выполнил секретное задание!");  // TODO: paste some secret task
        }
    }

    @FXML
    void orderFoodCategory(MouseEvent event) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Шаурма");
        Corridor2Talk.sendOrder(data);
    }

    @FXML
    void orderFriedCategory(MouseEvent event) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Шашлык");
        Corridor2Talk.sendOrder(data);
    }

    @FXML
    void orderMeetCategory(MouseEvent event) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Маринованное");
        Corridor2Talk.sendOrder(data);
    }

}
