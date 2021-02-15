package no.ssb.kostra.control.regnskap.kostra;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.control.felles.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Definitions {
    public static Map<String, String> getKontoklasseAsMap(String skjema) {
        switch (skjema) {
            case "0A":
            case "0C":
                return Map.of("D", "1", "I", "0");

            case "0B":
            case "0D":
                return Map.of("B", "2");

            case "0I":
            case "0K":
            case "0M":
            case "0P":
                return Map.of("D", "3", "I", "4");

            case "0J":
            case "0L":
            case "0N":
            case "0Q":
                return Map.of("B", "5");

            default:
                return Map.of("R", " ");
        }
    }

    public static List<String> getKontoklasseAsList(String skjema) {
        return getKontoklasseAsMap(skjema).values().stream().map(String::trim).sorted().collect(Collectors.toList());
    }

    public static List<String> getFunksjonKapittelAsList(String skjema, String region) {
        // Funksjoner
        List<String> kommunaleFunkjoner = List.of(
                "100", "110", "120", "121", "130",
                "170", "171", "172", "173", "180",
                "201", "202", "211", "213", "215", "221", "222", "223", "231", "232", "233", "234", "241", "242", "243", "244",
                "251", "252", "253", "254", "256", "261", "265", "273", "275", "276", "281", "283", "285", "290",
                "301", "302", "303", "315", "320", "321", "325", "329", "330", "332", "335", "338", "339", "340", "345",
                "350", "353", "354", "355", "360", "365", "370", "373", "375", "377", "380", "381", "383", "385", "386", "390", "392", "393"
        );

        List<String> fylkeskommunaleFunksjoner = List.of(
                "400", "410", "420", "421", "430",
                "460", "465", "470", "471", "472", "473", "480",
                "510", "515", "520", "521", "522", "523", "524", "525", "526", "527", "528", "529", "530", "531", "532", "533", "534", "535", "536", "537",
                "554", "559", "561", "562", "570", "581", "590",
                "660",
                "665",
                "701", "710", "711", "715", "716", "722", "730", "731", "732", "733", "734", "735", "740",
                "750", "760", "771", "772", "775", "790"
        );

        List<String> osloFunksjoner = List.of(
                "691", "692", "693", "694", "696"
        );

        List<String> finansielleFunksjoner = List.of(
                "800", "840", "841", "850", "860", "870", "880", "899"
        );

        List<String> finansielleSbdrFunksjoner = List.of(
                "841", "850", "860", "870", "880", "899"
        );

        // Kapitler
        List<String> basisKapitler = List.of(
                "10", "11", "12", "13", "14", "15", "16", "18", "19", "20", "21", "22", "23", "24", "27", "28", "29",
                "31", "32", "33", "34", "35", "39", "40", "41", "42", "43", "45", "47", "51", "53", "55", "56", "580", "581",
                "5900", "5970", "5990",
                "9100", "9110", "9200", "9999"
        );

        List<String> regionaleKapitler = List.of(
                "17", "28", "46"
        );

        List<String> result = new ArrayList<>();

        switch (skjema) {
            // Funksjoner
            case "0A":
            case "0M":
                if ("030100".equals(region)) {
                    result.addAll(osloFunksjoner);
                    result.addAll(fylkeskommunaleFunksjoner);
                }
                result.addAll(kommunaleFunkjoner);
                result.addAll(finansielleFunksjoner);
                break;

            case "0C":
            case "0P":
                result.addAll(fylkeskommunaleFunksjoner);
                result.addAll(finansielleFunksjoner);
                break;

            case "0I":
                if ("030100".equals(region)) {
                    result.addAll(osloFunksjoner);
                    result.addAll(fylkeskommunaleFunksjoner);
                }
                result.addAll(kommunaleFunkjoner);
                result.addAll(finansielleSbdrFunksjoner);
                break;

            case "0K":
                result.addAll(fylkeskommunaleFunksjoner);
                result.addAll(finansielleSbdrFunksjoner);
                break;

            // Kapitler
            case "0B":
            case "0D":
                result.addAll(basisKapitler);
                break;

            case "0J":
            case "0L":
            case "0N":
            case "0Q":
                result.addAll(basisKapitler);
                result.addAll(regionaleKapitler);
                break;
        }

        return Utils.rpadList(result.stream().sorted().collect(Collectors.toList()), 4);
    }

    public static List<String> getArtSektorAsList(String skjema, String region) {
        List<String> osloKommuner = List.of(
                "030100",
                "030101", "030102", "030103", "030104", "030105",
                "030106", "030107", "030108", "030109", "030110",
                "030111", "030112", "030113", "030114", "030115",
                "030199"
        );

        // Arter
        List<String> basisArter = List.of(
                "010", "020", "030", "040", "050", "070", "075", "080", "089", "090", "099",
                "100", "105", "110", "114", "115", "120", "130", "140", "150", "160", "165", "170", "180", "181", "182", "183", "184", "185", "190", "195",
                "200", "209", "210", "220", "230", "240", "250", "260", "270", "280", "285",
                "300", "330", "350", "370", "375",
                "400", "429", "430", "450", "470", "475",
                "500", "501", "509", "510", "511", "512", "520", "521", "522", "529", "530", "540", "550", "570", "589", "590",
                "600", "620", "629", "630", "640", "650", "660", "670",
                "700", "710", "729", "730", "750", "770", "775",
                "810", "830", "850", "870", "874", "875", "877", "890", "895",
                "900", "901", "905", "909", "910", "911", "912", "920", "921", "922", "929", "930", "940", "950", "970", "980", "989", "990"
        );

        List<String> kommunaleArter = List.of(
                "800"
        );

        List<String> konserninterneArter = List.of(
                "380", "480", "780", "880"
        );

        List<String> osloArter = List.of(
                "298", "379", "798"
        );

        // Sektorer
        List<String> basisSektorer = List.of(
                "000", "070", "080", "110", "151", "152", "200", "320", "355", "395", "430", "450", "499", "550", "570", "610", "640", "650", "890", "900"
        );

        List<String> result = new ArrayList<>();

        if (Arrays.asList("0A", "0C", "0I", "0K", "0M", "0P").contains(skjema)) {
            result.addAll(basisArter);

            if (Arrays.asList("0A", "0C", "0M", "0P").contains(skjema)) {
                result.addAll(kommunaleArter);
            }

            if (Arrays.asList("0A", "0C", "0I", "0K").contains(skjema)) {
                result.addAll(konserninterneArter);
            }

            if (osloKommuner.contains(region)) {
                result.addAll(osloArter);
            }
        }

        if (Arrays.asList("0B", "0D", "0J", "0L", "0N", "0Q").contains(skjema)) {
            result.addAll(basisSektorer);
        }

        return result.stream().sorted().collect(Collectors.toList());
    }

    public static List<String> getSpesifikkeFunksjoner(String skjema, String region, String kontoklasse) {
        List<String> alle = getFunksjonKapittelAsList(skjema, region);
        // Kun gyldig i investering og skal fjernes fra drift
        List<String> ugyldigDrift = List.of("841");
        // Kun gyldig i drift og skal fjernes fra investering
        List<String> ugyldigInvestering = List.of("800", "840", "860");

        return Utils.rpadList(getList(kontoklasse, alle, ugyldigDrift, ugyldigInvestering).stream().sorted().collect(Collectors.toList()), 4);
    }

    public static List<String> getSpesifikkeArter(String skjema, String region, String kontoklasse) {
        List<String> alle = getArtSektorAsList(skjema, region);
        // Kun gyldig i investering og skal fjernes fra drift
        List<String> ugyldigDrift = List.of("512", "522", "529", "670", "910", "911", "912", "922", "929", "970");
        // Kun gyldig i drift og skal fjernes fra investering
        List<String> ugyldigInvestering = List.of("240", "509", "570", "590", "600", "629", "640", "800", "870", "874", "875", "877", "909", "990");

        return getList(kontoklasse, alle, ugyldigDrift, ugyldigInvestering);
    }

    private static List<String> getList(String kontoklasse, List<String> alle, List<String> ugyldigDrift, List<String> ugyldigInvestering) {
        switch (kontoklasse) {
            // Drift
            case "1":
            case "3":
                return alle.stream()
                        .filter(code -> !Comparator.isCodeInCodelist(code, ugyldigDrift))
                        .sorted()
                        .collect(Collectors.toList());
            // Investering
            case "0":
            case "4":
                return alle.stream()
                        .filter(code -> !Comparator.isCodeInCodelist(code, ugyldigInvestering))
                        .sorted()
                        .collect(Collectors.toList());

            default:
                return List.of();
        }
    }


}


