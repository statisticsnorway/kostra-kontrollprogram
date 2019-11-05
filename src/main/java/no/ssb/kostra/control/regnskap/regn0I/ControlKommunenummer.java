package no.ssb.kostra.control.regnskap.regn0I;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlKommunenummer extends no.ssb.kostra.control.Control {
    private Vector<Integer> divergingRegions = new Vector<>();
    private String region = "";

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        this.region = region;

        String knr = RecordFields.getRegion(line);

        if (!knr.equalsIgnoreCase(region)) {
            lineHasError = true;
            divergingRegions.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 4, kommunenummer:" + lf;
        int numOfRecords = divergingRegions.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: ukjent kommunenummer i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + ".";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + divergingRegions.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + "\tKorreksjon: Rett kommunenummeret." + lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return (divergingRegions.size() > 0);
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}
