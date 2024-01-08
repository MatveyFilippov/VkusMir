package homer.vkusmir;

import javafx.scene.layout.AnchorPane;

public class ControlHelper {
    public static void switchPane(AnchorPane toClose, AnchorPane toOpen) {
        toOpen.setVisible(true);
        toClose.setVisible(false);
    }
}
