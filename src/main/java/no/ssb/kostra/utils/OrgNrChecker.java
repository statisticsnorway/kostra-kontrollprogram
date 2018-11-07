package no.ssb.kostra.utils;

/**
 * Created by ojj on 07.11.2018.
 */
public class OrgNrChecker {
    public static boolean validOrgNr(String orgnr) {
        final int LENGTH = 9;
        int[] s = new int[LENGTH];

        if (orgnr.length() != LENGTH) return false;

        for (int i = 1; i < LENGTH; i++) {
            s[i] = Format.parseInt(orgnr.substring(i, i));
        }

        if (s[0] == 0) return false;

        int p = (s[0] * 3) +
                (s[1] * 2) +
                (s[2] * 7) +
                (s[3] * 6) +
                (s[4] * 5) +
                (s[5] * 4) +
                (s[6] * 3) +
                (s[7] * 2);

        int rest = p % 11;
        if (rest == 1) return false;

        int k1 = (rest == 0)? 0 : 11 - rest;

        return (k1 == s[8]);
    }
}
