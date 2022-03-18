package no.ssb.kostra.control.famvern.s52b;

import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
public class Main {
    private static String createKontorNr(String kontornr) {
        return "Kontornummer ".concat(kontornr);
    }

    private static String createJournalNr(
            final String gruppenr, final String gruppenavn, final String linjenr) {
        return "Gruppenr "
                .concat(gruppenr).concat(" ")
                .concat(gruppenavn)
                .concat(" / Linjenummer ")
                .concat(linjenr);
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
        final var records = inputFileContent.stream()
                .map(p -> new KostraRecord(p, fieldDefinitions))
                .toList();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, errorReport);

        records.forEach(r -> {
            // Kontroll 3: Regionsnummer
            if (!Definitions.isRegionValid(r.getFieldAsString("REGION_NR_B"))) {
                errorReport.addEntry(
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
                errorReport.addEntry(
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
                errorReport.addEntry(
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
        final var dubletter = records.stream()
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
                .toList();

        if (!dubletter.isEmpty()) {
            errorReport.addEntry(
                    new ErrorReportEntry("Filuttrekk", "Dubletter", " ", " "
                            , "Kontroll 06 Dubletter"
                            , "Gruppenummeret er benyttet på mer enn en sak. Dubletter på kontornummer - gruppenummer. (Gjelder for:<br/>\n" + String.join(",<br/>\n", dubletter) + ")"
                            , Constants.NORMAL_ERROR
                    ));
        }

        records.forEach(currentRecord -> {
            ControlFelt1Lengde.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 07 Gruppenavn"
                            , "Det er ikke oppgitt navn på gruppen. Tekstfeltet skal ha maksimalt 30 posisjoner."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsTrimmedString("GRUPPE_NAVN_B")
            );

            ControlFelt1Dato.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 08 Start dato"
                            , "Det er ikke oppgitt dato for gruppebehandlingens start, eller feltet har ugyldig format."
                            + "Fant '" + currentRecord.getFieldAsString("DATO_GRSTART_B") + "'."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsString("DATO_GRSTART_B")
                    , currentRecord.getFieldDefinitionByName("DATO_GRSTART_B").getDatePattern()
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 09 Målgruppe"
                            , "Det er ikke fylt ut hva som er målgruppe for behandlingen. "
                            + "Fant '" + currentRecord.getFieldAsString("STRUKTUR_GR_B") + "', "
                            + "forventet én av: " + currentRecord.getFieldDefinitionByName("STRUKTUR_GR_B").getCodeList().stream().map(Code::toString).toList() + " ). "

                            , Constants.NORMAL_ERROR
                    ),
                    currentRecord.getFieldAsString("STRUKTUR_GR_B"),
                    currentRecord.getFieldDefinitionByName("STRUKTUR_GR_B").getCodeList().stream().map(Code::getCode).toList());

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 10 Gruppens hovedtema"
                            , "Det er ikke fylt ut hva som er hovedtema for gruppen."
                            + "Fant '" + currentRecord.getFieldAsString("HOVEDI_GR_B") + "', "
                            + "forventet én av: " + currentRecord.getFieldDefinitionByName("HOVEDI_GR_B").getCodeList().stream().map(Code::toString).toList() + " ). "
                            , Constants.NORMAL_ERROR
                    ),
                    currentRecord.getFieldAsString("HOVEDI_GR_B"),
                    currentRecord.getFieldDefinitionByName("HOVEDI_GR_B").getCodeList().stream().map(Code::getCode).toList());

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 11 Antall gruppemøter gjennomført totalt i løpet av året"
                            , "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt i løpet av rapporteringsåret."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("ANTMOTERTOT_IARET_B")
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 12 Antall gruppemøter gjennomført siden opprettelsen"
                            , "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt siden gruppen ble opprettet."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("ANTMOTERTOT_OPPR_B")
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 13 Antall timer anvendt i gruppen totalt i løpet av året"
                            , "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen i løpet av året. (For og etterarbeid skal ikke regnes med)."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("TIMERTOT_IARET_B")
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 14 Antall timer anvendt i gruppen totalt siden opprettelsen"
                            , "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen siden opprettelsen "
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("TIMERTOT_OPPR_B")
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 15 Antall deltagere i gruppen i løpet av året"
                            , "Det er ikke fylt ut hvor mange som har deltatt i gruppen i løpet av året. Terapeuter holdes utenom."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("ANTDELT_IARET_B")
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 16 Antall deltagere i gruppen siden opprettelsen"
                            , "Det er ikke fylt ut hvor mange som har deltatt i gruppen siden opprettelsen. Terapeuter holdes utenom"
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("ANTDELT_OPPR_B")
                    , ">"
                    , 0

            );

            ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 17 Antall terapeuter involvert i gruppebehandlingen"
                            , "Det er ikke oppgitt hvor mange hovedterapeut eller andre ansatte som har deltatt i gruppen."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsIntegerDefaultEquals0("ANTTER_GRUPPEB_B")
                    , ">"
                    , 0

            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 18 Er det benyttet tolk i minst én gruppesamtale?"
                            , "Kontroller at feltet er utfylt. "
                            + "Fant '" + currentRecord.getFieldAsString("TOLK_B") + "', "
                            + "forventet én av: " + currentRecord.getFieldDefinitionByName("TOLK_B").getCodeList().stream().map(Code::toString).toList() + " ). "
                            , Constants.NORMAL_ERROR
                    ),
                    currentRecord.getFieldAsString("TOLK_B"),
                    currentRecord.getFieldDefinitionByName("TOLK_B").getCodeList().stream().map(Code::getCode).toList());

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 19 Status ved året slutt"
                            , "Det er ikke fylt ut hva som er gruppens status ved utgangen av året."
                            + "Fant '" + currentRecord.getFieldAsString("STATUS_ARETSSL_B") + "', "
                            + "forventet én av: " + currentRecord.getFieldDefinitionByName("STATUS_ARETSSL_B").getCodeList().stream().map(Code::toString).toList() + " ). "
                            , Constants.NORMAL_ERROR
                    ),
                    currentRecord.getFieldAsString("STATUS_ARETSSL_B"),
                    currentRecord.getFieldDefinitionByName("STATUS_ARETSSL_B").getCodeList().stream().map(Code::getCode).toList());

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 20 Gruppebehandlingen er avsluttet, men avslutningsdato mangler"
                            , "Det er krysset av for at gruppebehandlingen er avsluttet i rapporteringsåret, "
                            + "men ikke fylt ut dato (" + currentRecord.getFieldAsString("DATO_GRAVSLUTN_B") + ") for avslutning av saken."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsString("STATUS_ARETSSL_B")
                    , List.of("2")
                    , currentRecord.getFieldAsLocalDate("DATO_GRAVSLUTN_B")
            );

            ControlFelt1DatoSaaFelt2Dato.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            createKontorNr(currentRecord.getFieldAsString("KONTOR_NR_B"))
                            , createJournalNr(currentRecord.getFieldAsString("GRUPPE_NR_B"), currentRecord.getFieldAsString("GRUPPE_NAVN_B"), String.valueOf(currentRecord.getLine()))
                            , String.valueOf(currentRecord.getLine())
                            , " "
                            , "Kontroll 21 Avslutningsdato før første samtale"
                            , "Dato for avslutting av gruppebehandlingen (" + currentRecord.getFieldAsString("DATO_GRAVSLUTN_B") + ") "
                            + "kommer før dato for gruppebehandlingens start (" + currentRecord.getFieldAsString("DATO_GRSTART_B") + ")."
                            , Constants.NORMAL_ERROR
                    )
                    , currentRecord.getFieldAsString("DATO_GRSTART_B")
                    , currentRecord.getFieldDefinitionByName("DATO_GRSTART_B").getDatePattern()
                    , currentRecord.getFieldAsString("DATO_GRAVSLUTN_B")
                    , currentRecord.getFieldDefinitionByName("DATO_GRAVSLUTN_B").getDatePattern()
            );
        });

        return errorReport;
    }
}
