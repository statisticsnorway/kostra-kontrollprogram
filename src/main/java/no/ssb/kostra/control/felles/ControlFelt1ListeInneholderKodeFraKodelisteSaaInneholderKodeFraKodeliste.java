package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.between;

// TODO: Not in use
@SuppressWarnings("SpellCheckingInspection")
public class ControlFelt1ListeInneholderKodeFraKodelisteSaaInneholderKodeFraKodeliste {

    public static boolean doControl(
            final ErrorReport errorReport, final String controlCategoryTitle,
            final String title, final List<String> codes1,
            final List<String> codeList1, final List<String> codes2,
            final List<String> codeList2, final int errorType) {

        final var result = new HashMap<String, List<Integer>>();

        for (var i = 0; i < codes1.size(); i++) {
            final var code1 = codes1.get(i);

            if (Comparator.isCodeInCodeList(code1, codeList1)) {
                for (var j = 0; j < codes1.size(); j++) {
                    final var code2 = codes2.get(j);

                    if (!Comparator.isCodeInCodeList(code2, codeList2)) {
                        if (!result.containsKey(code2)) {
                            result.put(code2, new ArrayList<>());
                        }

                        final var temp = result.get(code2);
                        temp.add((i + 1));
                        result.put(code2, temp);
                    }
                }
            }
        }

        if (!result.isEmpty()) {
            for (var key : result.keySet()) {
                final var indices = result.get(key);
                String errorText;
                if (indices.size() == 1) {
                    errorText = "Gjelder for linje " + indices.get(0);
                } else if (between(indices.size(), 2, 50)) {
                    errorText = "Gjelder for linjene " + indices;
                } else {
                    errorText = "Gjelder for flere enn 50 linjer";
                }

                errorReport.addEntry(new ErrorReportEntry(controlCategoryTitle, "Kontroll " + title, " ", " "
                        , "Korrigér " + title.toLowerCase() + " (" + key + ")"
                        , errorText
                        , errorType));
            }
            return true;
        }
        return false;
    }
}
