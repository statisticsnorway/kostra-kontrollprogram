package no.ssb.kostra.control.regnskap.regn0G;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlKapitler extends no.ssb.kostra.control.Control {

    private String[] validFunksjoner = {"10", "11", "12", "13", "18", "21",
            "22", "24", "27", "31", "32", "41", "43", "45", "51", "53", "55",
            "56", "580", "581", "5900", "5950", "5960", "5970", "5990", "9100",
            "9200", "9999"};

    private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String funksjon = RecordFields.getFunksjon(line);
        boolean lineHasError = !validFunksjon(funksjon);

        if (lineHasError) {
            String[] container = new String[2];
            container[0] = Integer.toString(lineNumber);
            container[1] = RecordFields.getFunksjonUncropped(line);
            invalidFunksjoner.add(container);
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 6, gyldige kapitler:" + lf + lf;
        int numOfRecords = invalidFunksjoner.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: Ukjent" + (numOfRecords == 1 ? "" : "e")
                    + (numOfRecords == 1 ? " kapittel" : " kapitler") + ":"
                    + lf;
            for (int i = 0; i < numOfRecords; i++) {
                String[] container = (String[]) invalidFunksjoner.elementAt(i);
                errorReport += "\t\tkapittel " + container[1] + " (Record nr. "
                        + container[0] + ")" + lf;
            }
        }
        errorReport += lf;
        return errorReport;
    }

    public boolean foundError() {
        return invalidFunksjoner.size() > 0;
    }

    private boolean validFunksjon(String funksjon) {
        for (int i = validFunksjoner.length - 1; i >= 0; i--) {
            if (funksjon.equalsIgnoreCase(validFunksjoner[i])) {
                return true;
            }
        }
        return false;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}
