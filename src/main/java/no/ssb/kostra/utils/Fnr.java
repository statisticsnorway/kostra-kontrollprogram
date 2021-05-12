package no.ssb.kostra.utils;

import no.ssb.kostra.felles.Constants;

public final class Fnr {
    static final int[] ANT_DAGER = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static int day, month, year;


    static void parseDate(int yfmt, String dt) {
        day = Format.parseInt(dt.substring(0, 2));

        /* Hvis man bruker D-nummer legger man til 4 på første siffer. */
        /* Når dag er større enn 31 benyttes D-nummer, trekk fra 40 og få gyldig dag  */
        if (day > 31) {
            day = day - 40;
        }

        month = Format.parseInt(dt.substring(2, 4));
        switch (yfmt) {
            case 6:
                year = Format.parseInt(dt.substring(4, 6));
                break;
            case 8:
                year = Format.parseInt(dt.substring(4, 8));
                break;
            default:
                year = 0;
        }
    }

    public static int validDateDDMMYY(String dt) {
        try {
            Integer.parseInt(dt);
        } catch (Exception e) {
            return 0;
        }
        if (dt.length() < 6) return (0);
        parseDate(6, dt);
        if ((month < 1) || (month > 12)) return (0);
        // Leap year - NB gives wrong result for 29.02.1900 !!! Correct for 2000
        if (((day > 0) && (day <= ANT_DAGER[month])) || ((month == 2) && (day == 29) && (year % 4 == 0))) return (1);
        else return (0);


    }


    public static int validNorwId(String fnr) {
        int[] s = new int[12];
        int k1, k2, rest;

        if (fnr.length() < 11) return (0);
        String f_dt = fnr.substring(0, 6);

        if (validDateDDMMYY(f_dt) == 0) return (0);

        for (int i = 1; i <= 11; i++) s[i] = Format.parseInt(fnr.substring(i - 1, i));

        k1 = (s[1] * 3) + (s[2] * 7) + (s[3] * 6) + s[4] + (s[5] * 8) +
                (s[6] * 9) + (s[7] * 4) + (s[8] * 5) + (s[9] * 2);

        rest = k1 % 11;
        if (rest == 1) return (0);
        if (rest == 0) k1 = 0;
        else k1 = 11 - rest;

        k2 = (s[1] * 5) + (s[2] * 4) + (s[3] * 3) + (s[4] * 2) + (s[5] * 7) +
                (s[6] * 6) + (s[7] * 5) + (s[8] * 4) + (s[9] * 3) + (k1 * 2);

        rest = k2 % 11;
        if (rest == 1) return (0);
        if (rest == 0) k2 = 0;
        else k2 = 11 - rest;

        if ((k1 != s[10]) || (k2 != s[11])) return (0);
        return (1);
    }

    public static int getAlderFromFnr(String fnr, int rappAar) throws Exception {

        int alder;

        boolean fnrIsValid = validNorwId(fnr) == 1;

        if (fnrIsValid)
            alder = getAlderFromValidFnr(fnr, rappAar);
        else
            alder = getAlderFromInvalidFnr(fnr, rappAar);

        return alder;
    }

    private static int getAlderFromInvalidFnr(String fnr, int rappAar) throws Exception {

        int fodselsAar, alder;

        boolean dateIsValid = validDateDDMMYY(fnr.substring(0, 6)) == 1;

        if (dateIsValid) {
            int fAar = Integer.parseInt(fnr.substring(4, 6));

            if (fAar < rappAar)
                fodselsAar = Integer.parseInt("20" + fnr.substring(4, 6));
            else
                fodselsAar = Integer.parseInt("19" + fnr.substring(4, 6));

        } else {
            throw new Exception("Ugyldig datodel i fødselsnummer.");
        }

        alder = rappAar - fodselsAar;

        return alder;
    }

    private static int getAlderFromValidFnr(String fnr, int rappAar) throws Exception {
        int alder, fodselsAar;
        int individNr = Integer.parseInt(fnr.substring(6, 9));
        int fAar = Integer.parseInt(fnr.substring(4, 6));

        if (individNr >= 500 && individNr <= 999 && fAar < 40) {
            fodselsAar = Integer.parseInt("20" + fnr.substring(4, 6));

        } else if (individNr >= 0 && individNr <= 499) {
            fodselsAar = Integer.parseInt("19" + fnr.substring(4, 6));

        } else if (individNr >= 900 && individNr <= 999 && fAar > 39) {
            fodselsAar = Integer.parseInt("19" + fnr.substring(4, 6));

        } else {
            return getAlderFromInvalidFnr(fnr, rappAar);
        }

        alder = rappAar - fodselsAar;

        return alder;
    }
}






