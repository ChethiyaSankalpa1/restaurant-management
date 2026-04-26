import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class DbInspector {
    public static void main(String[] args) {
        String uri = "mongodb+srv://chethiyasankalpa456_db_user:6Fc8re0tinSU4V8a@cluster0.2ytw9tz.mongodb.net/?appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("restaurant_db"); 
            
            System.out.println("--- MENU ITEMS ---");
            MongoCollection<Document> collection = database.getCollection("menuItems");
            List<Document> items = collection.find().into(new ArrayList<>());
            for (Document item : items) {
                System.out.println("ID: " + item.get("_id") + ", Name: " + item.get("name") + ", Price: " + item.get("price") + ", Category: " + item.get("categoryId") + ", Available: " + item.get("available"));
            }
            
            System.out.println("--- CATEGORIES ---");
            System.out.println("--- CUSTOMERS ---");
            collection = database.getCollection("customers");
            List<Document> customers = collection.find().into(new ArrayList<>());
            for (Document c : customers) {
                System.out.println("ID: " + c.get("_id") + ", Name: " + c.get("name") + ", Phone: " + c.get("phone"));
            }
            
            System.out.println("--- TABLES ---");
            collection = database.getCollection("tables");
            List<Document> tables = collection.find().into(new ArrayList<>());
            for (Document t : tables) {
                System.out.println("ID: " + t.get("_id") + ", Num: " + t.get("tableNumber") + ", Cap: " + t.get("capacity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
