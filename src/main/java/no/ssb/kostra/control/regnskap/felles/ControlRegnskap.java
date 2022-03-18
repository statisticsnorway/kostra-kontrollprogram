package no.ssb.kostra.control.regnskap.felles;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.control.felles.ControlFelt1ListeInneholderKodeFraKodeliste;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.KostraRecord;

import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.between;
import static no.ssb.kostra.control.felles.Comparator.isCodeInCodeList;

@SuppressWarnings("SpellCheckingInspection")
public class ControlRegnskap {

    public static int getSumUtgifter(
            final Arguments arguments, final List<KostraRecord> regnskap,
            final List<String> regnskapList, final String kontoklasse) {

        if (isCodeInCodeList(arguments.getSkjema(), regnskapList)) {
            return regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 10, 590))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
        }
        return 0;
    }

    public static int getSumInntekter(
            final Arguments arguments, final List<KostraRecord> regnskap,
            final List<String> regnskapList, final String kontoklasse) {

        if (isCodeInCodeList(arguments.getSkjema(), regnskapList)) {
            return regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 600, 990))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
        }
        return 0;
    }

    // TODO: Not in use
    public static int getSumTilskudd(final List<KostraRecord> regnskap) {
        return regnskap.stream()
                .filter(p -> p.getFieldAsString("art_sektor").equalsIgnoreCase("830"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);
    }

    public static int getSumAktiva(
            final Arguments arguments, final List<KostraRecord> regnskap,
            final List<String> regnskapList, final String kontoklasse) {

        if (isCodeInCodeList(arguments.getSkjema(), regnskapList)) {
            return regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && between(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 10, 29))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
        }

        return 0;
    }

    public static int getSumPassiva(
            final Arguments arguments, final List<KostraRecord> regnskap,
            final List<String> regnskapList, final String kontoklasse) {

        if (isCodeInCodeList(arguments.getSkjema(), regnskapList)) {
            return regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && between(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 31, 5990))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
        }
        return 0;
    }

    // Kombinasjonskontroller, per record
    public static boolean controlKombinasjonKontoklasseArt(
            final ErrorReport errorReport, final List<KostraRecord> regnskap,
            final List<String> regnskapList, final String kontoklasse,
            final List<String> artList, final String formattedControlText, final int errorType) {

        if (isCodeInCodeList(errorReport.getArgs().getSkjema(), regnskapList)) {
            final var filteredRegnskap = regnskap.stream()
                    .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && isCodeInCodeList(record.getFieldAsString("art_sektor"), artList)
                    )
                    .toList();

            return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                    "5. Kombinasjonskontroller",
                    "Kombinasjon kontoklasse og art",
                    formattedControlText,
                    "art_sektor",
                    filteredRegnskap,
                    artList,
                    errorType
            );
        }
        return false;
    }

    public static boolean controlKombinasjonFunksjonArt(
            final ErrorReport errorReport, final List<KostraRecord> regnskap,
            final List<String> funksjonList, final List<String> artList,
            final String formattedControlText, final int errorType) {

        final var filteredRegnskap = regnskap.stream()
                .filter(record -> isCodeInCodeList(record.getFieldAsString("funksjon_kapittel"), funksjonList)
                        && isCodeInCodeList(record.getFieldAsString("art_sektor"), artList)
                )
                .toList();

        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(errorReport,
                "5. Kombinasjonskontroller",
                "Kombinasjon funksjon og art",
                formattedControlText,
                "art_sektor",
                filteredRegnskap,
                artList,
                errorType
        );
    }
}

