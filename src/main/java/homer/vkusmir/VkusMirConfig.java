package homer.vkusmir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class VkusMirConfig {
    private static Map<String, Map<String, String>> values = new HashMap<>();

    public static void readIniFile(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            String currentSection = "";

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("[")) {  // New section
                    int endIndex = line.indexOf("]");
                    currentSection = line.substring(1, endIndex);

                    Map<String, String> sectionValues = new HashMap<>();
                    values.put(currentSection, sectionValues);
                } else {  // Key-value pair
                    int equalsIndex = line.indexOf("=");
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();

                    values.get(currentSection).put(key, value);
                }
            }
        }
    }

    public static ArrayList<String> getIpsList() {
        ArrayList<String> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, String>> section : values.entrySet()) {
            if (section.getKey().equals("IPs")) {
                for (Map.Entry<String, String> item : section.getValue().entrySet()) {
                    result.add(item.getValue());
                }
            }
        }

        return result;
    }
}
