package no.ssb.kostra.control.regnskap.regn0P;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlFylkeskommunenummer extends no.ssb.kostra.control.Control {
    private Vector<Integer> divergingRegions = new Vector<>();
    private String region = "";

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        this.region = region;

        String fnr = RecordFields.getRegion(line);

        if (!fnr.equalsIgnoreCase(region)) {
            lineHasError = true;
            divergingRegions.add(new Integer(lineNumber));
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 4, fylkeskommunenummer:" + lf;
        int numOfRecords = divergingRegions.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: fylkeskommunenummer i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + " avviker fra oppgitt fylkeskommunenummer (" + region + ").";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + divergingRegions.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + "\tKorreksjon: Rett fylkeskommunenummeret." + lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return (divergingRegions.size() > 0);
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}