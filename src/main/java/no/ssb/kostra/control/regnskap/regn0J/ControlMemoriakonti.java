package no.ssb.kostra.control.regnskap.regn0J;

import no.ssb.kostra.control.Constants;

/**
 * Created by ojj on 02.05.2018.
 */
final class ControlMemoriakonti extends no.ssb.kostra.control.Control {
    private final int MAX_DIFF = 10;
    private int sumForMemoriakonti = 0;

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineControlled = false;
        int belop;

        try {

            belop = RecordFields.getBelopIntValue(line);

        } catch (Exception e) {
            return lineControlled;
        }

        if (isMemoriakonti(line)) {
            lineControlled = true;
            sumForMemoriakonti += belop;
        }

        return lineControlled;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 15, Memoriakonti:" + lf + lf;

        if (Math.abs(sumForMemoriakonti) >= MAX_DIFF) {
            errorReport += "\tAdvarsel: Differanse mellom memoriakontiene og motkonto for memoriakontiene : " +
                    (sumForMemoriakonti) + lf;
        }
        errorReport += lf + "\tKorreksjon: Rett opp differansen mellom aktiva og passiva i balanseregnskapet."
                + lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return Math.abs(sumForMemoriakonti) >= MAX_DIFF;
    }


    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }

    private boolean isMemoriakonti(String line) {
        try {
            String kontoklasse = RecordFields.getKontoklasse(line);
            int funksjon = RecordFields.getFunksjonIntValue(line);
            int art = RecordFields.getArtIntValue(line);

            boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("5");
            boolean rettFunksjon = (funksjon == 9100 || funksjon == 9200 || funksjon == 9999);
            boolean rettArt = (art >= 0 && art <= 999);

            return rettKontoklasse && rettFunksjon && rettArt;
        } catch (Exception e) {
            //Maa logges!
            return false;
        }
    }
}
