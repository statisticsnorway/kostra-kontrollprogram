package no.ssb.kostra.control.regnskap.regn0J;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;

final class ControlMemoriakonti extends no.ssb.kostra.control.Control {
    private final int MAX_DIFF = 10;
    private int sumForControl = 0;

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineControlled = false;

        try {
            String kontoklasse = RecordFields.getKontoklasse(line);
            int funksjon = RecordFields.getFunksjonIntValue(line);
            int art = RecordFields.getArtIntValue(line);

            boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("5");
            boolean rettFunksjon = Arrays.asList(9100, 9110, 9200, 9999).contains(funksjon);
            boolean rettArt = (art >= 0 && art <= 990);

            if (rettKontoklasse && rettFunksjon && rettArt) {
                sumForControl += RecordFields.getBelopIntValue(line);
                lineControlled = true;
            }

        } catch (Exception e) {

            // Exception her er sannsynligvis NumberFormatException.
            // Det er da OK at lineControlled = false;
        }

        return lineControlled;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 15, Memoriakonti:" + lf + lf;

        if (foundError()) {
            errorReport += "\tAdvarsel: Differanse mellom memoriakonti og motkonto for memoriakonti: " +
                    (sumForControl) + "'";
            errorReport += lf + "\tKorreksjon: Rett opp differansen mellom aktiva og passiva i balanseregnskapet.";
        }
        errorReport += lf + lf;

        return errorReport;
    }

    public boolean foundError() {
        return Math.abs(sumForControl) >= MAX_DIFF;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}