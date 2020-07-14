package no.ssb.kostra.utils;

public class Between {
    public static boolean betweenInclusive(int i, int minValueInclusive, int maxValueInclusive) {
        return (minValueInclusive <= i && i <= maxValueInclusive);
    }

    public static boolean betweenExclusive(int i, int minValueExclusive, int maxValueExclusive) {
        return (minValueExclusive < i && i < maxValueExclusive);
    }
}
