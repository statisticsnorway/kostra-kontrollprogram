package no.ssb.helseforetak.control.regnskap.regn0X;

import no.ssb.kostra.control.Constants;

import java.util.Vector;


final class Control18OrgNrMotFunksjon extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 18, Funksjon 400:";
    private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {

        boolean lineHasError = false;

        String orgnr = RecordFields.getOrgNummer(line);
        String funksjon = RecordFields.getFunksjon(line);

        orgnr = orgnr.replace(' ', '0');

        if (!orgnr.equalsIgnoreCase("000000000")) {

            if (funksjon.equalsIgnoreCase("400")) {

                lineHasError = !(orgnr.equalsIgnoreCase("991324968") ||
                        orgnr.equalsIgnoreCase("983658725") ||
                        orgnr.equalsIgnoreCase("983658776") ||
                        orgnr.equalsIgnoreCase("883658752") ||
                        orgnr.equalsIgnoreCase("911912759") ||
                        orgnr.equalsIgnoreCase("913454405") ||
                        orgnr.equalsIgnoreCase("914637651") ||
                        orgnr.equalsIgnoreCase("814630722") ||
                        orgnr.equalsIgnoreCase("987601787") ||
                        orgnr.equalsIgnoreCase("915536255") ||
                        orgnr.equalsIgnoreCase("916879067") ||
                        orgnr.equalsIgnoreCase("818711832") ||
                        orgnr.equalsIgnoreCase("918177833") ||
                        orgnr.equalsIgnoreCase("918695079") ||
                        orgnr.equalsIgnoreCase("922307814"));
            }

        }

        if (lineHasError) {

            lineHasError = true;
            String[] container = new String[2];
            container[0] = Integer.toString(lineNumber);
            container[1] = funksjon;
            invalidFunksjoner.add(container);
        }

        return lineHasError;
    }


    public String getErrorReport(int totalLineNumber) {

        String errorReport = ERROR_TEXT + lf + lf;
        int numOfRecords = invalidFunksjoner.size();

        if (numOfRecords > 0) {

            errorReport += "\tFeil: Ugyldig funksjon i " + numOfRecords + " record" +
                    (numOfRecords == 1 ? "" : "s") + " Funksjonen kan kun benyttes av RHF og Nasjonale felleseide HF.";

//      String[] container;
//
//      if (numOfRecords <= 10) {
//
//          for (int i=0; i<numOfRecords; i++) {
//
//            container = (String[]) invalidFunksjoner.elementAt(i);
//            errorReport += "\t\tfunksjon " + container[1] +
//                " (Record nr. " + container[0] + ")" + lf;
//          }
//      }
        }

        errorReport += lf + lf;
        return errorReport;
    }


    public boolean foundError() {
        return invalidFunksjoner.size() > 0;
    }


    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }


}