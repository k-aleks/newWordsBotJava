package newWordsBot;
import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;
import newWordsBot.dictionary.MacmillanDictionary;
import newWordsBot.handlers.BackgroundQuizHandler;
import newWordsBot.handlers.BackgroundQuizHandlerMessanger;
import newWordsBot.handlers.HelpHandler;
import newWordsBot.handlers.HelpHandlerMessanger;
import newWordsBot.handlers.IHandler;
import newWordsBot.handlers.IPendingQuizRequests;
import newWordsBot.handlers.NewWordsHandler;
import newWordsBot.handlers.NewWordsHandlerMessanger;
import newWordsBot.handlers.PendingQuizRequests;
import newWordsBot.methodology.ILearningMethodology;
import newWordsBot.methodology.IRandomWordDefinitionSelector;
import newWordsBot.methodology.ITimeProvider;
import newWordsBot.methodology.LearningMethodology;
import newWordsBot.methodology.RandomWordDefinitionSelector;
import newWordsBot.methodology.TimeProvider;
import newWordsBot.methodology.TimeProviderForTests;
import newWordsBot.storage.MongoClientFactory;
import newWordsBot.storage.StorageClient;
import newWordsBot.storage.UsersStorage;
import newWordsBot.storage.WordsStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;


import java.io.IOException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws TelegramApiRequestException, InterruptedException, IOException {

        Config config = Config.readFromFile();

        MongoClient mongoClient = MongoClientFactory.create(config.getMongoDbConnectionString());

        StorageClient storageClient = new StorageClient(mongoClient, config.getDatabaseName(), config.getUsersCollection(), config.getWordsForUserCollectionPrefix());

        UsersStorage usersStorage = new UsersStorage(storageClient, 30 * 60 * 1000);

        WordsStorage wordsStorage = new WordsStorage(storageClient);

        MacmillanDictionary dictionary = new MacmillanDictionary();

        ITimeProvider timeProvider = config.isTestMode() ? new TimeProviderForTests() : new TimeProvider();

        ILearningMethodology learningMethodology = new LearningMethodology(timeProvider);

        IPendingQuizRequests pendingQuizRequests = new PendingQuizRequests();

        IRandomWordDefinitionSelector randomDefinitionsSelector = new RandomWordDefinitionSelector(dictionary);

        ImmutableList<IHandler> handlers = ImmutableList.of(
                new NewWordsHandler(usersStorage, wordsStorage, dictionary, new NewWordsHandlerMessanger(), learningMethodology),
                new BackgroundQuizHandler(usersStorage, wordsStorage, pendingQuizRequests, randomDefinitionsSelector, learningMethodology, new BackgroundQuizHandlerMessanger()),
                new HelpHandler(new HelpHandlerMessanger(), usersStorage)
        );

        Bot bot = Bot.startNew(config.getTelegramBotName(), config.getTelegramToken(), handlers);
    }
}


