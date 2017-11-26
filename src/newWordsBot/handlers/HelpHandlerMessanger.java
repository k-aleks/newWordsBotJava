package newWordsBot.handlers;

import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;

public class HelpHandlerMessanger extends HandlerMessanger {

    public void sendHelpMessage(long chatId) {
        String usage = "*Usage:*\n" +
                "/add <word or expression> — to add a new word or expression\n";
        putToOutbox(new SendMessage(chatId, usage).setParseMode(ParseMode.MARKDOWN));
    }

}
