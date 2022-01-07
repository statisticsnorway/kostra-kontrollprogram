package no.ssb.kostra.control.regnskap.kirkekostra;

import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static no.ssb.kostra.control.felles.Comparator.*;
import static no.ssb.kostra.control.felles.ControlIntegritet.*;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.*;


public class Main {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Definitions
    public static Map<String, String> getKontoklasseAsMap(String skjema) {
        switch (skjema) {
            case "0F":
                return Map.of("D", "3", "I", "4");

            case "0G":
                return Map.of("B", "5");

            default:
                return Map.of("X", "9");
        }
    }

    public static List<String> getKontoklasseAsList(String skjema) {
        return getKontoklasseAsMap(skjema).values().stream().map(String::trim).sorted().collect(Collectors.toList());
    }

    public static List<String> getFunksjonKapittelAsList(String skjema) {
        // Funksjoner
        List<String> funksjoner = List.of(
                "041", "042", "043", "044", "045",
                "089"
        );

        // Kapitler
        List<String> kapitler = List.of(
                "10", "11", "12", "13", "18",
                "21", "22", "24", "27",
                "31", "32",
                "41", "43", "45",
                "51", "53", "55", "56", "580", "581", "5900", "5950", "5960", "5970", "5990",
                "9100", "9200", "9999"
        );

        List<String> result = new ArrayList<>();

        switch (skjema) {
            // Funksjoner
            case "0F":
                result.addAll(funksjoner);
                break;

            case "0G":
                result.addAll(kapitler);
                break;
        }

        return result.stream()
                // rightPad / legger til mellomrom på slutten av kodene slik at alle blir 4 tegn lange
                .map(c -> String.format("%1$-4s", c))
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<String> getArtSektorAsList(String skjema) {
        // Arter
        List<String> arter = List.of(
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
        List<String> sektorer = List.of(
                "   "
        );

        List<String> result = new ArrayList<>();

        if (Objects.equals("0F", skjema)) {
            result.addAll(arter);
        }

        if (Objects.equals("0G", skjema)) {
            result.addAll(sektorer);
        }

        return result.stream().sorted().collect(Collectors.toList());
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
    public static ErrorReport doControls(Arguments args) {
        ErrorReport errorReport = new ErrorReport(args);
        List<String> list1 = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(list1, errorReport, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return errorReport;
        }

        List<FieldDefinition> fieldDefinitions = Utils.mergeFieldDefinitionsAndArguments(FieldDefinitions.getFieldDefinitions(), args);
        List<Record> regnskap1 = Utils.addLineNumbering(
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

        if (isCodeInCodelist(args.getSkjema(), getBevilgningRegnskapList())) {
            controlFunksjon(errorReport, regnskap1, getFunksjonKapittelAsList(args.getSkjema()));
            controlArt(errorReport, regnskap1, getArtSektorAsList(args.getSkjema()));
        }

        if (isCodeInCodelist(args.getSkjema(), getBalanseRegnskapList())) {
            controlKapittel(errorReport, regnskap1, getFunksjonKapittelAsList(args.getSkjema()));
            controlSektor(errorReport, regnskap1, getBalanseRegnskapList());
        }

        controlBelop(errorReport, regnskap1);
        controlUgyldigeBelop(errorReport, regnskap1);

        // Fjerner posteringer der beløp = 0
        List<Record> regnskap = Utils.removeBelopEquals0(regnskap1);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Dublett-, kombinasjon-, summerings-,  kontroller
        if (isCodeInCodelist(args.getSkjema(), getBevilgningRegnskapList())) {
            ControlDubletter.doControl(regnskap, errorReport, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"), List.of("kontoklasse", "funksjon", "art"));

            controlKombinasjonKontoklasseArt(errorReport,
                    regnskap,
                    getBevilgningRegnskapList(),
                    getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"),
                    removeCodesFromCodelist(getArtSektorAsList(errorReport.getArgs().getSkjema()), getArterUgyldigInvestering()),
                    "Korrigér art (%s) til gyldig art i investeringsregnskapet, eller overfør posteringen til driftsregnskapet",
                    Constants.CRITICAL_ERROR
            );

            controlKombinasjonKontoklasseArt(errorReport,
                    regnskap,
                    getBevilgningRegnskapList(),
                    getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"),
                    removeCodesFromCodelist(getArtSektorAsList(errorReport.getArgs().getSkjema()), getArterUgyldigDrift()),
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

            List<String> funksjon089Arter = getArtSektorAsList(errorReport.getArgs().getSkjema())
                    .stream()
                    .mapToInt(Integer::valueOf)
                    .filter(i -> between(i, 500, 580)
                            || i == 830
                            || between(i, 900, 980)
                    )
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());

            controlKombinasjonFunksjonArt(errorReport,
                    regnskap,
                    List.of("089 "),
                    funksjon089Arter,
                    "Korrigér i fila slik at art (%s) er gyldig mot funksjon 089. Gyldige arter er: " + funksjon089Arter + ".",
                    Constants.CRITICAL_ERROR
            );
        }

        if (isCodeInCodelist(args.getSkjema(), getBalanseRegnskapList())) {
            ControlDubletter.doControl(regnskap, errorReport, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"), List.of("kontoklasse", "kapittel", "sektor"));
            controlSumAktiva(errorReport, regnskap);
            controlSumPassiva(errorReport, regnskap);
            controlSumBalanseDifferanse(errorReport, regnskap);
        }

        return errorReport;
    }


    public static boolean controlSektor(ErrorReport er, List<Record> regnskap, List<String> balanseRegnskapList) {
        if (isCodeInCodelist(er.getArgs().getSkjema(), balanseRegnskapList)) {
            return ControlFelt1ListeBlank.doControl(er,
                    "3. Feltkontroller",
                    "Sektor",
                    regnskap.stream().map(record -> record.getFieldAsTrimmedString("art_sektor")).collect(Collectors.toList()),
                    Constants.CRITICAL_ERROR);
        }

        return false;
    }

    public static boolean controlSumInvesteringsDifferanse(ErrorReport errorReport, List<Record> regnskap) {
        // 95) Sum investering = Sum investeringsutgifter + Sum investeringsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();
        int sumInvesteringsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));
        int sumInvesteringsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));
        int sumDifferanse = sumInvesteringsUtgifter + sumInvesteringsInntekter;

        if (outsideOf(sumDifferanse, -30, 30)) {
            errorReport.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller", "Kontroll Summeringskontroller investeringsregnskapet, differanse i investeringsregnskapet", " ", " "
                    , "Korrigér differansen (" + sumDifferanse + ") mellom inntekter (" + sumInvesteringsInntekter + ") og utgifter (" + sumInvesteringsUtgifter + ") i investeringsregnskapet"
                    , ""
                    , Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }

    public static boolean controlSumDriftsUtgifter(ErrorReport errorReport, List<Record> regnskap) {
        // 100) Driftsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
        Arguments arguments = errorReport.getArgs();
        int sumDriftsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));

        if (isCodeInCodelist(arguments.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))) {
            if (!(0 < sumDriftsUtgifter)) {
                errorReport.addEntry(new ErrorReportEntry(
                        "6. Summeringskontroller", "Kontroll Summeringskontroller bevilgningsregnskap, utgiftsposteringer i driftsregnskapet", " ", " "
                        , "Korrigér slik at fila inneholder utgiftsposteringene (" + sumDriftsUtgifter + ") i driftsregnskapet"
                        , ""
                        , Constants.CRITICAL_ERROR
                ));
                return true;
            }
        }

        return false;
    }

    public static boolean controlSumDriftsInntekter(ErrorReport errorReport, List<Record> regnskap) {
        // 105) Driftsregnskapet må ha inntektsføringer, dvs. være mindre enn 0
        Arguments arguments = errorReport.getArgs();
        int sumDriftsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));

        if (isCodeInCodelist(arguments.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))) {
            if (!(sumDriftsInntekter < 0)) {
                errorReport.addEntry(new ErrorReportEntry(
                        "6. Summeringskontroller", "Kontroll Summeringskontroller bevilgningsregnskap, inntektsposteringer i driftsregnskapet", " ", " "
                        , "Korrigér slik at fila inneholder inntektsposteringene (" + sumDriftsInntekter + ") i driftsregnskapet"
                        , ""
                        , Constants.CRITICAL_ERROR
                ));

                return true;
            }
        }

        return false;
    }

    public static boolean controlSumDriftsDifferanse(ErrorReport errorReport, List<Record> regnskap) {
        // 110) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();
        int sumDriftsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));
        int sumDriftsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));
        int sumDifferanse = sumDriftsUtgifter + sumDriftsInntekter;

        if (outsideOf(sumDifferanse, -30, 30)) {
            errorReport.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller", "Kontroll Summeringskontroller bevilgningsregnskap, differanse i driftsregnskapet", " ", " "
                    , "Korrigér differansen (" + sumDifferanse + ") mellom inntekter (" + sumDriftsInntekter + ") og utgifter (" + sumDriftsUtgifter + ") i driftsregnskapet"
                    , ""
                    , Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }

    public static boolean controlSumAktiva(ErrorReport errorReport, List<Record> regnskap) {
        // 115) Balanse må ha føring på aktiva, dvs. være høyere enn 0
        Arguments arguments = errorReport.getArgs();
        int sumAktiva = getSumAktiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));

        if (!(0 < sumAktiva)) {
            errorReport.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller", "Kontroll Summeringskontroller balanseregnskap, registrering av aktiva (Eiendeler)", " ", " "
                    , "Korrigér slik at fila inneholder registrering av aktiva/eiendeler (" + sumAktiva + ") i balanse."
                    , ""
                    , Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }

    public static boolean controlSumPassiva(ErrorReport errorReport, List<Record> regnskap) {
        // 120) Balanse må ha føring på passiva, dvs. være mindre enn 0
        Arguments arguments = errorReport.getArgs();
        int sumPassiva = getSumPassiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));

        if (!(sumPassiva < 0)) {
            errorReport.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller", "Balanseregnskap", " ", " "
                    , "Kontroll Summeringskontroller balanseregnskap, registrering av passiva (Gjeld og egenkapital)"
                    , "Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital (" + sumPassiva + ") i balanse."
                    , Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }

    public static boolean controlSumBalanseDifferanse(ErrorReport errorReport, List<Record> regnskap) {
        // 125) Aktiva skal være lik passiva. Differanser opptil ±10' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();
        int sumAktiva = getSumAktiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));
        int sumPassiva = getSumPassiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));
        int sumDifferanse = sumAktiva + sumPassiva;

        if (outsideOf(sumDifferanse, -10, 10)) {
            errorReport.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller", "Balanseregnskap", " ", " "
                    , "Kontroll Summeringskontroller balanseregnskap, differanse"
                    , "Korrigér differansen (" + sumDifferanse + ") mellom "
                    + "aktiva (" + sumAktiva + ") og "
                    + "passiva (" + sumPassiva + ") i fila (Differanser opptil ±10' godtas)"
                    , Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;

    }

    public static boolean controlSumInterneOverforinger(ErrorReport errorReport, List<Record> regnskap) {
        // Kontroll Interne overføringer
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        int sumInternKjop = regnskap.stream()
                .filter(p -> Objects.equals("380", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumInternSalg = regnskap.stream()
                .filter(p -> Objects.equals("780", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumInternKjopOgSalg = sumInternKjop + sumInternSalg;

        if (Comparator.outsideOf(sumInternKjopOgSalg, -30, 30)) {
            errorReport.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller",
                    "Kontroll Interne overføringer, internkjøp og internsalg",
                    " ",
                    " ",
                    "Korrigér i fila slik at differansen (" + sumInternKjopOgSalg + ") mellom internkjøp (" + sumInternKjop + ") og internsalg (" + sumInternSalg + ")  stemmer overens (margin på +/- 30')",
                    "",
                    Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }

    public static boolean controlSumKalkulatoriskeUtgifterInntekter(ErrorReport errorReport, List<Record> regnskap) {
        int sumKalkulatoriskeUtgifter = regnskap.stream()
                .filter(p -> Objects.equals("390", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumKalkulatoriskeInntekter = regnskap.stream()
                .filter(p -> Objects.equals("790", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumKalkulatoriske = sumKalkulatoriskeUtgifter + sumKalkulatoriskeInntekter;

        if (Comparator.outsideOf(sumKalkulatoriske, -30, 30)) {
            errorReport.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller",
                    "Kontroll Interne overføringer, kalkulatoriske utgifter og inntekter",
                    " ",
                    " ",
                    "Korrigér i fila slik at differansen (" + sumKalkulatoriske + ") mellom kalkulatoriske utgifter (" + sumKalkulatoriskeUtgifter + ") og inntekter (" + sumKalkulatoriskeInntekter + ")ved kommunal tjenesteytelse stemmer overens (margin på +/- 30')",
                    "",
                    Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }

    public static boolean controlInterneOverforinger(ErrorReport er, List<Record> regnskap) {
        int sumOverforinger = regnskap.stream()
                .filter(p -> Objects.equals("465", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumInnsamledeMidler = regnskap.stream()
                .filter(p -> Objects.equals("865", p.getFieldAsString("art_sektor")))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumMidler = sumOverforinger + sumInnsamledeMidler;

        if (Comparator.outsideOf(sumMidler, -30, 30)) {
            er.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller",
                    "Kontroll Interne overføringer, midler",
                    " ",
                    " ",
                    "Korrigér i fila slik at differansen (" + sumMidler + ") mellom overføringer av midler (" + sumOverforinger + ") og innsamlede midler (" + sumInnsamledeMidler + ") stemmer overens (margin på +/- 30')",
                    "",
                    Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }

    public static boolean controlOverforinger(ErrorReport er, List<Record> regnskap) {
        // Kontroll Overføring mellom drifts- og investeringsregnskap
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        int sumDriftsoverforinger = regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                        && p.getFieldAsString("art_sektor").equalsIgnoreCase("570")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumInvesteringsoverforinger = regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                        && p.getFieldAsString("art_sektor").equalsIgnoreCase("970")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumDifferanse = sumDriftsoverforinger + sumInvesteringsoverforinger;

        if (Comparator.outsideOf(sumDifferanse, -30, 30)) {
            er.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller",
                    "Kontroll Overføring mellom drifts- og investeringsregnskap",
                    " ",
                    " ",
                    "Korrigér i fila slik at differansen (" + sumDifferanse + ") i overføringer mellom drifts- (" + sumDriftsoverforinger + ") og investeringsregnskapet (" + sumInvesteringsoverforinger + ") stemmer overens",
                    "",
                    Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }

    public static boolean controlAvskrivninger(ErrorReport er, List<Record> regnskap) {
        // Kontroll Avskrivninger
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        // 1) Art 590 for sum funksjoner D.041-D.089 + art 990 for sum funksjoner D.041-D.089 = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
        int sumAvskrivninger590 = regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                        && Objects.equals("590", p.getFieldAsString("art_sektor"))
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumMotpostAvskrivninger990 = regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                        && Objects.equals("990", p.getFieldAsString("art_sektor"))
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);

        int sumAvskrivninger = sumAvskrivninger590 + sumMotpostAvskrivninger990;

        if (Comparator.outsideOf(sumAvskrivninger, -30, 30)) {
            er.addEntry(new ErrorReportEntry(
                    "6. Summeringskontroller",
                    "Kontroll Avskrivninger, art 590, art 990",
                    " ",
                    " ",
                    "Korrigér i fila slik at differansen (" + sumAvskrivninger + ") mellom art 590 (" + sumAvskrivninger590 + ") stemmer overens med art 990 (" + sumMotpostAvskrivninger990 + ") (margin på +/- 30')",
                    "",
                    Constants.CRITICAL_ERROR
            ));

            return true;
        }

        return false;
    }
}

