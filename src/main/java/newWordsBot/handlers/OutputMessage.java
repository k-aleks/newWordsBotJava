package newWordsBot.handlers;

import newWordsBot.User;
import org.telegram.telegrambots.api.methods.send.SendMessage;

public class OutputMessage {
   private User user;
   private SendMessage message;

    public OutputMessage(User user, SendMessage message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public SendMessage getMessage() {
        return message;
    }
}
