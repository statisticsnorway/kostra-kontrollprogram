package no.ssb.kostra.control.felles;

import no.ssb.kostra.utils.Format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Comparator {
    private Comparator(){}

    public static boolean compareIntegerOperatorInteger(Integer a, String op, Integer b) {
        boolean ok = false;

        if (a != null && op != null && !op.isEmpty() && b != null) {
            if (op.equalsIgnoreCase("<")) {
                ok = (a < b);

            } else if (op.equalsIgnoreCase("<=")) {
                ok = (a <= b);

            } else if (op.equalsIgnoreCase(">")) {
                ok = (a > b);

            } else if (op.equalsIgnoreCase(">=")) {
                ok = (a >= b);

            } else if (op.equalsIgnoreCase("==")) {
                ok = (a.intValue() == b.intValue());

            } else if (op.equalsIgnoreCase("!=")) {
                ok = (a.intValue() != b.intValue());
            }
        }

        return ok;
    }

    public static boolean isCodeInCodelist(final String code, final List<String> codeList){
        return codeList.stream().anyMatch(item -> compareString1EqualsString2(item, code));
    }

    public static List<String> removeCodesFromCodelist(final List<String> codeList, final List<String> codesToRemoveList){
        return codeList.stream()
                .filter(code -> !isCodeInCodelist(code, codesToRemoveList))
                .sorted()
                .collect(Collectors.toList());
    }

    public static boolean isValidOrgnr(final String orgnr) {
        final int LENGTH = 9;
        if (orgnr.length() != LENGTH) return false;
        if (orgnr.substring(0,1).equalsIgnoreCase("0")) return false;

        List<Integer> s = Arrays.stream(orgnr.split(""))
                .map(Format::parseInt)
                .collect(Collectors.toList());

        int sum = IntStream.range(0, s.size() - 1)
        .map(index -> s.get(index) * (7 - (index + 4) % 6))
        .reduce(0, Integer::sum);

        int rest = sum % 11;
        if (rest == 1) return false;

        int k1 = (rest == 0) ? 0 : 11 - rest;
        return (k1 == s.get(8));
    }

    public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        return (minValueInclusive <= i && i <= maxValueInclusive);
    }

    public static boolean outsideOf(int i, int minValueInclusive, int maxValueInclusive) {
        return (i < minValueInclusive || maxValueInclusive < i);
    }

    public static boolean isValidDate(final String date, final String datePattern) {
        String blankZeroDate = "0".repeat(datePattern.length());
        String blankSpaceDate = " ".repeat(datePattern.length());

        if (date.equalsIgnoreCase(blankZeroDate) || date.equalsIgnoreCase(blankSpaceDate)) {
            return false;
        }

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
            LocalDate.parse(date, dtf);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String defaultString(String s, String defaultString) {
        return (!isEmpty(s)) ? s : defaultString;
    }

    public static boolean isEmpty(final String s) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    public static boolean compareString1EqualsString2(String string1, String string2){
        return Objects.equals(string1, string2);
    }
}
