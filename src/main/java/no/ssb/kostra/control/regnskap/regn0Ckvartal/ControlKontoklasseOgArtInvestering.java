package no.ssb.kostra.control.regnskap.regn0Ckvartal;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

final class ControlKontoklasseOgArtInvestering
        extends no.ssb.kostra.control.Control {
    private Vector<String[]> invalidCombinations = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;
        String kontoklasse = RecordFields.getKontoklasse(line);
        String art = RecordFields.getArt(line);

        if (Arrays.asList("529", "670", "910", "911", "929", "970").contains(art)
                && !kontoklasse.equalsIgnoreCase("0")) {
            lineHasError = true;
            String[] container = {kontoklasse, art, Integer.toString(lineNumber)};
            invalidCombinations.add(container);
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 9, kombinasjon kontoklasse og art i investeringsregnskapet:" + lf + lf;
        if (foundError()) {
            int numOfRecords = invalidCombinations.size();
            errorReport += "\tFeil: Arten er kun tillatt brukt i investeringsregnskapet." + lf;
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