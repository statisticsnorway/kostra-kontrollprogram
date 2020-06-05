package no.ssb.kostra.control.famvern;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Definitions {
    public static List<List<String>> getKontorFylkeRegionMappingAsList() {
        return List.of(
                List.of("017", "30", "667600"),
                List.of("023", "30", "667600"),
                List.of("024", "30", "667600"),
                List.of("025", "30", "667600"),
                List.of("027", "30", "667600"),
                List.of("030", "03", "667600"),
                List.of("037", "03", "667600"),
                List.of("038", "03", "667600"),
                List.of("039", "03", "667600"),
                List.of("045", "34", "667600"),
                List.of("052", "34", "667600"),
                List.of("061", "30", "667500"),
                List.of("065", "30", "667500"),
                List.of("071", "38", "667500"),
                List.of("073", "38", "667500"),
                List.of("081", "38", "667500"),
                List.of("082", "38", "667500"),
                List.of("091", "42", "667500"),
                List.of("101", "42", "667500"),
                List.of("111", "11", "667400"),
                List.of("112", "11", "667400"),
                List.of("125", "46", "667400"),
                List.of("127", "46", "667400"),
                List.of("141", "46", "667400"),
                List.of("142", "46", "667400"),
                List.of("151", "15", "667300"),
                List.of("152", "15", "667300"),
                List.of("153", "15", "667300"),
                List.of("162", "50", "667300"),
                List.of("171", "50", "667300"),
                List.of("172", "50", "667300"),
                List.of("181", "18", "667200"),
                List.of("183", "18", "667200"),
                List.of("184", "18", "667200"),
                List.of("185", "18", "667200"),
                List.of("192", "54", "667200"),
                List.of("193", "54", "667200"),
                List.of("194", "54", "667200"),
                List.of("202", "54", "667200"),
                List.of("203", "54", "667200"),
                List.of("205", "54", "667200")
        );
    }

    public static boolean isKontorValid(String kontor) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(r -> Objects.equals(r.get(0), kontor));
    }

    public static boolean isFylkeValid(String fylke) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(r -> Objects.equals(r.get(1), fylke));
    }

    public static boolean isRegionValid(String region) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(r -> Objects.equals(r.get(2), region));
    }

    public static boolean isRegionAndKontorValid(String region, String kontor) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(
                r -> Objects.equals(r.get(0), kontor)
                        && Objects.equals(r.get(2), region));
    }

    public static boolean isFylkeAndKontorValid(String fylke, String kontor) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(
                r -> Objects.equals(r.get(0), kontor)
                        && Objects.equals(r.get(1), fylke));
    }

    public static Map<String, String> getFylkeNavnAsList() {
        return
                Stream.of(new String[][]{
                                {"30", "Viken"},
                                {"03", "Oslo"},
                                {"34", "Innlandet"},
                                {"38", "Vestfold og Telemark"},
                                {"42", "Agder"},
                                {"11", "Rogaland"},
                                {"46", "Vestland"},
                                {"15", "Møre og Romsdal"},
                                {"50", "Trøndelag"},
                                {"18", "Nordland"},
                                {"54", "Troms og Finnmark"}
                        }
                ).collect(Collectors.collectingAndThen(
                        Collectors.toMap(data -> data[0], data -> data[1]),
                        Collections::unmodifiableMap));
    }
}
