package newWordsBot.handlers;

import newWordsBot.User;
import newWordsBot.Word;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class NewWordsHandlerMessanger extends HandlerMessanger {

    void SendNewWordConfirmation(User user, Word word) {
        String messageText = String.format("Added new word *%s* with definition _%s_", word.getWord(), word.getDefinition());
        SendMessage message = new SendMessage(user.getChatId(), messageText)
                .setParseMode(ParseMode.MARKDOWN);
        putToOutbox(message);
    }

    void SendSelectDefinitionRequest(User user, String word, List<String> definitions) {
        int defenitionsCount = Math.min(3, definitions.size());
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        for (int i = 0; i < defenitionsCount; i++) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(definitions.get(i))
                    .setCallbackData("/add " + i);
            keyboardButtons.add(Collections.singletonList(inlineKeyboardButton));
        }
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup()
                .setKeyboard(keyboardButtons);
        SendMessage message = new SendMessage(user.getChatId(), String.format("Choose definition for the word \"%s\"", word))
                .setReplyMarkup(keyboard);
        putToOutbox(message);
    }
}
