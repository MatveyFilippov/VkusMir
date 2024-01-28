package homer.vkusmir;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Map;

public class ControlHelper {

    private static int timesClickedForLog = 0;

    public static boolean alreadyGetLog = false;

    private static boolean isDefActive = false;

    public static boolean isClickedFiveTimes() {
        if (alreadyGetLog) {
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

    public static String toRGBCode(String webColour) {
        Color color = Color.web(webColour);
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }

    public static VBox getProductsVboxForOrder(String productCategory, AnchorPane addProductInOrderPane,
                                               Text typeOfProductInAddOrder, Text priceOfProductInAddOrder,
                                               TextField name2addInOrderTextField) {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);

        Map<String, Map<String, String>> products = ProductsJson.getProductsItems(productCategory);

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
    }

    public static VBox getProductsForDellVbox(String productCategory, AnchorPane pane2dell,
                                              TextField price2dellTextField, TextField name2dellTextField,
                                              TextField type2dellTextField, TextField category2dellTextField) {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);

        Map<String, Map<String, String>> products = ProductsJson.getProductsItems(productCategory);

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
    }

    public static VBox getPositionsVboxForEndOrder() {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);

        for (Map<String, String> position : Order.orderList) {
            String productName= position.get(Order.keyName);
            String productScore = position.get(Order.keyScore);
            String productPrice = position.get(Order.keyPrice);

            AnchorPane clickablePosition = new AnchorPane();
            clickablePosition.setPrefSize(725, 30);
            clickablePosition.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");

            Label productLabel = new Label(
                    "     " + productName + "  |  Кол-во: " + productScore + "  -->  " + productPrice + "₽"
            );
            productLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

            clickablePosition.getChildren().add(productLabel);
//            clickablePosition.setOnMouseClicked(e ->
//                    def2DellProductInList(productCategory, productType, productPrice, productName, pane2dell,
//                            price2dellTextField, name2dellTextField, type2dellTextField, category2dellTextField)
//            );
            // TODO: остановился здесь (вставить функцию для удаления)
            clickableList.getChildren().add(clickablePosition);
        }

        return clickableList;
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

}
