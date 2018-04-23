package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import no.ssb.kostra.control.Constants;

public final class Control_23 extends no.ssb.kostra.control.Control implements
		no.ssb.kostra.control.SingleRecordErrorReport {
	private final String ERROR_TEXT = "K23: Programmets type tiltak/aktiviteter/bistand i løpet av året. Gyldige verdier.";
	private Vector<Integer> linesWithError = new Vector<Integer>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		boolean lineHasError = false;
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("171", "01");
		mapping.put("172", "02");
		mapping.put("173", "03");
		mapping.put("174", "04");
		mapping.put("175", "05");
		mapping.put("176", "18");
		mapping.put("177", "06");
		mapping.put("178", "07");
		mapping.put("179", "08");
		mapping.put("1710", "09");
		mapping.put("1711", "10");
		mapping.put("1712", "11");
		mapping.put("1713", "12");
		mapping.put("1714", "13");
		mapping.put("1715", "14");
		mapping.put("1716", "15");
		mapping.put("1717", "16");
		mapping.put("1718", "17");

		for (String key : mapping.keySet()) {
			String value = mapping.get(key);
			String field = RecordFields.getFieldValue(line,
					Integer.parseInt(key));
			field = field.replace(' ', '0');

			if (!field.equalsIgnoreCase(value) && !field.equalsIgnoreCase("00"))
				lineHasError = true;
		}

		if (lineHasError)
			linesWithError.add(new Integer(lineNumber));

		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = ERROR_TEXT + ":" + lf;
		int numOfRecords = linesWithError.size();
		if (numOfRecords > 0) {
			errorReport += lf
					+ "\tFeil (i "
					+ numOfRecords
					+ " record"
					+ (numOfRecords == 1 ? "" : "s")
					+ "): Det er ikke "
					+ "krysset av for"
					+ lf
					+ "\t\"Hvilke typer tiltak/aktiviteter/bistand "
					+ "deltakerens program har bestått av i løpet av "
					+ Constants.getRapporteringsAar()
					+ "\", "
					+ lf
					+ "\teller feil koder er benyttet. Feltet er obligatorisk å fylle ut.";
			if (numOfRecords <= 10) {
				errorReport += lf + "\t\t(Gjelder record nr.";
				for (int i = 0; i < numOfRecords; i++) {
					errorReport += " " + linesWithError.elementAt(i);
				}
				errorReport += ").";
			}
		}
		errorReport += lf + lf;
		return errorReport;
	}

	public boolean foundError() {
		return linesWithError.size() > 0;
	}

	public String getErrorText() {
		return ERROR_TEXT;
	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}
}