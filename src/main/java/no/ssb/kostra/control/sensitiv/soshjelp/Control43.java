package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
*/

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public final class Control43 extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K43: Type vilkår det stilles til mottakeren";
    private Vector<Integer> linesWithError = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String field21 = RecordFields.getFieldValue(line, 21);
        List<Integer> fields = Arrays.asList(251, 252, 254, 256, 257, 258, 2510, 2511, 2514, 2515);
        List<String> validValues = Arrays.asList("16", "17", "04", "06", "07", "08", "10", "18", "14", "15");

        if (field21.equalsIgnoreCase("1")) {
            for (int i = 0; i < fields.size(); i++) {
                String fieldValue = RecordFields.getFieldValue(line, fields.get(i));
                fieldValue = fieldValue.replace(' ', '0');
                String validValue = validValues.get(i);

                if (!(fieldValue.equalsIgnoreCase(validValue) || fieldValue.equalsIgnoreCase("00"))) {
                    lineHasError = true;
                }
            }

            if (lineHasError) {
                linesWithError.add(new Integer(lineNumber));
            }
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf;
        int numOfRecords = linesWithError.size();
        if (numOfRecords > 0) {
            errorReport += lf
                    + "\tFeil: i "
                    + numOfRecords
                    + " record"
                    + (numOfRecords == 1 ? "" : "s")
                    + "Feltet for"
                    + lf
                    + "\t\"Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven, så skal det oppgis hvilke vilkår som stilles til mottakeren.\""
                    + lf
                    + "\tFeltet er obligatorisk ";
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