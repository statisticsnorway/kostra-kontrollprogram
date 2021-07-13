package no.ssb.kostra.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static no.ssb.kostra.control.felles.Comparator.between;

public final class Fnr {
    static boolean isValidDate(String dateStr, String dateFormat) {
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidNorwId(String fnr) {
        int[] s = new int[12];

        if (fnr.length() != 11) return false;
        if (fnr.trim().length() == 0) return false;

        for (int i = 1; i <= 11; i++) s[i] = Format.parseInt(fnr.substring(i - 1, i));

        int k1 = (s[1] * 3) + (s[2] * 7) + (s[3] * 6) + s[4] + (s[5] * 8) +
                (s[6] * 9) + (s[7] * 4) + (s[8] * 5) + (s[9] * 2);

        int rest = k1 % 11;
        if (rest == 1) return false;
        if (rest == 0) k1 = 0;
        else k1 = 11 - rest;

        int k2 = (s[1] * 5) + (s[2] * 4) + (s[3] * 3) + (s[4] * 2) + (s[5] * 7) +
                (s[6] * 6) + (s[7] * 5) + (s[8] * 4) + (s[9] * 3) + (k1 * 2);

        rest = k2 % 11;
        if (rest == 1) return false;
        if (rest == 0) k2 = 0;
        else k2 = 11 - rest;

        return (k1 == s[10]) && (k2 == s[11]);
    }

    public static int getAlderFromFnr(String fnr, int rappAar) {
        final int fAar = Integer.parseInt(fnr.substring(4, 6));
        final int y20xx = Integer.parseInt("20" + fnr.substring(4, 6));
        final int y19xx = Integer.parseInt("19" + fnr.substring(4, 6));

        if (isValidNorwId(fnr)) {
            int individNr = Integer.parseInt(fnr.substring(6, 9));
            int fodselsAar = (between(individNr, 500, 900) && fAar < 40) ? y20xx : y19xx;
            return rappAar - fodselsAar;

        } else {
            if (isValidDate(fnr.substring(0, 6), "ddMMyy")) {
                int fodselsAar = (fAar < rappAar) ? y20xx : y19xx;
                return rappAar - fodselsAar;
            }

            return -1;
        }
    }

    public static boolean isValidDUFnr(String dufnr) {
        if (dufnr.length() != 12) return false;
        if (dufnr.trim().length() == 0) return false;

        List<Integer> weights = List.of(4, 6, 3, 2, 4, 6, 3, 2, 7, 5);
        String dufNr = dufnr.replace(' ', '0');
        List<Character> numbers = dufNr.chars().mapToObj(e -> (char) e).collect(Collectors.toList());

        try {
            Integer sum = IntStream.range(0, Math.min(numbers.size(), weights.size()))
                    .mapToObj(i -> Integer.parseInt(String.valueOf(numbers.get(i))) * weights.get(i))
                    .reduce(0, Integer::sum);
            int remainder = sum % 11;
            String kontrollTall = (remainder < 10 ? "0" : "") + remainder;
            return dufNr.substring(10).equalsIgnoreCase(kontrollTall);
        } catch (Exception e) {
            return false;
        }
    }
}
