package homer.vkusmir;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(Application::logError);
        VkusMirConfig.readIniFile("Datas4VkusMirApp/VkusMirProperties.ini");

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("VkusMirView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 550);
        stage.setTitle("VkusMir");
        stage.setScene(scene);
        stage.show();
    }

    private static void logError(Thread thread, Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        String errorString = stringWriter.toString();


        try {
            FileWriter fileWriter = new FileWriter("Datas4VkusMirApp/VkusMirLogs.log", true);
            PrintWriter errorLog = new PrintWriter(fileWriter);
            errorLog.println("Error occurred on thread: " + thread.getName());
            errorLog.println("Stack Trace:");
            errorLog.println(errorString);
            errorLog.println("------------------------");
            errorLog.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}