package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ControlBasic;

public final class ControlRecordlengde extends ControlBasic

{
	private int length = 0;

	public ControlRecordlengde(String errorReportTitle,
			String errorReportHeading, String errorReportHint, int errorType,
			int length) {
		super(errorReportTitle, errorReportHeading, errorReportHint, errorType);
		this.length = length;
	}

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		errorText = null;

		if (line.length() != this.length) {
			lineNumbers.add(new Integer(lineNumber));
			errorText = errorReportTitle + ": " + errorReportHeading
					+ ". Recordlengde er " + line.length() + ", men skal være "
					+ length + ".";

			return true;
		}

		return false;
	}
}
