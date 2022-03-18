package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.between;

@SuppressWarnings("SpellCheckingInspection")
public class ControlFelt1ListeHeltall {

    public static boolean doControl(
            final ErrorReport errorReport, final String title,
            final List<Integer> integers, final int errorType) {

        final var indices = new ArrayList<Integer>();

        for (var i = 0; i < integers.size(); i++) {
            if (integers.get(i) == null) {
                indices.add((i + 1));
            }
        }

        if (indices.isEmpty()) {
            return false;
        }

        String errorText;
        if (indices.size() == 1) {
            errorText = "Gjelder for linje " + indices.get(0);
        } else if (between(indices.size(), 2, 50)) {
            errorText = "Gjelder for linjene " + indices;
        } else {
            errorText = "Gjelder for flere enn 50 linjer";
        }

        errorReport.addEntry(new ErrorReportEntry("3. Feltkontroller", "Kontroll " + title, " ", " "
                , "Korrigér " + title.toLowerCase()
                , errorText
                , errorType));

        return true;
    }
}
