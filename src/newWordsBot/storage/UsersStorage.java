package newWordsBot.storage;

import newWordsBot.User;
import newWordsBot.dotNetStyle.DateTime;
import newWordsBot.dotNetStyle.Guid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersStorage implements IUsersStorage {
    private IStorageClient storageClient;
    private long cacheUpdatePeriodMilliseconds;
    private Logger logger = LogManager.getLogger(StorageClient.class);
    private final Hashtable<String, User> usersCache = new Hashtable<String, User>();

    public UsersStorage(IStorageClient storageClient, long cacheUpdatePeriodMilliseconds) {
        this.storageClient = storageClient;
        this.cacheUpdatePeriodMilliseconds = cacheUpdatePeriodMilliseconds;
        RunChacheUpdater();
    }

    public User GetOrRegisterUser(String username, long chatId) {
        synchronized (usersCache) {
            if (usersCache.containsKey(username) &&
                    usersCache.get(username).getUsername() == username &&
                    usersCache.get(username).getChatId() == chatId) {
                return usersCache.get(username);
            }
        }

        User user = new User(Guid.NewGuid(), username, chatId, DateTime.UtcNow());
        storageClient.InsertUser(user);
        AddOrUpdateUserToCache(user);
        return user;
    }

    public ArrayList<User> GetAllUsers() {
        synchronized (usersCache) {
            Collection<User> values = usersCache.values();
            return new ArrayList<>(values);
        }
    }

    private void RunChacheUpdater() {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                InternalUpdateCacheRoutine();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void InternalUpdateCacheRoutine() throws InterruptedException {
        while (true) {
            try {
                ArrayList<User> list = storageClient.GetUsers();
                if (list != null)
                    for (User user : list) {
                        AddOrUpdateUserToCache(user);
                    }
            } catch (Exception e) {
                logger.error(e);
            }
            Thread.sleep(cacheUpdatePeriodMilliseconds);
        }
    }

    private void AddOrUpdateUserToCache(User user) {
        synchronized (usersCache) {
            if (!usersCache.containsKey(user.getUsername())) {
                usersCache.put(user.getUsername(), user);
            } else {
                if (usersCache.get(user.getUsername()).getRegisteredDate().before(user.getRegisteredDate()))
                    usersCache.put(user.getUsername(), user);
            }
        }
    }
}
