package newWordsBot.handlers;

import newWordsBot.User;
import newWordsBot.storage.IUsersStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.objects.Update;


import java.util.List;

public class HelpHandler implements IHandler {

    private Logger logger = LogManager.getLogger(HelpHandler.class);

    HelpHandlerMessanger messanger;
    private IUsersStorage usersStorage;

    public HelpHandler(HelpHandlerMessanger messanger, IUsersStorage usersStorage) {
        this.messanger = messanger;
        this.usersStorage = usersStorage;
    }

    public void Start() {
    }

    @Override
    public boolean tryHandleInputMessage(Update update) {
        User user = null;
        try {
            user = usersStorage.GetOrRegisterUser(update.getMessage().getChat().getUserName(), update.getMessage().getChat().getId());
            messanger.sendHelpMessage(user);
        } catch (Exception e) {
            logger.error(e);
            if (user != null)
                messanger.sendError(user);
            return false;
        }
        return true;
    }

    @Override
    public List<OutputMessage> getOutputMessages() {
        return messanger.getOutputMessages();
    }
}

