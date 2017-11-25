package newWordsBot.handlers;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;


import java.util.List;

public interface IHandler
{
    void Start();
    boolean tryHandleInputMessage(Update update);
    List<SendMessage> getOutputMessages();
}