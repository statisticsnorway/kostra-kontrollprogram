package no.ssb.kostra.control.sensitiv.famvern;

import no.ssb.kostra.control.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Control16 extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K16: Utdypende om primærklientens formelle sivilstand";
    private List<Integer> lineNumbers = new ArrayList<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        List<String> samlivsstatusList = Arrays.asList("3", "4");
        List<String> sivilstandList = Arrays.asList("1", "2", "3", "4", "5", "6");
        String samlivsstatus = RecordFields.getFieldValue(line, 91);
        String sivilstand = RecordFields.getFieldValue(line, 92);

        if (samlivsstatusList.contains(samlivsstatus) && !sivilstandList.contains(sivilstand)) {
            lineNumbers.add(new Integer(lineNumber));
            return true;
        }

        return false;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf + lf;
        if (foundError()) {
            int numOfRecords = lineNumbers.size();
            errorReport += "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") +
                    " er det oppgitt at primærklientens samlivsstatus er Samboer eller " + lf +
                    "\tat primærklient ikke lever i samliv, men det er ikke fylt ut hva " + lf +
                    "\tsom er primærklientens korrekt sivilstand.";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + lineNumbers.get(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return lineNumbers.size() > 0;
    }

    public String getErrorText() {
        return ERROR_TEXT;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}
