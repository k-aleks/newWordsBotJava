package newWordsBot.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;


import java.util.List;

public class HelpHandler implements IHandler {

    private Logger logger = LogManager.getLogger(HelpHandler.class);

    HelpHandlerMessanger messanger = new HelpHandlerMessanger();

    public void Start() {
    }

    @Override
    public boolean tryHandleInputMessage(Update update) {
        try {
            messanger.sendHelpMessage(update.getMessage().getChatId());
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    @Override
    public List<SendMessage> getOutputMessages() {
        return messanger.getOutputMessages();
    }
}

