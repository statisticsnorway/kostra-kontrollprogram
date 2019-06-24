package no.ssb.kostra.control.regnskap.regn0N;

import java.util.Vector;

import no.ssb.kostra.control.Constants;

final class ControlKapitler extends no.ssb.kostra.control.Control {

	private String[] validFunksjoner = {
			"10", "11", "12", "13", "14", "15",
			"18", "19", "20", "21", "22", "23",
			"24", "27", "31", "32", "33", "34",
			"39", "40", "41", "42", "43", "45",
			"47", "51", "53", "55", "56", "580",
			"581", "5900", "5950", "5960", "5970", "5990",
			"9100",	"9110", "9200", "9999" };

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
		String errorReport = "Kontroll 6, Ugyldig bruk av kapitler:" + lf + lf;
		int numOfRecords = invalidFunksjoner.size();
		if (numOfRecords > 0) {
			errorReport += "\tFeil: Ugyldige kapitler."
//					+ (numOfRecords == 1 ? "" : "e") + (numOfRecords == 1 ? " kapittel" : " kapitler") + ":"
					+ lf;
			for (int i = 0; i < numOfRecords; i++) {
				String[] container = (String[]) invalidFunksjoner.elementAt(i);
				errorReport += "\t\tkapittel " + container[1] + " (Record nr. "
						+ container[0] + ")" + lf;
			}
		}
		errorReport += "\tKorreksjon: Rett opp feil kapittel med gyldig kapittel. For oversikt over gyldige kapitler se gjeldende versjon av KOSTRA-kontoplanen."
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