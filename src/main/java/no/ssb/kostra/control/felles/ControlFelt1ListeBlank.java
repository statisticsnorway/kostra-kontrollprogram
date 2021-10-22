package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.between;

public class ControlFelt1ListeBlank {
    public static boolean doControl(ErrorReport errorReport, String controlCategoriTitle, String title, List<String> emptyTrimmedStrings, int errorType) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < emptyTrimmedStrings.size(); i++) {
            String fieldvalue = emptyTrimmedStrings.get(i);
            if (0 < fieldvalue.length()) {
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

            ErrorReportEntry errorReportEntry = new ErrorReportEntry(controlCategoriTitle, "Kontroll " + title + ", Felt skal være blank", " ", " "
                    , "Korrigér " + title.toLowerCase() + " til kun mellomrom / blanke tegn"
                    , errorText
                    , errorType);
            errorReport.addEntry(errorReportEntry);


            return true;
        }

        return false;
    }
}
