package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static no.ssb.kostra.control.felles.Comparator.between;

public class ControlFelt1ListeInneholderKodeFraKodelisteSaaInneholderKodeFraKodeliste {
    public static boolean doControl(ErrorReport errorReport, String controlCategoriTitle, String title, List<String> codes1, List<String> codeList1, List<String> codes2, List<String> codeList2, int errorType) {
        Map<String, List<Integer>> result = new HashMap<>();

        for (int i = 0; i < codes1.size(); i++) {
            String code1 = codes1.get(i);

            if (Comparator.isCodeInCodelist(code1, codeList1)) {
                for (int j = 0; j < codes1.size(); j++) {
                    String code2 = codes2.get(j);

                    if (!Comparator.isCodeInCodelist(code2, codeList2)) {
                        if (!result.containsKey(code2)) {
                            result.put(code2, new ArrayList<>());
                        }

                        List<Integer> temp = result.get(code2);
                        temp.add((i + 1));
                        result.put(code2, temp);
                    }
                }
            }
        }

        if (!result.isEmpty()) {
            for (String key : result.keySet()) {
                List<Integer> indices = result.get(key);
                String errorText;
                if (indices.size() == 1) {
                    errorText = "Gjelder for linje " + indices.get(0);
                } else if (between(indices.size(), 2, 50)) {
                    errorText = "Gjelder for linjene " + indices.toString();
                } else {
                    errorText = "Gjelder for flere enn 50 linjer";
                }

                ErrorReportEntry errorReportEntry = new ErrorReportEntry(controlCategoriTitle, "Kontroll " + title, " ", " "
                        , "Korrigér " + title.toLowerCase() + " (" + key + ")"
                        , errorText
                        , errorType);
                errorReport.addEntry(errorReportEntry);
            }

            return true;
        }

        return false;
    }
}
