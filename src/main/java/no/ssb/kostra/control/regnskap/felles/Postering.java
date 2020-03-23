package no.ssb.kostra.control.regnskap.felles;

public class Postering {
    private final int linjenummer;
    private final String skjema;
    private final String aargang;
    private final String kvartal;
    private final String region;
    private final String orgnr;
    private final String foretaknr;
    private final String kontoklasse;
    private final String funksjonKapittel;
    private final String artSektor;
    private final int belop;
    private static int linje;

    public Postering(String record) {
        skjema = record.substring(0, 2);
        aargang = record.substring(2, 6);
        kvartal = record.substring(6, 7);
        region = record.substring(7, 13);
        orgnr = record.substring(13, 22);
        foretaknr = record.substring(22, 31);
        kontoklasse = record.substring(31, 32);
        funksjonKapittel = record.substring(32, 36);
        artSektor = record.substring(36, 39);
        belop = Integer.parseInt(record.substring(39, 48));
        this.linjenummer = ++linje;
    }

    public int getLinjenummer() {
        return linjenummer;
    }

    public String getSkjema() {
        return skjema;
    }

    public String getAargang() {
        return aargang;
    }

    public String getKvartal() {
        return kvartal;
    }

    public String getRegion() {
        return region;
    }

    public String getOrgnr() {
        return orgnr;
    }

    public String getForetaknr() {
        return foretaknr;
    }

    public String getKontoklasse() {
        return kontoklasse.trim();
    }

    public String getFunksjonKapittel() {
        return funksjonKapittel.trim();
    }

    public String getArtSektor() {
        return artSektor.trim();
    }

    public int getBelop() {
        return belop;
    }

    public int getKontoklasseIntValue() {
        try {
            return Integer.valueOf(getKontoklasse());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getFunksjonKapittelIntValue() {
        try {
            return Integer.valueOf(getFunksjonKapittel());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getArtSektorIntValue() {
        try {
            return Integer.valueOf(getArtSektor());
        } catch (NumberFormatException e) {
            return 0;
        }

    }
}
