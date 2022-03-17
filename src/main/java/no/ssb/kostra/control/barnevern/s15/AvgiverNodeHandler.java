package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.controlprogram.Arguments;

import javax.xml.xpath.XPathExpressionException;

@SuppressWarnings("SpellCheckingInspection")
public class AvgiverNodeHandler extends NodeHandler {
    public AvgiverNodeHandler(
            ErrorReport errorReport,
            Arguments args) {
        super(errorReport, args);
    }

    /**
     * <pre>
     * Prosesserer/kontrollerer et Avgiver-nodetre ved at man: <br/>
     * - Avgiver K1: Validering av avgiver
     *   - Kontrollerer mot filbeskrivelse for avgiver, Avgiver.xsd
     * - Avgiver K2: Årgang
     *   - Kontrollerer at årgangen for filutrekket er for gjeldende rapporteringsår
     * - Avgiver K3: Organisasjonnummer
     *   - Kontrollerer at filen har organisasjonsnummer for avgiver
     * - Avgiver K4: Kommunenummer
     *   - Kontrollerer at kommunenummer i webskjemaet likt med tilsvarende i filutrekket
     * - Avgiver K5: Gyldig kommunenummer
     *   - Kontrollerer at kommunenummeret fins i liste over gyldige kommunenumre som er med i denne rapporteringen
     * - Avgiver K6: Kommunenavn
     *   - Kontrollerer at kommunenavn fins i filen
     * </pre>
     *
     * @param node StructuredNode som er en Avgiver-node
     */
    @Override
    public void process(final StructuredNode node) {
        try {
            /*
             * <pre>
             * - Avgiver K1: Validering av avgiver
             *   - Kontrollerer mot filbeskrivelse for avgiver, Avgiver.xsd
             * </pre>
             */
            controlValidateByXSD(errorReport, new ErrorReportEntry(" ",
                    " ", " ", " ", "Avgiver K1: Validering av avgiver",
                    "Klarer ikke å validere Avgiver mot filspesifikasjon",
                    Constants.CRITICAL_ERROR), node.getNode()
                    .getOwnerDocument(), "Avgiver.xsd");

            /*
             * <pre>
             * - Avgiver K2: Årgang
             *   - Kontrollerer at årgangen for filutrekket er for gjeldende rapporteringsår
             * </pre>
             */
            controlEquals(errorReport, new ErrorReportEntry(" ", "", " ", " ",
                            "Avgiver K2: Årgang",
                            "Filen inneholder feil rapporteringsår (" + node.queryString("@Versjon") + "), forventet " + args.getAargang() + ".",
                            Constants.CRITICAL_ERROR), node.queryString("@Versjon"),
                    args.getAargang());

            /*
             * <pre>
             * - Avgiver K3: Organisasjonnummer
             *   - Kontrollerer at filen har organisasjonsnummer for avgiver
             * </pre>
             */
            controlOrgnr(errorReport, new ErrorReportEntry(" ", " ", " ", " ",
                            "Avgiver K3: Organisasjonnummer",
                            "Filen mangler organisasjonsnummer. Oppgitt organisasjonsnummer er '" + node.queryString("@Organisasjonsnummer").trim() + "'",
                            Constants.CRITICAL_ERROR),
                    node.queryString("@Organisasjonsnummer").trim());

            /*
             * <pre>
             * - Avgiver K4: Kommunenummer
             *   - Kontrollerer at kommunenummer i webskjemaet likt med tilsvarende i filutrekket
             * </pre>
             */
            controlEquals(
                    errorReport,
                    new ErrorReportEntry(
                            " ",
                            " ",
                            " ",
                            " ",
                            "Avgiver K4: Kommunenummer",
                            "Filen inneholder feil kommunenummer. Forskjellig kommunenummer i skjema og filuttrekk."
                                    + node.queryString("@Kommunenummer")
                                    + " : " + args.getRegion().substring(0, 4),
                            Constants.CRITICAL_ERROR),
                    node.queryString("@Kommunenummer"), args.getRegion().substring(0, 4));


            /*
             * <pre>
             * - Avgiver K6: Kommunenavn
             *   - Kontrollerer at kommunenavn fins i filen
             * </pre>
             */
            controlExistsAndHasLength(errorReport, new ErrorReportEntry(" ", " ", " ", " ",
                    "Avgiver K6: Kommunenavn", "Filen mangler kommunenavn.",
                    Constants.CRITICAL_ERROR), node.queryString("@Kommunenavn").trim());

        } catch (XPathExpressionException e) {
            errorReport.addEntry(
                    new ErrorReportEntry(
                            " ",
                            " ",
                            " ",
                            " ",
                            "Kontrollprogrammet",
                            "Klarer ikke å lese fil. Feil i Avgiver. Får feilmeldingen: " + e.getMessage(),
                            Constants.CRITICAL_ERROR)
            );
        }
    }
}
