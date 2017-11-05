package newWordsBot.dictionary;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

//TODO: move functional tests to separate package
class MacmillanWebClientTests {

    @Test
    void getDefinitionsFromDocument_shouldReturnDefinitionsForAsset() throws IOException {
        List<String> definitions = new MacmillanWebClient().getDefinitions("asset");
        Assert.assertEquals(3, definitions.size());
        Assert.assertEquals("something such as money or property that a person or company owns",
                definitions.get(0));
        Assert.assertEquals("an item of text or media that has been put into a digital form that includes the right to use it",
                definitions.get(1));
        Assert.assertEquals("a major benefit",
                definitions.get(2));
    }

    @Test
    void getDefinitionsFromDocument_shouldReturnDefinitionsForGetRidOf() throws IOException {
        List<String> definitions = new MacmillanWebClient().getDefinitions("get rid of");
        Assert.assertEquals(3, definitions.size());
        Assert.assertEquals("to throw away, give away, or sell a possession that you no longer want or need",
                definitions.get(0));
        Assert.assertEquals("to take action that stops something annoying, unpleasant, or not wanted from affecting you",
                definitions.get(1));
        Assert.assertEquals("to make someone go away because they are annoying, unpleasant, or not wanted",
                definitions.get(2));
    }

    @Test
    void getDefinitionsFromDocument_shouldReturnDefinitionsForConverge() throws IOException {
        List<String> definitions = new MacmillanWebClient().getDefinitions("converge");
        Assert.assertEquals(2, definitions.size());
        Assert.assertEquals("to come from different directions to reach the same point",
                definitions.get(0));
        Assert.assertEquals("to become the same or very similar",
                definitions.get(1));
    }

    @Test
    void getDefinitionsFromDocument_shouldReturnDefinitionsForThatSaid() throws IOException {
        List<String> definitions = new MacmillanWebClient().getDefinitions("that said");
        Assert.assertEquals(1, definitions.size());
        Assert.assertEquals("used for adding an opinion that seems to be the opposite of what you have just said, although you think both are true",
                definitions.get(0));
    }

    @Test
    void getDefinitionsFromDocument_shouldReturnEmptyListIfNoResults() throws IOException {
        List<String> definitions = new MacmillanWebClient().getDefinitions("lalala");
        Assert.assertEquals(0, definitions.size());
    }
}
