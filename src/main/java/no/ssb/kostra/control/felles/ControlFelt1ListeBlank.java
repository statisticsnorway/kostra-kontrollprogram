package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.between;

@SuppressWarnings("SpellCheckingInspection")
public class ControlFelt1ListeBlank {

    public static boolean doControl(
            final ErrorReport errorReport, final String controlCategoriTitle, final String title,
            final List<String> emptyTrimmedStrings, final int errorType) {

        final var indices = new ArrayList<Integer>();

        for (var i = 0; i < emptyTrimmedStrings.size(); i++) {
            final var fieldValue = emptyTrimmedStrings.get(i);
            if (0 < fieldValue.length()) {
                indices.add((i + 1));
            }
        }

        if (!indices.isEmpty()) {
            String errorText;
            if (indices.size() == 1) {
                errorText = "Gjelder for linje " + indices.get(0);
            } else if (between(indices.size(), 2, 50)) {
                errorText = "Gjelder for linjene " + indices;
            } else {
                errorText = "Gjelder for flere enn 50 linjer";
            }

            errorReport.addEntry(new ErrorReportEntry(controlCategoriTitle, "Kontroll " + title + ", Felt skal være blank", " ", " "
                    , "Korrigér " + title.toLowerCase() + " til kun mellomrom / blanke tegn"
                    , errorText
                    , errorType));

            return true;
        }
        return false;
    }
}
