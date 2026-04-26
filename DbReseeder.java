import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class DbReseeder {
    public static void main(String[] args) {
        String uri = "mongodb+srv://chethiyasankalpa456_db_user:6Fc8re0tinSU4V8a@cluster0.2ytw9tz.mongodb.net/?appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("restaurant_db"); 
            
            // Clear collections
            database.getCollection("menuItems").deleteMany(new Document());
            database.getCollection("categories").deleteMany(new Document());
            database.getCollection("customers").deleteMany(new Document());
            database.getCollection("tables").deleteMany(new Document());
            
            System.out.println("Cleared collections");
            
            // Seed 3 Categories
            MongoCollection<Document> catColl = database.getCollection("categories");
            List<Document> cats = new ArrayList<>();
            cats.add(new Document("name", "Starters").append("active", true).append("sortOrder", 1));
            cats.add(new Document("name", "Main").append("active", true).append("sortOrder", 2));
            cats.add(new Document("name", "Drinks").append("active", true).append("sortOrder", 3));
            catColl.insertMany(cats);
            
            String starterId = cats.get(0).getObjectId("_id").toHexString();
            
            // Seed 5 Menu Items
            MongoCollection<Document> itemColl = database.getCollection("menuItems");
            List<Document> items = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                items.add(new Document("name", "Item " + i)
                    .append("price", 10.0 * i)
                    .append("categoryId", starterId)
                    .append("available", true)
                    .append("description", "Test item " + i));
            }
            itemColl.insertMany(items);
            
            // Seed 1 Customer
            database.getCollection("customers").insertOne(new Document("name", "Test Customer").append("phone", "123456"));
            
            // Seed 1 Table
            database.getCollection("tables").insertOne(new Document("tableNumber", "T1").append("capacity", 4).append("status", "Available"));
            
            System.out.println("Seeding complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
