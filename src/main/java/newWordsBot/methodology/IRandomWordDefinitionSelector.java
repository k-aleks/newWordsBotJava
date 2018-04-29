package newWordsBot.methodology;

import newWordsBot.PartOfSpeech;

public interface IRandomWordDefinitionSelector
{
    String Select(PartOfSpeech partOfSpeech);
}
