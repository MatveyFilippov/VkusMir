package homer.vkusmir;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Corridor2Talk {
    private static final int port = 1212;
    private static Thread listenOrdersThread = null;
    public static ServerSocket serverSocket;
    private static Text tempTextArea;
    private static final ArrayList<String> ips = VkusMirConfig.getIpsList();

    public static void initKitchen(Text tmp) throws IOException {
        tempTextArea = tmp;
        serverSocket = new ServerSocket(port);
        startWaitingOrders();
    }

    public static void killKitchen() {
        try {
            if (listenOrdersThread != null) {
                listenOrdersThread.interrupt();
                listenOrdersThread = null;
            }
            serverSocket.close();
        } catch (Exception ex) {
            // pass
        }
    }

    private static void startWaitingOrders() {
        if (listenOrdersThread != null) {
            return;
        }

        listenOrdersThread = new Thread(() -> {
            try {
                waitOrders();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        listenOrdersThread.setName("Wait orders");
        listenOrdersThread.setDaemon(true);
        listenOrdersThread.start();
    }

    private static void waitOrders() throws IOException {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                InputStream inputStream = clientSocket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                Map<String, String> orderMap = (Map<String, String>) objectInputStream.readObject();

                tempTextArea.setText(orderMap.get("name"));  // TODO: do smt with order

                clientSocket.close();
            } catch (IOException | ClassNotFoundException ex) {
                // pass
            }
        }
    }

    public static void sendOrder(Map<String, String> orderMap, AnchorPane errorPane, TextArea errorTextArea) throws IOException {
        if (orderMap.size() == 0) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                boolean isSent = false;
                for (String host : ips) {
                    try (Socket clientSocket = new Socket(host, port)) {
                        OutputStream outputStream = clientSocket.getOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.writeObject(orderMap);
                        isSent = true;
                        return null;
                    } catch (IOException ex) {
                        // pass
                    }
                }

                if (!isSent) {
                    ControlHelper.printErrorInApp(errorPane, errorTextArea,
                            "Возможно кухня не включена, ведь я не могу отправить заказ (нет сервера)\nCorridor2Talk.java :: sendOrder()");
                    throw new IOException("can't send order in 'Corridor2Talk.java :: sendOrder()'");
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setName("Sending order to kitchen");
        thread.setDaemon(true);
        thread.start();
    }
}
