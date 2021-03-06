package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.utils.Fnr;
import no.ssb.kostra.utils.Format;

import javax.xml.xpath.XPathExpressionException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;


@SuppressWarnings("SpellCheckingInspection")
public class IndividNodeHandler extends NodeHandler {

    public static final String DATO_FORMAT_LANGT = "yyyy-MM-dd";

    private static Map<String, List<String>> mapDublettJournalnummer = new TreeMap<>();
    private static Map<String, List<String>> mapDublettFodselsnummer = new TreeMap<>();
    private LocalDate forrigeTelleDato;
    private int individAlder = -1;

    public IndividNodeHandler(ErrorReport er, Arguments args) {
        super(er, args);
        er.setReportHeaders(List.of("Saksbehandler", "Journalnummer", "Kontroll", "Feilmelding"));
    }

    public static void reset() {
        mapDublettJournalnummer = new TreeMap<>();
        mapDublettFodselsnummer = new TreeMap<>();
    }

    /**
     * Setter individets alder
     *
     * @param alder Age
     */
    public void setIndividAlder(final int alder) {
        this.individAlder = alder;
    }

    /**
     * @return int Individets alder
     */
    public int getIndividAlder() {
        return this.individAlder;
    }

    /**
     *
     */
    @Override
    void process(final StructuredNode individ) {
        String saksbehandler;
        var journalnummer = "";
        var individId = "";
        String fodselsnummer;
        String fodselsnummerString;
        var refNr = "";
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

            // Oslo sender inn p?? alle bydeler, et individ skal ha muligheten
            // til ?? v??re registret i flere bydeler
            var attrBydelsnummer = "@Bydelsnummer";

            if (args.getRegion().startsWith("0301")) {
                bydelsnummer = (individ.queryString(attrBydelsnummer) != null && individ
                        .queryString(attrBydelsnummer).length() > 0) ? individ
                        .queryString(attrBydelsnummer) : "99";

                // Bruker fodselnummerString til ?? dekke den unike kombinasjonen
                // av fodselsnummer og bydelsnummer
                fodselsnummerString = fodselsnummer + "@Bydel_" + bydelsnummer;

                // legger til bydelsnummer p?? journalnummeret i tilfelle en
                // journalnummer benyttes i flere bydeler
                journalnummer = journalnummer + "@Bydel_" + bydelsnummer;
            }

            controlValidateByXSD(
                    errorReport,
                    new ErrorReportEntry(
                            " ",
                            " ",
                            " ",
                            " ",
                            "Individ Kontroll 01: Validering av individ",
                            "Definisjon av Individ er feil i forhold til filspesifikasjonen",
                            Constants.CRITICAL_ERROR), individ.getNode()
                            .getOwnerDocument(), "Individ.xsd");

            final var tempVersjon = avgiverVersjon + "-12-31";
            final var telleDato = assignDateFromString(tempVersjon, DATO_FORMAT_LANGT);
            forrigeTelleDato = telleDato.minusYears(1);

            final var alder = (fodselsnummer != null) ? Fnr.getAlderFromFnr(fodselsnummer.substring(0, 6), args.getAargang()) : -2;
            final var individStartDato = assignDateFromString(individ.queryString("@StartDato"), DATO_FORMAT_LANGT);
            final var individSluttDato = assignDateFromString(individ.queryString("@SluttDato"), DATO_FORMAT_LANGT);
            setIndividAlder(alder);

            final var individStartDatoString = (individStartDato != null) ? individStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat)) : "uoppgitt";
            final var individSluttDatoString = (individSluttDato != null) ? individSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat)) : "uoppgitt";
            final var forrigeTelleDatoString = (forrigeTelleDato != null) ? forrigeTelleDato.format(DateTimeFormatter.ofPattern(datePresentionFormat)) : "uoppgitt";

            individId = (individ.queryString("@Id") != null && individ.queryString("@Id").length() > 0) ? individ.queryString("@Id") : "Uoppgitt";

            if (journalnummer != null) {
                errorReport.incrementCount();
            }

            // Kontroller for Individ
            controlDatoEtterDato(errorReport, new ErrorReportEntry(saksbehandler,
                            journalnummer, individId, refNr,
                            "Individ Kontroll 02a: Startdato etter sluttdato",
                            "Individets startdato  (" + individStartDatoString
                                    + ") er etter sluttdato (" + individSluttDatoString
                                    + ")", Constants.CRITICAL_ERROR), individStartDato,
                    individSluttDato);

            controlDatoEtterDato(errorReport, new ErrorReportEntry(saksbehandler,
                            journalnummer, individId, refNr,
                            "Individ Kontroll 02b: Sluttdato mot versjon",
                            "Individets sluttdato (" + individSluttDatoString
                                    + ") er f??r forrige telletidspunkt ("
                                    + forrigeTelleDatoString + ")",
                            Constants.CRITICAL_ERROR), forrigeTelleDato,
                    individSluttDato);

            // Datokontroll 02c utg??r fordi individslutt sjekkes mot selv

            controlAvslutta3112(
                    errorReport,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 02d: Avslutta 31 12 medf??rer at sluttdato skal v??re satt",
                            "Individet er avsluttet hos barnevernet og skal dermed v??re avsluttet. Sluttdato er "
                                    + individSluttDatoString + ". Kode for avsluttet er '" + avslutta3112 + "'.",
                            Constants.CRITICAL_ERROR), avslutta3112,
                    individSluttDato, telleDato);

            // Datokontroll 02e utg??r fordi individstart sjekkes mot selv

            controlFodselsnummerOgDUFnummer(errorReport, new ErrorReportEntry(
                            saksbehandler, journalnummer, individId, refNr,
                            "Individ Kontroll 03: F??dselsnummer og DUFnummer", " ",
                            Constants.CRITICAL_ERROR), fodselsnummer,
                    individ.queryString("@DUFnummer"),
                    "Feil i f??dselsnummer. Kan ikke identifisere individet.",
                    "DUFnummer mangler. Kan ikke identifisere individet.",
                    "F??dselsnummer og DUFnummer mangler. Kan ikke identifisere individet.");

            controlDublettFodselsnummer(
                    errorReport,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 04: Dublett p?? f??dselsnummer",
                            "Dublett for f??dselsnummer for journalnummer (Liste p?? journaler)",
                            Constants.CRITICAL_ERROR), fodselsnummerString);

            controlDublettJournalnummer(
                    errorReport,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 05: Dublett p?? journalnummer",
                            "Dublett for journalnummer for f??dselsnummer (Liste p?? f??dselsnumre)",
                            Constants.CRITICAL_ERROR), fodselsnummerString);

            controlHarInnhold(
                    errorReport,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 06: Har meldinger, planer eller tiltak",
                            "Individet har ingen meldinger, planer eller tiltak i l??pet av ??ret",
                            Constants.CRITICAL_ERROR),
                    individ.queryNodeList("Melding | Tiltak | Plan"));

            controlAlder(errorReport, new ErrorReportEntry(saksbehandler, journalnummer,
                    individId, refNr,
                    "Individ Kontroll 07: Klient over 25 ??r avsluttes",
                    "Individet er " + this.getIndividAlder()
                            + " ??r og skal avsluttes som klient",
                    Constants.CRITICAL_ERROR), this.getIndividAlder());

            controlAlderOverAldersgrenseSkalHaUndernoder(errorReport,
                    new ErrorReportEntry(saksbehandler, journalnummer,
                            individId, refNr,
                            "Individ Kontroll 08: Alder i forhold til tiltak",
                            "Individet er over 18 ??r og skal dermed ha tiltak",
                            Constants.NORMAL_ERROR), this.getIndividAlder(),
                    18, individ.queryNodeList("Tiltak"));

            if (args.getRegion().startsWith("0301")) {
                controlExists(errorReport,
                        new ErrorReportEntry(saksbehandler, journalnummer,
                                individId, refNr,
                                "Individ Kontroll 09: Bydelsnummer",
                                "Filen mangler bydelsnummer.",
                                Constants.CRITICAL_ERROR),
                        individ.queryString(attrBydelsnummer));
                controlExists(errorReport, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Individ Kontroll 10: Bydelsnavn",
                                "Filen mangler bydelsnavn.", Constants.CRITICAL_ERROR),
                        individ.queryString("@Bydelsnavn"));

                // System.out.print(individ.queryString("@Bydelsnummer") + " : "
                // + region);
            }

            controlFodselsnummer(errorReport, new ErrorReportEntry(
                            saksbehandler, journalnummer, individId, refNr,
                            "Individ Kontroll 11: F??dselsnummer", " ",
                            Constants.NORMAL_ERROR), fodselsnummer,
                    "Individet har ufullstendig f??dselsnummer. Korriger f??dselsnummer.");

            controlUtfyltFodselsnummer(errorReport, new ErrorReportEntry(
                    saksbehandler, journalnummer, individId, refNr,
                    "Individ Kontroll 12: F??dselsnummer",
                    "Individet har ufullstendig f??dselsnummer. Korriger f??dselsnummer.",
                    Constants.NORMAL_ERROR), fodselsnummer);

            controlDUFnummer(errorReport, new ErrorReportEntry(
                    saksbehandler, journalnummer, individId, refNr,
                    "Individ Kontroll 19: DUF-nummer",
                    "Individet har ufullstendig DUF-nummer. Korriger DUF-nummer.",
                    Constants.NORMAL_ERROR), individ.queryString("@DUFnummer")
            );

            // Kontroller for Melding
            final var meldingList = individ.queryNodeList("Melding");

            for (var melding : meldingList) {
                final var meldingId = defaultString(melding.queryString("@Id"), "uoppgitt");
                final var meldingStartDato = assignDateFromString(
                        melding.queryString("@StartDato"),
                        DATO_FORMAT_LANGT);
                final var meldingSluttDato = assignDateFromString(
                        melding.queryString("@SluttDato"),
                        DATO_FORMAT_LANGT);
                final var meldingStartDatoString = (meldingStartDato != null)
                        ? meldingStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat))
                        : "uoppgitt";
                final var meldingSluttDatoString = (meldingSluttDato != null)
                        ? meldingSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat))
                        : "uoppgitt";

                final var henlagt = "1";
                final var meldingKonklusjon = melding.queryString("@Konklusjon");
                final var meldingKonklusjonString = defaultString(meldingKonklusjon, "uoppgitt");

                controlDatoEtterDato(
                        errorReport,
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
                                DATO_FORMAT_LANGT));

                // kj??res kun hvis ikke er henlagt
                if (!meldingKonklusjonString.equalsIgnoreCase(henlagt)) {
                    controlDatoEtterDato(
                            errorReport,
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
                        errorReport,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Melding Kontroll 2d: Avslutta 31 12 medf??rer at sluttdato skal v??re satt p??",
                                "Melding ("
                                        + meldingId
                                        + "). Individet er avsluttet hos barnevernet og dets meldinger skal dermed v??re avsluttet. Sluttdato er "
                                        + meldingSluttDatoString,
                                Constants.CRITICAL_ERROR), avslutta3112,
                        meldingSluttDato, telleDato);

                controlDatoEtterDato(
                        errorReport,
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
                                        + ") skal v??re lik eller etter individets startdato ("
                                        + individStartDatoString + ")",
                                Constants.CRITICAL_ERROR), individStartDato,
                        meldingStartDato);

                controlTidDager(
                        errorReport,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Melding Kontroll 3: Behandlingstid av melding",
                                "Melding ("
                                        + meldingId
                                        + "). Fristoverskridelse p?? behandlingstid for melding,  ("
                                        + meldingStartDatoString + " -> "
                                        + meldingSluttDatoString + ")",
                                Constants.NORMAL_ERROR), meldingStartDato,
                        meldingSluttDato, 7);

                controlKonkludertMeldingUnderNoder(
                        errorReport,
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
                        errorReport,
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

                final var melderList = melding.queryNodeList("Melder");

                // Kontroller for Melder
                if (meldingSluttDato != null && meldingSluttDato.isAfter(forrigeTelleDato)) {
                    if (melderList != null) {
                        for (StructuredNode melder : melderList) {
                            // 22 = Andre offentlige instanser
                            final var koder = List.of("22");

                            final var melderKode = melder.queryString("@Kode") != null && melder
                                    .queryString("@Kode").length() > 0
                                    ? melder.queryString("@Kode")
                                    : "";

                            final var melderKodeString = (melderKode.length() > 0)
                                    ? melderKode
                                    : "uoppgitt";

                            controlPresisering(
                                    errorReport,
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

                    final var saksinnholdList = melding.queryNodeList("Saksinnhold");

                    // Kontroller for Saksinnhold
                    if (saksinnholdList != null) {
                        for (var saksinnhold : saksinnholdList) {
                            // 18 = Andre forhold ved foreldre/familien
                            // 19 = Andre forhold ved barnets situasjon
                            final var koder = List.of("18", "19");
                            final var saksinnholdKode = defaultString(saksinnhold.queryString("@Kode"), "");
                            final var saksinnholdKodeString = defaultString(saksinnholdKode, "uoppgitt");

                            controlPresisering(
                                    errorReport,
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
                final var undersokelse = melding.queryNode("Undersokelse");

                // Kontroller for Undersokelse
                if (undersokelse != null) {
                    final var undersokelseId = defaultString(undersokelse.queryString("@Id"), "uoppgitt");
                    final var undersokelseStartDato = assignDateFromString(
                            undersokelse.queryString("@StartDato"),
                            DATO_FORMAT_LANGT);
                    assert undersokelseStartDato != null;

                    final var undersokelseSluttDato = assignDateFromString(
                            undersokelse.queryString("@SluttDato"),
                            DATO_FORMAT_LANGT);
                    final var undersokelseStartDatoString = undersokelseStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat));
                    final var undersokelseSluttDatoString = (undersokelseSluttDato != null)
                            ? undersokelseSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat))
                            : "uoppgitt";
                    final var undersokelseKonklusjon = defaultString(undersokelse.queryString("@Konklusjon"), "");
                    final var undersokelsePresisering = defaultString(undersokelse.queryString("Presisering"), "");

                    controlDatoEtterDato(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 2a: Startdato etter sluttdato",
                                    "Unders??kelse ("
                                            + undersokelseId
                                            + "). Unders??kelsens startdato ("
                                            + undersokelseStartDatoString
                                            + ") er etter unders??kelsens sluttdato ("
                                            + undersokelseSluttDatoString + ")",
                                    Constants.CRITICAL_ERROR),
                            undersokelseStartDato, undersokelseSluttDato);

                    controlDatoEtterDato(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 2b: Sluttdato mot rapporterings??r",
                                    "Unders??kelse (" + undersokelseId
                                            + "). Unders??kelsens sluttdato ("
                                            + undersokelseSluttDatoString
                                            + ") er ikke i rapporterings??ret ("
                                            + avgiverVersjon + ")",
                                    Constants.CRITICAL_ERROR),
                            forrigeTelleDato, undersokelseSluttDato);

                    controlDatoEtterDato(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 2c: Sluttdato mot individets sluttdato",
                                    "Unders??kelse ("
                                            + undersokelseId
                                            + "). Unders??kelsens sluttdato ("
                                            + undersokelseSluttDatoString
                                            + ") er etter individets sluttdato ("
                                            + individSluttDatoString + ")",
                                    Constants.CRITICAL_ERROR),
                            undersokelseSluttDato, individSluttDato);

                    controlAvslutta3112(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 2d: Avslutta 31 12 medf??rer at sluttdato skal v??re satt p?? unders??kelsen",
                                    "Unders??kelse ("
                                            + undersokelseId
                                            + "). Individet er avsluttet hos barnevernet og dets unders??kelser skal dermed v??re avsluttet. Sluttdato er "
                                            + undersokelseSluttDatoString + "",
                                    Constants.CRITICAL_ERROR), avslutta3112,
                            undersokelseSluttDato, telleDato);

                    controlDatoEtterDato(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 2e: Startdato mot individets startdato",
                                    "Unders??kelse ("
                                            + undersokelseId
                                            + "). StartDato ("
                                            + undersokelseStartDatoString
                                            + ") skal v??re lik eller etter StartDatoen ("
                                            + individStartDatoString
                                            + ") p?? individet",
                                    Constants.CRITICAL_ERROR),
                            individStartDato, undersokelseStartDato);

                    // 5 = Unders??kelsen er henlagt pga flytting
                    final var koderPresisering = List.of("5");
                    controlPresisering(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 3: Kontroll av kode og presisering",
                                    "Unders??kelse ("
                                            + undersokelseId
                                            + "). Unders??kelse der kode for konklusjon er "
                                            + undersokelseKonklusjon
                                            + " mangler presisering",
                                    Constants.CRITICAL_ERROR),
                            undersokelseKonklusjon, koderPresisering,
                            undersokelsePresisering);

                    controlHarInnhold(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 4: Konklusjon av unders??kelse",
                                    "Unders??kelse ("
                                            + undersokelseId
                                            + "). Avsluttet unders??kelse mangler konklusjon",
                                    Constants.CRITICAL_ERROR),
                            undersokelse.queryString("@SluttDato"),
                            undersokelseKonklusjon);

                    final var vedtaksgrunnlagList = undersokelse.queryNodeList("Vedtaksgrunnlag");
                    // 1 = Barneverntjenesten fatter vedtak om tiltak
                    // 2 = Begj??ring om tiltak for fylkesnemnda
                    final var koderKonklusjon = List.of("1", "2");

                    controlFeltMedKoderSkalHaUndernoder(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 7: Konkludert unders??kelse skal ha vedtaksgrunnlag",
                                    "Unders??kelse ("
                                            + undersokelseId
                                            + "). Unders??kelse konkludert med kode "
                                            + undersokelseKonklusjon
                                            + " skal ha vedtaksgrunnlag",
                                    Constants.CRITICAL_ERROR),
                            undersokelseKonklusjon, koderKonklusjon,
                            vedtaksgrunnlagList);

                    controlUndersokelseStartetTidligereEnn1JuliUtenKonklusjon(
                            errorReport,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Unders??kelse Kontroll 8: Ukonkludert unders??kelse p??begynt f??r 1. juli er ikke konkludert",
                                    "Unders??kelse ("
                                            + undersokelseId
                                            + "). Unders??kelsen startet "
                                            + undersokelseStartDatoString
                                            + " og skal konkluderes da den har p??g??tt i mer enn 6 m??neder",
                                    Constants.NORMAL_ERROR), avgiverVersjon,
                            undersokelseStartDato, undersokelseSluttDato);

                    // Kontroller for Vedtaksgrunnlag

                    if (meldingSluttDato != null
                            && forrigeTelleDato != null
                            && meldingSluttDato.isAfter(forrigeTelleDato)) {

                        for (var vedtaksgrunnlag : vedtaksgrunnlagList) {
                            // 18 = Andre forhold ved foreldre/familien
                            // 19 = Andre forhold ved barnets situasjon
                            final var kodelisteVedtaksgrunnlag = List.of("18", "19");
                            final var vedtaksgrunnlagPresisering = defaultString(vedtaksgrunnlag.queryString("Presisering"), "");
                            final var vedtaksgrunnlagKode = defaultString(vedtaksgrunnlag.queryString("@Kode"), "");

                            controlPresisering(
                                    errorReport,
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
            final var planList = individ.queryNodeList("Plan");
            for (var plan : planList) {
                final var planId = defaultString(plan.queryString("@Id"), "uoppgitt");

                final var planStartDato = assignDateFromString(
                        plan.queryString("@StartDato"),
                        DATO_FORMAT_LANGT);
                final var planSluttDato = assignDateFromString(
                        plan.queryString("@SluttDato"),
                        DATO_FORMAT_LANGT);
                final var planStartDatoString = (planStartDato != null)
                        ? planStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat))
                        : "uoppgitt";
                final var planSluttDatoString = (planSluttDato != null)
                        ? planSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat))
                        : "uoppgitt";

                controlDatoEtterDato(
                        errorReport,
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
                                DATO_FORMAT_LANGT));

                controlDatoEtterDato(errorReport, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Plan Kontroll 2b: Sluttdato mot rapporterings??r",
                                "Plan (" + planId + "). Planens sluttdato ("
                                        + planSluttDatoString
                                        + ") er ikke i rapporterings??ret ("
                                        + avgiverVersjon + ")",
                                Constants.CRITICAL_ERROR), forrigeTelleDato,
                        planSluttDato);

                controlDatoEtterDato(errorReport, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Plan Kontroll 2c: Sluttdato mot individets sluttdato",
                                "Plan (" + planId + "). Planens sluttdato ("
                                        + planSluttDatoString
                                        + ") er etter individets sluttdato ("
                                        + individSluttDatoString + ")",
                                Constants.CRITICAL_ERROR), planSluttDato,
                        individSluttDato);

                controlAvslutta3112(
                        errorReport,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Plan Kontroll 2d: Avslutta 31 12 medf??rer at sluttdato skal v??re satt p??",
                                "Plan ("
                                        + planId
                                        + "). Individet er avsluttet hos barnevernet og dets planer skal dermed v??re avsluttet. Sluttdato er "
                                        + planSluttDatoString + "",
                                Constants.CRITICAL_ERROR), avslutta3112,
                        planSluttDato, telleDato);

                controlDatoEtterDato(
                        errorReport,
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
                                        + ") skal v??re lik eller etter individets startdato ("
                                        + individStartDatoString + ")",
                                Constants.CRITICAL_ERROR), individStartDato,
                        planStartDato);
            }

            // Kontroller for tiltak
            final var tiltakList = individ.queryNodeList("Tiltak");
            tiltakList.sort(new ComparatorNodeByStartDato());

            for (var tiltak : tiltakList) {
                final var tiltakId = defaultString(tiltak.queryString("@Id"), "uoppgitt");
                final var tiltakStartDato = assignDateFromString(
                        tiltak.queryString("@StartDato"),
                        DATO_FORMAT_LANGT);
                final var tiltakSluttDato = assignDateFromString(
                        tiltak.queryString("@SluttDato"),
                        DATO_FORMAT_LANGT);
                final var tiltakStartDatoString = (tiltakStartDato != null)
                        ? tiltakStartDato.format(DateTimeFormatter.ofPattern(datePresentionFormat))
                        : "uoppgitt";
                final var tiltakSluttDatoString = (tiltakSluttDato != null)
                        ? tiltakSluttDato.format(DateTimeFormatter.ofPattern(datePresentionFormat))
                        : "uoppgitt";
                final var tiltakKategoriKode = defaultString(tiltak.queryString("Kategori/@Kode"), "");
                final var kodelisteKategoriKode = List.of("1.99", "2.99", "3.7",
                        "3.99", "4.99", "5.99", "6.99", "7.99", "8.99");
                final var tiltakKategoriPresisering = defaultString(tiltak.queryString("Kategori/Presisering"), "");
                final var tiltakOpphevelseKode = defaultString(tiltak.queryString("Opphevelse/@Kode"), "");
                final var kodelisteOpphevelseKode = List.of("4");
                final var tiltakOpphevelsePresisering = defaultString(tiltak.queryString("Opphevelse/Presisering"), "");

                controlDatoEtterDato(errorReport, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Tiltak Kontroll 2a: Startdato etter sluttdato",
                                "Tiltak (" + tiltakId + "). Startdato ("
                                        + tiltakStartDatoString
                                        + ") for tiltaket er etter sluttdato ("
                                        + tiltakSluttDatoString + ") for tiltaket",
                                Constants.CRITICAL_ERROR), tiltakStartDato,
                        tiltakSluttDato);

                controlDatoEtterDato(errorReport, new ErrorReportEntry(saksbehandler,
                                journalnummer, individId, refNr,
                                "Tiltak Kontroll 2b: Sluttdato mot rapporterings??r",
                                "Tiltak (" + tiltakId + "). Sluttdato ("
                                        + tiltakSluttDatoString
                                        + ") er ikke i rapporterings??ret ("
                                        + avgiverVersjon + ")",
                                Constants.CRITICAL_ERROR), forrigeTelleDato,
                        tiltakSluttDato);

                controlDatoEtterDato(
                        errorReport,
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
                        errorReport,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 2d: Avslutta 31 12 medf??rer at sluttdato skal v??re satt p??",
                                "Tiltak ("
                                        + tiltakId
                                        + "). Individet er avsluttet hos barnevernet og dets tiltak skal dermed v??re avsluttet. Sluttdato er "
                                        + tiltakSluttDatoString + "",
                                Constants.CRITICAL_ERROR), avslutta3112,
                        tiltakSluttDato, telleDato);

                controlDatoEtterDato(
                        errorReport,
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
                                        + ") skal v??re lik eller etter individets startdato ("
                                        + individStartDatoString + ")",
                                Constants.CRITICAL_ERROR), individStartDato,
                        tiltakStartDato);

                controlTiltakOmsorgstiltakPresisering(
                        errorReport,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 4: Omsorgstiltak med sluttdato krever ??rsak til opphevelse",
                                "Tiltak (" + tiltakId
                                        + "). Omsorgstiltak med sluttdato ("
                                        + tiltakSluttDatoString
                                        + ") krever kode for opphevelse",
                                Constants.CRITICAL_ERROR), tiltak);

                controlOver7OgIBarnehage(
                        errorReport,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 5: Barn over 7 ??r og i barnehage",
                                "Tiltak ("
                                        + tiltakId
                                        + "). Barnet er over 7 ??r og i barnehage. Barnets alder er "
                                        + this.getIndividAlder() + " ??r",
                                Constants.NORMAL_ERROR), tiltakKategoriKode,
                        individAlder);

                controlOver11OgISFO(
                        errorReport,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 6: Barn over 11 ??r og i SFO",
                                "Tiltak ("
                                        + tiltakId
                                        + "). Barnet er over 11 ??r og i SFO. Barnets alder er "
                                        + this.getIndividAlder() + " ??r",
                                Constants.NORMAL_ERROR), tiltakKategoriKode,
                        individAlder);

                controlPresisering(
                        errorReport,
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
                        errorReport,
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
                final var lovhjemmelList = new ArrayList<StructuredNode>();
                lovhjemmelList.add(tiltak.queryNode("Lovhjemmel"));
                lovhjemmelList.addAll(tiltak.queryNodeList("JmfrLovhjemmel"));

                try {
                    for (var lovhjemmel : lovhjemmelList) {
                        final var kapittel = lovhjemmel.queryString("@Kapittel");
                        final var paragraf = lovhjemmel.queryString("@Paragraf");
                        final var ledd = lovhjemmel.queryString("@Ledd");

                        controlTiltakLovhjemmelOmsorgstiltakSluttDato(
                                errorReport,
                                new ErrorReportEntry(
                                        saksbehandler,
                                        journalnummer,
                                        individId,
                                        refNr,
                                        "Lovhjemmel Kontroll 2: omsorgstiltak (" + tiltakId + ") med sluttdato krever ??rsak til opphevelse",
                                        "Tiltak ("
                                                + tiltakId
                                                + "). Opphevelse av omsorgstiltak mangler presisering",
                                        Constants.NORMAL_ERROR),
                                tiltakSluttDato, kapittel, paragraf, ledd,
                                tiltakOpphevelseKode, kodelisteOpphevelseKode,
                                tiltakOpphevelsePresisering);

                        controlTiltakLovhjemmelOver18OgPaOmsorgstiltak(
                                errorReport,
                                new ErrorReportEntry(
                                        saksbehandler,
                                        journalnummer,
                                        individId,
                                        refNr,
                                        "Lovhjemmel Kontroll 3: Individet er over 18 ??r og har omsorgstiltak",
                                        "Tiltak ("
                                                + tiltakId
                                                + "). Individet er "
                                                + this.getIndividAlder()
                                                + " ??r og skal dermed ikke ha omsorgstiltak",
                                        Constants.CRITICAL_ERROR), kapittel,
                                paragraf, ledd, this.getIndividAlder());

                        controlTiltakLovhjemmel(
                                errorReport,
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

            final var kodelistePlasseringsTiltak =
                    List.of("1.1", "1.2", "1.99", "2.1", "2.2", "2.3", "2.4", "2.5", "2.6", "2.99", "8.2");

            controlFlerePlasseringstiltakISammePeriode(
                    errorReport,
                    new ErrorReportEntry(
                            saksbehandler,
                            journalnummer,
                            individId,
                            refNr,
                            "Tiltak Kontroll 9: Flere plasseringstiltak i samme periode",
                            "Plasseringstiltak kan ikke overlappe med mer enn 3 m??neder",
                            Constants.NORMAL_ERROR), tiltakList,
                    kodelistePlasseringsTiltak, 3,
                    "Uh??ndterlig feil i forbindelse med tiltak");
        } catch (NullPointerException e) {
            errorReport.addEntry(new ErrorReportEntry("Kontrollprogram", journalnummer,
                    individId, refNr, "Individ K1: Feil for individet",
                    Arrays.stream(e.getStackTrace()).toList().toString(), Constants.CRITICAL_ERROR));
        } catch (Exception e) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            " ",
                            " ",
                            " ",
                            " ",
                            "Kontrollprogrammet",
                            "Klarer ikke ?? lese fil. F??r feilmeldingen: " + e.getMessage(),
                            Constants.CRITICAL_ERROR));
        }
    }

    /**
     * Kontrollerer at en dato er etter en annen dato. Hvis dato1 er etter
     * dato2, return??res false samt at ErrorReportEntry legges til i
     * ErrorReport, ellers returneres true
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param dato1            - LocalDate
     * @param dato2            - LocalDate
     */
    public void controlDatoEtterDato(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final LocalDate dato1,
            final LocalDate dato2) {
        if (dato1 != null && dato2 != null && dato1.isAfter(dato2)) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at f??dselsnummer er korrekt. Hvis f??dselnummer mangler
     * sjekkes DUF-nummer om det er korrekt. Ved feil eller at begge mangler
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param fodselsnummer    - String
     * @param duFnummer        - String
     */
    public void controlFodselsnummerOgDUFnummer(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String fodselsnummer,
            final String duFnummer,
            final String errorTextFodselsnummer,
            final String errorTextDUFnummer,
            final String errorTextMangler) {

        if (fodselsnummer != null) {
            if (!(Fnr.isValidNorwId(fodselsnummer)
                    || fodselsnummer.endsWith("00100")
                    || fodselsnummer.endsWith("00200")
                    || fodselsnummer.endsWith("55555")
                    || fodselsnummer.endsWith("99999"))) {
                errorReportEntry.setErrorText(errorTextFodselsnummer);
                errorReport.addEntry(errorReportEntry);
            }

        } else if (duFnummer != null) {
            final var pattern = Pattern.compile("^\\d{12}$");
            final var matcher = pattern.matcher(duFnummer);

            if (!matcher.matches()) {
                errorReportEntry.setErrorText(errorTextDUFnummer);
                errorReport.addEntry(errorReportEntry);
            }
        } else {
            errorReportEntry.setErrorText(errorTextMangler);
            errorReport.addEntry(errorReportEntry);
        }
    }


    /**
     * Kontrollerer at f??dselsnummer er korrekt. Hvis f??dselnummer mangler
     * sjekkes DUF-nummer om det er korrekt. Ved feil eller at begge mangler
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param fodselsnummer    - String
     */
    public void controlUtfyltFodselsnummer(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String fodselsnummer) {

        if (!(Fnr.isValidNorwId(fodselsnummer)
                || fodselsnummer.endsWith("00100")
                || fodselsnummer.endsWith("00200")
                || fodselsnummer.endsWith("99999"))) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at DUF-nummer er korrekt hvis det er utfylt.
     * Ved feil eller mangler legges ErrorReportEntry til i ErrorReport
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param duFnummer        - String
     */
    public void controlDUFnummer(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String duFnummer) {

        if (duFnummer != null && !Fnr.isValidDUFnr(duFnummer)) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at f??dselsnummer er korrekt. Ved feil eller mangler
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param fodselsnummer    - String
     */
    public void controlFodselsnummer(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String fodselsnummer,
            final String errorTextFodselsnummer) {

        if (empty(fodselsnummer)
                || (!(Fnr.isValidNorwId(fodselsnummer))
                || fodselsnummer.endsWith("55555"))) {
            errorReportEntry.setErrorText(errorTextFodselsnummer);
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at et f??dselnummer kun forekommer ??n gang i filutrekket. Ved
     * feil lages en liste hvor fodselnummeret forekommer, returneres false samt
     * at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param fodselsnummer    - String
     */
    public void controlDublettFodselsnummer(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String fodselsnummer) {

        List<String> duplicateList = new ArrayList<>();

        try {
            synchronized (this) {
                if (fodselsnummer != null) {
                    if (!fodselsnummer.endsWith("00100")
                            && !fodselsnummer.endsWith("00200")
                            && !fodselsnummer.endsWith("55555")
                            && !fodselsnummer.endsWith("99999")) {

                        if (mapDublettFodselsnummer.containsKey(fodselsnummer)) {
                            duplicateList = mapDublettFodselsnummer.get(fodselsnummer);
                            var concatinatedErrorText = new StringBuilder();
                            concatinatedErrorText.append(errorReportEntry.getErrorText());
                            concatinatedErrorText
                                    .append("Fins ogs?? i f??lgende journaler: ");

                            for (var element : duplicateList) {
                                concatinatedErrorText.append(element);
                            }

                            errorReportEntry.setErrorText(concatinatedErrorText.toString());
                            errorReport.addEntry(errorReportEntry);
                        }
                        duplicateList.add(errorReportEntry.getJournalnummer());
                        mapDublettFodselsnummer.put(fodselsnummer, duplicateList);
                    }
                }
            }
        } catch (NullPointerException e) {
            errorReportEntry.setErrorText("NullPointerException !!");
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at et journalnummer kun forekommer ??n gang i filutrekket.
     * Ved feil lages en liste hvor journalnummeret forekommer, returneres false
     * samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param fodselsnummer    - String
     */
    public void controlDublettJournalnummer(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String fodselsnummer) {

        List<String> duplicateList = new ArrayList<>();

        try {
            if (fodselsnummer != null) {
                if (mapDublettJournalnummer.containsKey(errorReportEntry.getJournalnummer())) {
                    duplicateList = mapDublettJournalnummer.get(errorReportEntry.getJournalnummer());
                    var concatinatedErrorText = new StringBuilder();
                    concatinatedErrorText.append(errorReportEntry.getErrorText());
                    concatinatedErrorText.append("Fins i f??lgende personnummer: ");

                    for (var element : duplicateList) {
                        concatinatedErrorText.append(element);
                    }
                    errorReportEntry.setErrorText(concatinatedErrorText.toString());
                    errorReport.addEntry(errorReportEntry);
                }

                duplicateList.add(fodselsnummer.substring(0, 4).concat("*******"));
                mapDublettJournalnummer.put(errorReportEntry.getJournalnummer(), duplicateList);
            }
        } catch (NullPointerException e) {
            errorReportEntry.setErrorText("NullPointerException !!");
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at listen har innhold, hvis den er tom returneres false samt
     * at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param list             - List<StructuredNode>
     */
    public void controlHarInnhold(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final List<StructuredNode> list) {

        if (list.isEmpty()) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at hvis felt1 har innhold skal ogs?? felt2 ha innhold, ellers
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param felt1            - String
     * @param felt2            - String
     */
    public void controlHarInnhold(
            final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry,
            final String felt1,
            final String felt2) {

        if (!empty(felt1) && empty(felt2)) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at alder p?? klient er 23 ??r eller yngre. Hvis alder er over
     * 23 ??r returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param alder            - int
     */
    public void controlAlder(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final long alder) {
        if (25L < alder) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at et tidsspenn mellom dato1 og dato2 er innenfor
     * antallKalenderDager, ellers returneres false samt at ErrorReportEntry
     * legges til i ErrorReport.
     *
     * @param errorReport         - ErrorReport
     * @param errorReportEntry    - ErrorReportEntry
     * @param dato1               - LocalDate
     * @param dato2               - LocalDate
     * @param antallKalenderDager - int
     */
    public void controlTidDager(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final LocalDate dato1, final LocalDate dato2, final int antallKalenderDager) {

        // hvis dato1 ikke er blank, dvs. har innhold/dato
        if (dato1 != null) {
            // Finner dato for frist
            final var frist = dato1.plusDays(antallKalenderDager);

            // hvis dato2 ikke er blank og er etter fristen
            if (dato2 != null && dato2.isAfter(frist)) {
                // legg til feilmelding
                errorReport.addEntry(errorReportEntry);
            }
        }

        // alt gikk bra, ellers s?? manglet det informasjon som gj??r kontrollen
        // ikke lar seg kj??re
    }

    /**
     * Kontrollerer at hvis alder er over aldersgrense s?? skal listen ha
     * innhold, ellers returneres false samt at ErrorReportEntry legges til i
     * ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param alder            - int
     * @param aldersgrense     - int
     * @param list             - List<StructuredNode>
     */
    public void controlAlderOverAldersgrenseSkalHaUndernoder(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final long alder, final long aldersgrense, final List<StructuredNode> list) {

        if (aldersgrense < alder && list.isEmpty()) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at hvis kode er i kodeliste s?? skal list ha innhold, ellers
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param kode             - String
     * @param kodeliste        - List<String>
     * @param list             - List<StructuredNode>
     */
    public void controlFeltMedKoderSkalHaUndernoder(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String kode, final List<String> kodeliste, final List<StructuredNode> list) {

        try {
            if (erKodeIKodeliste(kode, kodeliste) && list.isEmpty()) {
                errorReport.addEntry(errorReportEntry);
            }
        } catch (Exception e) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at konkluderte meldinger har gitt undernode (kan f.eks v??re
     * Melder eller Saksinnhold)
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param melding          - StructuredNode
     * @param underNode        - String
     * @param exceptionMelding - String
     */
    public void controlKonkludertMeldingUnderNoder(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final StructuredNode melding, final String underNode, final String exceptionMelding) {

        try {
            //
            // Kode for konklusjon
            final var konklusjon = melding.queryString("@Konklusjon");
            final var kodelisteKonklusjon = List.of("1", "2");

            // Sluttdato for Melding
            final var sluttDato = assignDateFromString(
                    melding.queryString("@SluttDato"),
                    DATO_FORMAT_LANGT);
            final var list = melding.queryNodeList(underNode);

            if (forrigeTelleDato.getYear() < sluttDato.getYear()
                    && !empty(konklusjon)
                    && erKodeIKodeliste(konklusjon, kodelisteKonklusjon)
                    && list.isEmpty()) {
                errorReport.addEntry(errorReportEntry);
            }

        } catch (XPathExpressionException | NullPointerException e) {
            errorReportEntry.setErrorText(exceptionMelding);
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at unders??kelser som er startet f??r 1. juli skal v??re
     * konkludert
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param versjon          - String
     * @param startDato        - LocalDate
     * @param sluttDato        - LocalDate
     */
    public void controlUndersokelseStartetTidligereEnn1JuliUtenKonklusjon(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String versjon, final LocalDate startDato, final LocalDate sluttDato) {

        final var fristDatoString = versjon + "-07-01";
        final var frist = assignDateFromString(fristDatoString, DATO_FORMAT_LANGT);

        if (startDato.isBefore(frist) && sluttDato == null) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at alle omsorgstiltak skal ha presisering
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param tiltak           - StructuredNode
     */
    public void controlTiltakOmsorgstiltakPresisering(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final StructuredNode tiltak) {

        try {
            final var lovhjemmelList = tiltak.queryNodeList("JmfrLovhjemmel");
            lovhjemmelList.add(tiltak.queryNode("Lovhjemmel"));

            final var tiltakSluttDato = assignDateFromString(
                    tiltak.queryString("@SluttDato"),
                    DATO_FORMAT_LANGT);

            var opphevelseMangler = false;

            if (tiltakSluttDato != null) {
                for (var lovhjemmel : lovhjemmelList) {
                    final var kapittel = lovhjemmel.queryString("@Kapittel");
                    final var paragraf = lovhjemmel.queryString("@Paragraf");
                    final var ledd = lovhjemmel.queryString("@Ledd");

                    if (Comparator.isCodeInCodeList(kapittel, List.of("4"))
                            &&
                            (
                                    Comparator.isCodeInCodeList(paragraf, List.of("12"))
                                            ||
                                            (
                                                    Comparator.isCodeInCodeList(paragraf, List.of("8"))
                                                            &&
                                                            Comparator.isCodeInCodeList(ledd, List.of("2", "3"))
                                            ))) {

                        if (tiltak.queryNode("Opphevelse") == null) {
                            opphevelseMangler = true;
                        }
                    }
                }
            }

            if (opphevelseMangler) {
                errorReport.addEntry(errorReportEntry);
            }
        } catch (XPathExpressionException e) {
            // No need to handle exception
        }
    }

    /**
     * Kontrollerer at alle omsorgstiltak skal ha sluttdato
     *
     * @param errorReport             - ErrorReport
     * @param errorReportEntry        - ErrorReportEntry
     * @param SluttDato               - LocalDate
     * @param kapittel                - String
     * @param paragraf                - String
     * @param ledd                    - String
     * @param tiltakOpphevelseKode    - String
     * @param kodelisteOpphevelseKode - List<String>
     * @param opphevelse              - String
     */
    public void controlTiltakLovhjemmelOmsorgstiltakSluttDato(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final LocalDate SluttDato,
            final String kapittel, final String paragraf, final String ledd,
            final String tiltakOpphevelseKode, final List<String> kodelisteOpphevelseKode,
            final String opphevelse) {
        try {
            if (Comparator.isCodeInCodeList(kapittel, List.of("4"))
                    &&
                    (
                            Comparator.isCodeInCodeList(paragraf, List.of("12"))
                                    ||
                                    (
                                            Comparator.isCodeInCodeList(paragraf, List.of("8"))
                                                    &&
                                                    Comparator.isCodeInCodeList(ledd, List.of("2", "3"))
                                    ))) {

                if (SluttDato != null && empty(opphevelse)) {
                    for (var kode : kodelisteOpphevelseKode) {
                        if (tiltakOpphevelseKode.equalsIgnoreCase(kode)) {
                            errorReport.addEntry(errorReportEntry);
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
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param kapittel         - String
     * @param paragraf         - String
     */
    public void controlTiltakLovhjemmel(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String kapittel, final String paragraf) {

        try {
            if (kapittel.startsWith("0") || paragraf.startsWith("0")) {
                errorReport.addEntry(errorReportEntry);
            }
        } catch (NullPointerException e) {
            // No need to handle exception
        }
    }

    /**
     * Kontrollerer at overlappende plasseringstiltak ikke overlapper med mer
     * enn 3 m??neder
     *
     * @param errorReport               - ErrorReport
     * @param errorReportEntry          - ErrorReportEntry
     * @param tiltakList                - List<StructuredNode>
     * @param plasseringstiltakKodeList - List<String>
     * @param antallMaaneder            - int
     * @param exceptionMelding          - String
     */
    public void controlFlerePlasseringstiltakISammePeriode(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final List<StructuredNode> tiltakList, final List<String> plasseringstiltakKodeList,
            final int antallMaaneder, final String exceptionMelding) {

        final var plasseringstiltakList = new ArrayList<StructuredNode>();

        // Skal bare gjelde de tiltak.kategori med @Kode = /^[1|2|8]/ og kunne
        // ha 3 m??neder overlapp mellom nr 1 og 2
        if (tiltakList != null
                && !tiltakList.isEmpty()
                && tiltakList.size() > 1) {

            try {
                for (var tiltak : tiltakList) {
                    final var kode = tiltak.queryString("Kategori/@Kode");

                    if (erKodeIKodeliste(kode, plasseringstiltakKodeList)) {
                        plasseringstiltakList.add(tiltak);
                    }
                }
            } catch (XPathExpressionException e) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                " ",
                                " ",
                                " ",
                                " ",
                                "Kontrollprogrammet",
                                "Klarer ikke ?? lese fil. F??r feilmeldingen: " + e.getMessage(),
                                Constants.CRITICAL_ERROR));
            }

            if (plasseringstiltakList.size() > 1) {
                final var feilmeldinger = new ArrayList<String>();

                for (var i = 0; i < plasseringstiltakList.size(); i++) {
                    for (var j = 0; j < plasseringstiltakList.size(); j++) {
                        if (i != j) {
                            final var first = plasseringstiltakList.get(i);
                            String firstId;
                            final var second = plasseringstiltakList.get(j);
                            String secondId;

                            try {
                                firstId = first.queryString("@Id");
                                secondId = second.queryString("@Id");

                                final var firstStartLocalDate = first.assignDateFromString(
                                        first.queryString("@StartDato"),
                                        DATO_FORMAT_LANGT);

                                final var firstSluttLocalDate = first
                                        .assignDateFromString(
                                                first.queryString("@SluttDato"),
                                                DATO_FORMAT_LANGT);

                                final var secondStartLocalDate = second
                                        .assignDateFromString(
                                                second.queryString("@StartDato"),
                                                DATO_FORMAT_LANGT);

                                if (firstStartLocalDate != null
                                        && firstSluttLocalDate != null
                                        && secondStartLocalDate != null
                                        && firstStartLocalDate
                                        .isBefore(firstSluttLocalDate
                                                .minusMonths(antallMaaneder))
                                        // periode for first m?? v??re mer enn 3 mnd for ??
                                        // at det skal kunne bli 3 mnd med overlapp
                                        && secondStartLocalDate
                                        .isAfter(firstStartLocalDate)
                                        // andres startdato skal etter f??rstes startdato
                                        && secondStartLocalDate
                                        .isBefore(firstSluttLocalDate
                                                .minusMonths(antallMaaneder))
                                    // OG tre m??neder f??r f??rstes sluttdato -> medf??rer 3
                                    // m??neder eller mer overlapp
                                ) {
                                    final var feilmelding = "Plasseringstiltak "
                                            + firstId
                                            + " med sluttdato "
                                            + firstSluttLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                            + " er mer enn 3 m??neder etter "
                                            + secondId
                                            + " med startdato "
                                            + secondStartLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                            + ". Dette gir en overlapp p?? mer enn 3 m??neder.";
                                    feilmeldinger.add(feilmelding);
                                }

                            } catch (XPathExpressionException e) {
                                errorReportEntry.setErrorText(exceptionMelding);
                                errorReport.addEntry(errorReportEntry);
                                return;
                            } catch (NullPointerException e) {
                                errorReport.addEntry(
                                        new ErrorReportEntry(
                                                " ",
                                                " ",
                                                " ",
                                                " ",
                                                "Kontrollprogrammet",
                                                "Klarer ikke ?? lese fil. F??r feilmeldingen: " + e.getMessage(),
                                                Constants.CRITICAL_ERROR));
                            }
                        }
                    }
                }

                if (0 < feilmeldinger.size()) {
                    errorReportEntry.setErrorText(String.join("<br/>\n", feilmeldinger));
                    errorReport.addEntry(errorReportEntry);
                }
            }
        }
    }

    /**
     * Kontroller at klienter over 18 ikke er p?? omsorgstiltak
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param kapittel         - String
     * @param paragraf         - String
     * @param ledd             - String
     * @param alder            - int
     */
    public void controlTiltakLovhjemmelOver18OgPaOmsorgstiltak(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String kapittel, final String paragraf, final String ledd, final long alder) {

        try {
            if (alder > 18
                    &&
                    (
                            Comparator.isCodeInCodeList(kapittel, List.of("4"))
                                    &&
                                    (
                                            Comparator.isCodeInCodeList(paragraf, List.of("12"))
                                                    ||
                                                    (
                                                            Comparator.isCodeInCodeList(paragraf, List.of("8"))
                                                                    &&
                                                                    Comparator.isCodeInCodeList(ledd, List.of("2", "3"))
                                                    )))) {
                errorReport.addEntry(errorReportEntry);
            }
        } catch (NullPointerException e) {
            // No need to handle exception
        }
    }

    // TODO: FEILER, lag tester individ 9 og 10

    /**
     * Kontroller at barn over 7 ikke er i barnehage
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param kode             - String
     * @param alder            - int
     */
    public void controlOver7OgIBarnehage(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String kode, final long alder) {

        if (alder > 7 && kode.equalsIgnoreCase("4.1")) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontroller at barn over 11 ??r ikke er i SFO
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param kode             - String
     * @param alder            - int
     */
    public void controlOver11OgISFO(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String kode, final long alder) {

        if (alder > 11 && kode.equalsIgnoreCase("4.2")) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Kontrollerer at sluttdato er satt
     *
     * @param errorReport      - ErrorReport
     * @param errorReportEntry - ErrorReportEntry
     * @param kode             - String
     * @param sluttDato        - LocalDate
     * @param frist            - LocalDate
     */
    public void controlAvslutta3112(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String kode, final LocalDate sluttDato, final LocalDate frist) {

        try {
            if (kode.equalsIgnoreCase("1")) {
                if (sluttDato == null
                        || frist == null
                        || sluttDato.isAfter(frist)) {
                    errorReport.addEntry(errorReportEntry);
                }
            }
        } catch (NullPointerException e) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    private String dnr2fnr(final String dnr) {
        var day = Format.parseInt(dnr.substring(0, 2));

        /* Hvis man bruker D-nummer legger man til 4 p?? f??rste siffer. */
        /* N??r dag er st??rre enn 31 benyttes D-nummer, trekk fra 40 og f?? gyldig dag  */
        if (day > 31) {
            day = day - 40;
        }
        return String.format("%02d", day).concat(dnr.substring(2, 6));
    }
}
