package newWordsBot;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;


import java.util.Date;

public class Word {
    @BsonId
    private String word;
    @BsonProperty("Definition")
    private String definition;
    @BsonProperty("Form")
    private PartOfSpeech form;
    @BsonProperty("Stage")
    private LearningStage stage;
    @BsonProperty("NextRepetition")
    private Date nextRepetition;
    @BsonProperty("AddedToDictionary")
    private Date addedToDictionary;

    public Word() {
    }

    public Word(String word, String definition, PartOfSpeech form, LearningStage stage, Date nextRepetition, Date addedToDictionary) {
        this.word = word;
        this.definition = definition;
        this.form = form;
        this.stage = stage;
        this.nextRepetition = nextRepetition;
        this.addedToDictionary = addedToDictionary;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public PartOfSpeech getForm() {
        return form;
    }

    public void setForm(PartOfSpeech form) {
        this.form = form;
    }

    public LearningStage getStage() {
        return stage;
    }

    public void setStage(LearningStage stage) {
        this.stage = stage;
    }

    public Date getNextRepetition() {
        return nextRepetition;
    }

    public void setNextRepetition(Date nextRepetition) {
        this.nextRepetition = nextRepetition;
    }

    public Date getAddedToDictionary() {
        return addedToDictionary;
    }

    public void setAddedToDictionary(Date addedToDictionary) {
        this.addedToDictionary = addedToDictionary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (!word.equals(word1.word)) return false;
        if (!definition.equals(word1.definition)) return false;
        if (form != word1.form) return false;
        if (stage != word1.stage) return false;
        if (!nextRepetition.equals(word1.nextRepetition)) return false;
        return addedToDictionary.equals(word1.addedToDictionary);
    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + definition.hashCode();
        result = 31 * result + form.hashCode();
        result = 31 * result + stage.hashCode();
        result = 31 * result + nextRepetition.hashCode();
        result = 31 * result + addedToDictionary.hashCode();
        return result;
    }
}
