package homer.vkusmir;

import javafx.concurrent.Task;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class Corridor2Talk {  // TODO: беда со связью при отключении одного, нужно бы решить
    private static final int port = 1212;
    private static Thread listenOrdersThread = null;
    private static Thread listenDoneOrdersThread = null;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ScrollPane activeOrders;
    private static AnchorPane errPane;
    private static TextArea errTextArea;
    private static final ArrayList<String> ips = VkusMirConfig.getIpsList();

    private static Socket getClientSocket4Kitchen() throws IOException {
        for (String host : ips) {
            try {
                return new Socket(host, port);
            } catch (IOException ex) {
                // pass
            }
        }
        throw new IOException("can't connect to kitchen in 'Corridor2Talk.java :: initOrderTable()'");
    }

    public static void initKitchen(AnchorPane errorPane, TextArea errorTextArea, ScrollPane activeOrdersPane) throws IOException {
        activeOrders = activeOrdersPane;
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
        System.gc();
    }

    public static void initOrderTable(ScrollPane activeOrdersPane, AnchorPane errorPane, TextArea errorTextArea) throws IOException {
        activeOrders = activeOrdersPane;
        errPane = errorPane;
        errTextArea = errorTextArea;
        clientSocket = getClientSocket4Kitchen();
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
        System.gc();
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
                            "Возможно стол заказов не включен, ведь я не могу отправить выполненный заказ\nCorridor2Talk.java :: sendDoneOrder()");
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
                    if (!clientSocket.isConnected()) {
                        throw new IOException("connection with kitchen is lost");
                    }
                    OutputStream outputStream = clientSocket.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(orderMap);
                } catch (Exception ex) {
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
        while (true) {
            clientSocket = serverSocket.accept();
            while (true) {
                try {
                    InputStream inputStream = clientSocket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                    Map<String, Object> orderMap = (Map<String, Object>) objectInputStream.readObject();
                    loadOrder(orderMap);
                } catch (IOException | ClassNotFoundException ex) {
                    break;
                }
            }
            if (listenOrdersThread.isInterrupted() || serverSocket.isClosed()) {
                listenOrdersThread = null;
                return;
            }
        }
    }

    private static void waitDoneOrders() throws IOException {
        while (true) {
            clientSocket = getClientSocket4Kitchen();
            while (true) {
                try {
                    InputStream inputStream = clientSocket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);

                    String receivedOrderNum = dataInputStream.readUTF();
                    loadDoneOrder(receivedOrderNum);
                } catch (IOException ex) {
                    if (listenDoneOrdersThread.isInterrupted()) {
                        listenDoneOrdersThread = null;
                        return;
                    }
                    ControlHelper.printErrorInApp(errPane, errTextArea,
                            "Возможно кухня выключена (если нет, то перезапустите приложение)\nОшибка со связью\nCorridor2Talk.java :: waitDoneOrders()");
                    break;
                }
            }
            if (listenDoneOrdersThread.isInterrupted()) {
                listenDoneOrdersThread = null;
                return;
            }
        }
    }

    private static void loadOrder(Map<String, Object>  order) {
        System.out.println(order);
    }

    private static void loadDoneOrder(String doneOrderNum) {
        // TODO: вставить обработку
    }
}
