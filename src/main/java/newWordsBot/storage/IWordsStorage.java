package newWordsBot.storage;

import newWordsBot.User;
import newWordsBot.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface IWordsStorage
{
    void AddOrUpdate(User user, Word word);
    Word GetNextReadyToRepeat(User user);
}

