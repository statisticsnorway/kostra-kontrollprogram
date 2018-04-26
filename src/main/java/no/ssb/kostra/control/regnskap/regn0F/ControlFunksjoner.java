package no.ssb.kostra.control.regnskap.regn0F;

import java.util.Vector;

import no.ssb.kostra.control.Constants;

final class ControlFunksjoner extends no.ssb.kostra.control.Control {
	private String[] validFunksjoner = { "041", "042", "043", "044", "045",
			"089" };

	private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		String funksjon = RecordFields.getFunksjon(line);
		boolean lineHasError = !validFunksjon(funksjon);

		if (lineHasError) {
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = RecordFields.getFunksjonUncropped(line);
			invalidFunksjoner.add(container);
		}
		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 6, gyldige funksjoner:" + lf + lf;
		int numOfRecords = invalidFunksjoner.size();
		if (numOfRecords > 0) {
			errorReport += "\tFeil: Ukjent" + (numOfRecords == 1 ? "" : "e")
					+ " funksjon" + (numOfRecords == 1 ? "" : "er") + ":" + lf;
			for (int i = 0; i < numOfRecords; i++) {
				String[] container = (String[]) invalidFunksjoner.elementAt(i);
				errorReport += "\t\tfunksjon " + container[1] + " (Record nr. "
						+ container[0] + ")" + lf;
			}
		}
		errorReport += lf;
		return errorReport;
	}

	public boolean foundError() {
		return (invalidFunksjoner.size() > 0);
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
