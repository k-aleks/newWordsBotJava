package newWordsBot.dictionary;

import newWordsBot.PartOfSpeech;

import java.util.List;

public class DictionaryItem
{
    private final String word;
    private final List<String> definitions;
    private final PartOfSpeech partOfSpeech;

    public DictionaryItem(String word, List<String> definitions, PartOfSpeech partOfSpeech)
    {
        this.word = word;
        this.definitions = definitions;
        this.partOfSpeech = partOfSpeech;
    }

    public String getWord() {
        return word;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }
}