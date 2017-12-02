package newWordsBot.handlers;

import com.google.common.collect.ImmutableList;
import newWordsBot.User;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BackgroundQuizHandlerMessanger extends HandlerMessanger{

    void sendRightResponse(User user)
    {
        SendMessage message = new SendMessage(user.getChatId(), "Correct!");
        putToOutbox(new OutputMessage(user, message));
    }

    void sendWrongReponse(User user)
    {
        SendMessage message = new SendMessage(user.getChatId(), "Incorrect definition :(");
        putToOutbox(new OutputMessage(user, message));
    }

    void askUser(User user, String word, List<String> definitions)
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
        putToOutbox(new OutputMessage(user, message));
    }
}
