package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
*/

import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class Control43 extends no.ssb.kostra.control.Control
		implements no.ssb.kostra.control.SingleRecordErrorReport {
	private final String ERROR_TEXT = "K43: Type vilkår det stilles til mottakeren";
	private Vector<Integer> linesWithError = new Vector<Integer>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {

		boolean lineHasError = !isFilledField25(line);

		if (lineHasError)
			linesWithError.add(new Integer(lineNumber));

		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = ERROR_TEXT + ":" + lf;
		int numOfRecords = linesWithError.size();
		if (numOfRecords > 0) {
			errorReport += lf
					+ "\tFeil: i "
					+ numOfRecords
					+ " record"
					+ (numOfRecords == 1 ? "" : "s")
					+ "Feltet for"
					+ lf
					+ "\t\"Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven, så skal det oppgis hvilke vilkår som stilles til mottakeren.\""
					+ lf
					+ "\tFeltet er obligatorisk ";
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

	static boolean isFilledField25(String line) {
		boolean isFilled = false;
		boolean isCorrect = true;

		String field;

		// Felt 25.1 - 25.9:
		for (int i = 1; i < 10; i++) {
			field = RecordFields.getFieldValue(line, (250 + i));
			field = field.replace(' ', '0');
			if (
					field.equalsIgnoreCase("0" + Integer.toString(i)) || field.equalsIgnoreCase("00")
					) {
				isFilled = true;
			} else {
				isCorrect = false;
			}

		}

		// Felt 25.10 - 25.15
		for (int i = 10; i < 16; i++) {
			field = RecordFields.getFieldValue(line, (2500 + i));
			field = field.replace(' ', '0');
			if (
				 field.equalsIgnoreCase(Integer.toString(i)) || field.equalsIgnoreCase("00")
			    )
				{
				 isFilled = true;
				} else
				{
				 isCorrect = false;
				}
		}

		return isFilled && isCorrect;
	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}
}