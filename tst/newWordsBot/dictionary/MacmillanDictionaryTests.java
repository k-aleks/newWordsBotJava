package newWordsBot.dictionary;

import newWordsBot.PartOfSpeech;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

//TODO: move functional tests to separate package of mark them somehow
class MacmillanDictionaryTests {

    @Test
    void findFromDocument_shouldReturnDefinitionsForAsset() throws IOException {
        DictionaryItem result = new MacmillanDictionary().find("asset");

        Assert.assertEquals("asset", result.getWord());

        Assert.assertEquals(PartOfSpeech.Noun, result.getPartOfSpeech());

        Assert.assertEquals(3, result.getDefinitions().size());
        Assert.assertEquals("something such as money or property that a person or company owns",
                result.getDefinitions().get(0));
        Assert.assertEquals("an item of text or media that has been put into a digital form that includes the right to use it",
                result.getDefinitions().get(1));
        Assert.assertEquals("a major benefit",
                result.getDefinitions().get(2));
    }

    @Test
    void findFromDocument_shouldReturnDefinitionsForGetRidOf() throws IOException {
        DictionaryItem result = new MacmillanDictionary().find("get rid of");

        Assert.assertEquals("get rid of", result.getWord());

        Assert.assertEquals(PartOfSpeech.Phrase, result.getPartOfSpeech());

        Assert.assertEquals(3, result.getDefinitions().size());
        Assert.assertEquals("to throw away, give away, or sell a possession that you no longer want or need",
                result.getDefinitions().get(0));
        Assert.assertEquals("to take action that stops something annoying, unpleasant, or not wanted from affecting you",
                result.getDefinitions().get(1));
        Assert.assertEquals("to make someone go away because they are annoying, unpleasant, or not wanted",
                result.getDefinitions().get(2));
    }

    @Test
    void findFromDocument_shouldReturnDefinitionsForConverge() throws IOException {
        DictionaryItem result = new MacmillanDictionary().find("converge");

        Assert.assertEquals("converge", result.getWord());

        Assert.assertEquals(PartOfSpeech.Verb, result.getPartOfSpeech());

        Assert.assertEquals(2, result.getDefinitions().size());
        Assert.assertEquals("to come from different directions to reach the same point",
                result.getDefinitions().get(0));
        Assert.assertEquals("to become the same or very similar",
                result.getDefinitions().get(1));
    }

    @Test
    void findFromDocument_shouldReturnDefinitionsForThatSaid() throws IOException {
        DictionaryItem result = new MacmillanDictionary().find("that said");

        Assert.assertEquals("that said", result.getWord());

        Assert.assertEquals(PartOfSpeech.Phrase, result.getPartOfSpeech());

        Assert.assertEquals(1, result.getDefinitions().size());
        Assert.assertEquals("used for adding an opinion that seems to be the opposite of what you have just said, although you think both are true",
                result.getDefinitions().get(0));
    }

    @Test
    void findFromDocument_shouldReturnDefinitionsForBuyIn() throws IOException {
        DictionaryItem result = new MacmillanDictionary().find("buy in");

        Assert.assertEquals("buy in", result.getWord());

        Assert.assertEquals(PartOfSpeech.PhrasalVerb, result.getPartOfSpeech());

        Assert.assertEquals(1, result.getDefinitions().size());
        Assert.assertEquals("to buy a large quantity of something",
                result.getDefinitions().get(0));
    }

    @Test
    void findFromDocument_shouldReturnNullIfNoResults() throws IOException {
        DictionaryItem result = new MacmillanDictionary().find("lalala");
        Assert.assertNull(result);
    }
}
