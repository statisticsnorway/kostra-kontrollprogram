package no.ssb.helseforetak.control.regnskap.regn0X;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlOrgNummer extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 4, Organisasjonsnummer:";
    private Vector<Integer> recordNumbers = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String orgNum = RecordFields.getOrgNummer(line);

        boolean lineHasError = !statistiskEnhet.equals(orgNum);

        if (lineHasError) {
            recordNumbers.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + lf + lf;
        int numOfRecords = recordNumbers.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: Det er benyttet feil organisasjonsnummer i " +
                    numOfRecords + " av " + totalLineNumber + " records." + lf +
                    "\tSjekk at organisasjonsnummeret er korrekt." + lf;

            if (numOfRecords <= 10) {
                errorReport += "\t\t(Gjelder record nr.:";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + recordNumbers.elementAt(i);
                }
                errorReport += ")" + lf;
            }
            errorReport += lf + lf;
        }
        return errorReport;
    }

    public boolean foundError() {
        return recordNumbers.size() > 0;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}
