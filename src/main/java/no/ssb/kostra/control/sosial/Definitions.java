package no.ssb.kostra.control.sosial;

import java.util.List;

public class Definitions {
    private static final int AGELIMIT = 60;

    public static List<String> getBydelerAsList() {
        return List.of(
                "01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15"
        );
    }

    public static int getAgeLimit(){
        return AGELIMIT;
    }
}
