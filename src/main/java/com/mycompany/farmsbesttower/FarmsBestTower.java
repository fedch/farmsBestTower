/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.farmsbesttower;

/**
 *
 * @author elena
 */
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmsBestTower {

    public static String fetchBestTower(String targetFarmId) throws IOException {
        // This method returns the best tower for a given farmId
        // The best tower is the one with the highest average RSSI

        StringBuilder result = new StringBuilder();
        Map<String, Integer> totalRSSIMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        try {
            // Get the list of file links
            URL url = new URL("https://api.onizmx.com/lambda/tower_stream");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Read the response
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null;) {
                    result.append(line);
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL");
        }

        // Parse the response, removing the quotes and brackets
        List<String> files = new ArrayList<>(
                Arrays.asList(result.toString().replaceAll("[\\[\\]\"]", "").split(",")));

        // Process each file
        for (String path : files) {
            URL url = new URL(path);
            CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();

            // Parse each CSV file
            try (CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat)) {
                for (CSVRecord csvRecord : csvParser) {
                    if (csvRecord.get("farmId").equals(targetFarmId)) {
                        String farmId = csvRecord.get("farmId");
                        String towerId = csvRecord.get("towerId");
                        int rssi = Integer.parseInt(csvRecord.get("rssi"));

                        // Create a key for the current farm-tower combination
                        String key = farmId + "|" + towerId;

                        /*
                         * Update the total RSSI for the current farm-tower combination.
                         * If it doesn't exist, default to 0 and add the current RSSI
                         */
                        totalRSSIMap.put(key, totalRSSIMap.getOrDefault(key, 0) + rssi);
                        /*
                         * Increment the count for the current farm-tower combination.
                         * If it doesn't exist, initialize it to 1
                         */
                        countMap.put(key, countMap.getOrDefault(key, 0) + 1);
                    }
                }
            } catch (IOException e) {
                System.err.println("\nError processing URL: " + path + ".\nReason: " + e.getMessage());
            }
        }

        double highestAverageRSSI = 0;
        String bestTower = "";

        // Find the best tower
        if (totalRSSIMap.isEmpty()) {
            System.out.println("No towers found for farmId: " + targetFarmId);
            return "";
        } else {
            // Iterate through the map and find the highest average RSSI
            for (String key : totalRSSIMap.keySet()) {
                double averageRSSI = (double) totalRSSIMap.get(key) / countMap.get(key);

                if (averageRSSI > highestAverageRSSI || bestTower.isEmpty()) {
                    highestAverageRSSI = averageRSSI;
                    bestTower = key.split("\\|")[1];
                }
            }
            return bestTower;
        }
    }

    public static void main(String[] args) {
        String farmId = "06fc1b97-d26a-4081-b527-50184c387d89";

        try {
            String bestTower = fetchBestTower(farmId);
            System.out.println("\nThe best tower for farmId \"" + farmId + "\" is: " + bestTower + "\n");
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
}
