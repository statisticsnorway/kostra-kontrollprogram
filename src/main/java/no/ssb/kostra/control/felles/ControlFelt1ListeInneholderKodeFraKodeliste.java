package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.KostraRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.between;

public class ControlFelt1ListeInneholderKodeFraKodeliste {

    public static boolean doControl(
            final ErrorReport errorReport, final String controlCategoryTitle,
            final String controlTitle, final String formattedControlText,
            final String fieldName, final List<KostraRecord> records,
            final List<String> codeList, final int errorType) {

        final var result = new HashMap<String, List<Integer>>();

        for (var record : records) {
            final var code = record.getFieldAsString(fieldName);

            if (!Comparator.isCodeInCodeList(code, codeList)) {
                if (!result.containsKey(code)) {
                    result.put(code, new ArrayList<>());
                }

                final var temp = result.get(code);
                temp.add(record.getFieldAsInteger("linjenummer"));
                result.put(code, temp);
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

                errorReport.addEntry(new ErrorReportEntry(controlCategoryTitle, controlTitle, " ", " "
                        , String.format(formattedControlText, key)
                        , errorText
                        , errorType));
            }
            return true;
        }
        return false;
    }
}