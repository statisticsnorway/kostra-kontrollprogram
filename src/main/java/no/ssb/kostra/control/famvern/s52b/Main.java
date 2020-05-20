package no.ssb.kostra.control.famvern.s52b;

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

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(records.stream(), er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        records.stream()
                // Kontroll 3: Regionsnummer
                .peek(r -> {
                    if (!Definitions.isRegionValid(r.getFieldAsString("REGION_NR_B"))) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
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
                    if (!Definitions.isKontorValid(r.getFieldAsString("KONTOR_NR_B"))) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
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
                    if (!Definitions.isRegionAndKontorValid(r.getFieldAsString("REGION_NR_B"), r.getFieldAsString("KONTOR_NR_B"))) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
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
                .map(r -> r.getFieldAsString("GRUPPE_NR_B"))
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
                .peek(r ->
                        ControlFelt1Lengde.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 7 Gruppenavn"
                                        , "Dette er ikke oppgitt navn på gruppen. Tekstfeltet skal ha maksimalt 30 posisjoner."
                                        , Constants.NORMAL_ERROR
                                )
                                , "GRUPPE_NAVN_B"
                        )
                )
                .peek(r -> ControlFelt1Dato.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("KONTOR_NR_B")
                                , r.getFieldAsString("GRUPPE_NR_B")
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 8 Start dato"
                                , "Dette er ikke oppgitt dato for gruppebehandlingens start, eller feltet har ugyldig format."
                                , Constants.NORMAL_ERROR
                        )
                        , "DATO_GRSTART_B"
                ))
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 9 Målgruppe"
                                        , "Det er ikke fylt ut hva som er målgruppe for behandlingen."
                                        , Constants.NORMAL_ERROR
                                )
                                , "STRUKTUR_GR_B"
                                , r.getFieldDefinitionByName("STRUKTUR_GR_B").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 10 Gruppens hovedtema"
                                        , "Det er ikke fylt ut hva som er hovedtema for gruppen."
                                        , Constants.NORMAL_ERROR
                                )
                                , "HOVEDI_GR_B"
                                , r.getFieldDefinitionByName("HOVEDI_GR_B").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 11 Antall gruppemøter gjennomført totalt i løpet av året"
                                        , "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt i løpet av rapporteringsåret."
                                        , Constants.NORMAL_ERROR
                                )
                                , "ANTMOTERTOT_IARET_B"
                                , ">"
                                , 0

                        )
                )

                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 12 Antall gruppemøter gjennomført siden opprettelsen"
                                        , "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt siden gruppen ble opprettet."
                                        , Constants.NORMAL_ERROR
                                )
                                , "ANTMOTERTOT_OPPR_B"
                                , ">"
                                , 0

                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 13 Antall timer anvendt i gruppen totalt i løpet av året"
                                        , "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen i løpet av året. (For og etterarbeid skal ikke regnes med)."
                                        , Constants.NORMAL_ERROR
                                )
                                , "TIMERTOT_IARET_B"
                                , ">"
                                , 0

                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 14 Antall timer siden opprettelsen"
                                        , "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen siden opprettelsen "
                                        , Constants.NORMAL_ERROR
                                )
                                , "TIMERTOT_OPPR_B"
                                , ">"
                                , 0

                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 15 Antall deltagere i gruppen i løpet av året"
                                        , "Det er ikke fylt ut hvor mange som har deltatt i gruppen i løpet av året. Terapeuter holdes utenom."
                                        , Constants.NORMAL_ERROR
                                )
                                , "ANTDELT_IARET_B"
                                , ">"
                                , 0

                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 16 Antall deltagere i gruppen siden opprettelsen"
                                        , "Det er ikke fylt ut hvor mange som har deltatt i gruppen siden opprettelsen. Terapeuter holdes utenom"
                                        , Constants.NORMAL_ERROR
                                )
                                , "ANTDELT_OPPR_B"
                                , ">"
                                , 0

                        )
                )
                .peek(r ->
                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 17 Antall terapeuter involvert i gruppebehandlingen"
                                        , "Det er ikke oppgitt hvor mange hovedterapeut eller andre ansatte som har deltatt i gruppen."
                                        , Constants.NORMAL_ERROR
                                )
                                , "ANTTER_GRUPPEB_B"
                                , ">"
                                , 0

                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 18 Er det benyttet tolk i minst én gruppesamtale?"
                                        , "Kontroller at feltet er utfylt og ikke inneholder andre verdier enn de gyldige 1 til 2."
                                        , Constants.NORMAL_ERROR
                                )
                                , "TOLK_B"
                                , r.getFieldDefinitionByName("TOLK_B").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 19 Status ved året slutt"
                                        , "Det er ikke fylt ut hva som er gruppens status ved utgangen av året."
                                        , Constants.NORMAL_ERROR
                                )
                                , "STATUS_ARETSSL_B"
                                , r.getFieldDefinitionByName("STATUS_ARETSSL_B").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                        )
                )
                .peek(r ->
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 20 Gruppebehandlingen er avsluttet, men avslutningsdato mangler"
                                        , "Det er krysset av for at gruppebehandlingen er avsluttet i rapporteringsåret, men ikke fylt ut dato for avslutning av saken."
                                        , Constants.NORMAL_ERROR
                                )
                                , "STATUS_ARETSSL_B"
                                , List.of("2")
                                , "DATO_GRAVSLUTN_B"
                        )
                )
                .peek(r ->
                        ControlFelt1DatoSaaFelt2Dato.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("KONTOR_NR_B")
                                        , r.getFieldAsString("GRUPPE_NR_B")
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 21 Avslutningsdato før første samtale"
                                        , "Dato for avslutting av gruppebehandlingen kommer før dato for gruppebehandlingens start."
                                        , Constants.NORMAL_ERROR
                                )
                                , "DATO_GRSTART_B"
                                , "DATO_GRAVSLUTN_B"
                        )
                )


                .close();

        return er;
    }
}
