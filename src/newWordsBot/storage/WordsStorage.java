package newWordsBot.storage;

import newWordsBot.User;
import newWordsBot.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WordsStorage implements IWordsStorage {
    private IStorageClient storageClient;
    private Logger logger = LogManager.getLogger(StorageClient.class);

    public WordsStorage(IStorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public void AddOrUpdate(User user, Word word) {
        storageClient.AddOrUpdateWord(user, word);
    }

    public Word GetNextReadyToRepeat(User user) {
        return storageClient.FindWordWithNextRepetitionLessThenNow(user);
    }
}
