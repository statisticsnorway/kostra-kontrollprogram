package no.ssb.helseforetak.control.regnskap.regn0X;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlAargang extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 2, Årgang:";
    private final int YEAR = Integer.parseInt(Constants.kostraYear);
    private Vector<Integer> previousYear = new Vector<Integer>();
    private Vector<Integer> nextYear = new Vector<Integer>();
    private Vector<Integer> otherYears = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;
        try {
            int year = RecordFields.getAargangIntValue(line);

            if (year != YEAR) {
                lineHasError = true;
                if (year == (YEAR - 1)) {
                    previousYear.add(new Integer(lineNumber));
                } else if (year == (YEAR + 1)) {
                    nextYear.add(new Integer(lineNumber));
                } else {
                    otherYears.add(new Integer(lineNumber));
                }
            }
        } catch (Exception e) {
            lineHasError = true;
            otherYears.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + lf + lf;
        int numOfRecords = previousYear.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber +
                    " records tilhører fjorårets resultatregnskap." + lf;
            if (numOfRecords <= 10) {
                errorReport += "\t\t(Gjelder record nr.:";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + previousYear.elementAt(i);
                }
                errorReport += ")." + lf;
            }
        }
        numOfRecords = nextYear.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber +
                    " records tilhører neste års resultatbudsjett." + lf;
            if (numOfRecords <= 10) {
                errorReport += "\t\t(Gjelder record nr.:";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + nextYear.elementAt(i);
                }
                errorReport += ")." + lf;
            }
        }
        numOfRecords = otherYears.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber +
                    " records tilhører andre års resultatregnskap/-budsjett." + lf;
            if (numOfRecords <= 10) {
                errorReport += "\t\t(Gjelder record nr.:";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + otherYears.elementAt(i);
                }
                errorReport += ")." + lf;
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return (nextYear.size() > 0 ||
                previousYear.size() > 0 ||
                otherYears.size() > 0);
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}