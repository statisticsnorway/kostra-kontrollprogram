package no.ssb.helseforetak.control.regnskap.regn0X;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlRegion extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 3, Region:";

    private Vector<Integer> invalidRegions = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = (!region.equals(RecordFields.getRegion(line)));

        if (lineHasError) {
            invalidRegions.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + lf + lf;
        int numOfRecords = invalidRegions.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: ukjent regionskode i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + ".";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + invalidRegions.elementAt(i);
                }
                errorReport += ")." + lf;
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return (invalidRegions.size() > 0);
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}