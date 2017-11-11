package newWordsBot.dotNetStyle;

import java.util.Date;

public class DateTime {
    public static Date UtcNow() {
        return new Date();
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
}

