package no.ssb.kostra.control.regnskap.regn0F;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlArter extends no.ssb.kostra.control.Control {
	private String[] validArter = { "010", "020", "030", "040", "050", "060",
			"080", "090", "095", "099", "100", "110", "120", "130", "140",
			"150", "155", "160", "165", "170", "180", "185", "190", "195",
			"200", "210", "220", "230", "240", "250", "260", "265", "270",
			"280", "285", "300", "330", "340", "350", "370", "380", "390",
			"400", "429", "430", "440", "450", "465", "470", "500", "510",
			"520", "530", "540", "550", "570", "580", "590", "600", "610",
			"620", "630", "650", "660", "670", "700", "710", "729", "730",
			"740", "750", "770", "780", "790", "800", "830", "840", "850",
			"860", "865", "870", "900", "905", "910", "920", "930", "940",
			"950", "970", "980", "990" };

	private Vector<String[]> invalidArter = new Vector<String[]>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		String art = RecordFields.getArt(line);

		// Kontrollen skal ikke foretas hvis belop = 0 og art er definert.
		try {
			int belop = RecordFields.getBelopIntValue(line);
			if (belop == 0 && art.trim().length() > 0) {
				return false;
			}
		} catch (Exception e) {
			// Returnerer her ogsaa. Gir ikke mening med kontroll
			// hvis belop ikke er angitt.
			return false;
		}

		boolean lineHasError = !isValidArt(art);

		if (lineHasError) {
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = RecordFields.getArtUncropped(line);
			invalidArter.add(container);
		}
		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 7, gyldige arter:" + lf + lf;
		int numOfRecords = invalidArter.size();
		if (numOfRecords > 0) {
			errorReport += "\tFeil: ukjent" + (numOfRecords == 1 ? "" : "e")
					+ " art" + (numOfRecords == 1 ? "" : "er") + ":" + lf;
			for (int i = 0; i < numOfRecords; i++) {
				String[] container = (String[]) invalidArter.elementAt(i);
				errorReport += "\t\tart " + container[1] + " (Record nr. "
						+ container[0] + ")" + lf;
			}
		}
		errorReport += lf;
		return errorReport;
	}

	public boolean foundError() {
		return (invalidArter.size() > 0);
	}

	private boolean isValidArt(String art) {
		for (int i = validArter.length - 1; i >= 0; i--) {
			if (art.equalsIgnoreCase(validArter[i])) {
				return true;
			}
		}
		return false;
	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}
}
