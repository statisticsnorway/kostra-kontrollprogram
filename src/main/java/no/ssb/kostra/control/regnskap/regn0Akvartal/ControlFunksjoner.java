package no.ssb.kostra.control.regnskap.regn0Akvartal;

import no.ssb.kostra.control.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

final class ControlFunksjoner extends no.ssb.kostra.control.Control {
    private List<String> spesielleKommuner = Arrays.asList("030100", "500100");

    private List<String> osloKommuner = Arrays.asList("030100");

    private List<String> validFunksjonerKommune = Arrays.asList(
            "100", "110", "120", "121", "130", "170", "171", "172", "173", "180",
            "201", "202", "211", "213", "215", "221", "222", "223", "231", "232", "233", "234", "241", "242", "243", "244",
            "251", "252", "253", "254", "255", "256", "261", "265", "273", "275", "276", "281", "283", "285", "290",
            "301", "302", "303", "315", "320", "321", "325", "329", "330", "332", "335", "338", "339", "340", "345",
            "350", "353", "354", "355", "357", "360", "365", "370", "373",
            "375", "377", "380", "381", "383", "385", "386", "390", "392", "393",
            "800", "840", "841", "850", "860", "870", "880", "899",
            "z", "Z"
    );

    private List<String> validFunksjonerFylkeskommune = Arrays.asList(
            "400", "410", "420", "421", "430", "460", "465", "470", "471", "472", "473", "480",
            "510", "515", "520", "521", "522", "523", "524", "525",
            "526", "527", "528", "529", "530", "531", "532", "533", "534", "535", "536", "537",
            "554", "559", "561", "562", "570", "581", "590",
            "660", "665",
            "700", "705", "710", "711", "715", "716", "722", "730", "731", "732", "733", "734", "735", "740",
            "750", "760", "771", "772", "775", "790"
    );

    private List<String> validFunksjonerOslo = Arrays.asList(
            "691", "692", "693", "694", "696"
    );

    private Vector<String[]> invalidFunksjoner = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

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

        List<String> validFunksjoner = new ArrayList<>(validFunksjonerKommune);

        if (spesielleKommuner.contains(region)){
            validFunksjoner.addAll(validFunksjonerFylkeskommune);
        }

        if (osloKommuner.contains(region)){
            validFunksjoner.addAll(validFunksjonerOslo);
        }


        if (!validFunksjoner.contains(funksjon)){
            lineHasError = true;
            String[] container = new String[2];
            container[0] = Integer.toString(lineNumber);
            container[1] = funksjon;
            invalidFunksjoner.add(container);
        }

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 6, gyldige funksjoner:" + lf + lf;
        int numOfRecords = invalidFunksjoner.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: Ukjent" + (numOfRecords == 1 ? "" : "e")
                    + " funksjon" + (numOfRecords == 1 ? "" : "er") + ":" + lf;
            for (int i = 0; i < numOfRecords; i++) {
                String[] container = invalidFunksjoner.elementAt(i);
                errorReport += "\t\tfunksjon " + container[1] + " (Record nr. "
                        + container[0] + ")" + lf;
            }
        }
        errorReport += lf;
        return errorReport;
    }

    public boolean foundError() {
        return (invalidFunksjoner.size() > 0);
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}