package no.ssb.kostra.control.regnskap.regn0F;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlOrgNummer extends no.ssb.kostra.control.Control {
    private Vector<Integer> recordNumbers = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String orgNum = RecordFields.getOrgNummer(line).trim();

        boolean lineHasError = (!orgNum.equalsIgnoreCase(statistiskEnhet));

        if (lineHasError) {
            recordNumbers.add(new Integer(lineNumber));
        }

        return lineHasError;

    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 4, organisasjonsnummer:" + lf + lf;
        int numOfRecords = recordNumbers.size();
        if (numOfRecords > 0) {
            errorReport += "\t Feil: Ukjent organisasjonsnummer. " + lf +
                    "\t\t" + numOfRecords + " av " + totalLineNumber +
                    " records har et ukjent organisasjonsnummer.";
            if (numOfRecords <= 10) {
                errorReport += "(Gjelder følgende records:";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + recordNumbers.elementAt(i);
                }
                errorReport += ")";
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
