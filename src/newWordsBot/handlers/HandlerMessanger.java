package newWordsBot.handlers;

import newWordsBot.User;
import org.telegram.telegrambots.api.methods.send.SendMessage;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

abstract class HandlerMessanger {
    private ConcurrentLinkedQueue<SendMessage> outbox = new ConcurrentLinkedQueue<>();

    public void sendError(User user) {
        SendMessage message = new SendMessage(user.getChatId(), "Oops, some error occurred");
        putToOutbox(message);
    }

    List<SendMessage> getOutputMessages() {
        return new ArrayList<>(outbox);
    }

    void putToOutbox(SendMessage message) {
        outbox.add(message);
    }
}
