package homer.vkusmir;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.math.BigDecimal;

public class AppController {

    @FXML
    private AnchorPane mainKitchenPane, mainOrderPane, mainStartPane, orderCategoriesPane, errorPane, editMenuPane;

    @FXML
    private AnchorPane orderProductsListPane, sure2dellProductPane, addProductInOrderPane, endOrderPane, virtualKeyboardPane;

    @FXML
    private Text categoryNameInProductList, typeOfProductInAddOrder, priceOfProductInAddOrder, finalPriceInEndOrder;

    @FXML
    private TextArea errorTextArea;

    @FXML
    private ScrollPane scrollPane4Products, scrollPane4ActualOrderTable, scrollPane4DoneOrderTable, scrollPane4EndOrder;

    @FXML
    private TextField createProductNameTextField, createProductPriceTextField, name2addInOrderTextField;

    @FXML
    private TextField price2dellTextField, name2dellTextField, type2dellTextField, category2dellTextField;

    @FXML
    private TextField price2addInOrderTextField, score2addInOrderTextField, deliveryAddressInEndOrder;

    @FXML
    private MenuButton newProductCategoryMenuBtn, newProductTypeMenuBtn;

    @FXML
    private CheckBox btnCheckDeliveryInEndOrder;


    @FXML
    void openMainKitchenPaneFromStart(MouseEvent event) throws IOException {
        try {
            Corridor2Talk.initKitchen(errorPane, errorTextArea);
            ControlHelper.switchPane(mainStartPane, mainKitchenPane);
        } catch (IOException ex) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea,
                    "Не могу запустить кухню\nОшибка запуска сервера\nAppController.java :: openMainKitchenPaneFromStart()");
            throw new IOException(ex);
        }
    }

    @FXML
    void openMainOrderPainFromOrder(ActionEvent event) {  // TODO: если есть ордер, то переспросить
        Order.killOrder();
        btnCheckDeliveryInEndOrder.setSelected(false);
        btnDeliveryInEndOrder();
        ControlHelper.switchPane(orderCategoriesPane, mainOrderPane);
    }

    @FXML
    void openMainOrderPaneFromStart() throws IOException {
        try {
            // Corridor2Talk.initOrderTable(scrollPane4ActualOrderTable, errorPane, errorTextArea);  // TODO: paste
            AppKeyboard.initKeyboard(virtualKeyboardPane);
            ControlHelper.switchPane(mainStartPane, mainOrderPane);
        } catch (Exception ex) {  // IOException ex
            ControlHelper.printErrorInApp(errorPane, errorTextArea,
                    "Не могу подключиться к кухне\nОшибка подключения к серверу\nAppController.java :: openMainOrderPaneFromStart()");
            throw new IOException(ex);
        }
    }

    @FXML
    void openOrderPaneFromMainOrder(ActionEvent event) {
        Order.newOrder();
        ControlHelper.switchPane(mainOrderPane, orderCategoriesPane);
    }

    @FXML
    void openStartPaneFromMainOrder(ActionEvent event) {
        Corridor2Talk.killOrderTable();
        ControlHelper.switchPane(mainOrderPane, mainStartPane);
    }

    @FXML
    void openStartPaneFromMainKitchen(ActionEvent event) {
        Corridor2Talk.killKitchen();
        ControlHelper.switchPane(mainKitchenPane, mainStartPane);
    }

    @FXML
    void runSecretTask(MouseEvent event) {
        if (ControlHelper.isClickedFiveTimes()) {
            ControlHelper.alreadyGetLog = true;
            System.out.println("Выполнил секретное задание!");  // TODO: create some secret task
        }
    }

    void orderOpenListWithProducts(String category) {
        categoryNameInProductList.setText(category);
        scrollPane4Products.setContent(
                ControlHelper.getProductsVboxForOrder(
                        category, addProductInOrderPane, typeOfProductInAddOrder, priceOfProductInAddOrder,
                        name2addInOrderTextField
                )
        );
        ControlHelper.switchPane(orderCategoriesPane, orderProductsListPane);
    }

    @FXML
    void orderFoodCategory(MouseEvent event) {
        orderOpenListWithProducts(ProductsJson.breadCategoryFood);
    }

    @FXML
    void orderFriedCategory(MouseEvent event) {
        orderOpenListWithProducts(ProductsJson.grillCategoryFood);
    }

    @FXML
    void orderMeetCategory(MouseEvent event) {
        orderOpenListWithProducts(ProductsJson.meetCategoryFood);
    }

    @FXML
    void orderMiscCategory(MouseEvent event) {
        orderOpenListWithProducts(ProductsJson.miscCategoryFood);
    }

    @FXML
    void closeErrorPane(ActionEvent event) {
        errorPane.setVisible(false);
        errorTextArea.setText("");
    }

    @FXML
    void openCategoriesPaneFromProductsList(ActionEvent event) {
        if (categoryNameInProductList.getText().contains("Удалить")) {
            ControlHelper.switchPane(orderProductsListPane, editMenuPane);
        } else {
            ControlHelper.switchPane(orderProductsListPane, orderCategoriesPane);
        }
    }

    @FXML
    void openEditMenuPaneFromMainOrder(ActionEvent event) {
        ControlHelper.switchPane(mainOrderPane, editMenuPane);
    }

    @FXML
    void openCategoriesPaneFromEditMenu(ActionEvent event) {
        AppKeyboard.hideKB();
        ControlHelper.switchPane(editMenuPane, mainOrderPane);
    }

    @FXML
    void openVirtualKeyboard() {
        AppKeyboard.showKB();
    }

    @FXML
    void closeVirtualKeyboard() {
        AppKeyboard.hideKB();
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
    void createProductButton(ActionEvent event) throws IOException {
        AppKeyboard.hideKB();
        if (NewProduct.isAllField(errorPane, errorTextArea, createProductPriceTextField, createProductNameTextField)) {
            NewProduct.putProductInBase();
            newProductTypeMenuBtn.setText("Тип");
            newProductCategoryMenuBtn.setText("Категория");
            createProductPriceTextField.setText("");
            createProductNameTextField.setText("");
        }
    }

    @FXML
    void pressCategoryDellProduct(ActionEvent event) {
        AppKeyboard.hideKB();
        MenuItem clickedType = (MenuItem) event.getSource();
        String categoryName = clickedType.getText();
        categoryNameInProductList.setText("Удалить: " + categoryName);
        scrollPane4Products.setContent(
                ControlHelper.getProductsForDellVbox(categoryName, sure2dellProductPane,
                        price2dellTextField, name2dellTextField, type2dellTextField, category2dellTextField)
        );
        ControlHelper.switchPane(editMenuPane, orderProductsListPane);
    }

    @FXML
    void noDellProductBtn(ActionEvent event) {
        price2dellTextField.setText("");
        name2dellTextField.setText("");
        type2dellTextField.setText("");
        category2dellTextField.setText("");
        sure2dellProductPane.setVisible(false);
    }

    @FXML
    void yesDellProductBtn(ActionEvent event) throws IOException {
        String categoryName = category2dellTextField.getText();
        ProductsJson.delProduct(categoryName, name2dellTextField.getText());

        scrollPane4Products.setContent(
                ControlHelper.getProductsForDellVbox(categoryName, sure2dellProductPane,
                        price2dellTextField, name2dellTextField, type2dellTextField, category2dellTextField)
        );

        price2dellTextField.setText("");
        name2dellTextField.setText("");
        type2dellTextField.setText("");
        category2dellTextField.setText("");
        sure2dellProductPane.setVisible(false);
    }

    @FXML
    void endOrderBtn() {
        if (Order.isOrderNull()) {
            ControlHelper.printErrorInApp(
                    errorPane, errorTextArea, "Завершить нельзя, заказ пуст\nНи одной позиции не добавлено"
            );
        } else {
            finalPriceInEndOrder.setText("Проверьте заказ общей стоимостью: " + Order.finalPrice.toString());
            scrollPane4EndOrder.setContent(
                    ControlHelper.getPositionsVboxForEndOrder()
            );
            endOrderPane.setVisible(true);
        }
    }

    @FXML
    void closeEndOrderPane() {
        endOrderPane.setVisible(false);
    }

    @FXML
    void closeAddProductPaneBtn() {
        typeOfProductInAddOrder.setText("()");
        priceOfProductInAddOrder.setText("()");
        score2addInOrderTextField.setText("");
        price2addInOrderTextField.setText("");
        name2addInOrderTextField.setText("");
        addProductInOrderPane.setVisible(false);
    }

    @FXML
    void addProductInOrderProductBtn(ActionEvent event) {
        String productScore = score2addInOrderTextField.getText();
        if (productScore.isEmpty()) {
            ControlHelper.printErrorInApp(
                    errorPane, errorTextArea, "Введите количество\nПока добавить товар в заказ нельзя"
            );
        } else {
            Order.appendPosition(
                    name2addInOrderTextField.getText(),
                    price2addInOrderTextField.getText(),
                    productScore
            );
            closeAddProductPaneBtn();
        }
    }

    @FXML
    void pressPlusMinusScoreInAddOrder(ActionEvent event) {
        Button clickedNum = (Button) event.getSource();
        String signPressed = clickedNum.getText();
        String alreadyWritten = score2addInOrderTextField.getText();
        String productType = typeOfProductInAddOrder.getText();

        BigDecimal actualScore = BigDecimal.ZERO;
        if (!alreadyWritten.isEmpty()) {
            actualScore = new BigDecimal(alreadyWritten);
        }

        BigDecimal numToAdd = productType.equals("Шт") ? BigDecimal.ONE : BigDecimal.valueOf(0.1);
        actualScore = signPressed.equals("+") ? actualScore.add(numToAdd) : actualScore.subtract(numToAdd);

        String actualScoreString = "";
        if (actualScore.signum() > 0) {
            if (actualScore.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                actualScoreString = String.valueOf(actualScore.intValue());
            } else {
                actualScoreString = actualScore.toString();
            }
            BigDecimal actualPrice = actualScore.multiply(new BigDecimal(priceOfProductInAddOrder.getText()));
            price2addInOrderTextField.setText(actualPrice.toString());
        } else {
            price2addInOrderTextField.setText("");
        }

        score2addInOrderTextField.setText(actualScoreString);
    }

    @FXML
    void pressNumInAddOrder(ActionEvent event) {
        Button clickedNum = (Button) event.getSource();
        String numPressed = clickedNum.getText();
        String alreadyWritten = score2addInOrderTextField.getText();
        if (!(numPressed.contains(".") && alreadyWritten.contains("."))) {
            String actualScore = alreadyWritten + numPressed;
            score2addInOrderTextField.setText(actualScore);
            BigDecimal actualPrice = new BigDecimal(actualScore).multiply(
                    new BigDecimal(priceOfProductInAddOrder.getText())
            );
            price2addInOrderTextField.setText(actualPrice.toString());
        }
    }

    @FXML
    void backspaceNumPressedInAddOrder() {
        String alreadyWritten = score2addInOrderTextField.getText();
        int strLen = alreadyWritten.length();
        if (strLen != 0) {
            String actualScore = alreadyWritten.substring(0, strLen - 1);
            score2addInOrderTextField.setText(actualScore);
            if (strLen == 1) {
                price2addInOrderTextField.setText("");
            } else {
                BigDecimal actualPrice = new BigDecimal(actualScore).multiply(
                        new BigDecimal(priceOfProductInAddOrder.getText())
                );
                price2addInOrderTextField.setText(actualPrice.toString());
            }
        }
    }

    @FXML
    void btnDeliveryInEndOrder() {  // TODO: в других функция поставить 'btnCheckDeliveryInEndOrder.setSelected(false); btnDeliveryInEndOrder();'
        if (btnCheckDeliveryInEndOrder.isSelected()) {
            deliveryAddressInEndOrder.setText("");
            deliveryAddressInEndOrder.setDisable(false);
            deliveryAddressInEndOrder.setEditable(true);
        } else {
            AppKeyboard.hideKB();
            deliveryAddressInEndOrder.setText("Выдать на месте");
            deliveryAddressInEndOrder.setDisable(true);
            deliveryAddressInEndOrder.setEditable(false);
        }
    }
}
