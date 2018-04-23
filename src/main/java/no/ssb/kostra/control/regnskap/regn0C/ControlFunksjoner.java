package no.ssb.kostra.control.regnskap.regn0C;

import java.util.Vector;

import no.ssb.kostra.control.Constants;

final class ControlFunksjoner extends no.ssb.kostra.control.Control {
	private String[] validFunksjoner = {
			"400", "410", "420", "421", "430", "460", "465",
			"470", "471", "472", "473", "480", "490", "510",
			"515", "520", "521", "522", "523", "524", "525",
			"526", "527", "528", "529", "530", "531", "533",
			"532", "554", "559", "561", "562", "570", "581",
			"590", "660", "665", "701", "710", "711", "715",
			"716", "722", "730", "731", "732", "733", "734",
			"740", "750", "760", "771", "772", "775", "790",
			"800", "840", "841", "860", "870", "880", "899",
			"z", "~" };

	private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		String funksjon = RecordFields.getFunksjon(line);
		// Kontrollen skal ikke foretas hvis belop = 0 og funksjon er definert.
		try {
			int belop = RecordFields.getBelopIntValue(line);
			if (belop == 0 && funksjon.trim().length() > 0) {
				return false;
			}
		} catch (Exception e) {
			// Returnerer her ogsaa. Gir ikke mening med kontroll
			// hvis belop ikke er angitt.
			return false;
		}

		boolean lineHasError = !validFunksjon(funksjon);

		if (lineHasError) {
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = funksjon;
			invalidFunksjoner.add(container);
		}
		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 6, Ugyldig bruk av funksjoner:" + lf + lf;
		int numOfRecords = invalidFunksjoner.size();
		if (numOfRecords > 0) {
			errorReport += "\tFeil: Ugyldig" + (numOfRecords == 1 ? "" : "e")
					+ " funksjon" + (numOfRecords == 1 ? "" : "er") + ":" + lf;
			for (int i = 0; i < numOfRecords; i++) {
				String[] container = (String[]) invalidFunksjoner.elementAt(i);
				errorReport += "\t\tfunksjon " + container[1] + " (Record nr. "
						+ container[0] + ")" + lf;
			}
		}
		errorReport += lf
				+ "\tKorreksjon: Rett opp feil funksjon med gyldig funksjon. For oversikt over gyldige funksjoner se gjeldende versjon av KOSTRA-kontoplanen."
				+ lf + lf;
		return errorReport;
	}

	public boolean foundError() {
		return invalidFunksjoner.size() > 0;
	}

	private boolean validFunksjon(String funksjon) {
		for (int i = validFunksjoner.length - 1; i >= 0; i--) {
			if (funksjon.equalsIgnoreCase(validFunksjoner[i])) {
				return true;
			}
		}
		return false;
	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}
}