package homer.vkusmir;

import javafx.scene.text.Text;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Corridor2Talk {
    private static int port = 1212;
    private static Thread listenOrdersThread = null;
    public static ServerSocket serverSocket;
    private static Text tempTextArea;

    public static void initKitchen(Text tmp) throws IOException {
        tempTextArea = tmp;
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                startWaitingOrders();
                break;
            } catch (IOException ex) {
                if (port > 9999) {
                    throw new IOException("Can't up server");
                }
                port++;
            }
        }
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

        Socket clientSocket = new Socket("localhost", port);

        OutputStream outputStream = clientSocket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(orderMap);

        clientSocket.close();
    }
}
