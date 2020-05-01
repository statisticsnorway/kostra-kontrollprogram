package no.ssb.kostra.control.regnskap.kirkekostra;

import java.util.*;
import java.util.stream.Collectors;

public class Definitions {
    public static Map<String, String> getKontoklasseAsMap(String skjema) {
        Map<String, String> map = new HashMap<>();
        switch (skjema) {
            case "0F":
                map.put("D", "3");
                map.put("I", "4");
                break;

            case "0G":
                map.put("B", "5");
                break;
        }

        return map;
    }

    public static List<String> getKontoklasseAsList(String skjema) {
        return (List<String>) getKontoklasseAsMap(skjema).values();
    }

    public static List<String> getFunksjonKapittelAsList(String skjema, String region) {
        // Funksjoner
        List<String> funksjoner = List.of(
                "041", "042", "043", "044", "045",
                "089"
        );

        // Kapitler
        List<String> kapitler = List.of(
                "10", "11", "12", "13", "18", "21", "22", "24", "27",
                "31", "32", "41", "43", "45", "51", "53", "55", "56", "580", "581",
                "5900", "5950", "5960", "5970", "5990",
                "9100", "9200", "9999"
        );

        List<String> result = new ArrayList<>();

        switch (skjema) {
            // Funksjoner
            case "0F":
                result.addAll(funksjoner);
                break;

            case "0G":
                result.addAll(kapitler);
                break;
        }

        return result.stream()
                // rightPad / legger til mellomrom på slutten av kodene slik at alle blir 4 tegn lange
                .map(c -> String.format("%1$-4s", c))
                .collect(Collectors.toList());
    }

    public static List<String> getArtSektorAsList(String skjema, String region) {
        // Arter
        List<String> arter = List.of(
                "010", "020", "030", "040", "050", "060", "080", "090", "095", "099",
                "100", "110", "120", "130", "140", "150", "155", "160", "165", "170", "180", "185", "190", "195",
                "200", "210", "220", "230", "240", "250", "260", "265", "270", "280", "285",
                "300", "305", "330", "340", "350", "370", "380", "390",
                "400", "405", "429", "430", "440", "450", "465", "470",
                "500", "510", "520", "530", "540", "550", "570", "580", "590",
                "600", "610", "620", "630", "650", "660", "670",
                "700", "705", "710", "729", "730", "740", "750", "770", "780", "790",
                "800", "805", "830", "840", "850", "860", "865", "870",
                "900", "905", "910", "920", "930", "940", "950", "970", "980", "990"
        );

        // Sektorer
        List<String> sektorer = List.of(
                "000", "070", "080", "110", "151", "152", "200", "320", "355", "395", "430", "450", "499", "550", "570", "610", "640", "650", "890", "900"
        );

        List<String> result = new ArrayList<>();

        if (List.of("0F").contains(skjema)) {
            result.addAll(arter);
        }

        if (List.of("0G").contains(skjema)) {
            result.addAll(sektorer);
        }

        return result;
    }
}
