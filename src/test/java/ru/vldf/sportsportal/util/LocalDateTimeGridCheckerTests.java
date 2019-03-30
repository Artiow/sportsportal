package ru.vldf.sportsportal.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class LocalDateTimeGridCheckerTests {

    @Test
    public void test_ValidHHATrueFHRFalse() {

        // arrange
        List<LocalDateTime> testedList = Arrays.asList(
                LocalDateTime.of(1, 1, 1, 1, 0, 0),
                LocalDateTime.of(1, 1, 1, 2, 0, 0),
                LocalDateTime.of(1, 1, 1, 4, 30, 0),
                LocalDateTime.of(1, 1, 1, 5, 0, 0)
        );

        // act and assert
        Assert.assertTrue(LocalDateTimeGridChecker.check(testedList, true, false, false));

        // additional assert
        Assert.assertFalse(LocalDateTimeGridChecker.check(testedList, true, true, false));
        Assert.assertFalse(LocalDateTimeGridChecker.check(testedList, false, false, false));
    }

    @Test
    public void test_InvalidHHATrueFHRFalse() {

        // arrange
        List<LocalDateTime> testedList = Arrays.asList(
                LocalDateTime.of(1, 1, 1, 1, 1, 0),
                LocalDateTime.of(1, 1, 1, 2, 1, 0),
                LocalDateTime.of(1, 1, 1, 4, 15, 0),
                LocalDateTime.of(1, 1, 1, 5, 1, 0)
        );

        // act and assert
        Assert.assertFalse(LocalDateTimeGridChecker.check(testedList, true, false, false));
    }

    @Test
    public void test_ValidHHAFalseFHRFalse() {

        // arrange
        List<LocalDateTime> testedList = Arrays.asList(
                LocalDateTime.of(1, 1, 1, 1, 0, 0),
                LocalDateTime.of(1, 1, 1, 2, 0, 0),
                LocalDateTime.of(1, 1, 1, 4, 0, 0),
                LocalDateTime.of(1, 1, 1, 5, 0, 0)
        );

        // act and assert
        Assert.assertTrue(LocalDateTimeGridChecker.check(testedList, false, false, false));
    }

    @Test
    public void test_InvalidHHAFalseFHRFalse() {

        // arrange
        List<LocalDateTime> testedList = Arrays.asList(
                LocalDateTime.of(1, 1, 1, 1, 0, 0),
                LocalDateTime.of(1, 1, 1, 2, 0, 0),
                LocalDateTime.of(1, 1, 1, 4, 30, 0),
                LocalDateTime.of(1, 1, 1, 5, 0, 0)
        );

        // act and assert
        Assert.assertFalse(LocalDateTimeGridChecker.check(testedList, false, false, false));
    }

    @Test
    public void test_ValidHHATrueFHRTrue() {

        // arrange
        List<LocalDateTime> testedList = Arrays.asList(
                LocalDateTime.of(1, 1, 1, 1, 30, 0),
                LocalDateTime.of(1, 1, 1, 2, 0, 0),
                LocalDateTime.of(1, 1, 1, 4, 30, 0),
                LocalDateTime.of(1, 1, 1, 5, 0, 0)
        );

        // act and assert
        Assert.assertTrue(LocalDateTimeGridChecker.check(testedList, true, true, false));
    }

    @Test
    public void test_InvalidHHATrueFHRTrue() {

        // arrange
        List<LocalDateTime> testedList = Arrays.asList(
                LocalDateTime.of(1, 1, 1, 1, 0, 0),
                LocalDateTime.of(1, 1, 1, 2, 0, 0),
                LocalDateTime.of(1, 1, 1, 4, 0, 0),
                LocalDateTime.of(1, 1, 1, 5, 0, 0)
        );

        // act and assert
        Assert.assertFalse(LocalDateTimeGridChecker.check(testedList, true, true, false));
    }
}
