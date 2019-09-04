package no.ssb.kostra.control.sensitiv.soshjelp;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class ControlIndividuellPlan
        extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K33: Utarbeidelse av individuell plan";
    private Vector<Integer> linesWithError = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String field_18 = RecordFields.getFieldValue(line, 18);
        try {
            int field_18_value = Integer.parseInt(field_18);
            lineHasError = !(field_18_value == 1 || field_18_value == 2);
        } catch (NumberFormatException e) {
            lineHasError = true;
        }

        if (lineHasError) {
            linesWithError.add(new Integer(lineNumber));
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf;
        int numOfRecords = linesWithError.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + " " +
                    "er det ikke krysset av for om mottakeren" + lf +
                    "\thar fått utarbeidet individuell plan. Feltet er obligatorisk. ";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + linesWithError.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return linesWithError.size() > 0;
    }

    public String getErrorText() {
        return ERROR_TEXT;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}