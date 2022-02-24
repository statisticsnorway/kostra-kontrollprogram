package no.ssb.kostra.control.regnskap.kostra;

import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.ssb.kostra.control.felles.Comparator.*;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.*;

public class Main {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Definitions 
    private static final List<String> osloKommuner = List.of(
            "030100",
            "030101", "030102", "030103", "030104", "030105",
            "030106", "030107", "030108", "030109", "030110",
            "030111", "030112", "030113", "030114", "030115"
    );
    private static final List<String> osloBydeler = List.of(
            "030101", "030102", "030103", "030104", "030105",
            "030106", "030107", "030108", "030109", "030110",
            "030111", "030112", "030113", "030114", "030115"
    );
    private static final List<String> svalbard = List.of("211100");
    private static final List<String> orgnrSpesial = List.of(
            "817920632", // Trøndelag
            "921234554", // Drammen
            "958935420", // Oslo
            "964338531"  // Bergen
    );

    private static final List<String> kommunenummerSpesial = List.of(
            "030100", // Oslo
            "300500", // Drammen
            "460100", // Bergen
            "500000"  // Trøndelag
    );

    public static List<String> getBevilgningRegnskapList() {
        return List.of("0A", "0C", "0I", "0K", "0M", "0P");
    }

    public static List<String> getRegionaleBevilgningRegnskapList() {
        return List.of("0A", "0C", "0M", "0P");
    }

    public static List<String> getBalanseRegnskapList() {
        return List.of("0B", "0D", "0J", "0L", "0N", "0Q");
    }

    public static Map<String, String> getKontoklasseAsMap(String skjema) {
        switch (skjema) {
            case "0A":
            case "0C":
                return Map.of("D", "1", "I", "0");

            case "0B":
            case "0D":
                return Map.of("B", "2");

            case "0I":
            case "0K":
            case "0M":
            case "0P":
                return Map.of("D", "3", "I", "4");

            case "0J":
            case "0L":
            case "0N":
            case "0Q":
                return Map.of("B", "5");

            default:
                return Map.of("R", " ");
        }
    }

    public static List<String> getKontoklasseAsList(String skjema) {
        return getKontoklasseAsMap(skjema).values().stream().map(String::trim).sorted().collect(Collectors.toList());
    }

    public static List<String> getFunksjonKapittelAsList(String skjema, String region) {
        // Funksjoner
        List<String> kommunaleFunksjoner = List.of(
                "100", "110", "120", "121", "130",
                "170", "171", "172", "173", "180",
                "201", "202", "211", "213", "215", "221", "222", "223", "231", "232", "233", "234", "241", "242", "243", "244",
                "251", "252", "253", "254", "256", "261", "265", "273", "275", "276", "281", "283", "285", "290",
                "301", "302", "303", "315", "320", "321", "322", "325", "329", "330", "332", "335", "338", "339", "340", "345",
                "350", "353", "354", "355", "360", "365", "370", "373", "375", "377", "380", "381", "383", "385", "386", "390", "392", "393"
        );

        List<String> fylkeskommunaleFunksjoner = List.of(
                "400", "410", "420", "421", "430",
                "460", "465", "470", "471", "472", "473", "480",
                "510", "515", "520", "521", "522", "523", "524", "525", "526", "527", "528", "529", "530", "531", "532", "533", "534", "535", "536", "537",
                "554", "559", "561", "562", "570", "581", "590",
                "660",
                "665",
                "701", "710", "711", "715", "716", "722", "730", "731", "732", "733", "734", "735", "740",
                "750", "760", "771", "772", "775", "790"
        );

        List<String> osloFunksjoner = List.of(
                "691", "692", "693", "694", "696"
        );

        List<String> kommuneFinansielleFunksjoner = List.of(
                "800", "840", "841", "850", "860", "870", "880", "899"
        );

        List<String> fylkeFinansielleFunksjoner = List.of(
                "800", "840", "841", "860", "870", "880", "899"
        );

        List<String> sbdrFinansielleFunksjoner = List.of(
                "841", "860", "870", "880", "899"
        );

        // Kapitler
        List<String> basisKapitler = List.of(
                "10", "11", "12", "13", "14", "15", "16", "18", "19",
                "20", "21", "22", "23", "24", "27", "28", "29",
                "31", "32", "33", "34", "35", "39",
                "40", "41", "42", "43", "45", "47",
                "51", "53", "55", "56", "580", "581", "5900", "5970", "5990",
                "9100", "9110", "9200", "9999"
        );

        List<String> sbdrKapitler = List.of(
                "17", "46"
        );

        List<String> result = new ArrayList<>();

        switch (skjema) {
            // Funksjoner
            case "0A":
            case "0M":
                if (isCodeInCodelist(region, osloKommuner)) {
                    result.addAll(osloFunksjoner);
                    result.addAll(fylkeskommunaleFunksjoner);
                }
                result.addAll(kommunaleFunksjoner);
                result.addAll(kommuneFinansielleFunksjoner);
                break;

            case "0C":
            case "0P":
                result.addAll(fylkeskommunaleFunksjoner);
                result.addAll(fylkeFinansielleFunksjoner);
                break;

            case "0I":
                if (isCodeInCodelist(region, osloKommuner)) {
                    result.addAll(osloFunksjoner);
                    result.addAll(fylkeskommunaleFunksjoner);
                }
                result.addAll(kommunaleFunksjoner);
                result.addAll(sbdrFinansielleFunksjoner);
                break;

            case "0K":
                result.addAll(fylkeskommunaleFunksjoner);
                result.addAll(sbdrFinansielleFunksjoner);
                break;

            // Kapitler
            case "0B":
            case "0D":
            case "0N":
            case "0Q":
                result.addAll(basisKapitler);
                break;

            case "0J":
            case "0L":
                result.addAll(basisKapitler);
                result.addAll(sbdrKapitler);
                break;
        }

        return Utils.rpadList(result.stream().sorted().collect(Collectors.toList()), 4);
    }

    public static List<String> getArtSektorAsList(String skjema, String region) {
        // Arter
        List<String> basisArter = List.of(
                "010", "020", "030", "040", "050", "070", "075", "080", "089", "090", "099",
                "100", "105", "110", "114", "115", "120", "130", "140", "150", "160", "165", "170", "180", "181", "182", "183", "184", "185", "190", "195",
                "200", "209", "210", "220", "230", "240", "250", "260", "270", "280", "285",
                "300", "330", "350", "370",
                "400", "429", "430", "450", "470",
                "500", "501", "509", "510", "511", "512", "520", "521", "522", "529", "530", "540", "550", "570", "589", "590",
                "600", "620", "629", "630", "640", "650", "660", "670",
                "700", "710", "729", "730", "750", "770",
                "800", "810", "830", "850", "870", "874", "875", "877", "890",
                "900", "901", "905", "909", "910", "911", "912", "920", "921", "922", "929", "940", "950", "970", "980", "989", "990"
        );

        List<String> konserninterneArter = List.of(
                "380", "480", "780", "880"
        );

        List<String> osloArter = List.of(
                "298", "379", "798"
        );

        // Sektorer
        List<String> basisSektorer = List.of(
                "000", "070", "080", "110", "151", "152", "200", "320", "355", "395", "430", "450", "499", "550", "570", "610", "640", "650", "890", "900"
        );

        List<String> result = new ArrayList<>();

        if (isCodeInCodelist(skjema, getBevilgningRegnskapList())) {
            result.addAll(basisArter);
            result.addAll(konserninterneArter);

            if (isCodeInCodelist(region, osloKommuner)) {
                result.addAll(osloArter);
            }
        }

        if (isCodeInCodelist(skjema, getBalanseRegnskapList())) {
            result.addAll(basisSektorer);
        }

        return result.stream().sorted().collect(Collectors.toList());
    }

    public static List<String> getFunksjonerUgyldigDrift() {
        // Kun gyldig i investering og skal fjernes fra drift
        return Utils.rpadList(List.of("841"), 4);
    }

    public static List<String> getFunksjonerUgyldigInvestering() {
        // Kun gyldig i drift og skal fjernes fra investering
        return Utils.rpadList(List.of("800", "840", "850", "860"), 4);
    }

    public static List<String> getArterUgyldigDrift(String skjema, String orgnr, String region) {
        // Kun gyldig i investering og skal fjernes fra drift
        if ((isCodeInCodelist(skjema, List.of("0I", "0K")) && isCodeInCodelist(orgnr, orgnrSpesial))
                || (isCodeInCodelist(skjema, List.of("0M", "0P")) && isCodeInCodelist(region, kommunenummerSpesial))
        ) {
            return List.of("280", "512", "521", "522", "529", "670", "910", "911", "912", "922", "929", "970");
        } else {
            return List.of("280", "512", "521", "522", "529", "670", "910", "911", "912", "921", "922", "929", "970");
        }
    }

    public static List<String> getArterUgyldigInvestering() {
        // Kun gyldig i drift og skal fjernes fra investering
        return List.of("070", "080", "110", "114", "240", "509", "570", "590", "600", "629", "630", "640", "800", "870", "874", "875", "877", "909", "990");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Controls
    public static ErrorReport doControls(Arguments arguments) {
        ErrorReport errorReport = new ErrorReport(arguments);
        List<String> list1 = arguments.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        ControlRecordLengde.doControl(list1, errorReport, FieldDefinitions.getFieldLength());

        if (errorReport.getErrorType() == Constants.CRITICAL_ERROR) {
            return errorReport;
        }

        List<FieldDefinition> fieldDefinitions = Utils.mergeFieldDefinitionsAndArguments(FieldDefinitions.getFieldDefinitions(), arguments);

        // legger til linjenummer
        List<Record> regnskap1 = Utils.addLineNumbering(
                Utils.getValidRecords(list1, fieldDefinitions)
        );

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap1, errorReport);

        // integritetskontroller
        ControlIntegritet.doControl(regnskap1, errorReport, arguments, getBevilgningRegnskapList(), getBalanseRegnskapList()
                , getKontoklasseAsList(arguments.getSkjema())
                , getFunksjonKapittelAsList(arguments.getSkjema(), arguments.getRegion())
                , getArtSektorAsList(arguments.getSkjema(), arguments.getRegion())
        );

        // Fjerner posteringer der beløp = 0
        List<Record> regnskap = Utils.removeBelopEquals0(regnskap1);

        // Dublett kontroll
        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            ControlDubletter.doControl(regnskap, errorReport, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"), List.of("kontoklasse", "funksjon", "art"));
        } else if (isCodeInCodelist(arguments.getSkjema(), getBalanseRegnskapList())) {
            ControlDubletter.doControl(regnskap, errorReport, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"), List.of("kontoklasse", "kapittel", "sektor"));
        }

        // Kombinasjonskontroller
        kontroll20(errorReport, regnskap);
        kontroll25(errorReport, regnskap);
        kontroll30(errorReport, regnskap);
        kontroll35(errorReport, regnskap);
        kontroll40(errorReport, regnskap);
        kontroll45(errorReport, regnskap);
        kontroll50(errorReport, regnskap);
        kontroll55(errorReport, regnskap);
        kontroll60(errorReport, regnskap);

        kontroll65(errorReport, regnskap);
        kontroll70(errorReport, regnskap);
        kontroll75(errorReport, regnskap);
        kontroll80(errorReport, regnskap);


        // SUMMERINGSKONTROLLER

        //Kontroll Summeringskontroller bevilgningsregnskap
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        // 85
        controlSumInvesteringsUtgifter(errorReport, regnskap);
        // 90
        controlSumInvesteringsInntekter(errorReport, regnskap);
        // 95
        controlSumInvesteringsDifferanse(errorReport, regnskap);
        // 100
        controlSumDriftsUtgifter(errorReport, regnskap);
        // 105
        controlSumDriftsInntekter(errorReport, regnskap);
        // 110
        controlSumDriftsDifferanse(errorReport, regnskap);

        //Kontroll Summeringskontroller balanseregnskap
        //B = Driftsregnskap (kontoklasse 2 / 5)
        // 115
        controlSumAktiva(errorReport, regnskap);
        // 120
        controlSumPassiva(errorReport, regnskap);
        // 125
        controlSumBalanseDifferanse(errorReport, regnskap);

        // Kontroll Skatteinntekter og rammetilskudd
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //Denne kontrollen gjelder IKKE for Longyearbyen Lokalstyre.
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        // 130
        controlSkatteInntekter(errorReport, regnskap);
        // 135
        controlRammetilskudd(errorReport, regnskap);

        // Kontroll Overføring mellom drifts- og investeringsregnskap
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        // 140
        controlOverforingMellomDriftOgInvestering(errorReport, regnskap);

        // Kontroll Avskrivninger
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
//        List<String> exceptionsOrgnr = List.of(
//                "958935420" // 0301 Oslo
//                , "921234554" // 3005 Drammen
//                , "964338531" // 4601 Bergen
//                , "817920632" // 5000 Sør-Trøndelag
//        );
        // 145
        controlMotpostAvskrivninger(errorReport, regnskap);
        // 150
        controlAvskrivninger(errorReport, regnskap);
        // 155
        controlAvskrivningerDifferanse(errorReport, regnskap);
        // 160
        controlAvskrivningerAndreFunksjoner(errorReport, regnskap);
        // 165
        controlAvskrivningerMotpostAndreFunksjoner(errorReport, regnskap);

        // Kontroll Funksjon 290
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1)
        //I = Investeringsregnskap (kontoklasse 0)
        // 170
        controlFunksjon290Investering(errorReport, regnskap);
        // 175
        controlFunksjon290Drift(errorReport, regnskap);

        // Kontroll Funksjon 465
        //D = Driftsregnskap (kontoklasse 1)
        //I = Investeringsregnskap (kontoklasse 0)
        // 180
        controlFunksjon465Investering(errorReport, regnskap);
        // 185
        controlFunksjon465Drift(errorReport, regnskap);

        // Kontroll Memoriakonti
        //B = Balanseregnskap (kontoklasse 2 / 5)
        // 190
        controlMemoriaKonti(errorReport, regnskap);


        return errorReport;
    }

    // Kombinasjonskontroller
    public static boolean kontroll20(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                                && isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), getFunksjonerUgyldigDrift())
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i driftsregnskapet, kontoklasse og funksjon.", " ", " "
                            , String.format("Korrigér funksjon (%s) til gyldig funksjon i driftsregnskapet, eller overfør posteringen til investeringsregnskapet", record.getFieldAsTrimmedString("funksjon_kapittel"))
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.CRITICAL_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll25(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                                && isCodeInCodelist(record.getFieldAsString("art_sektor"), getArterUgyldigDrift(errorReport.getArgs().getSkjema(), errorReport.getArgs().getOrgnr(), errorReport.getArgs().getRegion()))
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i driftsregnskapet, kontoklasse og art", " ", " "
                            , String.format("Korrigér art (%s) til gyldig art i driftsregnskapet, eller overfør posteringen til investeringsregnskapet", record.getFieldAsString("art_sektor"))
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.CRITICAL_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll30(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                                && isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("285", "660"))
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i driftsregnskapet, kontoklasse og art", " ", " "
                            , String.format("Kun advarsel, hindrer ikke innsending: (%s) regnes å være ulogisk art i driftsregnskapet. Vennligst vurder å postere på annen art eller om posteringen hører til i investeringsregnskapet.", record.getFieldAsString("art_sektor"))
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.NO_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll35(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                                && isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("520", "920"))
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i driftsregnskapet, kontoklasse og art", " ", " "
                            , String.format("Kun advarsel, hindrer ikke innsending: (%s) regnes å være ulogisk art i driftsregnskapet, med mindre posteringen gjelder sosiale utlån og næringsutlån eller mottatte avdrag på sosiale utlån og næringsutlån, som finansieres av driftsinntekter.", record.getFieldAsString("art_sektor"))
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.NO_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll40(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                                && isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), getFunksjonerUgyldigInvestering())
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i investeringsregnskapet, kontoklasse og funksjon", " ", " "
                            , String.format("Korrigér ugyldig funksjon (%s) i investeringsregnskapet til en gyldig funksjon i investeringsregnskapet eller overfør posteringen til driftsregnskapet.", record.getFieldAsTrimmedString("funksjon_kapittel"))
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.CRITICAL_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll45(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                                && isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), List.of("100 ", "110 ", "121 ", "170 ", "171 ", "400 ", "410 ", "421 ", "470 ", "471 "))
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i investeringsregnskapet, kontoklasse og funksjon", " ", " "
                            , String.format("Kun advarsel, hindrer ikke innsending: (%s) regnes å være ulogisk funksjon i investeringsregnskapet. Vennligst vurder å postere på annen funksjon eller om posteringen hører til i driftsregnskapet. ", record.getFieldAsTrimmedString("funksjon_kapittel"))
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.NO_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll50(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                                && isCodeInCodelist(record.getFieldAsString("art_sektor"), getArterUgyldigInvestering())
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i investeringsregnskapet, kontoklasse og art", " ", " "
                            , String.format("Korrigér ugyldig art (%s) i investeringsregnskapet til en gyldig art i investeringsregnskapet eller overfør posteringen til driftsregnskapet.", record.getFieldAsString("art_sektor"))
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.CRITICAL_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll55(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                                && isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("620", "650", "900"))
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i investeringsregnskapet, kontoklasse og art", " ", " "
                            , String.format("Kun advarsel, hindrer ikke innsending: (%s) regnes å være ulogisk art i investeringsregnskapet. Vennligst vurder å postere på annen art eller om posteringen hører til i driftsregnskapet. ", record.getFieldAsString("art_sektor"))
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.NO_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll60(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (!isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))) {
            if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
                List<Record> errors = regnskap.stream()
                        .filter(record -> record.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                                && !isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), List.of("841 "))
                                && isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("729"))
                        )
                        .collect(Collectors.toList());

                if (!errors.isEmpty()) {
                    errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon i investeringsregnskapet, kontoklasse, funksjon og art", " ", " "
                            , "Korrigér til riktig kombinasjon av kontoklasse, funksjon og art. Art 729 er kun gyldig i kombinasjon med funksjon 841 i investeringsregnskapet."
                            , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                            , Constants.CRITICAL_ERROR)));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean kontroll65(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            List<Record> errors = regnskap.stream()
                    .filter(record ->
                            (
                                    isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), List.of("899 "))
                                            && !isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("589", "980", "989"))
                            )
                                    ||
                                    (
                                            !isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), List.of("899 "))
                                                    && isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("589", "980", "989"))
                                    )
                    )
                    .collect(Collectors.toList());

            if (!errors.isEmpty()) {
                errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon funksjon og art", " ", " "
                        , "Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 589, 980 og 989."
                        , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                        , Constants.CRITICAL_ERROR)));

                return true;
            }
        }

        return false;
    }


    public static boolean kontroll70(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            List<Record> errors = regnskap.stream()
                    .filter(record ->
                            (
                                    isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("530"))
                                            &&
                                            !isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), List.of("880 "))
                            )
                    )
                    .collect(Collectors.toList());

            if (!errors.isEmpty()) {
                errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon funksjon og art", " ", " "
                        , "Art 530 er kun tillat brukt i kombinasjon med funksjon 880."
                        , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                        , Constants.CRITICAL_ERROR)));

                return true;
            }
        }

        return false;
    }

    public static boolean kontroll75(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            List<Record> errors = regnskap.stream()
                    .filter(record ->
                            (
                                    isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("870", "874", "875"))
                                            &&
                                            !isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), List.of("800 "))
                            )
                    )
                    .collect(Collectors.toList());

            if (!errors.isEmpty()) {
                errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon funksjon og art", " ", " "
                        , "Artene 870, 874 og 875 er kun tillat brukt i kombinasjon med funksjon 800."
                        , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                        , Constants.CRITICAL_ERROR)));

                return true;
            }
        }

        return false;
    }

    public static boolean kontroll80(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        if (isCodeInCodelist(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            List<Record> errors = regnskap.stream()
                    .filter(record ->
                            (
                                    isCodeInCodelist(record.getFieldAsString("art_sektor"), List.of("800"))
                                            &&
                                            !isCodeInCodelist(record.getFieldAsString("funksjon_kapittel"), List.of("840 "))
                            )
                    )
                    .collect(Collectors.toList());

            if (!errors.isEmpty()) {
                errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry("5. Kombinasjonskontroller", "Kombinasjon funksjon og art", " ", " "
                        , "Art 800 er kun tillat brukt i kombinasjon med funksjon 840."
                        , String.format("Gjelder for linjenummer %d.", record.getFieldAsInteger("linjenummer"))
                        , Constants.CRITICAL_ERROR)));

                return true;
            }
        }

        return false;
    }

    // Summeringskontroller
    public static boolean controlSumInvesteringsUtgifter(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 85) Investeringsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getRegionaleBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int sumInvesteringsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));

                if (!(0 < sumInvesteringsUtgifter)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Summeringskontroller investeringsregnskapet, utgiftsposteringer i investeringsregnskapet", " ", " "
                            , "Korrigér slik at fila inneholder utgiftsposteringene (" + sumInvesteringsUtgifter + ") i investeringsregnskapet"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlSumInvesteringsInntekter(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 90) Investeringsregnskapet må ha inntekter, dvs. være mindre enn 0
        Arguments arguments = errorReport.getArgs();
        int sumInvesteringsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));

        if (isCodeInCodelist(arguments.getSkjema(), getRegionaleBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {

                if (!(sumInvesteringsInntekter < 0)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Summeringskontroller investeringsregnskap, inntektsposteringene i investeringsregnskapet", " ", " "
                            , "Korrigér slik at fila inneholder inntektsposteringene (" + sumInvesteringsInntekter + ") i investeringsregnskapet"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlSumInvesteringsDifferanse(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 95) Sum investering = Sum investeringsutgifter + Sum investeringsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();
        int sumInvesteringsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));
        int sumInvesteringsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));
        int sumDifferanse = sumInvesteringsUtgifter + sumInvesteringsInntekter;

        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                if (outsideOf(sumDifferanse, -30, 30)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Summeringskontroller investeringsregnskapet, differanse i investeringsregnskapet", " ", " "
                            , "Korrigér differansen (" + sumDifferanse + ") mellom "
                            + "inntekter (" + sumInvesteringsInntekter + ") og "
                            + "utgifter (" + sumInvesteringsUtgifter + ") i investeringsregnskapet"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlSumDriftsUtgifter(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 100) Driftsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int sumDriftsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));

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
        }

        return false;
    }

    public static boolean controlSumDriftsInntekter(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 105) Driftsregnskapet må ha inntektsføringer, dvs. være mindre enn 0
        Arguments arguments = errorReport.getArgs();
        if (isCodeInCodelist(arguments.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int sumDriftsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));

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
        }

        return false;
    }

    public static boolean controlSumDriftsDifferanse(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 110) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();
        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
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
            }
        }

        return false;
    }

    public static boolean controlSumAktiva(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 115) Balanse må ha føring på aktiva, dvs. være høyere enn 0
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getBalanseRegnskapList())) {
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
        }

        return false;
    }

    public static boolean controlSumPassiva(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 120) Balanse må ha føring på passiva, dvs. være mindre enn 0
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getBalanseRegnskapList())) {
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
        }

        return false;
    }

    public static boolean controlSumBalanseDifferanse(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 125) Aktiva skal være lik passiva. Differanser opptil ±10' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getBalanseRegnskapList())) {
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
        }

        return false;

    }

    public static boolean controlSkatteInntekter(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 130) Driftsregnskapet skal ha skatteinntekter; Kontoklasse D, Funksjon 800, art 870 < 0)
        Arguments arguments = errorReport.getArgs();
        if (isCodeInCodelist(arguments.getSkjema(), getRegionaleBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler) && !isCodeInCodelist(arguments.getRegion(), svalbard)) {
                int sumSkatteInntekter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("800")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("870"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumSkatteInntekter < 0)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Skatteinntekter", " ", " "
                            , "Korrigér slik at fila inneholder skatteinntekter (" + sumSkatteInntekter + ")."
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlRammetilskudd(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 135) Rammetilskudd skal føres på funksjon 840. Ingen andre funksjoner skal være gyldig i kombinasjon med art 800.
        Arguments arguments = errorReport.getArgs();
        if (isCodeInCodelist(arguments.getSkjema(), getRegionaleBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)
                    && !isCodeInCodelist(arguments.getRegion(), svalbard)
                    // eget unntakstilfelle fra s212
                    && !isCodeInCodelist(arguments.getRegion(), List.of("501400"))
            ) {

                int sumRammetilskudd = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("840")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("800"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumRammetilskudd < 0)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Rammetilskudd", " ", " "
                            , "Korrigér slik at fila inneholder rammetilskudd (" + sumRammetilskudd + ")"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlOverforingMellomDriftOgInvestering(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 140
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {

                int sumDriftsOverforinger = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("570"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);
                int sumInvesteringsOverforinger = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("I"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("970"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);
                int sumDifferanse = sumDriftsOverforinger + sumInvesteringsOverforinger;

                if (outsideOf(sumDifferanse, -30, 30)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Overføring mellom drifts- og investeringsregnskap", " ", " "
                            , "Korrigér i fila slik at differansen (" + sumDifferanse
                            + ") i overføringer mellom drifts- (" + sumDriftsOverforinger
                            + ") og investeringsregnskapet (" + sumInvesteringsOverforinger + ") stemmer overens."
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    private static int getMotpostAvskrivninger(List<Record> regnskap, Arguments arguments) {
        return regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("860")
                        && p.getFieldAsString("art_sektor").equalsIgnoreCase("990")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);
    }

    public static boolean controlMotpostAvskrivninger(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 145
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int sumMotpostAvskrivninger = getMotpostAvskrivninger(regnskap, arguments);

                if (sumMotpostAvskrivninger == 0) {
                    // For 0I og 0K: Til informasjon / NO_ERROR, For alle andre: hard kontroll / CRITICAL_ERROR
                    int errorType = (isCodeInCodelist(arguments.getSkjema(), List.of("0I", "0K"))) ? Constants.NO_ERROR : Constants.CRITICAL_ERROR;
                    String errorText = (isCodeInCodelist(arguments.getSkjema(), List.of("0I", "0K")))
                            ? "Kun advarsel, hindrer ikke innsending. Korrigér i fila slik at den inneholder motpost avskrivninger (" + sumMotpostAvskrivninger + "), føres på funksjon 860 og art 990."
                            : "Korrigér i fila slik at den inneholder motpost avskrivninger (" + sumMotpostAvskrivninger + "), føres på funksjon 860 og art 990.";
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Avskrivninger, motpost avskrivninger", " ", " "
                            , errorText
                            , ""
                            , errorType
                    ));


                    return true;
                }
            }
        }

        return false;
    }

    private static int getAvskrivninger(List<Record> regnskap, Arguments arguments) {
        return regnskap.stream()
                .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && between(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 100, 799)
                        && p.getFieldAsString("art_sektor").equalsIgnoreCase("590"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                .reduce(0, Integer::sum);
    }

    public static boolean controlAvskrivninger(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 150
        Arguments arguments = errorReport.getArgs();
        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int sumAvskrivninger = getAvskrivninger(regnskap, arguments);

                // For 0I og 0K: Til informasjon / NO_ERROR, For alle andre: hard kontroll / CRITICAL_ERROR
                int errorType = (isCodeInCodelist(arguments.getSkjema(), List.of("0I", "0K"))) ? Constants.NO_ERROR : Constants.CRITICAL_ERROR;
                String errorText = (isCodeInCodelist(arguments.getSkjema(), List.of("0I", "0K")))
                        ? "Kun advarsel, hindrer ikke innsending. Korrigér i fila slik at den inneholder avskrivninger (" + sumAvskrivninger + "), føres på tjenestefunksjon og art 590."
                        : "Korrigér i fila slik at den inneholder avskrivninger (" + sumAvskrivninger + "), føres på tjenestefunksjon og art 590.";


                if (sumAvskrivninger == 0) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Avskrivninger", " ", " "
                            , errorText
                            , ""
                            , errorType
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlAvskrivningerDifferanse(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 155
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int sumMotpostAvskrivninger = getMotpostAvskrivninger(regnskap, arguments);
                int sumAvskrivninger = getAvskrivninger(regnskap, arguments);
                int sumDifferanse = sumMotpostAvskrivninger + sumAvskrivninger;

                if (outsideOf(sumDifferanse, -30, 30)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Avskrivninger", " ", " "
                            , "Kontroll Avskrivninger, differanse"
                            , "Korrigér i fila slik at avskrivninger (" + sumAvskrivninger + ") stemmer overens med motpost avskrivninger (" + sumMotpostAvskrivninger + ") (margin på +/- 30')"
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }


    public static boolean controlAvskrivningerAndreFunksjoner(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 160
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int sumAvskrivningerAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && between(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 800, 899)
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("590")
                        )
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);
                List<String> avskrivningerAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && between(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 800, 899)
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("590")
                        )
                        .map(p -> p.getFieldAsTrimmedString("funksjon_kapittel"))
                        .collect(Collectors.toList());

                if (!(sumAvskrivningerAndreFunksjoner == 0)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Avskrivninger, avskrivninger ført på andre funksjoner", " ", " "
                            , "Korrigér i fila slik at avskrivningene føres på tjenestefunksjon og ikke på funksjonene (" + avskrivningerAndreFunksjoner + ")"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlAvskrivningerMotpostAndreFunksjoner(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 165
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), getBevilgningRegnskapList())) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int sumMotpostAvskrivningerAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && !p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("860")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("990")
                        )
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);
                List<String> motpostAvskrivningerAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && !p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("860")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("990")
                        )
                        .map(p -> p.getFieldAsTrimmedString("funksjon_kapittel"))
                        .collect(Collectors.toList());

                if (!(sumMotpostAvskrivningerAndreFunksjoner == 0)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Avskrivninger, motpost avskrivninger ført på andre funksjoner", " ", " "
                            , "Korrigér i fila slik at motpost avskrivninger kun er ført på funksjon 860, art 990 og ikke på funksjonene (" + motpostAvskrivningerAndreFunksjoner + ")"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlFunksjon290Investering(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 170) Funksjon I.290 for sum alle arter = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), List.of("0A", "0M"))) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int funksjon290Investering = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("I"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("290"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (outsideOf(funksjon290Investering, -30, 30)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Funksjon 290, investeringsregnskapet", " ", " "
                            , "Korrigér i fila slik at differanse (" + funksjon290Investering + ") på funksjon 290 interkommunale samarbeid går i 0 i investeringsregnskapet . (margin på +/- 30')"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlFunksjon290Drift(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 175) Funksjon D.290 for sum artene 010-590 + funksjon D.290 for sum artene 600-990 = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();
        if (isCodeInCodelist(arguments.getSkjema(), List.of("0A", "0M"))) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int funksjon290Drift = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("290"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (outsideOf(funksjon290Drift, -30, 30)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Funksjon 290, driftsregnskapet", " ", " "
                            , "Korrigér i fila slik at differanse (" + funksjon290Drift + ") på funksjon 290 interkommunale samarbeid (§27-samarbeid) går i 0 i driftsregnskapet . (margin på +/- 30')"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlFunksjon465Investering(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 180) Funksjon I.465 for sum alle arter = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), List.of("0C", "0P"))) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int funksjon465Investering = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("I"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("465"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (outsideOf(funksjon465Investering, -30, 30)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Funksjon 465", " ", " "
                            , "Kontroll Funksjon 465, investeringsregnskapet"
                            , "Korrigér i fila slik at differanse (" + funksjon465Investering + ") på funksjon 465 Interfylkeskommunale samarbeid går i 0 i investeringsregnskapet . (margin på +/- 30')"
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlFunksjon465Drift(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 185) Funksjon D.465 for sum artene 010-590 + funksjon D.465 for sum artene 600-990 = < 30 og > - 30. Differanser opptil +-30' godtas, og skal ikke utlistes.
        Arguments arguments = errorReport.getArgs();

        if (isCodeInCodelist(arguments.getSkjema(), List.of("0C", "0P"))) {
            if (!isCodeInCodelist(arguments.getRegion(), osloBydeler)) {
                int funksjon465Drift = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("465"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (outsideOf(funksjon465Drift, -30, 30)) {
                    errorReport.addEntry(new ErrorReportEntry(
                            "6. Summeringskontroller", "Kontroll Funksjon 465, driftsregnskapet", " ", " "
                            , "Korrigér i fila slik at differanse (" + funksjon465Drift + ") på funksjon 465 Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet . (margin på +/- 30')"
                            , ""
                            , Constants.CRITICAL_ERROR
                    ));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean controlMemoriaKonti(ErrorReport errorReport, List<Record> regnskap) {
        errorReport.incrementCount();

        // 190)
        Arguments arguments = errorReport.getArgs();
        if (isCodeInCodelist(arguments.getSkjema(), getBalanseRegnskapList())) {
            int sumMemoriaKonti = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("B"))
                            && between(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 9100, 9200)
                    )
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
            int sumMotkontoMemoriaKonti = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("B"))
                            && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("9999")
                    )
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);
            int differanse = sumMemoriaKonti + sumMotkontoMemoriaKonti;

            if (outsideOf(differanse, -10, 10)) {
                errorReport.addEntry(new ErrorReportEntry(
                        "6. Summeringskontroller", "Balanseregnskap", " ", " "
                        , "Kontroll Memoriakonti"
                        , "Kun advarsel, hindrer ikke innsending. Korrigér i fila slik at differansen (" + differanse + ") mellom "
                        + "memoriakontiene (" + sumMemoriaKonti + ") og "
                        + "motkonto for memoriakontiene (" + sumMotkontoMemoriaKonti + ") går i 0. (margin på +/- 10')"
                        , Constants.NO_ERROR
                ));

                return true;
            }
        }

        return false;
    }


}
