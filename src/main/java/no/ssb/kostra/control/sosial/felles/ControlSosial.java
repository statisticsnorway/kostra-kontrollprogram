package no.ssb.kostra.control.sosial.felles;

import no.ssb.kostra.control.felles.ControlFelt1Boolsk;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFodselsnummer;
import no.ssb.kostra.control.sosial.Definitions;
import no.ssb.kostra.felles.*;
import no.ssb.kostra.utils.Fnr;
import no.ssb.kostra.utils.Format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControlSosial {

    public static LocalDate assignDateFromString(final String date, final String format) {
        if (date != null && format != null
                && date.length() == format.length()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(date, formatter);
        }
        return null;
    }

    public static String dnr2fnr(final String dnr) {
        var day = Format.parseInt(dnr.substring(0, 2));

        /* Hvis man bruker D-nummer legger man til 4 på første siffer. */
        /* Når dag er større enn 31 benyttes D-nummer, trekk fra 40 og få gyldig dag  */
        if (day > 31) {
            day = day - 40;
        }
        return String.format("%02d", day).concat(dnr.substring(2, 6));
    }

    public static boolean control03Kommunenummer(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport,
                new ErrorReportEntry(
                        record.getFieldAsString("SAKSBEHANDLER")
                        , record.getFieldAsString("PERSON_JOURNALNR")
                        , record.getFieldAsString("PERSON_FODSELSNR")
                        , " "
                        , "Kontroll 03 kommunenummer"
                        , "Korrigér kommunenummeret. Fant '" + record.getFieldAsTrimmedString("KOMMUNE_NR") + "', "
                        + "forventet '" + errorReport.getArgs().getRegion().substring(0, 4) + "'."
                        , Constants.CRITICAL_ERROR),
                record.getFieldAsString("KOMMUNE_NR"),
                Collections.singletonList(errorReport.getArgs().getRegion().substring(0, 4)));
    }

    public static boolean control03Bydelsnummer(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport,
                new ErrorReportEntry(
                        record.getFieldAsString("SAKSBEHANDLER")
                        , record.getFieldAsString("PERSON_JOURNALNR")
                        , record.getFieldAsString("PERSON_FODSELSNR")
                        , " "
                        , "Kontroll 03 Bydelsnummer"
                        , "Korrigér bydel. Fant '" + record.getFieldAsTrimmedString("BYDELSNR") + "', "
                        + "forventet én av '" + Definitions.getBydelerAsList(errorReport.getArgs().getRegion().substring(0, 4)) + "'."
                        , Constants.CRITICAL_ERROR),
                record.getFieldAsString("BYDELSNR"),
                Definitions.getBydelerAsList(errorReport.getArgs().getRegion().substring(0, 4)));
    }

    public static boolean control04OppgaveAar(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport,
                new ErrorReportEntry(
                        record.getFieldAsString("SAKSBEHANDLER")
                        , record.getFieldAsString("PERSON_JOURNALNR")
                        , record.getFieldAsString("PERSON_FODSELSNR")
                        , " "
                        , "Kontroll 04 Oppgaveår"
                        , "Korrigér årgang. Fant '" + record.getFieldAsTrimmedString("VERSION").substring(0, 2) + "', "
                        + "forventet '" + errorReport.getArgs().getAargang().substring(2, 4) + "'."
                        , Constants.CRITICAL_ERROR),
                record.getFieldAsString("VERSION"),
                Collections.singletonList(errorReport.getArgs().getAargang().substring(2, 4)));
    }

    public static boolean control05Fodselsnummer(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        return ControlFodselsnummer.doControl(
                errorReport,
                new ErrorReportEntry(
                        record.getFieldAsString("SAKSBEHANDLER")
                        , record.getFieldAsString("PERSON_JOURNALNR")
                        , record.getFieldAsString("PERSON_FODSELSNR")
                        , " "
                        , "Kontroll 05 Fødselsnummer"
                        , "Det er ikke oppgitt fødselsnummer/d-nummer på deltakeren eller fødselsnummeret/d-nummeret inneholder feil. "
                        + "Med mindre det er snakk om en utenlandsk statsborger som ikke er tildelt norsk personnummer eller d-nummer, "
                        + "skal feltet inneholde deltakeren fødselsnummer/d-nummer (11 siffer)."
                        , Constants.NORMAL_ERROR),
                record.getFieldAsString("PERSON_FODSELSNR"));
    }

    public static boolean control05AFodselsnummerDubletter(
            final ErrorReport errorReport, final List<KostraRecord> recordList) {

        errorReport.incrementCount();

        final var dubletter = recordList.stream()
                .filter(record -> Fnr.isValidNorwId(record.getFieldAsString("PERSON_FODSELSNR")))
                .collect(Collectors.groupingBy(record -> record.getFieldAsTrimmedString("PERSON_FODSELSNR"), Collectors.toList()))
                .entrySet()
                .stream()
                .filter(p -> 1 < p.getValue().size())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (0 < dubletter.size()) {
            dubletter.forEach((fnr, records) -> records.forEach(record -> {
                List<String> otherRecords = records.stream().filter(r -> !record.equals(r)).map(r -> r.getFieldAsTrimmedString("PERSON_JOURNALNR")).toList();
                errorReport.addEntry(
                        new ErrorReportEntry(
                                record.getFieldAsString("SAKSBEHANDLER")
                                , record.getFieldAsString("PERSON_JOURNALNR")
                                , record.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 05A Fødselsnummer, dubletter"
                                , "Fødselsnummeret i journalnummer " + record.getFieldAsString("PERSON_JOURNALNR") + " fins også i journalene " + otherRecords
                                , Constants.CRITICAL_ERROR));
            }));
            return true;
        }
        return false;
    }

    public static boolean control05BJournalnummerDubletter(
            final ErrorReport errorReport, final List<KostraRecord> recordList) {

        errorReport.incrementCount();

        final var dubletter = recordList.stream()
                .collect(Collectors.groupingBy(r -> r.getFieldAsString("PERSON_JOURNALNR"), Collectors.toList()))
                .entrySet()
                .stream()
                .filter(r -> r.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (0 < dubletter.size()) {
            dubletter.forEach((jnr, records) -> {
                final var count = records.size();
                records.forEach(record -> errorReport.addEntry(
                        new ErrorReportEntry(
                                record.getFieldAsString("SAKSBEHANDLER")
                                , record.getFieldAsString("PERSON_JOURNALNR")
                                , record.getFieldAsString("PERSON_FODSELSNR")
                                , " "
                                , "Kontroll 05B Journalnummer, dubletter"
                                , "Journalnummer " + record.getFieldAsString("PERSON_JOURNALNR") + " forekommer " + count + " ganger."
                                , Constants.CRITICAL_ERROR)));
            });
            return true;
        }
        return false;
    }

    public static boolean control06AlderUnder18Aar(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        if (record.getFieldAsInteger("FNR_OK") == 1) {
            return ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            record.getFieldAsString("SAKSBEHANDLER")
                            , record.getFieldAsString("PERSON_JOURNALNR")
                            , record.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 06 Alder under 18 år"
                            , "Deltakeren (" + record.getFieldAsTrimmedString("ALDER") + " år) er under 18 år."
                            , Constants.NORMAL_ERROR)
                    , record.getFieldAsInteger("ALDER")
                    , ">="
                    , 18);
        }
        return false;
    }

    public static boolean control07AlderEr96AarEllerOver(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        if (record.getFieldAsInteger("FNR_OK") == 1) {
            return ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            record.getFieldAsString("SAKSBEHANDLER")
                            , record.getFieldAsString("PERSON_JOURNALNR")
                            , record.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 07 Alder er 96 år eller over"
                            , "Deltakeren (" + record.getFieldAsTrimmedString("ALDER") + " år) er 96 år eller eldre."
                            , Constants.NORMAL_ERROR)
                    , record.getFieldAsInteger("ALDER")
                    , "<"
                    , 96);
        }
        return false;
    }

    public static boolean control07AlderEr68AarEllerOver(
            final ErrorReport errorReport, final KostraRecord record) {

        errorReport.incrementCount();

        if (record.getFieldAsInteger("FNR_OK") == 1) {
            return ControlFelt1Boolsk.doControl(
                    errorReport
                    , new ErrorReportEntry(
                            record.getFieldAsString("SAKSBEHANDLER")
                            , record.getFieldAsString("PERSON_JOURNALNR")
                            , record.getFieldAsString("PERSON_FODSELSNR")
                            , " "
                            , "Kontroll 07 Alder er 68 år eller over"
                            , "Deltakeren (" + record.getFieldAsTrimmedString("ALDER") + " år) er 68 år eller eldre."
                            , Constants.NORMAL_ERROR
                    )
                    , record.getFieldAsInteger("ALDER")
                    , "<"
                    , 68);
        }
        return false;
    }

    public static boolean control08Kjonn(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString("SAKSBEHANDLER")
                        , record.getFieldAsString("PERSON_JOURNALNR")
                        , record.getFieldAsString("PERSON_FODSELSNR")
                        , " "
                        , "Kontroll 08 Kjønn"
                        , "Korrigér kjønn. Fant '" + record.getFieldAsTrimmedString("KJONN") + "', "
                        + "forventet én av '" + record.getFieldDefinitionByName("KJONN").getCodeList().stream().map(Code::toString).toList() + "'. "
                        + "Mottakerens kjønn er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                ),
                record.getFieldAsString("KJONN"),
                record.getFieldDefinitionByName("KJONN").getCodeList().stream().map(Code::getCode).toList());
    }

    public static boolean control09Sivilstand(final ErrorReport errorReport, final KostraRecord record) {
        errorReport.incrementCount();

        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                errorReport
                , new ErrorReportEntry(
                        record.getFieldAsString("SAKSBEHANDLER")
                        , record.getFieldAsString("PERSON_JOURNALNR")
                        , record.getFieldAsString("PERSON_FODSELSNR")
                        , " "
                        , "Kontroll 09 Sivilstand"
                        , "Korrigér sivilstand. Fant '" + record.getFieldAsString("EKTSTAT") + "', "
                        + "forventet én av '" + record.getFieldDefinitionByName("EKTSTAT").getCodeList().stream().map(Code::toString).toList() + "'. "
                        + "Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                        , Constants.CRITICAL_ERROR
                ),
                record.getFieldAsString("EKTSTAT"),
                record.getFieldDefinitionByName("EKTSTAT").getCodeList().stream().map(Code::getCode).toList());
    }
}
