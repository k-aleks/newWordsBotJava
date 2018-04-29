package newWordsBot.storage;

import newWordsBot.User;
import newWordsBot.Word;


import java.util.ArrayList;

public interface IStorageClient {
    ArrayList<User> GetUsers();

    void InsertUser(User user);

    void AddOrUpdateWord(User user, Word word);

    Word FindWordWithNextRepetitionLessThenNow(User user);
}



