package no.ssb.kostra.control.regnskap.regn0Ckvartal;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

final class ControlKontoklasse extends no.ssb.kostra.control.Control {
    private Vector<String[]> invalidKontoklasser = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;
        String kontoklasse = RecordFields.getKontoklasse(line);

        if (!Arrays.asList("0", "1").contains(kontoklasse)) {
            lineHasError = true;
            String[] container = {kontoklasse, Integer.toString(lineNumber)};
            invalidKontoklasser.add(container);
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        StringBuilder errorReport = new StringBuilder("Kontroll 5, kontoklasse:" + lf + lf);
        if (foundError()) {
            String[] container;

            int numOfRecords = invalidKontoklasser.size();
            errorReport.append("\tFeil: ukjent kontoklasse i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + "." + lf);

            for (int i = 0; i < numOfRecords; i++) {
                container = invalidKontoklasser.elementAt(i);
                errorReport.append("\t\tkontoklasse " + container[0] + " (Record nr. " + container[1] + ")" + lf);
            }

        }
        errorReport.append(lf + lf);
        return errorReport.toString();
    }

    public boolean foundError() {
        return invalidKontoklasser.size() > 0;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}