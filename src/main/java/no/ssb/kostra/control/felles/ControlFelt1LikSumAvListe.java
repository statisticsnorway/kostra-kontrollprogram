package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.KostraRecord;

import java.util.List;
import java.util.stream.Collectors;

public class ControlFelt1LikSumAvListe {

    public static boolean doControl(
            final KostraRecord record, final ErrorReport errorReport,
            final ErrorReportEntry errorReportEntry, final List<List<String>> fieldLists) {

        return fieldLists.stream()
                .map(fieldList -> {
                    final var sumField = fieldList.get(0);
                    final var sum = record.getFieldAsIntegerDefaultEquals0(sumField);

                    final var aggregateFields = fieldList.subList(1, fieldList.size());
                    final var sumList = aggregateFields.stream()
                            .map(record::getFieldAsIntegerDefaultEquals0)
                            .reduce(0, Integer::sum);

                    if (!sum.equals(sumList)) {
                        final var errorText = "Summen (" + sumField + ") med verdi (" + sum + ") er ulik summen (" + sumList + ") av følgende liste "
                                + aggregateFields.stream()
                                .map(f -> "("
                                        .concat(f)
                                        .concat(" = ")
                                        .concat(record.getFieldAsIntegerDefaultEquals0(f).toString())
                                        .concat(")")
                                )
                                .collect(Collectors.joining(", "));

                        errorReport.addEntry(new ErrorReportEntry(
                                errorReportEntry.getSaksbehandler()
                                , errorReportEntry.getJournalnummer()
                                , errorReportEntry.getIndividId()
                                , String.valueOf(record.getLine())
                                , errorReportEntry.getKontrollNr().concat(", ").concat(sumField)
                                , errorText
                                , errorReportEntry.getErrorType()
                        ));
                        return true;
                    }
                    return false;
                })
                .reduce(false, (result, item) -> result || item);
    }

    public static List<List<String>> createFieldList(
            final String measure, final List<String> c1, final List<String> c2) {

        return c1.stream()
                .map(c1Item -> c2.stream()
                        .map(c2Item -> String.join("_", measure, c1Item, c2Item))
                        .toList())
                .toList();
    }

    public static List<List<String>> createFieldList(
            final String measure, final List<String> c1, final String c2) {

        return List.of(c1.stream()
                .map(c1Item -> String.join("_", measure, c1Item, c2))
                .toList());
    }

    public static List<List<String>> createFieldList(final List<String> c1, final List<String> c2) {
        return c1.stream()
                .map(c1Item -> c2.stream()
                        .map(c2Item -> String.join("_", c1Item, c2Item))
                        .toList())
                .toList();
    }
}
