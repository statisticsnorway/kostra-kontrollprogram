package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;
import java.util.stream.Collectors;

public class ControlFelt1LikSumAvListe {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, List<List<String>> fieldLists) {


        boolean ok = fieldLists.stream()
                .allMatch(fieldList -> {
                            String sumField = fieldList.get(0);
                            Integer sum = r.getFieldAsInteger(sumField);
                            List<String> aggregateFields = fieldList.subList(1, fieldList.size());
                            Integer sumList = aggregateFields.stream()
                                    .map(r::getFieldAsInteger)
                                    .reduce(0, Integer::sum);

                            return sum != null && sum.equals(sumList);
                        }
                );

        if (!ok) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
    public static List<List<String>> createFieldList(String measure, List<String> c1, List<String> c2) {
        List<List<String>> fieldLists = c1.stream()
                .map(c1Item -> c2.stream()
                        .map(c2Item -> String.join("_", measure, c1Item, c2Item))
                        .collect(Collectors.toList())

                )
                .collect(Collectors.toList());

        fieldLists.addAll(c2.stream()
                .map(c2Item -> c1.stream()
                        .map(c1Item -> String.join("_", measure, c1Item, c2Item))
                        .collect(Collectors.toList())

                )
                .collect(Collectors.toList())
        );
        return fieldLists;
    }

    public static List<List<String>> createFieldList(List<String> c1, List<String> c2) {
        List<List<String>> fieldLists = c1.stream()
                .map(c1Item -> c2.stream()
                        .map(c2Item -> String.join("_", c1Item, c2Item))
                        .collect(Collectors.toList())

                )
                .collect(Collectors.toList());

        fieldLists.addAll(c2.stream()
                .map(c2Item -> c1.stream()
                        .map(c1Item -> String.join("_", c1Item, c2Item))
                        .collect(Collectors.toList())

                )
                .collect(Collectors.toList())
        );
        return fieldLists;
    }
}
