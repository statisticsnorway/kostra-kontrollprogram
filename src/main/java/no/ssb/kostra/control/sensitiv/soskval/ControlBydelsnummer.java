package no.ssb.kostra.control.sensitiv.soskval;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by ojj on 16.01.2019.
 */
public class ControlBydelsnummer extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K3a: Bydelsnummer";
    private Vector<Integer> invalidRegions = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String kommunenr = RecordFields.getKommunenummer(line);
        String bydelnr = RecordFields.getBydelsnummer(line);
        List<String> koder = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "99");

        if (!region.substring(0, 4).equals("0301")) {
            return lineHasError;
        }

        if (!kommunenr.substring(0, 4).equals("0301")) {
            lineHasError = true;
            invalidRegions.add(new Integer(lineNumber));
            return lineHasError;
        }

        if (region.substring(4, 6).equalsIgnoreCase("00")) {
            if (!koder.contains(bydelnr)) {
                lineHasError = true;
                invalidRegions.add(new Integer(lineNumber));
            }

        } else {
            if (!region.substring(4, 6).equalsIgnoreCase(bydelnr)) {
                lineHasError = true;
                invalidRegions.add(new Integer(lineNumber));
            }
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