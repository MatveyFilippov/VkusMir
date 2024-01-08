package homer.vkusmir;

import javafx.scene.layout.AnchorPane;
import javafx.concurrent.Task;

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
}
