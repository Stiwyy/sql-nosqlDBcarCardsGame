package ch.bbw.cardgame;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CardNoSQLConnector {
    public List<Car> getCarsFromDB() {
        List<Car> result = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder().applyConnectionString(new ConnectionString("mongodb://root:1234@localhost/carCards")).build()
        )) {
            MongoDatabase database = mongoClient.getDatabase("carCards");
            try {
                MongoCollection<Document> carDocs = database.getCollection("car");
                FindIterable<Document> iterDoc = carDocs.find();
                Iterator<Document> it = iterDoc.iterator();
                while(it.hasNext()){
                    Document doc = it.next();
                    Car c = new Car();
                    try{
                        c.setPrize(doc.getDouble("prize"));
                    } catch (Exception e){
                        c.setPrize(doc.getInteger("prize"));
                    }
                    c.setModel(doc.getString("model"));
                    c.setTradeName(doc.getString("tradeName"));
                    c.setImageUrl(doc.getString("imageUrl"));
                    result.add(c);
                }
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
            }
        }
        System.out.println("Found " + result.size());
        return result;
    }
}
