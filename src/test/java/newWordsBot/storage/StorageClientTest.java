package newWordsBot.storage;

import com.mongodb.MongoClient;
import newWordsBot.Config;
import newWordsBot.LearningStage;
import newWordsBot.PartOfSpeech;
import newWordsBot.User;
import newWordsBot.Word;
import newWordsBot.dotNetStyle.DateTime;
import newWordsBot.dotNetStyle.Guid;
import org.bson.BsonDocument;
import static org.junit.Assert.*;
import org.junit.Test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//TODO: move functional tests to separate package of mark them somehow
public class StorageClientTest {

    private String databaseName;
    private String mongoDbConnectionString;
    private String usersCollection;
    private String wordsForUserCollectionPrefix;

    public StorageClientTest() throws IOException {
        Config config = Config.readFromFile();
        databaseName = config.getDatabaseName();
        mongoDbConnectionString = config.getMongoDbConnectionString();
        usersCollection = config.getUsersCollection();
        wordsForUserCollectionPrefix = config.getWordsForUserCollectionPrefix();
    }

    //TODO: when make take test to work fix UTC issue
    @Test
    public void GetUsers_should_return_all_users_after_InsertUser_call() throws Exception {

        User u1 = new User(Guid.NewGuid(), "user1", 123, DateTime.UtcNow());
        User u2 = new User(Guid.NewGuid(), "user2", 123, DateTime.UtcNow());
        User u3 = new User(Guid.NewGuid(), "user3", 123, DateTime.UtcNow());

        if (!databaseName.endsWith("-test"))
            throw new Exception("A-a-a-a, don't clear working database");

        MongoClient mongoClient = MongoClientFactory.create(mongoDbConnectionString);

        mongoClient
                .getDatabase(databaseName)
                .getCollection(usersCollection, User.class)
                .deleteMany(new BsonDocument());

        StorageClient storageClient = CreateStorageClient();

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
    public void FindWordWithNextRepetitionLessThenNow_should_return_null_if_no_words() throws Exception {

        User user = new User(Guid.NewGuid(), "testUser", 123, DateTime.UtcNow());

        ClearWordsCollection(GetCollectionName(user));

        StorageClient storageClient = CreateStorageClient();

        Word word = storageClient.FindWordWithNextRepetitionLessThenNow(user);
        assertNull(word);
    }

    @Test
    public void FindWordWithNextRepetitionLessThenNow_should_return_null_if_for_stale_words() throws Exception {

        User user = new User(Guid.NewGuid(), "testUser", 123, DateTime.UtcNow());

        ClearWordsCollection(GetCollectionName(user));

        StorageClient storageClient = CreateStorageClient();

        storageClient.AddOrUpdateWord(user, CeateRandomWord(new Date(DateTime.UtcNow().getTime() + 60 * 1000)));

        Word word = storageClient.FindWordWithNextRepetitionLessThenNow(user);
        assertNull(word);
    }

    @Test
    public void FindWordWithNextRepetitionLessThenNow_should_return_word_if_fresh() throws Exception {

        User user = new User(Guid.NewGuid(), "testUser", 123, DateTime.UtcNow());

        ClearWordsCollection(GetCollectionName(user));

        StorageClient storageClient = CreateStorageClient();

        Word word = CeateRandomWord(new Date(DateTime.UtcNow().getTime() - 60 * 1000));
        storageClient.AddOrUpdateWord(user, word);

        Word foundWord = storageClient.FindWordWithNextRepetitionLessThenNow(user);
        assertEquals(word, foundWord);
    }

    @Test
    public void AddOrUpdateWord_should_update_word_if_exists() throws Exception {

        User user = new User(Guid.NewGuid(), "testUser", 123, DateTime.UtcNow());

        ClearWordsCollection(GetCollectionName(user));

        StorageClient storageClient = CreateStorageClient();

        Word word = CeateRandomWord(new Date(DateTime.UtcNow().getTime() - 60 * 1000));
        storageClient.AddOrUpdateWord(user, word);
        EnsureNumerOfElementsInCollection(GetCollectionName(user), 1);

        storageClient.AddOrUpdateWord(user, CloneWithNewDefinition(word, "def1"));
        EnsureNumerOfElementsInCollection(GetCollectionName(user), 1);

        Word res = FindOne(GetCollectionName(user));
        assertEquals("def1", res.getDefinition());
    }

    private Word CeateRandomWord(Date nextRepetition) {
        return new Word(Guid.NewGuid().toString(), Guid.NewGuid().toString(), PartOfSpeech.Noun, LearningStage.First_1m, nextRepetition, DateTime.UtcNow());
    }

    private Word CloneWithNewDefinition(Word w, String newDefinition) {
        return new Word(w.getWord(), newDefinition, w.getForm(), w.getStage(), w.getNextRepetition(), w.getAddedToDictionary());
    }

    private StorageClient CreateStorageClient() {
        MongoClient mongoClient = MongoClientFactory.create(mongoDbConnectionString);
        return new StorageClient(mongoClient, databaseName, usersCollection, wordsForUserCollectionPrefix);
    }

    private String GetCollectionName(User user) {
        return wordsForUserCollectionPrefix + user.getUsername();
    }

    private void ClearWordsCollection(String collectionName) throws Exception {
        if (!databaseName.endsWith("-test"))
            throw new Exception("A-a-a-a, don't clear working database");

        MongoClient mongoClient = MongoClientFactory.create(mongoDbConnectionString);

        mongoClient
                .getDatabase(databaseName)
                .getCollection(collectionName, Word.class)
                .deleteMany(new BsonDocument());
    }

    private void EnsureNumerOfElementsInCollection(String collectionName, int expectedNumberOfElements) {
        MongoClient mongoClient = MongoClientFactory.create(mongoDbConnectionString);

        long count = mongoClient
                .getDatabase(databaseName)
                .getCollection(collectionName, Word.class)
                .count();

        assertEquals(expectedNumberOfElements, count);
    }

    private Word FindOne(String collectionName) {
        MongoClient mongoClient = MongoClientFactory.create(mongoDbConnectionString);

        return mongoClient
                .getDatabase(databaseName)
                .getCollection(collectionName, Word.class)
                .find().first();
    }

}