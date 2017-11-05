package newWordsBot.dictionary;

import newWordsBot.PartOfSpeech;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

//TODO: refactoring
public class MacmillanDictionary implements IWordsDictionary{
    private static final String baseUrl = "https://www.macmillandictionary.com/search/british/direct/?q=";
    //private static final String baseUrl = "https://www.macmillandictionary.com/dictionary/british/";
    private static final Logger logger = LogManager.getLogger(MacmillanDictionary.class);

    public DictionaryItem find(String word) {
        try {
            Document doc = Jsoup.connect(getUrl(word)).get();
            return getDefinitionsFromDocument(doc, word);
        } catch (Exception e) {
            logError(e, word);
            return null;
        }
    }

    private DictionaryItem getDefinitionsFromDocument(Document doc, String word) {
        ArrayList<String> definitions = new ArrayList<>();
        PartOfSpeech partOfSpeech = PartOfSpeech.Unknown;

        Element rootElement = doc;
        Elements definitionElements;
        if (isMultiword(word)) {
            Elements multiwordElements = doc.getElementsByClass("MULTIWORD");
            for (Element multiwordElement: multiwordElements) {
               if (multiwordElement.text().equals(word)) {
                   rootElement = multiwordElement.parent().parent();
                   partOfSpeech = PartOfSpeech.Phrase;
               }
            }
        }

        definitionElements = rootElement.getElementsByClass("DEFINITION");
        for (Element element: definitionElements) {
            String text = element.text();
            definitions.add(text);
        }

        if (partOfSpeech == PartOfSpeech.Unknown) {
            //Element entryContentElement = doc.getElementById("entryContent");
            Elements partOfSpeechElements = rootElement.getElementsByClass("PART-OF-SPEECH");
            if (partOfSpeechElements.size() > 0) {
                String partOfSpeechValue = partOfSpeechElements.get(0).text();
                partOfSpeech = getPartOfSpeech(partOfSpeechValue);
            }
        }

        if (definitions.size() == 0)
            return null;

        return new DictionaryItem(word, definitions, partOfSpeech);
    }

    private PartOfSpeech getPartOfSpeech(String partOfSpeechValue)
    {
        //TODO: complete part of speech
        switch (partOfSpeechValue.toLowerCase())
        {
            case "noun" : return PartOfSpeech.Noun;
            case "verb" : return PartOfSpeech.Verb;
            case "phrasal verb" : return PartOfSpeech.PhrasalVerb;
            case "adjective" : return PartOfSpeech.Adjective;
            case "phrase" : return PartOfSpeech.Phrase;
        }
        return PartOfSpeech.Unknown;
    }

    private boolean isMultiword(String word) {
        return word.indexOf(" ") > 0;
    }

    private String getUrl(String word) {
        return baseUrl + word.replace(" ", "+");
    }

    private void logError(Exception e, String word) {
        logger.error("Error during finding definition for the word '{}', exception: {}", word, e);
    }
}

