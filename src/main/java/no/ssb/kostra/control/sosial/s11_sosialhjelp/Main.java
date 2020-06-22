package no.ssb.kostra.control.sosial.s11_sosialhjelp;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.sosial.Definitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Toolkit;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> inputFileContent = args.getInputContentAsStringList();
        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<Record> records = inputFileContent.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
        final String lf = Constants.lineSeparator;

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(records.stream(), er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

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
                                    , "Korrigér kommunenummeret. Forventet '"
                                    + args.getRegion().substring(0, 4)
                                    + "', men fant '"
                                    + r.getFieldAsTrimmedString("KOMMUNE_NR") + "'."
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
                                        , "Kontroll 04 Bydelsnummer"
                                        , "Korrigér bydel. Forventet én av  '"
                                        + Definitions.getBydelerAsList()
                                        + "', men fant '"
                                        + r.getFieldAsTrimmedString("BYDELSNR") + "'."
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
                                    , "Kontroll 05 Årgang"
                                    , "Korrigér årgang. Forventet '"
                                    + args.getAargang().substring(0, 2)
                                    + "', men fant '"
                                    + r.getFieldAsTrimmedString("VERSION") + "'."
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
                                    , "Kontroll 06 Fødselsnummer"
                                    , "Det er ikke oppgitt fødselsnummer/d-nummer på "
                                    + "deltakeren" + lf + "\teller fødselsnummeret/d-nummeret inneholder" +
                                    " feil." + lf + "\tMed mindre det er snakk om en utenlandsk " +
                                    "statsborger som ikke er" + lf + "\ttildelt norsk personnummer eller d-nummer, " +
                                    "skal feltet inneholde" + lf + "\tdeltakeren fødselsnummer/d-nummer (11 siffer)."
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
                                    , "Kontroll 07 Alder under 18 år"
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
                                    , "Kontroll 08 Alder er 96 år eller over"
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
                                    , "Kontroll 09 Kjønn"
                                    , "Korrigér kjønn. Forventet én av  '"
                                    + r.getFieldDefinitionByName("KJONN").getCodeList().stream().map(Code::toString).collect(Collectors.toList())
                                    + "', men fant '"
                                    + r.getFieldAsTrimmedString("KJONN") + "'."
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
                                    , "Kontroll 10 Sivilstand"
                                    , "Korrigér sivilstand. Forventet én av  '"
                                    + r.getFieldDefinitionByName("EKTSTAT").getCodeList().stream().map(Code::toString).collect(Collectors.toList())
                                    + "', men fant '"
                                    + r.getFieldAsTrimmedString("EKTSTAT") + "'."
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
                                    , "Kontroll 11 Forsørgerplikt for barn under 18 år i husholdningen. Gyldige verdier"
                                    , "Det er ikke krysset av for om deltakeren har barn under 18 år, " + lf +
                                    "som deltakeren (eventuelt ektefelle/samboer) har \n" +
                                    "forsørgerplikt for," + lf + "\tog som bor i husholdningen ved\n" +
                                    "siste kontakt. Feltet er obligatorisk å fylle ut."
                                    + r.getFieldDefinitionByName("BU18").getCodeList().stream().map(Code::toString).collect(Collectors.toList())
                                    + "', men fant "
                                    + r.getFieldAsTrimmedString("BU18")
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
                                    , "Kontroll 12 Det bor barn under 18 år i husholdningen. Mangler antall barn."
                                    , "Deltakeren har barn under 18 år, som deltakeren " + lf +
                                    "\t(eventuelt ektefelle/samboer) har forsørgerplikt for, " +
                                    "og som bor i husholdningen" + lf + "\tved siste kontakt, men det er " +
                                    "ikke oppgitt hvor mange barn som bor i husholdningen. " + lf +
                                    "\tFeltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "BU18"
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
                                    , "Antall barn under 18 år i husholdningen er 10 eller flere, er dette riktig?"
                                    , Constants.NORMAL_ERROR
                            )
                            , "ANTBU18"
                            , ">"
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
                                    , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret skal oppgis. Rett feltet til en av de gyldige kodene: "
                                    + r.getFieldDefinitionByName("VKLO").getCodeList().stream().map(Code::toString).collect(Collectors.toList())
                                    + "', men fant '"
                                    + r.getFieldAsTrimmedString("VKLO") + "'."
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
                                    , "Kontroll 15 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Arbeidsinntekt."
                                    , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er arbeidsinntekt, "
                                    + "men arbeidssituasjonen er ikke arbeid heltid/deltid. Feltet er obligatorisk å fylle ut"
                                    , Constants.NORMAL_ERROR
                            )
                            , "VKLO"
                            , List.of("1")
                            , "ARBSIT"
                            , List.of("1", "2")
                    );

                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 16 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Kursstønad/lønn i arbeidsmarkedstiltak."
                                    , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er Kursstønad/lønn i arbeidsmarkedstiltak, "
                                    + "men arbeidssituasjonen er ikke Under utdanning, På arbeidsmarkedstiltak (statlig), Kommunal tiltaksplass, "
                                    + "Kurs gjennom Introduksjonsordning eller Kvalifiseringsprogram. Feltet er obligatorisk å fylle ut."
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
                                    , "Kontroll 17 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Stipend/lån."
                                    , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er Stipend/lån, "
                                    + "men arbeidssituasjonen er ikke Under utdanning. Feltet er obligatorisk å fylle ut."
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
                                    , "Kontroll 18 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Introduksjonsstøtte."
                                    , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er Introduksjonsstøtte, "
                                    + "men arbeidssituasjonen er ikke Kurs gjennom introduksjonsordning. Feltet er obligatorisk å fylle ut."
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
                                    , "Kontroll 19 Viktigste kilde til livsopphold i relasjon til arbeidssituasjon. Kvalifiseringsstønad."
                                    , "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er Kvalifiseringsstønad, "
                                    + "men arbeidssituasjonen er ikke Kvalifiseringsprogram. Feltet er obligatorisk å fylle ut."
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
                                    , "Kontroll 20 Viktigste kilde til livsopphold i relasjon til tilknytning til trygdesystemet. Trygd."
                                    , "Det er oppgitt at mottakerens viktigste kilde til livsopphold er Trygd/pensjon, "
                                    + "men det er ikke oppgitt hvilken trygd som utgjorde største verdi ved siste kontakt med sosial-/NAVkontoret. "
                                    + "Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "VKLO"
                            , List.of("3")
                            , "TRYGDESIT"
                            , r.getFieldDefinitionByName("TRYGDESIT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    ControlFelt1BoolskSaaFelt2InneholderKodeFraKodeliste.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    r.getFieldAsString("SAKSBEHANDLER")
                                    , r.getFieldAsString("PERSON_JOURNALNR")
                                    , r.getFieldAsString("PERSON_FODSELSNR")
                                    , " "
                                    , "Kontroll 22 Tilknytning til trygdesystemet og alder. 60 år eller yngre med alderspensjon."
                                    , "Mottakeren er 60 år eller yngre og mottar alderspensjon."
                                    , Constants.NORMAL_ERROR
                            )
                            , "ALDER"
                            , "<="
                            , Definitions.getAgeLimit()
                            , "TRYGDESIT"
                            , r.getFieldDefinitionByName("VKLO").getCodeList().stream().map(Code::getCode).filter(s -> !s.equalsIgnoreCase("07")).collect(Collectors.toList())
                    );

                    if (r.getFieldAsString("TRYGDESIT").equalsIgnoreCase("05")
                            && !r.getFieldAsString("BU18").equalsIgnoreCase("1")
                            && r.getFieldAsInteger("ANTBU18") == 0
                    ) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        r.getFieldAsString("SAKSBEHANDLER")
                                        , r.getFieldAsString("PERSON_JOURNALNR")
                                        , r.getFieldAsString("PERSON_FODSELSNR")
                                        , " "
                                        , "Kontroll 23 Tilknytning til trygdesystemet og barn. Overgangsstønad."
                                        , "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen.  "
                                        , Constants.NORMAL_ERROR
                                )
                        );
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
                                    , "Mottakeren mottar trygd, men det er ikke oppgitt Ikke arbeidssøker på arbeidssituasjon."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "TRYGDESIT"
                            , List.of("04", "07")
                            , "ARBSIT"
                            , r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().map(Code::getCode).filter(c -> !c.equalsIgnoreCase("04")).collect(Collectors.toList())
                    );

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
                                        , "Kontroll 24B Mottakeren mottar trygden arbeidsavklaringspenger, men det er oppgitt Arbeidsløs, ikke registrert på arbeidssituasjon"
                                        , "Mottakeren mottar trygden arbeidsavklaringspenger, men det er oppgitt Arbeidsledig, "
                                        + "men ikke registrert hos NAV, på arbeidssituasjon."
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
                                    , "Mottakerens arbeidssituasjon ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "ARBSIT"
                            , r.getFieldDefinitionByName("ARBSIT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    {
                        List<String> fields = List.of("STMND_1", "STMND_2", "STMND_3", "STMND_4", "STMND_5", "STMND_6", "STMND_7", "STMND_8", "STMND_9", "STMND_10", "STMND_11", "STMND_12");
                        boolean isAnyFilledIn = fields.stream()
                                .anyMatch(field -> r.getFieldDefinitionByName(field)
                                        .getCodeList()
                                        .stream()
                                        .map(Code::getCode)
                                        .collect(Collectors.toList())
                                        .contains(r.getFieldAsString(field))
                                );

                        Integer bidrag = r.getFieldAsInteger("BIDRAG");
                        boolean bidragOK = (bidrag != null);

                        Integer laan = r.getFieldAsInteger("LAAN");
                        boolean laanOK = (laan != null);

                        Integer stonad = bidrag + laan;
                        boolean stonadOK = bidragOK || laanOK;

                        Integer stonadSumMax = 500000;
                        Integer stonadSumMin = 50;

                        if (!isAnyFilledIn) {
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
                                            , Constants.NORMAL_ERROR
                                    )
                            );
                        }

                        if (isAnyFilledIn) {
                            if (stonadOK && 0 < stonad) {
                                er.addEntry(
                                        new ErrorReportEntry(
                                                r.getFieldAsString("SAKSBEHANDLER")
                                                , r.getFieldAsString("PERSON_JOURNALNR")
                                                , r.getFieldAsString("PERSON_FODSELSNR")
                                                , " "
                                                , "Kontroll 28 Har varighet men mangler stønadssum"
                                                , "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ") i løpet av året, "
                                                + "eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
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
                                                , "Kontroll 29 Har stønadssum men mangler varighet"
                                                , "Mottakeren har fått i økonomisk sosialhjelp (bidrag " + bidrag + " eller lån " + laan + ") i løpet av året, "
                                                + "men mangler utfylling for hvilke måneder i løpet av året mottakeren har mottatt økonomisk stønad."
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
                                            , "Kontroll 30 Kvalifiseringssum på kr " + stonadSumMax + ",- eller mer."
                                            , "Det samlede stønadsbeløpet (summen " + stonad + " av bidrag " + bidrag + " og lån " + laan + ") "
                                            + "som mottakeren har fått i løpet av rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på kr. " + stonadSumMax + ",-."
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
                                    , "Det er ikke krysset av for om mottakeren er gitt økonomisk rådgiving i forbindelse med utbetaling av økonomisk sosialhjelp. Feltet er obligatorisk å fylle ut."
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
                                    , "Det er ikke krysset av for om mottakeren har fått utarbeidet individuell plan. Feltet er obligatorisk."
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
                                    , "Det er ikke krysset av for mottakerens boligsituasjon. Feltet er obligatorisk."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "BOSIT"
                            , r.getFieldDefinitionByName("BOSIT").getCodeList().stream().map(Code::getCode).collect(Collectors.toList())
                    );

                    {
                        Integer bidrag = r.getFieldAsInteger("BIDRAG");
                        Integer bidragSum = List.of(
                                "BIDRAG_JAN", "BIDRAG_FEB", "BIDRAG_MAR",
                                "BIDRAG_APR", "BIDRAG_MAI", "BIDRAG_JUN",
                                "BIDRAG_JUL", "BIDRAG_AUG", "BIDRAG_SEP",
                                "BIDRAG_OKT", "BIDRAG_NOV", "BIDRAG_DES")
                                .stream()
                                .map(field -> {
                                    try {
                                        return r.getFieldAsInteger(field);
                                    } catch (NullPointerException e) {
                                        return 0;
                                    }

                                })
                                .reduce(0, Integer::sum);

                        if (0 < bidrag && !bidrag.equals(bidragSum)) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            r.getFieldAsString("SAKSBEHANDLER")
                                            , r.getFieldAsString("PERSON_JOURNALNR")
                                            , r.getFieldAsString("PERSON_FODSELSNR")
                                            , " "
                                            , "Kontroll 36 Bidrag fordelt på måneder"
                                            , "Det er ikke fylt ut bidrag (" + bidrag + ") fordelt på måneder eller sum (" + bidragSum + ") stemmer ikke med sum bidrag utbetalt i løpet av året."
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }
                    }

                    {
                        Integer laan = r.getFieldAsInteger("LAAN");
                        Integer laanSum = List.of(
                                "BIDRAG_JAN", "BIDRAG_FEB", "BIDRAG_MAR",
                                "BIDRAG_APR", "BIDRAG_MAI", "BIDRAG_JUN",
                                "BIDRAG_JUL", "BIDRAG_AUG", "BIDRAG_SEP",
                                "BIDRAG_OKT", "BIDRAG_NOV", "BIDRAG_DES")
                                .stream()
                                .map(r::getFieldAsInteger)
                                .reduce(0, Integer::sum);

                        if (0 < laan && laan != laanSum) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            r.getFieldAsString("SAKSBEHANDLER")
                                            , r.getFieldAsString("PERSON_JOURNALNR")
                                            , r.getFieldAsString("PERSON_FODSELSNR")
                                            , " "
                                            , "Kontroll 37 Lån fordelt på måneder"
                                            , "Det er ikke fylt ut laan (" + laan + ") fordelt på måneder eller sum (" + laanSum + ") stemmer ikke med sum lån utbetalt i løpet av året."
                                            , Constants.CRITICAL_ERROR
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
                                    , "Feltet for Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven, "
                                    + "så skal utbetalingsvedtakets dato (DDMMÅÅ) oppgis. Feltet er obligatorisk å fylle ut."
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
                                    , "Feltet for Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven, "
                                    + "så skal utbetalingsvedtakets dato (DDMMÅÅ) oppgis. Feltet er obligatorisk å fylle ut."
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
                                            , "Feltet for Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven, "
                                            + "så skal det oppgis hvilke vilkår som stilles til mottakeren. Feltet er obligatorisk å fylle ut."
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }
                    }
                });

        return er;
    }
}
