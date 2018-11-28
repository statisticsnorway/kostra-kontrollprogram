package no.ssb.helseforetak.control.regnskap.regn0X;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class Control13ForetaksNummer extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 13, Kontroll av at posisjon " +
            "23 - 31 ikke er fylt ut med 000000000:";
    private Vector<Integer> recordNumbers = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String foretaksNum = RecordFields.getForetaksNummer(line);
        boolean lineHasError = foretaksNum.equalsIgnoreCase("000000000");
        if (lineHasError) {
            recordNumbers.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + lf + lf;
        int numOfRecords = recordNumbers.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: Posisjonene 23-31 skal ikke rapporteres " +
                    "som 000000000." + lf + "\tFor nærmere informasjon " +
                    "se Håndbok for rapportering av regnskapsdata for " +
                    "helseforetak og regionale helseforetak " + Constants.kostraYear + "." + lf;

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
        return Constants.NORMAL_ERROR;
    }
}
