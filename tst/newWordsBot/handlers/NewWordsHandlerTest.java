package newWordsBot.handlers;

import com.google.common.collect.ImmutableList;
import newWordsBot.LearningStage;
import newWordsBot.PartOfSpeech;
import newWordsBot.User;
import newWordsBot.Word;
import newWordsBot.dictionary.DictionaryItem;
import newWordsBot.dictionary.IWordsDictionary;
import newWordsBot.dotNetStyle.DateTime;
import newWordsBot.dotNetStyle.Guid;
import newWordsBot.methodology.ILearningMethodology;
import newWordsBot.storage.IUsersStorage;
import newWordsBot.storage.IWordsStorage;
import newWordsBot.storage.UsersStorage;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;


import java.util.List;

public class NewWordsHandlerTest {

    @Test
    public void TryHandleRequest_should_return_false_for_nonAdd_request() {

        NewWordsHandler handler = new NewWordsHandler(MockUsersStorage(), MockWordsStorage(), MockWordsDictionary(), MockNewWordsHandlerMessanger(), MockLearningMethodology());

        Message message = mock(Message.class);
        when(message.getText()).thenReturn("/noadd");

        boolean result = handler.tryHandleMessage(message);
        assertFalse(result);
    }


    @Test
    public void TryHandleRequest_should_send_correct_request_to_user_and_return_true() {
        String wordToLearn = "word to learn";
        List<String> definitions = ImmutableList.of(
                                        "definition one",
                                        "definition two",
                                        "definition three");
        User user = new User(Guid.NewGuid(), "myUserName", 12345, DateTime.UtcNow());

        IUsersStorage usersStorage = MockUsersStorage();
        when(usersStorage.GetOrRegisterUser(any(String.class), any(long.class))).thenReturn(user);

        IWordsDictionary dictionary = MockWordsDictionary();
        when(dictionary.find(wordToLearn)).thenReturn(new DictionaryItem(wordToLearn, definitions, PartOfSpeech.Noun));

        NewWordsHandlerMessanger messanger = MockNewWordsHandlerMessanger();

        NewWordsHandler handler = new NewWordsHandler(usersStorage, MockWordsStorage(), dictionary, messanger, MockLearningMethodology());

        Message message = mock(Message.class);
        when(message.getText()).thenReturn("/add " + wordToLearn);
        when(message.getChat()).thenReturn(mock(Chat.class));

        boolean result = handler.tryHandleMessage(message);
        assertTrue(result);

        verify(messanger).SendSelectDefinitionRequest(user, wordToLearn, definitions);
    }

    @Test
    public void TryHandleCallback_should_return_false_if_no_pending_definitions() {

        User user = new User(Guid.NewGuid(), "myUserName", 12345, DateTime.UtcNow());
        IUsersStorage usersStorage = MockUsersStorage();
        when(usersStorage.GetOrRegisterUser(any(String.class), any(long.class))).thenReturn(user);

        NewWordsHandler handler = new NewWordsHandler(usersStorage, MockWordsStorage(), MockWordsDictionary(), MockNewWordsHandlerMessanger(), MockLearningMethodology());

        CallbackQuery callback = mock(CallbackQuery.class);
        when(callback.getData()).thenReturn("/add");
        Message message = mock(Message.class);
        when(message.getChat()).thenReturn(mock(Chat.class));
        when(callback.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(mock(Chat.class));

        boolean result = handler.TryHandleCallback(callback);

        assertFalse(result);
    }

    @Test
    public void TryHandleCallback_should_call_methodology_for_creating_new_word_and_save_word_to_storage_and_return_true() {
        String wordToLearn = "word to learn";
        List<String> definitions = ImmutableList.of(
                "definition one",
                "definition two",
                "definition three");
        User user = new User(Guid.NewGuid(), "myUserName", 12345, DateTime.UtcNow());
        Word word = new Word(wordToLearn, definitions.get(1), PartOfSpeech.Noun, LearningStage.First_1m, DateTime.MinValue(), DateTime.UtcNow());

        IUsersStorage usersStorage = MockUsersStorage();
        when(usersStorage.GetOrRegisterUser(any(String.class), any(long.class))).thenReturn(user);

        IWordsDictionary dictionary = MockWordsDictionary();
        when(dictionary.find(wordToLearn)).thenReturn(new DictionaryItem(wordToLearn, definitions, PartOfSpeech.Noun));

        NewWordsHandlerMessanger messanger = MockNewWordsHandlerMessanger();

        ILearningMethodology learningMethodology = MockLearningMethodology();
        when(learningMethodology.CreateNewWord(wordToLearn, definitions.get(1), PartOfSpeech.Noun)).thenReturn(word);

        IWordsStorage wordsStorage = MockWordsStorage();
        NewWordsHandler handler = new NewWordsHandler(usersStorage, wordsStorage, dictionary, messanger, learningMethodology);

        Message message = mock(Message.class);
        when(message.getText()).thenReturn("/add " + wordToLearn);
        when(message.getChat()).thenReturn(mock(Chat.class));

        boolean messageHandlingResult = handler.tryHandleMessage(message);
        assertTrue(messageHandlingResult);


        CallbackQuery callback = mock(CallbackQuery.class);
        when(callback.getData()).thenReturn("/add 1");
        when(callback.getMessage()).thenReturn(message);

        boolean result = handler.TryHandleCallback(callback);

        assertTrue(result);

        verify(learningMethodology).CreateNewWord(wordToLearn, definitions.get(1), PartOfSpeech.Noun);
        verify(wordsStorage).AddOrUpdate(user, word);
        verify(messanger).SendNewWordConfirmation(user, word);

    }

    @Test
    public void TryHandleCallback_should_not_do_nothing_on_second_callback_and_return_false() {
        String wordToLearn = "word to learn";
        List<String> definitions = ImmutableList.of(
                "definition one",
                "definition two",
                "definition three");
        User user = new User(Guid.NewGuid(), "myUserName", 12345, DateTime.UtcNow());
        Word word = new Word(wordToLearn, definitions.get(1), PartOfSpeech.Noun, LearningStage.First_1m, DateTime.MinValue(), DateTime.UtcNow());

        IUsersStorage usersStorage = MockUsersStorage();
        when(usersStorage.GetOrRegisterUser(any(String.class), any(long.class))).thenReturn(user);

        IWordsDictionary dictionary = MockWordsDictionary();
        when(dictionary.find(wordToLearn)).thenReturn(new DictionaryItem(wordToLearn, definitions, PartOfSpeech.Noun));

        NewWordsHandlerMessanger messanger = MockNewWordsHandlerMessanger();

        ILearningMethodology learningMethodology = MockLearningMethodology();
        when(learningMethodology.CreateNewWord(wordToLearn, definitions.get(1), PartOfSpeech.Noun)).thenReturn(word);

        IWordsStorage wordsStorage = MockWordsStorage();
        NewWordsHandler handler = new NewWordsHandler(usersStorage, wordsStorage, dictionary, messanger, learningMethodology);

        Message message = mock(Message.class);
        when(message.getText()).thenReturn("/add " + wordToLearn);
        when(message.getChat()).thenReturn(mock(Chat.class));

        boolean messageHandlingResult = handler.tryHandleMessage(message);
        assertTrue(messageHandlingResult);

        CallbackQuery callback = mock(CallbackQuery.class);
        when(callback.getData()).thenReturn("/add 1");
        when(callback.getMessage()).thenReturn(message);

        boolean result = handler.TryHandleCallback(callback);
        assertTrue(result);

        reset(learningMethodology);
        reset(wordsStorage);
        reset(messanger);

        result = handler.TryHandleCallback(callback);
        assertFalse(result);

        verify(learningMethodology, never()).CreateNewWord(any(), any(), any());
        verify(wordsStorage, never()).AddOrUpdate(any(), any());
        verify(messanger, never()).SendNewWordConfirmation(any(), any());
    }

    private static ILearningMethodology MockLearningMethodology() {
        return mock(ILearningMethodology.class);
    }

    private static NewWordsHandlerMessanger MockNewWordsHandlerMessanger() {
        return mock(NewWordsHandlerMessanger.class);
    }

    private static IUsersStorage MockUsersStorage() {
        return mock(IUsersStorage.class);
    }

    private static IWordsDictionary MockWordsDictionary() {
        return mock(IWordsDictionary.class);
    }

    private static IWordsStorage MockWordsStorage() {
        return mock(IWordsStorage.class);
    }
}

