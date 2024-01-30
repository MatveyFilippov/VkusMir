package homer.vkusmir;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    @FXML
    private AnchorPane mainKitchenPane, mainOrderPane, mainStartPane, orderCategoriesPane, errorPane, editMenuPane;

    @FXML
    private AnchorPane orderProductsListPane, sure2dellProductPane, addProductInOrderPane, endOrderPane, virtualKeyboardPane;

    @FXML
    private AnchorPane sure2DellPositionPane, lookWrittenOrderPane, notificationPane;

    @FXML
    private Text categoryNameInProductList, typeOfProductInAddOrder, priceOfProductInAddOrder, finalPriceInEndOrder;

    @FXML
    private Text yesDellPositionInOrder, notificationText;

    @FXML
    private TextArea errorTextArea;

    @FXML
    private ScrollPane scrollPane4Products, scrollPane4ActualOrderTable, scrollPane4DoneOrderTable, scrollPane4EndOrder;

    @FXML
    private ScrollPane orderPositionsInKitchenLook, orderNumbersKitchen, orderPositionsKitchen;

    @FXML
    private TextField createProductNameTextField, createProductPriceTextField, name2addInOrderTextField;

    @FXML
    private TextField price2dellTextField, name2dellTextField, type2dellTextField, category2dellTextField;

    @FXML
    private TextField price2addInOrderTextField, score2addInOrderTextField, deliveryAddressInEndOrder, orderNumKitchen;

    @FXML
    private TextField addressInDoneOrder, position2dellTextField, orderNumInKitchenLook, priceInDoneOrder, orderAddressKitchen;

    @FXML
    private MenuButton newProductCategoryMenuBtn, newProductTypeMenuBtn;

    @FXML
    private CheckBox btnCheckDeliveryInEndOrder;

    @FXML
    private Button orderIsDoneKitchenLook;

    private final VirtualKeyboard virtualKB = new VirtualKeyboard();
    private Map<String, Map<String, VBox>> vboxes = new HashMap<>();
    private boolean isKitchenOpen = false;


    @FXML
    void openMainKitchenPaneFromStart() throws IOException {
        try {
            Corridor2Talk.initKitchen(errorPane, errorTextArea, notificationText, notificationPane);
            ControlHelper.initAndFillActiveOrderInKitchen(
                    orderNumbersKitchen, orderNumKitchen, orderAddressKitchen, orderPositionsKitchen
            );
            ControlHelper.switchPane(mainStartPane, mainKitchenPane);
            isKitchenOpen = true;
        } catch (IOException ex) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea,
                    "Не могу запустить кухню\nОшибка запуска сервера\nAppController.java :: openMainKitchenPaneFromStart()");
            throw new IOException(ex);
        }
    }

    @FXML
    void openMainOrderPainFromOrder() {  // TODO: если есть ордер, то переспросить
        Order.killOrder();
        btnCheckDeliveryInEndOrder.setSelected(false);
        btnDeliveryInEndOrder();
        ControlHelper.switchPane(orderCategoriesPane, mainOrderPane);
    }

    @FXML
    void openMainOrderPaneFromStart() throws IOException {
        try {
            Corridor2Talk.initOrderTable(errorPane, errorTextArea, notificationText, notificationPane);
            ControlHelper.initAndFillOrdersVboxForOrderTable(
                    lookWrittenOrderPane, scrollPane4ActualOrderTable, scrollPane4DoneOrderTable, orderNumInKitchenLook,
                    orderIsDoneKitchenLook, addressInDoneOrder, priceInDoneOrder, orderPositionsInKitchenLook
            );
            virtualKB.initKeyboardForSimpleUsing(virtualKeyboardPane);
            ControlHelper.switchPane(mainStartPane, mainOrderPane);
            isKitchenOpen = false;
        } catch (Exception ex) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea,
                    "Не могу подключиться к кухне\nОшибка подключения к серверу\nAppController.java :: openMainOrderPaneFromStart()");
            throw new IOException(ex);
        }
    }

    @FXML
    void openOrderPaneFromMainOrder() {
        Order.newOrder();
        ControlHelper.switchPane(mainOrderPane, orderCategoriesPane);
    }

    @FXML
    void runSecretTask() {
        if (ControlHelper.isClickedFiveTimes()) {
            ControlHelper.alreadyGetLog = true;
            System.out.println("Выполнил секретное задание!");  // TODO: create some secret task
        }
    }

    private void fillListWithProductsInOrder(String category) {
        scrollPane4Products.setContent(null);
        String key4vbox = "order";
        if (!vboxes.containsKey(key4vbox)) {
            Map<String, VBox> tmp = new HashMap<>();
            vboxes.put(key4vbox, tmp);
            ProductsJson.isSomethingNew4Adding = true;
        }
        if (ProductsJson.isSomethingNew4Adding || !vboxes.get(key4vbox).containsKey(category)) {
            String[] categories = {
                    ProductsJson.breadCategoryFood, ProductsJson.grillCategoryFood,
                    ProductsJson.miscCategoryFood, ProductsJson.meetCategoryFood
            };
            for (String c : categories) {
                vboxes.get(key4vbox).put(
                        c, ControlHelper.getProductsVboxForOrder(
                                c, addProductInOrderPane, typeOfProductInAddOrder, priceOfProductInAddOrder, name2addInOrderTextField
                        )
                );
            }
            ProductsJson.isSomethingNew4Adding = false;
        }
        categoryNameInProductList.setText(category);
        scrollPane4Products.setContent(vboxes.get(key4vbox).get(category));
        ControlHelper.switchPane(orderCategoriesPane, orderProductsListPane);
    }

    @FXML
    void orderFoodCategory() {
        fillListWithProductsInOrder(ProductsJson.breadCategoryFood);
    }

    @FXML
    void orderFriedCategory() {
        fillListWithProductsInOrder(ProductsJson.grillCategoryFood);
    }

    @FXML
    void orderMeetCategory() {
        fillListWithProductsInOrder(ProductsJson.meetCategoryFood);
    }

    @FXML
    void orderMiscCategory() {
        fillListWithProductsInOrder(ProductsJson.miscCategoryFood);
    }

    @FXML
    void closeErrorPane() {
        errorPane.setVisible(false);
        errorTextArea.setText("");
    }

    @FXML
    void openCategoriesPaneFromProductsList() {
        if (categoryNameInProductList.getText().contains("Удалить")) {
            ControlHelper.switchPane(orderProductsListPane, editMenuPane);
        } else {
            ControlHelper.switchPane(orderProductsListPane, orderCategoriesPane);
        }
    }

    @FXML
    void openEditMenuPaneFromMainOrder() {
        ControlHelper.switchPane(mainOrderPane, editMenuPane);
    }

    @FXML
    void openCategoriesPaneFromEditMenu() {
        virtualKB.hideKB();
        newProductTypeMenuBtn.setText("Тип");
        newProductCategoryMenuBtn.setText("Категория");
        createProductPriceTextField.setText("");
        createProductNameTextField.setText("");
        ControlHelper.switchPane(editMenuPane, mainOrderPane);
    }

    @FXML
    void openVirtualKeyboard() {
        virtualKB.showKB();
    }

    @FXML
    void closeVirtualKeyboard() {
        virtualKB.hideKB();
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
    void createProductButton() throws IOException {
        virtualKB.hideKB();
        if (NewProduct.isAllField(errorPane, errorTextArea, createProductPriceTextField, createProductNameTextField)) {
            NewProduct.putProductInBase();
            newProductTypeMenuBtn.setText("Тип");
            newProductCategoryMenuBtn.setText("Категория");
            createProductPriceTextField.setText("");
            createProductNameTextField.setText("");
        }
    }

    private void fillListWithProducts2Dell(final String category) {
        scrollPane4Products.setContent(null);
        String key4vbox = "dell";
        if (!vboxes.containsKey(key4vbox)) {
            Map<String, VBox> tmp = new HashMap<>();
            vboxes.put(key4vbox, tmp);
            ProductsJson.isSomethingNew4Deleting = true;
        }
        if (ProductsJson.isSomethingNew4Deleting || !vboxes.get(key4vbox).containsKey(category)) {
            String[] categories = {
                    ProductsJson.breadCategoryFood, ProductsJson.grillCategoryFood,
                    ProductsJson.miscCategoryFood, ProductsJson.meetCategoryFood
            };
            for (String c : categories) {
                vboxes.get(key4vbox).put(
                        c, ControlHelper.getProductsForDellVbox(c, sure2dellProductPane,
                                price2dellTextField, name2dellTextField, type2dellTextField, category2dellTextField
                        )
                );
            }
            ProductsJson.isSomethingNew4Deleting = false;
        }
        categoryNameInProductList.setText("Удалить: " + category);
        scrollPane4Products.setContent(vboxes.get(key4vbox).get(category));
        ControlHelper.switchPane(orderCategoriesPane, orderProductsListPane);
    }

    @FXML
    void pressCategoryDellProduct(ActionEvent event) {
        virtualKB.hideKB();
        MenuItem clickedType = (MenuItem) event.getSource();
        fillListWithProducts2Dell(clickedType.getText());
        ControlHelper.switchPane(editMenuPane, orderProductsListPane);
    }

    @FXML
    void closeDellProductBtn() {
        price2dellTextField.setText("");
        name2dellTextField.setText("");
        type2dellTextField.setText("");
        category2dellTextField.setText("");
        sure2dellProductPane.setVisible(false);
    }

    @FXML
    void yesDellProductBtn() throws IOException {
        final String categoryName = category2dellTextField.getText();
        ProductsJson.delProduct(categoryName, name2dellTextField.getText());
        fillListWithProducts2Dell(categoryName);
        closeDellProductBtn();
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
                    ControlHelper.getPositionsVboxForEndOrder(
                            scrollPane4EndOrder, sure2DellPositionPane, position2dellTextField, yesDellPositionInOrder
                    )
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
    void addProductInOrderProductBtn() {
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
        final String signPressed = clickedNum.getText();
        final String alreadyWritten = score2addInOrderTextField.getText();
        final String productType = typeOfProductInAddOrder.getText();

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
        final String numPressed = clickedNum.getText();
        final String alreadyWritten = score2addInOrderTextField.getText();
        if (!(numPressed.contains(".") && alreadyWritten.contains("."))) {
            final String actualScore = alreadyWritten + numPressed;
            score2addInOrderTextField.setText(actualScore);
            BigDecimal actualPrice = new BigDecimal(actualScore).multiply(
                    new BigDecimal(priceOfProductInAddOrder.getText())
            );
            price2addInOrderTextField.setText(actualPrice.toString());
        }
    }

    @FXML
    void backspaceNumPressedInAddOrder() {
        final String alreadyWritten = score2addInOrderTextField.getText();
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
            virtualKB.hideKB();
            deliveryAddressInEndOrder.setText("Выдать на месте");
            deliveryAddressInEndOrder.setDisable(true);
            deliveryAddressInEndOrder.setEditable(false);
        }
    }

    @FXML
    void closePane2DellPositionInOrder() {
        sure2DellPositionPane.setVisible(false);
        position2dellTextField.setText("");
    }

    @FXML
    void closeLookOrderPane() {
        lookWrittenOrderPane.setVisible(false);
    }

    @FXML
    void sendOrderToKitchen() {
        String address = deliveryAddressInEndOrder.getText();
        btnCheckDeliveryInEndOrder.setSelected(false);
        btnDeliveryInEndOrder();
        if (address.isEmpty()) {
            address = "Выдать на месте";
        }
        try {
            Order.sendOrder(address);
        } catch (IOException ex) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea, "Заказ отправлен на кухню, но не записан в базу");
        }
        endOrderPane.setVisible(false);
        ControlHelper.switchPane(orderCategoriesPane, mainOrderPane);
        ControlHelper.fillOrdersVboxForOrderTable();
        scrollPane4EndOrder.setContent(null);
    }

    @FXML
    void doneOrderBtn() {
        String orderNum = orderNumKitchen.getText();
        try {
            if (!orderNum.isEmpty()) {
                Corridor2Talk.sendDoneOrder(Integer.parseInt(orderNum));
                OrdersJson.dellOrder(OrdersJson.inProcessKey, orderNum);
                ControlHelper.fillActiveOrderInKitchen();
                ControlHelper.showNotification(notificationPane, notificationText,
                        "Заказ " + orderNum + " выполнен"
                );
            } else {
                throw new Exception("can't end order in kitchen");
            }
        } catch (Exception ex) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea,
                    "Не могу завершить заказ\nAppController.java : doneOrderBtn()"
            );
        }
    }

    @FXML
    void closeNotification() {
        notificationPane.setVisible(false);
        if (isKitchenOpen) {
            ControlHelper.fillActiveOrderInKitchen();
        } else {
            ControlHelper.fillOrdersVboxForOrderTable();
        }
    }

    @FXML
    void refreshOrdersScrollPane() {
        if (isKitchenOpen) {
            ControlHelper.fillActiveOrderInKitchen();
        } else {
            ControlHelper.fillOrdersVboxForOrderTable();
        }
    }

    @FXML
    void orderIsDoneOrderTable() throws IOException {
        OrdersJson.dellOrder(OrdersJson.readyKey, orderNumInKitchenLook.getText());
        ControlHelper.fillOrdersVboxForOrderTable();
    }
}
