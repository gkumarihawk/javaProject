package com.inventory.store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ProductManager {
    private final String PRODUCTS_FILE = "products.txt";

    public void updateProductQuantity(String productName, int orderedQuantity) throws IOException {
        try (BufferedReader productsReader = new BufferedReader(new FileReader(PRODUCTS_FILE));
             BufferedWriter productsWriter = new BufferedWriter(new FileWriter(PRODUCTS_FILE + ".tmp", true))) {

            StringBuilder updatedProducts = new StringBuilder();
            String line;

            while ((line = productsReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(productName)) {
                    int currentQuantity = Integer.parseInt(parts[1]);
                    int updatedQuantity = currentQuantity - orderedQuantity;
                    if (updatedQuantity < 0) {
                        System.out.println("Error: Insufficient stock for " + productName);
                        return;
                    }
                    updatedProducts.append(productName).append(", ").append(updatedQuantity).append("\n");
                } else {
                    updatedProducts.append(line).append("\n");
                }
            }

            productsWriter.write(updatedProducts.toString());
        }
    }

}
