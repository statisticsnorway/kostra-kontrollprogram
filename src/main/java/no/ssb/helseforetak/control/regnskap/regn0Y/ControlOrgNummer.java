package no.ssb.helseforetak.control.regnskap.regn0Y;


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
        StringBuilder errorReport = new StringBuilder(ERROR_TEXT + lf + lf);
        int numOfRecords = recordNumbers.size();
        if (numOfRecords > 0) {
            errorReport.append("\tFeil: Det er benyttet feil organisasjonsnummer i " +
                    numOfRecords + " av " + totalLineNumber + " records." + lf +
                    "\tSjekk at organisasjonsnummeret er korrekt." + lf);

            if (numOfRecords <= 10) {
                errorReport.append("\t\t(Gjelder record nr.:");
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport.append(" " + recordNumbers.elementAt(i));
                }
                errorReport.append(")" + lf);
            }
            errorReport.append(lf + lf);
        }
        return errorReport.toString();
    }

    public boolean foundError() {
        return recordNumbers.size() > 0;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}
