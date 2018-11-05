package no.ssb.kostra.control.regnskap.regn0C;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

/**
 * Created by ojj on 05.11.2018.
 */
public class Control23
        extends no.ssb.kostra.control.Control

{
    private Vector<String[]> invalidCombinations = new Vector<String[]>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String funksjon = no.ssb.kostra.control.regnskap.regn0C.RecordFields.getFunksjon(line);
        String art = no.ssb.kostra.control.regnskap.regn0C.RecordFields.getArt(line);

        if (funksjon.equalsIgnoreCase("899") || art.equalsIgnoreCase("580") || art.equalsIgnoreCase("980")) {
            if (funksjon.equalsIgnoreCase("899")) {
                if (!art.equalsIgnoreCase("580") && !art.equalsIgnoreCase("980")) {
                    lineHasError = true;
                }
            } else if (art.equalsIgnoreCase("580") || art.equalsIgnoreCase("980")) {
                if (!funksjon.equalsIgnoreCase("899")) {
                    lineHasError = true;
                }
            }
        }

        if (lineHasError) {
            String[] container = {funksjon, art, Integer.toString(lineNumber)};
            invalidCombinations.add(container);
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 24, kombinasjon funksjon og art:" + lf + lf;
        if (foundError()) {
            int numOfRecords = invalidCombinations.size();
            errorReport += "\tFeil: Artene 580 og 980 er kun tillat brukt i kombinasjon med funksjon 899. Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 580 og 980." + lf;
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