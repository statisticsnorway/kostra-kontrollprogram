package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.sosial.Definitions;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
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
                    );

                    ControlFelt1Boolsk.doControl(
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
                                    , Constants.NORMAL_ERROR
                            )
                            , "ANTBU18"
                            , ">"
                            , 10
                    );
                })
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
                .peek(r -> ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 21 Ytelser de to siste månedene før registrert søknad ved NAV-kontoret"
                                , "Feltet for \"Hadde deltakeren i løpet av de siste to månedene før registrert søknad ved NAV-kontoret en eller flere av følgende ytelser?\" inneholder ugyldige verdier."
                                , Constants.CRITICAL_ERROR
                        )
                        , "YTELSE_SOSHJELP"
                        , List.of("1")
                        , "YTELSE_TYPE_SOSHJ"
                        , r.getFieldDefinitionByName("YTELSE_TYPE_SOSHJ").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                ))
                .peek(r -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 26 Mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " + args.getAargang()
                                , "Feltet for \"Har deltakeren i " + args.getAargang() + " i løpet av perioden med kvalifiseringsstønad også mottatt  økonomisk sosialhjelp, "
                                + "kommunal bostøtte eller Husbankens bostøtte?\", er ikke utfylt eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "KVP_MED_ASTONAD"
                        , r.getFieldDefinitionByName("KVP_MED_ASTONAD").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                ))
                // Kontroll 27 sjekker at flere felt skal være utfylt hvis KVP_MED_ASTONAD = 1 (Ja)
                // Disse feltene sjekkes hver for seg med en ferdig control.
                // Derfor gjentas kontroll 27 for hver av dem
                .peek(r -> {
                    List<String> fields = List.of("KVP_MED_KOMMBOS", "KVP_MED_HUSBANK", "KVP_MED_SOSHJ_ENGANG", "KVP_MED_SOSHJ_PGM", "KVP_MED_SOSHJ_SUP");

                    if (Objects.equals(r.getFieldAsString("KVP_MED_ASTONAD"), "1")) {
                        boolean isNoneFilledIn = fields.stream()
                                .noneMatch(field -> r.getFieldDefinitionByName(field).getCodeList().stream().map(Code::getCode).collect(Collectors.toList()).contains(r.getFieldAsString(field)));

                        if (isNoneFilledIn) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            r.getFieldAsString("SAKSBEHANDLER")
                                            , r.getFieldAsString("PERSON_JOURNALNR")
                                            , r.getFieldAsString("PERSON_FODSELSNR")
                                            , " "
                                            , "Kontroll 27 Mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " + args.getAargang() + ". Svaralternativer."
                                            , "Svaralternativer for feltet \"Har deltakeren i " + args.getAargang() + " i løpet av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, "
                                            + "kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. "
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }

                    } else {
                        boolean isNoneBlank = fields.stream()
                                .anyMatch(field -> List.of(" ", "0").contains(r.getFieldAsString(field)));

                        if (isNoneBlank) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            r.getFieldAsString("SAKSBEHANDLER")
                                            , r.getFieldAsString("PERSON_JOURNALNR")
                                            , r.getFieldAsString("PERSON_FODSELSNR")
                                            , " "
                                            , "Kontroll 27 Mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " + args.getAargang() + ". Svaralternativer."
                                            , "Svaralternativer for feltet \"Har deltakeren i " + args.getAargang() + " i løpet av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, "
                                            + "kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. "
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }
                    }
                })
                // Kontrollene 28-33 sjekker at koblingen mellom én av flere stønadsmåneder (som skal være utfylt) og stønadssumfelt
                .peek(r -> {
                    Integer stonad = r.getFieldAsInteger("KVP_STONAD");
                    boolean stonadOK = (stonad != null);
                    List<String> fields = List.of("STMND_1", "STMND_2", "STMND_3", "STMND_4", "STMND_5", "STMND_6", "STMND_7", "STMND_8", "STMND_9", "STMND_10", "STMND_11", "STMND_12");
                    boolean isAnyFilledIn = fields.stream()
                            .anyMatch(field -> r.getFieldDefinitionByName(field).getCodeList().stream().map(Code::getCode).collect(Collectors.toList()).contains(r.getFieldAsString(field)));

                    int stonadSumMax = 235000;
                    int stonadSumMin = 8000;

                    if (!isAnyFilledIn) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 28 Måneder med kvalifiseringsstønad. Gyldige koder."
                                        , "Det er ikke krysset av for hvilke måneder deltakeren har fått utbetalt kvalifiseringsstønad i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
                                        , Constants.CRITICAL_ERROR
                                )
                        );
                    }

                    if (!stonadOK) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 29 Kvalifiseringssum mangler eller har ugyldige tegn."
                                        , "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                                        , Constants.NORMAL_ERROR
                                )
                        );
                    }

                    if (isAnyFilledIn) {
                        if (!stonadOK || stonad == 0) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            r.getFieldAsString("SAKSBEHANDLER")
                                            , r.getFieldAsString("PERSON_JOURNALNR")
                                            , r.getFieldAsString("PERSON_FODSELSNR")
                                            , " "
                                            , "Kontroll 30 Har varighet men mangler kvalifiseringssum."
                                            , "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                                            , Constants.NORMAL_ERROR
                                    )
                            );
                        }
                    }

                    if (stonadOK && 0 < stonad) {
                        if (!isAnyFilledIn) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            r.getFieldAsString("SAKSBEHANDLER")
                                            , r.getFieldAsString("PERSON_JOURNALNR")
                                            , r.getFieldAsString("PERSON_FODSELSNR")
                                            , " "
                                            , "Kontroll 31 Har kvalifiseringssum men mangler varighet."
                                            , "Deltakeren har fått kvalifiseringsstønad i løpet av året, men mangler utfylling for hvilke måneder stønaden gjelder. Feltet er obligatorisk å fylle ut."
                                            , Constants.NORMAL_ERROR
                                    )
                            );
                        }
                    }

                    if (stonadOK && stonadSumMax < stonad) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 32 Kvalifiseringssum på kr " + stonadSumMax + ",- eller mer."
                                        , "Kvalifiseringsstønaden som deltakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMax + ",-."
                                        , Constants.NORMAL_ERROR
                                )
                        );
                    }

                    if (stonadOK && stonad < stonadSumMin) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 33 Kvalifiseringsstønad på kr " + stonadSumMin + ",- eller lavere."
                                        , "Kvalifiseringsstønaden som deltakeren har fått i løpet av rapporteringsåret er lavere enn Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMin + ",-."
                                        , Constants.NORMAL_ERROR
                                )
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
                                , "Kontroll 36 Status for deltakelse i kvalifiseringsprogram per 31.12." + args.getAargang() + ". Gyldige verdier."
                                , "Kontrollere at feltet er utfylt og ikke inneholder andre verdier enn de gyldige 1 – 6. Feltet er obligatorisk å fylle ut."
                                , Constants.CRITICAL_ERROR
                        )
                        , "STATUS"
                        , r.getFieldDefinitionByName("STATUS").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                ))

                .peek(r -> {
                    if (List.of("3", "4", "5").contains(r.getFieldAsString("STATUS"))) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 37 Dato for avsluttet program (gjelder fullførte, avsluttede etter avtale og varig avbrutte program, ikke for permisjoner) (DDMMÅÅ)."
                                        , "Feltet for \"Hvilken dato avsluttet deltakeren programmet?\" Må fylles ut dersom det er krysset av for svaralternativ"
                                        + "kode 3 = Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke flytting), "
                                        + "kode 4 = Deltakerens program er varig avbrutt på grunn av uteblivelse (gjelder ikke flytting) eller "
                                        + "kode 5 = Deltakerens program ble avbrutt på grunn av flytting til annen kommune under feltet for \"Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12." + args.getAargang() + "\"?"
                                        , Constants.CRITICAL_ERROR
                                )
                                , "STATUS"
                                , r.getFieldDefinitionByName("STATUS").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                                , "AVSL_DATO"
                        );
                    }
                })
                .peek(r -> {
                    if (r.getFieldAsString("STATUS").equalsIgnoreCase("3")) {
                        List<String> fields = List.of("AVSL_ORDINAERTARB", "AVSL_ARBLONNSTILS", "AVSL_TILRETTELARB", "AVSL_ARBMARK", "AVSL_SKOLE", "AVSL_AKTIVARBSOK", "AVSL_BEHANDL", "AVSL_AVKLAR_UFOR", "AVSL_INGEN_AKT", "AVSL_ANNET");
                        boolean isNoneFilledIn = fields.stream()
                                .noneMatch(field -> r.getFieldDefinitionByName(field).getCodeList().stream().map(Code::getCode).collect(Collectors.toList()).contains(r.getFieldAsString(field)));

                        if (isNoneFilledIn) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            r.getFieldAsString("SAKSBEHANDLER")
                                            , r.getFieldAsString("PERSON_JOURNALNR")
                                            , r.getFieldAsString("PERSON_FODSELSNR")
                                            , " "
                                            , "Kontroll 38 Fullførte/avsluttede program – til hvilken livssituasjon gikk deltakeren? Gyldige verdier."
                                            , "Feltet \"Ved fullført program eller program avsluttet etter avtale (gjelder ikke flytting) – hva var deltakerens viktigste livssituasjon umiddelbart etter avslutningen\"? "
                                            + "Må fylles ut dersom det er krysset av for svaralternativ 3 = Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke flytting) under feltet for "
                                            + "\"Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12." + args.getAargang() + "\"?"
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }
                    }
                })
                .close();


        return er;
    }
}
