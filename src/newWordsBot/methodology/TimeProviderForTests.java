package newWordsBot.methodology;

import newWordsBot.dotNetStyle.DateTime;


import java.util.Date;

public class TimeProviderForTests implements ITimeProvider {

    @Override
    public Date InOneMinute() {
        return DateTime.UtcNow();
    }

    @Override
    public Date InThirtyMinutes() {
        return DateTime.UtcNowPlusMinutes(1);
    }

    @Override
    public Date InOneDay() {
        return DateTime.UtcNowPlusMinutes(2);
    }

    @Override
    public Date InForteenDays() {
        return DateTime.UtcNowPlusMinutes(5);
    }

    @Override
    public Date InSixtyDays() {
        return DateTime.UtcNowPlusMinutes(10);
    }
}
