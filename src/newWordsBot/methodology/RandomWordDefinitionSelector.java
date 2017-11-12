package newWordsBot.methodology;

import newWordsBot.PartOfSpeech;
import newWordsBot.dictionary.DictionaryItem;
import newWordsBot.dictionary.IWordsDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class RandomWordDefinitionSelector implements IRandomWordDefinitionSelector {
    private Logger logger = LogManager.getLogger(LearningMethodology.class);
    private Random rnd = new Random();
    private IWordsDictionary dictionary;
    private List<String> words;
    private int maxAttempts = 1;

    public RandomWordDefinitionSelector(IWordsDictionary dictionary) throws IOException {
        this.dictionary = dictionary;

        words = Files.readAllLines(Paths.get("Resources/words"));
        logger.info("Initialized words for randoms difinitions. Words count: {}", words.size());
    }

    public String Select(PartOfSpeech partOfSpeech) {
        DictionaryItem dictionaryItem;
        for (int i = 0; i < maxAttempts; i++) {
            dictionaryItem = GetRandomWordFromDictionary();
            if (partOfSpeech == dictionaryItem.getPartOfSpeech()) {
                if (dictionaryItem.getDefinitions().size() > 0) {
                    return GetRandomDefinition(dictionaryItem);
                }
            }
        }
        logger.warn("Didn't manage to find '{}' in {} attempts. Let's return any word", partOfSpeech, maxAttempts);
        while ((dictionaryItem = GetRandomWordFromDictionary()).getDefinitions().size() == 0) {
        }
        return GetRandomDefinition(dictionaryItem);
    }

    private String GetRandomDefinition(DictionaryItem dictionaryItem) {
        int randomIndex = rnd.nextInt(dictionaryItem.getDefinitions().size());
        return dictionaryItem.getDefinitions().get(randomIndex);
    }

    private DictionaryItem GetRandomWordFromDictionary() {
        int randomIndex = rnd.nextInt(words.size());
        String rndWord = words.get(randomIndex);
        DictionaryItem dictionaryItem = dictionary.find(rndWord);
        return dictionaryItem;
    }
}
