package no.ssb.kostra.control.sosial.s11_sosialhjelp;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.sosial.Definitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Between;
import no.ssb.kostra.utils.Toolkit;

import java.util.Collections;
import java.util.List;
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
                // utled ALDER
                .map(r -> {
                    try {
                        r.setFieldAsInteger("ALDER", Toolkit.getAlderFromFnr(r.getFieldAsString("PERSON_FODSELSNR")));
                        r.setFieldAsInteger("FNR_OK", 1);
                    } catch (Exception e) {
                        r.setFieldAsInteger("ALDER", -1);
                        r.setFieldAsInteger("FNR_OK", 0);
                    }

                    return r;
                })
                .collect(Collectors.toList());
        final String lf = Constants.lineSeparator;
        Integer n = records.size();
        int l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, er);

//        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
//            return er;
//        }


        records.forEach(r -> {
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
                            + "forventet én av '" + Definitions.getBydelerAsList(args.getRegion().substring(0, 4)) + "'."
                            , Constants.CRITICAL_ERROR
                    )
                    , "BYDELSNR"
                    , Definitions.getBydelerAsList(args.getRegion().substring(0, 4))
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 04 Oppgaveår"
                            , "Korrigér årgang. Fant '" + r.getFieldAsTrimmedString("VERSION").substring(0, 2) + "', "
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
                    , "<"
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
                            , "Korrigér sivilstand. Fant '" + r.getFieldAsString("EKTSTAT") + "', "
                            + "forventet én av '" + r.getFieldDefinitionByName("EKTSTAT").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + "'. "
                            + "Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                            , Constants.CRITICAL_ERROR
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
                            , "Korrigér forsørgerplikt. Fant '" + r.getFieldAsString("BU18") + "', "
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
                            + "men det er ikke oppgitt hvor mange barn '(" + r.getFieldAsIntegerDefaultEquals0("ANTBU18") + ")' som bor i husholdningen. "
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
                            , "Kontroll 13 Mange barn under 18 år i husholdningen."
                            , "Antall barn (" + r.getFieldAsInteger("ANTBU18") + ") under 18 år i husholdningen er 10 eller flere, er dette riktig?"
                            , Constants.NORMAL_ERROR
                    )
                    , "ANTBU18"
                    , "<="
                    , 10
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 14 Viktigste kilde til livsopphold. Gyldige verdier"
                            , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret skal oppgis. Fant '" + r.getFieldAsString("VKLO") + "', forventet én av '"
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().map(Code::toString).collect(Collectors.toList()) + "'."
                            , Constants.CRITICAL_ERROR
                    )
                    , "VKLO"
                    , r.getFieldDefinitionByName("VKLO").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 15 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("1"))).map(Code::getValue).collect(Collectors.joining("")) + "."
                            , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("1"))).map(Code::getValue).collect(Collectors.joining("")) + ". "
                            + "Arbeidssituasjonen er '" + r.getFieldAsTrimmedString("ARBSIT") + "', forventet én av '"
                            + r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("01", "02"))).map(Code::toString).collect(Collectors.toList())
                            + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , "VKLO"
                    , List.of("1")
                    , "ARBSIT"
                    , List.of("01", "02")
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 16 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("2"))).map(Code::getValue).collect(Collectors.joining("")) + "."
                            , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("2"))).map(Code::getValue).collect(Collectors.joining("")) + ". "
                            + "Arbeidssituasjonen er '" + r.getFieldAsTrimmedString("ARBSIT") + "', forventet én av '"
                            + r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("03", "05", "06", "09", "10"))).map(Code::toString).collect(Collectors.toList())
                            + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , "VKLO"
                    , List.of("2")
                    , "ARBSIT"
                    , List.of("03", "05", "06", "09", "10")
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 17 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("4"))).map(Code::getValue).collect(Collectors.joining("")) + "."
                            , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("4"))).map(Code::getValue).collect(Collectors.joining("")) + ". "
                            + "Arbeidssituasjonen er '" + r.getFieldAsTrimmedString("ARBSIT") + "', forventet én av '"
                            + r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("03"))).map(Code::toString).collect(Collectors.toList())
                            + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , "VKLO"
                    , List.of("4")
                    , "ARBSIT"
                    , List.of("03")
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 18 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("6"))).map(Code::getValue).collect(Collectors.joining("")) + "."
                            , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("6"))).map(Code::getValue).collect(Collectors.joining("")) + ". "
                            + "Arbeidssituasjonen er '" + r.getFieldAsTrimmedString("ARBSIT") + "', forventet én av '"
                            + r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("09"))).map(Code::toString).collect(Collectors.toList())
                            + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , "VKLO"
                    , List.of("6")
                    , "ARBSIT"
                    , List.of("09")
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 19 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("8"))).map(Code::getValue).collect(Collectors.joining("")) + "."
                            , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("8"))).map(Code::getValue).collect(Collectors.joining("")) + ". "
                            + "Arbeidssituasjonen er '" + r.getFieldAsTrimmedString("ARBSIT") + "', forventet én av '"
                            + r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("10"))).map(Code::toString).collect(Collectors.toList())
                            + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.NORMAL_ERROR
                    )
                    , "VKLO"
                    , List.of("8")
                    , "ARBSIT"
                    , List.of("10")
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 20 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("3"))).map(Code::getValue).collect(Collectors.joining("")) + "."
                            , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er "
                            + r.getFieldDefinitionByName("VKLO").getCodeList().stream().filter(c -> Comparator.isCodeInCodelist(c.getCode(), List.of("3"))).map(Code::getValue).collect(Collectors.joining("")) + ". "
                            + "Arbeidssituasjonen er '" + r.getFieldAsTrimmedString("ARBSIT") + "', forventet én av '"
                            + r.getFieldDefinitionByName("TRYGDESIT").getCodeList().stream().map(Code::toString).collect(Collectors.toList())
                            + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.CRITICAL_ERROR
                    )
                    , "VKLO"
                    , List.of("3")
                    , "TRYGDESIT"
                    , r.getFieldDefinitionByName("TRYGDESIT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 22 Tilknytning til trygdesystemet og alder. 60 år eller yngre med alderspensjon."
                            , "Mottakeren (" + r.getFieldAsInteger("ALDER") + " år) er 60 år eller yngre og mottar alderspensjon."
                            , Constants.NORMAL_ERROR
                    )
                    , "TRYGDESIT"
                    , List.of("07")
                    , "ALDER"
                    , ">"
                    , 60
            );

            if (r.getFieldAsString("TRYGDESIT").equalsIgnoreCase("05")
                    && !r.getFieldAsString("BU18").equalsIgnoreCase("1")
                    && r.getFieldAsIntegerDefaultEquals0("ANTBU18") == 0
            ) {
                er.addEntry(
                        new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 23 Tilknytning til trygdesystemet og barn. Overgangsstønad."
                                , "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen."
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            {
                List<Code> trygdeSituasjon = r.getFieldDefinitionByName("TRYGDESIT").getCodeList().stream().filter(c -> c.getCode().equalsIgnoreCase(r.getFieldAsString("TRYGDESIT"))).collect(Collectors.toList());
                Code t = new Code("Uoppgitt", "Uoppgitt");

                if (!trygdeSituasjon.isEmpty()) {
                    t = trygdeSituasjon.get(0);
                }

                List<Code> arbeidSituasjon = r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().filter(c -> c.getCode().equalsIgnoreCase(r.getFieldAsString("ARBSIT"))).collect(Collectors.toList());
                Code a = new Code("Uoppgitt", "Uoppgitt");

                if (!arbeidSituasjon.isEmpty()) {
                    a = arbeidSituasjon.get(0);
                }


                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 24 Tilknytning til trygdesystemet og arbeidssituasjon. Uføretrygd/alderspensjon og ikke arbeidssøker."
                                , "Mottakeren mottar trygd (" + t + "), men det er ikke oppgitt 'Ikke arbeidssøker' på arbeidssituasjon (" + a + ")."
                                , Constants.CRITICAL_ERROR
                        )
                        , "TRYGDESIT"
                        , List.of("04", "07")
                        , "ARBSIT"
                        , r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().map(Code::getCode).filter(c -> c.equalsIgnoreCase("04")).collect(Collectors.toList())
                );
            }

            if (r.getFieldAsString("VKLO").equalsIgnoreCase("3")
                    && r.getFieldAsString("TRYGDESIT").equalsIgnoreCase("11")
                    && r.getFieldAsString("ARBSIT").equalsIgnoreCase("08")
            ) {
                er.addEntry(
                        new ErrorReportEntry(
                                r.getFieldAsString("SAKSBEHANDLER")
                                , r.getFieldAsString("PERSON_JOURNALNR")
                                , r.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 24B Tilknytning til trygdesystemet og arbeidssituasjon. Arbeidsavklaringspenger."
                                , "Mottakeren mottar trygden arbeidsavklaringspenger, men det er oppgitt 'Arbeidsløs, ikke registrert' på arbeidssituasjon"
                                , Constants.NORMAL_ERROR
                        )
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
                            , "Kontroll 25 Arbeidssituasjon. Gyldige koder."
                            , "Mottakerens arbeidssituasjon ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, eller feil kode er benyttet. Utfylt verdi er '"
                            + r.getFieldAsString("ARBSIT")
                            + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.CRITICAL_ERROR
                    )
                    , "ARBSIT"
                    , r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            {
                List<String> fields = List.of("STMND_1", "STMND_2", "STMND_3", "STMND_4", "STMND_5", "STMND_6", "STMND_7", "STMND_8", "STMND_9", "STMND_10", "STMND_11", "STMND_12");
                boolean harVarighet = fields.stream()
                        .anyMatch(field -> r.getFieldDefinitionByName(field)
                                .getCodeList()
                                .stream()
                                .map(Code::getCode)
                                .collect(Collectors.toList())
                                .contains(r.getFieldAsString(field))
                        );

                Integer bidrag = r.getFieldAsInteger("BIDRAG");
                boolean bidragOK = bidrag != null;


                Integer laan = r.getFieldAsInteger("LAAN");
                boolean laanOK = laan != null;

                int stonad = 0;
                boolean stonadOK = bidragOK || laanOK;

                if (bidragOK) {
                    stonad += bidrag;
                }

                if (laanOK) {
                    stonad += laan;
                }


                int stonadSumMax = 500000;
                int stonadSumMin = 50;

                if (!harVarighet) {
                    er.addEntry(
                            new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 26 Stønadsmåneder. Gyldige koder"
                                    , "Det er ikke krysset av for hvilke måneder mottakeren har fått utbetalt økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ")"
                                    + "i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
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
                                    , "Kontroll 27 Stønadssum mangler eller har ugyldige tegn."
                                    , "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ") i løpet av året, "
                                    + "eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
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
                                        , "Kontroll 28 Har varighet, men mangler stønadssum"
                                        , "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ") i løpet av året, "
                                        + "eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                                        , Constants.CRITICAL_ERROR
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
                                        , "Kontroll 29 Har stønadssum men mangler varighet"
                                        , "Mottakeren har fått i økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ") i løpet av året, "
                                        + "men mangler utfylling for hvilke måneder i løpet av året mottakeren har mottatt økonomisk stønad."
                                        , Constants.CRITICAL_ERROR
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
                                    , "Kontroll 30 Stønadssum på kr " + stonadSumMax + ",- eller mer."
                                    , "Det samlede stønadsbeløpet (summen " + stonad + " av bidrag " + bidrag + " og lån " + laan + ") "
                                    + "som mottakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMax + ",-."
                                    , Constants.NORMAL_ERROR
                            )
                    );
                }

                if (stonadOK && stonad <= stonadSumMin) {
                    er.addEntry(
                            new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 31 Stønadssum på kr " + stonadSumMin + ",- eller lavere."
                                    , "Det samlede stønadsbeløpet (summen " + stonad + " av bidrag " + bidrag + " og lån " + laan + ") "
                                    + "som mottakeren har fått i løpet av rapporteringsåret er lik/lavere enn Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMin + ",-."
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
                            , "Kontroll 32 Økonomiskrådgivning. Gyldige koder."
                            , "Det er ikke krysset av for om mottakeren er gitt økonomisk rådgiving i forbindelse med utbetaling av økonomisk sosialhjelp. "
                            + "Utfylt verdi er '" + r.getFieldAsString("GITT_OKONOMIRAD") + "'. Feltet er obligatorisk å fylle ut."
                            , Constants.CRITICAL_ERROR
                    )
                    , "GITT_OKONOMIRAD"
                    , r.getFieldDefinitionByName("GITT_OKONOMIRAD").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 33 Utarbeidelse av individuell plan"
                            , "Det er ikke krysset av for om mottakeren har fått utarbeidet individuell plan. "
                            + "Utfylt verdi er '" + r.getFieldAsString("FAAT_INDIVIDUELL_PLAN") + "'. Feltet er obligatorisk."
                            , Constants.CRITICAL_ERROR
                    )
                    , "FAAT_INDIVIDUELL_PLAN"
                    , r.getFieldDefinitionByName("FAAT_INDIVIDUELL_PLAN").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 35 Boligsituasjon"
                            , "Det er ikke krysset av for mottakerens boligsituasjon. "
                            + "Utfylt verdi er '" + r.getFieldAsString("BOSIT") + "'. Feltet er obligatorisk."
                            , Constants.CRITICAL_ERROR
                    )
                    , "BOSIT"
                    , r.getFieldDefinitionByName("BOSIT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            {
                Integer bidrag = r.getFieldAsIntegerDefaultEquals0("BIDRAG");
                Integer bidragMaanederSum = List.of(
                        "BIDRAG_JAN", "BIDRAG_FEB", "BIDRAG_MARS",
                        "BIDRAG_APRIL", "BIDRAG_MAI", "BIDRAG_JUNI",
                        "BIDRAG_JULI", "BIDRAG_AUG", "BIDRAG_SEPT",
                        "BIDRAG_OKT", "BIDRAG_NOV", "BIDRAG_DES")
                        .stream()
                        .map(r::getFieldAsIntegerDefaultEquals0)
                        .reduce(0, Integer::sum);

                if (0 < bidrag && bidrag.intValue() != bidragMaanederSum.intValue()) {
                    er.addEntry(
                            new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 36 Bidrag fordelt på måneder"
                                    , "Det er ikke fylt ut bidrag (" + bidragMaanederSum + ") fordelt på måneder eller sum stemmer ikke med sum bidrag (" + bidrag + ") utbetalt i løpet av året."
                                    , Constants.NORMAL_ERROR
                            )
                    );
                }
            }

            {
                Integer laan = r.getFieldAsIntegerDefaultEquals0("LAAN");
                Integer laanMaanederSum = List.of(
                        "LAAN_JAN", "LAAN_FEB", "LAAN_MARS",
                        "LAAN_APRIL", "LAAN_MAI", "LAAN_JUNI",
                        "LAAN_JULI", "LAAN_AUG", "LAAN_SEPT",
                        "LAAN_OKT", "LAAN_NOV", "LAAN_DES")
                        .stream()
                        .map(r::getFieldAsIntegerDefaultEquals0)
                        .reduce(0, Integer::sum);

                if (0 < laan && laan.intValue() != laanMaanederSum.intValue()) {
                    er.addEntry(
                            new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 37 Lån fordelt på måneder"
                                    , "Det er ikke fylt ut laan (" + laanMaanederSum + ") fordelt på måneder eller sum stemmer ikke med sum lån (" + laan + ") utbetalt i løpet av året."
                                    , Constants.NORMAL_ERROR
                            )
                    );
                }
            }

            ControlFodselsnummerDUFnummer.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 38 DUF-nummer"
                            , "Det er ikke oppgitt fødselsnummer/d-nummer på sosialhjelpsmottakeren eller fødselsnummeret/d-nummeret inneholder feil. "
                            + "Oppgi ett 12-sifret DUF- nummer."
                            , Constants.NORMAL_ERROR
                    )
                    , "PERSON_FODSELSNR"
                    , "PERSON_DUF"
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 39 Første vilkår i året, vilkår"
                            , "Det er ikke krysset av for om det stilles vilkår til mottakeren etter sosialtjenesteloven. "
                            + "Registreres for første vilkår i kalenderåret. Feltet er obligatorisk."
                            , Constants.CRITICAL_ERROR
                    )
                    , "VILKARSOSLOV"
                    , r.getFieldDefinitionByName("VILKARSOSLOV").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 40 Første vilkår i året, vilkår til søkerens samboer/ektefelle"
                            , "Det er ikke krysset av for om det stilles vilkår til mottakeren etter sosialtjenesteloven. "
                            + "Registreres for første vilkår i kalenderåret. Feltet er obligatorisk."
                            , Constants.CRITICAL_ERROR
                    )
                    , "VILKARSAMEKT"
                    , r.getFieldDefinitionByName("VILKARSAMEKT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 41 Dato for utbetalingsvedtak"
                            , "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven', "
                            + "så skal utbetalingsvedtakets dato (" + r.getFieldAsString("UTBETDATO") + ") (DDMMÅÅ) oppgis. Feltet er obligatorisk å fylle ut."
                            , Constants.CRITICAL_ERROR
                    )
                    , "VILKARSOSLOV"
                    , List.of("1")
                    , "UTBETDATO"
            );

            ControlFelt1InneholderKodeFraKodelisteSaaFelt2Dato.doControl(
                    r
                    , er
                    , new ErrorReportEntry(
                            r.getFieldAsString("SAKSBEHANDLER")
                            , r.getFieldAsString("PERSON_JOURNALNR")
                            , r.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 42 Til og med dato for utbetalingsvedtak"
                            , "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven', "
                            + "så skal utbetalingsvedtakets til og med dato (" + r.getFieldAsString("UTBETTOMDATO") + ") (DDMMÅÅ) oppgis. Feltet er obligatorisk å fylle ut."
                            , Constants.CRITICAL_ERROR
                    )
                    , "VILKARSOSLOV"
                    , List.of("1")
                    , "UTBETTOMDATO"
            );

            {
                String vilkar = r.getFieldAsString("VILKARSOSLOV");
                List<String> fields = List.of(
                        "VILKARARBEID", "VILKARKURS", "VILKARUTD",
                        "VILKARJOBBLOG", "VILKARJOBBTILB", "VILKARSAMT",
                        "VILKAROKRETT", "VILKARLIVSH", "VILKARHELSE",
                        "VILKARANNET", "VILKARDIGPLAN");
                boolean isAnyFilledIn = fields.stream()
                        .anyMatch(field -> r.getFieldDefinitionByName(field)
                                .getCodeList()
                                .stream()
                                .map(Code::getCode)
                                .collect(Collectors.toList())
                                .contains(r.getFieldAsString(field))
                        );

                if (vilkar.equalsIgnoreCase("1") && !isAnyFilledIn) {
                    er.addEntry(
                            new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 43 Type vilkår det stilles til mottakeren"
                                    , "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven', "
                                    + "så skal det oppgis hvilke vilkår som stilles til mottakeren. Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                    );
                }
            }
        });

        {
            if (er.getErrorType() < Constants.CRITICAL_ERROR) {

                Integer bidragSum = records.stream().map(r -> r.getFieldAsIntegerDefaultEquals0("BIDRAG")).reduce(0, Integer::sum);
                Integer laanSum = records.stream().map(r -> r.getFieldAsIntegerDefaultEquals0("LAAN")).reduce(0, Integer::sum);
                Integer stonadSum = bidragSum + laanSum;

                er.addStats(new StatsReportEntry(
                        "Sum"
                        , List.of(
                        new Code("STONAD", "Stønad")
                        , new Code("BIDRAG", "Bidrag")
                        , new Code("LAAN", "Lån")
                )
                        , List.of(
                        new StatsEntry("STONAD", stonadSum.toString())
                        , new StatsEntry("BIDRAG", bidragSum.toString())
                        , new StatsEntry("LAAN", laanSum.toString())
                )
                ));

                List<Integer> gyldigeRecordsAlder = records.stream().filter(r -> r.getFieldAsInteger("FNR_OK") == 1 && r.getFieldAsInteger("ALDER") != -1).map(r -> r.getFieldAsInteger("ALDER")).collect(Collectors.toList());
                er.addStats(new StatsReportEntry(
                        "Tilfeller"
                        , List.of(
                        new Code("I_ALT", "I alt")
                        , new Code("0_18", "Under 18")
                        , new Code("18_24", "18 - 24")
                        , new Code("25_44", "25 - 44")
                        , new Code("45_66", "45 - 66")
                        , new Code("67_999", "67 og over")
                        , new Code("UGYLDIG_FNR", "Ugyldig fnr")
                )
                        , List.of(
                        new StatsEntry("I_ALT", String.valueOf(records.size()))
                        , new StatsEntry("0_18", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Between.betweenInclusive(i, 0, 17)).count()))
                        , new StatsEntry("18_24", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Between.betweenInclusive(i, 18, 24)).count()))
                        , new StatsEntry("25_44", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Between.betweenInclusive(i, 25, 44)).count()))
                        , new StatsEntry("45_66", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Between.betweenInclusive(i, 45, 66)).count()))
                        , new StatsEntry("67_999", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Between.betweenInclusive(i, 67, 999)).count()))
                        , new StatsEntry("UGYLDIG_FNR", String.valueOf(records.stream().filter(r -> r.getFieldAsInteger("FNR_OK") == 0).count()))
                )
                ));

                List<Long> gyldigeRecordsStonadstid = records.stream()
                        .map(r -> List.of("STMND_1", "STMND_2", "STMND_3", "STMND_4", "STMND_5", "STMND_6", "STMND_7", "STMND_8", "STMND_9", "STMND_10", "STMND_11", "STMND_12")
                                .stream()
                                .filter(m -> 0 < r.getFieldAsIntegerDefaultEquals0(m))
                                .count()
                        )
                        .collect(Collectors.toList());
                er.addStats(new StatsReportEntry(
                        "Stønadstid"
                        , List.of(
                        new Code("1", "1 måned")
                        , new Code("2_3", "2 - 3 måneder")
                        , new Code("4_6", "4 - 6 måneder")
                        , new Code("7_9", "7 - 9 måneder")
                        , new Code("10_11", "10 - 11 måneder")
                        , new Code("12", "12 måneder")
                        , new Code("UOPPGITT", "Uoppgitt")
                )
                        , List.of(
                        new StatsEntry("1", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> i == 1).count()))
                        , new StatsEntry("2_3", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Between.betweenInclusive(i.intValue(), 2, 3)).count()))
                        , new StatsEntry("4_6", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Between.betweenInclusive(i.intValue(), 4, 6)).count()))
                        , new StatsEntry("7_9", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Between.betweenInclusive(i.intValue(), 7, 9)).count()))
                        , new StatsEntry("10_11", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Between.betweenInclusive(i.intValue(), 10, 11)).count()))
                        , new StatsEntry("12", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Between.betweenInclusive(i.intValue(), 12, 12)).count()))
                        , new StatsEntry("UOPPGITT", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> i == 0).count()))
                )
                ));

                List<Integer> gyldigeRecordsStonad = records.stream()
                        .map(r -> r.getFieldAsIntegerDefaultEquals0("BIDRAG") + r.getFieldAsIntegerDefaultEquals0("LAAN"))
                        .collect(Collectors.toList());
                er.addStats(new StatsReportEntry(
                        "Stønad"
                        , List.of(
                        new Code("1_9", "1 - 9999")
                        , new Code("10_49", "10000 - 49999")
                        , new Code("50_99", "50000 - 99999")
                        , new Code("100_149", "100000 - 149999")
                        , new Code("150_9999", "150000 og over")
                        , new Code("0", "Uoppgitt")
                )
                        , List.of(
                        new StatsEntry("1_9", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Between.betweenInclusive(i, 1, 9999)).count()))
                        , new StatsEntry("10_49", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Between.betweenInclusive(i, 10000, 49999)).count()))
                        , new StatsEntry("50_99", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Between.betweenInclusive(i, 50000, 99999)).count()))
                        , new StatsEntry("100_149", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Between.betweenInclusive(i, 100000, 149999)).count()))
                        , new StatsEntry("150_9999", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> 150000 <= i).count()))
                        , new StatsEntry("0", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> i == 0).count()))
                )
                ));
            }
        }

        return er;
    }
}
