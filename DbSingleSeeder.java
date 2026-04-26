import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class DbSingleSeeder {
    public static void main(String[] args) {
        String uri = "mongodb+srv://chethiyasankalpa456_db_user:6Fc8re0tinSU4V8a@cluster0.2ytw9tz.mongodb.net/?appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("restaurant_db"); 
            database.getCollection("menuItems").deleteMany(new Document());
            
            database.getCollection("menuItems").insertOne(new Document("name", "Single Item")
                .append("price", 10.0)
                .append("available", true));
            
            System.out.println("Seeded single item");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
