package com.sergeev.studapp.mongoOLD.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.model.User;
import org.bson.Document;

public class MongoDBExample {

    public static void main(String[] args) {
        User user = new User();
        user.setFirstName("Kirill");
        user.setLastName("Sergeev");
        user.setType(User.Role.TEACHER);

        MongoDatabase db = MongoDaoFactory.getConnection();
        MongoCollection<Document> collection = db.getCollection("test");
        Document doc = new Document("firstName", user.getFirstName())
               .append("lastName", user.getLastName())
                .append("role", user.getType().getId());
        collection.insertOne(doc);
    }
}
