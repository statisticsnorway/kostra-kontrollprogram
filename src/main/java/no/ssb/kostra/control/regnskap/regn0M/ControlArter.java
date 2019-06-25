package no.ssb.kostra.control.regnskap.regn0M;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

/*
 *
 */

final class ControlArter extends no.ssb.kostra.control.Control {
    private String[] spesielleKommuner = {
            // Kun Oslo i 2005, 2006
            "030100", "030101", "030102", "030103", "030104", "030105",
            "030106", "030107", "030108", "030109", "030110", "030111",
            "030112", "030113", "030114", "030115", "030199"};

    private String[] validArter = {"010", "020", "030", "040", "050", "070", "075", "080", "089", "090", "099",
            "100", "105", "110", "114", "115", "120", "130", "140", "150", "160", "165", "170", "180", "181", "182", "183", "184", "185", "190", "195",
            "200", "209", "210", "220", "230", "240", "250", "260", "270", "280", "285",
            "300", "330", "350", "370",
            "400", "429", "430", "450", "470", "490",
            "500", "501", "509", "510", "511", "520", "521", "529", "530", "540", "548", "550", "570", "580", "590",
            "600", "620", "629", "630", "640", "650", "660", "670", "690",
            "700", "710", "729", "730", "750", "770",
            "800", "810", "830", "850", "870", "874", "875", "877", "890",
            "900", "901", "905", "909", "910", "911", "920", "921", "929", "930", "940", "948", "950", "958", "970", "980", "990"};

    private String[] validArterForSpesielleKommuner = {"010", "020", "030",
            "040", "050", "070", "075", "080", "089", "090", "099",
            "100", "105", "110", "114", "115", "120", "130", "140", "150", "160", "165", "170", "180", "181", "182", "183", "184", "185", "190", "195",
            "200", "209", "210", "220", "230", "240", "250", "260", "270", "280", "285", "298",
            "300", "330", "350", "370", "379",
            "400", "429", "430", "450", "470", "490",
            "500", "501", "509", "510", "511", "520", "521", "529", "530", "540", "548", "550", "570", "580", "590",
            "600", "620", "629", "630", "640", "650", "660", "670", "690",
            "700", "710", "729", "730", "750", "770", "798",
            "800", "810", "830", "850", "870", "874", "875", "877", "890",
            "900", "901", "905", "909", "910", "911", "920", "921", "929", "930", "940", "948", "950", "958", "970", "980", "990"};

    private Vector<String[]> invalidArter = new Vector<String[]>();
    private Vector<String[]> invalidArterForSpesielleKommuner = new Vector<String[]>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String art = RecordFields.getArt(line);

        // Kontrollen skal ikke foretas hvis belop = 0 og art er definert.
        try {
            int belop = RecordFields.getBelopIntValue(line);
            if (belop == 0 && art.trim().length() > 0) {
                return false;
            }
        } catch (Exception e) {
            // Returnerer her ogsaa. Gir ikke mening med kontroll
            // hvis belop ikke er angitt.
            return false;
        }

        boolean lineHasErrors = false;
        if (isSpesiellKommune(region)) {
            if (!isValidArtForSpesielleKommuner(art)) {
                lineHasErrors = true;
                String[] container = new String[2];
                container[0] = Integer.toString(lineNumber);
                container[1] = art;
                invalidArterForSpesielleKommuner.add(container);
            }
        } else {
            if (!isValidArt(art)) {
                lineHasErrors = true;
                String[] container = new String[2];
                container[0] = Integer.toString(lineNumber);
                container[1] = art;
                invalidArter.add(container);
            }
        }
        return lineHasErrors;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 7, ugyldig bruk av arter:" + lf + lf;
        int numOfRecords = invalidArter.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: ugyldig" + (numOfRecords == 1 ? "" : "e")
                    + " art" + (numOfRecords == 1 ? "" : "er") + ":" + lf;
            for (int i = 0; i < numOfRecords; i++) {
                String[] container = (String[]) invalidArter.elementAt(i);
                errorReport += "\t\tart " + container[1] + " (Record nr. "
                        + container[0] + ")" + lf;
            }
        }
        numOfRecords = invalidArterForSpesielleKommuner.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: ugyldig" + (numOfRecords == 1 ? "" : "e")
                    + " art" + (numOfRecords == 1 ? "" : "er") + ":" + lf;
            for (int i = 0; i < numOfRecords; i++) {
                String[] container = (String[]) invalidArterForSpesielleKommuner
                        .elementAt(i);
                errorReport += "\t\tart " + container[1] + " (Record nr. "
                        + container[0] + ")" + lf;
            }
        }
        errorReport += "\tKorreksjon: Rett opp feil art med gyldig art. For oversikt over gyldige arter se gjeldende versjon av KOSTRA-kontoplanen."
                + lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return (invalidArter.size() > 0 || invalidArterForSpesielleKommuner
                .size() > 0);
    }

    private boolean isSpesiellKommune(String region) {
        for (int i = spesielleKommuner.length - 1; i >= 0; i--) {
            if (region.equalsIgnoreCase(spesielleKommuner[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidArt(String art) {
        for (int i = validArter.length - 1; i >= 0; i--) {
            if (art.equalsIgnoreCase(validArter[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidArtForSpesielleKommuner(String art) {
        for (int i = validArterForSpesielleKommuner.length - 1; i >= 0; i--) {
            if (art.equalsIgnoreCase(validArterForSpesielleKommuner[i])) {
                return true;
            }
        }
        return false;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}