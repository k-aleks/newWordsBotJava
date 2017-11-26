package newWordsBot.handlers;

import newWordsBot.User;
import newWordsBot.Word;
import java.util.Hashtable;
import java.util.Optional;

public interface IPendingQuizRequests {
    boolean ContainsRequest(User user);

    PendingQuizRequest TryGet(User user);

    void Add(User user, Word word, int rightVariantIndex);

    void Remove(User user);
}

