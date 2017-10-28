package newWordsBot;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;


import java.util.Date;
import java.util.UUID;

public class User {
    @BsonId
    private UUID id;
    @BsonProperty("Username")
    private String username;
    @BsonProperty("ChatId")
    private long chatId;
    @BsonProperty("RegisteredDate")
    private Date registeredDate;

    public User() {
    }

    public User(UUID id, String username, long chatId, Date registeredDate) {
        this.id = id;
        this.username = username;
        this.chatId = chatId;
        this.registeredDate = registeredDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (chatId != user.chatId) return false;
        if (!id.equals(user.id)) return false;
        if (!username.equals(user.username)) return false;
        return registeredDate.equals(user.registeredDate);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + (int) (chatId ^ (chatId >>> 32));
        result = 31 * result + registeredDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", chatId=" + chatId +
                ", registeredDate=" + registeredDate +
                '}';
    }
}
