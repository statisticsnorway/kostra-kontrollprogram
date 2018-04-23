package no.ssb.kostra.control.sensitiv.barnevern;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.utils.DatoFnr;
import org.joda.time.DateTime;
import org.joda.time.Years;

import javax.xml.xpath.XPathExpressionException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ojj
 */

public class IndividNodeHandler extends NodeHandler {
    private static Map<String, List<String>> mapDublettJournalnummer = new TreeMap<String, List<String>>();
    private static Map<String, List<String>> mapDublettFodselsnummer = new TreeMap<String, List<String>>();
    private static Map<String, Integer> mapUniqueID = new TreeMap<String, Integer>();
    private DateTime individStartDato;
    private DateTime individSluttDato;
    private DateTime fodselsDato;
    private DateTime telleDato;
    private DateTime forrigeTelleDato;
    private int individAlder = -1;

    public IndividNodeHandler(ErrorReport er, String region,
                              Map<String, String> avgiver) {
        super(er, region, avgiver);
    }

    public static void reset() {
        mapDublettJournalnummer = new TreeMap<String, List<String>>();
        mapDublettFodselsnummer = new TreeMap<String, List<String>>();
    }

    /**
     * Setter individets alder ved å finne forskjellen i antall år fra
     * fødselsdato til telledato
     *
     * @param fodselsDato
     * @param telleDato
     */
    public void setIndividAlder(DateTime fodselsDato, DateTime telleDato) {
        if (fodselsDato != null && telleDato != null) {
            Years years = Years.yearsBetween(fodselsDato, telleDato);
            this.individAlder = years.getYears();
        }
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
    void process(StructuredNode individ) {
        String saksbehandler = "";
        String journalnummer = "";
        String individId = "";
        String fodselsnummer = "";
        String fodselsnummerString = "";
        String refNr = "";
        String avgiverVersjon = "";
        String avslutta3112 = "";
        String bydelsnummer = "";

        try {
            // We can now conveniently query the sub-dom of each node
            // using XPATH:
            saksbehandler = individ.queryString("@Saksbehandler");
            journalnummer = individ.queryString("@Journalnummer");
            individId = "";
            fodselsnummerString = fodselsnummer = individ
                    .queryString("@Fodselsnummer");
            refNr = "";
            avgiverVersjon = avgiver.get("Versjon");
            avslutta3112 = individ.queryString("@Avslutta3112");

            // Oslo sender inn på alle bydeler, et individ skal ha muligheten
            // til å være registret i flere bydeler
            if (region.startsWith("0301")) {
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
                            "Kontrollprogram ",
                            journalnummer,
                            individId,
                            refNr,
                            "Individ Kontroll 01: Validering av individ",
                            "Definisjon av Individ er feil i forhold til filspesifikasjonen",
                            Constants.CRITICAL_ERROR), individ.getNode()
                            .getOwnerDocument(), "Individ.xsd");

            String tempVersjon = avgiverVersjon + "-12-31";
            telleDato = assignDateFromString(tempVersjon,
                    Constants.datoFormatLangt);
            forrigeTelleDato = telleDato.minusYears(1);
            fodselsDato = (fodselsnummer != null) ? assignDateFromString(
                    fodselsnummer.substring(0, 6),
                    no.ssb.kostra.control.Constants.datoFormatKort) : null;
            individStartDato = assignDateFromString(
                    individ.queryString("@StartDato"),
                    Constants.datoFormatLangt);
            individSluttDato = assignDateFromString(
                    individ.queryString("@SluttDato"),
                    Constants.datoFormatLangt);
            setIndividAlder(fodselsDato, telleDato);

            String individStartDatoString = (individStartDato != null) ? individStartDato
                    .toString(Constants.datoFormatKort) : "uoppgitt";
            String individSluttDatoString = (individSluttDato != null) ? individSluttDato
                    .toString(Constants.datoFormatKort) : "uoppgitt";
            String forrigeTelleDatoString = (forrigeTelleDato != null) ? forrigeTelleDato
                    .toString(Constants.datoFormatKort) : "uoppgitt";

            individId = (individ.queryString("@Id") != null && individ
                    .queryString("@Id").length() > 0) ? individ
                    .queryString("@Id") : "Uoppgitt";

            if (journalnummer != null) {
                er.incrementAntall();
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
                                    + individSluttDatoString + " ",
                            Constants.CRITICAL_ERROR), avslutta3112,
                    individSluttDato, telleDato);

            // Datokontroll 02e utgår fordi individstart sjekkes mot selv

            // TODO:HER
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

            if (region.startsWith("0301")) {
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
                    "Individet har ufullstendig fødselsnummer. Korriger fødselsnummer for individ med journalnummer" + journalnummer + ".");

            // Kontroller for Melding
            List<StructuredNode> meldingList = individ.queryNodeList("Melding");

            for (StructuredNode melding : meldingList) {
                String meldingId = (melding.queryString("@Id") != null && melding
                        .queryString("@Id").length() > 0) ? melding
                        .queryString("@Id") : "uoppgitt";
                DateTime meldingStartDato = assignDateFromString(
                        melding.queryString("@StartDato"),
                        Constants.datoFormatLangt);
                DateTime meldingSluttDato = assignDateFromString(
                        melding.queryString("@SluttDato"),
                        Constants.datoFormatLangt);
                String meldingStartDatoString = (meldingStartDato != null) ? meldingStartDato
                        .toString(Constants.datoFormatKort) : "uoppgitt";
                String meldingSluttDatoString = (meldingSluttDato != null) ? meldingSluttDato
                        .toString(Constants.datoFormatKort) : "uoppgitt";
                String henlagt = "1";
                String meldingKonklusjon = melding.queryString("@Konklusjon");
                String meldingKonklusjonString = (meldingKonklusjon != null) ? meldingKonklusjon
                        : "uoppgitt";

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
                                Constants.CRITICAL_ERROR), melding, "Melder",
                        "exceptionMelding");

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
                        "Saksinnhold", "exceptionMelding");

                List<StructuredNode> melderList = melding
                        .queryNodeList("Melder");

                // Kontroller for Melder
                if (meldingSluttDato.isAfter(forrigeTelleDato)) {
                    if (melderList != null) {
                        for (StructuredNode melder : melderList) {
                            // 22 = Andre offentlige instanser
                            // 23 = Andre
                            String[] koder = {"22", "23"};
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
                                            "Melder Kontroll 2: Kontroll av kode og presisering",
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
                            String[] koder = {"18", "19"};
                            String saksinnholdKode = (saksinnhold
                                    .queryString("@Kode") != null && saksinnhold
                                    .queryString("@Kode").length() > 0) ? saksinnhold
                                    .queryString("@Kode") : "";
                            String saksinnholdKodeString = (saksinnholdKode
                                    .length() > 0) ? saksinnholdKode
                                    : "uoppgitt";

                            controlPresisering(
                                    er,
                                    new ErrorReportEntry(
                                            saksbehandler,
                                            journalnummer,
                                            individId,
                                            refNr,
                                            "Saksinnhold Kontroll 2: Kontroll av kode og presisering",
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
                    String undersokelseId = (undersokelse.queryString("@Id") != null && undersokelse
                            .queryString("@Id").length() > 0) ? undersokelse
                            .queryString("@Id") : "uoppgitt";
                    DateTime undersokelseStartDato = assignDateFromString(
                            undersokelse.queryString("@StartDato"),
                            Constants.datoFormatLangt);
                    DateTime undersokelseSluttDato = assignDateFromString(
                            undersokelse.queryString("@SluttDato"),
                            Constants.datoFormatLangt);
                    String undersokelseStartDatoString = (undersokelseStartDato != null) ? undersokelseStartDato
                            .toString(Constants.datoFormatKort) : "uoppgitt";
                    String undersokelseSluttDatoString = (undersokelseSluttDato != null) ? undersokelseSluttDato
                            .toString(Constants.datoFormatKort) : "uoppgitt";
                    String undersokelseKonklusjon = (undersokelse
                            .queryString("@Konklusjon") != null && undersokelse
                            .queryString("@Konklusjon").length() > 0) ? undersokelse
                            .queryString("@Konklusjon") : "";
                    String undersokelsePresisering = (undersokelse
                            .queryString("Presisering") != null && undersokelse
                            .queryString("Presisering").length() > 0) ? undersokelse
                            .queryString("Presisering") : "";

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
                                            + forrigeTelleDatoString + ")",
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
                                    "Undersøkelse Kontroll 2d: Avslutta 31 12 medfører at sluttdato skal være satt på",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Individet er avsluttet hos barnevernet og dets undersøkelser skal dermed være avsluttet. Sluttdato er "
                                            + undersokelseStartDatoString + "",
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
                    String[] koderPresisering = {"5"};
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

                    controlTidManederUker(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 5: Behandlingstid for undersøkelse",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Fristoverskridelse, behandlingstid over 3 måneder for undersøkelse, "
                                            + meldingSluttDatoString + " -> "
                                            + undersokelseSluttDatoString,
                                    Constants.NORMAL_ERROR), meldingStartDato,
                            undersokelseSluttDato, 3, 1);

                    controlTidManederUker(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 6: Fristoverskridelse for undersøkelse",
                                    "Undersøkelse ("
                                            + undersokelseId
                                            + "). Fristoverskridelse, behandlingstid over 6 måneder for undersøkelse, "
                                            + meldingSluttDatoString + " -> "
                                            + undersokelseSluttDatoString,
                                    Constants.NORMAL_ERROR), meldingStartDato,
                            undersokelseSluttDato, 6, 1);

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

                    List<StructuredNode> vedtaksgrunnlagList = undersokelse
                            .queryNodeList("Vedtaksgrunnlag");

                    // 1 = Barneverntjenesten fatter vedtak om tiltak
                    // 2 = Begjæring om tiltak for fylkesnemnda
                    String[] koderKonklusjon = {"1", "2"};
                    controlFeltMedKoderSkalHaUndernoder(
                            er,
                            new ErrorReportEntry(
                                    saksbehandler,
                                    journalnummer,
                                    individId,
                                    refNr,
                                    "Undersøkelse Kontroll 7: Konkludert undersøkelse skal ha vedtaksgrunnlag",
                                    "Undersøkelse konkludert med kode "
                                            + undersokelseKonklusjon
                                            + " skal ha vedtaksgrunnlag",
                                    Constants.CRITICAL_ERROR),
                            undersokelseKonklusjon, koderKonklusjon,
                            vedtaksgrunnlagList);

                    // Kontroller for Vedtaksgrunnlag
                    if (meldingSluttDato.isAfter(forrigeTelleDato)) {
                        for (StructuredNode vedtaksgrunnlag : vedtaksgrunnlagList) {
                            // 18 = Andre forhold ved foreldre/familien
                            // 19 = Andre forhold ved barnets situasjon
                            String[] kodelisteVedtaksgrunnlag = {"18", "19"};
                            String vedtaksgrunnlagKode = (vedtaksgrunnlag
                                    .queryString("@Kode") != null && vedtaksgrunnlag
                                    .queryString("@Kode").length() > 0) ? vedtaksgrunnlag
                                    .queryString("@Kode") : "";
                            String vedtaksgrunnlagPresisering = (vedtaksgrunnlag
                                    .queryString("Presisering") != null && vedtaksgrunnlag
                                    .queryString("Presisering").length() > 0) ? vedtaksgrunnlag
                                    .queryString("Presisering") : "";

                            controlPresisering(
                                    er,
                                    new ErrorReportEntry(
                                            saksbehandler,
                                            journalnummer,
                                            individId,
                                            refNr,
                                            "Vedtaksgrunnlag Kontroll 2: Kontroll av kode og presisering",
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
                String planId = (plan.queryString("@Id") != null && plan
                        .queryString("@Id").length() > 0) ? plan
                        .queryString("@Id") : "uoppgitt";

                DateTime planStartDato = assignDateFromString(
                        plan.queryString("@StartDato"),
                        Constants.datoFormatLangt);
                DateTime planSluttDato = assignDateFromString(
                        plan.queryString("@SluttDato"),
                        Constants.datoFormatLangt);
                String planStartDatoString = (planStartDato != null) ? planStartDato
                        .toString(Constants.datoFormatKort) : "uoppgitt";
                String planSluttDatoString = (planSluttDato != null) ? planSluttDato
                        .toString(Constants.datoFormatKort) : "uoppgitt";

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
                                        + forrigeTelleDatoString + ")",
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
            Collections.sort(tiltakList, new ComparatorNodeByStartDato());

            for (StructuredNode tiltak : tiltakList) {
                String tiltakId = (tiltak.queryString("@Id") != null && tiltak
                        .queryString("@Id").length() > 0) ? tiltak
                        .queryString("@Id") : "uoppgitt";
                DateTime tiltakStartDato = assignDateFromString(
                        tiltak.queryString("@StartDato"),
                        Constants.datoFormatLangt);
                DateTime tiltakSluttDato = assignDateFromString(
                        tiltak.queryString("@SluttDato"),
                        Constants.datoFormatLangt);
                String tiltakStartDatoString = (tiltakStartDato != null) ? tiltakStartDato
                        .toString(Constants.datoFormatKort) : "uoppgitt";
                String tiltakSluttDatoString = (tiltakSluttDato != null) ? tiltakSluttDato
                        .toString(Constants.datoFormatKort) : "uoppgitt";
                String tiltakKategoriKode = (tiltak
                        .queryString("Kategori/@Kode") != null && tiltak
                        .queryString("Kategori/@Kode").length() > 0) ? tiltak
                        .queryString("Kategori/@Kode") : "";
                String[] kodelisteKategoriKode = {"1.99", "2.99", "3.7",
                        "3.99", "4.99", "5.99", "6.99", "7.99", "8.99"};
                String tiltakKategoriPresisering = (tiltak
                        .queryString("Kategori/Presisering") != null && tiltak
                        .queryString("Kategori/Presisering").length() > 0) ? tiltak
                        .queryString("Kategori/Presisering") : "";
                String tiltakOpphevelseKode = (tiltak
                        .queryString("Opphevelse/@Kode") != null && tiltak
                        .queryString("Opphevelse/@Kode").length() > 0) ? tiltak
                        .queryString("Opphevelse/@Kode") : "";
                String[] kodelisteOpphevelseKode = {"4"};
                String tiltakOpphevelsePresisering = (tiltak
                        .queryString("Opphevelse/Presisering") != null && tiltak
                        .queryString("Opphevelse/Presisering").length() > 0) ? tiltak
                        .queryString("Opphevelse/Presisering") : "";

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
                                        + forrigeTelleDatoString + ")",
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

                // TODO: ekstra sjekk mot kravspek
                controlAlderOverAldersgrenseSkalHaUndernoder(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 3: Individ over 18 år skal ha tiltak",
                                "Tiltak ("
                                        + tiltakId
                                        + "). Individ som er 18 år eller eldre skal ha tiltak",
                                Constants.NORMAL_ERROR),
                        this.getIndividAlder(), 18, tiltakList);

                controlTiltakOmsorgstiltakPresisering(
                        er,
                        new ErrorReportEntry(
                                saksbehandler,
                                journalnummer,
                                individId,
                                refNr,
                                "Tiltak Kontroll 4: Omsorgstiltak med sluttdato krever årsak til opphevelse",
                                "Tiltak (" + tiltakId
                                        + "). Omsorgstiltak med sluttdato ("
                                        + tiltakSluttDatoString
                                        + ") krever årsak til opphevelse",
                                Constants.NORMAL_ERROR), tiltak);

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
                                        + "). Barnet er over 11 år og i SFO",
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
                List<StructuredNode> lovhjemmelList = new ArrayList<StructuredNode>();
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
                                        "Lovhjemmel Kontroll 2: omsorgstiltak med sluttdato krever årsak til opphevelse",
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

            String[] kodelistePlasseringsTiltak = {"1.1", "1.2", "1.99", "2.1", "2.2",
                    "2.3", "2.4", "2.5", "2.99", "8.2"};

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
                    kodelistePlasseringsTiltak, 3, telleDato,
                    "Uhåndterlig feil i forbindelse med tiltak");

        } catch (XPathExpressionException e) {
            e.printStackTrace();

        } catch (NullPointerException e) {
            e.printStackTrace();
            er.addEntry(new ErrorReportEntry("Kontrollprogram", journalnummer,
                    individId, refNr, "Individ K1: Feil for individet", e
                    .getMessage(), Constants.CRITICAL_ERROR));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sjekker om tekststreng ikke eksisterer eller har innhold. Returnérer true
     * hvis den ikke fins eller har innhold, ellers false samt at
     * ErrorReportEntry legges til i ErrorReport
     *
     * @param er
     * @param ere
     * @param val1
     * @return boolean
     */
    public boolean controlUndefinedOrHasLength(ErrorReport er,
                                               ErrorReportEntry ere, String val1) {
        if (val1 == null || !val1.isEmpty()) {
            return true;
        } else {
            er.addEntry(ere);
            return false;
        }
    }

    /**
     * Kontrollerer at en dato er etter en annen dato. Hvis dato1 er etter
     * dato2, returnéres false samt at ErrorReportEntry legges til i
     * ErrorReport, ellers returneres true
     *
     * @param er
     * @param ere
     * @param dato1
     * @param dato2
     * @return boolean
     */
    public boolean controlDatoEtterDato(ErrorReport er, ErrorReportEntry ere,
                                        DateTime dato1, DateTime dato2) {
        if (dato1 != null && dato2 != null && dato1.isAfter(dato2)) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at fødselsnummer er korrekt. Hvis fødselnummer mangler
     * sjekkes DUF-nummer om det er korrekt. Ved feil eller at begge mangler
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param fodselsnummer
     * @param duFnummer
     * @return boolean
     */
    public boolean controlFodselsnummerOgDUFnummer(ErrorReport er,
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
                return false;
            }

        } else if (duFnummer != null) {
            Pattern p = Pattern.compile("^[0-9]{12}$");
            Matcher m = p.matcher(duFnummer);

            if (!m.matches()) {
                ere.setErrorText(errorTextDUFnummer);
                er.addEntry(ere);
                return false;

            }
        } else {
            ere.setErrorText(errorTextMangler);
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at fødselsnummer er korrekt. Ved feil eller mangler
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param fodselsnummer
     * @return boolean
     */
    public boolean controlFodselsnummer(ErrorReport er,
                                        ErrorReportEntry ere, String fodselsnummer,
                                        String errorTextFodselsnummer) {
        if (fodselsnummer == null || (!(DatoFnr.validNorwId(fodselsnummer) == 1) || fodselsnummer.endsWith("55555"))) {
            ere.setErrorText(errorTextFodselsnummer);
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at et fødselnummer kun forekommer én gang i filutrekket. Ved
     * feil lages en liste hvor fodselnummeret forekommer, returneres false samt
     * at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param fodselsnummer
     * @return boolean
     */
    public boolean controlDublettFodselsnummer(ErrorReport er,
                                               ErrorReportEntry ere, String fodselsnummer) {
        List<String> l = new ArrayList<String>();
        boolean flag = true;

        try {
            synchronized (this) {
                if (fodselsnummer != null) {
                    if (!fodselsnummer.endsWith("00100")
                            && !fodselsnummer.endsWith("00200")
                            && !fodselsnummer.endsWith("55555")
                            && !fodselsnummer.endsWith("99999")) {

                        if (mapDublettFodselsnummer.containsKey(fodselsnummer)) {
                            l = mapDublettFodselsnummer.get(fodselsnummer);
                            StringBuffer concatinatedErrorText = new StringBuffer();
                            concatinatedErrorText.append(ere.getErrorText());
                            concatinatedErrorText
                                    .append("Fins også i følgende journaler: ");

                            for (String element : l) {
                                concatinatedErrorText.append(element);
                            }

                            ere.setErrorText(concatinatedErrorText.toString());
                            er.addEntry(ere);
                            flag = false;
                        }
                        l.add(ere.getJournalnummer());
                        mapDublettFodselsnummer.put(fodselsnummer, l);
                    }
                }
            }
        } catch (NullPointerException e) {
            ere.setErrorText("NullPointerException !!");
            er.addEntry(ere);
            return false;
        }

        return flag;
    }

    /**
     * Kontrollerer at et journalnummer kun forekommer én gang i filutrekket.
     * Ved feil lages en liste hvor journalnummeret forekommer, returneres false
     * samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param fodselsnummer
     * @return boolean
     */
    public boolean controlDublettJournalnummer(ErrorReport er,
                                               ErrorReportEntry ere, String fodselsnummer) {
        List<String> l = new ArrayList<String>();
        boolean flag = true;
        try {

            if (fodselsnummer != null) {
                if (mapDublettJournalnummer.containsKey(ere.getJournalnummer())) {
                    l = mapDublettJournalnummer.get(ere.getJournalnummer());
                    StringBuffer concatinatedErrorText = new StringBuffer();
                    concatinatedErrorText.append(ere.getErrorText());
                    concatinatedErrorText
                            .append("Fins i følgende personnummer: ");

                    for (String element : l) {
                        concatinatedErrorText.append(element);
                    }
                    ere.setErrorText(concatinatedErrorText.toString());
                    er.addEntry(ere);
                    flag = false;
                }

                l.add(fodselsnummer.substring(0, 4).concat("*******"));
                mapDublettJournalnummer.put(ere.getJournalnummer(), l);
            }

        } catch (NullPointerException e) {
            ere.setErrorText("NullPointerException !!");
            er.addEntry(ere);
            return false;
        }

        return flag;
    }

    /**
     * Kontrollerer at listen har innhold, hvis den er tom returneres false samt
     * at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param list
     * @return boolean
     */
    public boolean controlHarInnhold(ErrorReport er, ErrorReportEntry ere,
                                     List<StructuredNode> list) {
        if (list.isEmpty()) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at hvis felt1 har innhold skal også felt2 ha innhold, ellers
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param felt1
     * @param felt2
     * @return boolean
     */
    public boolean controlHarInnhold(ErrorReport er, ErrorReportEntry ere,
                                     String felt1, String felt2) {
        if (felt1 != null && !felt1.isEmpty()) {
            if (felt2 == null || felt2.isEmpty()) {
                er.addEntry(ere);
                return false;
            }
        }

        return true;
    }

    /**
     * Kontrollerer at alder på klient er 23 år eller yngre. Hvis alder er over
     * 23 år returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param alder
     * @return boolean
     */
    public boolean controlAlder(ErrorReport er, ErrorReportEntry ere, int alder) {
        if (23 < alder) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at et tidsspenn mellom dato1 og dato2 er innenfor
     * antallKalenderDager, ellers returneres false samt at ErrorReportEntry
     * legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param dato1
     * @param dato2
     * @param antallKalenderDager
     * @return boolean
     */
    public boolean controlTidDager(ErrorReport er, ErrorReportEntry ere,
                                   DateTime dato1, DateTime dato2, int antallKalenderDager) {
        // hvis dato1 ikke er blank, dvs. har innhold/dato
        if (dato1 != null) {
            // Finner dato for frist
            DateTime frist = dato1.plusDays(antallKalenderDager);

            // hvis dato2 ikke er blank og er etter fristen
            if (dato2 != null && dato2.isAfter(frist)) {
                // legg til feilmelding
                er.addEntry(ere);
                return false;
            }
        }

        // alt gikk bra, ellers så manglet det informasjon som gjør kontrollen
        // ikke lar seg kjøre
        return true;
    }

    /**
     * Kontrollerer at et tidsspenn mellom dato1 og dato2 er innenfor
     * antallMaaneder + antallUker, ellers returneres false samt at
     * ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param dato1
     * @param dato2
     * @param antallMaaneder
     * @param antallUker
     * @return boolean
     */
    public boolean controlTidManederUker(ErrorReport er, ErrorReportEntry ere,
                                         DateTime dato1, DateTime dato2, int antallMaaneder, int antallUker) {

        // hvis dato1 ikke er blank, dvs. har innhold/dato
        if (dato1 != null) {
            // Finner dato for frist
            DateTime frist = dato1.plusMonths(antallMaaneder).plusWeeks(
                    antallUker);

            // hvis dato2 ikke er blank og er etter fristen
            if (dato2 != null && dato2.isAfter(frist)) {
                // legg til feilmelding
                er.addEntry(ere);
                return false;
            }
        }
        return true;
    }

    /**
     * Kontrollerer at et tidsspenn mellom dato1 og (dato2 eller telledato) er innenfor
     * antallMaaneder, ellers returneres false samt at ErrorReportEntry legges
     * til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param dato1
     * @param dato2
     * @param antallMaaneder
     * @return boolean
     */
    public boolean controlTidManeder(ErrorReport er, ErrorReportEntry ere,
                                     DateTime dato1, DateTime dato2, DateTime telledato, int antallMaaneder) {
        // hvis dato1 ikke er blank, dvs. har innhold/dato
        if (dato1 != null) {
            // Finner dato for frist
            DateTime frist = dato1.plusMonths(antallMaaneder);

            // hvis dato2 er blank så sett dato2 (f.eks sluttDato) til telledato (31.12)
            if (dato2 == null) {
                dato2 = telledato;
            }

            // hvis dato2 ikke er blank og er etter fristen
            if (dato2 != null && dato2.isAfter(frist)) {
                // legg til feilmelding
                er.addEntry(ere);
                return false;
            }
        }
        return true;
    }

    /**
     * Kontrollerer at et tidsspenn mellom dato1 og dato2 er innenfor antallAar,
     * ellers returneres false samt at ErrorReportEntry legges til i
     * ErrorReport.
     *
     * @param er
     * @param ere
     * @param dato1
     * @param dato2
     * @param antallAar
     * @return boolean
     */
    public boolean controlTidAar(ErrorReport er, ErrorReportEntry ere,
                                 DateTime dato1, DateTime dato2, int antallAar) {
        // hvis dato1 ikke er blank, dvs. har innhold/dato
        if (dato1 != null) {
            // Finner dato for frist
            DateTime frist = dato1.plusYears(antallAar);

            // hvis dato2 ikke er blank og er etter fristen
            if (dato2 != null && dato2.isAfter(frist)) {
                // legg til feilmelding
                er.addEntry(ere);
                return false;
            }
        }

        return true;
    }

    /**
     * Kontrollerer at hvis alder er over aldersgrense så skal listen ha
     * innhold, ellers returneres false samt at ErrorReportEntry legges til i
     * ErrorReport.
     *
     * @param er
     * @param ere
     * @param alder
     * @param aldersgrense
     * @param list
     * @return boolean
     */
    public boolean controlAlderOverAldersgrenseSkalHaUndernoder(ErrorReport er,
                                                                ErrorReportEntry ere, int alder, int aldersgrense,
                                                                List<StructuredNode> list) {
        if (aldersgrense < alder) {
            if (list.isEmpty()) {
                er.addEntry(ere);
                return false;
            }
        }

        return true;
    }

    /**
     * Kontrollerer at hvis kode er i kodeliste så skal list ha innhold, ellers
     * returneres false samt at ErrorReportEntry legges til i ErrorReport.
     *
     * @param er
     * @param ere
     * @param kode
     * @param kodeliste
     * @param list
     * @return boolean
     */
    public boolean controlFeltMedKoderSkalHaUndernoder(ErrorReport er,
                                                       ErrorReportEntry ere, String kode, String[] kodeliste,
                                                       List<StructuredNode> list) {
        try {
            if (erKodeIKodeliste(kode, kodeliste) && list.isEmpty()) {
                er.addEntry(ere);
                return false;
            }

        } catch (Exception e) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at konkluderte meldinger har gitt undernode (kan f.eks være
     * Melder eller Saksinnhold)
     *
     * @param er
     * @param ere
     * @param melding
     * @param exceptionMelding
     * @return boolean
     */
    public boolean controlKonkludertMeldingUnderNoder(ErrorReport er,
                                                      ErrorReportEntry ere, StructuredNode melding, String underNode,
                                                      String exceptionMelding) {
        try {
            //
            // Kode for konklusjon
            String konklusjon = melding.queryString("@Konklusjon");
            String[] kodelisteKonklusjon = {"1", "2"};

            // Sluttdato for Melding
            DateTime sluttDato = assignDateFromString(
                    melding.queryString("@SluttDato"),
                    Constants.datoFormatLangt);
            List<StructuredNode> list = melding.queryNodeList(underNode);

            if (forrigeTelleDato.getYear() < sluttDato.getYear()
                    && erKodeIKodeliste(konklusjon, kodelisteKonklusjon)
                    && sluttDato != null && list.isEmpty()) {
                er.addEntry(ere);
                return false;
            }

        } catch (XPathExpressionException e) {
            ere.setErrorText(exceptionMelding);
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at undersøkelser som er startet før 1. juli skal være
     * konkludert
     *
     * @param er
     * @param ere
     * @param versjon
     * @param startDato
     * @param sluttDato
     * @return boolean
     */
    public boolean controlUndersokelseStartetTidligereEnn1JuliUtenKonklusjon(
            ErrorReport er, ErrorReportEntry ere, String versjon,
            DateTime startDato, DateTime sluttDato) {
        String fristDatoString = versjon + "-07-01";
        DateTime frist = assignDateFromString(fristDatoString,
                Constants.datoFormatLangt);

        if (startDato.isBefore(frist) && sluttDato == null) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at alle omsorgstiltak skal ha presisering
     *
     * @param er
     * @param ere
     * @param tiltak
     * @return boolean
     */
    public boolean controlTiltakOmsorgstiltakPresisering(ErrorReport er,
                                                         ErrorReportEntry ere, StructuredNode tiltak) {
        try {
            List<StructuredNode> lovhjemmelList = tiltak
                    .queryNodeList("JmfrLovhjemmel");
            lovhjemmelList.add(tiltak.queryNode("Lovhjemmel"));

            boolean bool = false;

            for (StructuredNode lovhjemmel : lovhjemmelList) {
                String kapittel = lovhjemmel.queryString("Kapittel");
                String paragraf = lovhjemmel.queryString("Paragraf");
                String ledd = lovhjemmel.queryString("Ledd");
                String opphevelse = tiltak
                        .queryString("Opphevelse/Presisering");

                if (kapittel == "4" && (paragraf == "12")
                        || (paragraf == "8" && (ledd == "2" || ledd == "3"))) {
                    if (opphevelse != null && opphevelse.length() > 0) {
                        bool = true;
                    }
                }
            }

            if (bool == true) {
                er.addEntry(ere);
                return false;
            }

        } catch (XPathExpressionException e) {
            // No need to handle exception
        }

        return true;
    }

    /**
     * Kontrollerer at alle omsorgstiltak skal ha sluttdato
     *
     * @param er
     * @param ere
     * @param SluttDato
     * @param kapittel
     * @param paragraf
     * @param ledd
     * @param tiltakOpphevelseKode
     * @param kodelisteOpphevelseKode
     * @param opphevelse
     * @return boolean
     */
    public boolean controlTiltakLovhjemmelOmsorgstiltakSluttDato(
            ErrorReport er, ErrorReportEntry ere, DateTime SluttDato,
            String kapittel, String paragraf, String ledd,
            String tiltakOpphevelseKode, String[] kodelisteOpphevelseKode,
            String opphevelse) {
        try {
            if (kapittel.equalsIgnoreCase("4")
                    && (paragraf.equalsIgnoreCase("12"))
                    || (paragraf.equalsIgnoreCase("8") && (ledd
                    .equalsIgnoreCase("2") || ledd
                    .equalsIgnoreCase("3")))) {
                if (SluttDato != null
                        && (opphevelse == null || opphevelse.length() == 0)) {
                    for (String kode : kodelisteOpphevelseKode) {
                        if (tiltakOpphevelseKode.equalsIgnoreCase(kode)) {
                            er.addEntry(ere);
                            return false;
                        }
                    }
                }
            }

        } catch (NullPointerException e) {
            // No need to handle exception
        }

        return true;
    }

    /**
     * Kontrollerer at Lovhjemmel sine koder for kapittel og paragraf ikke
     * starter med 0
     *
     * @param er
     * @param ere
     * @param kapittel
     * @param paragraf
     * @return boolean
     */
    public boolean controlTiltakLovhjemmel(ErrorReport er,
                                           ErrorReportEntry ere, String kapittel, String paragraf) {
        try {
            if (kapittel.startsWith("0") || paragraf.startsWith("0")) {
                er.addEntry(ere);
                return false;
            }

        } catch (NullPointerException e) {
            // No need to handle exception
        }

        return true;
    }

    /**
     * Kontrollerer at overlappende plasseringstiltak ikke overlapper med mer
     * enn 3 måneder
     *
     * @param er
     * @param ere
     * @param tiltakList
     * @param plasseringstiltakKodeList
     * @param antallMaaneder
     * @param defaultSluttDato
     * @param exceptionMelding
     * @return boolean
     */
    public boolean controlFlerePlasseringstiltakISammePeriode(ErrorReport er,
                                                              ErrorReportEntry ere, List<StructuredNode> tiltakList,
                                                              String[] plasseringstiltakKodeList, int antallMaaneder,
                                                              DateTime defaultSluttDato, String exceptionMelding) {
        List<StructuredNode> plasseringstiltakList = new ArrayList<StructuredNode>();
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

                for (int i = 1; i < plasseringstiltakList.size(); i++) {
                    StructuredNode first = plasseringstiltakList.get(i - 1);
                    String firstId = "";
                    StructuredNode second = plasseringstiltakList.get(i);
                    String secondId = "";

                    try {
                        firstId = first.queryString("@Id");
                        secondId = second.queryString("@Id");

                        DateTime firstStartDateTime = first
                                .assignDateFromString(
                                        first.queryString("@StartDato"),
                                        Constants.datoFormatLangt);

                        DateTime firstSluttDateTime = first
                                .assignDateFromString(
                                        first.queryString("@SluttDato"),
                                        Constants.datoFormatLangt);

                        DateTime secondStartDateTime = second
                                .assignDateFromString(
                                        second.queryString("@StartDato"),
                                        Constants.datoFormatLangt);

                        if (firstStartDateTime != null
                                && firstSluttDateTime != null
                                && secondStartDateTime != null
                                && firstStartDateTime
                                .isBefore(firstSluttDateTime
                                        .minusMonths(antallMaaneder))
                                // periode for first må være mer enn 3 mnd for å
                                // at det skal kunne bli 3 mnd med overlapp
                                && secondStartDateTime
                                .isAfter(firstStartDateTime)
                                // andres startdato skal etter førstes startdato
                                && secondStartDateTime
                                .isBefore(firstSluttDateTime
                                        .minusMonths(antallMaaneder))
                            // OG tre måneder før førstes sluttdato -> medfører 3
                            // måneder eller mer overlapp
                                ) {
                            String feilmelding = "Plasseringstiltak "
                                    + firstId
                                    + " med sluttdato "
                                    + firstSluttDateTime.toString("dd.MM.yyyy")
                                    + " er mer enn 3 måneder etter "
                                    + secondId
                                    + " med startdato "
                                    + secondStartDateTime
                                    .toString("dd.MM.yyyy")
                                    + ". Dette gir en overlapp på mer enn 3 måneder.";
                            ere.setErrorText(feilmelding);
                            er.addEntry(ere);
                            return false;
                        }

                    } catch (XPathExpressionException e) {
                        ere.setErrorText(exceptionMelding);
                        er.addEntry(ere);
                        return false;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    /**
     * Kontroller at klienter over 18 ikke er på omsorgstiltak
     *
     * @param er
     * @param ere
     * @param kapittel
     * @param paragraf
     * @param ledd
     * @param alder
     * @return boolean
     */
    public boolean controlTiltakLovhjemmelOver18OgPaOmsorgstiltak(
            ErrorReport er, ErrorReportEntry ere, String kapittel,
            String paragraf, String ledd, int alder) {
        try {
            if (alder > 18
                    && (kapittel.equalsIgnoreCase("4")
                    && (paragraf.equalsIgnoreCase("12")) || (paragraf
                    .equalsIgnoreCase("8") && (ledd
                    .equalsIgnoreCase("2") || ledd
                    .equalsIgnoreCase("3"))))) {
                er.addEntry(ere);
                return false;
            }

        } catch (NullPointerException e) {
            // No need to handle exception
        }

        return true;
    }

    /**
     * Kontroller at barn over 7 ikke er i barnehage
     *
     * @param er
     * @param ere
     * @param kode
     * @param alder
     * @return boolean
     */
    public boolean controlOver7OgIBarnehage(ErrorReport er,
                                            ErrorReportEntry ere, String kode, int alder) {
        if (alder > 7 && kode.equalsIgnoreCase("4.1")) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontroller at barn over 11 år ikke er i SFO
     *
     * @param er
     * @param ere
     * @param kode
     * @param alder
     * @return boolean
     */
    public boolean controlOver11OgISFO(ErrorReport er, ErrorReportEntry ere,
                                       String kode, int alder) {
        if (alder > 11 && kode.equalsIgnoreCase("4.2")) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    // TODO: FEILER, lag tester individ 9 og 10

    /**
     * Kontrollerer at sluttdato er satt
     *
     * @param er
     * @param ere
     * @param kode
     * @param sluttDato
     * @param frist
     * @return boolean
     */
    public boolean controlAvslutta3112(ErrorReport er, ErrorReportEntry ere,
                                       String kode, DateTime sluttDato, DateTime frist) {
        try {
            if (kode.equalsIgnoreCase("1")) {
                if (sluttDato == null || frist == null
                        || sluttDato.isAfter(frist)) {
                    er.addEntry(ere);
                    return false;
                }
            }
        } catch (NullPointerException e) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }

    /**
     * Kontrollerer at kombinasjonen elementType og id er unik
     *
     * @param er
     * @param ere
     * @param elementType
     * @param id
     * @return boolean
     */
    public boolean controlUniqueID(ErrorReport er, ErrorReportEntry ere,
                                   String elementType, String id) {
        try {
            synchronized (this) {
                if (elementType == null) {
                    elementType = "null";
                }

                if (id == null) {
                    id = "null";
                }

                String key = elementType + id;

                if (mapUniqueID.containsKey(key)) {
                    Integer value = mapUniqueID.get(key);
                    value++;
                    mapUniqueID.put(key, value);
                    er.addEntry(ere);
                    return false;
                }

                mapUniqueID.put(key, 1);
                return true;
            }

        } catch (NullPointerException e) {
            ere.setErrorText("NullPointerException !!");
            er.addEntry(ere);
            return false;
        }
    }
}
