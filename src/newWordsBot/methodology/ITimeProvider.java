package newWordsBot.methodology;

import java.util.Date;

public interface ITimeProvider {
    Date InOneMinute();
    Date InThirtyMinutes();
    Date InOneDay();
    Date InForteenDays();
    Date InSixtyDays();
}
