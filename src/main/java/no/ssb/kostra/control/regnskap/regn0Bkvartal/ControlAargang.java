package no.ssb.kostra.control.regnskap.regn0Bkvartal;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlAargang extends no.ssb.kostra.control.Control {
    private final int YEAR = Integer.parseInt(Constants.kvartalKostraYear);
    private Vector<Integer> previousYear = new Vector<>();
    private Vector<Integer> nextYear = new Vector<>();
    private Vector<Integer> otherYears = new Vector<>();

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
        String errorReport = "Kontroll 2, årgang:" + lf + lf;
        int numOfRecords = previousYear.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber +
                    " records tilhører fjorårets balanse: " + lf;
            for (int i = 0; i < previousYear.size(); i++) {
                errorReport += "\t\t Record nr. " + previousYear.get(i) + lf;
            }
        }
        numOfRecords = nextYear.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber +
                    " records tilhører neste års balanse." + lf;
            for (int i = 0; i < nextYear.size(); i++) {
                errorReport += "\t\t Record nr. " + nextYear.get(i) + lf;
            }
        }
        numOfRecords = otherYears.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber +
                    " records tilhører andre års balanse." + lf;
            for (int i = 0; i < otherYears.size(); i++) {
                errorReport += "\t\t Record nr. " + otherYears.get(i) + lf;
            }
        }
        errorReport += lf;
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