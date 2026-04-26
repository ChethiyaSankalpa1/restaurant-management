package com.restaurant.scratch;
import com.mongodb.client.*;
import org.bson.Document;

public class DbCheck {
    public static void main(String[] args) {
        String uri = "mongodb+srv://chethiyasankalpa456_db_user:6Fc8re0tinSU4V8a@cluster0.2ytw9tz.mongodb.net/?appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("restaurant_db");
            System.out.println("Products: " + database.getCollection("inventory").countDocuments());
            System.out.println("Categories: " + database.getCollection("categories").countDocuments());
            System.out.println("MenuItems: " + database.getCollection("menuItems").countDocuments());
            System.out.println("Users: " + database.getCollection("users").countDocuments());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
