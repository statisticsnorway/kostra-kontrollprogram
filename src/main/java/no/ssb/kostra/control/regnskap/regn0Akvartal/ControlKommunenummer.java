package no.ssb.kostra.control.regnskap.regn0Akvartal;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlKommunenummer extends no.ssb.kostra.control.Control {
    private Vector<Integer> invalidRegions = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;
        String knr = RecordFields.getRegion(line);

        if (!knr.equalsIgnoreCase(region)) {
            lineHasError = true;
            invalidRegions.add(new Integer(lineNumber));
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 4, kommunenummer/bydelsnummer:" + lf;
        int numOfRecords = invalidRegions.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: ukjent kommunenummer og/eller bydelsnummer i " + numOfRecords +
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
        return (invalidRegions.size() > 0 );
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}