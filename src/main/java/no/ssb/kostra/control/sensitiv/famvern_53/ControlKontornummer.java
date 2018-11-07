package no.ssb.kostra.control.sensitiv.famvern_53;

/*
*/

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public final class ControlKontornummer extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K4: Kontornummer";
    private Vector<Integer> lineNumbers = new Vector<Integer>();
    private Vector<Integer> invalidKontorNumber = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = true;

        String recordKontorNumber = RecordFields.getFieldValue(line, 2);
        recordKontorNumber = recordKontorNumber.replace(' ', '0');

        List<String> kontorNumberList = Arrays.asList(
                "016", "017",
                "021", "023", "024", "026",
                "030", "037", "038", "039",
                "041", "043", "044", "045",
                "051", "052", "054",
                "061", "062", "064", "065",
                "071", "073",
                "081", "082",
                "091",
                "101",
                "111", "112",
                "121", "122", "123", "124", "125", "126", "127",
                "141", "142",
                "151", "152", "153",
                "162",
                "171", "172",
                "181", "182", "183", "184", "185",
                "191", "192", "193",
                "201", "202", "203", "204");

        lineHasError = !kontorNumberList.contains(recordKontorNumber);


        if (lineHasError) {
            invalidKontorNumber.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf + lf;
        if (foundError()) {
            int numOfRecords = lineNumbers.size();
            errorReport += "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") +
                    " Det er ikke oppgitt kontornummer, eller feil kode er benyttet.";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + lineNumbers.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return lineNumbers.size() > 0;
    }

    public String getErrorText() {
        return ERROR_TEXT;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}
