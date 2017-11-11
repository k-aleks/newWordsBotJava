package newWordsBot.methodology;

import newWordsBot.LearningStage;
import newWordsBot.PartOfSpeech;
import newWordsBot.Word;
import newWordsBot.dotNetStyle.DateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LearningMethodology implements ILearningMethodology {
    private Logger logger = LogManager.getLogger(LearningMethodology.class);
    private ITimeProvider timeProvider;

    public LearningMethodology(ITimeProvider timeProvider)
    {
        this.timeProvider = timeProvider;
    }

    public Word OnRightResponse(Word word)
    {
        if (word.getStage() == LearningStage.First_1m)
            return new Word(word.getWord(), word.getDefinition(), word.getForm(), LearningStage.Second_30m, timeProvider.InThirtyMinutes(), word.getAddedToDictionary());
        if (word.getStage() == LearningStage.Second_30m)
            return new Word(word.getWord(), word.getDefinition(), word.getForm(), LearningStage.Third_24h, timeProvider.InOneDay(), word.getAddedToDictionary());
        if (word.getStage() == LearningStage.Third_24h)
            return new Word(word.getWord(), word.getDefinition(), word.getForm(), LearningStage.Forth_14d, timeProvider.InForteenDays(), word.getAddedToDictionary());
        if (word.getStage() == LearningStage.Forth_14d)
            return new Word(word.getWord(), word.getDefinition(), word.getForm(), LearningStage.Fifth_60d, timeProvider.InSixtyDays(), word.getAddedToDictionary());
        if (word.getStage() == LearningStage.Fifth_60d)
            return new Word(word.getWord(), word.getDefinition(), word.getForm(), LearningStage.Sixth_Completed, DateTime.MaxValue(), word.getAddedToDictionary());
        logger.error("Unexpected stage {}", word.getStage());
        return null;
    }

    public Word OnWrongResponse(Word word)
    {
        return new Word(word.getWord(), word.getDefinition(), word.getForm(), LearningStage.First_1m, timeProvider.InOneMinute(), word.getAddedToDictionary());
    }

    public Word CreateNewWord(String word, String definition, PartOfSpeech form)
    {
        return new Word(word, definition, form, LearningStage.First_1m, timeProvider.InOneMinute(), DateTime.UtcNow());
    }
}
