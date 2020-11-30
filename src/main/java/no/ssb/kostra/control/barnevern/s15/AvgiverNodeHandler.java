package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.controlprogram.Arguments;

import javax.xml.xpath.XPathExpressionException;
import java.util.Map;

public class AvgiverNodeHandler extends NodeHandler {
	public AvgiverNodeHandler(ErrorReport er, Arguments args) {
		super(er, args);
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
	 * @param node
	 *            StructuredNode som er en Avgiver-node
	 */
	@Override
	public void process(StructuredNode node) {
		try {
			/**
			 * <pre>
			 * - Avgiver K1: Validering av avgiver
			 *   - Kontrollerer mot filbeskrivelse for avgiver, Avgiver.xsd
			 * </pre>
			 */
			controlValidateByXSD(er, new ErrorReportEntry(" ",
					" ", " ", " ", "Avgiver K1: Validering av avgiver",
					"Klarer ikke å validere Avgiver mot filspesifikasjon",
					Constants.CRITICAL_ERROR), node.getNode()
					.getOwnerDocument(), "Avgiver.xsd");

			/**
			 * <pre>
			 * - Avgiver K2: Årgang
			 *   - Kontrollerer at årgangen for filutrekket er for gjeldende rapporteringsår
			 * </pre>
			 */
			controlEquals(er, new ErrorReportEntry(" ", "", " ", " ",
					"Avgiver K2: Årgang",
					"Filen inneholder feil rapporteringsår (" + node.queryString("@Versjon") + "), forventet " + args.getAargang() + ".",
					Constants.CRITICAL_ERROR), node.queryString("@Versjon"),
					Constants.kostraYear);

			/**
			 * <pre>
			 * - Avgiver K3: Organisasjonnummer
			 *   - Kontrollerer at filen har organisasjonsnummer for avgiver
			 * </pre>
			 */
			controlOrgnr(er, new ErrorReportEntry(" ", " ", " ", " ",
					"Avgiver K3: Organisasjonnummer",
					"Filen mangler organisasjonsnummer. Oppgitt organisasjonsnummer er '"+ node.queryString("@Organisasjonsnummer").trim() +"'",
					Constants.CRITICAL_ERROR),
					node.queryString("@Organisasjonsnummer").trim());

			/**
			 * <pre>
			 * - Avgiver K4: Kommunenummer
			 *   - Kontrollerer at kommunenummer i webskjemaet likt med tilsvarende i filutrekket
			 * </pre>
			 */
			controlEquals(
					er,
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


			/**
			 * <pre>
			 * - Avgiver K6: Kommunenavn
			 *   - Kontrollerer at kommunenavn fins i filen
			 * </pre>
			 */
			controlExistsAndHasLength(er, new ErrorReportEntry(" ", " ", " ", " ",
					"Avgiver K6: Kommunenavn", "Filen mangler kommunenavn.",
					Constants.CRITICAL_ERROR), node.queryString("@Kommunenavn").trim());

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}
