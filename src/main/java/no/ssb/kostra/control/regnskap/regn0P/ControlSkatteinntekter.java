package no.ssb.kostra.control.regnskap.regn0P;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlSkatteinntekter extends no.ssb.kostra.control.Control {

	private int sumForControl_13_1 = 0;
	private int sumForControl_13_2_a = 0;
	private int sumForControl_13_2_b = 0;
	private int sumForControl_13_3_a = 0;
	private int sumForControl_13_3_b = 0;

	private Vector<String[]> checkedLines = new Vector<String[]>();
	private Vector<String[]> checkedLines2 = new Vector<String[]>();
	private Vector<String[]> checkedLines3 = new Vector<String[]>();

	private boolean field_13_1_isFound = false;

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {

		int belop = 0;
		try {
			belop = RecordFields.getBelopIntValue(line);
		} catch (Exception e) {
			return false;
		}

		boolean lineControlled = false;

		if (doControl_13_1(line)) {
			field_13_1_isFound = true;
			lineControlled = true;
			sumForControl_13_1 += belop;
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = Integer.toString(belop);
			checkedLines.add(container);
		}
		if (doControl_13_2_a(line)) {
			lineControlled = true;
			sumForControl_13_2_a += belop;
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = Integer.toString(belop);
			checkedLines2.add(container);
		}
		if (doControl_13_2_b(line)) {
			lineControlled = true;
			sumForControl_13_2_b += belop;
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = Integer.toString(belop);
			checkedLines2.add(container);
		}
		if (doControl_13_3_a(line)) {
			lineControlled = true;
			sumForControl_13_3_a += belop;
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = Integer.toString(belop);
			checkedLines3.add(container);
		}
		if (doControl_13_3_b(line)) {
			lineControlled = true;
			sumForControl_13_3_b += belop;
			String[] container = new String[2];
			container[0] = Integer.toString(lineNumber);
			container[1] = Integer.toString(belop);
			checkedLines3.add(container);
		}
		return lineControlled;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 13, skatteinntekter og rammetilskudd:"
				+ lf + lf;
		if (!field_13_1_isFound && sumForControl_13_1 == 0) {
			errorReport += "\tFeil: Driftsregnskapet skal ha skatteinntekter. "
					+ lf;
			int lineNum = checkedLines.size();
			if (lineNum <= 10) {
				String[] container;
				int belop, lineNumber;
				for (int i = 0; i < lineNum; i++) {
					container = (String[]) checkedLines.elementAt(i);
					lineNumber = Integer.parseInt(container[0]);
					belop = Integer.parseInt(container[1]);
					if (belop == 0) {
						errorReport += "\t\tRecord " + lineNumber
								+ " har beløp lik 0." + lf;
					} else if (belop > 0) {
						errorReport += "\t\tRecord " + lineNumber
								+ " har beløp større enn 0." + lf;
					}
				}
			}
			errorReport += "\tKorreksjon: Rett opp slik at fila inneholder skatteinntekter."
					+ lf + lf;
		}
		if (((sumForControl_13_2_a + sumForControl_13_2_b) != 0)) { // &&
																	// field_13_2_a_isFound
																	// &&
																	// field_13_2_b_isFound)
																	// {
			errorReport += "\tFeil: Skatteinntekter skal føres på funksjon 800 og art 870. "
					+ lf;
			int lineNum = checkedLines2.size();
			if (lineNum <= 10) {
				String[] container;
				int belop, lineNumber;
				for (int i = 0; i < lineNum; i++) {
					container = (String[]) checkedLines2.elementAt(i);
					lineNumber = Integer.parseInt(container[0]);
					belop = Integer.parseInt(container[1]);
					errorReport += "\t\tRecord " + lineNumber + " har beløp "
							+ belop + lf;
				}
			}
			errorReport += "\tKorreksjon: Rett opp slik at skatteinntekter er ført på funksjon 800, art 870."
					+ lf + lf;
		}
		if (((sumForControl_13_3_a + sumForControl_13_3_b) != 0)) { // &&
																	// field_13_3_a_isFound
																	// &&
																	// field_13_3_b_isFound)
																	// {
			errorReport += "\tFeil: Rammetilskudd skal føres på funksjon 840 og art 800. "
					+ lf + "\tArt 800 skal ikke benyttes på andre funksjoner enn 840."
					+ lf;
			int lineNum = checkedLines3.size();
			if (lineNum <= 10) {
				String[] container;
				int belop, lineNumber;
				for (int i = 0; i < lineNum; i++) {
					container = (String[]) checkedLines3.elementAt(i);
					lineNumber = Integer.parseInt(container[0]);
					belop = Integer.parseInt(container[1]);
					errorReport += "\t\tRecord " + lineNumber + " har beløp "
							+ belop + lf;
				}
			}
			errorReport += "\tKorreksjon: Rett opp slik at art 800 ikke benyttes på andre funksjoner enn 840."
					+ lf + lf;
		}
		errorReport += lf;
		return errorReport;
	}

	public boolean foundError() {
		return (sumForControl_13_1 == 0) || (!field_13_1_isFound)
				|| ((sumForControl_13_2_a + sumForControl_13_2_b) != 0)
				|| ((sumForControl_13_3_a + sumForControl_13_3_b) != 0);
	}

	private boolean doControl_13_1(String line) {
		String kontoklasse = RecordFields.getKontoklasse(line);
		String funksjon = RecordFields.getFunksjon(line);
		String art = RecordFields.getArt(line);

		boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
		boolean rettFunksjon = (funksjon.equalsIgnoreCase("800"));
		boolean rettArt = (art.equalsIgnoreCase("870"));

		return rettKontoklasse && rettFunksjon && rettArt;
	}

	private boolean doControl_13_2_a(String line) {

		boolean do_control;

		try {

			String kontoklasse = RecordFields.getKontoklasse(line);
			int funksjon = RecordFields.getFunksjonIntValue(line);
			String art = RecordFields.getArt(line);

			boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
			boolean rettFunksjon = (funksjon >= 400 && funksjon <= 799);
			boolean rettArt = (art.equalsIgnoreCase("870"));

			do_control = rettKontoklasse && rettFunksjon && rettArt;

		} catch (Exception e) {

			do_control = false;

		}
		return do_control;
	}

	private boolean doControl_13_2_b(String line) {

		boolean do_control;

		try {

			String kontoklasse = RecordFields.getKontoklasse(line);
			int funksjon = RecordFields.getFunksjonIntValue(line);
			String art = RecordFields.getArt(line);

			boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
			boolean rettFunksjon = (funksjon >= 840 && funksjon <= 899);
			boolean rettArt = (art.equalsIgnoreCase("870"));

			do_control = rettKontoklasse && rettFunksjon && rettArt;

		} catch (Exception e) {

			do_control = false;

		}

		return do_control;
	}

	private boolean doControl_13_3_a(String line) {

		boolean do_control;

		try {

			String kontoklasse = RecordFields.getKontoklasse(line);
			int funksjon = RecordFields.getFunksjonIntValue(line);
			String art = RecordFields.getArt(line);

			boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
			boolean rettFunksjon = (funksjon >= 400 && funksjon <= 800);
			boolean rettArt = (art.equalsIgnoreCase("800"));

			do_control = rettKontoklasse && rettFunksjon && rettArt;

		} catch (Exception e) {

			do_control = false;

		}

		return do_control;
	}

	private boolean doControl_13_3_b(String line) {

		boolean do_control;

		try {

			String kontoklasse = RecordFields.getKontoklasse(line);
			int funksjon = RecordFields.getFunksjonIntValue(line);
			String art = RecordFields.getArt(line);

			boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
			boolean rettFunksjon = (funksjon >= 850 && funksjon <= 899);
			boolean rettArt = (art.equalsIgnoreCase("800"));

			do_control = rettKontoklasse && rettFunksjon && rettArt;

		} catch (Exception e) {

			do_control = false;

		}

		return do_control;
	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}

}