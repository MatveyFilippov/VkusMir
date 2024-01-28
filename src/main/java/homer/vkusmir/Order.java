package homer.vkusmir;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class Order {
    public static String keyName = "name";
    public static String keyPrice = "price";
    public static String keyScore = "score";
    public static String keyAddress= "name";
    public static String keyNumber = "price";
    public static String keyPositions = "score";
    private static int orderNum = 0;
    public static ArrayList<Map<String, String>> orderList;
    public static BigDecimal finalPrice;

    public static void newOrder() {
        if (orderNum == 99) {
            orderNum = 1;
        } else {
            orderNum++;
        }
        orderList = new ArrayList<>();
        finalPrice = BigDecimal.ZERO;
    }

    public static void killOrder() {
        if (orderNum == 1) {
            orderNum = 99;
        } else {
            orderNum--;
        }
        cleanOrder();
    }

    public static boolean isOrderNull() {
        return orderList.size() == 0;
    }

    public static void appendPosition(String name, String price, String score) {
        finalPrice = finalPrice.add(new BigDecimal(price));
        Map<String, String> position = new HashMap<>();
        position.put(keyName, name);
        position.put(keyPrice, price);
        position.put(keyScore, score);
        orderList.add(position);
    }


    public static void sendOrder(String address) {
        Map<String, Object> data = new HashMap<>();
        data.put(keyPositions, orderList);
        data.put(keyAddress, address);
        data.put(keyNumber, String.valueOf(orderNum));
        Corridor2Talk.sendOrder(data);
    }

    public static void cleanOrder() {
        orderList = null;
        finalPrice = null;
    }
}
