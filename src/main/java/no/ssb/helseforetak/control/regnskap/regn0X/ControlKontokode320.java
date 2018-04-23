package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 */

import java.util.Vector;

import no.ssb.kostra.control.Constants;

final class ControlKontokode320 extends no.ssb.kostra.control.Control {
	private final String ERROR_TEXT = "Kontroll 20, Kontokode 320:";
	private Vector<String[]> invalidCombinations = new Vector<String[]>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		boolean lineHasError = false;

		String kontokode = RecordFields.getKontokode(line);
		String funksjon = RecordFields.getFunksjon(line);

		 if (kontokode.equalsIgnoreCase("320"))
		 {
			 if (!funksjon.equalsIgnoreCase ("620") &&
			 !funksjon.equalsIgnoreCase ("630") &&
			 !funksjon.equalsIgnoreCase ("636") &&
			 !funksjon.equalsIgnoreCase ("637") &&
			 !funksjon.equalsIgnoreCase ("641") &&
			 !funksjon.equalsIgnoreCase ("642") &&
			 !funksjon.equalsIgnoreCase ("651") &&
			 !funksjon.equalsIgnoreCase ("681") &&
			 !funksjon.equalsIgnoreCase ("840"))
			 {
				 lineHasError = true;
			 }
		 }
		if (lineHasError) {

			lineHasError = true;
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = funksjon;
			invalidCombinations.add(container);
		}
		return lineHasError;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = ERROR_TEXT + lf + lf;
		if (foundError()) {
			int numOfRecords = invalidCombinations.size();
			errorReport += "\tFeil: Ugyldig funksjon. Kontokode 320 - ISF inntekter kan kun benyttes "
					+ lf
					+ "\tav somatisk, psykisk helsevern og rus funksjon "
					+ lf
					+ "\t620 og/eller 630 og/eller 636, 637, 641, 642, 651 og 681."
					+ lf;
			for (int i = 0; i < numOfRecords; i++) {
				String[] container = (String[]) invalidCombinations
						.elementAt(i);
				errorReport += "\t\tfunksjon: " + container[1]
						+ " (Record nr. " + container[0] + ")" + lf;
			}
		}
		errorReport += lf + lf;
		return errorReport;
	}

	public boolean foundError() {
		return invalidCombinations.size() > 0;
	}

	public int getErrorType() {
		return Constants.NORMAL_ERROR;
	}
}
