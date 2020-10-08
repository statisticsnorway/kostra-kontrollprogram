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

/**
 * @author pll
 */
public final class Toolkit {
    public static int getAlderFromFnr(String fnr) throws Exception {

        int alder;

        boolean fnrIsValid = DatoFnr.validNorwId(fnr) == 1;

        if (fnrIsValid)
            alder = getAlderFromValidFnr(fnr);
        else
            alder = getAlderFromInvalidFnr(fnr);

        return alder;
    }


    private static int getAlderFromInvalidFnr(String fnr) throws Exception {

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

            throw new Exception("Ugyldig datodel i fødselsnummer.");
        }

        alder = Constants.getRapporteringsAar() - fodselsAar;

        return alder;
    }


    private static int getAlderFromValidFnr(String fnr) throws Exception {
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


}


