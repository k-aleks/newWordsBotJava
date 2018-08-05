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
        ArrayList<InlineKeyboardButton> buttons = new ArrayList<>();
        for (int i = 0; i < definitions.size(); i++)
        {
            InlineKeyboardButton button = new InlineKeyboardButton(String.format("%s", i+1))
                                                .setCallbackData(String.format("/response %s %s", i, word));
            buttons.add(button);
        }
        keyboardButtons.add(buttons);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup()
                                                .setKeyboard(keyboardButtons);
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("Choose correct definition for the word *%s*\r\n\r\n", word));
        for (int i = 0; i < definitions.size(); i++)
        {
            messageBuilder.append(String.format("*%s)* %s\r\n\r\n", i+1, definitions.get(i)));
        }
        SendMessage message = new SendMessage(user.getChatId(), messageBuilder.toString())
                .setParseMode(ParseMode.MARKDOWN)
                .setReplyMarkup(keyboard);
        putToOutbox(new OutputMessage(user, message));
    }
}
