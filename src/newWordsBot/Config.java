package newWordsBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    private String mongoDbConnectionString;
    private String databaseName;
    private String usersCollection;
    private boolean testMode;
    private String telegramBotName;
    private String telegramToken;
    private String wordsForUserCollectionPrefix;

    public static Config readFromFile() throws IOException {

        List<String> allLines = Files.readAllLines(Paths.get("config"));
        Map<String, String> map = allLines.stream()
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.toMap(line -> line.split("=", 2)[0].trim(),
                                          line -> line.split("=", 2)[1].trim()));

        boolean testMode = Boolean.parseBoolean(map.get("TestMode"));
        String telegramToken = map.get("TelegramToken");
        String telegramBotName = map.get("TelegramBotName");
        String mongoDbConnectionString = map.get("MongoDbConnectionString");
        String databaseName = map.get("DatabaseName");
        String usersCollection = map.get("UsersCollection");
        String wordsForUserCollectionPrefix = map.get("WordsForUserCollectionPrefix");

        return new Config(mongoDbConnectionString, databaseName, usersCollection, testMode, telegramBotName, telegramToken, wordsForUserCollectionPrefix);
    }

    public Config(String mongoDbConnectionString, String databaseName, String usersCollection, boolean testMode, String telegramBotName, String telegramToken, String wordsForUserCollectionPrefix) {
        this.mongoDbConnectionString = mongoDbConnectionString;
        this.databaseName = databaseName;
        this.usersCollection = usersCollection;
        this.testMode = testMode;
        this.telegramBotName = telegramBotName;
        this.telegramToken = telegramToken;
        this.wordsForUserCollectionPrefix = wordsForUserCollectionPrefix;
    }

    public String getMongoDbConnectionString() {
        return mongoDbConnectionString;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsersCollection() {
        return usersCollection;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public String getTelegramBotName() {
        return telegramBotName;
    }

    public String getTelegramToken() {
        return telegramToken;
    }

    public String getWordsForUserCollectionPrefix() {
        return wordsForUserCollectionPrefix;
    }
}
