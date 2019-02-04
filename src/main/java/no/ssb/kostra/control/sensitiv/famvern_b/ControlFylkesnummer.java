package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * 
 */

import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class ControlFylkesnummer extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K3: Regionsnummer";
    private Vector<Integer> invalidRegions = new Vector<Integer>();
    private Vector<Integer> divergingRegions = new Vector<Integer>();
    private String region = "";

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;
        String recordRegion = RecordFields.getRegionNr(line);

        if (
                !recordRegion.equalsIgnoreCase("667200") &&
                        !recordRegion.equalsIgnoreCase("667300") &&
                        !recordRegion.equalsIgnoreCase("667400") &&
                        !recordRegion.equalsIgnoreCase("667500") &&
                        !recordRegion.equalsIgnoreCase("667600")
                ) {
            lineHasError = true;
            invalidRegions.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf;
        int numOfRecords = invalidRegions.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: ukjent regionsnummer i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + ".";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + invalidRegions.elementAt(i);
                }
                errorReport += ").";
            }
        }
        numOfRecords = divergingRegions.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: regionsnummer i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + " avviker fra oppgitt " +
                    "regionsnummer (" + region + ")." + lf + "\tKontroller at riktig regionsnummer " +
                    "ble oppgitt til konverteringsprogrammet." + lf + "\tHvis dette stemmer, må " +
                    "regionsnummer i recorden (filuttrekket) rettes.";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + divergingRegions.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return (invalidRegions.size() > 0 ||
                divergingRegions.size() > 0);
    }

    public String getErrorText() {
        return ERROR_TEXT;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}
