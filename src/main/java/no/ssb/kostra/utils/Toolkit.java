/*
 * Toolkit.java
 *
 * Created on 24. september 2007, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.ssb.kostra.utils;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.sensitiv.InvalidFnrException;

import java.util.Calendar;

/**
 * @author pll
 */
public final class Toolkit {
    public static int getAlderFromFnr(String fnr) throws InvalidFnrException {

        int alder;

        boolean fnrIsValid = DatoFnr.validNorwId(fnr) == 1;

        if (fnrIsValid)
            alder = getAlderFromValidFnr(fnr);
        else
            alder = getAlderFromInvalidFnr(fnr);

        return alder;
    }


    private static int getAlderFromInvalidFnr(String fnr) throws InvalidFnrException {

        int fodselsAar, alder;

        boolean dateIsValid = DatoFnr.validDateDDMMYY(fnr.substring(0, 6)) == 1;

        if (dateIsValid) {

            int fAar = Integer.parseInt(fnr.substring(4, 6));
            int rappAar = Constants.getRapporteringsAarTwoDigits();

            if (fAar < rappAar)
                fodselsAar = Integer.parseInt("20" + fnr.substring(4, 6));
            else
                fodselsAar = Integer.parseInt("19" + fnr.substring(4, 6));

        } else {

            throw new InvalidFnrException("Ugyldig datodel i fødselsnummer.");
        }

        alder = Constants.getRapporteringsAar() - fodselsAar;

        return alder;
    }


    private static int getAlderFromValidFnr(String fnr) throws InvalidFnrException {
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
            return getAlderFromInvalidFnr(fnr);
        }

        alder = Constants.getRapporteringsAar() - fodselsAar;

        return alder;
    }


    public static boolean isFirstDateBeforeLastDate_DDMMYYYY(String firstDate, String lastDate) throws Exception {
        if (CompatJdk13.isNumerical(firstDate) && CompatJdk13.isNumerical(lastDate)) {
            int checkValue = 0;
            checkValue = DatoFnr.validDateDDMMYY(firstDate);
            checkValue += DatoFnr.validDateDDMMYY(lastDate);
            if (checkValue == 2) {
                int year_firstDate = DatoFnr.findYearYYYY(Integer.parseInt(firstDate.substring(4)));
                Calendar date_firstDate = Calendar.getInstance();
                date_firstDate.set(year_firstDate,
                        Integer.parseInt(firstDate.substring(2, 4)) - 1,
                        Integer.parseInt(firstDate.substring(0, 2)));
                int year_lastDate = DatoFnr.findYearYYYY(Integer.parseInt(lastDate.substring(4)));
                Calendar date_lastDate = Calendar.getInstance();
                date_lastDate.set(year_lastDate,
                        Integer.parseInt(lastDate.substring(2, 4)) - 1,
                        Integer.parseInt(lastDate.substring(0, 2)));
                return date_firstDate.before(date_lastDate);
            }
        }

        throw new Exception("Not valid DDMMYYYY date format.");
    }


    public static boolean isFirstDateBeforeLastDateOrEqual_DDMMYYYY(String firstDate, String lastDate) throws Exception {

        if (firstDate.equalsIgnoreCase(lastDate))
            // Sjekk av at dati har riktig format er ikke metodens ansvar.
            return true;
        else
            return isFirstDateBeforeLastDate_DDMMYYYY(firstDate, lastDate);

    }

}


