package no.ssb.kostra.utils;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ojj on 07.11.2018.
 */
public class FamilievernFylkeKontornummerChecker {
    public static boolean hasCorrectFylkeKontorNr(String fylkesNumber, String kontorNumber) {
        Map<String, List<String>> gyldigeFylkeKontor = new HashMap<>();
        gyldigeFylkeKontor.put("01", Arrays.asList("016", "017"));
        gyldigeFylkeKontor.put("02", Arrays.asList("021", "023", "024", "026"));
        gyldigeFylkeKontor.put("03", Arrays.asList("030", "037", "038", "039"));
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
        gyldigeFylkeKontor.put("18", Arrays.asList("181", "182", "183", "184", "185"));
        gyldigeFylkeKontor.put("19", Arrays.asList("191", "192", "193", "194"));
        gyldigeFylkeKontor.put("20", Arrays.asList("201", "202", "203", "204", "205"));
        gyldigeFylkeKontor.put("50", Arrays.asList("162", "171", "172"));


        String field = fylkesNumber;
        if (gyldigeFylkeKontor.containsKey(fylkesNumber)) {
            List<String> gyldigeKontor = gyldigeFylkeKontor.get(fylkesNumber);
            if (gyldigeKontor.contains(kontorNumber)) {
                return true;
            }
        }

        return false;
    }
}
