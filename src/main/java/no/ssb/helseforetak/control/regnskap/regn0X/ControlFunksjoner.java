package no.ssb.helseforetak.control.regnskap.regn0X;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlFunksjoner extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 7, Funksjoner, gyldige:";

    private String[] validFunksjoner =
            {
                    "400", "460", "600", "606", "620", "630", "636", "637", "641", "642", "651", "681", "840"
            };

    private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String funksjon = RecordFields.getFunksjon(line);

        if (!validFunksjon(funksjon)) {
            lineHasError = true;
            String[] container = new String[2];
            container[0] = Integer.toString(lineNumber);
            container[1] = funksjon;
            invalidFunksjoner.add(container);
        }
        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        StringBuilder errorReport = new StringBuilder(ERROR_TEXT + lf + lf);
        int numOfRecords = invalidFunksjoner.size();
        if (numOfRecords > 0) {
            errorReport.append("\tFeil: Ukjent" + (numOfRecords == 1 ? "" : "e") +
                    " funksjonskode" + (numOfRecords == 1 ? "" : "r"));
            if (numOfRecords <= 10) {
                errorReport.append(":" + lf);
                for (int i = 0; i < numOfRecords; i++) {
                    String[] container = (String[]) invalidFunksjoner.elementAt(i);
                    errorReport.append("\t\tfunksjon " + container[1] +
                            " (Record nr. " + container[0] + ")" + (container[1].length() == 4 ? " NB! Funksjonskodene skal rapporteres med en blank posisjon til slutt." : "") + lf);
                }
            } else {
                errorReport.append("." + lf + "\t\tGjelder " + numOfRecords + " records.");
            }

        }
        errorReport.append(lf + lf);
        return errorReport.toString();
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