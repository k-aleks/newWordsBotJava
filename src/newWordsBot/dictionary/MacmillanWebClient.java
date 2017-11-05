package newWordsBot.dictionary;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MacmillanWebClient {
    private static final String baseUrl = "https://www.macmillandictionary.com/search/british/direct/?q=";
    //private static final String baseUrl = "https://www.macmillandictionary.com/dictionary/british/";

    public List<String> getDefinitions(String word) throws IOException {
        Document doc = Jsoup.connect(getUrl(word)).get();
        return getDefinitionsFromDocument(doc, word);
    }

    private List<String> getDefinitionsFromDocument(Document doc, String word) {
        ArrayList<String> definitions = new ArrayList<>();

        Element rootElement = doc;
        Elements definitionElements;
        if (isMultiword(word)) {
            Elements multiwordElements = doc.getElementsByClass("MULTIWORD");
            for (Element multiwordElement: multiwordElements) {
               if (multiwordElement.text().equals(word))
                   rootElement = multiwordElement.parent().parent();
            }
        }
        definitionElements = rootElement.getElementsByClass("DEFINITION");
        for (Element element: definitionElements) {
            String text = element.text();
            definitions.add(text);
        }
        return definitions;
    }

    private boolean isMultiword(String word) {
        return word.indexOf(" ") > 0;
    }

    private String getUrl(String word) {
        return baseUrl + word.replace(" ", "+");
    }

}

