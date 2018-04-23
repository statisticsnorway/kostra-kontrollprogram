package no.ssb.kostra.control.regnskap.regn0C;

/*
 */

import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control8a extends no.ssb.kostra.control.Control {
	private Vector<String[]> invalidCombinations = new Vector<String[]>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		boolean lineHasError = false;

		String kontoklasse = RecordFields.getKontoklasse(line);

		String funksjon = RecordFields.getFunksjon(line);

		if ((funksjon.equalsIgnoreCase("800")
				|| funksjon.equalsIgnoreCase("840") || funksjon
					.equalsIgnoreCase("860"))
				&& !kontoklasse.equalsIgnoreCase("1")) {
			lineHasError = true;
			String[] container = { kontoklasse, funksjon,
					Integer.toString(lineNumber) };
			invalidCombinations.add(container);
		}

		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 8a, kombinasjon kontoklasse og funksjon i driftsregnskapet:"
				+ lf + lf;
		if (foundError()) {
			int numOfRecords = invalidCombinations.size();
			errorReport += "\tFeil: Funksjonen er kun tillatt brukt i driftsregnskapet."
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
				+ "\tKorreksjon: Rett opp feil kontoklasse eller funksjon."
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