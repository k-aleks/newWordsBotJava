package newWordsBot.storage;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import newWordsBot.User;
import newWordsBot.Word;
import org.apache.logging.log4j.LogManager;
import com.mongodb.MongoClient;
import org.apache.logging.log4j.Logger;
import org.bson.BsonDocument;
import org.bson.BsonString;
import static com.mongodb.client.model.Filters.*;


import java.util.ArrayList;
import java.util.Date;

//TODO: refactor according best practices for working with mongodb via java driver
public class StorageClient implements IStorageClient
{
    private Logger logger = LogManager.getLogger(StorageClient.class);
    private MongoClient mongoClient;
    private String databaseName;
    private String usersCollectionName;
    private String wordsForUserCollectionPrefix;

    public StorageClient(MongoClient mongoClient, String databaseName, String usersCollectionName, String wordsForUserCollectionPrefix)
    {
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
        this.usersCollectionName = usersCollectionName;
        this.wordsForUserCollectionPrefix = wordsForUserCollectionPrefix;
    }

    public ArrayList<User> GetUsers()
    {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<User> collection = database.getCollection(usersCollectionName, User.class);
        ArrayList<User> users = collection.find(new BsonDocument()).into(new ArrayList<User>());
        logger.debug("Found %s users in database", users.size());
        return users;
    }

    public void InsertUser(User user)
    {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<User> collection = database.getCollection(usersCollectionName, User.class);
        collection.insertOne(user);
        logger.info("Inserted new user into database: %s", user.toString());
    }

    public void AddOrUpdateWord(User user, Word word)
    {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Word> collection = database.getCollection(GetWordsCollectionName(user), Word.class);
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.upsert(true);

        BsonDocument searchCriteria = new BsonDocument("_id", new BsonString(word.getWord()));
        collection.replaceOne(searchCriteria, word, updateOptions);
    }

    public Word FindWordWithNextRepetitionLessThenNow(User user)
    {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Word> collection = database.getCollection(GetWordsCollectionName(user), Word.class);
        Word res = collection.find(lt("NextRepetition", new Date().getTime())).first();
        return res;
    }

    private String GetWordsCollectionName(User user)
    {
        return wordsForUserCollectionPrefix + user.getUsername();
    }
}
