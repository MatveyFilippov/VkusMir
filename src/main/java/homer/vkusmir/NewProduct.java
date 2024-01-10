package homer.vkusmir;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class NewProduct {
    public static String category = null;
    public static String type = null;
    private static float price = 0;
    private static String name = null;


    public static boolean isAllField(AnchorPane errorPane, TextArea errorTextArea, TextField price, TextField name) {
        if (category == null) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea, "Вы не выбрали категорию товара");
            return false;
        }
        if (type == null) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea, "Вы не выбрали тип товара");
            return false;
        }
        try {
            String tmpPrice = price.getText();
            if (tmpPrice.replace(" ", "").isEmpty()) {
                throw new Exception();
            }
            NewProduct.price = Float.parseFloat(tmpPrice);
        } catch (Exception ex) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea, "Ошибка в цене товара");
            return false;
        }
        try {
            String tmpName = name.getText();
            if (tmpName.replace(" ", "").isEmpty()) {
                throw new Exception();
            }
            NewProduct.name = tmpName;
        } catch (Exception ex) {
            ControlHelper.printErrorInApp(errorPane, errorTextArea, "Вы не ввели название товара");
            return false;
        }

        return true;
    }

    public static void putProductInBase() {
        category = null;
        type = null;
        price = 0;
        name = null;
    }
}
