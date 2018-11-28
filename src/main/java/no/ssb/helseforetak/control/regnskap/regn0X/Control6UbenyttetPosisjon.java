package no.ssb.helseforetak.control.regnskap.regn0X;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class Control6UbenyttetPosisjon extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 6, Ubenyttet posisjon:";
    private Vector<Integer> recordNumbers = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String field7 = RecordFields.getFieldValue(line, 7);

        boolean lineHasError = !field7.equalsIgnoreCase(" ");

        if (lineHasError) {
            recordNumbers.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + lf + lf;
        int numOfRecords = recordNumbers.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: Posisjon 32 skal rapporteres som en blank posisjon." + lf +
                    "\tI " + numOfRecords + " av " + totalLineNumber +
                    " records er posisjon 32 ikke blank." + lf;
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