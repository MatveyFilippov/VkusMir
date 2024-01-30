package homer.vkusmir;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ControlHelper {

    private static int timesClickedForLog = 0;
    public static boolean alreadyShowSecretTask = false;
    private static boolean isDefActive = false;
    private static Map<String, Object> map4fillingOrders;

    public static boolean isClickedFiveTimes() {
        if (alreadyShowSecretTask) {
            return false;
        }

        timesClickedForLog++;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (!isDefActive) {
                    isDefActive = true;
                    Thread.sleep(2000);
                    timesClickedForLog = 0;
                    isDefActive = false;
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setName("Clicked 5 times");
        thread.setDaemon(true);
        thread.start();


        return timesClickedForLog > 4;
    }

    public static void switchPane(AnchorPane toClose, AnchorPane toOpen) {
        toOpen.setVisible(true);
        toClose.setVisible(false);
    }

    public static void printErrorInApp(AnchorPane errorPane, TextArea errorTextArea, String errorText) {
        errorPane.setVisible(true);
        errorTextArea.setText(errorText);
    }

    public static VBox getProductsVboxForOrder(String productCategory, AnchorPane addProductInOrderPane,
                                               Text typeOfProductInAddOrder, Text priceOfProductInAddOrder,
                                               TextField name2addInOrderTextField) {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);
        clickableList.getChildren().clear();

        Map<String, Map<String, String>> products = ProductsJson.getProductsItems(productCategory);
        try {
            for (String productName : products.keySet()) {
                String productType = products.get(productName).get(ProductsJson.typeKeyFood);
                String productPrice = products.get(productName).get(ProductsJson.priceKeyFood);

                AnchorPane clickableProduct = new AnchorPane();
                clickableProduct.setPrefSize(725, 30);
                clickableProduct.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");

                Label productLabel = new Label("     " + productName + "  |  " + productPrice + "₽/" + productType);
                productLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

                clickableProduct.getChildren().add(productLabel);
                clickableProduct.setOnMouseClicked(e ->
                        def2AddProductInOrder(
                                addProductInOrderPane, name2addInOrderTextField,
                                typeOfProductInAddOrder, priceOfProductInAddOrder,
                                productType, productPrice, productName
                        )
                );

                clickableList.getChildren().add(clickableProduct);
            }

            return clickableList;
        } finally {
            System.gc();
        }
    }

    public static VBox getProductsForDellVbox(String productCategory, AnchorPane pane2dell,
                                              TextField price2dellTextField, TextField name2dellTextField,
                                              TextField type2dellTextField, TextField category2dellTextField) {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);
        clickableList.getChildren().clear();

        Map<String, Map<String, String>> products = ProductsJson.getProductsItems(productCategory);
        try {
            for (String productName : products.keySet()) {
                String productType = products.get(productName).get(ProductsJson.typeKeyFood);
                String productPrice = products.get(productName).get(ProductsJson.priceKeyFood);

                AnchorPane clickableProduct = new AnchorPane();
                clickableProduct.setPrefSize(725, 30);
                clickableProduct.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");

                Label productLabel = new Label("     " + productName);
                productLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

                clickableProduct.getChildren().add(productLabel);
                clickableProduct.setOnMouseClicked(e ->
                        def2DellProductInList(productCategory, productType, productPrice, productName, pane2dell,
                                price2dellTextField, name2dellTextField, type2dellTextField, category2dellTextField)
                );

                clickableList.getChildren().add(clickableProduct);
            }

            return clickableList;
        } finally {
            System.gc();
        }
    }

    public static VBox getPositionsVboxForEndOrder(ScrollPane positionsPane, AnchorPane sure2dell,
                                                   TextField position2dell, Text yesDellPosition,
                                                   AnchorPane endOrderPane) {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);
        clickableList.getChildren().clear();

        try {
            for (Map<String, String> position : Order.orderList) {
                AnchorPane clickablePosition = new AnchorPane();
                clickablePosition.setPrefSize(725, 30);
                clickablePosition.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");

                final String positionInfo = "     " + position.get(Order.keyName) + "  |  Кол-во: " + position.get(Order.keyScore) + "  -->  " + position.get(Order.keyPrice) + "₽";

                Label positionLabel = new Label(positionInfo);
                positionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

                clickablePosition.getChildren().add(positionLabel);
                clickablePosition.setOnMouseClicked(e ->
                        def2SureDellProductInOrder(position, positionsPane, sure2dell, position2dell, yesDellPosition,
                                positionInfo, endOrderPane)
                );
                clickableList.getChildren().add(clickablePosition);
            }

            return clickableList;
        } finally {
            System.gc();
        }
    }

    public static void initAndFillOrdersVboxForOrderTable(AnchorPane lookOrderPane, ScrollPane scrollPaneProcess,
                                                          ScrollPane scrollPaneDone, TextField orderNumText, Button doneBtn,
                                                          TextField addressField, TextField priceField, ScrollPane positions) {
        map4fillingOrders = null;
        map4fillingOrders = new HashMap<>();
        map4fillingOrders.put("lookOrderPane", lookOrderPane);
        map4fillingOrders.put("scrollPaneProcess", scrollPaneProcess);
        map4fillingOrders.put("scrollPaneDone", scrollPaneDone);
        map4fillingOrders.put("orderNumText", orderNumText);
        map4fillingOrders.put("doneBtn", doneBtn);
        map4fillingOrders.put("addressField", addressField);
        map4fillingOrders.put("priceField", priceField);
        map4fillingOrders.put("positions", positions);
        fillOrdersVboxForOrderTable();
    }

    public static void fillOrdersVboxForOrderTable() {
        AnchorPane lookOrderPane = (AnchorPane) map4fillingOrders.get("lookOrderPane");
        ScrollPane scrollPaneProcess = (ScrollPane) map4fillingOrders.get("scrollPaneProcess");
        ScrollPane scrollPaneDone = (ScrollPane) map4fillingOrders.get("scrollPaneDone");
        ScrollPane positions = (ScrollPane) map4fillingOrders.get("positions");
        TextField orderNumText = (TextField) map4fillingOrders.get("orderNumText");
        TextField addressField = (TextField) map4fillingOrders.get("addressField");
        TextField priceField = (TextField) map4fillingOrders.get("priceField");
        Button doneBtn = (Button) map4fillingOrders.get("doneBtn");


        scrollPaneProcess.setContent(null);
        scrollPaneDone.setContent(null);
        scrollPaneProcess.setContent(
                getOrdersVboxForOrderTable(
                        OrdersJson.getCategoryFromJson(OrdersJson.inProcessKey), false, lookOrderPane,
                        orderNumText, doneBtn, addressField, priceField, positions
                )
        );
        scrollPaneDone.setContent(
                getOrdersVboxForOrderTable(
                        OrdersJson.getCategoryFromJson(OrdersJson.readyKey), true, lookOrderPane, orderNumText,
                        doneBtn, addressField, priceField, positions
                )
        );
    }

    private static VBox getOrdersVboxForOrderTable(JSONObject jsonCategory, boolean closeable, AnchorPane lookOrderPane,
                                                   TextField orderNumText, Button doneBtn, TextField addressField,
                                                   TextField priceField, ScrollPane positionsScrollPane) {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);
        clickableList.getChildren().clear();

        try {
            for (String orderNum : jsonCategory.keySet()) {
                AnchorPane clickablePosition = new AnchorPane();
                clickablePosition.setPrefSize(725, 30);
                clickablePosition.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");

                final JSONObject order = (JSONObject) jsonCategory.get(orderNum);
                final String address = (String) order.get(Order.keyAddress);
                final String globalPrice = (String) order.get(Order.keyOrderPrice);
                final JSONArray positions = (JSONArray) order.get(Order.keyPositions);

                Label positionLabel = new Label("     " + orderNum + "  |  " + address + "  |  " + globalPrice + "₽");
                positionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

                clickablePosition.getChildren().add(positionLabel);
                clickablePosition.setOnMouseClicked(e ->
                        {
                            lookOrderPane.setVisible(true);
                            doneBtn.setDisable(!closeable);
                            def2lookOrder(
                                    positions, orderNum, address, globalPrice, orderNumText, addressField, priceField,
                                    positionsScrollPane
                            );
                        }
                );
                clickableList.getChildren().add(clickablePosition);
            }
            return clickableList;
        } finally {
            System.gc();
        }
    }

    public static void initAndFillActiveOrderInKitchen(ScrollPane ordersPane, TextField orderNum, TextField orderAddress,
                                                       ScrollPane positionsPane) {
        map4fillingOrders = null;
        map4fillingOrders = new HashMap<>();
        map4fillingOrders.put("ordersPane", ordersPane);
        map4fillingOrders.put("orderNum", orderNum);
        map4fillingOrders.put("orderAddress", orderAddress);
        map4fillingOrders.put("positionsPane", positionsPane);
        fillActiveOrderInKitchen();
    }

    public static void fillActiveOrderInKitchen() {
        ScrollPane ordersPane = (ScrollPane) map4fillingOrders.get("ordersPane");
        ordersPane.setContent(null);
        ScrollPane positionsPane = (ScrollPane) map4fillingOrders.get("positionsPane");
        TextField orderAddress = (TextField) map4fillingOrders.get("orderAddress");
        TextField orderNum = (TextField) map4fillingOrders.get("orderNum");
        ordersPane.setContent(
                getOrdersVboxForOrderKitchen(orderNum, orderAddress, positionsPane)
        );
    }

    private static VBox getOrdersVboxForOrderKitchen(TextField orderNum, TextField orderAddress,
                                                     ScrollPane positionsPane) {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);
        clickableList.getChildren().clear();
        JSONObject jsonCategory = OrdersJson.getCategoryFromJson(OrdersJson.inProcessKey);

        try {
            for (String num : jsonCategory.keySet()) {
                AnchorPane clickableOrder = new AnchorPane();
                clickableOrder.setPrefSize(125, 30);
                clickableOrder.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");

                final JSONObject order = (JSONObject) jsonCategory.get(num);
                final String address = (String) order.get(Order.keyAddress);
                final JSONArray positions = (JSONArray) order.get(Order.keyPositions);

                Label orderLabel = new Label("     " + num);
                orderLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

                clickableOrder.getChildren().add(orderLabel);
                clickableOrder.setOnMouseClicked(e ->
                        {
                            orderNum.setText(num);
                            orderAddress.setText(address);
                            positionsPane.setContent(null);
                            positionsPane.setContent(
                                    getPositionsInKitchen(positions)
                            );
                        }
                );
                clickableList.getChildren().add(clickableOrder);
            }
            return clickableList;
        } finally {
            System.gc();
        }
    }

    private static void def2DellProductInList(String category, String type, String price, String name, AnchorPane pane2dell,
                                              TextField price2dellTextField, TextField name2dellTextField,
                                              TextField type2dellTextField, TextField category2dellTextField) {
        price2dellTextField.setText(price);
        name2dellTextField.setText(name);
        type2dellTextField.setText(type);
        category2dellTextField.setText(category);
        pane2dell.setVisible(true);
    }

    private static void def2AddProductInOrder(AnchorPane addProductInOrderPane, TextField name2addInOrderTextField,
                                              Text typeOfProductInAddOrder, Text priceOfProductInAddOrder,
                                              String productType, String productPrice, String productName) {
        name2addInOrderTextField.setText(productName);
        typeOfProductInAddOrder.setText(productType);
        priceOfProductInAddOrder.setText(productPrice);
        addProductInOrderPane.setVisible(true);
    }

    private static void def2SureDellProductInOrder(Map<String, String> position, ScrollPane positionsPane,
                                                   AnchorPane sure2dell, TextField position2dell, Text yesDellPosition,
                                                   final String positionInfo, AnchorPane endOrderPane) {
        sure2dell.setVisible(true);
        position2dell.setText(positionInfo);
        yesDellPosition.setOnMouseClicked(e ->
                {
                    Order.delPosition(position);
                    sure2dell.setVisible(false);
                    if (Order.isOrderNull()) {
                        endOrderPane.setVisible(false);
                        return;
                    }
                    positionsPane.setContent(
                            ControlHelper.getPositionsVboxForEndOrder(
                                    positionsPane, sure2dell, position2dell, yesDellPosition, endOrderPane
                            )
                    );
                    position2dell.setText("");
                }
        );
    }

    private static void def2lookOrder(final JSONArray positions, final String num, final String address,
                                      final String globalPrice, TextField orderNum, TextField addressField,
                                      TextField priceField, ScrollPane positionsScrollPane) {
        try {
            addressField.setText(address);
            orderNum.setText(num);
            priceField.setText(globalPrice);
            positionsScrollPane.setContent(null);

            VBox clickableList = new VBox();
            clickableList.setPadding(new Insets(10, 10, 10, 10));
            clickableList.setSpacing(10);
            clickableList.getChildren().clear();
            for (Object position : positions) {
                AnchorPane positionLine = new AnchorPane();
                positionLine.setPrefSize(725, 30);
                positionLine.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");
                JSONObject workablePosition = (JSONObject) position;

                final String positionInfo = "     " + workablePosition.get(Order.keyName) + "  |  Кол-во: " + workablePosition.get(Order.keyScore) + "  -->  " + workablePosition.get(Order.keyPrice) + "₽";

                Label positionLabel = new Label(positionInfo);
                positionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");
                positionLine.getChildren().add(positionLabel);
                clickableList.getChildren().add(positionLine);
            }
            positionsScrollPane.setContent(clickableList);
        } finally {
            System.gc();
        }
    }

    private static VBox getPositionsInKitchen(JSONArray positions) {
        try {
            VBox clickableList = new VBox();
            clickableList.setPadding(new Insets(10, 10, 10, 10));
            clickableList.setSpacing(10);
            clickableList.getChildren().clear();

            for (Object position : positions) {
                AnchorPane positionLine = new AnchorPane();
                positionLine.setPrefSize(500, 30);
                positionLine.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");
                JSONObject workablePosition = (JSONObject) position;

                final String positionInfo = "     " + workablePosition.get(Order.keyName) + "  |  Кол-во: " + workablePosition.get(Order.keyScore);

                Label positionLabel = new Label(positionInfo);
                positionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");
                positionLine.getChildren().add(positionLabel);
                clickableList.getChildren().add(positionLine);
            }
            return clickableList;
        } finally {
            System.gc();
        }
    }

    public static void showNotification(AnchorPane notePane, Text noteText, final String text) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                notePane.setVisible(true);
                noteText.setText(text);
                Thread.sleep(5000);
                notePane.setVisible(false);
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setName("Show notification");
        thread.setDaemon(true);
        thread.start();
    }
}
