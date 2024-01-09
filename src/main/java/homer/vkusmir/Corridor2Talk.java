package homer.vkusmir;

import javafx.scene.text.Text;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class Corridor2Talk {
    private static int port = 1212;
    private static Thread listenOrdersThread = null;
    public static ServerSocket serverSocket;
    private static Text tempTextArea;
    private static final ArrayList<String> ips = VkusMirConfig.getIpsList();

    public static void initKitchen(Text tmp) throws IOException {
        tempTextArea = tmp;
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                tempTextArea.setText(serverSocket.toString());
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

    public static void sendOrder(Map<String, String> orderMap) {
        if (orderMap.size() == 0) {
            return;
        }

        for (String host : ips) {
            try (Socket clientSocket = new Socket(host, port)) {
                OutputStream outputStream = clientSocket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(orderMap);
                break;
            } catch (Exception ex) {
                // pass
            }
        }

        // TODO: create mark that you don't send and throw Exception
    }
}
