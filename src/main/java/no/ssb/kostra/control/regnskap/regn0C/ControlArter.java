package no.ssb.kostra.control.regnskap.regn0C;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlArter extends no.ssb.kostra.control.Control {
	private String[] validArter = {
			"010", "020", "030", "040", "050", "070", "075", "080",
			"089", "090", "099", "100", "105", "110", "114", "115",
			"120", "130", "140", "150", "160", "165", "170", "180",
			"181", "182", "183", "184", "185", "190", "195", "200",
			"209", "210", "220", "230", "240", "250", "260", "270",
			"280", "285", "300", "330", "350", "370", "375", "380",
			"400", "429", "430", "450", "470", "475", "480", "490",
			"500", "501", "509", "510", "511", "520", "521", "529",
			"530", "540", "548", "550", "570", "580", "590", "600",
			"620", "629", "630", "640", "650", "660", "670", "690",
			"700", "710", "729", "730", "750", "770", "775", "780",
			"800", "810", "830", "850", "870", "877", "880", "890",
			"895", "900", "901", "905", "909", "910", "911", "920",
			"921", "929", "930", "940", "948", "950", "958", "970",
			"980", "990" };

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
			container[1] = art;
			invalidArter.add(container);
		}
		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 7, Ugyldig bruk av arter:" + lf + lf;
		int numOfRecords = invalidArter.size();
		if (numOfRecords > 0) {
			errorReport += "\tFeil: Ugyldig" + (numOfRecords == 1 ? "" : "e")
					+ " art" + (numOfRecords == 1 ? "" : "er") + ":" + lf;
			for (int i = 0; i < numOfRecords; i++) {
				String[] container = (String[]) invalidArter.elementAt(i);
				errorReport += "\t\tart " + container[1] + " (Record nr. "
						+ container[0] + ")" + lf;
			}
		}
		errorReport += lf
				+ "\tKorreksjon: Rett opp feil art med gyldig art. For oversikt over gyldige arter se gjeldende versjon av KOSTRA-kontoplanen."
				+ lf + lf;
		return errorReport;
	}

	public boolean foundError() {
		return invalidArter.size() > 0;
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