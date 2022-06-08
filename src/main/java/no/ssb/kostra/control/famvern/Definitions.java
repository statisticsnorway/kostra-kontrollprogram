package no.ssb.kostra.control.famvern;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Definitions {
    private Definitions() {
        throw new IllegalStateException("Static class");
    }


    public static List<List<String>> getKontorFylkeRegionMappingAsList() {
        String region6 = "667600";
        String region5 = "667500";
        String region4 = "667400";
        String region3 = "667300";
        String region2 = "667200";

        return List.of(
                List.of("017", "30", region6),
                List.of("023", "30", region6),
                List.of("024", "30", region6),
                List.of("025", "30", region6),
                List.of("027", "30", region6),
                List.of("030", "03", region6),
                List.of("037", "03", region6),
                List.of("038", "03", region6),
                List.of("039", "03", region6),
                List.of("046", "34", region6),
                List.of("047", "34", region6),
                List.of("052", "34", region6),
                List.of("061", "30", region5),
                List.of("065", "30", region5),
                List.of("071", "38", region5),
                List.of("073", "38", region5),
                List.of("081", "38", region5),
                List.of("082", "38", region5),
                List.of("091", "42", region5),
                List.of("101", "42", region5),
                List.of("111", "11", region4),
                List.of("112", "11", region4),
                List.of("125", "46", region4),
                List.of("127", "46", region4),
                List.of("141", "46", region4),
                List.of("142", "46", region4),
                List.of("151", "15", region3),
                List.of("152", "15", region3),
                List.of("153", "15", region3),
                List.of("162", "50", region3),
                List.of("171", "50", region3),
                List.of("172", "50", region3),
                List.of("181", "18", region2),
                List.of("183", "18", region2),
                List.of("184", "18", region2),
                List.of("185", "18", region2),
                List.of("192", "54", region2),
                List.of("193", "54", region2),
                List.of("194", "54", region2),
                List.of("202", "54", region2),
                List.of("203", "54", region2),
                List.of("205", "54", region2)
        );
    }

    public static List<String> getFieldAsList(final int index) {
        return getKontorFylkeRegionMappingAsList().stream()
                .map(l -> l.get(index))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .keySet()
                .stream()
                .sorted()
                .toList();
    }

    public static List<String> getKontorAsList() {
        return getFieldAsList(0);
    }

    public static List<String> getFylkeAsList() {
        return getFieldAsList(1);
    }

    public static List<String> getRegionAsList() {
        return getFieldAsList(2);
    }

    public static boolean isKontorValid(final String kontor) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(r -> Objects.equals(r.get(0), kontor));
    }

    public static boolean isFylkeValid(final String fylke) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(r -> Objects.equals(r.get(1), fylke));
    }

    public static boolean isRegionValid(final String region) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(r -> Objects.equals(r.get(2), region));
    }

    public static boolean isRegionAndKontorValid(final String region, final String kontor) {
        return getKontorFylkeRegionMappingAsList().stream().anyMatch(
                r -> Objects.equals(r.get(0), kontor)
                        && Objects.equals(r.get(2), region));
    }

    public static boolean isFylkeAndKontorValid(final String fylke, final String kontor) {
        return getKontorFylkeRegionMappingAsList().stream()
                .anyMatch(r -> Objects.equals(r.get(0), kontor) && Objects.equals(r.get(1), fylke));
    }

}
