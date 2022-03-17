package no.ssb.kostra.control.felles;

import no.ssb.kostra.utils.Format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Comparator {

    private Comparator() {
    }

    public static boolean compareIntegerOperatorInteger(
            final Integer a, final String op, final Integer b) {

        if (a == null
                || op == null
                || op.isEmpty()
                || b == null) {
            return false;
        }

        if (op.equalsIgnoreCase("<")) {
            return a < b;
        }
        if (op.equalsIgnoreCase("<=")) {
            return a <= b;
        }
        if (op.equalsIgnoreCase(">")) {
            return a > b;
        }
        if (op.equalsIgnoreCase(">=")) {
            return a >= b;
        }
        if (op.equalsIgnoreCase("==")) {
            return a.intValue() == b.intValue();
        }
        if (op.equalsIgnoreCase("!=")) {
            return a.intValue() != b.intValue();
        }
        return false;
    }

    public static boolean isCodeInCodeList(final String code, final List<String> codeList) {
        return codeList.stream().anyMatch(item -> compareString1EqualsString2(item, code));
    }

    public static List<String> removeCodesFromCodeList(
            final List<String> codeList, final List<String> codesToRemoveList) {

        return codeList.stream()
                .filter(code -> !isCodeInCodeList(code, codesToRemoveList))
                .sorted()
                .collect(Collectors.toList());
    }

    public static boolean isValidOrgnr(final String orgnr) {
        final var LENGTH = 9;

        if (orgnr.length() != LENGTH) return false;
        if (orgnr.substring(0, 1).equalsIgnoreCase("0")) return false;

        final var s = Arrays.stream(orgnr.split(""))
                .map(Format::parseInt)
                .collect(Collectors.toList());

        final var sum = IntStream.range(0, s.size() - 1)
                .map(index -> s.get(index) * (7 - (index + 4) % 6))
                .reduce(0, Integer::sum);

        final var rest = sum % 11;
        if (rest == 1) return false;

        final var k1 = (rest == 0) ? 0 : 11 - rest;
        return k1 == s.get(8);
    }

    public static boolean between(
            final int i, final int minValueInclusive, final int maxValueInclusive) {

        return minValueInclusive <= i && i <= maxValueInclusive;
    }

    public static boolean outsideOf(
            final int i, final int minValueInclusive, final int maxValueInclusive) {
        return i < minValueInclusive || maxValueInclusive < i;
    }

    public static boolean isValidDate(final String date, final String datePattern) {
        final var blankZeroDate = "0".repeat(datePattern.length());
        final var blankSpaceDate = " ".repeat(datePattern.length());

        if (date.equalsIgnoreCase(blankZeroDate) || date.equalsIgnoreCase(blankSpaceDate)) {
            return false;
        }

        try {
            final var dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
            LocalDate.parse(date, dateTimeFormatter);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String defaultString(final String s, final String defaultString) {
        return !isEmpty(s)
                ? s
                : defaultString;
    }

    public static boolean isEmpty(final String s) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    public static boolean compareString1EqualsString2(final String string1, final String string2) {
        return Objects.equals(string1, string2);
    }
}
