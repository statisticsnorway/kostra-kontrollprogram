package no.ssb.kostra.control.regnskap.regn0C;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control8b extends no.ssb.kostra.control.Control {
	private Vector<String[]> invalidCombinations = new Vector<String[]>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		boolean lineHasError = false;

		String kontoklasse = RecordFields.getKontoklasse(line);
		String funksjon = RecordFields.getFunksjon(line);

		if (funksjon.equalsIgnoreCase("841")
				&& !kontoklasse.equalsIgnoreCase("0")) {
			lineHasError = true;
			String[] container = { kontoklasse, funksjon,
					Integer.toString(lineNumber) };
			invalidCombinations.add(container);
		}
		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 8b, kombinasjon kontoklasse og funksjon i investeringsregnskapet:"
				+ lf + lf;
		if (foundError()) {
			int numOfRecords = invalidCombinations.size();
			errorReport += "\tFeil: Funksjonen er kun tillatt brukt i investeringsregnskapet."
					+ lf;
			for (int i = 0; i < numOfRecords; i++) {
				String[] container = (String[]) invalidCombinations
						.elementAt(i);
				errorReport += "\t\tkontoklasse: " + container[0]
						+ " funksjon: " + container[1] + " (Record nr. "
						+ container[2] + ")" + lf;
			}
		}
		errorReport += lf
				+ "\tKorreksjon: Rett opp til funksjon som er gyldig i driftsregnskapet, eller overfør posteringen til investeringsregnskapet."
				+ lf + lf;
		return errorReport;
	}

	public boolean foundError() {
		return invalidCombinations.size() > 0;
	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}
}