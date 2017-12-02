package newWordsBot.handlers;

import org.telegram.telegrambots.api.objects.Update;


import java.util.List;

public interface IHandler
{
    void Start();
    boolean tryHandleInputMessage(Update update);
    List<OutputMessage> getOutputMessages();
}