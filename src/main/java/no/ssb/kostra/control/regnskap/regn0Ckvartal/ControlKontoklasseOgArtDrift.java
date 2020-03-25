package no.ssb.kostra.control.regnskap.regn0Ckvartal;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

final class ControlKontoklasseOgArtDrift
        extends no.ssb.kostra.control.Control {
    private Vector<String[]> invalidCombinations = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;
        String kontoklasse = RecordFields.getKontoklasse(line);
        String art = RecordFields.getArt(line);

        if (Arrays.asList("509", "540", "570", "590", "800", "870", "874", "875", "877", "909", "990").contains(art)
                && !kontoklasse.equalsIgnoreCase("1")) {
            lineHasError = true;
            String[] container = {kontoklasse, art, Integer.toString(lineNumber)};
            invalidCombinations.add(container);
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 10, kombinasjon kontoklasse og art i driftsregnskapet:" + lf + lf;
        if (foundError()) {
            int numOfRecords = invalidCombinations.size();
            errorReport += "\tFeil: Arten er kun tillatt brukt i driftsregnskapet." + lf;
            for (int i = 0; i < numOfRecords; i++) {
                String[] container = invalidCombinations.elementAt(i);
                errorReport += "\t\tkontoklasse: " + container[0] +
                        " art: " + container[1] + " (Record nr. " + container[2] + ")" + lf;
            }
        }
        errorReport += lf;
        return errorReport;
    }

    public boolean foundError() {
        return invalidCombinations.size() > 0;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}