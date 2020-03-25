package no.ssb.kostra.control.regnskap.regn0Akvartal;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

final class ControlKontoklasseOgFunksjon extends no.ssb.kostra.control.Control {
    private Vector<String[]> invalidCombinations = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String kontoklasse = RecordFields.getKontoklasse(line);
        String funksjon = RecordFields.getFunksjon(line);

        if (Arrays.asList("800", "860").contains(funksjon) && !kontoklasse.equalsIgnoreCase("1")) {
            lineHasError = true;
            String[] container = {kontoklasse, funksjon, Integer.toString(lineNumber)};
            invalidCombinations.add(container);
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 8, kombinasjon kontoklasse og funksjon:" + lf + lf;
        if (foundError()) {
            int numOfRecords = invalidCombinations.size();
            errorReport += "\tFeil: Funksjonen er kun tillatt brukt i driftsregnskapet." + lf;
            for (int i = 0; i < numOfRecords; i++) {
                String[] container = invalidCombinations.elementAt(i);
                errorReport += "\t\tkontoklasse: " + container[0] +
                        " funksjon: " + container[1] + " (Record nr. " + container[2] + ")" + lf;
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