package newWordsBot.handlers;

import static org.junit.Assert.*;
import org.junit.Test;

public class QuizCallbackDataTest {
    @Test
    public void serialize() {
        QuizCallbackData data = new QuizCallbackData("show me the World", 3);

        String serialized = data.serialize();

        assertEquals("/response 3 show me the World", serialized);
    }

    @Test
    public void deserialize() {
        QuizCallbackData data = QuizCallbackData.deserialize("/response 3 show me the World");

        assertEquals("show me the World", data.getWord());
        assertEquals(3, data.getVariantIndex());
    }

}