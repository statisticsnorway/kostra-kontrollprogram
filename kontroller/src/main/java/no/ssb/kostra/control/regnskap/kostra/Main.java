package no.ssb.kostra.control.regnskap.kostra;

import no.ssb.kostra.control.felles.ControlDubletter;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlIntegritet;
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

import static java.util.stream.Collectors.toList;
import static no.ssb.kostra.control.felles.Comparator.*;
import static no.ssb.kostra.control.regnskap.felles.ControlRegnskap.*;

@SuppressWarnings("SpellCheckingInspection")
public class Main {
    private static final String KONTOKLASSE = "kontoklasse";
    private static final String FUNKSJON_KAPITTEL = "funksjon_kapittel";
    private static final String ART_SEKTOR = "art_sektor";
    private static final String BELOP = "belop";
    private static final String KOMBINASJON_I_DRIFT_K_A = "Kombinasjon i driftsregnskapet, kontoklasse og art";
    private static final String KOMBINASJON_F_A = "Kombinasjon funksjon og art";
    private static final String KOMBINASJONSKONTROLLER = "5. Kombinasjonskontroller";
    private static final String SUMMERINGSKONTROLLER = "6. Summeringskontroller";
    private static final String GJELDER_FOR_LINJENUMMER = "Gjelder for linjenummer %d.";
    private static final String LINJENUMMER = "linjenummer";
    private static final String BALANSEREGNSKAP = "Balanseregnskap";


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

    private Main() {
    }

    public static List<String> getBevilgningRegnskapList() {
        return List.of("0A", "0C", "0I", "0K", "0M", "0P");
    }

    public static List<String> getRegionaleBevilgningRegnskapList() {
        return List.of("0A", "0C", "0M", "0P");
    }

    public static List<String> getBalanseRegnskapList() {
        return List.of("0B", "0D", "0J", "0L", "0N", "0Q");
    }

    public static Map<String, String> getKontoklasseAsMap(final String skjema) {
        if ("0A".equals(skjema) || "0C".equals(skjema)) {
            return Map.of("D", "1", "I", "0");
        } else if ("0B".equals(skjema) || "0D".equals(skjema)) {
            return Map.of("B", "2");
        } else if ("0I".equals(skjema) || "0K".equals(skjema) || "0M".equals(skjema) || "0P".equals(skjema)) {
            return Map.of("D", "3", "I", "4");
        } else if ("0J".equals(skjema) || "0L".equals(skjema) || "0N".equals(skjema) || "0Q".equals(skjema)) {
            return Map.of("B", "5");
        }
        return Map.of("R", " ");
    }

    public static List<String> getKontoklasseAsList(final String skjema) {
        return getKontoklasseAsMap(skjema).values().stream()
                .map(String::trim)
                .sorted()
                .toList();
    }

    public static List<String> getFunksjonKapittelAsList(final String skjema, final String region, final String orgnr) {
        // Funksjoner
        final var kommunaleFunksjoner = List.of(
                "100", "110", "120", "121", "130",
                "170", "171", "172", "173", "180",
                "201", "202", "211", "213", "215", "221", "222", "223", "231", "232", "233", "234", "241", "242", "243", "244",
                "251", "252", "253", "254", "256", "261", "265", "273", "275", "276", "281", "283", "285", "290",
                "301", "302", "303", "315", "320", "321", "322", "325", "329", "330", "332", "335", "338", "339", "340", "345",
                "350", "353", "354", "355", "360", "365", "370", "373", "375", "377", "380", "381", "383", "385", "386", "390", "392", "393"
        );

        final var fylkeskommunaleFunksjoner = List.of(
                "400", "410", "420", "421", "430",
                "460", "465", "470", "471", "472", "473", "480",
                "510", "515", "520", "521", "522", "523", "524", "525", "526", "527", "528", "529", "530", "531", "532", "533", "534", "535", "536", "537",
                "554", "559", "561", "562", "570", "581", "590",
                "660",
                "665",
                "701", "710", "711", "715", "716", "722", "730", "731", "732", "733", "734", "735", "740",
                "750", "760", "771", "772", "775", "790"
        );

        final var osloFunksjoner = List.of("691", "692", "693", "694", "696");

        final var kommuneFinansielleFunksjoner = List.of(
                "800", "840", "841", "850", "860", "870", "880", "899");

        final var fylkeFinansielleFunksjoner = List.of("800", "840", "841", "860", "870", "880", "899");

        final var fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner = List.of("841", "860", "870", "880", "899");

        final var kommunaleSbdrFinansielleFunksjoner = List.of("841", "850", "860", "870", "880", "899");

        // Kapitler
        final var basisKapitler = List.of(
                "10", "11", "12", "13", "14", "15", "16", "18", "19",
                "20", "21", "22", "23", "24", "27", "28", "29",
                "31", "32", "33", "34", "35", "39",
                "40", "41", "42", "43", "45", "47",
                "51", "53", "55", "56", "580", "581", "5900", "5970", "5990",
                "9100", "9110", "9200", "9999"
        );

        final var sbdrKapitler = List.of("17", "46");

        final var result = new ArrayList<String>();

        switch (skjema) {
            // Funksjoner
            case "0A", "0M" -> {
                if (isCodeInCodeList(region, osloKommuner)) {
                    result.addAll(osloFunksjoner);
                    result.addAll(fylkeskommunaleFunksjoner);
                }
                result.addAll(kommunaleFunksjoner);
                result.addAll(kommuneFinansielleFunksjoner);
            }
            case "0C", "0P" -> {
                result.addAll(fylkeskommunaleFunksjoner);
                result.addAll(fylkeFinansielleFunksjoner);
            }
            case "0I" -> {
                if (isCodeInCodeList(region, osloKommuner)) {
                    result.addAll(osloFunksjoner);
                    result.addAll(fylkeskommunaleFunksjoner);
                }
                result.addAll(kommunaleFunksjoner);

                if (isCodeInCodeList(orgnr, orgnrSpesial)){
                    result.addAll(fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner);
                } else {
                    result.addAll(kommunaleSbdrFinansielleFunksjoner);
                }

            }
            case "0K" -> {
                result.addAll(fylkeskommunaleFunksjoner);
                result.addAll(fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner);
            }

            // Kapitler
            case "0B", "0D", "0N", "0Q" -> result.addAll(basisKapitler);
            case "0J", "0L" -> {
                result.addAll(basisKapitler);
                result.addAll(sbdrKapitler);
            }
            default -> {
            }
        }
        return Utils.rpadList(result.stream().sorted().toList(), 4);
    }

    public static List<String> getArtSektorAsList(final String skjema, final String region) {
        // Arter
        final var basisArter = List.of(
                "010", "020", "030", "040", "050", "070", "075", "080", "089", "090", "099",
                "100", "105", "110", "114", "115", "120", "130", "140", "150", "160", "165", "170", "180", "181", "182", "183", "184", "185", "190", "195",
                "200", "209", "210", "220", "230", "240", "250", "260", "270", "280", "285",
                "300", "330", "350", "370",
                "400", "429", "430", "450", "470",
                "500", "501", "509", "510", "511", "512", "520", "521", "522", "529", "530", "540", "550", "570", "589", "590",
                "600", "620", "629", "630", "640", "650", "660", "670",
                "700", "710", "729", "730", "750", "770",
                "800", "810", "830", "850", "870", "890",
                "900", "901", "905", "909", "910", "911", "912", "920", "921", "922", "929", "940", "950", "970", "980", "989", "990"
        );

        final var konserninterneArter = List.of("380", "480", "780", "880");
        final var osloArter = List.of("298", "379", "798");
        final var kommunaleArter = List.of("874", "875", "877");
        final var fylkeskommunaleArter = List.of("877");

        // Sektorer
        final var basisSektorer = List.of(
                "000", "070", "080", "110", "151", "152", "200", "320", "355", "395", "430", "450", "499", "550",
                "570", "610", "640", "650", "890", "900");

        final var result = new ArrayList<String>();

        switch (skjema){
            case "0A", "0M" -> {
                result.addAll(basisArter);
                result.addAll(konserninterneArter);
                result.addAll(kommunaleArter);

                if (isCodeInCodeList(region, osloKommuner)) {
                    result.addAll(osloArter);
                }
            }

            case "0C", "0P" -> {
                result.addAll(basisArter);
                result.addAll(konserninterneArter);
                result.addAll(fylkeskommunaleArter);
            }

            case "0I", "0K" -> {
                result.addAll(basisArter);
                result.addAll(konserninterneArter);
            }

            case "0B", "0D", "0J", "0L", "0N", "0Q" -> {
                result.addAll(basisSektorer);
            }
        }

        return result.stream()
                .sorted()
                .toList();
    }

    public static List<String> getFunksjonerUgyldigDrift() {
        // Kun gyldig i investering og skal fjernes fra drift
        return Utils.rpadList(List.of("841"), 4);
    }

    public static List<String> getFunksjonerUgyldigInvestering() {
        // Kun gyldig i drift og skal fjernes fra investering
        return Utils.rpadList(List.of("800", "840", "850", "860"), 4);
    }

    public static List<String> getArterUgyldigDrift(final String skjema, final String orgnr, final String region) {
        // Kun gyldig i investering og skal fjernes fra drift
        if (isCodeInCodeList(skjema, List.of("0I", "0K")) && isCodeInCodeList(orgnr, orgnrSpesial)
                ||
                isCodeInCodeList(skjema, List.of("0M", "0P")) && isCodeInCodeList(region, kommunenummerSpesial)) {
            return List.of("280", "512", "521", "522", "529", "670", "910", "911", "912", "922", "929", "970");
        } else {
            return List.of("280", "512", "521", "522", "529", "670", "910", "911", "912", "921", "922", "929", "970");
        }
    }

    public static List<String> getArterUgyldigInvestering() {
        // Kun gyldig i drift og skal fjernes fra investering
        return List.of(
                "070", "080", "110", "114", "240", "509", "570", "590", "600", "629", "630", "640", "800",
                "870", "874", "875", "877", "909", "990");
    }

    public static List<String> getArtSektorAsList(final String skjema, final String region, final String orgnr, final String kontoklasse) {
        List<String> artSektor = getArtSektorAsList(skjema, region);

        switch (getKontoklasseAsMap(skjema).getOrDefault(kontoklasse, " ")){
            case "D" -> {
               return removeCodesFromCodeList(artSektor, getArterUgyldigDrift(skjema, orgnr, region));
            }

            case "I" -> {
                return removeCodesFromCodeList(artSektor, getArterUgyldigInvestering());
            }
        }

        return artSektor;
    }

    public static List<String> getFunksjonKapittelAsList(final String skjema, final String region, final String orgnr, final String kontoklasse) {
        List<String> funksjonKapittel = getFunksjonKapittelAsList(skjema, region, orgnr);

        switch (getKontoklasseAsMap(skjema).getOrDefault(kontoklasse, " ")){
            case "D" -> {
                return removeCodesFromCodeList(funksjonKapittel, getFunksjonerUgyldigDrift());
            }

            case "I" -> {
                return removeCodesFromCodeList(funksjonKapittel, getFunksjonerUgyldigInvestering());
            }
        }

        return funksjonKapittel;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Controls
    public static ErrorReport doControls(final Arguments arguments) {
        final var errorReport = new ErrorReport(arguments);
        final var list1 = arguments.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        ControlRecordLengde.doControl(list1, errorReport, FieldDefinitions.getFieldLength());

        if (errorReport.getErrorType() == Constants.CRITICAL_ERROR) {
            return errorReport;
        }

        final var fieldDefinitions = Utils.mergeFieldDefinitionsAndArguments(FieldDefinitions.getFieldDefinitions(), arguments);

        // legger til linjenummer
        final var regnskap1 = Utils.addLineNumbering(Utils.getValidRecords(list1, fieldDefinitions));

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap1, errorReport);

        // integritetskontroller
        ControlIntegritet.doControl(
                regnskap1,
                errorReport,
                arguments,
                getBevilgningRegnskapList(),
                getBalanseRegnskapList(),
                getKontoklasseAsList(arguments.getSkjema()),
                getFunksjonKapittelAsList(arguments.getSkjema(), arguments.getRegion(), arguments.getOrgnr()),
                getArtSektorAsList(arguments.getSkjema(), arguments.getRegion()));

        // Fjerner posteringer der beløp = 0
        final var regnskap = Utils.removeBelopEquals0(regnskap1);

        // Dublett kontroll
        if (isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())) {
            ControlDubletter.doControl(
                    regnskap,
                    errorReport,
                    List.of(KONTOKLASSE, FUNKSJON_KAPITTEL, ART_SEKTOR),
                    List.of(KONTOKLASSE, "funksjon", "art"));
        } else if (isCodeInCodeList(arguments.getSkjema(), getBalanseRegnskapList())) {
            ControlDubletter.doControl(
                    regnskap,
                    errorReport,
                    List.of(KONTOKLASSE, FUNKSJON_KAPITTEL, ART_SEKTOR),
                    List.of(KONTOKLASSE, "kapittel", "sektor"));
        }

        kontroll1(errorReport, regnskap);
        kontroll5(errorReport, regnskap);
        kontroll10(errorReport, regnskap);
        kontroll15(errorReport, regnskap);

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

    // Posteringskontroller
    public static boolean kontroll1(final ErrorReport errorReport, final List<KostraRecord> regnskap){
        errorReport.incrementCount();

        final var errors = regnskap
                .stream()
                .filter(kostraRecord -> getBevilgningRegnskapList()
                        .stream()
                        .anyMatch(item -> item.equalsIgnoreCase(kostraRecord.getFieldAsString("skjema"))))
                .filter(kostraRecord ->
                        !isCodeInCodeList(
                                kostraRecord.getFieldAsString("funksjon_kapittel")
                                , getFunksjonKapittelAsList(
                                        kostraRecord.getFieldAsString("skjema")
                                        , kostraRecord.getFieldAsString("region")
                                        , kostraRecord.getFieldAsString("orgnr")
                                        , kostraRecord.getFieldAsString("kontoklasse")

                                )
                        ))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry("3. Feltkontroller", "Kontroll 1 Funksjon", " ", " "
                , String.format("Fant ugyldig funksjon '(%s)'. Korrigér funksjon i henhold til KOSTRA kontoplan.", r.getFieldAsString(FUNKSJON_KAPITTEL))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll5(final ErrorReport errorReport, final List<KostraRecord> regnskap){
        errorReport.incrementCount();

        final var errors = regnskap
                .stream()
                .filter(kostraRecord -> getBalanseRegnskapList()
                        .stream()
                        .anyMatch(item -> item.equalsIgnoreCase(kostraRecord.getFieldAsString("skjema"))))
                .filter(kostraRecord ->
                        !isCodeInCodeList(
                                kostraRecord.getFieldAsString("funksjon_kapittel")
                                , getFunksjonKapittelAsList(
                                        kostraRecord.getFieldAsString("skjema")
                                        , kostraRecord.getFieldAsString("region")
                                        , kostraRecord.getFieldAsString("orgnr")
                                        , kostraRecord.getFieldAsString("kontoklasse")

                                )
                        ))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry("3. Feltkontroller", "Kontroll 5 Funksjon", " ", " "
                , String.format("Fant ugyldig kapittel '(%s)'. Korrigér kapittel i henhold til KOSTRA kontoplan.", r.getFieldAsString(FUNKSJON_KAPITTEL))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll10(final ErrorReport errorReport, final List<KostraRecord> regnskap){
        errorReport.incrementCount();

        final var errors = regnskap
                .stream()
                .filter(kostraRecord -> getBevilgningRegnskapList()
                        .stream()
                        .anyMatch(item -> item.equalsIgnoreCase(kostraRecord.getFieldAsString("skjema"))))
                .filter(kostraRecord ->
                        !isCodeInCodeList(
                                kostraRecord.getFieldAsString("art_sektor")
                                , getArtSektorAsList(
                                        kostraRecord.getFieldAsString("skjema")
                                        , kostraRecord.getFieldAsString("region")
                                        , kostraRecord.getFieldAsString("orgnr")
                                        , kostraRecord.getFieldAsString("kontoklasse")

                                )
                        ))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry("3. Feltkontroller", "Kontroll 10 Art", " ", " "
                , String.format("Fant ugyldig art '(%s)'. Korrigér art i henhold til KOSTRA kontoplan.", r.getFieldAsString(ART_SEKTOR))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll15(final ErrorReport errorReport, final List<KostraRecord> regnskap){
        errorReport.incrementCount();

        final var errors = regnskap
                .stream()
                .filter(kostraRecord -> getBalanseRegnskapList()
                        .stream()
                        .anyMatch(item -> item.equalsIgnoreCase(kostraRecord.getFieldAsString("skjema"))))
                .filter(kostraRecord ->
                    !isCodeInCodeList(
                            kostraRecord.getFieldAsString("art_sektor")
                            , getArtSektorAsList(
                                    kostraRecord.getFieldAsString("skjema")
                                    , kostraRecord.getFieldAsString("region")
                                    , kostraRecord.getFieldAsString("orgnr")
                                    , kostraRecord.getFieldAsString("kontoklasse")

                            )
                ))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry("3. Feltkontroller", "Kontroll 10 Art", " ", " "
                , String.format("Fant ugyldig sektor '(%s)'. Korrigér sektor i henhold til KOSTRA kontoplan.", r.getFieldAsString(ART_SEKTOR))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    // Kombinasjonskontroller
    public static boolean kontroll20(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                        && isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), getFunksjonerUgyldigDrift()))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, "Kombinasjon i driftsregnskapet, kontoklasse og funksjon.", " ", " "
                , String.format("Korrigér funksjon (%s) til gyldig funksjon i driftsregnskapet, eller overfør posteringen til investeringsregnskapet", r.getFieldAsTrimmedString(FUNKSJON_KAPITTEL))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll25(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                        && isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), getArterUgyldigDrift(errorReport.getArgs().getSkjema(), errorReport.getArgs().getOrgnr(), errorReport.getArgs().getRegion())))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, KOMBINASJON_I_DRIFT_K_A, " ", " "
                , String.format("Korrigér art (%s) til gyldig art i driftsregnskapet, eller overfør posteringen til investeringsregnskapet", r.getFieldAsString(ART_SEKTOR))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll30(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                        && isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("285", "660")))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, KOMBINASJON_I_DRIFT_K_A, " ", " "
                , String.format("Kun advarsel, hindrer ikke innsending: (%s) regnes å være ulogisk art i driftsregnskapet. Vennligst vurder å postere på annen art eller om posteringen hører til i investeringsregnskapet.", r.getFieldAsString(ART_SEKTOR))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.NO_ERROR)));
        return true;
    }

    public static boolean kontroll35(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("D"))
                        && isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("520", "920")))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, KOMBINASJON_I_DRIFT_K_A, " ", " "
                , String.format("Kun advarsel, hindrer ikke innsending: (%s) regnes å være ulogisk art i driftsregnskapet, med mindre posteringen gjelder sosiale utlån og næringsutlån eller mottatte avdrag på sosiale utlån og næringsutlån, som finansieres av driftsinntekter.", r.getFieldAsString(ART_SEKTOR))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.NO_ERROR)));
        return true;
    }

    public static boolean kontroll40(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                        && isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), getFunksjonerUgyldigInvestering()))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(record -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, "Kombinasjon i investeringsregnskapet, kontoklasse og funksjon", " ", " "
                , String.format("Korrigér ugyldig funksjon (%s) i investeringsregnskapet til en gyldig funksjon i investeringsregnskapet eller overfør posteringen til driftsregnskapet.", record.getFieldAsTrimmedString(FUNKSJON_KAPITTEL))
                , String.format(GJELDER_FOR_LINJENUMMER, record.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll45(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                        && isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), List.of("100 ", "110 ", "121 ", "170 ", "171 ", "400 ", "410 ", "421 ", "470 ", "471 ")))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, "Kombinasjon i investeringsregnskapet, kontoklasse og funksjon", " ", " "
                , String.format("Kun advarsel, hindrer ikke innsending: (%s) regnes å være ulogisk funksjon i investeringsregnskapet. Vennligst vurder å postere på annen funksjon eller om posteringen hører til i driftsregnskapet. ", r.getFieldAsTrimmedString(FUNKSJON_KAPITTEL))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.NO_ERROR)));
        return true;
    }

    public static boolean kontroll50(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                        && isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), getArterUgyldigInvestering()))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, "Kombinasjon i investeringsregnskapet, kontoklasse og art", " ", " "
                , String.format("Korrigér ugyldig art (%s) i investeringsregnskapet til en gyldig art i investeringsregnskapet eller overfør posteringen til driftsregnskapet.", r.getFieldAsString(ART_SEKTOR))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll55(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                        && isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("620", "650", "900")))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, "Kombinasjon i investeringsregnskapet, kontoklasse og art", " ", " "
                , String.format("Kun advarsel, hindrer ikke innsending: (%s) regnes å være ulogisk art i investeringsregnskapet. Vennligst vurder å postere på annen art eller om posteringen hører til i driftsregnskapet. ", r.getFieldAsString(ART_SEKTOR))
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.NO_ERROR)));
        return true;
    }

    public static boolean kontroll60(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (isEmpty(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                || !isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r -> r.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(errorReport.getArgs().getSkjema()).get("I"))
                        && !isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), List.of("841 "))
                        && isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("729")))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, "Kombinasjon i investeringsregnskapet, kontoklasse, funksjon og art", " ", " "
                , "Korrigér til riktig kombinasjon av kontoklasse, funksjon og art. Art 729 er kun gyldig i kombinasjon med funksjon 841 i investeringsregnskapet."
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll65(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (!isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r ->
                        (
                                isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), List.of("899 "))
                                        && !isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("589", "980", "989"))
                        )
                                ||
                                (
                                        !isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), List.of("899 "))
                                                && isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("589", "980", "989"))
                                ))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, KOMBINASJON_F_A, " ", " "
                , "Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 589, 980 og 989."
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }


    public static boolean kontroll70(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (!isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r ->
                        (
                                isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("530"))
                                        &&
                                        !isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), List.of("880 "))
                        ))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, KOMBINASJON_F_A, " ", " "
                , "Art 530 er kun tillat brukt i kombinasjon med funksjon 880."
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll75(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (!isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r ->
                        (
                                isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("870", "874", "875"))
                                        &&
                                        !isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), List.of("800 "))
                        ))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, KOMBINASJON_F_A, " ", " "
                , "Artene 870, 874 og 875 er kun tillat brukt i kombinasjon med funksjon 800."
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    public static boolean kontroll80(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        if (!isCodeInCodeList(errorReport.getArgs().getSkjema(), getBevilgningRegnskapList())) {
            return false;
        }

        final var errors = regnskap.stream()
                .filter(r ->
                        (
                                isCodeInCodeList(r.getFieldAsString(ART_SEKTOR), List.of("800"))
                                        &&
                                        !isCodeInCodeList(r.getFieldAsString(FUNKSJON_KAPITTEL), List.of("840 "))
                        ))
                .toList();

        if (errors.isEmpty()) {
            return false;
        }

        errors.forEach(r -> errorReport.addEntry(new ErrorReportEntry(KOMBINASJONSKONTROLLER, KOMBINASJON_F_A, " ", " "
                , "Art 800 er kun tillat brukt i kombinasjon med funksjon 840."
                , String.format(GJELDER_FOR_LINJENUMMER, r.getFieldAsInteger(LINJENUMMER))
                , Constants.CRITICAL_ERROR)));
        return true;
    }

    // Summeringskontroller
    public static boolean controlSumInvesteringsUtgifter(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 85) Investeringsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getRegionaleBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumInvesteringsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));

        if (0 < sumInvesteringsUtgifter) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Summeringskontroller investeringsregnskapet, utgiftsposteringer i investeringsregnskapet", " ", " "
                , String.format("Korrigér slik at fila inneholder utgiftsposteringene (%d) i investeringsregnskapet", sumInvesteringsUtgifter)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumInvesteringsInntekter(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 90) Investeringsregnskapet må ha inntekter, dvs. være mindre enn 0
        final var arguments = errorReport.getArgs();
        final var sumInvesteringsInntekter = getSumInntekter(
                arguments,
                regnskap,
                getBevilgningRegnskapList(),
                getKontoklasseAsMap(arguments.getSkjema()).get("I"));

        if (!isCodeInCodeList(arguments.getSkjema(), getRegionaleBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)
                || (0 > sumInvesteringsInntekter)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Summeringskontroller investeringsregnskap, inntektsposteringene i investeringsregnskapet", " ", " "
                , String.format("Korrigér slik at fila inneholder inntektsposteringene (%d) i investeringsregnskapet", sumInvesteringsInntekter)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumInvesteringsDifferanse(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 95) Sum investering = Sum investeringsutgifter + Sum investeringsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();
        final var sumInvesteringsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));
        final var sumInvesteringsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("I"));
        final var sumDifferanse = sumInvesteringsUtgifter + sumInvesteringsInntekter;

        if (!isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)
                || !outsideOf(sumDifferanse, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Summeringskontroller investeringsregnskapet, differanse i investeringsregnskapet", " ", " "
                , String.format("Korrigér differansen (%d) mellom inntekter (%d) og utgifter (%d) i investeringsregnskapet", sumDifferanse, sumInvesteringsInntekter, sumInvesteringsUtgifter)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumDriftsUtgifter(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 100) Driftsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumDriftsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));
        if (sumDriftsUtgifter > 0) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Summeringskontroller bevilgningsregnskap, utgiftsposteringer i driftsregnskapet", " ", " "
                , String.format("Korrigér slik at fila inneholder utgiftsposteringene (%d) i driftsregnskapet", sumDriftsUtgifter)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumDriftsInntekter(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        // 105) Driftsregnskapet må ha inntektsføringer, dvs. være mindre enn 0
        final var arguments = errorReport.getArgs();
        if (!isCodeInCodeList(arguments.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumDriftsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));

        if (0 > sumDriftsInntekter) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Summeringskontroller bevilgningsregnskap, inntektsposteringer i driftsregnskapet", " ", " "
                , String.format("Korrigér slik at fila inneholder inntektsposteringene (%d) i driftsregnskapet", sumDriftsInntekter)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumDriftsDifferanse(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 110) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumDriftsUtgifter = getSumUtgifter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));
        final var sumDriftsInntekter = getSumInntekter(arguments, regnskap, getBevilgningRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("D"));
        final var sumDifferanse = sumDriftsUtgifter + sumDriftsInntekter;

        if (!outsideOf(sumDifferanse, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Summeringskontroller bevilgningsregnskap, differanse i driftsregnskapet", " ", " "
                , String.format("Korrigér differansen (%d) mellom inntekter (%d) og utgifter (%d) i driftsregnskapet", sumDifferanse, sumDriftsInntekter, sumDriftsUtgifter)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumAktiva(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        // 115) Balanse må ha føring på aktiva, dvs. være høyere enn 0
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBalanseRegnskapList())) {
            return false;
        }

        final var sumAktiva = getSumAktiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));
        if (sumAktiva > 0) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Summeringskontroller balanseregnskap, registrering av aktiva (Eiendeler)", " ", " "
                , String.format("Korrigér slik at fila inneholder registrering av aktiva/eiendeler (%d) i balanse.", sumAktiva)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumPassiva(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        // 120) Balanse må ha føring på passiva, dvs. være mindre enn 0
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBalanseRegnskapList())) {
            return false;
        }

        final var sumPassiva = getSumPassiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));
        if (0 > sumPassiva) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, BALANSEREGNSKAP, " ", " "
                , "Kontroll Summeringskontroller balanseregnskap, registrering av passiva (Gjeld og egenkapital)"
                , String.format("Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital (%d) i balanse.", sumPassiva)
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSumBalanseDifferanse(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 125) Aktiva skal være lik passiva. Differanser opptil ±10' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBalanseRegnskapList())) {
            return false;
        }

        final var sumAktiva = getSumAktiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));
        final var sumPassiva = getSumPassiva(arguments, regnskap, getBalanseRegnskapList(), getKontoklasseAsMap(arguments.getSkjema()).get("B"));
        final var sumDifferanse = sumAktiva + sumPassiva;

        if (!outsideOf(sumDifferanse, -10, 10)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, BALANSEREGNSKAP, " ", " "
                , "Kontroll Summeringskontroller balanseregnskap, differanse"
                , String.format("Korrigér differansen (%d) mellom aktiva (%d) og passiva (%d) i fila (Differanser opptil ±10' godtas)", sumDifferanse, sumAktiva, sumPassiva)
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlSkatteInntekter(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        // 130) Driftsregnskapet skal ha skatteinntekter; Kontoklasse D, Funksjon 800, art 870 < 0)
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getRegionaleBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler) || isCodeInCodeList(arguments.getRegion(), svalbard)) {
            return false;
        }

        final var sumSkatteInntekter = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("800")
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("870"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        if (0 > sumSkatteInntekter) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Skatteinntekter", " ", " "
                , String.format("Korrigér slik at fila inneholder skatteinntekter (%d).", sumSkatteInntekter)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlRammetilskudd(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        // 135) Rammetilskudd skal føres på funksjon 840. Ingen andre funksjoner skal være gyldig i kombinasjon med art 800.
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getRegionaleBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)
                || isCodeInCodeList(arguments.getRegion(), svalbard)
                // eget unntakstilfelle fra s212
                || isCodeInCodeList(arguments.getRegion(), List.of("501400"))) {
            return false;
        }

        final var sumRammetilskudd = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("840")
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("800"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        if (0 > sumRammetilskudd) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Rammetilskudd", " ", " "
                , String.format("Korrigér slik at fila inneholder rammetilskudd (%d)", sumRammetilskudd)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlOverforingMellomDriftOgInvestering(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 140
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumDriftsOverforinger = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("570"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        final var sumInvesteringsOverforinger = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("I"))
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("970"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        final var sumDifferanse = sumDriftsOverforinger + sumInvesteringsOverforinger;
        if (!outsideOf(sumDifferanse, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Overføring mellom drifts- og investeringsregnskap", " ", " "
                , String.format("Korrigér i fila slik at differansen (%d) i overføringer mellom drifts- (%d) og investeringsregnskapet (%d) stemmer overens.", sumDifferanse, sumDriftsOverforinger, sumInvesteringsOverforinger)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    private static int getMotpostAvskrivninger(final List<KostraRecord> regnskap, final Arguments arguments) {
        return regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("860")
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("990")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);
    }

    public static boolean controlMotpostAvskrivninger(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 145
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumMotpostAvskrivninger = getMotpostAvskrivninger(regnskap, arguments);
        if (sumMotpostAvskrivninger != 0) {
            return false;
        }

        // For 0I og 0K: Til informasjon / NO_ERROR, For alle andre: hard kontroll / CRITICAL_ERROR
        int errorType = (isCodeInCodeList(arguments.getSkjema(), List.of("0I", "0K"))) ? Constants.NO_ERROR : Constants.CRITICAL_ERROR;
        String errorText = (isCodeInCodeList(arguments.getSkjema(), List.of("0I", "0K")))
                ? String.format("Kun advarsel, hindrer ikke innsending. Korrigér i fila slik at den inneholder motpost avskrivninger (%d), føres på funksjon 860 og art 990.", sumMotpostAvskrivninger)
                : String.format("Korrigér i fila slik at den inneholder motpost avskrivninger (%d), føres på funksjon 860 og art 990.", sumMotpostAvskrivninger);

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Avskrivninger, motpost avskrivninger", " ", " "
                , errorText
                , ""
                , errorType));
        return true;
    }

    private static int getAvskrivninger(final List<KostraRecord> regnskap, final Arguments arguments) {
        return regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && between(p.getFieldAsIntegerDefaultEquals0(FUNKSJON_KAPITTEL), 100, 799)
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("590"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);
    }

    public static boolean controlAvskrivninger(final ErrorReport errorReport, final List<KostraRecord> regnskap) {
        errorReport.incrementCount();

        // 150
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumAvskrivninger = getAvskrivninger(regnskap, arguments);
        if (sumAvskrivninger != 0) {
            return false;
        }

        // For 0I og 0K: Til informasjon / NO_ERROR, For alle andre: hard kontroll / CRITICAL_ERROR
        final var errorType = (isCodeInCodeList(arguments.getSkjema(), List.of("0I", "0K"))) ? Constants.NO_ERROR : Constants.CRITICAL_ERROR;

        final var errorText = (isCodeInCodeList(arguments.getSkjema(), List.of("0I", "0K")))
                ? String.format("Kun advarsel, hindrer ikke innsending. Korrigér i fila slik at den inneholder avskrivninger (%d), føres på tjenestefunksjon og art 590.", sumAvskrivninger)
                : String.format("Korrigér i fila slik at den inneholder avskrivninger (%d), føres på tjenestefunksjon og art 590.", sumAvskrivninger);

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Avskrivninger", " ", " "
                , errorText
                , ""
                , errorType));
        return true;
    }

    public static boolean controlAvskrivningerDifferanse(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 155
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumMotpostAvskrivninger = getMotpostAvskrivninger(regnskap, arguments);
        final var sumAvskrivninger = getAvskrivninger(regnskap, arguments);
        final var sumDifferanse = sumMotpostAvskrivninger + sumAvskrivninger;

        if (!outsideOf(sumDifferanse, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Avskrivninger", " ", " "
                , "Kontroll Avskrivninger, differanse"
                , String.format("Korrigér i fila slik at avskrivninger (%d) stemmer overens med motpost avskrivninger (%d) (margin på +/- 30')", sumAvskrivninger, sumMotpostAvskrivninger)
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlAvskrivningerAndreFunksjoner(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 160
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumAvskrivningerAndreFunksjoner = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && between(p.getFieldAsIntegerDefaultEquals0(FUNKSJON_KAPITTEL), 800, 899)
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("590")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        final var avskrivningerAndreFunksjoner = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && between(p.getFieldAsIntegerDefaultEquals0(FUNKSJON_KAPITTEL), 800, 899)
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("590")
                )
                .map(p -> p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL)).toList();

        if (sumAvskrivningerAndreFunksjoner == 0) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Avskrivninger, avskrivninger ført på andre funksjoner", " ", " "
                , String.format("Korrigér i fila slik at avskrivningene føres på tjenestefunksjon og ikke på funksjonene (%s)", avskrivningerAndreFunksjoner)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlAvskrivningerMotpostAndreFunksjoner(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 165
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), getBevilgningRegnskapList())
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var sumMotpostAvskrivningerAndreFunksjoner = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && !p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("860")
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("990")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        final var motpostAvskrivningerAndreFunksjoner = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && !p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("860")
                        && p.getFieldAsString(ART_SEKTOR).equalsIgnoreCase("990")
                )
                .map(p -> p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL))
                .toList();

        if (sumMotpostAvskrivningerAndreFunksjoner == 0) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Avskrivninger, motpost avskrivninger ført på andre funksjoner", " ", " "
                , String.format("Korrigér i fila slik at motpost avskrivninger kun er ført på funksjon 860, art 990 og ikke på funksjonene (%s)", motpostAvskrivningerAndreFunksjoner)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlFunksjon290Investering(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 170) Funksjon I.290 for sum alle arter = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), List.of("0A", "0M"))
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var funksjon290Investering = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("I"))
                        && p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("290"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        if (!outsideOf(funksjon290Investering, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Funksjon 290, investeringsregnskapet", " ", " "
                , String.format("Korrigér i fila slik at differanse (%d) på funksjon 290 interkommunale samarbeid går i 0 i investeringsregnskapet . (margin på +/- 30')", funksjon290Investering)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlFunksjon290Drift(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 175) Funksjon D.290 for sum artene 010-590 + funksjon D.290 for sum artene 600-990 = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), List.of("0A", "0M"))
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var funksjon290Drift = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("290"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        if (!outsideOf(funksjon290Drift, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Funksjon 290, driftsregnskapet", " ", " "
                , String.format("Korrigér i fila slik at differanse (%d) på funksjon 290 interkommunale samarbeid (§27-samarbeid) går i 0 i driftsregnskapet . (margin på +/- 30')", funksjon290Drift)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlFunksjon465Investering(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 180) Funksjon I.465 for sum alle arter = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), List.of("0C", "0P"))
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var funksjon465Investering = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("I"))
                        && p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("465"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        if (!outsideOf(funksjon465Investering, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Funksjon 465", " ", " "
                , "Kontroll Funksjon 465, investeringsregnskapet"
                , String.format("Korrigér i fila slik at differanse (%d) på funksjon 465 Interfylkeskommunale samarbeid går i 0 i investeringsregnskapet . (margin på +/- 30')", funksjon465Investering)
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlFunksjon465Drift(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 185) Funksjon D.465 for sum artene 010-590 + funksjon D.465 for sum artene 600-990 = < 30 og > - 30. Differanser opptil +-30' godtas, og skal ikke utlistes.
        final var arguments = errorReport.getArgs();

        if (!isCodeInCodeList(arguments.getSkjema(), List.of("0C", "0P"))
                || isCodeInCodeList(arguments.getRegion(), osloBydeler)) {
            return false;
        }

        final var funksjon465Drift = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("D"))
                        && p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("465"))
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        if (!outsideOf(funksjon465Drift, -30, 30)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, "Kontroll Funksjon 465, driftsregnskapet", " ", " "
                , String.format("Korrigér i fila slik at differanse (%d) på funksjon 465 Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet . (margin på +/- 30')", funksjon465Drift)
                , ""
                , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean controlMemoriaKonti(
            final ErrorReport errorReport, final List<KostraRecord> regnskap) {

        errorReport.incrementCount();

        // 190)
        final var arguments = errorReport.getArgs();
        if (!isCodeInCodeList(arguments.getSkjema(), getBalanseRegnskapList())) {
            return false;
        }

        final var sumMemoriaKonti = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("B"))
                        && between(p.getFieldAsIntegerDefaultEquals0(FUNKSJON_KAPITTEL), 9100, 9200)
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        final var sumMotkontoMemoriaKonti = regnskap.stream()
                .filter(p -> p.getFieldAsString(KONTOKLASSE).equalsIgnoreCase(getKontoklasseAsMap(arguments.getSkjema()).get("B"))
                        && p.getFieldAsTrimmedString(FUNKSJON_KAPITTEL).equalsIgnoreCase("9999")
                )
                .map(p -> p.getFieldAsIntegerDefaultEquals0(BELOP))
                .reduce(0, Integer::sum);

        final var differanse = sumMemoriaKonti + sumMotkontoMemoriaKonti;
        if (!outsideOf(differanse, -10, 10)) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry(
                SUMMERINGSKONTROLLER, BALANSEREGNSKAP, " ", " "
                , "Kontroll Memoriakonti"
                , String.format("Kun advarsel, hindrer ikke innsending. Korrigér i fila slik at differansen (%d) mellom memoriakontiene (%d) og motkonto for memoriakontiene (%d) går i 0. (margin på +/- 10')", differanse, sumMemoriaKonti, sumMotkontoMemoriaKonti)
                , Constants.NO_ERROR));
        return true;
    }
}
