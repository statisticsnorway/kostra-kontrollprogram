package no.ssb.kostra.control.regnskap.regn0I;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by ojj on 05.11.2018.
 */
public class Control25
        extends no.ssb.kostra.control.Control {
    private Vector<String[]> invalidCombinations = new Vector<String[]>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String funksjon = RecordFields.getFunksjon(line);
        String art = RecordFields.getArt(line);

        List<String> funksjonList = Arrays.asList("899");
        List<String> artList = Arrays.asList("580", "589", "980", "989");


        if ((funksjonList.contains(funksjon) && !artList.contains(art))
                ||
                (!funksjonList.contains(funksjon) && artList.contains(art))
                ) {
            lineHasError = true;
        }

        if (lineHasError) {
            String[] container = {funksjon, art, Integer.toString(lineNumber)};
            invalidCombinations.add(container);
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 25, kombinasjon funksjon og art:" + lf + lf;
        if (foundError()) {
            int numOfRecords = invalidCombinations.size();
            errorReport += "\tFeil: Artene 580, 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 580, 589, 980 og 989." + lf;
            for (int i = 0; i < numOfRecords; i++) {
                String[] container = (String[]) invalidCombinations.elementAt(i);
                errorReport += "\t\tFunksjon: " + container[0] +
                        " art: " + container[1] + " (Record nr. " + container[2] + ")" + lf;
            }
        }
        errorReport += lf + "\tKorreksjon: Rett opp til riktig kombinasjon av funksjon og art." + lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return invalidCombinations.size() > 0;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}