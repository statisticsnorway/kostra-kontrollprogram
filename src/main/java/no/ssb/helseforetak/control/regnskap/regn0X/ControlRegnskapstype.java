package no.ssb.helseforetak.control.regnskap.regn0X;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlRegnskapstype extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 1, Regnskapstype:";
    private final String EXPECTED_TYPE_REGNSKAP = "0X";
    private final String NONEXPECTED_TYPE_REGNSKAP = "0Y";
    private Vector<Integer> lineNumbers = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String regnskapsType = RecordFields.getRegnskapstype(line);

        boolean lineHasError = !regnskapsType.equals(EXPECTED_TYPE_REGNSKAP);

        if (lineHasError) {
            lineNumbers.add(new Integer(lineNumber));
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + lf + lf;

        int numOfRecords = lineNumbers.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber +
                    " records inneholder ikke \"" + EXPECTED_TYPE_REGNSKAP + "\"" + lf +
                    "\ti de to første posisjonene." + lf +
                    "\tResultatregnskap skal rapporteres med " + EXPECTED_TYPE_REGNSKAP + " i de to første posisjonene i filen. " + lf +
                    "\tBalansen skal rapporteres med " + NONEXPECTED_TYPE_REGNSKAP + " i de samme posisjonene." + lf;
            if (numOfRecords <= 10) {
                errorReport += "\t\t(Gjelder record nr.:";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + lineNumbers.elementAt(i);
                }
                errorReport += ")." + lf;
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return lineNumbers.size() > 0;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}