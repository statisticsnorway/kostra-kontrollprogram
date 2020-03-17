package no.ssb.kostra.utils;

public final class FamilievernKontorNrChecker {

    public static boolean hasCorrectKontorNr(String regionNr, String kontorNr) {
        try {
            int kontorNrNumVal = Integer.parseInt(kontorNr);
            switch (regionNr){
                case "667600": return between(kontorNrNumVal, 17, 52);
                case "667500": return between(kontorNrNumVal, 61, 101);
                case "667400": return between(kontorNrNumVal, 111, 142);
                case "667300": return between(kontorNrNumVal, 151, 172);
                case "667200": return between(kontorNrNumVal, 181, 205);
                // Vi har ugyldig regionnr.
                default: return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean between(int theNumber, int lowerLimit, int upperLimit){
        return lowerLimit <= theNumber && theNumber <= upperLimit;
    }
}
