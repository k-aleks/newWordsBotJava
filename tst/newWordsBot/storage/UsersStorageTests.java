package newWordsBot.storage;

import newWordsBot.User;
import newWordsBot.dotNetStyle.DateTime;
import newWordsBot.dotNetStyle.Guid;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;


import java.util.ArrayList;

class UsersStorageTests {

    @Test
    void GetOrRegisterUser_should_save_user_to_storage_if_there_is_no_user_with_such_username()
    {
        IStorageClient storageClient = Mockito.mock(IStorageClient.class);

        UsersStorage usersStorage = new UsersStorage(storageClient, 100);

        User user = usersStorage.GetOrRegisterUser("user1", 123);

        assertEquals("user1", user.getUsername());
        assertEquals(123, user.getChatId());
        verify(storageClient).InsertUser(user);
    }

    @Test
    void GetOrRegisterUser_should_not_save_user_to_storage_if_there_is_already_user_with_such_username()
    {
        IStorageClient storageClient = Mockito.mock(IStorageClient.class);

        UsersStorage usersStorage = new UsersStorage(storageClient, 100);

        User user = usersStorage.GetOrRegisterUser("user1", 123);

        reset(storageClient);

        user = usersStorage.GetOrRegisterUser("user1", 123);

        assertEquals("user1", user.getUsername());
        assertEquals(123, user.getChatId());
        verify(storageClient, never()).InsertUser(any());
    }

    @Test
    void InternalUpdateCacheRoutine_should_replace_user_if_new_RegisteredDate_is_newer() throws InterruptedException {
        User u1 = new User(Guid.NewGuid(), "user1", 123, DateTime.UtcNow());
        User u2 = new User(Guid.NewGuid(), "user1", 321, DateTime.UtcNowPlusSeconds(10));

        IStorageClient storageClient = Mockito.mock(IStorageClient.class);
        ArrayList<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(u1);
        when(storageClient.GetUsers()).thenReturn(listOfUsers);

        UsersStorage usersStorage = new UsersStorage(storageClient, 100);

        Thread.sleep(1000);
        ArrayList<User> users = usersStorage.GetAllUsers();
        assertEquals(u1, users.get(0));

        listOfUsers.remove(0);
        listOfUsers.add(u2);
        when(storageClient.GetUsers()).thenReturn(listOfUsers);
        Thread.sleep(500);
        users = usersStorage.GetAllUsers();
        assertEquals(u2, users.get(0));
    }

    @Test
    void InternalUpdateCacheRoutine_should_not_replace_user_if_new_RegisteredDate_is_older() throws InterruptedException {
        User u1 = new User(Guid.NewGuid(), "user1", 123, DateTime.UtcNow());
        User u2 = new User(Guid.NewGuid(), "user1", 321, DateTime.UtcNowPlusSeconds(-10));

        IStorageClient storageClient = Mockito.mock(IStorageClient.class);
        ArrayList<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(u1);
        when(storageClient.GetUsers()).thenReturn(listOfUsers);

        UsersStorage usersStorage = new UsersStorage(storageClient, 100);

        Thread.sleep(1000);
        ArrayList<User> users = usersStorage.GetAllUsers();
        assertEquals(u1, users.get(0));

        listOfUsers.remove(0);
        listOfUsers.add(u2);
        when(storageClient.GetUsers()).thenReturn(listOfUsers);
        Thread.sleep(500);
        users = usersStorage.GetAllUsers();
        assertEquals(u1, users.get(0));
    }
}