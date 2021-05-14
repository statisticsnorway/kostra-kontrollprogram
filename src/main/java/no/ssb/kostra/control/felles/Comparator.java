package no.ssb.kostra.control.felles;

import no.ssb.kostra.utils.Format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Comparator {
    public static boolean compareIntegerOperatorInteger(Integer a, String op, Integer b) {
        boolean ok = true;
        if (a == null){
            a = 0;
        }

        if (!op.isEmpty() && b != null) {
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
                ok = !(a.intValue() == b.intValue());
            }
        }

        return ok;
    }

    public static boolean isCodeInCodelist(String code, List<String> codeList){
        return codeList.stream().anyMatch(item -> item.equalsIgnoreCase(code));
    }

    public static boolean isValidOrgnr(String orgnr) {
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

    public static boolean isValidDate(String date, String datePattern) {
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
}
