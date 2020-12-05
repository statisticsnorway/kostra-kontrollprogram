package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.DatoFnr;

import javax.xml.xpath.XPathExpressionException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class IndividNodeHandler extends NodeHandler {
    private static Map<String, List<String>> mapDublettJournalnummer = new TreeMap<>();
    private static Map<String, List<String>> mapDublettFodselsnummer = new TreeMap<>();
    private LocalDate forrigeTelleDato;
    private long individAlder = -1;

    public IndividNodeHandler(ErrorReport er, Arguments args) {
        super(er, args);
    }

    public static void reset() {
        mapDublettJournalnummer = new TreeMap<>();
        mapDublettFodselsnummer = new TreeMap<>();
    }

    /**
     * Setter individets alder ved å finne forskjellen i antall år fra
     * fødselsdato til telledato
     *
     * @param fodselsDato - Fødselsdato på individet
     * @param telleDato   - 31. des i rapporteringsåret
     */
    public void setIndividAlder(LocalDate fodselsDato, LocalDate telleDato) {
        if (fodselsDato != null && telleDato != null) {
            Period p = Period.between(fodselsDato, telleDato);
            this.individAlder = p.getYears();
        }
    }

    /**
     * @return long Individets alder
     */
    public long getIndividAlder() {
        return this.individAlder;
    }

    /**
     *
     */
    @Override
    void process(StructuredNode individ) {
        String saksbehandler;
        String journalnummer = "";
        String individId = "";
        String fodselsnummer;
        String fodselsnummerString;
        String refNr = "";
        String avgiverVersjon;
        String avslutta3112;
        String bydelsnummer;

        try {
            // We can now conveniently query the sub-dom of each node
            // using XPATH:
            saksbehandler = individ.queryString("@Saksbehandler");
            journalnummer = individ.queryString("@Journalnummer");
            individId = "";
            fodselsnummerString = fodselsnummer = individ
                    .queryString("@Fodselsnummer");
            refNr = "";
            avgiverVersjon = args.getAargang();
            avslutta3112 = individ.queryString("@Avslutta3112");

            // Oslo sender inn på alle bydeler, et individ skal ha muligheten
            // til å være registret i flere bydeler
            if (args.getRegion().startsWith("0301")) {
                bydelsnummer = (individ.queryString("@Bydelsnummer") != null && individ
                        .queryString("@Bydelsnummer").length() > 0) ? individ
                        .queryString("@Bydelsnummer") : "99";

                // Bruker fodselnummerString til å dekke den unike kombinasjonen
                // av fodselsnummer og bydelsnummer
                fodselsnummerString = fodselsnummer + "@Bydel_" + bydelsnummer;

                // legger til bydelsnummer på journalnummeret i tilfelle en
                // journalnummer benyttes i flere bydeler
                journalnummer = journalnummer + "@Bydel_" + bydelsnummer;
            }

            controlValidateByXSD(
                    er,
                    new ErrorReportEntry(
                            " ",
                            " ",
                            " ",
                            " ",
                            "Individ Kontroll 01: Validering av individ",
                            "Definisjon av Individ er feil i forhold til filspesifikasjonen",
                            Constants.CRITICAL_ERROR), individ.getNode()
                            .getOwnerDocument(), "Individ.xsd");

            String tempVersjon = avgiverVersjon + "-12-31";
            LocalDate telleDato = assignDateFromString(tempVersjon,
                    Constants.datoFormatLangt);
            forrigeTelleDato = telleDato.minusYears(1);
            LocalDate fodselsDato = (fodselsnummer != null) ? assignDateFromString(fodselsnummer.substring(0, 6), Constants.datoFormatKort) : null;
            if (fodselsDato.isAfter(telleDato)) {
                fodselsDato = fodselsDato.minusYears(100L);
            }
            LocalDate individStartDato = assignDateFromString(individ.queryString("@StartDato"), Constants.datoFormatLangt);
            LocalDate individSluttDato = assignDateFromString(individ.queryString("@SluttDato"), Constants.datoFormatLangt);
            setIndividAlder(fodselsDato, telleDato);

            String telleDatoString = telleDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()));
            String individStartDatoString = (individStartDato != null) ? individStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat())) : "uoppgitt";
            String individSluttDatoString = (individSluttDato != null) ? individSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat())) : "uoppgitt";
            String forrigeTelleDatoString = (forrigeTelleDato != null) ? forrigeTelleDato.format(DateTimeFormatter.ofPattern(datePresentionFormat())) : "uoppgitt";

            individId = (individ.queryString("@Id") != null && individ.queryString("@Id").length() > 0) ? individ.queryString("@Id") : "Uoppgitt";

            if (journalnummer != null) {
                er.incrementCount();
            }

            // Kontroller for Individ
            controlDatoEtterDato(er, new ErrorReportEntry(saksbehandler,
                            journalnummer, individId, refNr,
                            "Individ Kontroll 02a: Startdato etter sluttdato",
                            "Individets startdato  (" + individStartDatoString
                                    + ") er etter sluttdato (" + individSluttDatoString
                                    + ")", Constants.CRITICAL_ERROR), individStartDato,
                    individSluttDato);

            controlDatoEtterDato(er, new ErrorReportEntry(saksbehandler,
                            journalnummer, individId, refNr,
                            "Individ Kontroll 02b: Sluttdato mot versjon",
                            "Individets sluttdato (" + individSluttDatoString
                                    + ") er før forrige telletidspunkt ("
                                    + forrigeTelleDatoString + ")",
                            Constants.CRITICAL_ERROR), forrigeTelleDato,
                    individSluttDato);

            // Datokontroll 02c utgår fordi individslutt sjekkes mot selv

            controlAvslutta3112(
                    er,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 02d: Avslutta 31 12 medfører at sluttdato skal være satt",
                            "Individet er avsluttet hos barnevernet og skal dermed være avsluttet. Sluttdato er "
                                    + individSluttDatoString + ". Kode for avsluttet er '" + avslutta3112 + "'.",
                            Constants.CRITICAL_ERROR), avslutta3112,
                    individSluttDato, telleDato);

            // Datokontroll 02e utgår fordi individstart sjekkes mot selv

            controlFodselsnummerOgDUFnummer(er, new ErrorReportEntry(
                            saksbehandler, journalnummer, individId, refNr,
                            "Individ Kontroll 03: Fødselsnummer og DUFnummer", " ",
                            Constants.CRITICAL_ERROR), fodselsnummer,
                    individ.queryString("@DUFnummer"),
                    "Feil i fødselsnummer. Kan ikke identifisere individet.",
                    "DUFnummer mangler. Kan ikke identifisere individet.",
                    "Fødselsnummer og DUFnummer mangler. Kan ikke identifisere individet.");

            controlDublettFodselsnummer(
                    er,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 04: Dublett på fødselsnummer",
                            "Dublett for fødselsnummer for journalnummer (Liste på journaler)",
                            Constants.CRITICAL_ERROR), fodselsnummerString);

            controlDublettJournalnummer(
                    er,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 05: Dublett på journalnummer",
                            "Dublett for journalnummer for fødselsnummer (Liste på fødselsnumre)",
                            Constants.CRITICAL_ERROR), fodselsnummerString);

            controlHarInnhold(
                    er,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 06: Har meldinger, planer eller tiltak",
                            "Individet har ingen meldinger, planer eller tiltak i løpet av året",
                            Constants.CRITICAL_ERROR),
                    individ.queryNodeList("Melding | Tiltak | Plan"));

            controlAlder(er, new ErrorReportEntry(saksbehandler, journalnummer,
                    individId, refNr,
                    "Individ Kontroll 07: Klient over 23 år avsluttes",
                    "Individet er " + this.getIndividAlder()
                            + " år og skal avsluttes som klient",
                    Constants.CRITICAL_ERROR), this.getIndividAlder());

            controlAlderOverAldersgrenseSkalHaUndernoder(er,
                    new ErrorReportEntry(saksbehandler, journalnummer,
                            individId, refNr,
                            "Individ Kontroll 08: Alder i forhold til tiltak",
                            "Individet er over 18 år og skal dermed ha tiltak",
                            Constants.NORMAL_ERROR), this.getIndividAlder(),
                    18, individ.queryNodeList("Tiltak"));

            if (args.getRegion().startsWith("0301")) {
                controlExists(er,
                        new ErrorReportEntry(saksbehandler, journalnummer,
                                individId, refNr,
                                "Individ Kontroll 09: Bydelsnummer",
                                "Filen mangler bydelsnummer.",
                                Constants.CRITICAL_ERROR),
                        individ.queryString("@Bydelsnummer"));
                controlExists(er, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Individ Kontroll 10: Bydelsnavn",
                                "Filen mangler bydelsnavn.", Constants.CRITICAL_ERROR),
                        individ.queryString("@Bydelsnavn"));

                // System.out.print(individ.queryString("@Bydelsnummer") + " : "
                // + region);
            }

            controlFodselsnummer(er, new ErrorReportEntry(
                            saksbehandler, journalnummer, individId, refNr,
                            "Individ Kontroll 11: Fødselsnummer", " ",
                            Constants.NORMAL_ERROR), fodselsnummer,
                    "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer.");

            // Kontroller for Melding
            List<StructuredNode> meldingList = individ.queryNodeList("Melding");

            for (StructuredNode melding : meldingList) {
                String meldingId = defaultString(melding.queryString("@Id"), "uoppgitt");
                LocalDate meldingStartDato = assignDateFromString(
                        melding.queryString("@StartDato"),
                        Constants.datoFormatLangt);
                LocalDate meldingSluttDato = assignDateFromString(
                        melding.queryString("@SluttDato"),
                        Constants.datoFormatLangt);
                String meldingStartDatoString = (meldingStartDato != null)
                        ? meldingStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()))
                        : "uoppgitt";
                String meldingSluttDatoString = (meldingSluttDato != null)
                        ? meldingSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()))
                        : "uoppgitt";
                String henlagt = "1";
                String meldingKonklusjon = melding.queryString("@Konklusjon");
                String meldingKonklusjonString = defaultString(meldingKonklusjon, "uoppgitt");

                controlDatoEtterDato(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Melding Kontroll 2a: Startdato etter sluttdato",
                                "Melding (" + meldingId
                                        + "). Meldingens startdato ("
                                        + meldingStartDatoString
                                        + ") er etter meldingens sluttdato ("
                                        + meldingSluttDatoString + ")",
                                Constants.CRITICAL_ERROR),
                        meldingStartDato,
                        assignDateFromString(melding.queryString("@SluttDato"),
                                Constants.datoFormatLangt));

                // kjøres kun hvis ikke er henlagt
                if (!meldingKonklusjonString.equalsIgnoreCase(henlagt)) {
                    controlDatoEtterDato(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Melding Kontroll 2c: Sluttdato mot individets sluttdato ",
                                    "Melding ("
                                            + meldingId
                                            + "). Meldingens sluttdato ("
                                            + meldingSluttDatoString
                                            + ") er etter individets sluttdato ("
                                            + individSluttDatoString + ")",
                                    Constants.CRITICAL_ERROR),
                            meldingSluttDato, individSluttDato);
                }
                controlAvslutta3112(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Melding Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på",
                                "Melding ("
                                        + meldingId
                                        + "). Individet er avsluttet hos barnevernet og dets meldinger skal dermed være avsluttet. Sluttdato er "
                                        + meldingSluttDatoString,
                                Constants.CRITICAL_ERROR), avslutta3112,
                        meldingSluttDato, telleDato);

                controlDatoEtterDato(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Melding Kontroll 2e: Startdato mot individets startdato",
                                "Melding ("
                                        + meldingId
                                        + "). Startdato ("
                                        + meldingStartDatoString
                                        + ") skal være lik eller etter individets startdato ("
                                        + individStartDatoString + ")",
                                Constants.CRITICAL_ERROR), individStartDato,
                        meldingStartDato);

                controlTidDager(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Melding Kontroll 3: Behandlingstid av melding",
                                "Melding ("
                                        + meldingId
                                        + "). Fristoverskridelse på behandlingstid for melding,  ("
                                        + meldingStartDatoString + " -> "
                                        + meldingSluttDatoString + ")",
                                Constants.NORMAL_ERROR), meldingStartDato,
                        meldingSluttDato, 7);

                controlKonkludertMeldingUnderNoder(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Melding Kontroll 4: Kontroll av konkludert melding, melder",
                                "Melding ("
                                        + meldingId
                                        + "). Konkludert melding mangler melder(e).",
                                Constants.CRITICAL_ERROR), melding,
                        "Melder",
                        "Melding ("
                                + meldingId
                                + "). Konkludert melding mangler melder(e).");

                controlKonkludertMeldingUnderNoder(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Melding Kontroll 5: Kontroll av konkludert melding, saksinnhold",
                                "Melding ("
                                        + meldingId
                                        + "). Konkludert melding mangler saksinnhold.",
                                Constants.CRITICAL_ERROR), melding,
                        "Saksinnhold",
                        "Melding ("
                                + meldingId
                                + "). Konkludert melding mangler saksinnhold.");

                List<StructuredNode> melderList = melding
                        .queryNodeList("Melder");

                // Kontroller for Melder
                if (meldingSluttDato != null && meldingSluttDato.isAfter(forrigeTelleDato)) {
                    if (melderList != null) {
                        for (StructuredNode melder : melderList) {
                            // 22 = Andre offentlige instanser
                            List<String> koder = List.of("22");
                            String melderKode = (melder.queryString("@Kode") != null && melder
                                    .queryString("@Kode").length() > 0) ? melder
                                    .queryString("@Kode") : "";
                            String melderKodeString = (melderKode.length() > 0) ? melderKode
                                    : "uoppgitt";
                            controlPresisering(
                                    er,
                                    new ErrorReportEntry(
                                            saksbehandler,
                                            journalnummer,
                                            individId,
                                            refNr,
                                            "Melder Kontroll 2: Kontroll av kode " + melderKodeString + " og presisering",
                                            "Melder med kode ("
                                                    + melderKodeString
                                                    + ") mangler presisering",
                                            Constants.CRITICAL_ERROR),
                                    melderKode, koder,
                                    melder.queryString("Presisering"));
                        }
                    }

                    List<StructuredNode> saksinnholdList = melding
                            .queryNodeList("Saksinnhold");

                    // Kontroller for Saksinnhold
                    if (saksinnholdList != null) {
                        for (StructuredNode saksinnhold : saksinnholdList) {
                            // 18 = Andre forhold ved foreldre/familien
                            // 19 = Andre forhold ved barnets situasjon
                            List<String> koder = List.of("18", "19");
                            String saksinnholdKode = defaultString(saksinnhold.queryString("@Kode"), "");
                            String saksinnholdKodeString = defaultString(saksinnholdKode, "uoppgitt");

                            controlPresisering(
                                    er,
                                    new ErrorReportEntry(
                                            saksbehandler,
                                            journalnummer,
                                            individId,
                                            refNr,
                                            "Saksinnhold Kontroll 2: Kontroll av kode " + saksinnholdKodeString + " og presisering",
                                            "Saksinnhold med kode ("
                                                    + saksinnholdKodeString
                                                    + ") mangler presisering",
                                            Constants.CRITICAL_ERROR),
                                    saksinnholdKode, koder,
                                    saksinnhold.queryString("Presisering"));
                        }
                    }
                }
                StructuredNode undersokelse = melding.queryNode("Undersokelse");

                // Kontroller for Undersokelse
                if (undersokelse != null) {
                    String undersokelseId = defaultString(undersokelse.queryString("@Id"), "uoppgitt");
                    LocalDate undersokelseStartDato = assignDateFromString(
                            undersokelse.queryString("@StartDato"),
                            Constants.datoFormatLangt);
                    assert undersokelseStartDato != null;

                    LocalDate undersokelseSluttDato = assignDateFromString(
                            undersokelse.queryString("@SluttDato"),
                            Constants.datoFormatLangt);
                    String undersokelseStartDatoString = (undersokelseStartDato != null)
                            ? undersokelseStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()))
                            : "uoppgitt";
                    String undersokelseSluttDatoString = (undersokelseSluttDato != null)
                            ? undersokelseSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()))
                            : "uoppgitt";
                    String undersokelseKonklusjon = defaultString(undersokelse.queryString("@Konklusjon"), "");
                    String undersokelsePresisering = defaultString(undersokelse.queryString("Presisering"), "");

                    controlDatoEtterDato(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 2a: Startdato etter sluttdato",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Undersøkelsens startdato ("
                                            + undersokelseStartDatoString
                                            + ") er etter undersøkelsens sluttdato ("
                                            + undersokelseSluttDatoString + ")",
                                    Constants.CRITICAL_ERROR),
                            undersokelseStartDato, undersokelseSluttDato);

                    controlDatoEtterDato(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 2b: Sluttdato mot rapporteringsår",
                                    "Undersøkelse (" + undersokelseId
                                            + "). Undersøkelsens sluttdato ("
                                            + undersokelseSluttDatoString
                                            + ") er ikke i rapporteringsåret ("
                                            + avgiverVersjon + ")",
                                    Constants.CRITICAL_ERROR),
                            forrigeTelleDato, undersokelseSluttDato);

                    controlDatoEtterDato(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 2c: Sluttdato mot individets sluttdato",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Undersøkelsens sluttdato ("
                                            + undersokelseSluttDatoString
                                            + ") er etter individets sluttdato ("
                                            + individSluttDatoString + ")",
                                    Constants.CRITICAL_ERROR),
                            undersokelseSluttDato, individSluttDato);

                    controlAvslutta3112(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på undersøkelsen",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Individet er avsluttet hos barnevernet og dets undersøkelser skal dermed være avsluttet. Sluttdato er "
                                            + undersokelseSluttDatoString + "",
                                    Constants.CRITICAL_ERROR), avslutta3112,
                            undersokelseSluttDato, telleDato);

                    controlDatoEtterDato(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 2e: Startdato mot individets startdato",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). StartDato ("
                                            + undersokelseStartDatoString
                                            + ") skal være lik eller etter StartDatoen ("
                                            + individStartDatoString
                                            + ") på individet",
                                    Constants.CRITICAL_ERROR),
                            individStartDato, undersokelseStartDato);

                    // 5 = Undersøkelsen er henlagt pga flytting
                    List<String> koderPresisering = List.of("5");
                    controlPresisering(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 3: Kontroll av kode og presisering",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Undersøkelse der kode for konklusjon er "
                                            + undersokelseKonklusjon
                                            + " mangler presisering",
                                    Constants.CRITICAL_ERROR),
                            undersokelseKonklusjon, koderPresisering,
                            undersokelsePresisering);

                    controlHarInnhold(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 4: Konklusjon av undersøkelse",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Avsluttet undersøkelse mangler konklusjon",
                                    Constants.CRITICAL_ERROR),
                            undersokelse.queryString("@SluttDato"),
                            undersokelseKonklusjon);

                    List<StructuredNode> vedtaksgrunnlagList = undersokelse
                            .queryNodeList("Vedtaksgrunnlag");
                    // 1 = Barneverntjenesten fatter vedtak om tiltak
                    // 2 = Begjæring om tiltak for fylkesnemnda
                    List<String> koderKonklusjon = List.of("1", "2");
                    controlFeltMedKoderSkalHaUndernoder(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 7: Konkludert undersøkelse skal ha vedtaksgrunnlag",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Undersøkelse konkludert med kode "
                                            + undersokelseKonklusjon
                                            + " skal ha vedtaksgrunnlag",
                                    Constants.CRITICAL_ERROR),
                            undersokelseKonklusjon, koderKonklusjon,
                            vedtaksgrunnlagList);

                    controlUndersokelseStartetTidligereEnn1JuliUtenKonklusjon(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 8: Ukonkludert undersøkelse påbegynt før 1. juli er ikke konkludert",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Undersøkelsen startet "
                                            + undersokelseStartDatoString
                                            + " og skal konkluderes da den har pågått i mer enn 6 måneder",
                                    Constants.NORMAL_ERROR), avgiverVersjon,
                            undersokelseStartDato, undersokelseSluttDato);

                    // Kontroller for Vedtaksgrunnlag
                    if (meldingSluttDato != null && forrigeTelleDato != null && meldingSluttDato.isAfter(forrigeTelleDato)) {
                        for (StructuredNode vedtaksgrunnlag : vedtaksgrunnlagList) {
                            // 18 = Andre forhold ved foreldre/familien
                            // 19 = Andre forhold ved barnets situasjon
                            List<String> kodelisteVedtaksgrunnlag = List.of("18", "19");
                            String vedtaksgrunnlagKode = defaultString(vedtaksgrunnlag.queryString("@Kode"), "");
                            String vedtaksgrunnlagPresisering = defaultString(vedtaksgrunnlag.queryString("Presisering"), "");

                            controlPresisering(
                                    er,
                                    new ErrorReportEntry(
                                            saksbehandler,
                                            journalnummer,
                                            individId,
                                            refNr,
                                            "Vedtaksgrunnlag Kontroll 2: Kontroll av kode " + vedtaksgrunnlagKode + " og presisering",
                                            "Vedtaksgrunnlag med kode "
                                                    + vedtaksgrunnlagKode
                                                    + " mangler presisering",
                                            Constants.CRITICAL_ERROR),
                                    vedtaksgrunnlagKode,
                                    kodelisteVedtaksgrunnlag,
                                    vedtaksgrunnlagPresisering);
                        }
                    }
                }
            }

            // Kontroller for Plan
            List<StructuredNode> planList = individ.queryNodeList("Plan");
            for (StructuredNode plan : planList) {
                String planId = defaultString(plan.queryString("@Id"), "uoppgitt");

                LocalDate planStartDato = assignDateFromString(
                        plan.queryString("@StartDato"),
                        Constants.datoFormatLangt);
                LocalDate planSluttDato = assignDateFromString(
                        plan.queryString("@SluttDato"),
                        Constants.datoFormatLangt);
                String planStartDatoString = (planStartDato != null)
                        ? planStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()))
                        : "uoppgitt";
                String planSluttDatoString = (planSluttDato != null)
                        ? planSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()))
                        : "uoppgitt";

                controlDatoEtterDato(
                        er,
                        new ErrorReportEntry(saksbehandler, journalnummer,
                                individId, refNr,
                                "Plan Kontroll 2a: Startdato etter sluttdato",
                                "Plan (" + planId + "). Planens startdato ("
                                        + planStartDatoString
                                        + ") er etter planens sluttdato ("
                                        + planSluttDatoString + ")",
                                Constants.CRITICAL_ERROR),
                        planStartDato,
                        assignDateFromString(plan.queryString("@SluttDato"),
                                Constants.datoFormatLangt));

                controlDatoEtterDato(er, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Plan Kontroll 2b: Sluttdato mot rapporteringsår",
                                "Plan (" + planId + "). Planens sluttdato ("
                                        + planSluttDatoString
                                        + ") er ikke i rapporteringsåret ("
                                        + avgiverVersjon + ")",
                                Constants.CRITICAL_ERROR), forrigeTelleDato,
                        planSluttDato);

                controlDatoEtterDato(er, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Plan Kontroll 2c: Sluttdato mot individets sluttdato",
                                "Plan (" + planId + "). Planens sluttdato ("
                                        + planSluttDatoString
                                        + ") er etter individets sluttdato ("
                                        + individSluttDatoString + ")",
                                Constants.CRITICAL_ERROR), planSluttDato,
                        individSluttDato);

                controlAvslutta3112(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Plan Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på",
                                "Plan ("
                                        + planId
                                        + "). Individet er avsluttet hos barnevernet og dets planer skal dermed være avsluttet. Sluttdato er "
                                        + planSluttDatoString + "",
                                Constants.CRITICAL_ERROR), avslutta3112,
                        planSluttDato, telleDato);

                controlDatoEtterDato(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Plan Kontroll 2e: Startdato mot individets startdato",
                                "Plan ("
                                        + planId
                                        + "). StartDato ("
                                        + planStartDatoString
                                        + ") skal være lik eller etter individets startdato ("
                                        + individStartDatoString + ")",
                                Constants.CRITICAL_ERROR), individStartDato,
                        planStartDato);
            }

            // Kontroller for tiltak
            List<StructuredNode> tiltakList = individ.queryNodeList("Tiltak");
            tiltakList.sort(new ComparatorNodeByStartDato());

            for (StructuredNode tiltak : tiltakList) {
                String tiltakId = defaultString(tiltak.queryString("@Id"), "uoppgitt");
                LocalDate tiltakStartDato = assignDateFromString(
                        tiltak.queryString("@StartDato"),
                        Constants.datoFormatLangt);
                LocalDate tiltakSluttDato = assignDateFromString(
                        tiltak.queryString("@SluttDato"),
                        Constants.datoFormatLangt);
                String tiltakStartDatoString = (tiltakStartDato != null)
                        ? tiltakStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()))
                        : "uoppgitt";
                String tiltakSluttDatoString = (tiltakSluttDato != null)
                        ? tiltakSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat()))
                        : "uoppgitt";
                String tiltakKategoriKode = defaultString(tiltak.queryString("Kategori/@Kode"), "");
                List<String> kodelisteKategoriKode = List.of("1.99", "2.99", "3.7",
                        "3.99", "4.99", "5.99", "6.99", "7.99", "8.99");
                String tiltakKategoriPresisering = defaultString(tiltak.queryString("Kategori/Presisering"), "");
                String tiltakOpphevelseKode = defaultString(tiltak.queryString("Opphevelse/@Kode"), "");
                List<String> kodelisteOpphevelseKode = List.of("4");
                String tiltakOpphevelsePresisering = defaultString(tiltak.queryString("Opphevelse/Presisering"), "");

                controlDatoEtterDato(er, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Tiltak Kontroll 2a: Startdato etter sluttdato",
                                "Tiltak (" + tiltakId + "). Startdato ("
                                        + tiltakStartDatoString
                                        + ") for tiltaket er etter sluttdato ("
                                        + tiltakSluttDatoString + ") for tiltaket",
                                Constants.CRITICAL_ERROR), tiltakStartDato,
                        tiltakSluttDato);

                controlDatoEtterDato(er, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Tiltak Kontroll 2b: Sluttdato mot rapporteringsår",
                                "Tiltak (" + tiltakId + "). Sluttdato ("
                                        + tiltakSluttDatoString
                                        + ") er ikke i rapporteringsåret ("
                                        + avgiverVersjon + ")",
                                Constants.CRITICAL_ERROR), forrigeTelleDato,
                        tiltakSluttDato);

                controlDatoEtterDato(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 2c: Sluttdato mot individets sluttdato",
                                "Tiltak (" + tiltakId + "). Sluttdato ("
                                        + tiltakSluttDatoString
                                        + ") er etter individets sluttdato ("
                                        + individSluttDatoString + ")",
                                Constants.CRITICAL_ERROR), tiltakSluttDato,
                        individSluttDato);

                controlAvslutta3112(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på",
                                "Tiltak ("
                                        + tiltakId
                                        + "). Individet er avsluttet hos barnevernet og dets tiltak skal dermed være avsluttet. Sluttdato er "
                                        + tiltakSluttDatoString + "",
                                Constants.CRITICAL_ERROR), avslutta3112,
                        tiltakSluttDato, telleDato);

                controlDatoEtterDato(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 2e: Startdato mot individets startdato",
                                "Tiltak ("
                                        + tiltakId
                                        + "). StartDato ("
                                        + tiltakStartDatoString
                                        + ") skal være lik eller etter individets startdato ("
                                        + individStartDatoString + ")",
                                Constants.CRITICAL_ERROR), individStartDato,
                        tiltakStartDato);

                controlTiltakOmsorgstiltakPresisering(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 4: Omsorgstiltak (" + tiltakId + ") med sluttdato krever årsak til opphevelse",
                                "Tiltak (" + tiltakId
                                        + "). Omsorgstiltak med sluttdato ("
                                        + tiltakSluttDatoString
                                        + ") krever årsak til opphevelse",
                                Constants.CRITICAL_ERROR), tiltak);

                controlOver7OgIBarnehage(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 5: Barn over 7 år og i barnehage",
                                "Tiltak ("
                                        + tiltakId
                                        + "). Barnet er over 7 år og i barnehage. Barnets alder er "
                                        + this.getIndividAlder() + " år",
                                Constants.NORMAL_ERROR), tiltakKategoriKode,
                        individAlder);

                controlOver11OgISFO(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 6: Barn over 11 år og i SFO",
                                "Tiltak ("
                                        + tiltakId
                                        + "). Barnet er over 11 år og i SFO. Barnets alder er "
                                        + this.getIndividAlder() + " år",
                                Constants.NORMAL_ERROR), tiltakKategoriKode,
                        individAlder);

                controlPresisering(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 7: Kontroll av manglende presisering for tiltakskategori",
                                "Tiltak (" + tiltakId + "). Tiltakskategori ("
                                        + tiltakKategoriKode
                                        + ") mangler presisering",
                                Constants.CRITICAL_ERROR), tiltakKategoriKode,
                        kodelisteKategoriKode, tiltakKategoriPresisering);

                controlPresisering(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 8: Kontroll av kode og presisering for opphevelse",
                                "Tiltak (" + tiltakId
                                        + "). Tiltaksopphevelse ("
                                        + tiltakOpphevelseKode
                                        + ") mangler presisering",
                                Constants.CRITICAL_ERROR),
                        tiltakOpphevelseKode, kodelisteOpphevelseKode,
                        tiltakOpphevelsePresisering);

                // Kontroll av Lovhjemmel-relaterte ting
                List<StructuredNode> lovhjemmelList = new ArrayList<>();
                lovhjemmelList.add(tiltak.queryNode("Lovhjemmel"));
                lovhjemmelList.addAll(tiltak.queryNodeList("JmfrLovhjemmel"));

                try {
                    for (StructuredNode lovhjemmel : lovhjemmelList) {
                        String kapittel = lovhjemmel.queryString("@Kapittel");
                        String paragraf = lovhjemmel.queryString("@Paragraf");
                        String ledd = lovhjemmel.queryString("@Ledd");

                        controlTiltakLovhjemmelOmsorgstiltakSluttDato(
                                er,
                                new ErrorReportEntry(
                                        saksbehandler,
                                        journalnummer,
                                        individId,
                                        refNr,
                                        "Lovhjemmel Kontroll 2: omsorgstiltak (" + tiltakId + ") med sluttdato krever årsak til opphevelse",
                                        "Tiltak ("
                                                + tiltakId
                                                + "). Opphevelse av omsorgstiltak mangler presisering",
                                        Constants.NORMAL_ERROR),
                                tiltakSluttDato, kapittel, paragraf, ledd,
                                tiltakOpphevelseKode, kodelisteOpphevelseKode,
                                tiltakOpphevelsePresisering);

                        controlTiltakLovhjemmelOver18OgPaOmsorgstiltak(
                                er,
                                new ErrorReportEntry(
                                        saksbehandler,
                                        journalnummer,
                                        individId,
                                        refNr,
                                        "Lovhjemmel Kontroll 3: Individet er over 18 år og har omsorgstiltak",
                                        "Tiltak ("
                                                + tiltakId
                                                + "). Individet er "
                                                + this.getIndividAlder()
                                                + " år og skal dermed ikke ha omsorgstiltak",
                                        Constants.CRITICAL_ERROR), kapittel,
                                paragraf, ledd, this.getIndividAlder());

                        controlTiltakLovhjemmel(
                                er,
                                new ErrorReportEntry(
                                        saksbehandler,
                                        journalnummer,
                                        individId,
                                        refNr,
                                        "Lovhjemmel Kontroll 4: Lovhjemmel",
                                        "Tiltak ("
                                                + tiltakId
                                                + "). Kapittel ("
                                                + kapittel
                                                + ") eller paragraf ("
                                                + paragraf
                                                + ") er rapportert med den ugyldige koden 0",
                                        Constants.CRITICAL_ERROR), kapittel,
                                paragraf);
                    }

                } catch (XPathExpressionException e) {
                    // No need to handle exception
                }
            }

            List<String> kodelistePlasseringsTiltak = List.of("1.1", "1.2", "1.99", "2.1", "2.2", "2.3", "2.4", "2.5", "2.6", "2.99", "8.2");

            controlFlerePlasseringstiltakISammePeriode(
                    er,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Tiltak Kontroll 9: Flere plasseringstiltak i samme periode",
                            "Plasseringstiltak kan ikke overlappe med mer enn 3 måneder",
                            Constants.NORMAL_ERROR), tiltakList,
                    kodelistePlasseringsTiltak, 3,
                    "Uhåndterlig feil i forbindelse med tiltak");

        } catch (NullPointerException e) {
            e.printStackTrace();
            er.addEntry(new ErrorReportEntry("Kontrollprogram", journalnummer,
                    individId, refNr, "Individ K1: Feil for individet",
                    Arrays.stream(e.getStackTrace()).collect(Collectors.toList()).toString(), Constants.CRITICAL_ERROR));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Kontrollerer at en dato er etter en annen dato. Hvis dato1 er etter
     * dato2, returnéres false samt at ErrorReportEntry legges til i
     * ErrorReport, ellers returneres true
     *
     * @param er    - ErrorReport
     * @param ere   - ErrorReportEntry
     * @param dato1 - LocalDate
     * @param dato2 - LocalDate
     */
    public void controlDatoEtterDato(ErrorReport er, ErrorReportEntry ere, LocalDate dato1, LocalDate dato2) {
        if (dato1 != null && dato2 != null && dato1.isAfter(dato2)) {
            er.addEntry(ere);
        }

    }

    /**
     * Kontrollerer at fødselsnummer er korrekt. Hvis fødselnummer mangler
     * sjekkes DUF-nummer om det er korrekt. Ved feil eller at begge mangler
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er            - ErrorReport
     * @param ere           - ErrorReportEntry
     * @param fodselsnummer - String
     * @param duFnummer     - String
     */
    public void controlFodselsnummerOgDUFnummer(ErrorReport er,
                                                ErrorReportEntry ere, String fodselsnummer, String duFnummer,
                                                String errorTextFodselsnummer, String errorTextDUFnummer,
                                                String errorTextMangler) {
        if (fodselsnummer != null) {
            if (!((DatoFnr.validNorwId(fodselsnummer) == 1)
                    || fodselsnummer.endsWith("00100")
                    || fodselsnummer.endsWith("00200")
                    || fodselsnummer.endsWith("55555")
                    || fodselsnummer.endsWith("99999"))) {
                ere.setErrorText(errorTextFodselsnummer);
                er.addEntry(ere);
            }

        } else if (duFnummer != null) {
            Pattern p = Pattern.compile("^[0-9]{12}$");
            Matcher m = p.matcher(duFnummer);

            if (!m.matches()) {
                ere.setErrorText(errorTextDUFnummer);
                er.addEntry(ere);

            }
        } else {
            ere.setErrorText(errorTextMangler);
            er.addEntry(ere);
        }

    }

    /**
     * Kontrollerer at fødselsnummer er korrekt. Ved feil eller mangler
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er            - ErrorReport
     * @param ere           - ErrorReportEntry
     * @param fodselsnummer - String
     */
    public void controlFodselsnummer(ErrorReport er,
                                     ErrorReportEntry ere, String fodselsnummer,
                                     String errorTextFodselsnummer) {
        if (empty(fodselsnummer) || (!(DatoFnr.validNorwId(fodselsnummer) == 1) || fodselsnummer.endsWith("55555"))) {
            ere.setErrorText(errorTextFodselsnummer);
            er.addEntry(ere);
        }

    }

    /**
     * Kontrollerer at et fødselnummer kun forekommer én gang i filutrekket. Ved
     * feil lages en liste hvor fodselnummeret forekommer, returneres false samt
     * at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er            - ErrorReport
     * @param ere           - ErrorReportEntry
     * @param fodselsnummer - String
     */
    public void controlDublettFodselsnummer(ErrorReport er, ErrorReportEntry ere, String fodselsnummer) {
        List<String> l = new ArrayList<>();

        try {
            synchronized (this) {
                if (fodselsnummer != null) {
                    if (!fodselsnummer.endsWith("00100")
                            && !fodselsnummer.endsWith("00200")
                            && !fodselsnummer.endsWith("55555")
                            && !fodselsnummer.endsWith("99999")) {

                        if (mapDublettFodselsnummer.containsKey(fodselsnummer)) {
                            l = mapDublettFodselsnummer.get(fodselsnummer);
                            StringBuilder concatinatedErrorText = new StringBuilder();
                            concatinatedErrorText.append(ere.getErrorText());
                            concatinatedErrorText
                                    .append("Fins også i følgende journaler: ");

                            for (String element : l) {
                                concatinatedErrorText.append(element);
                            }

                            ere.setErrorText(concatinatedErrorText.toString());
                            er.addEntry(ere);
                        }
                        l.add(ere.getJournalnummer());
                        mapDublettFodselsnummer.put(fodselsnummer, l);
                    }
                }
            }
        } catch (NullPointerException e) {
            ere.setErrorText("NullPointerException !!");
            er.addEntry(ere);
        }

    }

    /**
     * Kontrollerer at et journalnummer kun forekommer én gang i filutrekket.
     * Ved feil lages en liste hvor journalnummeret forekommer, returneres false
     * samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er            - ErrorReport
     * @param ere           - ErrorReportEntry
     * @param fodselsnummer - String
     */
    public void controlDublettJournalnummer(ErrorReport er, ErrorReportEntry ere, String fodselsnummer) {
        List<String> l = new ArrayList<>();
        try {

            if (fodselsnummer != null) {
                if (mapDublettJournalnummer.containsKey(ere.getJournalnummer())) {
                    l = mapDublettJournalnummer.get(ere.getJournalnummer());
                    StringBuilder concatinatedErrorText = new StringBuilder();
                    concatinatedErrorText.append(ere.getErrorText());
                    concatinatedErrorText.append("Fins i følgende personnummer: ");

                    for (String element : l) {
                        concatinatedErrorText.append(element);
                    }
                    ere.setErrorText(concatinatedErrorText.toString());
                    er.addEntry(ere);
                }

                l.add(fodselsnummer.substring(0, 4).concat("*******"));
                mapDublettJournalnummer.put(ere.getJournalnummer(), l);
            }

        } catch (NullPointerException e) {
            ere.setErrorText("NullPointerException !!");
            er.addEntry(ere);
        }

    }

    /**
     * Kontrollerer at listen har innhold, hvis den er tom returneres false samt
     * at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er   - ErrorReport
     * @param ere  - ErrorReportEntry
     * @param list - List<StructuredNode>
     */
    public void controlHarInnhold(ErrorReport er, ErrorReportEntry ere,
                                  List<StructuredNode> list) {
        if (list.isEmpty()) {
            er.addEntry(ere);
        }
    }

    /**
     * Kontrollerer at hvis felt1 har innhold skal også felt2 ha innhold, ellers
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er    - ErrorReport
     * @param ere   - ErrorReportEntry
     * @param felt1 - String
     * @param felt2 - String
     */
    public void controlHarInnhold(ErrorReport er, ErrorReportEntry ere, String felt1, String felt2) {
        if (!empty(felt1) && empty(felt2)) {
            er.addEntry(ere);
        }
    }


    /**
     * Kontrollerer at alder på klient er 23 år eller yngre. Hvis alder er over
     * 23 år returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er    - ErrorReport
     * @param ere   - ErrorReportEntry
     * @param alder - int
     */
    public void controlAlder(ErrorReport er, ErrorReportEntry ere, long alder) {
        if (23L < alder) {
            er.addEntry(ere);
        }

    }

    /**
     * Kontrollerer at et tidsspenn mellom dato1 og dato2 er innenfor
     * antallKalenderDager, ellers returneres false samt at ErrorReportEntry
     * legges til i ErrorReport.
     *
     * @param er                  - ErrorReport
     * @param ere                 - ErrorReportEntry
     * @param dato1               - LocalDate
     * @param dato2               - LocalDate
     * @param antallKalenderDager - int
     */
    public void controlTidDager(ErrorReport er, ErrorReportEntry ere,
                                LocalDate dato1, LocalDate dato2, int antallKalenderDager) {
        // hvis dato1 ikke er blank, dvs. har innhold/dato
        if (dato1 != null) {
            // Finner dato for frist
            LocalDate frist = dato1.plusDays(antallKalenderDager);

            // hvis dato2 ikke er blank og er etter fristen
            if (dato2 != null && dato2.isAfter(frist)) {
                // legg til feilmelding
                er.addEntry(ere);
            }
        }

        // alt gikk bra, ellers så manglet det informasjon som gjør kontrollen
        // ikke lar seg kjøre
    }

    /**
     * Kontrollerer at hvis alder er over aldersgrense så skal listen ha
     * innhold, ellers returneres false samt at ErrorReportEntry legges til i
     * ErrorReport.
     *
     * @param er           - ErrorReport
     * @param ere          - ErrorReportEntry
     * @param alder        - int
     * @param aldersgrense - int
     * @param list         - List<StructuredNode>
     */
    public void controlAlderOverAldersgrenseSkalHaUndernoder(ErrorReport er,
                                                             ErrorReportEntry ere, long alder, long aldersgrense,
                                                             List<StructuredNode> list) {
        if (aldersgrense < alder) {
            if (list.isEmpty()) {
                er.addEntry(ere);
            }
        }
    }

    /**
     * Kontrollerer at hvis kode er i kodeliste så skal list ha innhold, ellers
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er        - ErrorReport
     * @param ere       - ErrorReportEntry
     * @param kode      - String
     * @param kodeliste - List<String>
     * @param list      - List<StructuredNode>
     */
    public void controlFeltMedKoderSkalHaUndernoder(ErrorReport er,
                                                    ErrorReportEntry ere, String kode, List<String> kodeliste,
                                                    List<StructuredNode> list) {
        try {
            if (erKodeIKodeliste(kode, kodeliste) && list.isEmpty()) {
                er.addEntry(ere);
            }

        } catch (Exception e) {
            er.addEntry(ere);
        }
    }

    /**
     * Kontrollerer at konkluderte meldinger har gitt undernode (kan f.eks være
     * Melder eller Saksinnhold)
     *
     * @param er               - ErrorReport
     * @param ere              - ErrorReportEntry
     * @param melding          - StructuredNode
     * @param underNode        - String
     * @param exceptionMelding - String
     */
    public void controlKonkludertMeldingUnderNoder(ErrorReport er,
                                                   ErrorReportEntry ere, StructuredNode melding, String underNode,
                                                   String exceptionMelding) {
        try {
            //
            // Kode for konklusjon
            String konklusjon = melding.queryString("@Konklusjon");
            List<String> kodelisteKonklusjon = List.of("1", "2");

            // Sluttdato for Melding
            LocalDate sluttDato = assignDateFromString(
                    melding.queryString("@SluttDato"),
                    Constants.datoFormatLangt);
            List<StructuredNode> list = melding.queryNodeList(underNode);

            if (forrigeTelleDato.getYear() < sluttDato.getYear()
                    && !empty(konklusjon)
                    && erKodeIKodeliste(konklusjon, kodelisteKonklusjon)
                    && list.isEmpty()
            ) {
                er.addEntry(ere);
            }

        } catch (XPathExpressionException | NullPointerException e) {
            ere.setErrorText(exceptionMelding);
            er.addEntry(ere);
        }
    }

    /**
     * Kontrollerer at undersøkelser som er startet før 1. juli skal være
     * konkludert
     *
     * @param er        - ErrorReport
     * @param ere       - ErrorReportEntry
     * @param versjon   - String
     * @param startDato - LocalDate
     * @param sluttDato - LocalDate
     */
    public void controlUndersokelseStartetTidligereEnn1JuliUtenKonklusjon(
            ErrorReport er, ErrorReportEntry ere, String versjon,
            LocalDate startDato, LocalDate sluttDato) {
        String fristDatoString = versjon + "-07-01";
        LocalDate frist = assignDateFromString(fristDatoString,
                Constants.datoFormatLangt);

        if (startDato.isBefore(frist) && sluttDato == null) {
            er.addEntry(ere);
        }
    }

    /**
     * Kontrollerer at alle omsorgstiltak skal ha presisering
     *
     * @param er     - ErrorReport
     * @param ere    - ErrorReportEntry
     * @param tiltak - StructuredNode
     */
    public void controlTiltakOmsorgstiltakPresisering(ErrorReport er,
                                                      ErrorReportEntry ere, StructuredNode tiltak) {
        try {
            List<StructuredNode> lovhjemmelList = tiltak.queryNodeList("JmfrLovhjemmel");
            lovhjemmelList.add(tiltak.queryNode("Lovhjemmel"));

            boolean bool = false;

            for (StructuredNode lovhjemmel : lovhjemmelList) {
                String kapittel = lovhjemmel.queryString("@Kapittel");
                String paragraf = lovhjemmel.queryString("@Paragraf");
                String ledd = lovhjemmel.queryString("@Ledd");

                if (Comparator.isCodeInCodelist(kapittel, List.of("4"))
                        &&
                        (
                                Comparator.isCodeInCodelist(paragraf, List.of("12"))
                                        ||
                                        (
                                                Comparator.isCodeInCodelist(paragraf, List.of("8"))
                                                        &&
                                                        Comparator.isCodeInCodelist(ledd, List.of("2", "3"))
                                        )
                        )
                ) {
                    String opphevelse = tiltak.queryString("Opphevelse/Presisering");

                    if (opphevelse == null || opphevelse.length() == 0) {
                        bool = true;
                    }
                }
            }

            if (bool) {
                er.addEntry(ere);
            }

        } catch (XPathExpressionException e) {
            // No need to handle exception
        }
    }

    /**
     * Kontrollerer at alle omsorgstiltak skal ha sluttdato
     *
     * @param er                      - ErrorReport
     * @param ere                     - ErrorReportEntry
     * @param SluttDato               - LocalDate
     * @param kapittel                - String
     * @param paragraf                - String
     * @param ledd                    - String
     * @param tiltakOpphevelseKode    - String
     * @param kodelisteOpphevelseKode - List<String>
     * @param opphevelse              - String
     */
    public void controlTiltakLovhjemmelOmsorgstiltakSluttDato(
            ErrorReport er, ErrorReportEntry ere, LocalDate SluttDato,
            String kapittel, String paragraf, String ledd,
            String tiltakOpphevelseKode, List<String> kodelisteOpphevelseKode,
            String opphevelse) {
        try {
            if (Comparator.isCodeInCodelist(kapittel, List.of("4"))
                    &&
                    (
                            Comparator.isCodeInCodelist(paragraf, List.of("12"))
                                    ||
                                    (
                                            Comparator.isCodeInCodelist(paragraf, List.of("8"))
                                                    &&
                                                    Comparator.isCodeInCodelist(ledd, List.of("2", "3"))
                                    )
                    )
            ) {

                if (SluttDato != null && empty(opphevelse)) {
                    for (String kode : kodelisteOpphevelseKode) {
                        if (tiltakOpphevelseKode.equalsIgnoreCase(kode)) {
                            er.addEntry(ere);
                            return;
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            // No need to handle exception
        }
    }

    /**
     * Kontrollerer at Lovhjemmel sine koder for kapittel og paragraf ikke
     * starter med 0
     *
     * @param er       - ErrorReport
     * @param ere      - ErrorReportEntry
     * @param kapittel - String
     * @param paragraf - String
     */
    public void controlTiltakLovhjemmel(ErrorReport er, ErrorReportEntry ere, String kapittel, String paragraf) {
        try {
            if (kapittel.startsWith("0") || paragraf.startsWith("0")) {
                er.addEntry(ere);
            }

        } catch (NullPointerException e) {
            // No need to handle exception
        }
    }

    /**
     * Kontrollerer at overlappende plasseringstiltak ikke overlapper med mer
     * enn 3 måneder
     *
     * @param er                        - ErrorReport
     * @param ere                       - ErrorReportEntry
     * @param tiltakList                - List<StructuredNode>
     * @param plasseringstiltakKodeList - List<String>
     * @param antallMaaneder            - int
     * @param exceptionMelding          - String
     */
    public void controlFlerePlasseringstiltakISammePeriode(ErrorReport er,
                                                           ErrorReportEntry ere, List<StructuredNode> tiltakList,
                                                           List<String> plasseringstiltakKodeList, int antallMaaneder,
                                                           String exceptionMelding) {
        List<StructuredNode> plasseringstiltakList = new ArrayList<>();
        // Skal bare gjelde de tiltak.kategori med @Kode = /^[1|2|8]/ og kunne
        // ha 3 måneder overlapp mellom nr 1 og 2
        if (tiltakList != null && !tiltakList.isEmpty()
                && tiltakList.size() > 1) {
            try {
                for (StructuredNode tiltak : tiltakList) {
                    String kode = tiltak.queryString("Kategori/@Kode");

                    if (erKodeIKodeliste(kode, plasseringstiltakKodeList)) {
                        plasseringstiltakList.add(tiltak);
                    }
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }

            if (plasseringstiltakList.size() > 1) {
                List<String> feilmeldinger = new ArrayList<>();

                for (int i = 0; i < plasseringstiltakList.size(); i++) {
                    for (int j = 0; j < plasseringstiltakList.size(); j++) {
                        if (i != j) {
                            StructuredNode first = plasseringstiltakList.get(i);
                            String firstId;
                            StructuredNode second = plasseringstiltakList.get(j);
                            String secondId;

                            try {
                                firstId = first.queryString("@Id");
                                secondId = second.queryString("@Id");

                                LocalDate firstStartLocalDate = first.assignDateFromString(
                                        first.queryString("@StartDato"),
                                        Constants.datoFormatLangt);

                                LocalDate firstSluttLocalDate = first
                                        .assignDateFromString(
                                                first.queryString("@SluttDato"),
                                                Constants.datoFormatLangt);

                                LocalDate secondStartLocalDate = second
                                        .assignDateFromString(
                                                second.queryString("@StartDato"),
                                                Constants.datoFormatLangt);

                                if (firstStartLocalDate != null
                                        && firstSluttLocalDate != null
                                        && secondStartLocalDate != null
                                        && firstStartLocalDate
                                        .isBefore(firstSluttLocalDate
                                                .minusMonths(antallMaaneder))
                                        // periode for first må være mer enn 3 mnd for å
                                        // at det skal kunne bli 3 mnd med overlapp
                                        && secondStartLocalDate
                                        .isAfter(firstStartLocalDate)
                                        // andres startdato skal etter førstes startdato
                                        && secondStartLocalDate
                                        .isBefore(firstSluttLocalDate
                                                .minusMonths(antallMaaneder))
                                    // OG tre måneder før førstes sluttdato -> medfører 3
                                    // måneder eller mer overlapp
                                ) {
                                    String feilmelding = "Plasseringstiltak "
                                            + firstId
                                            + " med sluttdato "
                                            + firstSluttLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                            + " er mer enn 3 måneder etter "
                                            + secondId
                                            + " med startdato "
                                            + secondStartLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                            + ". Dette gir en overlapp på mer enn 3 måneder.";
                                    feilmeldinger.add(feilmelding);
                                }

                            } catch (XPathExpressionException e) {
                                ere.setErrorText(exceptionMelding);
                                er.addEntry(ere);
                                return;
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if (0 < feilmeldinger.size()) {
                    ere.setErrorText(String.join("<br/>\n", feilmeldinger));
                    er.addEntry(ere);
                }
            }
        }
    }

    /**
     * Kontroller at klienter over 18 ikke er på omsorgstiltak
     *
     * @param er       - ErrorReport
     * @param ere      - ErrorReportEntry
     * @param kapittel - String
     * @param paragraf - String
     * @param ledd     - String
     * @param alder    - int
     */
    public void controlTiltakLovhjemmelOver18OgPaOmsorgstiltak(
            ErrorReport er, ErrorReportEntry ere, String kapittel, String paragraf, String ledd, long alder) {
        try {
            if (alder > 18
                    &&
                    (
                            Comparator.isCodeInCodelist(kapittel, List.of("4"))
                                    &&
                                    (
                                            Comparator.isCodeInCodelist(paragraf, List.of("12"))
                                                    ||
                                                    (
                                                            Comparator.isCodeInCodelist(paragraf, List.of("8"))
                                                                    &&
                                                                    Comparator.isCodeInCodelist(ledd, List.of("2", "3"))
                                                    )
                                    )
                    )
            ) {
                er.addEntry(ere);
            }

        } catch (NullPointerException e) {
            // No need to handle exception
        }
    }

    // TODO: FEILER, lag tester individ 9 og 10

    /**
     * Kontroller at barn over 7 ikke er i barnehage
     *
     * @param er    - ErrorReport
     * @param ere   - ErrorReportEntry
     * @param kode  - String
     * @param alder - int
     */
    public void controlOver7OgIBarnehage(ErrorReport er,
                                         ErrorReportEntry ere, String kode, long alder) {
        if (alder > 7 && kode.equalsIgnoreCase("4.1")) {
            er.addEntry(ere);
        }
    }

    /**
     * Kontroller at barn over 11 år ikke er i SFO
     *
     * @param er    - ErrorReport
     * @param ere   - ErrorReportEntry
     * @param kode  - String
     * @param alder - int
     */
    public void controlOver11OgISFO(ErrorReport er, ErrorReportEntry ere, String kode, long alder) {
        if (alder > 11 && kode.equalsIgnoreCase("4.2")) {
            er.addEntry(ere);
        }
    }

    /**
     * Kontrollerer at sluttdato er satt
     *
     * @param er        - ErrorReport
     * @param ere       - ErrorReportEntry
     * @param kode      - String
     * @param sluttDato - LocalDate
     * @param frist     - LocalDate
     */
    public void controlAvslutta3112(ErrorReport er, ErrorReportEntry ere, String kode, LocalDate sluttDato, LocalDate frist) {
        try {
            if (kode.equalsIgnoreCase("1")) {
                if (sluttDato == null || frist == null
                        || sluttDato.isAfter(frist)) {
                    er.addEntry(ere);
                }
            }
        } catch (NullPointerException e) {
            er.addEntry(ere);
        }
    }
}
