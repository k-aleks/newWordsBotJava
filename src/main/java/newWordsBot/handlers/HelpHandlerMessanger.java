package newWordsBot.handlers;

import newWordsBot.User;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;

public class HelpHandlerMessanger extends HandlerMessanger {

    public void sendHelpMessage(User user) {
        String usage = "*Usage:*\n" +
                "/add <word or expression> — to add a new word or expression\n";
        SendMessage message = new SendMessage(user.getChatId(), usage).setParseMode(ParseMode.MARKDOWN);
        putToOutbox(new OutputMessage(user, message));
    }
}
