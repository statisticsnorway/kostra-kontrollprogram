package no.ssb.kostra.control.regnskap.regn0J;

import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.OrgNrForIKS;;

final class ControlOrgNummer extends no.ssb.kostra.control.Control {
	private Vector<Integer> recordNumbers = new Vector<Integer>();

	public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
		String orgNum = RecordFields.getOrgNummer(line);
		OrgNrForIKS o = new OrgNrForIKS();
		boolean orgNummerIsNotValid = !o.isValidOrgNr(orgNum); 

		if (orgNummerIsNotValid) {
			recordNumbers.add(new Integer(lineNumber));
		}
		return orgNummerIsNotValid;
	}

	public String getErrorReport(int totalLineNumber) {
		String errorReport = "Kontroll 4, organisasjonsnummer:" + lf + lf;
		int numOfRecords = recordNumbers.size();
		if (numOfRecords > 0) {
			errorReport += "\tFeil: Mangler organisasjonsnummer eller oppgitt organisasjonsnummer er ikke korrekt i hht. Brønnøysundregisterets opplysninger pr oktober." + lf +
			               "\tDersom foretaket ikke er registrert for regnskapsåret bes særskilt melding gitt til SSB." + lf +
					       "\t\t" + numOfRecords + " av " + totalLineNumber + " records mangler eller har feil organisasjonsnummer." + lf;
			if (numOfRecords <= 10) {
				errorReport += "\t(Gjelder følgende records:";
				for (int i = 0; i < numOfRecords; i++) {
					errorReport += " " + recordNumbers.elementAt(i) + (i>0 ? "" : ", ");
				}
				errorReport += ")";
			}
			errorReport += lf + "\tKorreksjon: Legg til organisasjonsnummeret eller rett opp i fila slik at alle recordene inneholder samme organisasjonsnummer." + lf + lf;
		}
		return errorReport;
	}

	public boolean foundError() {
		return recordNumbers.size() > 0;
	}

	public int getErrorType() {
		return Constants.CRITICAL_ERROR;
	}
}
