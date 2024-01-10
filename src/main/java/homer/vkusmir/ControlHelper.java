package homer.vkusmir;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ControlHelper {

    private static int timesClickedForLog = 0;

    public static boolean alreadyGetLog = false;

    private static boolean isDefActive = false;

    public static boolean canReturnLog() {
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

    public static VBox getProductsVbox(String categoryName) {
        VBox clickableList = new VBox();
        clickableList.setPadding(new Insets(10, 10, 10, 10));
        clickableList.setSpacing(10);

        for (int i = 1; i <= 22; i++) {
            AnchorPane clickableProduct = new AnchorPane();
            clickableProduct.setPrefSize(725, 30);
            clickableProduct.setStyle("-fx-border-color: black; -fx-background-color: #fffdb8;");

            Label productName = new Label("     Маринованное мясо в собственном соку (синее)"+i);
            productName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

            clickableProduct.getChildren().add(productName);
            // clickableProduct.setOnMouseClicked(e -> writeHereYourDef());

            clickableList.getChildren().add(clickableProduct);
        }

        return clickableList;
    }
}
