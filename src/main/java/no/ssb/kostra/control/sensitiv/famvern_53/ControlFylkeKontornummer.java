package no.ssb.kostra.control.sensitiv.famvern_53;

/*
 */

import no.ssb.kostra.control.Constants;

import java.util.*;

public final class ControlFylkeKontornummer extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K5: Manglende samsvar mellom fylkes- og kontornummer";
    private Vector<Integer> lineNumbers = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        Map<String, List<String>> gyldigeFylkeKontor = new HashMap<>();
        gyldigeFylkeKontor.put("01", Arrays.asList("016", "017"));
        gyldigeFylkeKontor.put("02", Arrays.asList("021", "023", "024", "026"));
        gyldigeFylkeKontor.put("03", Arrays.asList("037", "038", "039"));
        gyldigeFylkeKontor.put("04", Arrays.asList("041", "043", "044", "045"));
        gyldigeFylkeKontor.put("05", Arrays.asList("051", "052", "054"));
        gyldigeFylkeKontor.put("06", Arrays.asList("061", "062", "064", "065"));
        gyldigeFylkeKontor.put("07", Arrays.asList("071", "073"));
        gyldigeFylkeKontor.put("08", Arrays.asList("081", "082"));
        gyldigeFylkeKontor.put("09", Arrays.asList("091"));
        gyldigeFylkeKontor.put("10", Arrays.asList("101"));
        gyldigeFylkeKontor.put("11", Arrays.asList("111", "112"));
        gyldigeFylkeKontor.put("12", Arrays.asList("121", "122", "123", "124", "125", "126", "127"));
        gyldigeFylkeKontor.put("14", Arrays.asList("141", "142"));
        gyldigeFylkeKontor.put("15", Arrays.asList("151", "152", "153"));
        gyldigeFylkeKontor.put("16", Arrays.asList("162"));
        gyldigeFylkeKontor.put("17", Arrays.asList("171", "172"));
        gyldigeFylkeKontor.put("18", Arrays.asList("181", "182", "183", "184", "185"));
        gyldigeFylkeKontor.put("19", Arrays.asList("191", "192", "193", "194"));
        gyldigeFylkeKontor.put("20", Arrays.asList("201", "202", "203", "204", "205"));

        String fylkesNumber = RecordFields.getRegionNr(line);
        String kontorNumber = RecordFields.getKontornummer(line);

        String field = fylkesNumber;
        if (gyldigeFylkeKontor.containsKey(fylkesNumber)){
            List<String> gyldigeKontor = gyldigeFylkeKontor.get(fylkesNumber);
            if (gyldigeKontor.contains(kontorNumber)){
                return false;
            }
        }

        lineNumbers.add(new Integer(lineNumber));

        return true;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf + lf;
        if (foundError()) {
            int numOfRecords = lineNumbers.size();
            errorReport += "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") +
                    " fylkesnummer og kontornummer stemmer ikke overens.";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + lineNumbers.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return lineNumbers.size() > 0;
    }

    public String getErrorText() {
        return ERROR_TEXT;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}
