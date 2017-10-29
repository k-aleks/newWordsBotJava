package newWordsBot.storage;

import newWordsBot.User;
import java.util.ArrayList;

public interface IUsersStorage {
    User GetOrRegisterUser(String username, long chatId);
    ArrayList<User> GetAllUsers();
}