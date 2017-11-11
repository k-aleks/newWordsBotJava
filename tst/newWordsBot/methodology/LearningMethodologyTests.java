package newWordsBot.methodology;

import newWordsBot.LearningStage;
import newWordsBot.PartOfSpeech;
import newWordsBot.Word;
import newWordsBot.dotNetStyle.DateTime;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.Date;

class LearningMethodologyTests {

    private Date timeProviderInOneMinuteResult = DateTime.UtcNowPlusMinutes(1);
    private Date timeProviderInThirtyMinutesResult = DateTime.UtcNowPlusMinutes(30);
    private Date timeProviderInOneDayResult = DateTime.UtcNowPlusDays(1);
    private Date timeProviderInForteenDaysResult = DateTime.UtcNowPlusDays(14);
    private Date timeProviderInSixtyDaysResult = DateTime.UtcNowPlusDays(60);

    private Date[] timeProviderResults;

    LearningMethodologyTests()
    {
        timeProviderResults = new Date[]
        {
            timeProviderInOneMinuteResult,
            timeProviderInThirtyMinutesResult,
            timeProviderInOneDayResult,
            timeProviderInForteenDaysResult,
            timeProviderInSixtyDaysResult,
            DateTime.MaxValue(),
        };
    }

    //TODO: replace with theories or something like that
    @Test
    void OnRightResponse_should_promote_learning_progress() {
        OnRightResponse_should_promote_learning_progress(LearningStage.First_1m, LearningStage.Second_30m, 1);
        OnRightResponse_should_promote_learning_progress(LearningStage.Second_30m, LearningStage.Third_24h, 2);
        OnRightResponse_should_promote_learning_progress(LearningStage.Third_24h, LearningStage.Forth_14d, 3);
        OnRightResponse_should_promote_learning_progress(LearningStage.Forth_14d, LearningStage.Fifth_60d, 4);
        OnRightResponse_should_promote_learning_progress(LearningStage.Fifth_60d, LearningStage.Sixth_Completed, 5);
    }

    private void OnRightResponse_should_promote_learning_progress(LearningStage currentStage, LearningStage expectedResultStage, int expectedNextRepetitionIndex)
    {
        ITimeProvider timeProvider = CreateTimeProviderMock();
        LearningMethodology lm = new LearningMethodology(timeProvider);

        Word resultWord = lm.OnRightResponse(CreateWord(currentStage));

        assertEquals(expectedResultStage, resultWord.getStage());
        assertEquals(timeProviderResults[expectedNextRepetitionIndex], resultWord.getNextRepetition());
    }

    @Test
    void OnWrongResponse_should_reset_learning_progress()
    {
        ITimeProvider timeProvider = CreateTimeProviderMock();
        LearningMethodology lm = new LearningMethodology(timeProvider);

        Word resultWord = lm.OnWrongResponse(CreateWord(LearningStage.Second_30m));

        assertEquals(LearningStage.First_1m, resultWord.getStage());
        assertEquals(timeProviderInOneMinuteResult, resultWord.getNextRepetition());
        Mockito.verify(timeProvider, Mockito.times(1)).InOneMinute();
    }

    private ITimeProvider CreateTimeProviderMock()
    {
        ITimeProvider timeProvider = Mockito.mock(ITimeProvider.class);
        Mockito.when(timeProvider.InOneMinute()).thenReturn(timeProviderInOneMinuteResult);
        Mockito.when(timeProvider.InThirtyMinutes()).thenReturn(timeProviderInThirtyMinutesResult);
        Mockito.when(timeProvider.InOneDay()).thenReturn(timeProviderInOneDayResult);
        Mockito.when(timeProvider.InForteenDays()).thenReturn(timeProviderInForteenDaysResult);
        Mockito.when(timeProvider.InSixtyDays()).thenReturn(timeProviderInSixtyDaysResult);
        return timeProvider;
    }

    private Word CreateWord(LearningStage learningStage)
    {
        return new Word("", "", PartOfSpeech.Noun, learningStage, new Date(0), new Date(0));
    }
}