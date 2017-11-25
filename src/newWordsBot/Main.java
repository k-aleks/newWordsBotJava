package newWordsBot;
import com.mongodb.MongoClientURI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;


import java.awt.*;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws TelegramApiRequestException, InterruptedException {

        Bot bot = Bot.startNew(Config.TelegramBotName, Config.TelegramToken, Collections.emptyList());

        Thread.sleep(-1);
    }
}


