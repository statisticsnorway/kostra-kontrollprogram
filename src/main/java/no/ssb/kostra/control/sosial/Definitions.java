package no.ssb.kostra.control.sosial;

import java.util.List;

public class Definitions {
    public static List<String> getBydelerAsList(String region) {
        switch (region) {
            case "0301":
                return List.of(
                        "01", "02", "03", "04", "05",
                        "06", "07", "08", "09", "10",
                        "11", "12", "13", "14", "15"
                );
            default:
                return List.of("  ");
        }
    }
}
