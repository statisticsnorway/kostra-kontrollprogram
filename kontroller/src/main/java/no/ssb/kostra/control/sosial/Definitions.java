package no.ssb.kostra.control.sosial;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public final class Definitions {
    private Definitions() {
    }

    public static List<String> getBydelerAsList(String region) {
        return "0301".equals(region)
                ? List.of(
                "01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15")
                : List.of("  ");
    }
}
