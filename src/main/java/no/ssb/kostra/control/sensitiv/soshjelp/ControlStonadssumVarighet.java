package no.ssb.kostra.control.sensitiv.soshjelp;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class ControlStonadssumVarighet extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K29: Har stønadssum men mangler varighet";
    private Vector<Integer> linesWithError = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean field_14isFilled = ControlStonadsmaaneder.isFilledField14(line);
        int field_15_1, field_15_2;

        try {
            field_15_1 = Integer.parseInt(RecordFields.getFieldValue(line, 151));
        } catch (NumberFormatException e) {
            field_15_1 = 0;
        }

        try {
            field_15_2 = Integer.parseInt(RecordFields.getFieldValue(line, 152));
        } catch (NumberFormatException e) {
            field_15_2 = 0;
        }

        boolean lineHasError = ((field_15_1 + field_15_2) > 0) && !field_14isFilled;


        if (lineHasError)
            linesWithError.add(new Integer(lineNumber));

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf;
        int numOfRecords = linesWithError.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s.") + lf +
                    "\tMottakeren har fått i økonomisk sosialhjelp (bidrag eller lån) " + lf +
                    "\ti løpet av året, men mangler utfylling for hvilke måneder " +
                    "i løpet av året" + lf + "\tmottakeren har mottatt økonomisk stønad.";
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