package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.sosial.Definitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Toolkit;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        er.setReportHeaders(List.of("Saksbehandler", "Journalnummer", "Kontroll", "Feilmelding"));
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
        final String lf = Constants.lineSeparator;
        Integer n = records.size();
        int l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, er);

//        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
//            return er;
//        }

        records.stream()
                // utled ALDER
                .map(r -> {
                    try {
                        r.setFieldAsInteger("ALDER", Toolkit.getAlderFromFnr(r.getFieldAsString("PERSON_FODSELSNR")));
                    } catch (Exception e) {
                        r.setFieldAsInteger("ALDER", -1);
                    }

                    return r;
                })
                .forEach(r -> {
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 03 kommunenummer"
                                    , "Korrigér kommunenummeret. Fant '" + r.getFieldAsTrimmedString("KOMMUNE_NR") + "', "
                                    + "forventet '" + args.getRegion().substring(0, 4) + "'."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "KOMMUNE_NR"
                            , Collections.singletonList(args.getRegion().substring(0, 4))
                    );

                    if (r.getFieldAsString("KOMMUNE_NR").equalsIgnoreCase("0301")) {
                        ControlFelt1InneholderKodeFraKodeliste.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 03 Bydelsnummer"
                                        , "Korrigér bydel. Fant '" + r.getFieldAsTrimmedString("BYDELSNR") + "', "
                                        + "forventet én av '" + Definitions.getBydelerAsList() + "'."
                                        , Constants.CRITICAL_ERROR
                                )
                                , "BYDELSNR"
                                , Definitions.getBydelerAsList()
                        );
                    }

                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 04 Årgang"
                                    , "Korrigér årgang. Fant '" + r.getFieldAsTrimmedString("VERSION") + "', "
                                    + "forventet '" + args.getAargang().substring(0, 2) + "'."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "VERSION"
                            , Collections.singletonList(args.getAargang().substring(0, 2))
                    );

                    ControlFodselsnummer.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 05 Fødselsnummer"
                                    , "Det er ikke oppgitt fødselsnummer/d-nummer på deltakeren eller fødselsnummeret/d-nummeret inneholder feil. "
                                    + "Med mindre det er snakk om en utenlandsk statsborger som ikke er tildelt norsk personnummer eller d-nummer, "
                                    + "skal feltet inneholde deltakeren fødselsnummer/d-nummer (11 siffer)."
                                    , Constants.NORMAL_ERROR
                            )
                            , "PERSON_FODSELSNR"
                    );


                    ControlFelt1Boolsk.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 06 Alder under 18 år"
                                    , "Deltakeren (" + r.getFieldAsTrimmedString("ALDER") + " år) er under 18 år."
                                    , Constants.NORMAL_ERROR
                            )
                            , "ALDER"
                            , ">="
                            , 18
                    );

                    ControlFelt1Boolsk.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 07 Alder er 96 år eller over"
                                    , "Deltakeren (" + r.getFieldAsTrimmedString("ALDER") + " år) er 96 år eller eldre."
                                    , Constants.NORMAL_ERROR
                            )
                            , "ALDER"
                            , "<="
                            , 96
                    );

                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 08 Kjønn"
                                    , "Korrigér kjønn. Fant '" + r.getFieldAsTrimmedString("KJONN") + "', "
                                    + "forventet én av '" + r.getFieldDefinitionByName("KJONN").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + "'. "
                                    + "Mottakerens kjønn er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "KJONN"
                            , r.getFieldDefinitionByName("KJONN").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 09 Sivilstand"
                                    , "Korrigér sivilstand. Fant '" + r.getFieldAsTrimmedString("EKTSTAT") + "', "
                                    + "forventet én av '" + r.getFieldDefinitionByName("EKTSTAT").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + "'. "
                                    + "Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                    , Constants.NORMAL_ERROR
                            )
                            , "EKTSTAT"
                            , r.getFieldDefinitionByName("EKTSTAT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 10 Forsørgerplikt for barn under 18 år i husholdningen. Gyldige verdier"
                                    , "Korrigér forsørgerplikt. Fant '" + r.getFieldAsTrimmedString("BU18") + "', "
                                    + "forventet én av " + r.getFieldDefinitionByName("BU18").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + "'. "
                                    + "Det er ikke krysset av for om deltakeren har barn under 18 år, "
                                    + "som deltakeren (eventuelt ektefelle/samboer) har forsørgerplikt for, og som bor i husholdningen ved siste kontakt. Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "BU18"
                            , r.getFieldDefinitionByName("BU18").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 11 Det bor barn under 18 år i husholdningen. Mangler antall barn."
                                    , "Det er krysset av for at det bor barn under 18 år i husholdningen som mottaker eller ektefelle/samboer har forsørgerplikt for, "
                                    + "men det er ikke oppgitt hvor mange barn '(" + r.getFieldAsInteger("ANTBU18") + ")' som bor i husholdningen. "
                                    + "Feltet er obligatorisk å fylle ut når det er oppgitt at det bor barn under 18 år i husholdningen."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "BU18"
                            , List.of("1")
                            , "ANTBU18"
                            , ">"
                            , 0
                    );

                    ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 12 Det bor barn under 18 år i husholdningen."
                                    , "Det er oppgitt " + r.getFieldAsInteger("ANTBU18") + " barn under 18 år som bor i husholdningen som "
                                    + "mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke "
                                    + "oppgitt at det bor barn i husholdningen. "
                                    + "Feltet er obligatorisk å fylle ut når det er oppgitt antall barn under 18 år som bor i husholdningen."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "ANTBU18"
                            , ">"
                            , 0
                            , "BU18"
                            , List.of("1")
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
                                    , "Antall barn ('" + r.getFieldAsTrimmedString("ANTBU18") + "') under 18 år i husholdningen er 10 eller flere, er dette riktig?"
                                    , Constants.NORMAL_ERROR
                            )
                            , "ANTBU18"
                            , "<="
                            , 10
                    );

                    ControlFelt1Dato.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 14 Dato for registrert søknad ved NAV-kontoret."
                                    , "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' mangler utfylling eller har ugyldig dato (" + r.getFieldAsString("REG_DATO") + "). Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "REG_DATO"
                    );

                    ControlFelt1Dato.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 15 Dato for fattet vedtak om program (søknad innvilget)"
                                    , "Feltet for 'Hvilken dato det ble fattet vedtak om program (søknad innvilget)' mangler utfylling eller har ugyldig dato (" + r.getFieldAsString("VEDTAK_DATO") + "). Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "VEDTAK_DATO"
                    );

                    ControlFelt1Dato.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 16 Dato for når deltakeren begynte i program (iverksettelse)."
                                    , "Feltet for 'Hvilken dato begynte deltakeren i program? (iverksettelse)' mangler utfylling eller har ugyldig dato (" + r.getFieldAsString("BEGYNT_DATO") + "). Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "BEGYNT_DATO"
                    );

                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 19 Kvalifiseringsprogram i annen kommune"
                                    , "Feltet for 'Kommer deltakeren fra kvalifiseringsprogram i annen kommune?' er ikke fylt ut, eller feil kode er benyttet (" + r.getFieldAsString("KVP_KOMM") + "). Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "KVP_KOMM"
                            , r.getFieldDefinitionByName("KVP_KOMM").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 20 Kvalifiseringsprogram i annen kommune. Kommunenummer."
                                    , "Deltakeren kommer fra kvalifiseringsprogram i annen kommune ('" + r.getFieldAsString("KOMMNR_KVP_KOMM") + "'), men kommunenummer mangler eller er ugyldig. Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "KVP_KOMM"
                            , List.of("1")
                            , "KOMMNR_KVP_KOMM"
                            , r.getFieldDefinitionByName("KOMMNR_KVP_KOMM").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 20a Fra kvalifiseringsprogram i annen bydel i Oslo."
                                    , "Manglende/ugyldig utfylling for om deltakeren kommer fra kvalifiseringsprogram i annen bydel (). Feltet er obligatorisk å fylle ut for Oslo."
                                    , Constants.NORMAL_ERROR
                            )
                            , "KOMMUNE_NR"
                            , List.of("0301")
                            , "KVP_OSLO"
                            , r.getFieldDefinitionByName("KVP_OSLO").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 21 Ytelser de to siste månedene før registrert søknad ved NAV-kontoret"
                                    , "Feltet for 'Hadde deltakeren i løpet av de siste to månedene før registrert søknad ved NAV-kontoret en eller flere av følgende ytelser?' inneholder ugyldige verdier."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "YTELSE_SOSHJELP"
                            , List.of("1")
                            , "YTELSE_TYPE_SOSHJ"
                            , r.getFieldDefinitionByName("YTELSE_TYPE_SOSHJ").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 26 Mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " + args.getAargang()
                                    , "Feltet for 'Har deltakeren i " + args.getAargang() + " i løpet av perioden med kvalifiseringsstønad også mottatt  økonomisk sosialhjelp, "
                                    + "kommunal bostøtte eller Husbankens bostøtte?', er ikke utfylt eller feil kode (" + r.getFieldAsString("KVP_MED_ASTONAD") + ") er benyttet. Feltet er obligatorisk å fylle ut."
                                    + r.getFieldDefinitionByName("KVP_MED_ASTONAD").getCodeList().stream().collect(Collectors.toMap(Code::getCode, Code::getValue))
                                    , Constants.CRITICAL_ERROR
                            )
                            , "KVP_MED_ASTONAD"
                            , r.getFieldDefinitionByName("KVP_MED_ASTONAD").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    // Kontroll 27 sjekker at flere felt skal være utfylt hvis KVP_MED_ASTONAD = 1 (Ja)
                    // Disse feltene sjekkes hver for seg med en ferdig control.
                    // Derfor gjentas kontroll 27 for hver av dem
                    {
                        List<String> fields = List.of("KVP_MED_KOMMBOS", "KVP_MED_HUSBANK", "KVP_MED_SOSHJ_ENGANG", "KVP_MED_SOSHJ_PGM", "KVP_MED_SOSHJ_SUP");

                        Map<String, String> values = fields.stream()
                                .collect(Collectors.toMap(s -> s, r::getFieldAsString));

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
                                                + "kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. Det er mottatt støtte. " + values
                                                , Constants.CRITICAL_ERROR
                                        )
                                );
                            }

                        } else if (Objects.equals(r.getFieldAsString("KVP_MED_ASTONAD"), "2")) {
                            boolean isAllBlank = fields.stream()
                                    .allMatch(field -> Comparator.isCodeInCodelist(r.getFieldAsString(field), List.of(" ", "0")));

                            if (!isAllBlank) {
                                er.addEntry(
                                        new ErrorReportEntry(
                                                r.getFieldAsString("SAKSBEHANDLER")
                                                , r.getFieldAsString("PERSON_JOURNALNR")
                                                , r.getFieldAsString("PERSON_FODSELSNR")
                                                , " "
                                                , "Kontroll 27 Ikke mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " + args.getAargang() + ". Svaralternativer."
                                                , "Svaralternativer for feltet \"Har deltakeren i " + args.getAargang() + " i løpet av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, "
                                                + "kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. Det er IKKE mottatt støtte. " + values
                                                , Constants.CRITICAL_ERROR
                                        )
                                );
                            }
                        }
                    }

                    // Kontrollene 28-33 sjekker at koblingen mellom én av flere stønadsmåneder (som skal være utfylt) og stønadssumfelt
                    {
                        Integer stonad = r.getFieldAsInteger("KVP_STONAD");
                        boolean stonadOK = (stonad != null);
                        List<String> fields = List.of("STMND_1", "STMND_2", "STMND_3", "STMND_4", "STMND_5", "STMND_6", "STMND_7", "STMND_8", "STMND_9", "STMND_10", "STMND_11", "STMND_12");
                        boolean harVarighet = fields.stream()
                                .anyMatch(field -> r.getFieldDefinitionByName(field)
                                        .getCodeList()
                                        .stream()
                                        .map(Code::getCode)
                                        .collect(Collectors.toList())
                                        .contains(r.getFieldAsString(field)));

                        int stonadSumMax = 235000;
                        int stonadSumMin = 8000;

                        if (!harVarighet) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            r.getFieldAsString("SAKSBEHANDLER")
                                            , r.getFieldAsString("PERSON_JOURNALNR")
                                            , r.getFieldAsString("PERSON_FODSELSNR")
                                            , " "
                                            , "Kontroll 28 Måneder med kvalifiseringsstønad. Gyldige koder."
                                            , "Det er ikke krysset av for hvilke måneder deltakeren har fått utbetalt kvalifiseringsstønad (" + r.getFieldAsString("KVP_STONAD") + ") i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
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
                                            , "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad (" + r.getFieldAsString("KVP_STONAD") + ") i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                                            , Constants.NORMAL_ERROR
                                    )
                            );
                        }

                        if (harVarighet) {
                            if (!stonadOK) {
                                er.addEntry(
                                        new ErrorReportEntry(
                                                r.getFieldAsString("SAKSBEHANDLER")
                                                , r.getFieldAsString("PERSON_JOURNALNR")
                                                , r.getFieldAsString("PERSON_FODSELSNR")
                                                , " "
                                                , "Kontroll 30 Har varighet, men mangler kvalifiseringssum."
                                                , "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad (" + r.getFieldAsString("KVP_STONAD") + ") i løpet av året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                                                , Constants.NORMAL_ERROR
                                        )
                                );
                            }
                        }

                        if (stonadOK && 0 < stonad) {
                            if (!harVarighet) {
                                er.addEntry(
                                        new ErrorReportEntry(
                                                r.getFieldAsString("SAKSBEHANDLER")
                                                , r.getFieldAsString("PERSON_JOURNALNR")
                                                , r.getFieldAsString("PERSON_FODSELSNR")
                                                , " "
                                                , "Kontroll 31 Har kvalifiseringssum, men mangler varighet."
                                                , "Deltakeren har fått kvalifiseringsstønad (" + r.getFieldAsString("KVP_STONAD") + ") i løpet av året, men mangler utfylling for hvilke måneder stønaden gjelder. Feltet er obligatorisk å fylle ut."
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
                                            , "Kvalifiseringsstønaden (" + r.getFieldAsString("KVP_STONAD") + ") som deltakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMax + ",-."
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
                                            , "Kvalifiseringsstønaden (" + r.getFieldAsString("KVP_STONAD") + ") som deltakeren har fått i løpet av rapporteringsåret er lavere enn Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMin + ",-."
                                            , Constants.NORMAL_ERROR
                                    )
                            );
                        }
                    }

                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 36 Status for deltakelse i kvalifiseringsprogram per 31.12." + args.getAargang() + ". Gyldige verdier."
                                    , "Korrigér status. Fant '" + r.getFieldAsTrimmedString("STATUS") + "', forventet én av '"
                                    + r.getFieldDefinitionByName("STATUS").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + "'. "
                                    + "Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "STATUS"
                            , r.getFieldDefinitionByName("STATUS").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );


                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 37 Dato for avsluttet program (gjelder fullførte, avsluttede etter avtale og varig avbrutte program, ikke for permisjoner) (DDMMÅÅ)."
                                    , "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', fant (" + r.getFieldAsString("AVSL_DATO") + "), må fylles ut dersom det er krysset av for svaralternativ "
                                    + r.getFieldDefinitionByName("STATUS").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("3", "4", "5"))).map(Code::toString).collect(Collectors.toList())
                                    + " under feltet for 'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12." + args.getAargang() + "'?"
                                    , Constants.CRITICAL_ERROR
                            )
                            , "STATUS"
                            , r.getFieldDefinitionByName("STATUS").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("3", "4", "5"))).map(Code::getCode).collect(Collectors.toList())
                            , "AVSL_DATO"
                    );

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
                                            , "Feltet 'Ved fullført program eller program avsluttet etter avtale (gjelder ikke flytting) – hva var deltakerens viktigste livssituasjon umiddelbart etter avslutningen'? "
                                            + "Må fylles ut dersom det er krysset av for svaralternativ 3 = Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke flytting) under feltet for "
                                            + "'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12." + args.getAargang() + "'?"
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }
                    }
                });


        return er;
    }
}
