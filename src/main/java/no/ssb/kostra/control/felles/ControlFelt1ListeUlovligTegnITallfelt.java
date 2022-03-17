package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.between;

@SuppressWarnings("SpellCheckingInspection")
public class ControlFelt1ListeUlovligTegnITallfelt {

    public static boolean doControl(
            final ErrorReport errorReport, final String title,
            final List<String> integersAsStrings, final int errorType) {

        final var indices = new ArrayList<Integer>();

        for (var i = 0; i < integersAsStrings.size(); i++) {
            final var fieldValue = integersAsStrings.get(i);
            if ((fieldValue.split("\t").length != 1 || !fieldValue.matches("^\\s*?-?\\d+$"))) {
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

            errorReport.addEntry(new ErrorReportEntry("3. Feltkontroller", "Kontroll Ulovlig tegn i tallfelt, " + title, " ", " "
                    , "Korrigér " + title.toLowerCase()
                    , errorText
                    , errorType));

            return true;
        }
        return false;
    }
}
