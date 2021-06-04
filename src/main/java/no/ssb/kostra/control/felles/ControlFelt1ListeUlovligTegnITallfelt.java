package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.between;

public class ControlFelt1ListeUlovligTegnITallfelt {
    public static boolean doControl(ErrorReport errorReport, String title, List<String> integersAsStrings, int errorType) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < integersAsStrings.size(); i++) {
            String fieldvalue = integersAsStrings.get(i);
            if ((fieldvalue.split("\t").length != 1 || !fieldvalue.matches("^\\s*?-?\\d+$"))) {
                indices.add((i + 1));
            }
        }

        if (!indices.isEmpty()) {
            String errorText;
            if (indices.size() == 1) {
                errorText = "Gjelder for linje " + indices.get(0);
            } else if (between(indices.size(), 2, 50)) {
                errorText = "Gjelder for linjene " + indices.toString();
            } else {
                errorText = "Gjelder for flere enn 50 linjer";
            }

            ErrorReportEntry errorReportEntry = new ErrorReportEntry("3. Feltkontroller", "Kontroll Ulovlig tegn i tallfelt, " + title, " ", " "
                    , "Korrigér " + title.toLowerCase()
                    , errorText
                    , errorType);
            errorReport.addEntry(errorReportEntry);


            return true;
        }

        return false;
    }
}
