package homer.vkusmir;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ProductsJson {
    public static final String meetCategoryFood = "Маринованное";
    public static final String grillCategoryFood = "Шашлык";
    public static final String breadCategoryFood = "Шаурма";
    public static final String miscCategoryFood = "Прочее";
    public static final String typeKeyFood = "Тип";
    public static final String priceKeyFood = "Стоимость";

    private static String readFile() {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(Application.productsJsonPath)));
        } catch (IOException e) {
            content = "{\""+meetCategoryFood+"\":{},\""+grillCategoryFood+"\":{},\""+breadCategoryFood+"\":{},\""+miscCategoryFood+"\":{}}";
        }
        return content;
    }

    private static void writeFile(String content) throws IOException {
        try {
            Files.write(Paths.get(Application.productsJsonPath), content.getBytes());
        } catch (IOException e) {
            try {
                FileWriter writer = new FileWriter(Application.productsJsonPath);
                writer.write(content);
                writer.close();
            } catch (IOException e2nd) {
                throw new IOException("can't write data to json; ProductsJson.java :: writeToFile()");
            }
        }
    }

    private static void refreshCategory(String categoryName, JSONObject jsonCategory) throws IOException {
        JSONObject jsonData = new JSONObject(readFile());
        jsonData.put(categoryName, jsonCategory);
        writeFile(jsonData.toString());
    }

    private static JSONObject getCategoryFromJson(String category) {
        JSONObject jsonData = new JSONObject(readFile());
        return jsonData.getJSONObject(category);
    }

    public static void appendNewProduct(String category, String type, String price, String name) throws IOException {
        JSONObject jsonCategory = getCategoryFromJson(category);

        JSONObject jsonItems = new JSONObject();
        jsonItems.put(typeKeyFood, type);
        jsonItems.put(priceKeyFood, price);

        jsonCategory.put(name, jsonItems);
        refreshCategory(category, jsonCategory);
    }

    public static Map<String, Map<String, String>> getProductsItems(String category) {
        JSONObject jsonCategory = getCategoryFromJson(category);
        Map<String, Map<String, String>> result = new HashMap<>();

        for (String name : jsonCategory.keySet()) {
            Map<String, String> tmp = new HashMap<>();
            result.put(name, tmp);
            JSONObject items = jsonCategory.getJSONObject(name);
            for (String key : items.keySet()) {
                result.get(name).put(key, items.get(key).toString());
            }
        }

        return result;
    }

    public static void delProduct(String category, String name) throws IOException {
        JSONObject jsonCategory = getCategoryFromJson(category);
        jsonCategory.remove(name);
        refreshCategory(category, jsonCategory);
    }
}
