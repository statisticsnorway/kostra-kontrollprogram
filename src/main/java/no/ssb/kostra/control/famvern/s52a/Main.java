package no.ssb.kostra.control.famvern.s52a;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> inputFileContent = args.getInputFileContent();
        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<Record> records = inputFileContent.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
        Integer n = records.size();
        Integer l = String.valueOf(n).length();
        final String lf = Constants.lineSeparator;

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(records.stream(), er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        records.stream()
                // Kontroll 3: Regionsnummer
                .peek(r -> {
                    if (!Definitions.isRegionValid(r.getFieldAsString("REGION_NR_A"))) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 3 Regionsnummer"
                                        , "Regionsnummeret som er oppgitt i kontrollprogrammet stemmer ikke med regions-nummeret som er oppgitt i recorden (filuttrekket). "
                                        + "Kontroller at riktig regionsnummeret ble oppgitt til kontrollprogrammet. "
                                        + "Hvis dette stemmer, må regionsnummeret i recorden (filuttrekket) rettes."
                                        , Constants.NORMAL_ERROR
                                )

                        );
                    }
                })
                // Kontroll 4: Kontornummer
                .peek(r -> {
                    if (!Definitions.isKontorValid(r.getFieldAsString("KONTOR_NR_A"))) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 4 Kontornummer"
                                        , "Det er ikke oppgitt kontornummer, eller feil kode er benyttet. Feltet er obligatorisk og må fylles ut. "
                                        , Constants.NORMAL_ERROR
                                )

                        );
                    }
                })
                // Kontroll 5: Manglende samsvar mellom regions- og kontornummer.
                .peek(r -> {
                    if (!Definitions.isRegionAndKontorValid(r.getFieldAsString("REGION_NR_A"), r.getFieldAsString("KONTOR_NR_A"))) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 5 Manglende samsvar mellom regions- og kontornummer."
                                        , "Regionsnummer og kontornummer stemmer ikke overens."
                                        , Constants.NORMAL_ERROR
                                )

                        );
                    }
                })
                .close();


        // Kontroll 6 Dublett på journalnummer
        List<String> dubletter = records.stream()
                .map(r -> r.getFieldAsString("JOURNAL_NR_A"))
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
                            , "Kontroll Dubletter"
                            , "Dubletter på journalnummer. (Gjelder for:<br/>\n" + String.join(",<br/>\n", dubletter) + ")"
                            , Constants.NORMAL_ERROR
                    ));
        }


        records.stream()
                // Kontroll 7 Henvendelsesdato
                .peek(r ->
                        ControlFelt1Dato.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 15 Dato for registrert søknad ved NAV-kontoret."
                                        , "Feltet for Hvilken dato ble søknaden registrert ved NAV-kontoret? mangler utfylling eller har ugyldig dato. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "HENV_DATO_A"
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 9 Primærklient hatt kontakt med eller vært klient tideligere."
                                        , "Det er ikke fylt ut om primærklienten har vært i kontakt med/klient ved familievernet tidligere, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "KONTAKT_TIDL_A"
                                , r.getFieldDefinitionByName("KONTAKT_TIDL_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 11 Primærklientens viktigste begrunnelse for å ta kontakt."
                                        , "Dette er ikke oppgitt hva som er primærklientens viktigste ønsker med kontakten, eller feltet har ugyldig format. Feltet er obligatorisk."
                                        , Constants.NORMAL_ERROR
                                )
                                , "HENV_GRUNN_A"
                                , r.getFieldDefinitionByName("HENV_GRUNN_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 13 Primærklientens kjønn"
                                        , "Primærklientens kjønn er ikke fylt ut eller feil kode er benyttet. Feltet er obligatorisk."
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_KJONN_A"
                                , r.getFieldDefinitionByName("PRIMK_KJONN_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlHeltall.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 14 Primærklientens fødselsår"
                                        , "Dette er ikke oppgitt fødselsår på primærklienten eller feltet har ugyldig format. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_FODT_A"
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 15 Primærklientens samlivsstatus"
                                        , "Primærklientens samlivsstatus ved sakens opprettelse er ikke fylt ut eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_SIVILS_A"
                                , r.getFieldDefinitionByName("PRIMK_SIVILS_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 16 Utdypende om primærklientens formelle sivilstand"
                                        , "Det er oppgitt at primærklientens samlivsstatus er Samboer eller at primærklient ikke lever i samliv, men det er ikke fylt ut hva som er primærklientens korrekt sivilstand"
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_SIVILS_A"
                                , List.of("3", "4")
                                , "FORMELL_SIVILS_A"
                                , r.getFieldDefinitionByName("FORMELL_SIVILS_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 17 Primærklientens bosituasjon ved opprettelsen"
                                        , "Det er ikke fylt ut om primærklienten bor sammen med andre ved sakens opprettelse eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_SAMBO_A"
                                , r.getFieldDefinitionByName("PRIMK_SAMBO_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 18 Primærklientens tilknytning til utdanning og arbeidsliv"
                                        , "Det er ikke krysset av for primærklientens tilknytning til arbeidslivet ved sakens opprettelse eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_ARBSIT_A"
                                , r.getFieldDefinitionByName("PRIMK_ARBSIT_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 19A Varighet på relasjon mellom primærklient og viktigste samtalepartner, partnere"
                                        , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere."
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_VSRELASJ_A"
                                , List.of("1")
                                , "PART_LENGDE_A"
                                , r.getFieldDefinitionByName("PART_LENGDE_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                // Kontroll 19 sjekker 2 forskjellige forhold og blir derfor repetert
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 19B Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere"
                                        , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. "
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_VSRELASJ_A"
                                , List.of("2")
                                , "EKSPART_LENGDE_A"
                                , r.getFieldDefinitionByName("EKSPART_LENGDE_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 19B Varighet på relasjon mellom primærklient og viktigste samtalepartner, ekspartnere"
                                        , "Det er oppgitt at primærklientens relasjon til viktigste deltager er partner, men det er ikke oppgitt hvor lenge partene har vært gift, samboere eller registrerte partnere. "
                                        , Constants.NORMAL_ERROR
                                )
                                , "PRIMK_VSRELASJ_A"
                                , List.of("2")
                                , "EKSPART_VARIGH_A"
                                , r.getFieldDefinitionByName("EKSPART_VARIGH_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1Dato.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 20 Dato for første behandlingssamtale"
                                        , "Dette er ikke oppgitt dato for første behandlingssamtale eller feltet har ugyldig format. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "FORSTE_SAMT_A"
                        )
                )
                .peek(r ->
                        ControlFelt1DatoSaaFelt2Dato.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 21 Første behandlingssamtale er før henvendelsesdato"
                                        , "Dato for første behandlingssamtale er før dato for primærklientens henvendelse til familievernkontoret."
                                        , Constants.NORMAL_ERROR
                                )
                                , "HENV_DATO_A"
                                , "FORSTE_SAMT_A"
                        )
                )
                .peek(r -> {
                    List<String> fields = List.of(
                            "TEMA_PARREL_A", "TEMA_AVKLAR_A", "TEMA_SAMLBRUDD_A", "TEMA_SAMSPILL_A",
                            "TEMA_BARNSIT_A", "TEMA_BARNFOR_A", "TEMA_BOSTED_A", "TEMA_FORELDRE_A",
                            "TEMA_FORBARN_A", "TEMA_FLERGEN_A", "TEMA_SAMBARN_A", "TEMA_SÆRBARN_A",
                            "TEMA_KULTUR_A", "TEMA_TVANG_A", "TEMA_RUS_A", "TEMA_SYKD_A",
                            "TEMA_VOLD_A", "TEMA_ALVH_A");
                    boolean isAnyFilledIn = fields.stream()
                            .anyMatch(field -> r.getFieldDefinitionByName(field).getCodeList().stream().map(Code::getCode).collect(Collectors.toList()).contains(r.getFieldAsString(field)));

                    if (!isAnyFilledIn) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 22 Områder det har vært arbeidet med i saken"
                                        , "Det er ikke fylt ut hvilke områder det har vært arbeidet med siden saken ble opprettet. "
                                        + "Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område."
                                        , Constants.NORMAL_ERROR
                                )
                        );
                    }
                })
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 23 Hovedform på behandlingstilbudet"
                                        , "Det er ikke krysset av for hva som har vært hovedformen på behandlingstilbudet siden saken ble opprettet, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "HOVEDF_BEHAND_A"
                                , r.getFieldDefinitionByName("HOVEDF_BEHAND_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r -> {
                    List<String> fields = List.of(
                            "BEKYMR_MELD_A", "DELT_PARTNER_A", "DELT_EKSPART_A", "DELT_BARNU18_A",
                            "DELT_BARNO18_A", "DELT_FORELDRE_A", "DELT_OVRFAM_A", "DELT_VENN_A", "DELT_ANDR_A");
                    boolean isAllFilledIn = fields.stream()
                            .allMatch(field -> r.getFieldDefinitionByName(field).getCodeList().stream().map(Code::getCode).collect(Collectors.toList()).contains(r.getFieldAsString(field)));

                    if (!isAllFilledIn) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 24 Deltagelse i behandlingssamtaler med primærklienten i løpet av året."
                                        , "Det er ikke krysset av for om andre deltakere i saken har deltatt i samtaler "
                                        + "med primærklienten i løpet av rapporteringsåret. Feltene er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                        );
                    }
                })
                .peek(r -> {
                    List<String> fields = List.of(
                            "SAMT_PRIMK_A", "SAMT_PARTNER_A", "SAMT_EKSPART_A", "SAMT_BARNU18_A", "SAMT_BARNO18_A",
                            "SAMT_FORELDRE_A", "SAMT_OVRFAM_A", "SAMT_VENN_A", "SAMT_ANDRE_A");
                    boolean isAnyFilledIn = fields.stream().anyMatch(field -> 0 < r.getFieldAsInteger(field));

                    if (!isAnyFilledIn) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 25 Behandlingssamtaler for de involverte i saken i løpet av året"
                                        , "Det er ikke oppgitt hvor mange behandlingssamtaler de ulike deltakerne "
                                        + "i saken har deltatt i gjennom året. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                        );
                    }
                })
                .peek(r -> {
                    List<List<String>> fieldPairs = List.of(
                            List.of("DELT_PARTNER_A", "SAMT_PARTNER_A"),
                            List.of("DELT_EKSPART_A", "SAMT_EKSPART_A"),
                            List.of("DELT_BARNU18_A", "SAMT_BARNU18_A"),
                            List.of("DELT_BARNO18_A", "SAMT_BARNO18_A"),
                            List.of("DELT_FORELDRE_A", "SAMT_FORELDRE_A"),
                            List.of("DELT_OVRFAM_A", "SAMT_OVRFAM_A"),
                            List.of("DELT_VENN_A", "SAMT_VENN_A"),
                            List.of("DELT_ANDR_A", "SAMT_ANDRE_A")
                    );

                    fieldPairs.stream()
                            .peek(fieldPair ->
                                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                                            r,
                                            er,
                                            new ErrorReportEntry(
                                                    r.getFieldAsString("SAKSBEHANDLER")
                                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                                    , " "
                                                    , "Kontroll 26 Relasjon mellom antall behandlingssamtaler og hvem som har deltatt"
                                                    , "Det er oppgitt at andre personer har deltatt i samtaler med primærklient i løpet av året, "
                                                    + "men det er ikke oppgitt hvor mange behandlingssamtaler de ulike personene har deltatt i gjennom av året."
                                                    , Constants.NORMAL_ERROR
                                            ),
                                            fieldPair.get(0),
                                            r.getFieldDefinitionByName(fieldPair.get(0)).getCodeList().stream().map(Code::getCode).collect(Collectors.toList()),
                                            fieldPair.get(1),
                                            "<",
                                            0
                                    )
                            )
                            .close();
                })
                .peek(r -> {
                    List<String> fields = List.of("ANTSAMT_HOVEDT_A", "ANTSAMT_ANDREANS_A");
                    boolean isAnyFilledIn = fields.stream().anyMatch(field -> 0 < r.getFieldAsInteger(field));

                    if (!isAnyFilledIn) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 27 Antall behandlingssamtaler for ansatte ved kontoret i løpet av året"
                                        , "Det er ikke oppgitt hvor mange behandlingssamtaler hovedterapeut eller andre ansatte har deltatt i gjennom året. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                        );
                    }
                })
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 28 Antall behandlingssamtaler i løpet av året"
                                        , "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført i saken i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "ANTSAMT_IARET_A"
                                , ">"
                                , 0
                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 29 Antall behandlingssamtaler siden opprettelsen"
                                        , "Det er ikke fylt ut hvor mange behandlingssamtaler det er gjennomført siden saken ble opprettet. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "ANTSAMT_OPPR_A"
                                , ">"
                                , 0
                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 30 Totalt antall timer i løpet av året"
                                        , "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt på saken "
                                        + "(timer benyttet til gruppesamtaler skal holdes utenfor) i løpet av året "
                                        + "(for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "TIMER_IARET_A"
                                , ">"
                                , 0
                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 31 Totalt antall timer siden saken ble opprettet"
                                        , "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre ved kontoret har anvendt siden saken ble opprettet "
                                        + "(for og etterarbeid skal ikke regnes med). Feltet er obligatorisk å fylle ut"
                                        , Constants.NORMAL_ERROR
                                )
                                , "TIMER_OPPR_A"
                                , ">"
                                , 0
                        )
                )
                .peek(r -> {
                    List<String> fields = List.of(
                            "SAMARB_INGEN_A", "SAMARB_LEGE_A", "SAMARB_HELSE_A", "SAMARB_PSYKH_A", "SAMARB_JURIST_A", "SAMARB_KRISES_A",
                            "SAMARB_SKOLE_A", "SAMARB_SOS_A", "SAMARB_KOMMB_A", "SAMARB_STATB_A", "SAMARB_ANDRE_A");
                    boolean isAnyFilledIn = fields.stream().anyMatch(field -> 0 < r.getFieldAsInteger(field));

                    if (!isAnyFilledIn) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 32 Samarbeid med andre instanser siden opprettelsen"
                                        , "Det er ikke krysset av for om det har vært samarbeid med andre instanser "
                                        + "siden saken ble opprettet. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                        );
                    }
                })
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 33 Status ved året slutt"
                                        , "Det er ikke fylt ut hva som er sakens status ved utgangen av året. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                                , "STATUS_ARETSSL_A"
                                , r.getFieldDefinitionByName("STATUS_ARETSSL_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 34 Sakens hovedtema (Fylles ut når saken avsluttes)."
                                        , "Det er krysset av for at saken er avsluttet i rapporteringsåret, men ikke fylt ut hovedtema for saken, eller feltet har ugyldig format."
                                        , Constants.NORMAL_ERROR
                                )
                                , "STATUS_ARETSSL_A"
                                , List.of("1", "2")
                                , "HOVEDTEMA_A"
                                , r.getFieldDefinitionByName("HOVEDTEMA_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        ))
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 35 Saken avsluttet, men ikke satt avslutningsdato"
                                        , "Det er krysset av for at saken er avsluttet i rapporteringsåret, men ikke fylt ut dato for avslutning av saken."
                                        , Constants.NORMAL_ERROR
                                )
                                , "STATUS_ARETSSL_A"
                                , List.of("1", "2")
                                , "DATO_AVSL_A"
                        )
                )
                .peek(r ->
                        ControlFelt1DatoSaaFelt2Dato.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 36 Avslutningsdato før første samtale"
                                        , "Dato for avslutting av saken kommer før dato for første behandlingssamtale."
                                        , Constants.NORMAL_ERROR
                                )
                                , "FORSTE_SAMT_A"
                                , "DATO_AVSL_A"
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_A")
                                        , r.getFieldAsString("JOURNAL_NR_A")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 37 Bekymringsmelding sendt barnevernet"
                                        , ""
                                        , Constants.NORMAL_ERROR
                                )
                                , "BEKYMR_MELD_A"
                                , r.getFieldDefinitionByName("BEKYMR_MELD_A").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .close();

        return er;
    }
}
