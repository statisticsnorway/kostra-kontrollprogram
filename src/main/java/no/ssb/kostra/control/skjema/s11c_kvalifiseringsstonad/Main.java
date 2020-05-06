package no.ssb.kostra.control.skjema.s11c_kvalifiseringsstonad;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.Collections;
import java.util.List;
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
                .peek(r -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 3 kommunenummer"
                                , "Korrigér kommunenummeret. Forventet '"
                                + args.getRegion().substring(1, 4)
                                + "', men fant "
                                + r.getFieldAsTrimmedString("KOMMUNE_NR")
                                , Constants.CRITICAL_ERROR
                        )
                        , "KOMMUNE_NR"
                        , Collections.singletonList(args.getRegion().substring(1, 4))
                ))
                .peek(r -> {
                    if (r.getFieldAsString("KOMMUNE_NR").equalsIgnoreCase("0301")) {
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 4 Bydelsnummer"
                                        , "Korrigér bydel. Forventet én av  '"
                                        + Definitions.getBydelerAsList()
                                        + "', men fant "
                                        + r.getFieldAsTrimmedString("BYDELSNR")
                                        , Constants.CRITICAL_ERROR
                                )
                                , "BYDELSNR"
                                , Definitions.getBydelerAsList()

                        );
                    }
                })
                .peek(r -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 5 Årgang"
                                , "Korrigér årgang. Forventet '"
                                + args.getAargang()
                                + "', men fant "
                                + r.getFieldAsTrimmedString("VERSION")
                                , Constants.CRITICAL_ERROR
                        )
                        , "VERSION"
                        , Collections.singletonList(args.getAargang())
                ))
                .peek(r -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 5 Årgang"
                                , "Korrigér årgang. Forventet '"
                                + args.getAargang()
                                + "', men fant "
                                + r.getFieldAsTrimmedString("VERSION")
                                , Constants.CRITICAL_ERROR
                        )
                        , "VERSION"
                        , Collections.singletonList(args.getAargang())
                ))
                .peek(r -> ControlFodselsnummer.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 6 Fødselsnummer"
                                , "Det er ikke oppgitt fødselsnummer/d-nummer på "
                                + "deltakeren" + lf + "\teller fødselsnummeret/d-nummeret inneholder" +
                                " feil." + lf + "\tMed mindre det er snakk om en utenlandsk " +
                                "statsborger som ikke er" + lf + "\ttildelt norsk personnummer eller d-nummer, " +
                                "skal feltet inneholde" + lf + "\tdeltakeren fødselsnummer/d-nummer (11 siffer)."
                                , Constants.NORMAL_ERROR
                        )
                        , "PERSON_FODSELSNR"
                ))
                .peek(r -> ControlAlderFraFodselsnummer.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 7 Alder under 18 år"
                                , "Deltaker er under 18 år."
                                , Constants.NORMAL_ERROR
                        )
                        , "PERSON_FODSELSNR"
                        , "<"
                        , 18
                ))
                .peek(r -> ControlAlderFraFodselsnummer.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 8 Alder er 96 år eller over"
                                , "Deltakeren er 96 år eller eldre."
                                , Constants.NORMAL_ERROR
                        )
                        , "PERSON_FODSELSNR"
                        , ">"
                        , 95
                ))
                .peek(r -> {
                    List<String> listCodes = r.getFieldDefinitionByName("KJONN").getCodeList().stream().map(Code::getCode).collect(Collectors.toList());
                    List<String> listTexts = r.getFieldDefinitionByName("KJONN").getCodeList().stream().map(Code::toString).collect(Collectors.toList());
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 9 Kjønn"
                                    , "Korrigér kjønn. Forventet én av  '"
                                    + listTexts
                                    + "', men fant "
                                    + r.getFieldAsTrimmedString("KJONN")
                                    , Constants.CRITICAL_ERROR
                            )
                            , "KJONN"
                            , listCodes
                    );
                })
                .peek(r -> {
                    List<String> listCodes = r.getFieldDefinitionByName("EKTSTAT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList());
                    List<String> listTexts = r.getFieldDefinitionByName("EKTSTAT").getCodeList().stream().map(Code::toString).collect(Collectors.toList());
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 10 Sivilstand"
                                    , "Korrigér sivilstand. Forventet én av  '"
                                    + listTexts
                                    + "', men fant "
                                    + r.getFieldAsTrimmedString("EKTSTAT")
                                    , Constants.NORMAL_ERROR
                            )
                            , "EKTSTAT"
                            , listCodes
                    );
                })
                .peek(r -> {
                    List<String> listCodes = r.getFieldDefinitionByName("BU").getCodeList().stream().map(Code::getCode).collect(Collectors.toList());
                    List<String> listTexts = r.getFieldDefinitionByName("BU").getCodeList().stream().map(Code::toString).collect(Collectors.toList());
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 11 Forsørgerplikt for barn under 18 år i husholdningen. Gyldige verdier"
                                    , "Det er ikke krysset av for om deltakeren har barn under 18 år, " + lf +
                                    "som deltakeren (eventuelt ektefelle/samboer) har \n" +
                                    "forsørgerplikt for," + lf + "\tog som bor i husholdningen ved\n" +
                                    "siste kontakt. Feltet er obligatorisk å fylle ut."
                                    + listTexts
                                    + "', men fant "
                                    + r.getFieldAsTrimmedString("BU")
                                    , Constants.CRITICAL_ERROR
                            )
                            , "BU"
                            , listCodes
                    );
                })
                .peek(r -> ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 12 Det bor barn under 18 år i husholdningen. Mangler antall barn."
                                , "Deltakeren har barn under 18 år, som deltakeren " + lf +
                                "\t(eventuelt ektefelle/samboer) har forsørgerplikt for, " +
                                "og som bor i husholdningen" + lf + "\tved siste kontakt, men det er " +
                                "ikke oppgitt hvor mange barn som bor i husholdningen. " + lf +
                                "\tFeltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "BU"
                        , List.of("1")
                        , "ANTBU18"
                        , "<"
                        , 0
                ))
                .peek(r -> ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 13 Det bor barn under 18 år i husholdningen."
                                , "Deltakeren har barn under 18 år, som deltakeren " + lf +
                                "\t(eventuelt ektefelle/samboer) har forsørgerplikt for, " +
                                "og som bor i husholdningen" + lf + "\tved siste kontakt, men det er " +
                                "ikke oppgitt hvor mange barn som bor i husholdningen. " + lf +
                                "\tFeltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "ANTBU18"
                        , "<"
                        , 0
                        , "BU"
                        , List.of("1")
                ))
                .peek(r -> ControlFelt1Dato.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 15 Dato for registrert søknad ved NAV-kontoret."
                                , "Feltet for Hvilken dato ble søknaden registrert ved NAV-kontoret? mangler utfylling eller har ugyldig dato. Feltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "REG_DATO"
                ))
                .peek(r -> ControlFelt1Dato.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 16 Dato for fattet vedtak om program (søknad innvilget)"
                                , "Feltet for Hvilken dato det ble fattet vedtak om program (søknad innvilget) mangler utfylling eller har ugyldig dato. Feltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "VEDTAK_DATO"
                ))
                .peek(r -> ControlFelt1Dato.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 17 Dato for når deltakeren begynte i program (iverksettelse)."
                                , "Feltet for Hvilken dato begynte deltakeren i program? (iverksettelse) mangler utfylling eller har ugyldig dato. Feltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "BEGYNT_DATO"
                ))
                .peek(r -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 19 Kvalifiseringsprogram i annen kommune"
                                , "Feltet for Kommer deltakeren fra kvalifiseringsprogram i annen kommune? er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "KVP_KOMM"
                        , r.getFieldDefinitionByName("KVP_KOMM").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                ))
                .peek(r -> ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 20 Kvalifiseringsprogram i annen kommune. Kommunenummer."
                                , "Deltakeren kommer fra kvalifiseringsprogram i annen kommune, men kommunenummer mangler eller er ugyldig. Feltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "KVP_KOMM"
                        , List.of("1")
                        , "KOMMNR_KVP_KOMM"
                        , r.getFieldDefinitionByName("KOMMNR_KVP_KOMM").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                ))
                .peek(r -> ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 20a Fra kvalifiseringsprogram i annen bydel i Oslo."
                                , "Deltakeren kommer fra kvalifiseringsprogram i annen kommune, men kommunenummer mangler eller er ugyldig. Feltet er obligatorisk å fylle ut."
                                , Constants.NORMAL_ERROR
                        )
                        , "KOMMUNE_NR"
                        , List.of("0301")
                        , "KVP_OSLO"
                        , r.getFieldDefinitionByName("KVP_OSLO").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                ))
                .peek(r -> {
                    boolean lineHasError = false;

                    if (r.getFieldAsString("YTELSE_SOSHJELP").equalsIgnoreCase("1")) {
                        lineHasError = !List.of("2", "3").contains(r.getFieldAsString("YTELSE_TYPE_SOSHJ"));


//      lineHasError =
//          !(field_15_2.equalsIgnoreCase("2") || field_15_2.equalsIgnoreCase("3"));
//
//      field = RecordFields.getFieldValue(line, 153);
//      lineHasError |=  !(field.equalsIgnoreCase("0") || field.equalsIgnoreCase(" ") || field.equalsIgnoreCase("4"));
//
//      field = RecordFields.getFieldValue(line, 154);
//      lineHasError |=  !(field.equalsIgnoreCase("0") || field.equalsIgnoreCase(" ") || field.equalsIgnoreCase("5"));
//
//      field = RecordFields.getFieldValue(line, 155);
//      lineHasError |=  !(field.equalsIgnoreCase("0") || field.equalsIgnoreCase(" ") || field.equalsIgnoreCase("6"));
    }


                    if (lineHasError) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 21 Ytelser de to siste månedene før registrert søknad ved NAV-kontoret.\""
                                        , "Feltet for " +
                                        "\"Hadde deltakeren i løpet av de siste to månedene før " + lf +
                                        "\tregistrert søknad ved NAV-kontoret en eller flere av " +
                                        "følgende ytelser?\"" + lf + "\tinneholder ugyldige verdier."
                                        , Constants.NORMAL_ERROR
                                )
                        );
                    }

                }
                )

                .close();



        return er;
    }
}
