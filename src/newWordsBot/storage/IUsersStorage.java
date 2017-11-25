package newWordsBot.storage;

import newWordsBot.User;
import java.util.ArrayList;

public interface IUsersStorage {
    public User GetOrRegisterUser(String username, long chatId);
    public ArrayList<User> GetAllUsers();
}