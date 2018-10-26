package no.ssb.kostra.control.sensitiv.famvern_53;

/*
*/

import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class ControlFylkesnummer extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K3: Fylkesnummer";
    private Vector<Integer> invalidRegions = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = true;

        String recordRegion = RecordFields.getFieldValue(line, 1);
        recordRegion = recordRegion.replace(' ', '0');
        if (recordRegion.equalsIgnoreCase("01") ||
                recordRegion.equalsIgnoreCase("02") ||
                recordRegion.equalsIgnoreCase("03") ||
                recordRegion.equalsIgnoreCase("04") ||
                recordRegion.equalsIgnoreCase("05") ||
                recordRegion.equalsIgnoreCase("06") ||
                recordRegion.equalsIgnoreCase("07") ||
                recordRegion.equalsIgnoreCase("08") ||
                recordRegion.equalsIgnoreCase("09") ||
                recordRegion.equalsIgnoreCase("10") ||
                recordRegion.equalsIgnoreCase("11") ||
                recordRegion.equalsIgnoreCase("12") ||
                recordRegion.equalsIgnoreCase("14") ||
                recordRegion.equalsIgnoreCase("15") ||
                recordRegion.equalsIgnoreCase("18") ||
                recordRegion.equalsIgnoreCase("19") ||
                recordRegion.equalsIgnoreCase("20") ||
                recordRegion.equalsIgnoreCase("50")
                )
            lineHasError = false;

        if (lineHasError) {
            invalidRegions.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf;
        int numOfRecords = invalidRegions.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: ukjent fylkesnummer i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + "." +
                    " Det er ikke oppgitt fylkesnummer, eller feil kode er benyttet.";
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
        return Constants.NORMAL_ERROR;
    }
}
