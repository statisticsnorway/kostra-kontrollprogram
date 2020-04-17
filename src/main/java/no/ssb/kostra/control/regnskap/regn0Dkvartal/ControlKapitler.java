package no.ssb.kostra.control.regnskap.regn0Dkvartal;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

final class ControlKapitler extends no.ssb.kostra.control.Control {
    private List<String> validFunksjoner = Arrays.asList(
            // VIKTIG: kapittel inneholder posisjonen for kontoklasse også, slik
            // at kapittel blir 5 posisjoner lang!
            "210", "211", "212", "213", "214", "215", "216", "218", "219",
            "220", "221", "222", "223", "224", "227", "229",
            "231", "232", "233", "234", "235", "239",
            "240", "241", "242", "243", "245", "247",
            "251", "253", "255", "256",
            "2580", "2581",
            "25900", "25950", "25960", "25970", "25990",
            "29100", "29110", "29200", "29999",
            "z", "Z", "~"
    );

    private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String funksjon = RecordFields.getFunksjon(line);

        // Kontrollen skal ikke foretas hvis belop = 0 og funksjon er definert.
        try {
            int belop = RecordFields.getBelopIntValue(line);
            if (belop == 0 && funksjon.trim().length() > 0) {
                return false;
            }
        } catch (Exception e) {
            // Returnerer her ogsaa. Gir ikke mening med kontroll
            // hvis belop ikke er angitt.
            return false;
        }

        boolean lineHasError = !validFunksjoner.contains(funksjon);

        if (lineHasError) {
            String[] container = {Integer.toString(lineNumber), funksjon};
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
                String[] container = invalidFunksjoner.elementAt(i);
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

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}
