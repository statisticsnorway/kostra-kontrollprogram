package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
 */

import java.util.Vector;

import no.ssb.kostra.control.Constants;

public final class ControlStonadsmaaneder extends no.ssb.kostra.control.Control
		implements no.ssb.kostra.control.SingleRecordErrorReport {
	private final String ERROR_TEXT = "K26: Stønadsmåneder. Gyldige koder";
	private Vector<Integer> linesWithError = new Vector<Integer>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {

		boolean lineHasError = !isFilledField14(line);

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
					+ " er det ikke krysset av for hvilke måneder mottakeren har fått"
					+ lf
					+ "\tutbetalt økonomisk sosialhjelp (bidrag eller lån), "
					+ "eller det er benyttet ugyldige koder. " + lf
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

	static boolean isFilledField14(String line) {
		boolean isFilled = false;
		boolean isCorrect = true;

		String field;

		// Felt 14.1 - 14.9:
		for (int i = 1; i < 10; i++) {
			field = RecordFields.getFieldValue(line, (140 + i));
			field = field.replace(' ', '0');
			if (
				  field.equalsIgnoreCase("0" + Integer.toString(i)) || field.equalsIgnoreCase("00")
				)
				 {
				   isFilled = true;
				 } else
				 {
				   isCorrect = false;
			}

		}

		// Felt 14.10 - 14.12
		for (int i = 10; i < 13; i++) {
			field = RecordFields.getFieldValue(line, (1400 + i));
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
		return Constants.NORMAL_ERROR;
	}
}