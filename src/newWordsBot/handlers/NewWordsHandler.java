package newWordsBot.handlers;


import newWordsBot.User;
import newWordsBot.Word;
import newWordsBot.dictionary.DictionaryItem;
import newWordsBot.dictionary.IWordsDictionary;
import newWordsBot.methodology.ILearningMethodology;
import newWordsBot.storage.IUsersStorage;
import newWordsBot.storage.IWordsStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;


import java.util.HashMap;
import java.util.List;

public class NewWordsHandler implements IHandler {

    private Logger logger = LogManager.getLogger(NewWordsHandler.class);
    private HashMap<String, DictionaryItem> pendingDefinitions = new HashMap<String, DictionaryItem>();
    private IUsersStorage usersStorage;
    private IWordsStorage wordsStorage;
    private IWordsDictionary dictionary;
    private ILearningMethodology learningMethodology;
    private NewWordsHandlerMessanger messanger;

    public NewWordsHandler(IUsersStorage usersStorage, IWordsStorage wordsStorage, IWordsDictionary dictionary, NewWordsHandlerMessanger messanger, ILearningMethodology learningMethodology) {
        this.usersStorage = usersStorage;
        this.wordsStorage = wordsStorage;
        this.dictionary = dictionary;
        this.messanger = messanger;
        this.learningMethodology = learningMethodology;
    }

    public void Start() {
    }

    public boolean tryHandleInputMessage(Update update) {
        if (update.hasMessage())
            return tryHandleMessage(update.getMessage());
        if (update.hasCallbackQuery())
            return TryHandleCallback(update.getCallbackQuery());
        return false;
    }

    @Override
    public List<OutputMessage> getOutputMessages() {
        return messanger.getOutputMessages();
    }

    boolean tryHandleMessage(Message message) {
        if (!IsOurRequest(message))
            return false;
        User user = null;
        try {
            logger.debug("New message: " + message.getText());
            user = usersStorage.GetOrRegisterUser(message.getChat().getUserName(), message.getChatId());
            String wordToLearn = message.getText().replace("/add", "").trim();
            DictionaryItem dictionaryItem = dictionary.find(wordToLearn);
            messanger.SendSelectDefinitionRequest(user, dictionaryItem.getWord(), dictionaryItem.getDefinitions());
            pendingDefinitions.put(user.getUsername(), dictionaryItem);
            return true;
        } catch (Exception e) {
            logger.error(e);
            if (user != null)
                messanger.sendError(user);
        }
        return false;
    }

    //TODO: don't handle callback for another word
    boolean TryHandleCallback(CallbackQuery callback) {
        if (!IsOurCallback(callback))
            return false;
        User user = null;
        try {
            user = usersStorage.GetOrRegisterUser(callback.getMessage().getChat().getUserName(), callback.getMessage().getChatId());

            String callbackData = callback.getData();

            logger.debug(String.format("New callback query from %s with callback data: %s", user.getUsername(), callbackData));

            if (!pendingDefinitions.containsKey(user.getUsername())) {
                logger.warn(String.format("No pending definitions. User %s, callback data: %s", user.getUsername(), callbackData));
                return false;
            }
            DictionaryItem dicItem = pendingDefinitions.get(user.getUsername());
            int definitionIndex = Integer.parseInt(callbackData.split(" ")[1]);

            Word word = learningMethodology.CreateNewWord(dicItem.getWord(), dicItem.getDefinitions().get(definitionIndex), dicItem.getPartOfSpeech());

            wordsStorage.AddOrUpdate(user, word);

            messanger.SendNewWordConfirmation(user, word);
            logger.info(String.format("Added new word '%s' with definition '%s'", word.getWord(), word.getDefinition()));

            pendingDefinitions.remove(user.getUsername());
            return true;
        } catch (Exception e) {
            logger.error(e);
            if (user != null)
                messanger.sendError(user);
        }
        return false;
    }

    private boolean IsOurRequest(Message message) {
        try {
            if (message.getText().startsWith("/add"))
                return true;
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    private boolean IsOurCallback(CallbackQuery callback) {
        try {
            if (callback.getData().startsWith("/add"))
                return true;
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }
}