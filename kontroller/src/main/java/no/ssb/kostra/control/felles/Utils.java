package no.ssb.kostra.control.felles;

public final class Utils {

    private Utils() {
    }

    public static String replaceSpaceWithNoBreakingSpace(final String s) {
        return s.replace(" ", "&nbsp;");
    }
}
