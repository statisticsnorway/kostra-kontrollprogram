package no.ssb.kostra.control.famvern.s52a;

import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    private static String createKontorNr(String kontornr) {
        return "Kontornummer ".concat(kontornr);
    }

    private static String createJournalNr(String journalnr, String linjenr) {
        return "Journalnummer ".concat(journalnr).concat(" / Linjenummer ").concat(linjenr);
    }

    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> inputFileContent = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(inputFileContent, er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<Record> records = inputFileContent.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, er);

//        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
//            return er;
//        }


        records.forEach(r -> {
            // Kontroll 3: Regionsnummer
            if (!Definitions.isRegionValid(r.getFieldAsString("REGION_NR_A"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 03 Regionsnummer"
                                , "Regionsnummeret som er oppgitt i recorden fins ikke i listen med gyldige regionsnumre. "
                                + "Fant '" + r.getFieldAsString("REGION_NR_A") + "', forventet én av : " + Definitions.getRegionAsList() + ". "
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            // Kontroll 4: Kontornummer
            if (!Definitions.isKontorValid(r.getFieldAsString("KONTOR_NR_A"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 04 Kontornummer"
                                , "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. "
                                + "Fant '" + r.getFieldAsString("KONTOR_NR_A") + "', forventet én av : " + Definitions.getKontorAsList() + ". "
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            // Kontroll 5: Manglende samsvar mellom regions- og kontornummer.
            if (!Definitions.isRegionAndKontorValid(r.getFieldAsString("REGION_NR_A"), r.getFieldAsString("KONTOR_NR_A"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 05 Manglende samsvar mellom regions- og kontornummer."
                                , "Regionsnummer (" + r.getFieldAsString("REGION_NR_A") + ") og "
                                + "kontornummer (" + r.getFieldAsString("KONTOR_NR_A") + ") stemmer ikke overens."
                                , Constants.NORMAL_ERROR
                        )
                );
            }
        });

        // Kontroll 6 Dublett på journalnummer
        List<String> dubletter = records.stream()
                .map(r -> "Kontornummer ".concat(r.getFieldAsString("KONTOR_NR_A")).concat(" - Journalnummer ").concat(r.getFieldAsString("JOURNAL_NR_A")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(p -> p.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        if (!dubletter.isEmpty()) {
            er.addEntry(
                    new ErrorReportEntry("Filuttrekk", "Dubletter", " ", " "
                            , "Kontroll 06 Dubletter"
                            , "Journalnummeret er benyttet på mer enn en sak. Dubletter på kontornummer - journalnummer. (Gjelder for:<br/>\n" + String.join(",<br/>\n", dubletter) + ")"
                            , Constants.NORMAL_ERROR
                    ));
        }


        records.forEach(r -> {
            // Kontroll 7 Henvendelsesdato
            ControlFelt1Dato.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 07 Henvendelsesdato"
                            , "Dette er ikke oppgitt dato (" + r.getFieldAsString("HENV_DATO_A") + ") for når primærklienten henvendte seg til familievernkontoret eller feltet har ugyldig format (DDMMÅÅÅÅ). Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("HENV_DATO_A")
                    , r.getFieldDefinitionByName("HENV_DATO_A").getDatePattern()
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 09 Primærklient hatt kontakt med eller vært klient tidligere"
                            , "Det er ikke fylt ut om primærklienten har vært i kontakt med/klient ved familievernet tidligere, eller feil kode er benyttet. "
                            + "Fant '" + r.getFieldAsString("KONTAKT_TIDL_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("KONTAKT_TIDL_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("KONTAKT_TIDL_A")
                    , r.getFieldDefinitionByName("KONTAKT_TIDL_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 11 Primærklientens viktigste begrunnelse for å ta kontakt."
                            , "Dette er ikke oppgitt hva som er primærklientens viktigste ønsker med kontakten, eller feltet har ugyldig format."
                            + "Fant '" + r.getFieldAsString("HENV_GRUNN_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("HENV_GRUNN_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("HENV_GRUNN_A")
                    , r.getFieldDefinitionByName("HENV_GRUNN_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 13 Primærklientens kjønn"
                            , "Primærklientens kjønn er ikke fylt ut eller feil kode er benyttet. "
                            + "Fant '" + r.getFieldAsString("PRIMK_KJONN_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("PRIMK_KJONN_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , "PRIMK_KJONN_A"
                    , r.getFieldDefinitionByName("PRIMK_KJONN_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlHeltall.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 14 Primærklientens fødselsår"
                            , "Dette er ikke oppgitt fødselsår på primærklienten eller feltet har ugyldig format. Fant '" + r.getFieldAsString("PRIMK_FODT_A") + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsIntegerDefaultEquals0("PRIMK_FODT_A")
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 15 Primærklientens samlivsstatus"
                            , "Primærklientens samlivsstatus ved sakens opprettelse er ikke fylt ut eller feil kode er benyttet. "
                            + "Fant '" + r.getFieldAsString("PRIMK_SIVILS_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("PRIMK_SIVILS_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("PRIMK_SIVILS_A")
                    , r.getFieldDefinitionByName("PRIMK_SIVILS_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 16 Utdypende om primærklientens formelle sivilstand"
                            , "Det er oppgitt at primærklientens samlivsstatus er Samboer eller at primærklient ikke lever i samliv, men det er ikke fylt ut hva som er primærklientens korrekt sivilstand"
                            + "Fant '" + r.getFieldAsString("FORMELL_SIVILS_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("FORMELL_SIVILS_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("PRIMK_SIVILS_A")
                    , List.of("3", "4")
                    , r.getFieldAsString("FORMELL_SIVILS_A")
                    , r.getFieldDefinitionByName("FORMELL_SIVILS_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 17 Primærklientens bosituasjon ved opprettelsen"
                            , "Det er ikke fylt ut om primærklienten bor sammen med andre ved sakens opprettelse eller feil kode er benyttet. "
                            + "Fant '" + r.getFieldAsString("PRIMK_SAMBO_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("PRIMK_SAMBO_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("PRIMK_SAMBO_A")
                    , r.getFieldDefinitionByName("PRIMK_SAMBO_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 18 Primærklientens tilknytning til utdanning og arbeidsliv"
                            , "Det er ikke krysset av for primærklientens tilknytning til arbeidslivet ved sakens opprettelse eller feil kode er benyttet. "
                            + "Fant '" + r.getFieldAsString("PRIMK_ARBSIT_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("PRIMK_ARBSIT_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("PRIMK_ARBSIT_A")
                    , r.getFieldDefinitionByName("PRIMK_ARBSIT_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 19A Varighet på relasjon mellom primærklient og viktigste samtalepartner, partnere"
                            , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. "
                            + "Fant '" + r.getFieldAsString("PART_LENGDE_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("PART_LENGDE_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("PRIMK_VSRELASJ_A")
                    , List.of("1")
                    , r.getFieldAsString("PART_LENGDE_A")
                    , r.getFieldDefinitionByName("PART_LENGDE_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 19B1 Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere, lengde"
                            , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. "
                            + "Fant '" + r.getFieldAsString("EKSPART_LENGDE_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("EKSPART_LENGDE_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("PRIMK_VSRELASJ_A")
                    , List.of("2")
                    , r.getFieldAsString("EKSPART_LENGDE_A")
                    , r.getFieldDefinitionByName("EKSPART_LENGDE_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 19B2 Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere, varighet"
                            , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. "
                            + "Fant '" + r.getFieldAsString("EKSPART_VARIGH_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("EKSPART_VARIGH_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("PRIMK_VSRELASJ_A")
                    , List.of("2")
                    , r.getFieldAsString("EKSPART_VARIGH_A")
                    , r.getFieldDefinitionByName("EKSPART_VARIGH_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );


            ControlFelt1Dato.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 20 Dato for første behandlingssamtale"
                            , "Det er ikke oppgitt dato for første behandlingssamtale eller feltet har ugyldig format. "
                            + "Fant '" + r.getFieldAsString("FORSTE_SAMT_A") + "'. "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("FORSTE_SAMT_A")
                    , r.getFieldDefinitionByName("FORSTE_SAMT_A").getDatePattern()
            );

            ControlFelt1DatoSaaFelt2Dato.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 21 Første behandlingssamtale er før henvendelsesdato"
                            , "Dato for første behandlingssamtale (" + r.getFieldAsString("FORSTE_SAMT_A") + ") "
                            + "er før dato for primærklientens henvendelse (" + r.getFieldAsString("HENV_DATO_A") + ") til familievernkontoret."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("HENV_DATO_A")
                    , r.getFieldDefinitionByName("HENV_DATO_A").getDatePattern()
                    , r.getFieldAsString("FORSTE_SAMT_A")
                    , r.getFieldDefinitionByName("FORSTE_SAMT_A").getDatePattern()
            );

            {
                if (List.of(
                        "TEMA_PARREL_A", "TEMA_AVKLAR_A", "TEMA_SAMLBRUDD_A", "TEMA_SAMSPILL_A",
                        "TEMA_BARNSIT_A", "TEMA_BARNFOR_A", "TEMA_BOSTED_A", "TEMA_FORELDRE_A",
                        "TEMA_FORBARN_A", "TEMA_FLERGEN_A", "TEMA_SAMBARN_A", "TEMA_SÆRBARN_A",
                        "TEMA_KULTUR_A", "TEMA_TVANG_A", "TEMA_RUS_A", "TEMA_SYKD_A",
                        "TEMA_VOLD_A", "TEMA_ALVH_A")
                        .stream()
                        .noneMatch(field -> r.getFieldDefinitionByName(field)
                                .getCodeList()
                                .stream()
                                .map(Code::getCode)
                                .collect(Collectors.toList())
                                .contains(r.getFieldAsString(field)))) {
                    er.addEntry(
                            new ErrorReportEntry(
                                    createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                    , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                    , String.valueOf(r.getLine())
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
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 23 Hovedform på behandlingstilbudet"
                            , "Det er ikke krysset av for hva som har vært hovedformen på behandlingstilbudet siden saken ble opprettet, eller feil kode er benyttet. "
                            + "Fant '" + r.getFieldAsString("HOVEDF_BEHAND_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("HOVEDF_BEHAND_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("HOVEDF_BEHAND_A")
                    , r.getFieldDefinitionByName("HOVEDF_BEHAND_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
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
                            er,
                            new ErrorReportEntry(
                                    createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                    , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 24 Deltagelse i behandlingssamtaler med primærklienten i løpet av året, " + fieldPair.get(0)
                                    , "Det er ikke krysset av for om andre deltakere (" + fieldPair.get(0) + ") i saken har deltatt i samtaler "
                                    + "med primærklienten i løpet av rapporteringsåret. Feltene er obligatorisk å fylle ut.<br/>\n"
                                    , Constants.NORMAL_ERROR
                            ),
                            r.getFieldAsString(fieldPair.get(1)),
                            r.getFieldDefinitionByName(fieldPair.get(1)).getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    )
            );

            if (List.of(
                    "SAMT_PRIMK_A", "SAMT_PARTNER_A", "SAMT_EKSPART_A", "SAMT_BARNU18_A", "SAMT_BARNO18_A",
                    "SAMT_FORELDRE_A", "SAMT_OVRFAM_A", "SAMT_VENN_A", "SAMT_ANDRE_A")
                    .stream()
                    .map(field -> {
                        try {
                            return Objects.requireNonNullElse(r.getFieldAsInteger(field), 0);
                        } catch (NullPointerException e) {
                            return 0;
                        }
                    })
                    .noneMatch(value -> 0 < value)
            ) {
                er.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 25 Behandlingssamtaler for de involverte i saken i løpet av året"
                                , "Det er ikke oppgitt hvor mange behandlingssamtaler de ulike deltakerne "
                                + "i saken har deltatt i gjennom året. Feltet er obligatorisk å fylle ut."
                                , Constants.NORMAL_ERROR
                        )
                );
            }


            {
                List<List<String>> fieldPairs = List.of(
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
                                er,
                                new ErrorReportEntry(
                                        createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                        , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 26 Relasjon mellom antall behandlingssamtaler og hvem som har deltatt, " + fieldPair.get(0)
                                        , "Det er oppgitt at andre personer (" + fieldPair.get(0) + ") har deltatt i samtaler med primærklient i løpet av året, "
                                        + "men det er ikke oppgitt hvor mange behandlingssamtaler (" + r.getFieldAsString(fieldPair.get(2)) + ") de ulike personene har deltatt i gjennom av året."
                                        , Constants.NORMAL_ERROR
                                ),
                                r.getFieldAsString(fieldPair.get(1)),
                                r.getFieldDefinitionByName(fieldPair.get(1)).getCodeList().stream().filter(c -> c.getValue().equalsIgnoreCase("Ja")).map(Code::getCode).collect(Collectors.toList()),
                                r.getFieldAsIntegerDefaultEquals0(fieldPair.get(2)),
                                ">",
                                0
                        )
                );

            }

            {
                if (List.of("ANTSAMT_HOVEDT_A", "ANTSAMT_ANDREANS_A").stream().noneMatch(field -> 0 < r.getFieldAsIntegerDefaultEquals0(field))) {
                    er.addEntry(
                            new ErrorReportEntry(
                                    createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                    , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 27 Antall behandlingssamtaler for ansatte ved kontoret i løpet av året"
                                    , "Det er ikke oppgitt hvor mange behandlingssamtaler hovedterapeut (" + r.getFieldAsIntegerDefaultEquals0("ANTSAMT_HOVEDT_A") + ") "
                                    + "eller andre ansatte (" + r.getFieldAsIntegerDefaultEquals0("ANTSAMT_HOVEDT_A") + ") har deltatt i gjennom året. Feltet er obligatorisk å fylle ut."
                                    , Constants.NORMAL_ERROR
                            )
                    );
                }
            }

            ControlFelt1Boolsk.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 28 Antall behandlingssamtaler i løpet av året"
                            , "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført i saken i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsIntegerDefaultEquals0("ANTSAMT_IARET_A")
                    , ">"
                    , 0
            );

            ControlFelt1Boolsk.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 29 Antall behandlingssamtaler siden opprettelsen"
                            , "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført siden saken ble opprettet. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsIntegerDefaultEquals0("ANTSAMT_OPPR_A")
                    , ">"
                    , 0
            );

            ControlFelt1Boolsk.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 30 Totalt antall timer i løpet av året"
                            , "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt på saken "
                            + "(timer benyttet til gruppesamtaler skal holdes utenfor) i løpet av året "
                            + "(for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsIntegerDefaultEquals0("TIMER_IARET_A")
                    , ">"
                    , 0
            );

            ControlFelt1Boolsk.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 31 Totalt antall timer siden saken ble opprettet"
                            , "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt siden saken ble opprettet "
                            + "(for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut"
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsIntegerDefaultEquals0("TIMER_OPPR_A")
                    , ">"
                    , 0
            );

            {
                if (List.of(
                        "SAMARB_INGEN_A", "SAMARB_LEGE_A", "SAMARB_HELSE_A", "SAMARB_PSYKH_A", "SAMARB_JURIST_A", "SAMARB_KRISES_A",
                        "SAMARB_SKOLE_A", "SAMARB_SOS_A", "SAMARB_KOMMB_A", "SAMARB_STATB_A", "SAMARB_ANDRE_A")
                        .stream()
                        .map(field -> {
                            try {
                                return Objects.requireNonNullElse(r.getFieldAsInteger(field), 0);
                            } catch (NullPointerException e) {
                                return 0;
                            }
                        })
                        .noneMatch(value -> 0 < value)
                ) {
                    er.addEntry(
                            new ErrorReportEntry(
                                    createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                                    , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 32 Samarbeid med andre instanser siden opprettelsen"
                                    , "Det er ikke krysset av for om det har vært samarbeid med andre instanser "
                                    + "siden saken ble opprettet. Feltet er obligatorisk å fylle ut."
                                    , Constants.NORMAL_ERROR
                            )
                    );
                }
            }

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 33 Status ved året slutt"
                            , "Det er ikke fylt ut hva som er sakens status ved utgangen av året. "
                            + "Fant '" + r.getFieldAsString("STATUS_ARETSSL_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("STATUS_ARETSSL_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("STATUS_ARETSSL_A")
                    , r.getFieldDefinitionByName("STATUS_ARETSSL_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 34 Sakens hovedtema (Fylles ut når saken avsluttes)."
                            , "Det er krysset av for at saken er avsluttet i rapporteringsåret, men ikke fylt ut hovedtema for saken, eller feltet har ugyldig format."
                            + "Fant '" + r.getFieldAsString("HOVEDTEMA_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("HOVEDTEMA_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("STATUS_ARETSSL_A")
                    , List.of("1", "2")
                    , r.getFieldAsString("HOVEDTEMA_A")
                    , r.getFieldDefinitionByName("HOVEDTEMA_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 35 Saken avsluttet, men ikke satt avslutningsdato"
                            , "Det er krysset av for at saken er avsluttet i rapporteringsåret, "
                            + "men ikke fylt ut dato (" + r.getFieldAsString("DATO_AVSL_A") + ") for avslutning av saken."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("STATUS_ARETSSL_A")
                    , List.of("1", "2")
                    , r.getFieldAsLocalDate("DATO_AVSL_A")
            );

            ControlFelt1DatoSaaFelt2Dato.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 36 Avslutningsdato før første samtale"
                            , "Dato for avslutting av saken (" + r.getFieldAsString("DATO_AVSL_A") + ") "
                            + "kommer før dato for første behandlingssamtale (" + r.getFieldAsString("FORSTE_SAMT_A") + ")."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("FORSTE_SAMT_A")
                    , r.getFieldDefinitionByName("FORSTE_SAMT_A").getDatePattern()
                    , r.getFieldAsString("DATO_AVSL_A")
                    , r.getFieldDefinitionByName("DATO_AVSL_A").getDatePattern()
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_A"))
                            , createJournalNr(r.getFieldAsString("JOURNAL_NR_A"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 37 Bekymringsmelding sendt barnevernet"
                            , "Det er ikke svart på hvorvidt bekymringsmelding er sendt barnevernet eller ei, eller feil kode er benyttet. "
                            + "Fant '" + r.getFieldAsString("BEKYMR_MELD_A") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("BEKYMR_MELD_A").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            + "Feltet er obligatorisk."
                            , Constants.NORMAL_ERROR
                    )
                    , r.getFieldAsString("BEKYMR_MELD_A")
                    , r.getFieldDefinitionByName("BEKYMR_MELD_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );
        });

        return er;
    }
}
