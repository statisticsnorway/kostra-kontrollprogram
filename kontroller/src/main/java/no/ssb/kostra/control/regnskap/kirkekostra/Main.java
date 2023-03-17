package no.ssb.kostra.control.regnskap.kirkekostra;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.control.felles.ControlDubletter;
import no.ssb.kostra.control.felles.ControlFelt1ListeBlank;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.control.felles.Utils;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.KostraRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static no.ssb.kostra.control.felles.Comparator.between;
import static no.ssb.kostra.control.felles.Comparator.isCodeInCodeList;
import static no.ssb.kostra.control.felles.Comparator.outsideOf;
import static no.ssb.kostra.control.felles.Comparator.removeCodesFromCodeList;
import static no.ssb.kostra.control.felles.ControlIntegritet.*;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.controlKombinasjonFunksjonArt;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.controlKombinasjonKontoklasseArt;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.getSumAktiva;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.getSumInntekter;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.getSumPassiva;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.getSumUtgifter;


@SuppressWarnings("SpellCheckingInspection")
public class Main {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Definitions
    public static Map<String, String> getKontoklasseAsMap(final String skjema) {
        return switch (skjema) {
            case "0F" -> Map.of("D", "3", "I", "4");
            case "0G" -> Map.of("B", "5");
            default -> Map.of("X", "9");
        };
    }

    public static List<String> getKontoklasseAsList(final String skjema) {
        return getKontoklasseAsMap(skjema).values().stream()
                .map(String::trim)
                .sorted()
                .toList();
    }

    public static List<String> getFunksjonKapittelAsList(final String skjema) {
        // Funksjoner
        final var funksjoner = List.of(
                "041", "042", "043", "044", "045",
                "089"
        );

        // Kapitler
        final var kapitler = List.of(
                "10", "11", "12", "13", "18",
                "21", "22", "24", "27",
                "31", "32",
                "41", "43", "45",
                "51", "53", "55", "56", "580", "581", "5900", "5950", "5960", "5970", "5990",
                "9100", "9200", "9999"
        );

        final var result = new ArrayList<String>();

        switch (skjema) {
            // Funksjoner
            case "0F" -> result.addAll(funksjoner);
            case "0G" -> result.addAll(kapitler);
        }

        return result.stream()
                // rightPad / legger til mellomrom på slutten av kodene slik at alle blir 4 tegn lange
                .map(c -> String.format("%1$-4s", c))
                .sorted()
                .toList();
    }

    public static List<String> getArtSektorAsList(final String skjema) {
        // Arter
        final var arter = List.of(
                "010", "020", "030", "040", "050", "060", "080", "090", "095", "099",
                "100", "110", "120", "130", "140", "150", "155", "160", "165", "170", "180", "185", "190", "195",
                "200", "210", "220", "230", "240", "250", "260", "265", "270", "280", "285",
                "300", "305", "330", "340", "350", "370", "380", "390",
                "400", "405", "429", "430", "440", "450", "465", "470",
                "500", "510", "520", "530", "540", "550", "570", "580", "590",
                "600", "610", "620", "630", "650", "660", "670",
                "700", "705", "710", "729", "730", "740", "750", "770", "780", "790",
                "800", "805", "830", "840", "850", "860", "865", "870",
                "900", "905", "910", "920", "930", "940", "950", "970", "980", "990"
        );

        // Sektorer
        final var sektorer = List.of("   ");

        final var result = new ArrayList<String>();

        if (Objects.equals("0F", skjema)) {
            result.addAll(arter);
        }

        if (Objects.equals("0G", skjema)) {
            result.addAll(sektorer);
        }

        return result.stream()
                .sorted()
                .toList();
    }

    public static List<String> getArterUgyldigDrift() {
        // Kun gyldig i investering og skal fjernes fra drift
        return List.of("280", "285", "670", "910", "970");
    }

    public static List<String> getArterUgyldigInvestering() {
        // Kun gyldig i drift og skal fjernes fra investering
        return List.of("570", "590", "990");
    }

    public static List<String> getBevilgningRegnskapList() {
        return List.of("0F");
    }

    public static List<String> getBalanseRegnskapList() {
        return List.of("0G");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Controls
    public static ErrorReport doControls(final Arguments args) {
        final var errorReport = new ErrorReport(args);
        final var list1 = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        final var hasErrors = ControlRecordLengde.doControl(list1, errorReport, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return errorReport;
        }

        final var fieldDefinitions = Utils.mergeFieldDefinitionsAndArguments(FieldDefinitions.getFieldDefinitions(), args);
        final var regnskap1 = Utils.addLineNumbering(
                Utils.getValidRecords(list1, fieldDefinitions)
        );

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap1, errorReport);

        if (errorReport.getErrorType() == Constants.CRITICAL_ERROR) {
            return errorReport;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Feltkontroller
        controlSkjema(errorReport, regnskap1);
        controlAargang(errorReport, regnskap1);
        controlKvartal(errorReport, regnskap1);
        controlRegion(errorReport, regnskap1);
        controlOrganisasjonsnummer(errorReport, regnskap1);
        controlForetaksnummer(errorReport, regnskap1);
        controlKontoklasse(errorReport, regnskap1, getKontoklasseAsList(args.getSkjema()));

        if (isCodeInCodeList(args.getSkjema(), getBevilgningRegnskapList())) {
            controlFunksjon(errorReport, regnskap1, getFunksjonKapittelAsList(args.getSkjema()));
            controlArt(errorReport, regnskap1, getArtSektorAsList(args.getSkjema()));
        }

        if (isCodeInCodeList(args.getSkjema(), getBalanseRegnskapList())) {
            controlKapittel(errorReport, regnskap1, getFunksjonKapittelAsList(args.getSkjema()));
            controlSektor(errorReport, regnskap1, getArtSektorAsList(args.getSkjema()));
        }

        controlBelop(errorReport, regnskap1);
        controlUgyldigeBelop(errorReport, regnskap1);

        // Fjerner posteringer der beløp = 0
        final var regnskap = Utils.removeBelopEquals0(regnskap1);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Dublett-, kombinasjon-, summerings-,  kontroller
        if (isCodeInCodeList(args.getSkjema(), getBevilgningRegnskapList())) {
            ControlDubletter.doControl(regnskap, errorReport, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"), List.of("kontoklasse", "funksjon", "art"));

            controlKombinasjonKontoklasseArt(errorReport,
                    regnskap,
                    getBevilgningRegnskapList(),
                    getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"),
                    removeCodesFromCodeList(getArtSektorAsList(errorReport.getArgs().getSkjema()), getArterUgyldigInvestering()),
                    "Korrigér art (%s) til gyldig art i investeringsregnskapet, eller overfør posteringen til driftsregnskapet",
                    Constants.CRITICAL_ERROR
            );

            controlKombinasjonKontoklasseArt(errorReport,
                    regnskap,
                    getBevilgningRegnskapList(),
                    getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"),
                    removeCodesFromCodeList(getArtSektorAsList(errorReport.getArgs().getSkjema()), getArterUgyldigDrift()),
                    "Korrigér art (%s) til gyldig art i driftsregnskapet, eller overfør posteringen til investeringsregnskapet",
                    Constants.CRITICAL_ERROR
            );
            controlSumInvesteringsDifferanse(errorReport, regnskap);
            controlSumDriftsUtgifter(errorReport, regnskap);
            controlSumDriftsInntekter(errorReport, regnskap);
            controlSumDriftsDifferanse(errorReport, regnskap);
            controlSumInterneOverforinger(errorReport, regnskap);
            controlSumKalkulatoriskeUtgifterInntekter(errorReport, regnskap);
            controlInterneOverforinger(errorReport, regnskap);
            controlOverforinger(errorReport, regnskap);
            controlAvskrivninger(errorReport, regnskap);

            final var funksjon089Arter = getArtSektorAsList(errorReport.getArgs().getSkjema())
                    .stream()
                    .mapToInt(Integer::valueOf)
                    .filter(i -> between(i, 500, 580)
                            || i == 830
                            || between(i, 900, 980)
                    )
                    .mapToObj(String::valueOf)
                    .toList();

            controlKombinasjonFunksjonArt(errorReport,
                    regnskap,
                    List.of("089 "),
                    funksjon089Arter,
                    "Korrigér i fila slik at art (%s) er gyldig mot funksjon 089. Gyldige arter er: " + funksjon089Arter + ".",
                    Constants.CRITICAL_ERROR);
        }

        if (isCodeInCodeList(args.getSkjema(), getBalanseRegnskapList())) {
            ControlDubletter.doControl(regnskap, errorReport, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"), List.of("kontoklasse", "kapittel", "sektor"));
            controlSumAktiva(errorReport, regnskap);
            controlSumPassiva(errorReport, regnskap);
            controlSumBalanseDifferanse(errorReport, regnskap);
        }

        return errorReport;
    }


    public static boolean controlSumInvesteringsDifferanse(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        // 95) Sum investering = Sum investeringsutgifter + Sum investeringsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();
        final var sumInvesteringsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));
        final var sumInvesteringsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));
        final var sumDifferanse = sumInvesteringsUtgifter + sumInvesteringsInntekter;

        if (!outsideOf(sumDifferanse, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller", "Kontroll Summeringskontroller investeringsregnskapet, differanse i investeringsregnskapet", " ", " "
                , "Korrigér differansen (" + sumDifferanse + ") mellom inntekter (" + sumInvesteringsInntekter + ") og utgifter (" + sumInvesteringsUtgifter + ") i investeringsregnskapet"
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumDriftsUtgifter(final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        // 100) Driftsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
        final var arguments = errorReport.getArgs();
        final var sumDriftsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));

        if (!isCodeInCodeList(arguments.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))
                || 0 < sumDriftsUtgifter) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller", "Kontroll Summeringskontroller bevilgningsregnskap, utgiftsposteringer i driftsregnskapet", " ", " "
                , "Korrigér slik at fila inneholder utgiftsposteringene (" + sumDriftsUtgifter + ") i driftsregnskapet"
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumDriftsInntekter(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        // 105) Driftsregnskapet må ha inntektsføringer, dvs. være mindre enn 0
        final var arguments = errorReport.getArgs();
        final var sumDriftsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));

        if (!isCodeInCodeList(arguments.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))
                || sumDriftsInntekter < 0) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller", "Kontroll Summeringskontroller bevilgningsregnskap, inntektsposteringer i driftsregnskapet", " ", " "
                , "Korrigér slik at fila inneholder inntektsposteringene (" + sumDriftsInntekter + ") i driftsregnskapet"
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumDriftsDifferanse(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        // 110) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();
        final var sumDriftsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));
        final var sumDriftsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));
        final var sumDifferanse = sumDriftsUtgifter + sumDriftsInntekter;

        if (!outsideOf(sumDifferanse, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller", "Kontroll Summeringskontroller bevilgningsregnskap, differanse i driftsregnskapet", " ", " "
                , "Korrigér differansen (" + sumDifferanse + ") mellom inntekter (" + sumDriftsInntekter + ") og utgifter (" + sumDriftsUtgifter + ") i driftsregnskapet"
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumAktiva(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        // 115) Balanse må ha føring på aktiva, dvs. være høyere enn 0
        final var arguments = errorReport.getArgs();
        final var sumAktiva = getSumAktiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));

        if (0 < sumAktiva) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller", "Kontroll Summeringskontroller balanseregnskap, registrering av aktiva (Eiendeler)", " ", " "
                , "Korrigér slik at fila inneholder registrering av aktiva/eiendeler (" + sumAktiva + ") i balanse."
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumPassiva(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        // 120) Balanse må ha føring på passiva, dvs. være mindre enn 0
        final var arguments = errorReport.getArgs();
        final var sumPassiva = getSumPassiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));

        if (sumPassiva < 0) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller", "Balanseregnskap", " ", " "
                , "Kontroll Summeringskontroller balanseregnskap, registrering av passiva (Gjeld og egenkapital)"
                , "Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital (" + sumPassiva + ") i balanse."
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumBalanseDifferanse(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        // 125) Aktiva skal være lik passiva. Differanser opptil ±10' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();
        final var sumAktiva = getSumAktiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));
        final var sumPassiva = getSumPassiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));
        final var sumDifferanse = sumAktiva + sumPassiva;

        if (!outsideOf(sumDifferanse, -10, 10)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller", "Balanseregnskap", " ", " "
                , "Kontroll Summeringskontroller balanseregnskap, differanse"
                , "Korrigér differansen (" + sumDifferanse + ") mellom "
                + "aktiva (" + sumAktiva + ") og "
                + "passiva (" + sumPassiva + ") i fila (Differanser opptil ±10' godtas)"
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumInterneOverforinger(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        // Kontroll Interne overføringer
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        final var sumInternKjop = regnskap.stream()
                .filter(p -> Objects.equals("380", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumInternSalg = regnskap.stream()
                .filter(p -> Objects.equals("780", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumInternKjopOgSalg = sumInternKjop + sumInternSalg;

        if (!Comparator.outsideOf(sumInternKjopOgSalg, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller",
                "Kontroll Interne overføringer, internkjøp og internsalg",
                " ",
                " ",
                "Korrigér i fila slik at differansen (" + sumInternKjopOgSalg + ") mellom internkjøp (" + sumInternKjop + ") og internsalg (" + sumInternSalg + ")  stemmer overens (margin på +/- 30')",
                "",
                Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumKalkulatoriskeUtgifterInntekter(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        final var sumKalkulatoriskeUtgifter = regnskap.stream()
                .filter(p -> Objects.equals("390", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumKalkulatoriskeInntekter = regnskap.stream()
                .filter(p -> Objects.equals("790", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumKalkulatoriske = sumKalkulatoriskeUtgifter + sumKalkulatoriskeInntekter;

        if (!Comparator.outsideOf(sumKalkulatoriske, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller",
                "Kontroll Interne overføringer, kalkulatoriske utgifter og inntekter",
                " ",
                " ",
                "Korrigér i fila slik at differansen (" + sumKalkulatoriske + ") mellom kalkulatoriske utgifter (" + sumKalkulatoriskeUtgifter + ") og inntekter (" + sumKalkulatoriskeInntekter + ")ved kommunal tjenesteytelse stemmer overens (margin på +/- 30')",
                "",
                Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlInterneOverforinger(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        final var sumOverforinger = regnskap.stream()
                .filter(p -> Objects.equals("465", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumInnsamledeMidler = regnskap.stream()
                .filter(p -> Objects.equals("865", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumMidler = sumOverforinger + sumInnsamledeMidler;

        if (!Comparator.outsideOf(sumMidler, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller",
                "Kontroll Interne overføringer, midler",
                " ",
                " ",
                "Korrigér i fila slik at differansen (" + sumMidler + ") mellom overføringer av midler (" + sumOverforinger + ") og innsamlede midler (" + sumInnsamledeMidler + ") stemmer overens (margin på +/- 30')",
                "",
                Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlOverforinger(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        // Kontroll Overføring mellom drifts- og investeringsregnskap
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        final var sumDriftsoverforinger = regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                        && p.getFieldAsString("art_sektor").equalsIgnoreCase("570")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumInvesteringsoverforinger = regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                        && p.getFieldAsString("art_sektor").equalsIgnoreCase("970")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumDifferanse = sumDriftsoverforinger + sumInvesteringsoverforinger;

        if (!Comparator.outsideOf(sumDifferanse, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller",
                "Kontroll Overføring mellom drifts- og investeringsregnskap",
                " ",
                " ",
                "Korrigér i fila slik at differansen (" + sumDifferanse + ") i overføringer mellom drifts- (" + sumDriftsoverforinger + ") og investeringsregnskapet (" + sumInvesteringsoverforinger + ") stemmer overens",
                "",
                Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlAvskrivninger(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        // Kontroll Avskrivninger
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        // 1) Art 590 for sum funksjoner D.041-D.089 + art 990 for sum funksjoner D.041-D.089 = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
        final var sumAvskrivninger590 = regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                        && Objects.equals("590", p.getFieldAsString("art_sektor"))
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumMotpostAvskrivninger990 = regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                        && Objects.equals("990", p.getFieldAsString("art_sektor"))
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        final var sumAvskrivninger = sumAvskrivninger590 + sumMotpostAvskrivninger990;

        if (!Comparator.outsideOf(sumAvskrivninger, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                "6. Summeringskontroller",
                "Kontroll Avskrivninger, art 590, art 990",
                " ",
                " ",
                "Korrigér i fila slik at differansen (" + sumAvskrivninger + ") mellom art 590 (" + sumAvskrivninger590 + ") stemmer overens med art 990 (" + sumMotpostAvskrivninger990 + ") (margin på +/- 30')",
                "",
                Constants.CRITICAL_ERROR));
        return true;
    }
}

