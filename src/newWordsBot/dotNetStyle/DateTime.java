package newWordsBot.dotNetStyle;

import java.time.LocalDate;
import java.util.Date;

public class DateTime {
    public static Date UtcNow() {
        return new Date();
    }

    public static Date UtcNowPlusDays(int days) {
        return UtcNowPlusHours(days*24);
    }

    public static Date UtcNowPlusHours(int hours) {
        return UtcNowPlusMinutes(hours*60);
    }

    public static Date UtcNowPlusMinutes(int minutes) {
        return UtcNowPlusSeconds(minutes*60);
    }

    public static Date UtcNowPlusSeconds(int seconds) {
        return UtcNowPlusMilliseconds(seconds*1000);
    }

    public static Date UtcNowPlusMilliseconds(int milliseconds) {
       return new Date(new Date().getTime() + milliseconds);
    }

    public static Date MaxValue() {
        return new Date(Long.MAX_VALUE);
    }

    public static Date MinValue() {
        return new Date(0);
    }
}

