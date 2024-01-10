package homer.vkusmir;

import javafx.scene.text.Text;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

    public static void sendOrder(Map<String, String> orderMap) throws IOException {
        if (orderMap.size() == 0) {
            return;
        }

        boolean isSended = false;
        for (String host : ips) {
            try (Socket clientSocket = new Socket(host, port)) {
                OutputStream outputStream = clientSocket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(orderMap);
                isSended = true;
                break;
            } catch (IOException ex) {
                // pass
            }
        }

        if (!isSended) {
            throw new IOException("can't send order in 'Corridor2Talk.java :: sendOrder()'");
        }

//        try {
//            Corridor2Talk.sendOrder(data);
//        } catch (IOException ex) {
//            ControlHelper.printErrorInApp(errorPane, errorTextArea,
//                    "Возможно кухня не включена, ведь я не могу отправить заказ (нет сервера)\nCorridor2Talk.java :: sendOrder()");
//            throw new IOException(ex);
//        }
    }
}
