package homer.vkusmir;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    @FXML
    private AnchorPane mainKitchenPane, mainOrderPane, mainStartPane, orderCategoriesPane, errorPane, editMenuPane;

    @FXML
    private AnchorPane orderProductsListPane;

    @FXML
    private Text tempKitchenOutput, categoryNameInProductList;

    @FXML
    private TextArea errorTextArea;

    @FXML
    private ScrollPane scrollPane4Products;

    @FXML
    private TextField createProductNameTextField, createProductPriceTextField;

    @FXML
    private MenuButton newProductCategoryMenuBtn, newProductTypeMenuBtn;


    @FXML
    void openMainKitchenPaneFromStart(MouseEvent event) throws IOException {
        try {
            Corridor2Talk.initKitchen(tempKitchenOutput);
            ControlHelper.switchPane(mainStartPane, mainKitchenPane);
        } catch (IOException ex) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea,
                    "Не могу запустить кухню (ошибка запуска сервера)\nAppController.java :: openMainKitchenPaneFromStart()");
            throw new IOException(ex);
        }
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
            System.out.println("Выполнил секретное задание!");  // TODO: create some secret task
        }
    }

    @FXML
    void orderFoodCategory(MouseEvent event) {
        categoryNameInProductList.setText("Бурумчик / Шаурма");
        scrollPane4Products.setContent(ControlHelper.getProductsVbox(""));
        ControlHelper.switchPane(orderCategoriesPane, orderProductsListPane);
    }

    @FXML
    void orderFriedCategory(MouseEvent event) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Шашлык");
        Corridor2Talk.sendOrder(data, errorPane, errorTextArea);
    }

    @FXML
    void orderMeetCategory(MouseEvent event) {
        categoryNameInProductList.setText("Маринованное мясо");
        scrollPane4Products.setContent(ControlHelper.getProductsVbox(""));
        ControlHelper.switchPane(orderCategoriesPane, orderProductsListPane);
    }

    @FXML
    void orderMiscCategory(MouseEvent event) {
        categoryNameInProductList.setText("Прочее");
        scrollPane4Products.setContent(ControlHelper.getProductsVbox(""));
        ControlHelper.switchPane(orderCategoriesPane, orderProductsListPane);
    }

    @FXML
    void closeErrorPane(ActionEvent event) {
        errorPane.setVisible(false);
        errorTextArea.setText("");
    }

    @FXML
    void openCategoriesPaneFromProductsList(ActionEvent event) {
        ControlHelper.switchPane(orderProductsListPane, orderCategoriesPane);
    }

    @FXML
    void openEditMenuPaneFromMainOrder(ActionEvent event) {
        ControlHelper.switchPane(mainOrderPane, editMenuPane);
    }

    @FXML
    void openCategoriesPaneFromEditMenu(ActionEvent event) {
        ControlHelper.switchPane(editMenuPane, mainOrderPane);
    }

    @FXML
    void pressLetter(ActionEvent event) {
        Button clickedLetter = (Button) event.getSource();
        String alreadyWritten = createProductNameTextField.getText();
        if (alreadyWritten.isEmpty()) {
            createProductNameTextField.setText(clickedLetter.getText());
        } else {
            createProductNameTextField.setText(alreadyWritten + clickedLetter.getText().toLowerCase());
        }
    }

    @FXML
    void backspaceLetterPressed(ActionEvent event) {
        String alreadyWritten = createProductNameTextField.getText();
        int strLen = alreadyWritten.length();
        if (strLen != 0) {
            createProductNameTextField.setText(alreadyWritten.substring(0, strLen - 1));
        }
    }

    @FXML
    void pressNum(ActionEvent event) {
        Button clickedNum = (Button) event.getSource();
        String numPressed = clickedNum.getText();
        String alreadyWritten = createProductPriceTextField.getText();
        if (!(numPressed.contains(".") && alreadyWritten.contains("."))) {
            createProductPriceTextField.setText(alreadyWritten + numPressed);
        }
    }

    @FXML
    void backspaceNumPressed(ActionEvent event) {
        String alreadyWritten = createProductPriceTextField.getText();
        int strLen = alreadyWritten.length();
        if (strLen != 0) {
            createProductPriceTextField.setText(alreadyWritten.substring(0, strLen - 1));
        }
    }

    @FXML
    void pressCategoryNewProduct(ActionEvent event) {
        MenuItem clickedCategory = (MenuItem) event.getSource();
        String category = clickedCategory.getText();
        NewProduct.category = category;
        newProductCategoryMenuBtn.setText(category);
    }

    @FXML
    void pressTypeNewProduct(ActionEvent event) {
        MenuItem clickedType = (MenuItem) event.getSource();
        String type = clickedType.getText();
        NewProduct.type = type;
        newProductTypeMenuBtn.setText(type);
    }

    @FXML
    void createProductButton(ActionEvent event) {
        if (NewProduct.isAllField(errorPane, errorTextArea, createProductPriceTextField, createProductNameTextField)) {
            NewProduct.putProductInBase();
            newProductTypeMenuBtn.setText("Тип");
            newProductCategoryMenuBtn.setText("Категория");
            createProductPriceTextField.setText("");
            createProductNameTextField.setText("");
        }
    }

}
