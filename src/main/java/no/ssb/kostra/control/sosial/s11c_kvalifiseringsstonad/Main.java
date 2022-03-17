package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad;

import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;
import no.ssb.kostra.utils.Fnr;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static no.ssb.kostra.control.sosial.felles.ControlSosial.*;

public class Main {
    // Konstanter
    private static final String ALDER = "ALDER";
    private static final String FNR = "PERSON_FODSELSNR";
    private static final String FNR_OK = "FNR_OK";
    private static final String KVP_STONAD = "KVP_STONAD";
    private static final List<String> STMND = List.of("STMND_1", "STMND_2", "STMND_3", "STMND_4", "STMND_5", "STMND_6", "STMND_7", "STMND_8", "STMND_9", "STMND_10", "STMND_11", "STMND_12");
    private static final String SAKSBEHANDLER = "SAKSBEHANDLER";
    private static final String JOURNALNR = "PERSON_JOURNALNR";
    private static final String ANTBU18 = "ANTBU18";
    private static final String STATUS = "STATUS";


    private Main() {
    }

    public static ErrorReport doControls(Arguments arguments) {
        ErrorReport errorReport = new ErrorReport(arguments);
        errorReport.incrementCount();
        errorReport.setReportHeaders(List.of("Saksbehandler", "Journalnummer", "Kontroll", "Melding"));
        List<String> inputFileContent = arguments.getInputContentAsStringList();


        // Sjekker om man skal ha vedlegg
        if (ControlHarVedlegg.doControl(errorReport)) {
            return errorReport;
        }

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        if (ControlRecordLengde.doControl(inputFileContent, errorReport, FieldDefinitions.getFieldLength())) {
            return errorReport;
        }

        var fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        var records = inputFileContent.stream()
                .map(p -> new KostraRecord(p, fieldDefinitions))
                // utled ALDER og sett flagget FNR_OK i forhold til om ALDER lot seg utlede
                .peek(r -> {
                    try {
                        r.setFieldAsInteger(ALDER, Fnr.getAlderFromFnr(dnr2fnr(r.getFieldAsString(FNR)), arguments.getAargang()));
                        r.setFieldAsInteger(FNR_OK, 1);

                    } catch (Exception e) {
                        r.setFieldAsInteger(ALDER, -1);
                        r.setFieldAsInteger(FNR_OK, 0);
                    }

                })
                .collect(Collectors.toList());

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, errorReport);

        if (!errorReport.getArgs().getRegion().substring(0, 4).equalsIgnoreCase("0301")) {
            control05AFodselsnummerDubletter(errorReport, records);
            control05BJournalnummerDubletter(errorReport, records);
        }

        records.forEach(r -> {
            control03Kommunenummer(errorReport, r);
            control03Bydelsnummer(errorReport, r);
            control04OppgaveAar(errorReport, r);
            control05Fodselsnummer(errorReport, r);
            control06AlderUnder18Aar(errorReport, r);
            control07AlderEr68AarEllerOver(errorReport, r);
            control08Kjonn(errorReport, r);
            control09Sivilstand(errorReport, r);
            control10Bu18(errorReport, r);
            control11Bu18AntBu18(errorReport, r);
            control12AntBu18Bu18(errorReport, r);
            control13AntBu18(errorReport, r);
            control14RegDato(errorReport, r);
            control15VedtakDato(errorReport, r);
            control16BegyntDato(errorReport, r);
            control19KvalifiseringsprogramIAnnenKommune(errorReport, r);
            control20KvalifiseringsprogramIAnnenKommuneKommunenummer(errorReport, r);
            control20AFraKvalifiseringsprogramIAnnenBydelIOslo(errorReport, r);
            control21Ytelser(errorReport, r);
            control26MottattStotte(errorReport, r);
            control27MottattOkonomiskSosialhjelp(errorReport, r);
            control28MaanederMedKvalifiseringsstonad(errorReport, r);
            control29KvalifiseringssumMangler(errorReport, r);
            control30HarVarighetMenManglerKvalifiseringssum(errorReport, r);
            control31HarKvalifiseringssumMenManglerVarighet(errorReport, r);
            control32KvalifiseringssumOverMaksimum(errorReport, r);
            control33KvalifiseringssumUnderMinimum(errorReport, r);
            control36StatusForDeltakelseIKvalifiseringsprogram(errorReport, r);
            control37DatoForAvsluttetProgram(errorReport, r);
            control38FullforteAvsluttedeProgramSituasjon(errorReport, r);
            control39FullforteAvsluttedeProgramInntektkilde(errorReport, r);
        });
        // Kontroller ferdig
        // Lager statistikkrapport
        {
            if (errorReport.getErrorType() < Constants.CRITICAL_ERROR) {

                Integer stonadSum = records.stream().map(r -> r.getFieldAsIntegerDefaultEquals0(KVP_STONAD)).reduce(0, Integer::sum);

                errorReport.addStats(new StatsReportEntry(
                        "Sum"
                        , List.of(
                        new Code("STONAD", "Stønad")
                )
                        , List.of(
                        new StatsEntry("STONAD", stonadSum.toString())
                )
                ));

                List<Integer> gyldigeRecordsAlder = records.stream().filter(r -> r.getFieldAsInteger(FNR_OK) == 1 && r.getFieldAsInteger(ALDER) != -1).map(r -> r.getFieldAsInteger(ALDER)).collect(Collectors.toList());
                errorReport.addStats(new StatsReportEntry(
                        "Kvalifiseringsdeltakere"
                        , List.of(
                        new Code("I_ALT", "I alt")
                        , new Code("0_19", "19 år eller yngre")
                        , new Code("20_24", "20 - 24 år")
                        , new Code("25_29", "25 - 29 år")
                        , new Code("30_39", "30 - 39 år")
                        , new Code("40_49", "40 - 49 år")
                        , new Code("50_66", "50 - 66 år")
                        , new Code("67_999", "67 år eller eldre")
                        , new Code("UGYLDIG_FNR", "Alder ikke mulig å beregne")
                )
                        , List.of(
                        new StatsEntry("I_ALT", String.valueOf(records.size()))
                        , new StatsEntry("0_19", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 0, 19)).count()))
                        , new StatsEntry("20_24", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 20, 24)).count()))
                        , new StatsEntry("25_29", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 25, 29)).count()))
                        , new StatsEntry("30_39", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 30, 39)).count()))
                        , new StatsEntry("40_49", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 40, 49)).count()))
                        , new StatsEntry("50_66", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 45, 66)).count()))
                        , new StatsEntry("67_999", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 67, 999)).count()))
                        , new StatsEntry("UGYLDIG_FNR", String.valueOf(records.stream().filter(r -> r.getFieldAsInteger(FNR_OK) == 0).count()))
                )
                ));

                List<Long> gyldigeRecordsStonadstid = records.stream()
                        .map(r -> STMND
                                .stream()
                                .filter(m -> 0 < r.getFieldAsIntegerDefaultEquals0(m))
                                .count()
                        )
                        .collect(Collectors.toList());
                errorReport.addStats(new StatsReportEntry(
                        "Stønadsvarighet"
                        , List.of(
                        new Code("1", "1 måned")
                        , new Code("2_3", "2 - 3 måneder")
                        , new Code("4_6", "4 - 6 måneder")
                        , new Code("7_9", "7 - 9 måneder")
                        , new Code("10_11", "10 - 11 måneder")
                        , new Code("12", "12 måneder")
                        , new Code("UOPPGITT", "Uoppgitt")
                )
                        , List.of(
                        new StatsEntry("1", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> i == 1).count()))
                        , new StatsEntry("2_3", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 2, 3)).count()))
                        , new StatsEntry("4_6", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 4, 6)).count()))
                        , new StatsEntry("7_9", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 7, 9)).count()))
                        , new StatsEntry("10_11", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 10, 11)).count()))
                        , new StatsEntry("12", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 12, 12)).count()))
                        , new StatsEntry("UOPPGITT", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> i == 0).count()))
                )
                ));

                List<Integer> gyldigeRecordsStonad = records.stream()
                        .map(r -> r.getFieldAsIntegerDefaultEquals0(KVP_STONAD))
                        .collect(Collectors.toList());
                errorReport.addStats(new StatsReportEntry(
                        "Stønad"
                        , List.of(
                        new Code("1_7", " - 7999 kr")
                        , new Code("8_49", "8000 - 49999 kr")
                        , new Code("50_99", "50000 - 99999 kr")
                        , new Code("100_149", "100000 - 149999 kr")
                        , new Code("150_9999", "150000 kr og over")
                        , new Code("0", "Uoppgitt")
                )
                        , List.of(
                        new StatsEntry("1_7", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 1, 7999)).count()))
                        , new StatsEntry("8_49", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 8000, 49999)).count()))
                        , new StatsEntry("50_99", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 50000, 99999)).count()))
                        , new StatsEntry("100_149", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 100000, 149999)).count()))
                        , new StatsEntry("150_9999", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> 150000 <= i).count()))
                        , new StatsEntry("0", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> i == 0).count()))
                )
                ));
            }
        }

        return errorReport;
    }

    public static boolean control10Bu18(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 10 Forsørgerplikt for barn under 18 år i husholdningen. Gyldige verdier"
                        , "Korrigér forsørgerplikt. Fant '" + r.getFieldAsTrimmedString("BU18") + "', "
                        + "forventet én av " + r.getFieldDefinitionByName("BU18").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + "'. "
                        + "Det er ikke krysset av for om deltakeren har barn under 18 år, "
                        + "som deltakeren (eventuelt ektefelle/samboer) har forsørgerplikt for, og som bor i husholdningen ved siste kontakt. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString("BU18")
                , r.getFieldDefinitionByName("BU18").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
        );
    }

    public static boolean control11Bu18AntBu18(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 11 Det bor barn under 18 år i husholdningen. Mangler antall barn."
                        , "Det er krysset av for at det bor barn under 18 år i husholdningen som mottaker eller ektefelle/samboer har forsørgerplikt for, "
                        + "men det er ikke oppgitt hvor mange barn '(" + r.getFieldAsIntegerDefaultEquals0(ANTBU18) + ")' som bor i husholdningen. "
                        + "Feltet er obligatorisk å fylle ut når det er oppgitt at det bor barn under 18 år i husholdningen."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString("BU18")
                , List.of("1")
                , r.getFieldAsIntegerDefaultEquals0(ANTBU18)
                , ">"
                , 0
        );
    }

    public static boolean control12AntBu18Bu18(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        return ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 12 Det bor barn under 18 år i husholdningen."
                        , "Det er oppgitt " + r.getFieldAsIntegerDefaultEquals0(ANTBU18) + " barn under 18 år som bor i husholdningen som "
                        + "mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke "
                        + "oppgitt at det bor barn i husholdningen. "
                        + "Feltet er obligatorisk å fylle ut når det er oppgitt antall barn under 18 år som bor i husholdningen."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsIntegerDefaultEquals0(ANTBU18)
                , ">"
                , 0
                , r.getFieldAsString("BU18")
                , List.of("1")
        );
    }

    public static boolean control13AntBu18(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        return ControlFelt1Boolsk.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 13 Det bor barn under 18 år i husholdningen."
                        , "Antall barn (" + r.getFieldAsTrimmedString(ANTBU18) + ") under 18 år i husholdningen er 11 eller flere, er dette riktig?"
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsIntegerDefaultEquals0(ANTBU18)
                , "<="
                , 10
        );
    }

    public static boolean control14RegDato(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        final String REG_DATO = "REG_DATO";

        return ControlFelt1Dato.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 14 Dato for registrert søknad ved NAV-kontoret."
                        , "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' mangler utfylling eller har ugyldig dato (" + r.getFieldAsString(REG_DATO) + "). Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(REG_DATO)
                , r.getFieldDefinitionByName(REG_DATO).getDatePattern()
        );
    }

    public static boolean control15VedtakDato(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        final String VEDTAK_DATO = "VEDTAK_DATO";

        return ControlFelt1Dato.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 15 Dato for fattet vedtak om program (søknad innvilget)"
                        , "Feltet for 'Hvilken dato det ble fattet vedtak om program (søknad innvilget)' mangler utfylling eller har ugyldig dato (" + r.getFieldAsString(VEDTAK_DATO) + "). Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(VEDTAK_DATO)
                , r.getFieldDefinitionByName(VEDTAK_DATO).getDatePattern()
        );
    }

    public static boolean control16BegyntDato(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        final String BEGYNT_DATO = "BEGYNT_DATO";

        return ControlFelt1Dato.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 16 Dato for når deltakeren begynte i program (iverksettelse)."
                        , "Feltet for 'Hvilken dato begynte deltakeren i program? (iverksettelse)' mangler utfylling eller har ugyldig dato (" + r.getFieldAsString(BEGYNT_DATO) + "). Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(BEGYNT_DATO)
                , r.getFieldDefinitionByName(BEGYNT_DATO).getDatePattern()
        );
    }

    public static boolean control19KvalifiseringsprogramIAnnenKommune(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        final String KVP_KOMM = "KVP_KOMM";

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 19 Kvalifiseringsprogram i annen kommune"
                        , "Feltet for 'Kommer deltakeren fra kvalifiseringsprogram i annen kommune?' er ikke fylt ut, eller feil kode er benyttet (" + r.getFieldAsString(KVP_KOMM) + "). Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(KVP_KOMM)
                , r.getFieldDefinitionByName(KVP_KOMM).getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
        );
    }

    public static boolean control20KvalifiseringsprogramIAnnenKommuneKommunenummer(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        final String KOMMNR_KVP_KOMM = "KOMMNR_KVP_KOMM";
        final String KVP_KOMM = "KVP_KOMM";
        final String kvp_komm = r.getFieldAsString(KVP_KOMM);
        final String kommnr_kvp_komm = r.getFieldAsString(KOMMNR_KVP_KOMM);

        final Code kvpKommCode = r.getFieldDefinitionByName(KVP_KOMM)
                .getCodeList()
                .stream()
                .filter(item -> kvp_komm.equalsIgnoreCase(item.getCode()))
                .findFirst()
                .orElse(new Code(" ", "Uoppgitt"));

        final List<String> kommnrKvpKommCodes = r.getFieldDefinitionByName(KOMMNR_KVP_KOMM)
                .getCodeList()
                .stream()
                .map(Code::getCode)
                .collect(Collectors.toList());

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 20 Kvalifiseringsprogram i annen kommune. Kommunenummer."
                        , String.format("Det er svart '%s' på om deltakeren kommer fra kvalifiseringsprogram i annen kommune "
                                + ", men kommunenummer ('%s') mangler eller er ugyldig. Feltet er obligatorisk å fylle ut.", kvpKommCode, kommnr_kvp_komm)
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(KVP_KOMM)
                , List.of("1")
                , r.getFieldAsString(KOMMNR_KVP_KOMM)
                , kommnrKvpKommCodes
        );
    }

    public static boolean control20AFraKvalifiseringsprogramIAnnenBydelIOslo(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 20a Fra kvalifiseringsprogram i annen bydel i Oslo."
                        , "Manglende/ugyldig utfylling for om deltakeren kommer fra kvalifiseringsprogram i annen bydel (). Feltet er obligatorisk å fylle ut for Oslo."
                        , Constants.NORMAL_ERROR
                )
                , r.getFieldAsString("KOMMUNE_NR")
                , List.of("0301")
                , r.getFieldAsString("KVP_OSLO")
                , r.getFieldDefinitionByName("KVP_OSLO").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
        );
    }

    public static boolean control21Ytelser(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        final String YTELSE_SOSHJELP = "YTELSE_SOSHJELP";
        final String YTELSE_TYPE_SOSHJ = "YTELSE_TYPE_SOSHJ";

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 21 Ytelser de to siste månedene før registrert søknad ved NAV-kontoret"
                        , "Feltet for 'Hadde deltakeren i løpet av de siste to månedene før registrert søknad ved NAV-kontoret en eller flere av følgende ytelser?' inneholder ugyldige verdier."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(YTELSE_SOSHJELP)
                , List.of("1")
                , r.getFieldAsString(YTELSE_TYPE_SOSHJ)
                , r.getFieldDefinitionByName(YTELSE_TYPE_SOSHJ).getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
        );
    }

    public static boolean control26MottattStotte(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        final String KVP_MED_ASTONAD = "KVP_MED_ASTONAD";

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 26 Mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " + errorReport.getArgs().getAargang()
                        , "Feltet for 'Har deltakeren i " + errorReport.getArgs().getAargang() + " i løpet av perioden med kvalifiseringsstønad også mottatt  økonomisk sosialhjelp, "
                        + "kommunal bostøtte eller Husbankens bostøtte?', er ikke utfylt eller feil kode (" + r.getFieldAsString(KVP_MED_ASTONAD) + ") er benyttet. Feltet er obligatorisk å fylle ut."
                        + r.getFieldDefinitionByName(KVP_MED_ASTONAD).getCodeList().stream().collect(Collectors.toMap(Code::getCode, Code::getValue))
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(KVP_MED_ASTONAD)
                , r.getFieldDefinitionByName(KVP_MED_ASTONAD).getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
        );
    }

    public static boolean control27MottattOkonomiskSosialhjelp(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        // Kontroll 27 sjekker at flere felt skal være utfylt hvis KVP_MED_ASTONAD = 1 (Ja)
        // Disse feltene sjekkes hver for seg med en ferdig control.
        // Derfor gjentas kontroll 27 for hver av dem
        boolean hasErrors = false;
        List<String> fields = List.of("KVP_MED_KOMMBOS", "KVP_MED_HUSBANK", "KVP_MED_SOSHJ_ENGANG", "KVP_MED_SOSHJ_PGM", "KVP_MED_SOSHJ_SUP");

        Map<String, String> values = fields.stream()
                .collect(Collectors.toMap(s -> s, r::getFieldAsString));

        if (Objects.equals(r.getFieldAsString("KVP_MED_ASTONAD"), "1")) {
            boolean isNoneFilledIn = fields.stream()
                    .noneMatch(field -> r.getFieldDefinitionByName(field).getCodeList().stream().map(Code::getCode).collect(Collectors.toList()).contains(r.getFieldAsString(field)));

            if (isNoneFilledIn) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                r.getFieldAsString(SAKSBEHANDLER)
                                , r.getFieldAsString(JOURNALNR)
                                , r.getFieldAsString(FNR)
                                , " "
                                , "Kontroll 27 Mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " + errorReport.getArgs().getAargang() + ". Svaralternativer."
                                , "Svaralternativer for feltet \"Har deltakeren i " + errorReport.getArgs().getAargang() + " i løpet av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, "
                                + "kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. Det er mottatt støtte. " + values
                                , Constants.CRITICAL_ERROR
                        )
                );

                hasErrors = true;
            }

        } else if (Objects.equals(r.getFieldAsString("KVP_MED_ASTONAD"), "2")) {
            boolean isAllBlank = fields.stream()
                    .allMatch(field -> Comparator.isCodeInCodeList(r.getFieldAsString(field), List.of(" ", "0")));

            if (!isAllBlank) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                r.getFieldAsString(SAKSBEHANDLER)
                                , r.getFieldAsString(JOURNALNR)
                                , r.getFieldAsString(FNR)
                                , " "
                                , "Kontroll 27 Ikke mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " + errorReport.getArgs().getAargang() + ". Svaralternativer."
                                , "Svaralternativer for feltet \"Har deltakeren i " + errorReport.getArgs().getAargang() + " i løpet av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, "
                                + "kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. Det er IKKE mottatt støtte. " + values
                                , Constants.CRITICAL_ERROR
                        )
                );

                hasErrors = true;
            }
        }

        return hasErrors;
    }

    // Kontrollene 28-33 sjekker at koblingen mellom én av flere stønadsmåneder (som skal være utfylt) og stønadssumfelt
    public static boolean control28MaanederMedKvalifiseringsstonad(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        boolean harVarighet = STMND.stream()
                .anyMatch(field -> r.getFieldDefinitionByName(field)
                        .getCodeList()
                        .stream()
                        .map(Code::getCode)
                        .collect(Collectors.toList())
                        .contains(r.getFieldAsString(field)));

        boolean harPermisjon = r.getFieldAsString(STATUS).equalsIgnoreCase("2");

        if (!harVarighet && !harPermisjon) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            r.getFieldAsString(SAKSBEHANDLER)
                            , r.getFieldAsString(JOURNALNR)
                            , r.getFieldAsString(FNR)
                            , " "
                            , "Kontroll 28 Måneder med kvalifiseringsstønad. Gyldige koder."
                            , "Det er ikke krysset av for hvilke måneder deltakeren har fått utbetalt kvalifiseringsstønad (" + r.getFieldAsString(KVP_STONAD) + ") i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
            );

            return true;
        }

        return false;
    }

    public static boolean control29KvalifiseringssumMangler(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        Integer stonad = r.getFieldAsInteger(KVP_STONAD);
        boolean stonadOK = (stonad != null);

        if (!stonadOK) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            r.getFieldAsString(SAKSBEHANDLER)
                            , r.getFieldAsString(JOURNALNR)
                            , r.getFieldAsString(FNR)
                            , " "
                            , "Kontroll 29 Kvalifiseringssum mangler eller har ugyldige tegn."
                            , "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad (" + r.getFieldAsString(KVP_STONAD) + ") i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
            );

            return true;
        }

        return false;
    }

    public static boolean control30HarVarighetMenManglerKvalifiseringssum(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        Integer stonad = r.getFieldAsInteger(KVP_STONAD);
        boolean stonadOK = (stonad != null);
        boolean harVarighet = STMND.stream()
                .anyMatch(field -> r.getFieldDefinitionByName(field)
                        .getCodeList()
                        .stream()
                        .map(Code::getCode)
                        .collect(Collectors.toList())
                        .contains(r.getFieldAsString(field)));

        if (harVarighet && !stonadOK) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            r.getFieldAsString(SAKSBEHANDLER)
                            , r.getFieldAsString(JOURNALNR)
                            , r.getFieldAsString(FNR)
                            , " "
                            , "Kontroll 30 Har varighet, men mangler kvalifiseringssum."
                            , "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad (" + r.getFieldAsString(KVP_STONAD) + ") i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
            );

            return true;

        }

        return false;
    }

    public static boolean control31HarKvalifiseringssumMenManglerVarighet(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        Integer stonad = r.getFieldAsInteger(KVP_STONAD);
        boolean stonadOK = (stonad != null);
        boolean harVarighet = STMND.stream()
                .anyMatch(field -> r.getFieldDefinitionByName(field)
                        .getCodeList()
                        .stream()
                        .map(Code::getCode)
                        .collect(Collectors.toList())
                        .contains(r.getFieldAsString(field)));

        if (stonadOK && 0 < stonad && !harVarighet) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            r.getFieldAsString(SAKSBEHANDLER)
                            , r.getFieldAsString(JOURNALNR)
                            , r.getFieldAsString(FNR)
                            , " "
                            , "Kontroll 31 Har kvalifiseringssum, men mangler varighet."
                            , "Deltakeren har fått kvalifiseringsstønad (" + r.getFieldAsString(KVP_STONAD) + ") i løpet av året, men mangler utfylling for hvilke måneder stønaden gjelder. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
            );

            return true;
        }

        return false;
    }

    public static boolean control32KvalifiseringssumOverMaksimum(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        Integer stonad = r.getFieldAsInteger(KVP_STONAD);
        boolean stonadOK = (stonad != null);
        int stonadSumMax = 235000;

        if (stonadOK && stonadSumMax < stonad) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            r.getFieldAsString(SAKSBEHANDLER)
                            , r.getFieldAsString(JOURNALNR)
                            , r.getFieldAsString(FNR)
                            , " "
                            , "Kontroll 32 Kvalifiseringssum på kr " + stonadSumMax + ",- eller mer."
                            , "Kvalifiseringsstønaden (" + r.getFieldAsString(KVP_STONAD) + ") som deltakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMax + ",-."
                            , Constants.NORMAL_ERROR
                    )
            );

            return true;
        }

        return false;
    }

    public static boolean control33KvalifiseringssumUnderMinimum(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        Integer stonad = r.getFieldAsInteger(KVP_STONAD);
        boolean stonadOK = (stonad != null);
        int stonadSumMin = 8000;

        if (stonadOK && stonad < stonadSumMin) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            r.getFieldAsString(SAKSBEHANDLER)
                            , r.getFieldAsString(JOURNALNR)
                            , r.getFieldAsString(FNR)
                            , " "
                            , "Kontroll 33 Kvalifiseringsstønad på kr " + stonadSumMin + ",- eller lavere."
                            , "Kvalifiseringsstønaden (" + r.getFieldAsString(KVP_STONAD) + ") som deltakeren har fått i løpet av rapporteringsåret er lavere enn Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMin + ",-."
                            , Constants.NORMAL_ERROR
                    )
            );

            return true;
        }

        return false;
    }

    public static boolean control36StatusForDeltakelseIKvalifiseringsprogram(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 36 Status for deltakelse i kvalifiseringsprogram per 31.12." + errorReport.getArgs().getAargang() + ". Gyldige verdier."
                        , "Korrigér status. Fant '" + r.getFieldAsTrimmedString(STATUS) + "', forventet én av '"
                        + r.getFieldDefinitionByName(STATUS).getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + "'. "
                        + "Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(STATUS)
                , r.getFieldDefinitionByName(STATUS).getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
        );
    }

    public static boolean control37DatoForAvsluttetProgram(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();
        final String AVSL_DATO = "AVSL_DATO";
        String status = r.getFieldAsString(STATUS);
        List<String> codes = r.getFieldDefinitionByName(STATUS)
                .getCodeList()
                .stream()
                .map(Code::getCode)
                .filter(code -> Comparator.isCodeInCodeList(code, List.of("3", "4", "5")))
                .collect(Collectors.toList());

        if (Comparator.isCodeInCodeList(status, codes)) {
            return ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            r.getFieldAsString(SAKSBEHANDLER)
                            , r.getFieldAsString(JOURNALNR)
                            , r.getFieldAsString(FNR)
                            , " "
                            , "Kontroll 37 Dato for avsluttet program (gjelder fullførte, avsluttede etter avtale og varig avbrutte program, ikke for permisjoner) (DDMMÅÅ)."
                            , "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', fant (" + r.getFieldAsString(AVSL_DATO) + "), må fylles ut dersom det er krysset av for svaralternativ "
                            + r.getFieldDefinitionByName(STATUS).getCodeList().stream().filter(c -> Comparator.isCodeInCodeList(c.getCode(), List.of("3", "4", "5"))).map(Code::toString).collect(Collectors.toList())
                            + " under feltet for 'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12." + errorReport.getArgs().getAargang() + "'?"
                            , Constants.CRITICAL_ERROR
                    )
                    , r.getFieldAsString(STATUS)
                    , r.getFieldDefinitionByName(STATUS).getCodeList().stream()
                            .map(Code::getCode)
                            .filter(code -> Comparator.isCodeInCodeList(code, List.of("3", "4", "5")))
                            .collect(Collectors.toList())
                    , r.getFieldAsLocalDate(AVSL_DATO)
            );

        } else {
            return ControlFelt1InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            r.getFieldAsString(SAKSBEHANDLER)
                            , r.getFieldAsString(JOURNALNR)
                            , r.getFieldAsString(FNR)
                            , " "
                            , "Kontroll 37 Dato for avsluttet program (gjelder fullførte, avsluttede etter avtale og varig avbrutte program, ikke for permisjoner) (DDMMÅÅ)."
                            , "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', fant (" + r.getFieldAsString(AVSL_DATO) + "), skal være blankt dersom det er krysset av for svaralternativ "
                            + r.getFieldDefinitionByName(STATUS).getCodeList().stream().filter(c -> !Comparator.isCodeInCodeList(c.getCode(), List.of("3", "4", "5"))).map(Code::toString).collect(Collectors.toList())
                            + " under feltet for 'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12." + errorReport.getArgs().getAargang() + "'?"
                            , Constants.CRITICAL_ERROR
                    )
                    , r.getFieldAsString(AVSL_DATO)
                    , List.of("      ")
            );

        }
    }

    public static boolean control38FullforteAvsluttedeProgramSituasjon(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        if (r.getFieldAsString(STATUS).equalsIgnoreCase("3")) {
            List<String> fields = List.of("AVSL_ORDINAERTARB", "AVSL_ARBLONNSTILS", "AVSL_ARBMARK", "AVSL_SKOLE", "AVSL_UFORE", "AVSL_AAP", "AVSL_OK_AVKLAR", "AVSL_UTEN_OK_AVKLAR", "AVSL_ANNET", "AVSL_UKJENT");
            boolean isNoneFilledIn = fields.stream()
                    .noneMatch(field -> r.getFieldDefinitionByName(field).getCodeList().stream().map(Code::getCode).collect(Collectors.toList()).contains(r.getFieldAsString(field)));

            if (isNoneFilledIn) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                r.getFieldAsString(SAKSBEHANDLER)
                                , r.getFieldAsString(JOURNALNR)
                                , r.getFieldAsString(FNR)
                                , " "
                                , "Kontroll 38 Fullførte/avsluttede program – til hvilken situasjon gikk deltakeren? Gyldige verdier."
                                , "Feltet 'Ved fullført program eller program avsluttet etter avtale (gjelder ikke flytting) – hva var deltakerens situasjon umiddelbart etter avslutningen'? "
                                + "Må fylles ut dersom det er krysset av for svaralternativ 3 = Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke flytting) under feltet for "
                                + "'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12." + errorReport.getArgs().getAargang() + "'?"
                                , Constants.CRITICAL_ERROR
                        )
                );

                return true;
            }
        }

        return false;
    }

    public static boolean control39FullforteAvsluttedeProgramInntektkilde(ErrorReport errorReport, KostraRecord r) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        r.getFieldAsString(SAKSBEHANDLER)
                        , r.getFieldAsString(JOURNALNR)
                        , r.getFieldAsString(FNR)
                        , " "
                        , "Kontroll 39 Fullførte/avsluttede program – til hvilken inntektskilde gikk deltakeren? Gyldige verdier."
                        , "Feltet 'Hva var deltakerens <b>viktigste</b> inntektskilde umiddelbart etter avslutningen? "
                        + "Må fylles ut dersom det er krysset av for svaralternativ 3 = Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke flytting) "
                        + "under feltet for 'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12." + errorReport.getArgs().getAargang() + "'?"
                        , Constants.CRITICAL_ERROR
                )
                , r.getFieldAsString(STATUS)
                , List.of("3")
                , r.getFieldAsString("AVSL_VIKTIGSTE_INNTEKT")
                , r.getFieldDefinitionByName("AVSL_VIKTIGSTE_INNTEKT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
        );
    }
}
