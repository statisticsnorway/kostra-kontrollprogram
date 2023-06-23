package no.ssb.kostra.control.sosial.s11_sosialhjelp;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.control.felles.ControlFelt1Boolsk;
import no.ssb.kostra.control.felles.ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlFodselsnummerDUFnummer;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Code;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.KostraRecord;
import no.ssb.kostra.felles.StatsEntry;
import no.ssb.kostra.felles.StatsReportEntry;
import no.ssb.kostra.utils.Fnr;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.ssb.kostra.control.sosial.felles.ControlSosial.control03Bydelsnummer;
import static no.ssb.kostra.control.sosial.felles.ControlSosial.control03Kommunenummer;
import static no.ssb.kostra.control.sosial.felles.ControlSosial.control04OppgaveAar;
import static no.ssb.kostra.control.sosial.felles.ControlSosial.control05AFodselsnummerDubletter;
import static no.ssb.kostra.control.sosial.felles.ControlSosial.control05BJournalnummerDubletter;
import static no.ssb.kostra.control.sosial.felles.ControlSosial.control05Fodselsnummer;
import static no.ssb.kostra.control.sosial.felles.ControlSosial.control08Kjonn;
import static no.ssb.kostra.control.sosial.felles.ControlSosial.control09Sivilstand;
import static no.ssb.kostra.control.sosial.felles.ControlSosial.dnr2fnr;


@SuppressWarnings("SpellCheckingInspection")
public class Main {
    private static final String ALDER = "ALDER";
    private static final String FNR_OK = "FNR_OK";
    private static final String SAKSBEHANDLER = "SAKSBEHANDLER";
    private static final String PERSON_JOURNALNR = "PERSON_JOURNALNR";
    private static final String PERSON_FODSELSNR = "PERSON_FODSELSNR";

    private static final String STONAD = "STONAD";
    private static final String BIDRAG = "BIDRAG";
    private static final String LAAN = "LAAN";
    private static final List<String> STMND = List.of("STMND_1", "STMND_2", "STMND_3", "STMND_4", "STMND_5", "STMND_6", "STMND_7", "STMND_8", "STMND_9", "STMND_10", "STMND_11", "STMND_12");
    private static final String UOPPGITT = "Uoppgitt";
    private static final String ANTBU18 = "ANTBU18";
    private static final String VKLO = "VKLO";
    private static final String TRYGDESIT = "TRYGDESIT";
    private static final String ARBSIT = "ARBSIT";
    private static final String GITT_OKONOMIRAD = "GITT_OKONOMIRAD";
    private static final String FAAT_INDIVIDUELL_PLAN = "FAAT_INDIVIDUELL_PLAN";
    private static final String BOSIT = "BOSIT";
    private static final String VILKARSOSLOV = "VILKARSOSLOV";

    private static final String VKLO_ERRORTEXT = "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er %s. Arbeidssituasjonen er '%s', forventet én av '%s'. Feltet er obligatorisk å fylle ut.";


    private Main() {
    }

    public static ErrorReport doControls(final Arguments arguments) {

        final var errorReport = new ErrorReport(arguments);
        errorReport.setReportHeaders(List.of("Saksbehandler", "Journalnummer", "Kontroll", "Feilmelding"));
        final var inputFileContent = arguments.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        final var hasErrors = ControlRecordLengde.doControl(inputFileContent, errorReport, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return errorReport;
        }

        final var fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        final var records = inputFileContent.stream()
                .map(p -> new KostraRecord(p, fieldDefinitions))
                // utled ALDER og sett flagget FNR_OK i forhold til om ALDER lot seg utlede
                .peek(r -> {
                    try {
                        r.setFieldAsInteger(ALDER, Fnr.getAlderFromFnr(dnr2fnr(r.getFieldAsString(PERSON_FODSELSNR)), arguments.getAargang()));
                        r.setFieldAsInteger(FNR_OK, (Fnr.isValidNorwId(r.getFieldAsString(PERSON_FODSELSNR))) ? 1 : 0);

                    } catch (Exception e) {
                        r.setFieldAsInteger(ALDER, -1);
                        r.setFieldAsInteger(FNR_OK, -1);
                    }
                })
                .toList();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, errorReport);

        records.forEach(currentRecord -> {
            control03Kommunenummer(errorReport, currentRecord);
            control03Bydelsnummer(errorReport, currentRecord);
            control04OppgaveAar(errorReport, currentRecord);
            control05Fodselsnummer(errorReport, currentRecord);
            control08Kjonn(errorReport, currentRecord);
            control09Sivilstand(errorReport, currentRecord);

            control10ForsorgerpliktForBarnUnder18Aar(errorReport, currentRecord);
            control11AntallBarnIHusholdningenMangler(errorReport, currentRecord);
            control12AntallBarnIHusholdningen(errorReport, currentRecord);
            control13MangeBarnIHusholdningen(errorReport, currentRecord);
            control14ViktigsteKildeTilLivsoppholdGyldigeVerdier(errorReport, currentRecord);
            control15ViktigsteKildeTilLivsopphold(errorReport, currentRecord);
            control16ViktigsteKildeTilLivsopphold(errorReport, currentRecord);
            control17ViktigsteKildeTilLivsopphold(errorReport, currentRecord);
            control18ViktigsteKildeTilLivsopphold(errorReport, currentRecord);
            control19ViktigsteKildeTilLivsopphold(errorReport, currentRecord);
            control20ViktigsteKildeTilLivsopphold(errorReport, currentRecord);
// TODO: aktiveres for 2023-data            control21ViktigsteKildeTilLivsopphold(errorReport, currentRecord);
            control22TilknytningTilTrygdesystemetOgAlder(errorReport, currentRecord);
            control23TilknytningTilTrygdesystemetOgBarn(errorReport, currentRecord);
            control24TilknytningTilTrygdesystemetOgArbeidssituasjon(errorReport, currentRecord);
            control24BTilknytningTilTrygdesystemetOgArbeidssituasjonArbeidsavklaringspenger(errorReport, currentRecord);
            control25ArbeidssituasjonGyldigeKoder(errorReport, currentRecord);
            control26StonadsmaanederGyldigeKoder(errorReport, currentRecord);
            control27StonadssumManglerEllerHarUgyldigeTegn(errorReport, currentRecord);
            control28HarVarighetMenManglerStonadssum(errorReport, currentRecord);
            control29HarStonadssumMenManglerVarighet(errorReport, currentRecord);
            control30StonadssumPaaMaxEllerMer(errorReport, currentRecord);
            control31StonadssumPaaMinEllerMindre(errorReport, currentRecord);
            control32OkonomiskraadgivningGyldigeKoder(errorReport, currentRecord);
            control33UtarbeidelseAvIndividuellPlan(errorReport, currentRecord);
            control35Boligsituasjon(errorReport, currentRecord);
            control36BidragFordeltPaaMmaaneder(errorReport, currentRecord);
            control37LaanFordeltPaaMmaaneder(errorReport, currentRecord);
            control38DUFNummer(errorReport, currentRecord);
            control39ForsteVilkarIAaret(errorReport, currentRecord);
            control40ForsteVilkarIAaretSambo(errorReport, currentRecord);
            control41DatoForUtbetalingsvedtak(errorReport, currentRecord);
            control42TilOgMedDatoForUtbetalingsvedtak(errorReport, currentRecord);
            control43Vilkaar(errorReport, currentRecord);
        });

        control05AFodselsnummerDubletter(errorReport, records);
        control05BJournalnummerDubletter(errorReport, records);
        // Kontroller ferdig
        // Lager statistikkrapport
        {
            if (errorReport.getErrorType() < Constants.CRITICAL_ERROR) {
                final var bidragSum = records.stream().map(r -> r.getFieldAsIntegerDefaultEquals0(BIDRAG)).reduce(0, Integer::sum);
                final var laanSum = records.stream().map(r -> r.getFieldAsIntegerDefaultEquals0(LAAN)).reduce(0, Integer::sum);
                final var stonadSum = bidragSum + laanSum;

                errorReport.addStats(new StatsReportEntry(
                        "Sum"
                        , List.of(
                        new Code(STONAD, "Stønad")
                        , new Code(BIDRAG, "Bidrag")
                        , new Code(LAAN, "Lån"))
                        , List.of(
                        new StatsEntry(STONAD, Integer.toString(stonadSum))
                        , new StatsEntry(BIDRAG, bidragSum.toString())
                        , new StatsEntry(LAAN, laanSum.toString()))));

                final var gyldigeRecordsAlder = records.stream().filter(r -> r.getFieldAsInteger(FNR_OK) == 1 && r.getFieldAsInteger(ALDER) != -1).map(r -> r.getFieldAsInteger(ALDER)).toList();
                errorReport.addStats(new StatsReportEntry(
                        "Tilfeller"
                        , List.of(
                        new Code("I_ALT", "I alt")
                        , new Code("0_17", "Under 18")
                        , new Code("18_24", "18 - 24")
                        , new Code("25_44", "25 - 44")
                        , new Code("45_66", "45 - 66")
                        , new Code("67_999", "67 og over")
                        , new Code("UGYLDIG_FNR", "Ugyldig fnr")
                )
                        , List.of(
                        new StatsEntry("I_ALT", String.valueOf(records.size()))
                        , new StatsEntry("0_17", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 0, 17)).count()))
                        , new StatsEntry("18_24", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 18, 24)).count()))
                        , new StatsEntry("25_44", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 25, 44)).count()))
                        , new StatsEntry("45_66", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 45, 66)).count()))
                        , new StatsEntry("67_999", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 67, 999)).count()))
                        , new StatsEntry("UGYLDIG_FNR", String.valueOf(records.stream().filter(r -> r.getFieldAsInteger(FNR_OK) != 1).count()))
                )
                ));

                final var gyldigeRecordsStonadstid = records.stream()
                        .map(r -> STMND
                                .stream()
                                .filter(m -> 0 < r.getFieldAsIntegerDefaultEquals0(m))
                                .count()
                        )
                        .toList();

                errorReport.addStats(new StatsReportEntry(
                        "Stønadstid"
                        , List.of(
                        new Code("1", "1 måned")
                        , new Code("2_3", "2 - 3 måneder")
                        , new Code("4_6", "4 - 6 måneder")
                        , new Code("7_9", "7 - 9 måneder")
                        , new Code("10_11", "10 - 11 måneder")
                        , new Code("12", "12 måneder")
                        , new Code("UOPPGITT", UOPPGITT))
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

                final var gyldigeRecordsStonad = records.stream()
                        .map(r -> r.getFieldAsIntegerDefaultEquals0(BIDRAG) + r.getFieldAsIntegerDefaultEquals0(LAAN))
                        .toList();
                errorReport.addStats(new StatsReportEntry(
                        "Stønad"
                        , List.of(
                        new Code("1_9", "1 - 9999")
                        , new Code("10_49", "10000 - 49999")
                        , new Code("50_99", "50000 - 99999")
                        , new Code("100_149", "100000 - 149999")
                        , new Code("150_9999", "150000 og over")
                        , new Code("0", UOPPGITT)
                )
                        , List.of(
                        new StatsEntry("1_9", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 1, 9999)).count()))
                        , new StatsEntry("10_49", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 10000, 49999)).count()))
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


    public static boolean control10ForsorgerpliktForBarnUnder18Aar(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport,
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 10 Forsørgerplikt for barn under 18 år i husholdningen. Gyldige verdier"
                        , String.format("Korrigér forsørgerplikt. Fant '%s', forventet én av %s'. "
                                + "Det er ikke krysset av for om deltakeren har barn under 18 år, som deltakeren (eventuelt ektefelle/samboer) har forsørgerplikt for, og som bor i husholdningen ved siste kontakt. Feltet er obligatorisk å fylle ut.",
                        record.getFieldAsString("BU18"), record.getFieldDefinitionByName("BU18").getCodeList().stream().map(Code::toString).toList())
                        , Constants.CRITICAL_ERROR
                ),
                record.getFieldAsString("BU18"),
                record.getFieldDefinitionByName("BU18").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean control11AntallBarnIHusholdningenMangler(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                errorReport,
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 11 Det bor barn under 18 år i husholdningen. Mangler antall barn."
                        , String.format("Det er krysset av for at det bor barn under 18 år i husholdningen som mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt hvor mange barn '(%d)' som bor i husholdningen. "
                        + "Feltet er obligatorisk å fylle ut når det er oppgitt at det bor barn under 18 år i husholdningen.", record.getFieldAsIntegerDefaultEquals0(ANTBU18))
                        , Constants.CRITICAL_ERROR
                ),
                record.getFieldAsString("BU18"),
                List.of("1"),
                record.getFieldAsIntegerDefaultEquals0(ANTBU18),
                ">",
                0);
    }

    public static boolean control12AntallBarnIHusholdningen(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        return ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 12 Det bor barn under 18 år i husholdningen."
                        , String.format("Det er oppgitt %d barn under 18 år som bor i husholdningen som mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt at det bor barn i husholdningen. "
                        + "Feltet er obligatorisk å fylle ut når det er oppgitt antall barn under 18 år som bor i husholdningen.", record.getFieldAsInteger(ANTBU18))
                        , Constants.CRITICAL_ERROR
                )
                , record.getFieldAsInteger(ANTBU18)
                , ">"
                , 0
                , record.getFieldAsString("BU18")
                , List.of("1")

        );
    }

    public static boolean control13MangeBarnIHusholdningen(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        return ControlFelt1Boolsk.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 13 Mange barn under 18 år i husholdningen."
                        , String.format("Antall barn (%d) under 18 år i husholdningen er 10 eller flere, er dette riktig?", record.getFieldAsInteger(ANTBU18))
                        , Constants.NORMAL_ERROR
                )
                , record.getFieldAsInteger(ANTBU18)
                , "<"
                , 10
        );
    }

    public static boolean control14ViktigsteKildeTilLivsoppholdGyldigeVerdier(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 14 Viktigste kilde til livsopphold. Gyldige verdier"
                        , String.format("Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret skal oppgis. Fant '%s', forventet én av '%s'.",
                        record.getFieldAsString(VKLO), record.getFieldDefinitionByName(VKLO).getCodeList().stream().map(Code::toString).toList())
                        , Constants.CRITICAL_ERROR
                )
                , record.getFieldAsString(VKLO)
                , record.getFieldDefinitionByName(VKLO).getCodeList().stream().map(Code::code).toList()
        );
    }

    public static boolean control15ViktigsteKildeTilLivsopphold(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 15 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                        + record.getFieldDefinitionByName(VKLO).getCodeList().stream().filter(c -> Comparator.isCodeInCodeList(c.code(), List.of("1"))).map(Code::value).collect(Collectors.joining("")) + "."
                        , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                        + record.getFieldDefinitionByName(VKLO).getCodeList().stream().filter(c -> Comparator.isCodeInCodeList(c.code(), List.of("1"))).map(Code::value).collect(Collectors.joining("")) + ". "
                        + "Arbeidssituasjonen er '" + record.getFieldAsTrimmedString(ARBSIT) + "', forventet én av '"
                        + record.getFieldDefinitionByName(ARBSIT).getCodeList().stream().filter(c -> Comparator.isCodeInCodeList(c.code(), List.of("01", "02"))).map(Code::toString).toList()
                        + "'. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , record.getFieldAsString(VKLO)
                , List.of("1")
                , record.getFieldAsString(ARBSIT)
                , List.of("01", "02")
        );
    }

    private static boolean templateViktigsteKildeTilLivsopphold(
            final ErrorReport errorReport, final KostraRecord record,
            final String titleTemplate, final List<String> vkloCodeList, final List<String> arbsitCodeList) {

        errorReport.incrementCount();
        final var vklo = record.getFieldDefinitionByName(VKLO).getCodeList().stream()
                .filter(c -> Comparator.isCodeInCodeList(c.code(), vkloCodeList))
                .map(Code::value)
                .collect(Collectors.joining(""));

        final var arbsit = record.getFieldAsTrimmedString(ARBSIT);
        final var arbsitList = record.getFieldDefinitionByName(ARBSIT).getCodeList().stream()
                .filter(c -> Comparator.isCodeInCodeList(c.code(), arbsitCodeList))
                .map(Code::toString)
                .toList()
                .toString();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , String.format(titleTemplate, vklo)
                        , String.format(VKLO_ERRORTEXT, vklo, arbsit, arbsitList)
                        , Constants.CRITICAL_ERROR
                )
                , record.getFieldAsString(VKLO)
                , vkloCodeList
                , record.getFieldAsString(ARBSIT)
                , arbsitCodeList
        );
    }

    public static boolean control16ViktigsteKildeTilLivsopphold(
            final ErrorReport errorReport, final KostraRecord record) {

        return templateViktigsteKildeTilLivsopphold(
                errorReport,
                record,
                "Kontroll 16 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. %s.",
                List.of("2"),
                List.of("03", "05", "06")
        );
    }

    public static boolean control17ViktigsteKildeTilLivsopphold(
            final ErrorReport errorReport, final KostraRecord record) {

        return templateViktigsteKildeTilLivsopphold(
                errorReport,
                record,
                "Kontroll 17 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. %s.",
                List.of("4"),
                List.of("03")
        );
    }

    public static boolean control18ViktigsteKildeTilLivsopphold(
            final ErrorReport errorReport, final KostraRecord record) {

        return templateViktigsteKildeTilLivsopphold(
                errorReport,
                record,
                "Kontroll 18 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. %s.",
                List.of("6"),
                List.of("09")
        );
    }

    public static boolean control19ViktigsteKildeTilLivsopphold(
            final ErrorReport errorReport, final KostraRecord record) {

        return templateViktigsteKildeTilLivsopphold(
                errorReport,
                record,
                "Kontroll 19 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. %s.",
                List.of("8"),
                List.of("10")
        );
    }

    public static boolean control20ViktigsteKildeTilLivsopphold(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 20 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                        + record.getFieldDefinitionByName(VKLO).getCodeList().stream().filter(c -> Comparator.isCodeInCodeList(c.code(), List.of("3"))).map(Code::value).collect(Collectors.joining("")) + "."
                        , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                        + record.getFieldDefinitionByName(VKLO).getCodeList().stream().filter(c -> Comparator.isCodeInCodeList(c.code(), List.of("3"))).map(Code::value).collect(Collectors.joining("")) + ". "
                        + "Arbeidssituasjonen er '" + record.getFieldAsTrimmedString(ARBSIT) + "', forventet én av '"
                        + record.getFieldDefinitionByName(TRYGDESIT).getCodeList().stream().map(Code::toString).toList()
                        + "'. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , record.getFieldAsString(VKLO)
                , List.of("3")
                , record.getFieldAsString(TRYGDESIT)
                , List.of("01", "02", "04", "05", "06", "07", "09", "10", "11")
        );
    }


    public static boolean control21ViktigsteKildeTilLivsopphold(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 21 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                        + record.getFieldDefinitionByName(VKLO).getCodeList().stream().filter(c -> Comparator.isCodeInCodeList(c.code(), List.of("5"))).map(Code::value).collect(Collectors.joining("")) + "."
                        , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                        + record.getFieldDefinitionByName(VKLO).getCodeList().stream().filter(c -> Comparator.isCodeInCodeList(c.code(), List.of("5"))).map(Code::value).collect(Collectors.joining("")) + ". "
                        + "Arbeidssituasjonen er '" + record.getFieldAsTrimmedString(ARBSIT) + "', forventet én av '"
                        + record.getFieldDefinitionByName(ARBSIT).getCodeList().stream().map(Code::toString).toList()
                        + "'. Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                )
                , record.getFieldAsString(VKLO)
                , List.of("5")
                , record.getFieldAsString(ARBSIT)
                , List.of("02", "03", "04", "05", "06", "07", "08")
        );
    }
    public static boolean control22TilknytningTilTrygdesystemetOgAlder(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 22 Tilknytning til trygdesystemet og alder. 60 år eller yngre med alderspensjon."
                        , "Mottakeren (" + record.getFieldAsInteger(ALDER) + " år) er 60 år eller yngre og mottar alderspensjon."
                        , Constants.CRITICAL_ERROR
                )
                , record.getFieldAsString(TRYGDESIT)
                , List.of("07")
                , record.getFieldAsIntegerDefaultEquals0(ALDER)
                , ">"
                , 62
        );
    }

    public static boolean control23TilknytningTilTrygdesystemetOgBarn(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        if (!record.getFieldAsString(TRYGDESIT).equalsIgnoreCase("05")
                || record.getFieldAsString("BU18").equalsIgnoreCase("1")
                || record.getFieldAsIntegerDefaultEquals0(ANTBU18) != 0) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 23 Tilknytning til trygdesystemet og barn. Overgangsstønad."
                        , "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen."
                        , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean control24TilknytningTilTrygdesystemetOgArbeidssituasjon(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var trygdeSituasjon = record.getFieldDefinitionByName(TRYGDESIT).getCodeList().stream().filter(c -> c.code().equalsIgnoreCase(record.getFieldAsString(TRYGDESIT))).toList();
        final var t = (!trygdeSituasjon.isEmpty()) ? trygdeSituasjon.get(0) : new Code(UOPPGITT, UOPPGITT);

        final var arbeidSituasjon = record.getFieldDefinitionByName(ARBSIT).getCodeList().stream().filter(c -> c.code().equalsIgnoreCase(record.getFieldAsString(ARBSIT))).toList();
        final var a = (!arbeidSituasjon.isEmpty()) ? arbeidSituasjon.get(0) : new Code(UOPPGITT, UOPPGITT);

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 24 Tilknytning til trygdesystemet og arbeidssituasjon. Uføretrygd/alderspensjon og ikke arbeidssøker."
                        , "Mottakeren mottar trygd (" + t + "), men det er oppgitt ugyldig kode (" + a + ") på arbeidssituasjon."
                        , Constants.NORMAL_ERROR
                )
                , record.getFieldAsString(TRYGDESIT)
                , List.of("04", "07")
                , record.getFieldAsString(ARBSIT)
                , record.getFieldDefinitionByName(ARBSIT).getCodeList().stream().map(Code::code).filter(c -> Comparator.isCodeInCodeList(c, List.of("02", "04", "07"))).toList()
        );
    }

    public static boolean control24BTilknytningTilTrygdesystemetOgArbeidssituasjonArbeidsavklaringspenger(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var trygdeSituasjon = record.getFieldDefinitionByName(TRYGDESIT).getCodeList().stream().filter(c -> c.code().equalsIgnoreCase(record.getFieldAsString(TRYGDESIT))).toList();
        final var t = (!trygdeSituasjon.isEmpty()) ? trygdeSituasjon.get(0) : new Code(UOPPGITT, UOPPGITT);

        final var arbeidSituasjon = record.getFieldDefinitionByName(ARBSIT).getCodeList().stream().filter(c -> c.code().equalsIgnoreCase(record.getFieldAsString(ARBSIT))).toList();
        final var a = (!arbeidSituasjon.isEmpty()) ? arbeidSituasjon.get(0) : new Code(UOPPGITT, UOPPGITT);

        if (!(record.getFieldAsString(VKLO).equalsIgnoreCase("3")
                && record.getFieldAsString(TRYGDESIT).equalsIgnoreCase("11")
                && record.getFieldAsString(ARBSIT).equalsIgnoreCase("08"))) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 24B Tilknytning til trygdesystemet og arbeidssituasjon. Arbeidsavklaringspenger."
                        , "Mottakeren mottar trygd (" + t + "), men det er oppgitt ugyldig kode (" + a + ") på arbeidssituasjon."
                        , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean control25ArbeidssituasjonGyldigeKoder(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 25 Arbeidssituasjon. Gyldige koder."
                        , "Mottakerens arbeidssituasjon ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, eller feil kode er benyttet. Utfylt verdi er '"
                        + record.getFieldAsString(ARBSIT)
                        + "'. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                )
                , record.getFieldAsString(ARBSIT)
                , record.getFieldDefinitionByName(ARBSIT).getCodeList().stream().map(Code::code).toList());
    }

    public static boolean control26StonadsmaanederGyldigeKoder(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var harVarighet = STMND
                .stream()
                .anyMatch(field -> record.getFieldDefinitionByName(field)
                        .getCodeList()
                        .stream()
                        .map(Code::code)
                        .toList()
                        .contains(record.getFieldAsString(field))
                );

        final var bidrag = record.getFieldAsInteger(BIDRAG);
        final var laan = record.getFieldAsInteger(LAAN);

        if (harVarighet) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 26 Stønadsmåneder. Gyldige koder"
                        , "Det er ikke krysset av for hvilke måneder mottakeren har fått utbetalt økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ")"
                        + "i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR));
        return true;
    }


    public static boolean control27StonadssumManglerEllerHarUgyldigeTegn(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var bidrag = record.getFieldAsInteger(BIDRAG);
        final var bidragOK = bidrag != null && 0 < bidrag;
        final var laan = record.getFieldAsInteger(LAAN);
        final var laanOK = laan != null && 0 < laan;
        final var stonadOK = bidragOK || laanOK;

        if (stonadOK) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 27 Stønadssum mangler eller har ugyldige tegn."
                        , "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ") i løpet av året, "
                        + "eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean control28HarVarighetMenManglerStonadssum(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var harVarighet = STMND
                .stream()
                .anyMatch(field -> record.getFieldDefinitionByName(field)
                        .getCodeList()
                        .stream()
                        .map(Code::code)
                        .toList()
                        .contains(record.getFieldAsString(field))
                );

        final var bidrag = record.getFieldAsInteger(BIDRAG);
        final var bidragOK = bidrag != null && 0 < bidrag;
        final var laan = record.getFieldAsInteger(LAAN);
        final var laanOK = laan != null && 0 < laan;
        final var stonadOK = bidragOK || laanOK;

        if (!harVarighet || stonadOK) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 28 Har varighet, men mangler stønadssum"
                        , "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ") i løpet av året, "
                        + "eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean control29HarStonadssumMenManglerVarighet(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var harVarighet = STMND.stream()
                .anyMatch(field -> record.getFieldDefinitionByName(field)
                        .getCodeList()
                        .stream()
                        .map(Code::code)
                        .toList()
                        .contains(record.getFieldAsString(field))
                );

        final var bidrag = record.getFieldAsInteger(BIDRAG);
        final var bidragOK = bidrag != null;
        final var laan = record.getFieldAsInteger(LAAN);
        final var laanOK = laan != null;
        var stonad = 0;
        final var stonadOK = bidragOK || laanOK;

        if (bidragOK) {
            stonad += bidrag;
        }

        if (laanOK) {
            stonad += laan;
        }

        if (!stonadOK || 0 >= stonad || harVarighet) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 29 Har stønadssum men mangler varighet"
                        , "Mottakeren har fått i økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ") i løpet av året, "
                        + "men mangler utfylling for hvilke måneder i løpet av året mottakeren har mottatt økonomisk stønad."
                        , Constants.CRITICAL_ERROR));
        return true;
    }

    public static boolean control30StonadssumPaaMaxEllerMer(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var bidrag = record.getFieldAsIntegerDefaultEquals0(BIDRAG);
        final var laan = record.getFieldAsIntegerDefaultEquals0(LAAN);
        final var stonad = bidrag + laan;
        final var stonadOK = (bidrag != 0) || (laan != 0);
        final var stonadSumMax = 600000;

        if (!stonadOK || stonadSumMax >= stonad) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 30 Stønadssum på kr " + stonadSumMax + ",- eller mer."
                        , "Det samlede stønadsbeløpet (summen " + stonad + " av bidrag " + bidrag + " og lån " + laan + ") "
                        + "som mottakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMax + ",-."
                        , Constants.NORMAL_ERROR));
        return true;
    }

    public static boolean control31StonadssumPaaMinEllerMindre(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var bidrag = record.getFieldAsIntegerDefaultEquals0(BIDRAG);
        final var laan = record.getFieldAsIntegerDefaultEquals0(LAAN);
        final var stonad = bidrag + laan;
        final var stonadOK = (bidrag != 0) || (laan != 0);
        final var stonadSumMin = 200;

        if (!stonadOK || stonad > stonadSumMin) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 31 Stønadssum på kr " + stonadSumMin + ",- eller lavere."
                        , "Det samlede stønadsbeløpet (summen " + stonad + " av bidrag " + bidrag + " og lån " + laan + ") "
                        + "som mottakeren har fått i løpet av rapporteringsåret er lik/lavere enn Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMin + ",-."
                        , Constants.NORMAL_ERROR));
        return true;
    }

    public static boolean control32OkonomiskraadgivningGyldigeKoder(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 32 Økonomiskrådgivning. Gyldige koder."
                        , "Det er ikke krysset av for om mottakeren er gitt økonomisk rådgiving i forbindelse med utbetaling av økonomisk sosialhjelp. "
                        + "Utfylt verdi er '" + record.getFieldAsString(GITT_OKONOMIRAD) + "'. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR)
                , record.getFieldAsString(GITT_OKONOMIRAD)
                , record.getFieldDefinitionByName(GITT_OKONOMIRAD).getCodeList().stream().map(Code::code).toList());
    }

    public static boolean control33UtarbeidelseAvIndividuellPlan(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport,
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 33 Utarbeidelse av individuell plan"
                        , "Det er ikke krysset av for om mottakeren har fått utarbeidet individuell plan. "
                        + "Utfylt verdi er '" + record.getFieldAsString(FAAT_INDIVIDUELL_PLAN) + "'. Feltet er obligatorisk."
                        , Constants.CRITICAL_ERROR)
                , record.getFieldAsString(FAAT_INDIVIDUELL_PLAN)
                , record.getFieldDefinitionByName(FAAT_INDIVIDUELL_PLAN).getCodeList().stream().map(Code::code).toList());
    }

    public static boolean control35Boligsituasjon(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 35 Boligsituasjon"
                        , "Det er ikke krysset av for mottakerens boligsituasjon. "
                        + "Utfylt verdi er '" + record.getFieldAsString(BOSIT) + "'. Feltet er obligatorisk."
                        , Constants.CRITICAL_ERROR)
                , record.getFieldAsString(BOSIT)
                , record.getFieldDefinitionByName(BOSIT).getCodeList().stream().map(Code::code).toList());
    }


    public static boolean control36BidragFordeltPaaMmaaneder(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var bidrag = record.getFieldAsIntegerDefaultEquals0(BIDRAG);
        final var bidragMaanederSum = Stream.of(
                        "BIDRAG_JAN", "BIDRAG_FEB", "BIDRAG_MARS",
                        "BIDRAG_APRIL", "BIDRAG_MAI", "BIDRAG_JUNI",
                        "BIDRAG_JULI", "BIDRAG_AUG", "BIDRAG_SEPT",
                        "BIDRAG_OKT", "BIDRAG_NOV", "BIDRAG_DES")
                .map(record::getFieldAsIntegerDefaultEquals0)
                .reduce(0, Integer::sum);

        if (0 >= bidrag || bidrag.intValue() == bidragMaanederSum.intValue()) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 36 Bidrag fordelt på måneder"
                        , "Det er ikke fylt ut bidrag (" + bidragMaanederSum + ") fordelt på måneder eller sum stemmer ikke med sum bidrag (" + bidrag + ") utbetalt i løpet av året."
                        , Constants.NORMAL_ERROR));
        return true;
    }

    public static boolean control37LaanFordeltPaaMmaaneder(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var laan = record.getFieldAsIntegerDefaultEquals0(LAAN);
        final var laanMaanederSum = Stream.of(
                        "LAAN_JAN", "LAAN_FEB", "LAAN_MARS",
                        "LAAN_APRIL", "LAAN_MAI", "LAAN_JUNI",
                        "LAAN_JULI", "LAAN_AUG", "LAAN_SEPT",
                        "LAAN_OKT", "LAAN_NOV", "LAAN_DES")
                .map(record::getFieldAsIntegerDefaultEquals0)
                .reduce(0, Integer::sum);

        if (0 >= laan || laan.intValue() == laanMaanederSum.intValue()) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 37 Lån fordelt på måneder"
                        , "Det er ikke fylt ut laan (" + laanMaanederSum + ") fordelt på måneder eller sum stemmer ikke med sum lån (" + laan + ") utbetalt i løpet av året."
                        , Constants.NORMAL_ERROR));
        return true;
    }

    public static boolean control38DUFNummer(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFodselsnummerDUFnummer.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 38 DUF-nummer"
                        , "Det er ikke oppgitt fødselsnummer/d-nummer på sosialhjelpsmottakeren eller fødselsnummeret/d-nummeret inneholder feil. "
                        + "Oppgi ett 12-sifret DUF- nummer."
                        , Constants.CRITICAL_ERROR)
                , record.getFieldAsString(PERSON_FODSELSNR)
                , record.getFieldAsString("PERSON_DUF"));
    }

    public static boolean control39ForsteVilkarIAaret(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 39 Første vilkår i året, vilkår"
                        , "Det er ikke krysset av for om det stilles vilkår til mottakeren etter sosialtjenesteloven. "
                        + "Registreres for første vilkår i kalenderåret. Feltet er obligatorisk."
                        , Constants.CRITICAL_ERROR)
                , record.getFieldAsString(VILKARSOSLOV)
                , record.getFieldDefinitionByName(VILKARSOSLOV).getCodeList().stream().map(Code::code).toList());
    }

    public static boolean control40ForsteVilkarIAaretSambo(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 40 Første vilkår i året, vilkår til søkerens samboer/ektefelle"
                        , "Det er ikke krysset av for om det stilles vilkår til mottakeren etter sosialtjenesteloven. "
                        + "Registreres for første vilkår i kalenderåret. Feltet er obligatorisk."
                        , Constants.CRITICAL_ERROR)
                , record.getFieldAsString("VILKARSAMEKT")
                , record.getFieldDefinitionByName("VILKARSAMEKT").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean control41DatoForUtbetalingsvedtak(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 41 Dato for utbetalingsvedtak"
                        , "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven', "
                        + "så skal utbetalingsvedtakets dato (" + record.getFieldAsString("UTBETDATO") + ") (DDMMÅÅ) oppgis. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR)
                , record.getFieldAsString(VILKARSOSLOV)
                , List.of("1")
                , record.getFieldAsLocalDate("UTBETDATO"));
    }

    public static boolean control42TilOgMedDatoForUtbetalingsvedtak(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 42 Til og med dato for utbetalingsvedtak"
                        , "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven', "
                        + "så skal utbetalingsvedtakets til og med dato (" + record.getFieldAsString("UTBETTOMDATO") + ") (DDMMÅÅ) oppgis. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR)
                , record.getFieldAsString(VILKARSOSLOV)
                , List.of("1")
                , record.getFieldAsLocalDate("UTBETTOMDATO"));
    }

    public static boolean control43Vilkaar(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        final var vilkar = record.getFieldAsString(VILKARSOSLOV);
        final var fields = List.of(
                "VILKARARBEID", "VILKARKURS", "VILKARUTD",
                "VILKARJOBBLOG", "VILKARJOBBTILB", "VILKARSAMT",
                "VILKAROKRETT", "VILKARLIVSH", "VILKARHELSE",
                "VILKARANNET", "VILKARDIGPLAN");

        final var isNoneFilledIn = fields.stream()
                .noneMatch(field -> Comparator.isCodeInCodeList(
                        record.getFieldAsTrimmedString(field),
                        record.getFieldDefinitionByName(field)
                                .getCodeList()
                                .stream()
                                .map(Code::code)
                                .toList()));

        if (!(vilkar.equalsIgnoreCase("1") && isNoneFilledIn)) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        record.getFieldAsString(SAKSBEHANDLER)
                        , record.getFieldAsString(PERSON_JOURNALNR)
                        , record.getFieldAsString(PERSON_FODSELSNR)
                        , " "
                        , "Kontroll 43 Type vilkår det stilles til mottakeren"
                        , "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven', "
                        + "så skal det oppgis hvilke vilkår som stilles til mottakeren. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR));
        return true;
    }
}
