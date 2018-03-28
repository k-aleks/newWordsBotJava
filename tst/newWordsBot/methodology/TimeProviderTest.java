package newWordsBot.methodology;

import static org.junit.Assert.*;
import org.junit.Test;


import java.util.Date;

public class TimeProviderTest {
    private int randomizationTestsCount = 100;

    @Test
    public void constructor_should_throw_exception_if_randomizationFactor_too_large() {
        new TimeProvider(0.6);
    }

    @Test
    public void constructor_should_throw_exception_if_randomizationFactor_less_then_zero() {
        new TimeProvider(-0.1);
    }

    @Test
    public void inOneMinute_should_not_exceed_randomization_factor() {
        TimeProvider timeProvider = new TimeProvider(0.1);
        for (int i = 0; i < randomizationTestsCount; i++) {
            Date date = timeProvider.InOneMinute();
            validateDateDiffersFromExpectedForNoMoreThanRandomizationFactor(date, 0.1);
        }
    }

    @Test
    public void inThirtyMinutes() {
        TimeProvider timeProvider = new TimeProvider(0.1);
        for (int i = 0; i < randomizationTestsCount; i++) {
            Date date = timeProvider.InThirtyMinutes();
            validateDateDiffersFromExpectedForNoMoreThanRandomizationFactor(date, 0.1);
        }
    }

    @Test
    public void inOneDay() {
        TimeProvider timeProvider = new TimeProvider(0.1);
        for (int i = 0; i < randomizationTestsCount; i++) {
            Date date = timeProvider.InOneDay();
            validateDateDiffersFromExpectedForNoMoreThanRandomizationFactor(date, 0.1);
        }
    }

    @Test
    public void inForteenDays() {
        TimeProvider timeProvider = new TimeProvider(0.1);
        for (int i = 0; i < randomizationTestsCount; i++) {
            Date date = timeProvider.InForteenDays();
            validateDateDiffersFromExpectedForNoMoreThanRandomizationFactor(date, 0.1);
        }
    }

    @Test
    public void inSixtyDays() {
        TimeProvider timeProvider = new TimeProvider(0.1);
        for (int i = 0; i < randomizationTestsCount; i++) {
            Date date = timeProvider.InSixtyDays();
            validateDateDiffersFromExpectedForNoMoreThanRandomizationFactor(date, 0.1);
        }
    }

    private void validateDateDiffersFromExpectedForNoMoreThanRandomizationFactor(Date actualDate, Date expectedDate, double randomizationFactor) {
        long diff = Math.abs(expectedDate.getTime() - actualDate.getTime());
        double diffFactor = diff / new Date().getTime();
        assertTrue(diffFactor <= randomizationFactor);
    }

}