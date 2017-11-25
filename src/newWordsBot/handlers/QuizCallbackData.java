package newWordsBot.handlers;

public class QuizCallbackData {

    public static String PREFIX = "/response";

    private String word;
    private int variantIndex;

    public QuizCallbackData(String word, int variantIndex) {
        this.word = word;
        this.variantIndex = variantIndex;
    }

    public String serialize() {
        return String.format("%s %s %s", PREFIX, variantIndex, word);
    }

    public static QuizCallbackData deserialize(String data) {
        String response = data.replace(PREFIX + " ", "");
        String[] strings = response.split(" ", 2);
        int variantIndex = Integer.parseInt(strings[0]);
        String responseForWord = strings[1];
        return new QuizCallbackData(responseForWord, variantIndex);
    }

    public String getWord() {
        return word;
    }

    public int getVariantIndex() {
        return variantIndex;
    }

}
