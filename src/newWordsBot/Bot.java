package newWordsBot;

import newWordsBot.handlers.IHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;


import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private Logger logger = LogManager.getLogger(Bot.class);

    private String botUserName;
    private String botToken;
    private List<IHandler> handlers;

    private Bot(String botUserName, String botToken, List<IHandler> handlers) {
        this.botUserName = botUserName;
        this.botToken = botToken;
        this.handlers = handlers;
    }

    public static Bot startNew(String botUserName, String botToken, List<IHandler> handlers) throws TelegramApiRequestException {

        Bot bot = new Bot(botUserName, botToken, handlers);
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(bot);
        return bot;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                sendApiMethod(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        for (Update update: updates) {
            onUpdateReceived(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onClosing() {
        logger.info("onClosing() method was called");
    }
}
