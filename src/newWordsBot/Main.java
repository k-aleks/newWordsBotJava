package newWordsBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.Date;
import java.util.UUID;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        for (int i=0; i< 10;i++)
            logger.info("test logging " + i);
    }
}


