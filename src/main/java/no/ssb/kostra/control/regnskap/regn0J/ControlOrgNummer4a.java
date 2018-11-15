package no.ssb.kostra.control.regnskap.regn0J;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

/**
 * Created by ojj on 06.11.2018.
 */
public class ControlOrgNummer4a extends no.ssb.kostra.control.Control {
    private Vector<Integer> recordNumbers = new Vector<>();
    private String orgNummer = null;

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String orgNum = RecordFields.getOrgNummer(line).trim();

        if (this.orgNummer == null) {
            this.orgNummer = orgNum;
        }

        boolean orgNummerIsNotValid = (!this.orgNummer.equalsIgnoreCase(orgNum));

        if (orgNummerIsNotValid) {
            recordNumbers.add(new Integer(lineNumber));
        }
        return orgNummerIsNotValid;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = "Kontroll 4a, Samme organisasjonsnummer i hele filen og organisasjonsnummer forskjellig fra 0 (null):" + lf + lf;
        int numOfRecords = recordNumbers.size();
        if (numOfRecords > 0) {
            errorReport += "\tFeil: Det er oppgitt ulike organisasjonsnumre i filen, eller det er oppgitt organisasjonsnummer som bare består av 0 (null)." + lf +
                    "\t\t" + numOfRecords + " av " + totalLineNumber + " records mangler eller har feil organisasjonsnummer." + lf;
            if (numOfRecords <= 10) {
                errorReport += "\t(Gjelder følgende records:";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + recordNumbers.elementAt(i) + (i > 0 ? "" : ", ");
                }
                errorReport += ")";
            }
            errorReport += lf + "\tKorreksjon: Legg inn kun ett organisasjonsnummer og dette må samsvare med organisasjonsnummeret som ligger på skjemaforsiden." + lf + lf;
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
