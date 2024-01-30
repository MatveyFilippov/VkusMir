package homer.vkusmir;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class Order {
    public static final String keyName = "name";
    public static final String keyPrice = "price";
    public static final String keyScore = "score";
    public static final String keyAddress= "address";
    public static final String keyNumber = "order_num";
    public static final String keyPositions = "positions";
    public static final String keyOrderPrice = "global_price";
    private static int orderNum = OrdersJson.getLastInProcessNum();
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


    public static void sendOrder(String address) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put(keyPositions, orderList);
        data.put(keyAddress, address);
        data.put(keyNumber, String.valueOf(orderNum));
        data.put(keyOrderPrice, finalPrice.toString());
        Corridor2Talk.sendOrder(data);
        OrdersJson.appendNewOrder(data);
        cleanOrder();
    }

    public static void delPosition(Map<String, String> position) {
        orderList.remove(position);
    }

    private static void cleanOrder() {
        orderList = null;
        finalPrice = null;
    }
}
