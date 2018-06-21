package no.ssb.kostra.control.sensitiv.meklinger_55;

/*
*/

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public final class ControlFylkesnummer extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K3: Fylkesnummer";
    private Vector<Integer> invalidRegions = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String recordRegion = RecordFields.getFieldValue(line, 1).replace(' ', '0');
        List<String> fylker = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "14", "15", "16", "17", "18", "19", "20", "50");

        if (!fylker.contains(recordRegion)) {
            invalidRegions.add(new Integer(lineNumber));
            return true;
        }

        return false;
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
