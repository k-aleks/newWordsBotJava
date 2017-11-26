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
import java.util.concurrent.Executors;

public class Bot extends TelegramLongPollingBot {

    private Logger logger = LogManager.getLogger(Bot.class);

    private String botUserName;
    private String botToken;
    private List<IHandler> handlers;

    private Bot(String botUserName, String botToken, List<IHandler> handlers) {
        this.botUserName = botUserName;
        this.botToken = botToken;
        this.handlers = handlers;

        handlers.forEach(IHandler::Start);

        Executors.newSingleThreadExecutor().submit(this::OutputMessagesRoutine);
    }


    public static Bot startNew(String botUserName, String botToken, List<IHandler> handlers) throws TelegramApiRequestException {

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        Bot bot = new Bot(botUserName, botToken, handlers);
        botsApi.registerBot(bot);
        return bot;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            for (IHandler handler : handlers) {
                if (handler.tryHandleInputMessage(update))
                    return;
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        for (Update update : updates) {
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

    private void OutputMessagesRoutine() {
        while (true) {
            try {
                handlers.forEach(handler -> {
                    List<SendMessage> outputMessages = handler.getOutputMessages();
                    outputMessages.forEach(m -> {
                        try {
                            sendApiMethod(m);
                        } catch (TelegramApiException e) {
                            logger.error(e);
                        }
                    });
                });

            } catch (Exception e) {
                logger.error(e);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}
