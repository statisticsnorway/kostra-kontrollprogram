package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.KostraRecord;

import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("SpellCheckingInspection")
public final class ControlIntegritet {

    private ControlIntegritet() {
    }

    public static boolean controlSkjema(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Skjema",
                "Fant ugyldig skjema '(%s)'. Korrigér skjema (" + errorReport.getArgs().getSkjema() + ").",
                "skjema",
                regnskap,
                List.of(errorReport.getArgs().getSkjema()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlAargang(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Aargang",
                "Fant ugyldig årgang '(%s)'. Korrigér årgang til gjeldende årgang (" + errorReport.getArgs().getAargang() + ").",
                "aargang",
                regnskap,
                List.of(errorReport.getArgs().getAargang()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlKvartal(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Kvartal",
                "Fant ugyldig kvartal '%s'.",
                "kvartal",
                regnskap,
                List.of(errorReport.getArgs().getKvartal()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlRegion(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Region, fylkeskommune-/kommune-/bydelsnummer",
                "Fant ugyldig region '(%s)'. Korrigér region til '" + errorReport.getArgs().getRegion() + "'",
                "region",
                regnskap,
                List.of(errorReport.getArgs().getRegion()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlOrganisasjonsnummer(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Organisasjonsnummer",
                "Fant ugyldig organisasjonnummer '%s'. Korrigér organisasjonnummer til '" + errorReport.getArgs().getOrgnr() + "'",
                "orgnr",
                regnskap,
                Stream.of(errorReport.getArgs().getOrgnr().split(",")).toList(),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlForetaksnummer(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Foretaksnummer",
                "Fant ugyldig foretaksnummer '(%s)'. Korrigér foretaksnummer til '" + errorReport.getArgs().getForetaknr() + "'",
                "foretaksnr",
                regnskap,
                Stream.of(errorReport.getArgs().getForetaknr().concat(",").concat(errorReport.getArgs().getOrgnr()).split(",")).toList(),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlKontoklasse(
            final ErrorReport errorReport, final List<KostraRecord> regnskap, final List<String> kontoklasseList) {

        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Kontoklasse",
                "Fant ugyldig kontoklasse '(%s)'. Korrigér kontoklasse i henhold til KOSTRA kontoplan.",
                "kontoklasse",
                regnskap,
                kontoklasseList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlFunksjon(
            final ErrorReport errorReport, final List<KostraRecord> regnskap, final List<String> funksjonList) {

        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Funksjon",
                "Fant ugyldig funksjon '(%s)'. Korrigér funksjon i henhold til KOSTRA kontoplan.",
                "funksjon_kapittel",
                regnskap,
                funksjonList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlArt(
            final ErrorReport errorReport, final List<KostraRecord> regnskap, final List<String> artList) {

        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Art",
                "Fant ugyldig art '(%s)'. Korrigér art i henhold til KOSTRA kontoplan.",
                "art_sektor",
                regnskap,
                artList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlKapittel(
            final ErrorReport errorReport, final List<KostraRecord> regnskap, final List<String> kapittelList) {

        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Kapittel",
                "Fant ugyldig kapittel (%s) i liste",
                "funksjon_kapittel",
                regnskap,
                kapittelList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlSektor(
            final ErrorReport errorReport, final List<KostraRecord> regnskap, final List<String> sektorList) {

        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "3. Feltkontroller",
                "Kontroll Sektor",
                "Fant ugyldig sektor '(%s)'. Korrigér sektor i henhold til KOSTRA kontoplan.",
                "art_sektor",
                regnskap,
                sektorList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlBelop(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        return ControlFelt1ListeHeltall.doControl(errorReport,
                "Kontroll Beløp",
                regnskap.stream().map(record -> record.getFieldAsInteger("belop")).toList(),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlUgyldigeBelop(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        return ControlFelt1ListeUlovligTegnITallfelt.doControl(errorReport,
                "Kontroll Ugyldige beløp",
                regnskap.stream().map(record -> record.getFieldAsTrimmedString("belop")).toList(),
                Constants.CRITICAL_ERROR);
    }
}
