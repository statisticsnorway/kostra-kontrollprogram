package no.ssb.kostra.control.regnskap.felles;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.control.felles.ControlFelt1ListeInneholderKodeFraKodeliste;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.Record;

import java.util.List;
import java.util.stream.Collectors;

import static no.ssb.kostra.control.felles.Comparator.between;
import static no.ssb.kostra.control.felles.Comparator.isCodeInCodelist;

public class ControlRegnskap {
    public static int getSumUtgifter(Arguments arguments, List<Record> regnskap, List<String> regnskapList, String kontoklasse) {
        if (isCodeInCodelist(arguments.getSkjema(), regnskapList)) {
            return regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 10, 590))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
        }

        return 0;
    }

    public static int getSumInntekter(Arguments arguments, List<Record> regnskap, List<String> regnskapList, String kontoklasse) {
        if (isCodeInCodelist(arguments.getSkjema(), regnskapList)) {
            return regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 600, 990))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
        }

        return 0;
    }

    public static int getSumTilskudd(List<Record> regnskap) {
        return regnskap.stream()
                .filter(p -> p.getFieldAsString("art_sektor").equalsIgnoreCase("830"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);
    }

    public static int getSumAktiva(Arguments arguments, List<Record> regnskap, List<String> regnskapList, String kontoklasse) {
        if (isCodeInCodelist(arguments.getSkjema(), regnskapList)) {
            return regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && between(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 10, 28))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
        }

        return 0;
    }

    public static int getSumPassiva(Arguments arguments, List<Record> regnskap, List<String> regnskapList, String kontoklasse) {
        if (isCodeInCodelist(arguments.getSkjema(), regnskapList)) {
            return regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse)
                            && between(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 31, 5990))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
        }

        return 0;
    }

    // Kombinasjonskontroller, per record
    public static boolean controlKombinasjonKontoklasseArt(ErrorReport er, List<Record> regnskap, List<String> regnskapList, String kontoklasse, List<String> artList, String formattedControlText, int errorType) {
        if (isCodeInCodelist(er.getArgs().getSkjema(), regnskapList)) {
            List<String> investeringsRecordsArter = regnskap.stream()
                    .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse))
                    .map(record -> record.getFieldAsString("art_sektor"))
                    .collect(Collectors.toList());

            return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                    "5. Kombinasjonskontroller",
                    "Kombinasjon kontoklasse og art",
                    formattedControlText,
                    investeringsRecordsArter,
                    artList,
                    errorType
            );
        }

        return false;
    }

    // Kombinasjonskontroller, per record
    public static boolean controlKombinasjonKontoklasseFunksjon(ErrorReport er, List<Record> regnskap, List<String> regnskapList, String kontoklasse, List<String> funksjonList, String formattedControlText, int errorType) {
        if (isCodeInCodelist(er.getArgs().getSkjema(), regnskapList)) {
            List<String> investeringsRecordsArter = regnskap.stream()
                    .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(kontoklasse))
                    .map(record -> record.getFieldAsString("funksjon_kapittel"))
                    .collect(Collectors.toList());

            return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                    "5. Kombinasjonskontroller",
                    "Kombinasjon kontoklasse og funksjon",
                    formattedControlText,
                    investeringsRecordsArter,
                    funksjonList,
                    errorType
            );
        }

        return false;
    }

    public static boolean controlKombinasjonFunksjonArt(ErrorReport er, List<Record> regnskap, List<String> funksjonList, List<String> artList, String formattedControlText, int errorType) {
        List<String> records = regnskap.stream()
                .filter(record -> isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), funksjonList))
                .map(record -> record.getFieldAsString("art_sektor"))
                .collect(Collectors.toList());

        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "5. Kombinasjonskontroller",
                "Kombinasjon funksjon og art",
                formattedControlText,
                records,
                artList,
                errorType
        );
    }

    public static boolean controlKombinasjonArtFunksjon(ErrorReport er, List<Record> regnskap, List<String> artList, List<String> funksjonList, String formattedControlText, int errorType) {
        List<String> investeringsRecordsArter = regnskap.stream()
                .filter(record -> isCodeInCodelist(record.getFieldAsString("art_sektor"), artList))
                .map(record -> record.getFieldAsString("funksjon_kapittel"))
                .collect(Collectors.toList());

        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "5. Kombinasjonskontroller",
                "Kombinasjon art og funksjon",
                formattedControlText,
                investeringsRecordsArter,
                funksjonList,
                errorType
        );
    }


}
