package no.ssb.kostra.control.famvern.s52a;

import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.ControlFelt1Boolsk;
import no.ssb.kostra.control.felles.ControlFelt1Dato;
import no.ssb.kostra.control.felles.ControlFelt1DatoSaaFelt2Dato;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.control.felles.Utils;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Code;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.KostraRecord;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.ssb.kostra.control.felles.Comparator.outsideOf;

@SuppressWarnings("SpellCheckingInspection")
public final class Main {
    private Main() {}

    private static String createKontorNr(String kontornr) {
        return "Kontornummer ".concat(kontornr);
    }

    private static String createJournalNr(final String journalnr, final String linjenr) {
        return "Journalnummer ".concat(journalnr).concat(" / Linjenummer ").concat(linjenr);
    }

    public static ErrorReport doControls(final Arguments args) {
        final var errorReport = new ErrorReport(args);
        final var inputFileContent = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        final var hasErrors = ControlRecordLengde.doControl(inputFileContent, errorReport, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return errorReport;
        }

        final var fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        final var records = Utils.addLineNumbering(inputFileContent.stream()
                .map(p -> new KostraRecord(p, fieldDefinitions))
                .toList());

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, errorReport);

        records.forEach(currentRecord -> {
            kontroll03Regionsnummer(errorReport, currentRecord);
            kontroll04Kontornummer(errorReport, currentRecord);
            kontroll05RegionsnummerKontornummer(errorReport, currentRecord);
        });

        kontroll06Dubletter(errorReport, records);

        records.forEach(currentRecord -> {
            kontroll07Henvendelsesdato(errorReport, currentRecord);
            kontroll09KontaktTidligere(errorReport, currentRecord);
            kontroll11HenvendelsesBegrunnelse(errorReport, currentRecord);
            kontroll13Kjonn(errorReport, currentRecord);
            kontroll14Fodselsaar(errorReport, currentRecord);
            kontroll15Samlivsstatus(errorReport, currentRecord);
            kontroll16FormellSivilstand(errorReport, currentRecord);
            kontroll17Bosituasjon(errorReport, currentRecord);
            kontroll18Arbeidsosituasjon(errorReport, currentRecord);
            kontroll19AVarighetSamtalepartner(errorReport, currentRecord);
            kontroll19B1VarighetSidenBrudd(errorReport, currentRecord);
            kontroll19B2VarighetEkspartner(errorReport, currentRecord);
            kontroll20DatoForsteBehandlingssamtale(errorReport, currentRecord);
            kontroll21DatoForsteBehandlingssamtaleEtterHenvendelse(errorReport, currentRecord);

            {
                if (Stream.of(
                                "TEMA_PARREL_A", "TEMA_AVKLAR_A", "TEMA_SAMLBRUDD_A", "TEMA_SAMSPILL_A",
                                "TEMA_BARNSIT_A", "TEMA_BARNFOR_A", "TEMA_BOSTED_A", "TEMA_FORELDRE_A",
                                "TEMA_FORBARN_A", "TEMA_FLERGEN_A", "TEMA_SAMBARN_A", "TEMA_SÆRBARN_A",
                                "TEMA_KULTUR_A", "TEMA_TVANG_A", "TEMA_RUS_A", "TEMA_SYKD_A",
                                "TEMA_VOLD_A", "TEMA_ALVH_A")
                        .noneMatch(field -> currentRecord.getFieldDefinitionByName(field).getCodeList().stream()
                                .map(Code::code)
                                .toList()
                                .contains(currentRecord.getFieldAsString(field)))) {
                    errorReport.addEntry(
                            new ErrorReportEntry(
                                    createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                                    , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                                    , String.valueOf(currentRecord.getLine())
                                    , " "
                                    , "Kontroll 22 Områder det har vært arbeidet med i saken"
                                    , "Det er ikke fylt ut hvilke områder det har vært arbeidet med siden saken ble opprettet. "
                                    + "Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område."
                                    , Constants.NORMAL_ERROR
                            )
                    );
                }
            }

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 23 Hovedform på behandlingstilbudet"
                            , "Det er ikke krysset av for hva som har vært hovedformen på behandlingstilbudet siden saken ble opprettet, eller feil kode er benyttet. "
                            + "Fant '" + currentRecord.getFieldAsString("HOVEDF_BEHAND_A") + "', "
                            + "forventet én av: " + currentRecord.getFieldDefinitionByName("HOVEDF_BEHAND_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsString("HOVEDF_BEHAND_A")
                    , currentRecord.getFieldDefinitionByName("HOVEDF_BEHAND_A").getCodeList().stream().map(Code::code).toList()
            );

            List.of(
                    List.of("Partner", "DELT_PARTNER_A"),
                    List.of("Ekspartner", "DELT_EKSPART_A"),
                    List.of("Barn under 18år", "DELT_BARNU18_A"),
                    List.of("Barn over 18år", "DELT_BARNO18_A"),
                    List.of("Foreldre", "DELT_FORELDRE_A"),
                    List.of("Øvrige familie", "DELT_OVRFAM_A"),
                    List.of("Venner", "DELT_VENN_A"),
                    List.of("Andre", "DELT_ANDR_A")
            ).forEach(fieldPair ->
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            errorReport,
                            new ErrorReportEntry(
                                    createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                                    , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                                    , String.valueOf(currentRecord.getLine())
                                    , " "
                                    , "Kontroll 24 Deltagelse i behandlingssamtaler med primærklienten i løpet av året, " + fieldPair.get(0)
                                    , "Det er ikke krysset av for om andre deltakere (" + fieldPair.get(0) + ") i saken har deltatt i samtaler "
                                    + "med primærklienten i løpet av rapporteringsåret. Feltene er obligatorisk å fylle ut.<br/>\n"
                                    , Constants.NORMAL_ERROR
                            ),
                            currentRecord.getFieldAsString(fieldPair.get(1)),
                            currentRecord.getFieldDefinitionByName(fieldPair.get(1)).getCodeList().stream().map(Code::code).toList()));

            if (Stream.of(
                            "SAMT_PRIMK_A", "SAMT_PARTNER_A", "SAMT_EKSPART_A", "SAMT_BARNU18_A", "SAMT_BARNO18_A",
                            "SAMT_FORELDRE_A", "SAMT_OVRFAM_A", "SAMT_VENN_A", "SAMT_ANDRE_A")
                    .map(field -> {
                        try {
                            return Objects.requireNonNullElse(currentRecord.getFieldAsInteger(field), 0);
                        } catch (NullPointerException e) {
                            return 0;
                        }
                    })
                    .noneMatch(value -> 0 < value)
            ) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                                , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 25 Behandlingssamtaler for de involverte i saken i løpet av året"
                                , "Det er ikke oppgitt hvor mange behandlingssamtaler de ulike deltakerne "
                                + "i saken har deltatt i gjennom året. Feltet er obligatorisk å fylle ut."
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            {
                final var fieldPairs = List.of(
                        List.of("Partner", "DELT_PARTNER_A", "SAMT_PARTNER_A"),
                        List.of("Ekspartner", "DELT_EKSPART_A", "SAMT_EKSPART_A"),
                        List.of("Barn under 18år", "DELT_BARNU18_A", "SAMT_BARNU18_A"),
                        List.of("Barn over 18år", "DELT_BARNO18_A", "SAMT_BARNO18_A"),
                        List.of("Foreldre", "DELT_FORELDRE_A", "SAMT_FORELDRE_A"),
                        List.of("Øvrige familie", "DELT_OVRFAM_A", "SAMT_OVRFAM_A"),
                        List.of("Venner", "DELT_VENN_A", "SAMT_VENN_A"),
                        List.of("Andre", "DELT_ANDR_A", "SAMT_ANDRE_A")
                );

                fieldPairs.forEach(fieldPair ->
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                                errorReport,
                                new ErrorReportEntry(
                                        createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                                        , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                                        , String.valueOf(currentRecord.getLine())
                                        , " "
                                        , "Kontroll 26 Relasjon mellom antall behandlingssamtaler og hvem som har deltatt, " + fieldPair.get(0)
                                        , "Det er oppgitt at andre personer (" + fieldPair.get(0) + ") har deltatt i samtaler med primærklient i løpet av året, "
                                        + "men det er ikke oppgitt hvor mange behandlingssamtaler (" + currentRecord.getFieldAsString(fieldPair.get(2)) + ") de ulike personene har deltatt i gjennom av året."
                                        , Constants.NORMAL_ERROR
                                ),
                                currentRecord.getFieldAsString(fieldPair.get(1)),
                                currentRecord.getFieldDefinitionByName(fieldPair.get(1)).getCodeList().stream().filter(c -> c.value().equalsIgnoreCase("Ja")).map(Code::code).toList(),
                                currentRecord.getFieldAsIntegerDefaultEquals0(fieldPair.get(2)),
                                ">",
                                0
                        )
                );

            }

            if (Stream.of("ANTSAMT_HOVEDT_A", "ANTSAMT_ANDREANS_A")
                    .noneMatch(field -> 0 < currentRecord.getFieldAsIntegerDefaultEquals0(field))) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                                , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 27 Antall behandlingssamtaler for ansatte ved kontoret i løpet av året"
                                , "Det er ikke oppgitt hvor mange behandlingssamtaler hovedterapeut (" + currentRecord.getFieldAsIntegerDefaultEquals0("ANTSAMT_HOVEDT_A") + ") "
                                + "eller andre ansatte (" + currentRecord.getFieldAsIntegerDefaultEquals0("ANTSAMT_HOVEDT_A") + ") har deltatt i gjennom året. Feltet er obligatorisk å fylle ut."
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 28 Antall behandlingssamtaler i løpet av året"
                            , "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført i saken i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("ANTSAMT_IARET_A")
                    , ">"
                    , 0
            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 29 Antall behandlingssamtaler siden opprettelsen"
                            , "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført siden saken ble opprettet. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("ANTSAMT_OPPR_A")
                    , ">"
                    , 0
            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 30 Totalt antall timer i løpet av året"
                            , "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt på saken "
                            + "(timer benyttet til gruppesamtaler skal holdes utenfor) i løpet av året "
                            + "(for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("TIMER_IARET_A")
                    , ">"
                    , 0
            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 31 Totalt antall timer siden saken ble opprettet"
                            , "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt siden saken ble opprettet "
                            + "(for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut"
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("TIMER_OPPR_A")
                    , ">"
                    , 0
            );

            if (Stream.of(
                            "SAMARB_INGEN_A", "SAMARB_LEGE_A", "SAMARB_HELSE_A", "SAMARB_PSYKH_A", "SAMARB_JURIST_A", "SAMARB_KRISES_A",
                            "SAMARB_SKOLE_A", "SAMARB_SOS_A", "SAMARB_KOMMB_A", "SAMARB_STATB_A", "SAMARB_ANDRE_A")
                    .map(field -> {
                        try {
                            return Objects.requireNonNullElse(currentRecord.getFieldAsInteger(field), 0);
                        } catch (NullPointerException e) {
                            return 0;
                        }
                    })
                    .noneMatch(value -> 0 < value)
            ) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                                , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 32 Samarbeid med andre instanser siden opprettelsen"
                                , "Det er ikke krysset av for om det har vært samarbeid med andre instanser "
                                + "siden saken ble opprettet. Feltet er obligatorisk å fylle ut."
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 33 Status ved året slutt"
                            , "Det er ikke fylt ut hva som er sakens status ved utgangen av året. "
                            + "Fant '" + currentRecord.getFieldAsString("STATUS_ARETSSL_A") + "', "
                            + "forventet én av: " + currentRecord.getFieldDefinitionByName("STATUS_ARETSSL_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    ),
                    currentRecord.getFieldAsString("STATUS_ARETSSL_A"),
                    currentRecord.getFieldDefinitionByName("STATUS_ARETSSL_A").getCodeList().stream().map(Code::code).toList());

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 34 Sakens hovedtema (Fylles ut når saken avsluttes)."
                            , "Det er krysset av for at saken er avsluttet i rapporteringsåret, men ikke fylt ut hovedtema for saken, eller feltet har ugyldig format."
                            + "Fant '" + currentRecord.getFieldAsString("HOVEDTEMA_A") + "', "
                            + "forventet én av: " + currentRecord.getFieldDefinitionByName("HOVEDTEMA_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsString("STATUS_ARETSSL_A")
                    , List.of("1", "2")
                    , currentRecord.getFieldAsString("HOVEDTEMA_A")
                    , currentRecord.getFieldDefinitionByName("HOVEDTEMA_A").getCodeList().stream().map(Code::code).toList());

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 35 Saken avsluttet, men ikke satt avslutningsdato"
                            , "Det er krysset av for at saken er avsluttet i rapporteringsåret, "
                            + "men ikke fylt ut dato (" + currentRecord.getFieldAsString("DATO_AVSL_A") + ") for avslutning av saken."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsString("STATUS_ARETSSL_A")
                    , List.of("1", "2")
                    , currentRecord.getFieldAsLocalDate("DATO_AVSL_A")
            );

            ControlFelt1DatoSaaFelt2Dato.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 36 Avslutningsdato før første samtale"
                            , "Dato for avslutting av saken (" + currentRecord.getFieldAsString("DATO_AVSL_A") + ") "
                            + "kommer før dato for første behandlingssamtale (" + currentRecord.getFieldAsString("FORSTE_SAMT_A") + ")."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsString("FORSTE_SAMT_A")
                    , currentRecord.getFieldDefinitionByName("FORSTE_SAMT_A").getDatePattern()
                    , currentRecord.getFieldAsString("DATO_AVSL_A")
                    , currentRecord.getFieldDefinitionByName("DATO_AVSL_A").getDatePattern()
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(currentRecord.getFieldAsString("JOURNAL_NR_A"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 37 Bekymringsmelding sendt barnevernet"
                            , "Det er ikke svart på hvorvidt bekymringsmelding er sendt barnevernet eller ei, eller feil kode er benyttet. "
                            + "Fant '" + currentRecord.getFieldAsString("BEKYMR_MELD_A") + "', "
                            + "forventet én av: " + currentRecord.getFieldDefinitionByName("BEKYMR_MELD_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                            + "Feltet er obligatorisk."
                            , Constants.NORMAL_ERROR
                    ),
                    currentRecord.getFieldAsString("BEKYMR_MELD_A"),
                    currentRecord.getFieldDefinitionByName("BEKYMR_MELD_A").getCodeList().stream().map(Code::code).toList());
        });

        return errorReport;
    }

    public static boolean kontroll03Regionsnummer(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 3: Regionsnummer
        errorReport.incrementCount();

        if (Definitions.isRegionValid(record.getFieldAsString("REGION_NR_A"))) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 03 Regionsnummer"
                        , "Regionsnummeret som er oppgitt i recorden fins ikke i listen med gyldige regionsnumre. "
                        + "Fant '" + record.getFieldAsString("REGION_NR_A") + "', forventet én av : " + Definitions.getRegionAsList() + ". "
                        , Constants.NORMAL_ERROR
                )
        );
        return true;
    }

    public static boolean kontroll04Kontornummer(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 4: Kontornummer
        errorReport.incrementCount();

        if (Definitions.isKontorValid(record.getFieldAsString("KONTOR_NR_A"))) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 04 Kontornummer"
                        , "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. "
                        + "Fant '" + record.getFieldAsString("KONTOR_NR_A") + "', forventet én av : " + Definitions.getKontorAsList() + ". "
                        , Constants.NORMAL_ERROR
                )
        );
        return true;
    }

    public static boolean kontroll05RegionsnummerKontornummer(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 5: Manglende samsvar mellom regions- og kontornummer.
        errorReport.incrementCount();

        if (Definitions.isRegionAndKontorValid(record.getFieldAsString("REGION_NR_A"), record.getFieldAsString("KONTOR_NR_A"))) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 05 Manglende samsvar mellom regions- og kontornummer."
                        , "Regionsnummer (" + record.getFieldAsString("REGION_NR_A") + ") og "
                        + "kontornummer (" + record.getFieldAsString("KONTOR_NR_A") + ") stemmer ikke overens."
                        , Constants.NORMAL_ERROR
                )
        );
        return true;
    }

    public static boolean kontroll06Dubletter(
            final ErrorReport errorReport, final List<KostraRecord> records) {

        // Kontroll 6 Dublett på journalnummer
        errorReport.incrementCount();

        final var dubletter = records.stream()
                .map(r -> "Kontornummer ".concat(r.getFieldAsString("KONTOR_NR_A")).concat(" - Journalnummer ").concat(r.getFieldAsString("JOURNAL_NR_A")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(p -> p.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        if (dubletter.isEmpty()) {
            return false;
        }

        errorReport.addEntry(
                new ErrorReportEntry("Filuttrekk", "Dubletter", " ", " "
                        , "Kontroll 06 Dubletter"
                        , "Journalnummeret er benyttet på mer enn en sak. Dubletter på kontornummer - journalnummer. (Gjelder for:<br/>\n" + String.join(",<br/>\n", dubletter) + ")"
                        , Constants.NORMAL_ERROR));
        return true;
    }

    public static boolean kontroll07Henvendelsesdato(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 7 Henvendelsesdato
        errorReport.incrementCount();

        return ControlFelt1Dato.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 07 Henvendelsesdato"
                        , "Dette er ikke oppgitt dato (" + record.getFieldAsString("HENV_DATO_A") + ") for når primærklienten henvendte seg til familievernkontoret eller feltet har ugyldig format (DDMMÅÅÅÅ). Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                )
                , record.getFieldAsString("HENV_DATO_A")
                , record.getFieldDefinitionByName("HENV_DATO_A").getDatePattern()
        );
    }

    public static boolean kontroll09KontaktTidligere(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 09 Primærklient hatt kontakt med eller vært klient tidligere
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 09 Primærklient hatt kontakt med eller vært klient tidligere"
                        , "Det er ikke fylt ut om primærklienten har vært i kontakt med/klient ved familievernet tidligere, eller feil kode er benyttet. "
                        + "Fant '" + record.getFieldAsString("KONTAKT_TIDL_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("KONTAKT_TIDL_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        + "Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("KONTAKT_TIDL_A"),
                record.getFieldDefinitionByName("KONTAKT_TIDL_A").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean kontroll11HenvendelsesBegrunnelse(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 11 Primærklientens viktigste begrunnelse for å ta kontakt
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 11 Primærklientens viktigste begrunnelse for å ta kontakt."
                        , "Dette er ikke oppgitt hva som er primærklientens viktigste ønsker med kontakten, eller feltet har ugyldig format."
                        + "Fant '" + record.getFieldAsString("HENV_GRUNN_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("HENV_GRUNN_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        + "Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("HENV_GRUNN_A"),
                record.getFieldDefinitionByName("HENV_GRUNN_A").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean kontroll13Kjonn(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 13 Primærklientens kjønn
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 13 Primærklientens kjønn"
                        , "Primærklientens kjønn er ikke fylt ut eller feil kode er benyttet. "
                        + "Fant '" + record.getFieldAsString("PRIMK_KJONN_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("PRIMK_KJONN_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        + "Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("PRIMK_KJONN_A"),
                record.getFieldDefinitionByName("PRIMK_KJONN_A").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean kontroll14Fodselsaar(
            final ErrorReport errorReport, final KostraRecord record) {

    //    Kontroll 14:        Primærklientens fødselsår.
//    Felt som inngår i kontrollen: Feltnummer 8.
//    Beskrivelse:
//      Kontrollere
//      at feltet er utfylt og har gyldig format åååå,
//      at fødselsår ikke er et høyere tall en årgang (mindre eller lik årgang) og
//      at fødselsår ikke er 100 år lavere enn årgang. Feltet er obligatorisk.
//
//    Feilmelding:
//      Dette er ikke oppgitt fødselsår på primærklienten, årgang gir negativ alder, eller alder større enn 100 år, eller feltet har ugyldig format. Feltet er obligatorisk å fylle ut.
//    Forslag tiltak:
//      Primærklientens fødselsår skal fylles ut. Fyll inn fødselsår eller sjekk at fødselsårsfeltet har korrekt format og logisk verdi i forhold til alder. Er fødselsåret 1976 skal alle fire sifrene fylles ut.
//    Feil hindrer innsending: NEI
//
        // Kontroll 14 Primærklientens fødselsår
        errorReport.incrementCount();

        Integer primk_fodt_a = record.getFieldAsInteger("PRIMK_FODT_A");

        if (primk_fodt_a == null){
            errorReport.addEntry(
                    new ErrorReportEntry(
                            createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                            , String.valueOf(record.getLine())
                            , " "
                            , "Kontroll 14 Primærklientens fødselsår"
                            , "Dette er ikke oppgitt fødselsår ('" + record.getFieldAsString("PRIMK_FODT_A") + "') på primærklienten eller feltet har ugyldig format. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    ));
            return true;
        }

        if (Integer.parseInt(errorReport.getArgs().getAargang()) < primk_fodt_a) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                            , String.valueOf(record.getLine())
                            , " "
                            , "Kontroll 14 Primærklientens fødselsår"
                            , "Dette er oppgitt fødselsår ('" + record.getFieldAsString("PRIMK_FODT_A") + "') på primærklienten der årgang gir negativ alder. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    ));
            return true;
        }
        int alder = Integer.parseInt(errorReport.getArgs().getAargang()) - primk_fodt_a;

        if (outsideOf(alder, 0, 100)) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                            , String.valueOf(record.getLine())
                            , " "
                            , "Kontroll 14 Primærklientens fødselsår"
                            , "Dette er oppgitt fødselsår ('" + record.getFieldAsString("PRIMK_FODT_A") + "') på primærklienten som gir en alder større enn 100 år. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    ));
            return true;
        }

        return false;
    }

    public static boolean kontroll15Samlivsstatus(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 15 Primærklientens samlivsstatus
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 15 Primærklientens samlivsstatus"
                        , "Primærklientens samlivsstatus ved sakens opprettelse er ikke fylt ut eller feil kode er benyttet. "
                        + "Fant '" + record.getFieldAsString("PRIMK_SIVILS_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("PRIMK_SIVILS_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        + "Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("PRIMK_SIVILS_A"),
                record.getFieldDefinitionByName("PRIMK_SIVILS_A").getCodeList().stream().map(Code::code).toList());
    }


    public static boolean kontroll16FormellSivilstand(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 16 Utdypende om primærklientens formelle sivilstand
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 16 Utdypende om primærklientens formelle sivilstand"
                        , "Det er oppgitt at primærklientens samlivsstatus er Samboer eller at primærklient ikke lever i samliv, men det er ikke fylt ut hva som er primærklientens korrekt sivilstand"
                        + "Fant '" + record.getFieldAsString("FORMELL_SIVILS_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("FORMELL_SIVILS_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("PRIMK_SIVILS_A"),
                List.of("3", "4"),
                record.getFieldAsString("FORMELL_SIVILS_A"),
                record.getFieldDefinitionByName("FORMELL_SIVILS_A").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean kontroll17Bosituasjon(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 17 Primærklientens bosituasjon ved opprettelsen
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 17 Primærklientens bosituasjon ved opprettelsen"
                        , "Det er ikke fylt ut om primærklienten bor sammen med andre ved sakens opprettelse eller feil kode er benyttet. "
                        + "Fant '" + record.getFieldAsString("PRIMK_SAMBO_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("PRIMK_SAMBO_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        + "Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("PRIMK_SAMBO_A"),
                record.getFieldDefinitionByName("PRIMK_SAMBO_A").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean kontroll18Arbeidsosituasjon(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 18 Primærklientens tilknytning til utdanning og arbeidsliv
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 18 Primærklientens tilknytning til utdanning og arbeidsliv"
                        , "Det er ikke krysset av for primærklientens tilknytning til arbeidslivet ved sakens opprettelse eller feil kode er benyttet. "
                        + "Fant '" + record.getFieldAsString("PRIMK_ARBSIT_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("PRIMK_ARBSIT_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        + "Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("PRIMK_ARBSIT_A"),
                record.getFieldDefinitionByName("PRIMK_ARBSIT_A").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean kontroll19AVarighetSamtalepartner(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 19A Varighet på relasjon mellom primærklient og viktigste samtalepartner, partnere
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 19A Varighet på relasjon mellom primærklient og viktigste samtalepartner, partnere"
                        , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. "
                        + "Fant '" + record.getFieldAsString("PART_LENGDE_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("PART_LENGDE_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("PRIMK_VSRELASJ_A"),
                List.of("1"),
                record.getFieldAsString("PART_LENGDE_A"),
                record.getFieldDefinitionByName("PART_LENGDE_A").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean kontroll19B1VarighetSidenBrudd(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 19B1 Tid siden brudd for primærklient og viktigste samtalepartner, ekspartnere, lengde
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 19B1 Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere, lengde"
                        , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. "
                        + "Fant '" + record.getFieldAsString("EKSPART_LENGDE_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("EKSPART_LENGDE_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("PRIMK_VSRELASJ_A"),
                List.of("2"),
                record.getFieldAsString("EKSPART_LENGDE_A"),
                record.getFieldDefinitionByName("EKSPART_LENGDE_A").getCodeList().stream().map(Code::code).toList());
    }


    public static boolean kontroll19B2VarighetEkspartner(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 19B2 Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere, varighet
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 19B2 Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere, varighet"
                        , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. "
                        + "Fant '" + record.getFieldAsString("EKSPART_VARIGH_A") + "', "
                        + "forventet én av: " + record.getFieldDefinitionByName("EKSPART_VARIGH_A").getCodeList().stream().map(Code::toString).toList() + " ). "
                        , Constants.NORMAL_ERROR
                ),
                record.getFieldAsString("PRIMK_VSRELASJ_A"),
                List.of("2"),
                record.getFieldAsString("EKSPART_VARIGH_A"),
                record.getFieldDefinitionByName("EKSPART_VARIGH_A").getCodeList().stream().map(Code::code).toList());
    }

    public static boolean kontroll20DatoForsteBehandlingssamtale(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 20 Dato for første behandlingssamtale
        errorReport.incrementCount();

        return ControlFelt1Dato.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 20 Dato for første behandlingssamtale"
                        , "Det er ikke oppgitt dato for første behandlingssamtale eller feltet har ugyldig format. "
                        + "Fant '" + record.getFieldAsString("FORSTE_SAMT_A") + "'. "
                        + "Feltet er obligatorisk å fylle ut."
                        , Constants.NORMAL_ERROR
                )
                , record.getFieldAsString("FORSTE_SAMT_A")
                , record.getFieldDefinitionByName("FORSTE_SAMT_A").getDatePattern()
        );
    }

    public static boolean kontroll21DatoForsteBehandlingssamtaleEtterHenvendelse(
            final ErrorReport errorReport, final KostraRecord record) {

        // Kontroll 21 Første behandlingssamtale er før henvendelsesdato
        errorReport.incrementCount();

        return ControlFelt1DatoSaaFelt2Dato.doControl(
                errorReport
                , new ErrorReportEntry(
                        createKontorNr(record.getFieldAsString("KONTOR_NR_A"))
                        , createJournalNr(record.getFieldAsString("JOURNAL_NR_A"), String.valueOf(record.getLine()))
                        , String.valueOf(record.getLine())
                        , " "
                        , "Kontroll 21 Første behandlingssamtale er før henvendelsesdato"
                        , "Dato for første behandlingssamtale (" + record.getFieldAsString("FORSTE_SAMT_A") + ") "
                        + "er før dato for primærklientens henvendelse (" + record.getFieldAsString("HENV_DATO_A") + ") til familievernkontoret."
                        , Constants.NORMAL_ERROR
                )
                , record.getFieldAsString("HENV_DATO_A")
                , record.getFieldDefinitionByName("HENV_DATO_A").getDatePattern()
                , record.getFieldAsString("FORSTE_SAMT_A")
                , record.getFieldDefinitionByName("FORSTE_SAMT_A").getDatePattern()
        );
    }
}
