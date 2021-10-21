package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static no.ssb.kostra.control.felles.Comparator.between;

public class ControlFelt1ListeInneholderKodeFraKodeliste {
    public static boolean doControl(ErrorReport errorReport, String controlCategoriTitle, String controlTitle, String formattedControlText, List<String> codes, List<String> codeList, int errorType) {
        Map<String, List<Integer>> result = new HashMap<>();

        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i);

            if (!Comparator.isCodeInCodelist(code, codeList)) {
                if (!result.containsKey(code)) {
                    result.put(code, new ArrayList<>());
                }

                List<Integer> temp = result.get(code);
                temp.add((i + 1));
                result.put(code, temp);
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

                ErrorReportEntry errorReportEntry = new ErrorReportEntry(controlCategoriTitle, controlTitle, " ", " "
                        , String.format(formattedControlText, key)
                        , errorText
                        , errorType);
                errorReport.addEntry(errorReportEntry);
            }

            return true;
        }

        return false;
    }
}