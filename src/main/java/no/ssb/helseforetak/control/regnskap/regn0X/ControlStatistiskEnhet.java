package no.ssb.helseforetak.control.regnskap.regn0X;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlStatistiskEnhet extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 21, Org.nummer - samsvar mellom preutfylt og filuttrekk:";
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
            errorReport += "\tFeil: Org.nummeret i filuttrekket samsvarer ikke med org.nummeret som ble " + lf +
                    "\tfylt ut ved innlogging i portalen eller preutfylt org.nummer i skjemaet." + lf +
                    "\tKorreksjon: Korriger org.nummer." + lf;
//          numOfRecords + " av " + totalLineNumber + " records." + lf +
//          "\tSjekk at statistiskEnhetstatistiskEnhetstatistiskEnhetstatistiskEnhet er korrekt." + lf;

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
