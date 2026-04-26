import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

public class DbSwapper {
    public static void main(String[] args) {
        String uri = "mongodb+srv://chethiyasankalpa456_db_user:6Fc8re0tinSU4V8a@cluster0.2ytw9tz.mongodb.net/?appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("restaurant_db"); 
            MongoCollection<Document> collection = database.getCollection("menuItems");
            
            // Just rename "Crispy Spring Rolls" to "ZZZ" and "Grilled Salmon" to "AAA"
            collection.updateOne(eq("name", "Crispy Spring Rolls"), new Document("$set", new Document("name", "ZZZ")));
            collection.updateOne(eq("name", "Grilled Salmon"), new Document("$set", new Document("name", "AAA")));
            System.out.println("Swapped names");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
