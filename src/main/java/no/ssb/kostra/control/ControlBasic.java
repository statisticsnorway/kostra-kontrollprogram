package no.ssb.kostra.control;

import java.util.Vector;

public abstract class ControlBasic extends Control implements
		SingleRecordErrorReport {
	protected Vector<Integer> lineNumbers = new Vector<Integer>();
	protected String errorReportTitle;
	protected String errorReportHeading;
	protected String errorReportHint;
	protected String errorText = null;
	protected int errorType;

	public ControlBasic(String errorReportTitle, String errorReportHeading,
			String errorReportHint, int errorType) {
		this.errorReportTitle = errorReportTitle;
		this.errorReportHeading = errorReportHeading;
		this.errorReportHint = errorReportHint;
		this.errorType = errorType;

	}

	@Override
	public String getErrorReport(int totalLineNumber) {
		String errorReport = errorReportTitle + ":" + lf;

		if (lineNumbers.size() > 0) {
			errorReport += lf + "\t" + errorReportHeading + " i "
					+ lineNumbers.size() + " record"
					+ (lineNumbers.size() == 1 ? "" : "s") + ".";
			if (lineNumbers.size() <= 10) {
				errorReport += lf + "\t\t(Gjelder record nr.";
				for (int i = 0; i < lineNumbers.size(); i++) {
					errorReport += " " + lineNumbers.elementAt(i);
				}
				errorReport += ").";
			}
		}

		if (errorReportHint.length() > 0) {
			errorReport += lf + errorReportHint + lf + lf;
		}

		return errorReport;
	}

	@Override
	public boolean foundError() {
		return lineNumbers.size() > 0;
	}

	@Override
	public int getErrorType() {
		return this.errorType;
	}

	@Override
	public String getErrorText() {
		return (errorType > 0 && errorText != null && errorText.length() > 0) ? errorText: "";
	}
}
