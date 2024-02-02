package com.inventory.store;

import java.io.BufferedReader;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.Document;

public class OrderManager {
    private final String ORDERS_COLLECTION = "orders"; // MongoDB collection name
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> ordersCollection;

    public OrderManager() {
        try {
            // Connect to MongoDB
            this.mongoClient = MongoClients.create("mongodb://localhost:27017");
            this.database = mongoClient.getDatabase("your_database_name"); // Replace with your MongoDB database name
            this.ordersCollection = database.getCollection(ORDERS_COLLECTION);
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to MongoDB: " + e.getMessage(), e);
        }
    }

    void addOrder(String uniqueID, String productName, int orderedQuantity) {
        try {
            Date currentDate = new Date(orderedQuantity);
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);

            // Create a Document to represent the order
            Document orderDocument = new Document("uniqueID", uniqueID)
                    .append("productName", productName)
                    .append("orderedQuantity", orderedQuantity)
                    .append("orderDate", orderDate);

            // Insert the document into the MongoDB collection
            ordersCollection.insertOne(orderDocument);
        } catch (Exception e) {
            System.out.println("Error adding order to MongoDB: " + e.getMessage());
        }
    }

    void checkRecentOrder(String uniqueID) {
        try (BufferedReader ordersReader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            boolean foundOrder = false;

            System.out.println("Recent order:");

            while ((line = ordersReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].equals(uniqueID)) {
                    System.out.println(line);
                    foundOrder = true;
                    break; // Assuming we only need the most recent order
                }
            }

            if (!foundOrder) {
                System.out.println("No recent order found for the provided unique ID.");
            }
        } catch (IOException e) {
            System.out.println("Error reading orders file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error checking recent order: " + e.getMessage());
        }
    }

    void checkPreviousOrders(String uniqueID) {
        try (BufferedReader ordersReader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            boolean foundOrder = false;

            System.out.println("Previous orders:");

            while ((line = ordersReader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4 && parts[0].equals(uniqueID)) {
                    System.out.println(line);
                    foundOrder = true;
                }
            }

            if (!foundOrder) {
                System.out.println("No previous orders found for the provided unique ID.");
            }
        } catch (IOException e) {
            System.out.println("Error reading orders file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error checking previous orders: " + e.getMessage());
        }
    }

    // Add a method to close the MongoDB connection
    public void closeMongoDBConnection() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing MongoDB connection: " + e.getMessage());
        }
    }
}