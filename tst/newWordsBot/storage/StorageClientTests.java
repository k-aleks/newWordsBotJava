package newWordsBot.storage;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import newWordsBot.Config;
import newWordsBot.User;
import newWordsBot.dotNetStyle.DateTime;
import newWordsBot.dotNetStyle.Guid;
import org.bson.BsonDocument;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


import java.io.Console;
import java.util.ArrayList;
import java.util.Date;

class StorageClientTests {

    //TODO: when make take test to work fix UTC issue
    @Test
    void GetUsers_should_return_all_users_after_InsertUser_call() throws Exception {

        User u1 = new User(Guid.NewGuid(), "user1", 123, DateTime.UtcNow());
        User u2 = new User(Guid.NewGuid(), "user2", 123, DateTime.UtcNow());
        User u3 = new User(Guid.NewGuid(), "user3", 123, DateTime.UtcNow());

        if (!Config.DatabaseName.endsWith("-test"))
            throw new Exception("A-a-a-a, don't clear working database");

        MongoClient mongoClient = MongoClientFactory.create(Config.MongoDbConnectionString);

        mongoClient
                .getDatabase(Config.DatabaseName)
                .getCollection(Config.UsersCollection, User.class)
                .deleteMany(new BsonDocument());

        StorageClient storageClient = new StorageClient(mongoClient, Config.DatabaseName, Config.UsersCollection, Config.WordsForUserCollectionPrefix);

        storageClient.InsertUser(u1);
        ArrayList<User> users = storageClient.GetUsers();
        assertEquals(u1, users.get(0));

        storageClient.InsertUser(u2);
        users = storageClient.GetUsers();
        assertEquals(u1, users.get(0));
        assertEquals(u2, users.get(1));

        storageClient.InsertUser(u3);
        users = storageClient.GetUsers();
        assertEquals(u1, users.get(0));
        assertEquals(u2, users.get(1));
        assertEquals(u3, users.get(2));
    }

    @Test
    void insertUser() throws InterruptedException {
        Date d = new Date();
        Thread.sleep(1000);
        System.out.println(d.getHours());
        System.out.println(new Date(new Date().getTime()).toString());
    }

    @Test
    void addOrUpdateWord() {
    }

    @Test
    void findWordWithNextRepetitionLessThenNow() {
    }

}