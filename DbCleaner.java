import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

public class DbCleaner {
    public static void main(String[] args) {
        String uri = "mongodb+srv://chethiyasankalpa456_db_user:6Fc8re0tinSU4V8a@cluster0.2ytw9tz.mongodb.net/?appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("restaurant_db"); 
            MongoCollection<Document> collection = database.getCollection("menuItems");
            
            // Delete "Chicken Wings"
            collection.deleteOne(eq("name", "Chicken Wings"));
            System.out.println("Deleted Chicken Wings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
