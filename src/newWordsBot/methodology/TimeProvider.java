package newWordsBot.methodology;

import newWordsBot.dotNetStyle.DateTime;


import java.util.Date;

public class TimeProvider implements ITimeProvider {

    @Override
    public Date InOneMinute() {
        return DateTime.UtcNowPlusMinutes(1);
    }

    @Override
    public Date InThirtyMinutes() {
        return DateTime.UtcNowPlusMinutes(30);
    }

    @Override
    public Date InOneDay() {
        return DateTime.UtcNowPlusDays(1);
    }

    @Override
    public Date InForteenDays() {
        return DateTime.UtcNowPlusDays(14);
    }

    @Override
    public Date InSixtyDays() {
        return DateTime.UtcNowPlusDays(60);
    }
}

