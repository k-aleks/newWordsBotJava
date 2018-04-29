package newWordsBot.methodology;

import newWordsBot.dotNetStyle.DateTime;


import java.util.Date;
import java.util.Random;

public class TimeProvider implements ITimeProvider {
    private Random rnd = new Random();
    private double randomizationFactor;

    public TimeProvider(double randomizationFactor) {
        if (randomizationFactor < 0 || randomizationFactor > 0.5)
            throw new IllegalArgumentException();
        this.randomizationFactor = randomizationFactor;
    }

    @Override
    public Date InOneMinute() {
        return DateTime.UtcNowPlusMinutes(1);
    }

    @Override
    public Date InThirtyMinutes() {
        return DateTime.UtcNowPlusSeconds(randomize(30*60));
    }

    @Override
    public Date InOneDay() {
        return DateTime.UtcNowPlusHours(randomize(1*24));
    }

    @Override
    public Date InForteenDays() {
        return DateTime.UtcNowPlusHours(randomize(14*24));
    }

    @Override
    public Date InSixtyDays() {
        return DateTime.UtcNowPlusHours(randomize(60*24));
    }

    private int randomize(int initialValue) {
        int maxDelta = (int) (initialValue * randomizationFactor);
        int delta = rnd.nextInt(maxDelta + 1) * (rnd.nextBoolean() ? 1 : -1);
        return initialValue + delta;
    }
}

