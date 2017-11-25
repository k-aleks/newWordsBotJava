package newWordsBot.handlers;

import newWordsBot.User;
import newWordsBot.Word;
import newWordsBot.methodology.ILearningMethodology;
import newWordsBot.methodology.IRandomWordDefinitionSelector;
import newWordsBot.storage.IUsersStorage;
import newWordsBot.storage.IWordsStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

public class BackgroundQuizHandler implements IHandler {

    private Logger logger = LogManager.getLogger(BackgroundQuizHandler.class);
    private IUsersStorage usersStorage;
    private IWordsStorage wordsStorage;
    private IPendingQuizRequests pendingQuizRequests;
    private IRandomWordDefinitionSelector randomWordDefinitionDefenitionSelector;
    private ILearningMethodology learningMethodology;
    private Random rnd = new Random();
    private BackgroundQuizHandlerMessanger messanger;

    public BackgroundQuizHandler(IUsersStorage usersStorage,
                                 IWordsStorage wordsStorage,
                                 IPendingQuizRequests pendingQuizRequests,
                                 IRandomWordDefinitionSelector randomWordDefinitionDefenitionSelector,
                                 ILearningMethodology learningMethodology,
                                 BackgroundQuizHandlerMessanger messanger) {
        this.usersStorage = usersStorage;
        this.wordsStorage = wordsStorage;
        this.pendingQuizRequests = pendingQuizRequests;
        this.randomWordDefinitionDefenitionSelector = randomWordDefinitionDefenitionSelector;
        this.learningMethodology = learningMethodology;
        this.messanger = messanger;
    }

    @Override
    public void Start() {
        Executors.newSingleThreadExecutor().submit(this::Routine);
    }

    @Override
    public boolean tryHandleInputMessage(Update update) {
        if (!IsOurCallback(update))
            return false;
        CallbackQuery callback = update.getCallbackQuery();
        User user = null;
        try {
            user = usersStorage.GetOrRegisterUser(callback.getMessage().getChat().getUserName(), callback.getMessage().getChat().getId());
            logger.debug(callback.getData());
            QuizCallbackData callbackData = QuizCallbackData.deserialize(callback.getData());
            PendingQuizRequest pendingQuizRequest;
            if ((pendingQuizRequest = pendingQuizRequests.TryGet(user)) != null) {
                if (pendingQuizRequest.getWord().getWord().equals(callbackData.getWord())) {
                    Word transformatedWordToStore;
                    if (callbackData.getVariantIndex() == pendingQuizRequest.getRightVariantIndex()) {
                        messanger.sendRightResponse(user);
                        transformatedWordToStore = learningMethodology.OnRightResponse(pendingQuizRequest.getWord());
                    } else {
                        messanger.sendWrongReponse(user);
                        transformatedWordToStore = learningMethodology.OnWrongResponse(pendingQuizRequest.getWord());
                    }
                    wordsStorage.AddOrUpdate(user, transformatedWordToStore);
                    pendingQuizRequests.Remove(user);
                } else {
                    //just ignore
                    logger.warn(String.format("PendingQuizRequest differs from a word sent by user %s", user.getUsername()));
                }

            } else {
                //just ignore
                logger.warn(String.format("There is no PendingQuizRequest for user %s and word '%s'", user.getUsername(), callbackData.getWord()));
            }
            return true;
        } catch (Exception e) {
            logger.error(e);
            if (user != null) {
                messanger.sendError(user);
            }
        }
        return false;
    }

    @Override
    public List<SendMessage> getOutputMessages() {
        return messanger.getOutputMessages();
    }

    private boolean IsOurCallback(Update update) {
        try {
            if (!update.hasCallbackQuery())
                return false;
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if (callbackQuery.getData().startsWith(QuizCallbackData.PREFIX))
                return true;
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    private void Routine() {
        while (true) {
            try {
                RiddleRound();
            } catch (Exception e) {
                logger.error(e);
            }
            try {
                Thread.sleep(1000); //TODO: get rid of
            } catch (InterruptedException e) {
            }
        }
    }

    private void RiddleRound() {
        ArrayList<User> users = usersStorage.GetAllUsers();
        for(User user: users)
        {
            if (!pendingQuizRequests.ContainsRequest(user)) {
                Word word;
                if ((word = wordsStorage.GetNextReadyToRepeat(user)) != null) {
                    List<String> definitions = GetWrongDefinitions(word);
                    AddWordDefinitionsAtRandomPosition(definitions, word);
                    int rightVariantIndex = definitions.indexOf(word.getDefinition());
                    messanger.askUser(user, word.getWord(), definitions);
                    pendingQuizRequests.Add(user, word, rightVariantIndex);
                }
            }
        }
    }

    private void AddWordDefinitionsAtRandomPosition(List<String> definitions, Word word) {
        int rndIndex = rnd.nextInt(definitions.size());
        String tmp = definitions.get(rndIndex);
        definitions.set(rndIndex, word.getDefinition());
        definitions.add(tmp);
    }

    private List<String> GetWrongDefinitions(Word word) {
        List<String> defs = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            String w = randomWordDefinitionDefenitionSelector.Select(word.getForm());
            defs.add(w);
        }
        return defs;
    }
}

