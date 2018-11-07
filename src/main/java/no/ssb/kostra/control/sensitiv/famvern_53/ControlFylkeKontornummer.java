package no.ssb.kostra.control.sensitiv.famvern_53;

/*
 */

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.FamilievernFylkeKontornummerChecker;

import java.util.Vector;

public final class ControlFylkeKontornummer extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K5: Manglende samsvar mellom fylkes- og kontornummer";
    private Vector<Integer> lineNumbers = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String fylkesNumber = RecordFields.getRegionNr(line);
        String kontorNumber = RecordFields.getKontornummer(line);

        boolean lineHasError = !FamilievernFylkeKontornummerChecker.hasCorrectFylkeKontorNr(fylkesNumber, kontorNumber);

        if (lineHasError) {
            lineNumbers.add(new Integer(lineNumber));
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf + lf;
        if (foundError()) {
            int numOfRecords = lineNumbers.size();
            errorReport += "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") +
                    " fylkesnummer og kontornummer stemmer ikke overens.";
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
