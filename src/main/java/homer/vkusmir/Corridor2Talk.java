package homer.vkusmir;

import javafx.concurrent.Task;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Corridor2Talk {
    private static final int port = 1212;
    private static Thread listenOrdersThread = null;
    private static Thread listenDoneOrdersThread = null;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ScrollPane activeOrders;
    private static AnchorPane errPane;
    private static TextArea errTextArea;
    private static final ArrayList<String> ips = VkusMirConfig.getIpsList();

    public static void initKitchen(AnchorPane errorPane, TextArea errorTextArea) throws IOException {
        serverSocket = new ServerSocket(port);
        errPane = errorPane;
        errTextArea = errorTextArea;
        startWaitingOrders();
    }

    public static void killKitchen() {
        try {
            if (listenOrdersThread != null) {
                listenOrdersThread.interrupt();
            }
            serverSocket.close();
        } catch (Exception ex) {
            // pass
        }
    }

    public static void initOrderTable(ScrollPane activeOrdersPane, AnchorPane errorPane, TextArea errorTextArea) throws IOException {
        activeOrders = activeOrdersPane;
        errPane = errorPane;
        errTextArea = errorTextArea;
        boolean isConnected = false;
        for (String host : ips) {
            try {
                clientSocket = new Socket(host, port);
                isConnected = true;
                break;
            } catch (IOException ex) {
                // pass
            }
        }
        if (!isConnected) {
            throw new IOException("can't connect to kitchen in 'Corridor2Talk.java :: initOrderTable()'");
        }
        startWaitingDoneOrders();
    }

    public static void killOrderTable() {
        try {
            if (listenDoneOrdersThread != null) {
                listenDoneOrdersThread.interrupt();
            }
            clientSocket.close();
        } catch (Exception ex) {
            // pass
        }
    }

    public static void sendDoneOrder(int orderNumber) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    OutputStream outputStream = clientSocket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(String.valueOf(orderNumber));
                } catch (IOException ex) {
                    ControlHelper.printErrorInApp(errPane, errTextArea,
                            "Возможно стол заказов не включен, ведь я не могу отправить выполненный заказ (нет клиента)\nCorridor2Talk.java :: sendDoneOrder()");
                    throw new IOException("can't send done order in 'Corridor2Talk.java :: sendDoneOrder()'");
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setName("Sending done order to kitchen");
        thread.setDaemon(true);
        thread.start();
    }

    public static void sendOrder(Map<String, Object> orderMap) {
        if (orderMap.size() == 0) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    OutputStream outputStream = clientSocket.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(orderMap);
                } catch (IOException ex) {
                    ControlHelper.printErrorInApp(errPane, errTextArea,
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

    private static void startWaitingDoneOrders() {
        if (listenDoneOrdersThread != null) {
            return;
        }

        listenDoneOrdersThread = new Thread(() -> {
            try {
                waitDoneOrders();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        listenDoneOrdersThread.setName("Wait done orders");
        listenDoneOrdersThread.setDaemon(true);
        listenDoneOrdersThread.start();
    }

    private static void waitOrders() throws IOException {
        clientSocket = serverSocket.accept();
        while (true) {
            if (listenOrdersThread.isInterrupted()) {
                listenOrdersThread = null;
                return;
            }
            try {
                InputStream inputStream = clientSocket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                Map<String, Object> orderMap = (Map<String, Object>) objectInputStream.readObject();
                loadOrder(orderMap);
            } catch (IOException | ClassNotFoundException ex) {
                // pass
            }
        }
    }

    private static void waitDoneOrders() throws IOException {
        while (true) {
            if (listenDoneOrdersThread.isInterrupted()) {
                listenDoneOrdersThread = null;
                return;
            }
            try {
                InputStream inputStream = clientSocket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);

                String receivedOrderNum = dataInputStream.readUTF();
                loadDoneOrder(receivedOrderNum);
            } catch (IOException ex) {
                // pass
            }
        }
    }

    private static void loadOrder(Map<String, Object>  order) {
        // TODO: вставить обработку
    }

    private static void loadDoneOrder(String doneOrderNum) {
        // TODO: вставить обработку
    }
}
