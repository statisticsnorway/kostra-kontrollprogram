package no.ssb.kostra.control.sensitiv.soshjelp;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class ControlViktigsteKildeTilLivsopphold
        extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K14: Viktigste kilde til livsopphold. Gyldige verdier";
    private Vector<Integer> linesWithError = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String field_11 = RecordFields.getFieldValue(line, 11);
        try {
            int field_11_value = Integer.parseInt(field_11);
            lineHasError = (field_11_value < 1 || field_11_value > 9);
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
                    "er mottakerens viktigste kilde til livsopphold" + lf + "\tved siste kontakt med " +
                    "sosial-/NAV-kontoret ikke fylt ut, eller feil kode" + lf + "\ter benyttet. " +
                    "Feltet er obligatorisk.";
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