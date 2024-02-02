package com.inventory.store;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CompanyOrderSystem {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException{
        AuthenticationManager authManager = new AuthenticationManager();
        ProductManager productManager = new ProductManager();
        OrderManager orderManager = new OrderManager();

        while (true) {
            System.out.println("1. Place an order");
            System.out.println("2. Check recent order");
            System.out.println("3. Check previous order");
            System.out.println("4. Change password");
            System.out.println("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    placeOrder(authManager, productManager, orderManager);
                    break;
                case 2:
                    checkRecentOrder(orderManager);
                    break;
                case 3:
                    checkPreviousOrders(orderManager);
                    break;
                case 4:
                    changePassword(authManager);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void placeOrder(AuthenticationManager authManager, ProductManager productManager, OrderManager orderManager) {
        try {
            System.out.println("Enter your unique ID: ");
            String uniqueID = scanner.nextLine();

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            boolean isAuthenticated = authManager.authenticateUser(uniqueID, password);

            if (isAuthenticated) {
                System.out.println("Authentication successful. Available products:");

                try (BufferedReader productsReader = new BufferedReader(new FileReader("products.txt"))) {
                    String line;
                    while ((line = productsReader.readLine()) != null) {
                        System.out.println(line);
                    }

                    System.out.println("Enter product details (name, quantity): ");
                    String productDetails = scanner.nextLine();
                    String[] productInfo = productDetails.split(", ");

                    String productName = productInfo[0];
                    int orderedQuantity = Integer.parseInt(productInfo[1]);

                    productManager.updateProductQuantity(productName, orderedQuantity);
                    orderManager.addOrder(uniqueID, productName, orderedQuantity);

                    System.out.println("Order placed successfully. Thank you!");
                }
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        } catch (IOException e) {
            System.out.println("Error reading or writing files: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error placing order: " + e.getMessage());
        }
    }

    private static void checkRecentOrder(OrderManager orderManager) throws IOException {
        try {
            System.out.println("Enter your unique ID: ");
            String uniqueID = scanner.nextLine();

            orderManager.checkRecentOrder(uniqueID);
        } catch (Exception e) {
            System.out.println("Error checking recent order: " + e.getMessage());
        }
    }

    private static void checkPreviousOrders(OrderManager orderManager) throws IOException {
        try {
            System.out.println("Enter your unique ID: ");
            String uniqueID = scanner.nextLine();

            orderManager.checkPreviousOrders(uniqueID);
        } catch (Exception e) {
            System.out.println("Error checking previous orders: " + e.getMessage());
        }
    }

    private static void changePassword(AuthenticationManager authManager) {
        try {
            System.out.println("Enter your unique ID: ");
            String uniqueID = scanner.nextLine();

            System.out.println("Enter your current password: ");
            String currentPassword = scanner.nextLine();

            System.out.println("Enter your new password: ");
            String newPassword = scanner.nextLine();

            authManager.changePassword(uniqueID, currentPassword, newPassword);
        } catch (IOException e) {
            System.out.println("Error reading or writing files: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
        }
    }
}
