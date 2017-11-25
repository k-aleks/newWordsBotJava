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

class PendingQuizRequests implements IPendingQuizRequests {
    Hashtable<User, PendingQuizRequest> storage = new Hashtable<>();

    public boolean ContainsRequest(User user) {
        return storage.containsKey(user);
    }

    public PendingQuizRequest TryGet(User user) {
        return storage.get(user);
    }

    public void Add(User user, Word word, int rightVariantIndex) {
        storage.put(user, new PendingQuizRequest(word, rightVariantIndex));
    }

    public void Remove(User user) {
        storage.remove(user);
    }
}
