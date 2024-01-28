package homer.vkusmir;

import javafx.scene.layout.AnchorPane;

public class AppKeyboard {
    private static AnchorPane keyboardPane = null;

    public static void initKeyboard(AnchorPane virtualKeyboardPane) {
        if (keyboardPane == null) {
            VirtualKeyboard vkb = new VirtualKeyboard();
            vkb.view().setStyle("-fx-border-color: darkblue; -fx-border-radius: 5;");
            virtualKeyboardPane.getChildren().add(vkb.view());
            keyboardPane = virtualKeyboardPane;
        }
    }

    public static void showKB() {
        keyboardPane.setVisible(true);
    }

    public static void hideKB() {
        keyboardPane.setVisible(false);
    }
}
