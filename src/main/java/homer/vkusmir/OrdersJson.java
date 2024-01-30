package homer.vkusmir;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class OrdersJson {

    public static final String inProcessKey = "Готовятся";
    public static final String readyKey = "Готовы";

    private static String readFile() {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(Application.ordersJsonPath)));
        } catch (IOException e) {
            content = "{\""+inProcessKey+"\":{},\""+readyKey+"\":{}}";
        }
        return content;
    }

    private static void writeFile(String content) throws IOException {
        try {
            Files.write(Paths.get(Application.ordersJsonPath), content.getBytes());
        } catch (IOException e) {
            try {
                FileWriter writer = new FileWriter(Application.ordersJsonPath);
                writer.write(content);
                writer.close();
            } catch (IOException e2nd) {
                throw new IOException("can't write data to json; OrdersJson.java :: writeToFile()");
            }
        }
    }

    private static void refreshCategory(String categoryName, JSONObject jsonCategory) throws IOException {
        JSONObject jsonData = new JSONObject(readFile());
        jsonData.put(categoryName, jsonCategory);
        writeFile(jsonData.toString());
    }

    public static JSONObject getCategoryFromJson(String category) {
        JSONObject jsonData = new JSONObject(readFile());
        return jsonData.getJSONObject(category);
    }

    public static void appendNewOrder(final Map<String, Object> order) throws IOException {
        JSONObject jsonCategory = getCategoryFromJson(inProcessKey);

        JSONObject jsonOrder = new JSONObject(order);
        jsonOrder.remove(Order.keyNumber);

        jsonCategory.put((String) order.get(Order.keyNumber), jsonOrder);
        refreshCategory(inProcessKey, jsonCategory);
    }

    public static void dellOrder(final String category, final String num) throws IOException {
        JSONObject jsonCategory = getCategoryFromJson(category);
        if (jsonCategory.has(num)) {
            jsonCategory.remove(num);
            refreshCategory(category, jsonCategory);
        }
    }

    public static void changeCategory2Done(String orderNum) throws IOException {
        JSONObject jsonData = new JSONObject(readFile());
        JSONObject category = jsonData.getJSONObject(inProcessKey);
        if (category.has(orderNum)) {
            JSONObject order = category.getJSONObject(orderNum);
            jsonData.getJSONObject(readyKey).put(orderNum, order);
            jsonData.getJSONObject(inProcessKey).remove(orderNum);
            writeFile(jsonData.toString());
        }

    }

    public static int getLastInProcessNum() {
        final JSONObject jsonCategory = getCategoryFromJson(inProcessKey);
        int biggestNum = 0;
        int lowestNum = 99;
        for (String number : jsonCategory.keySet()) {
            int actualNum = Integer.parseInt(number);
            if (biggestNum < actualNum) {
                biggestNum = actualNum;
            }
            if (lowestNum > actualNum) {
                lowestNum = actualNum;
            }
        }
        if (biggestNum > 75 && lowestNum < 25) {
            biggestNum = 0;
            for (String number : jsonCategory.keySet()) {
                int actualNum = Integer.parseInt(number);
                if (biggestNum < actualNum  && actualNum < 25) {
                    biggestNum = actualNum;
                }
            }
        }
        return biggestNum;
    }
}
