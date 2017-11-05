//package newWordsBot.methodology;
//
//import newWordsBot.LearningStage;
//import newWordsBot.PartOfSpeech;
//import newWordsBot.Word;
//import newWordsBot.dotNetStyle.DateTime;
//
//public class LearningMethodology implements ILearningMethodology {
//    private ITimeProvider timeProvider;
//
//    public LearningMethodology(ITimeProvider timeProvider)
//    {
//        this.timeProvider = timeProvider;
//    }
//
//    public Word OnRightResponse(Word word)
//    {
//        if (word.Stage == LearningStage.First_1m)
//            return new Word(word.TheWord, word.Definition, word.Form, LearningStage.Second_30m, timeProvider.InThirtyMinutes(), word.AddedToDictionary);
//        if (word.Stage == LearningStage.Second_30m)
//            return new Word(word.TheWord, word.Definition, word.Form, LearningStage.Third_24h, timeProvider.InOneDay(), word.AddedToDictionary);
//        if (word.Stage == LearningStage.Third_24h)
//            return new Word(word.TheWord, word.Definition, word.Form, LearningStage.Forth_14d, timeProvider.InForteenDays(), word.AddedToDictionary);
//        if (word.Stage == LearningStage.Forth_14d)
//            return new Word(word.TheWord, word.Definition, word.Form, LearningStage.Fifth_60d, timeProvider.InSixtyDays(), word.AddedToDictionary);
//        if (word.Stage == LearningStage.Fifth_60d)
//            return new Word(word.TheWord, word.Definition, word.Form, LearningStage.Sixth_Completed, DateTime.MaxValue, word.AddedToDictionary);
//        throw new Exception($"Unexpected stage {word.Stage}");
//    }
//
//    public Word OnWrongResponse(Word word)
//    {
//        return new Word(word.TheWord, word.Definition, word.Form, LearningStage.First_1m, timeProvider.InOneMinute(), word.AddedToDictionary);
//    }
//
//    public Word CreateNewWord(string word, string definition, PartOfSpeech form)
//    {
//        return new Word(word, definition, form, LearningStage.First_1m, timeProvider.InOneMinute(), DateTime.UtcNow);
//    }
//}
