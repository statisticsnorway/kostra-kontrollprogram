package no.ssb.kostra.control.sensitiv.soskval;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class ControlKommunenummer extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K3: Kommunenummer";
    private Vector<Integer> invalidRegions = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String kommunenr = RecordFields.getKommunenummer(line);

        if (!kommunenr.equalsIgnoreCase(region.substring(0, 4))) {
            lineHasError = true;
            invalidRegions.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf;
        int numOfRecords = invalidRegions.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: ukjent kommunenummer i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + ".";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + invalidRegions.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return (invalidRegions.size() > 0);
    }

    public String getErrorText() {
        return ERROR_TEXT;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}