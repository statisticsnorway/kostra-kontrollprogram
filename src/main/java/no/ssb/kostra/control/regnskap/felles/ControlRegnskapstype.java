package no.ssb.kostra.control.regnskap.felles;

import no.ssb.kostra.control.ControlBasic;

public class ControlRegnskapstype extends ControlBasic {
	private int from;
	private int to;
	private String skjema;

	public ControlRegnskapstype(String errorReportTitle, String errorReportHeading,
			String errorReportHint, int errorType, int from, int to,
			String skjema) {
		super(errorReportTitle, errorReportHeading, errorReportHint, errorType);
		this.from = from;
		this.to = to;
		this.skjema = skjema;
	}

	@Override
	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		errorText = null;
		String lineSkjema = line.substring(from, to);

		if (!lineSkjema.equalsIgnoreCase(skjema)) {
			lineNumbers.add(new Integer(lineNumber));
			errorText = errorReportTitle + ": " + errorReportHeading
					+ ". Skjema er " + lineSkjema + ", men skal være " + skjema
					+ ".";

			return true;
		}

		return false;
	}
}
