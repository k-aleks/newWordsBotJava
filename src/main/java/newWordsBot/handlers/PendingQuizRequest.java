package newWordsBot.handlers;

import newWordsBot.Word;

public class PendingQuizRequest {
    private Word word;
    private int rightVariantIndex;

    public PendingQuizRequest(Word word, int rightVariantIndex) {
        this.word = word;
        this.rightVariantIndex = rightVariantIndex;
    }

    public Word getWord() {
        return word;
    }

    public int getRightVariantIndex() {
        return rightVariantIndex;
    }
}
