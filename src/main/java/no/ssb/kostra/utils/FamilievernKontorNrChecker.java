/*
 * FamilievernKontorNrChecker.java
 */

package no.ssb.kostra.utils;

/**
 *
 */
public final class FamilievernKontorNrChecker {

    public static boolean hasCorrectKontorNr(String regionNr, String kontorNr) {

        boolean correctKontorNr;
        int kontorNrNumVal;

        try {
            kontorNrNumVal = Integer.parseInt(kontorNr);
        } catch (NumberFormatException e) {
            return false;
        }

        if (regionNr.equalsIgnoreCase("667600")) {
            correctKontorNr = kontorNrNumVal >= 17 && kontorNrNumVal <= 52;

        } else if (regionNr.equalsIgnoreCase("667500")) {
            correctKontorNr = kontorNrNumVal >= 61 && kontorNrNumVal <= 101;

        } else if (regionNr.equalsIgnoreCase("667400")) {
            correctKontorNr = kontorNrNumVal >= 111 && kontorNrNumVal <= 142;

        } else if (regionNr.equalsIgnoreCase("667300")) {
            correctKontorNr = kontorNrNumVal >= 151 && kontorNrNumVal <= 172;

        } else if (regionNr.equalsIgnoreCase("667200")) {
            correctKontorNr = kontorNrNumVal >= 181 && kontorNrNumVal <= 205;

        } else {
            // Vi har ugyldig regionnr., men det er ikke vårt ansvar å
            // å rapportere dette...
            correctKontorNr = true;
        }

        return correctKontorNr;
    }
}
