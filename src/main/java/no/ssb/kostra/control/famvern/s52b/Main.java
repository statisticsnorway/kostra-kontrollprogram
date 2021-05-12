package no.ssb.kostra.control.famvern.s52b;

import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    private static String createKontorNr(String kontornr){
        return "Kontornummer ".concat(kontornr);
    }

    private static String createJournalNr(String gruppenr, String gruppenavn, String linjenr){
        return "Gruppenr ".concat(gruppenr).concat(" ").concat(gruppenavn).concat(" / Linjenummer ").concat(linjenr);
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

        records.forEach(r -> {
            // Kontroll 3: Regionsnummer
            if (!Definitions.isRegionValid(r.getFieldAsString("REGION_NR_B"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                                , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 03 Regionsnummer"
                                , "Regionsnummeret som er oppgitt i recorden fins ikke i listen med gyldige regionsnumre. "
                                + "Fant '" + r.getFieldAsString("REGION_NR_B") + "', forventet én av : " + Definitions.getRegionAsList() + ". "
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            // Kontroll 4: Kontornummer
            if (!Definitions.isKontorValid(r.getFieldAsString("KONTOR_NR_B"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                                , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 04 Kontornummer"
                                , "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. "
                                + "Fant '" + r.getFieldAsString("KONTOR_NR_B") + "', forventet én av : " + Definitions.getKontorAsList() + ". "
                                + "Feltet er obligatorisk og må fylles ut."
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            // Kontroll 5: Manglende samsvar mellom regions- og kontornummer.
            if (!Definitions.isRegionAndKontorValid(r.getFieldAsString("REGION_NR_B"), r.getFieldAsString("KONTOR_NR_B"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                                , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 05 Manglende samsvar mellom regions- og kontornummer."
                                , "Regionsnummer (" + r.getFieldAsString("REGION_NR_B") + ") og "
                                + "kontornummer (" + r.getFieldAsString("KONTOR_NR_B") + ") stemmer ikke overens."
                                , Constants.NORMAL_ERROR
                        )
                );
            }
        });

        // Kontroll 6 Dublett på gruppenummer
        List<String> dubletter = records.stream()
                .map(r -> "Kontornummer "
                        .concat(r.getFieldAsString("KONTOR_NR_B"))
                        .concat(" - Gruppe ")
                        .concat(r.getFieldAsString("GRUPPE_NR_B"))
                        .concat(" ")
                        .concat(r.getFieldAsString("GRUPPE_NAVN_B")))
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
                            , "Gruppenummeret er benyttet på mer enn en sak. Dubletter på kontornummer - gruppenummer. (Gjelder for:<br/>\n" + String.join(",<br/>\n", dubletter) + ")"
                            , Constants.NORMAL_ERROR
                    ));
        }


        records.forEach(r -> {
            ControlFelt1Lengde.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 07 Gruppenavn"
                            , "Det er ikke oppgitt navn på gruppen. Tekstfeltet skal ha maksimalt 30 posisjoner."
                            , Constants.NORMAL_ERROR
                    )
                    , "GRUPPE_NAVN_B"
            );

            ControlFelt1Dato.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 08 Start dato"
                            , "Det er ikke oppgitt dato for gruppebehandlingens start, eller feltet har ugyldig format."
                            + "Fant '" + r.getFieldAsString("DATO_GRSTART_B") + "'."
                            , Constants.NORMAL_ERROR
                    )
                    , "DATO_GRSTART_B"
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 09 Målgruppe"
                            , "Det er ikke fylt ut hva som er målgruppe for behandlingen. "
                            + "Fant '" + r.getFieldAsString("STRUKTUR_GR_B") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("STRUKTUR_GR_B").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "

                            , Constants.NORMAL_ERROR
                    )
                    , "STRUKTUR_GR_B"
                    , r.getFieldDefinitionByName("STRUKTUR_GR_B").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 10 Gruppens hovedtema"
                            , "Det er ikke fylt ut hva som er hovedtema for gruppen."
                            + "Fant '" + r.getFieldAsString("HOVEDI_GR_B") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("HOVEDI_GR_B").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , "HOVEDI_GR_B"
                    , r.getFieldDefinitionByName("HOVEDI_GR_B").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1Boolsk.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 11 Antall gruppemøter gjennomført totalt i løpet av året"
                            , "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt i løpet av rapporteringsåret."
                            , Constants.NORMAL_ERROR
                    )
                    , "ANTMOTERTOT_IARET_B"
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 12 Antall gruppemøter gjennomført siden opprettelsen"
                            , "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt siden gruppen ble opprettet."
                            , Constants.NORMAL_ERROR
                    )
                    , "ANTMOTERTOT_OPPR_B"
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 13 Antall timer anvendt i gruppen totalt i løpet av året"
                            , "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen i løpet av året. (For og etterarbeid skal ikke regnes med)."
                            , Constants.NORMAL_ERROR
                    )
                    , "TIMERTOT_IARET_B"
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 14 Antall timer anvendt i gruppen totalt siden opprettelsen"
                            , "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen siden opprettelsen "
                            , Constants.NORMAL_ERROR
                    )
                    , "TIMERTOT_OPPR_B"
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 15 Antall deltagere i gruppen i løpet av året"
                            , "Det er ikke fylt ut hvor mange som har deltatt i gruppen i løpet av året. Terapeuter holdes utenom."
                            , Constants.NORMAL_ERROR
                    )
                    , "ANTDELT_IARET_B"
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 16 Antall deltagere i gruppen siden opprettelsen"
                            , "Det er ikke fylt ut hvor mange som har deltatt i gruppen siden opprettelsen. Terapeuter holdes utenom"
                            , Constants.NORMAL_ERROR
                    )
                    , "ANTDELT_OPPR_B"
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 17 Antall terapeuter involvert i gruppebehandlingen"
                            , "Det er ikke oppgitt hvor mange hovedterapeut eller andre ansatte som har deltatt i gruppen."
                            , Constants.NORMAL_ERROR
                    )
                    , "ANTTER_GRUPPEB_B"
                    , ">"
                    , 0

            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 18 Er det benyttet tolk i minst én gruppesamtale?"
                            , "Kontroller at feltet er utfylt. "
                            + "Fant '" + r.getFieldAsString("TOLK_B") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("TOLK_B").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , "TOLK_B"
                    , r.getFieldDefinitionByName("TOLK_B").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 19 Status ved året slutt"
                            , "Det er ikke fylt ut hva som er gruppens status ved utgangen av året."
                            + "Fant '" + r.getFieldAsString("STATUS_ARETSSL_B") + "', "
                            + "forventet én av: " + r.getFieldDefinitionByName("STATUS_ARETSSL_B").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + " ). "
                            , Constants.NORMAL_ERROR
                    )
                    , "STATUS_ARETSSL_B"
                    , r.getFieldDefinitionByName("STATUS_ARETSSL_B").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 20 Gruppebehandlingen er avsluttet, men avslutningsdato mangler"
                            , "Det er krysset av for at gruppebehandlingen er avsluttet i rapporteringsåret, "
                            + "men ikke fylt ut dato (" + r.getFieldAsString("DATO_GRAVSLUTN_B") + ") for avslutning av saken."
                            , Constants.NORMAL_ERROR
                    )
                    , "STATUS_ARETSSL_B"
                    , List.of("2")
                    , "DATO_GRAVSLUTN_B"
            );

            ControlFelt1DatoSaaFelt2Dato.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            createKontorNr(r.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(r.getFieldAsString("GRUPPE_NR_B"), r.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(r.getLine()))
                            , String.valueOf(r.getLine())
                            , " "
                            , "Kontroll 21 Avslutningsdato før første samtale"
                            , "Dato for avslutting av gruppebehandlingen (" + r.getFieldAsString("DATO_GRAVSLUTN_B") + ") "
                            + "kommer før dato for gruppebehandlingens start (" + r.getFieldAsString("DATO_GRSTART_B") + ")."
                            , Constants.NORMAL_ERROR
                    )
                    , "DATO_GRSTART_B"
                    , "DATO_GRAVSLUTN_B"
            );
        });

        return er;
    }
}
