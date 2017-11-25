package newWordsBot.handlers;

import com.google.common.collect.ImmutableList;
import newWordsBot.User;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BackgroundQuizHandlerMessanger
{
    private ConcurrentLinkedQueue<SendMessage> outbox = new ConcurrentLinkedQueue<>();

    public void sendRightResponse(User user)
    {
        putToOutbox(new SendMessage(user.getChatId(), "Correct!"));
    }

    public void sendWrongReponse(User user)
    {
        putToOutbox(new SendMessage(user.getChatId(), "Incorrect definition :("));
    }

    public void sendError(User user)
    {
        putToOutbox(new SendMessage(user.getChatId(), "Oops, some error occurred"));
    }

    public void askUser(User user, String word, List<String> definitions)
    {
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        for (int i = 0; i < definitions.size(); i++)
        {
            InlineKeyboardButton button = new InlineKeyboardButton(definitions.get(i))
                                                .setCallbackData(String.format("/response %s %s", i, word));
            keyboardButtons.add(ImmutableList.of(button));
        }
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup()
                                                .setKeyboard(keyboardButtons);
        SendMessage message = new SendMessage(user.getChatId(), String.format("Choose correct definition for the word *%s*", word))
                .setParseMode(ParseMode.MARKDOWN)
                .setReplyMarkup(keyboard);
        putToOutbox(message);
    }

    public List<SendMessage> getOutputMessages() {
        return new ArrayList<>(outbox);
    }

    private void putToOutbox(SendMessage message) {
        outbox.add(message);
    }

}
