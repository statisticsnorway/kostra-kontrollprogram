package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DublettTest {
    public boolean doControl(List<Record> recordList, ErrorReport er, List<String> fieldList) {
        // Dublett kontroll
        List<String> dubletter = recordList.stream()
                .map(p -> fieldList.stream().map(p::getFieldAsTrimmedString).collect(Collectors.joining(" * ")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(p -> p.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        if (!dubletter.isEmpty()) {
            er.addEntry(
                    new ErrorReportEntry("9. Dublettkontroller", "Dubletter", " ", " "
                            , "Kontroll Dubletter"
                            , "Dubletter (lik " + String.join(" * ", fieldList) + ") summeres sammen. (Gjelder for:<br/>\n" + String.join(",<br/>\n", dubletter) + ")"
                            , Constants.NORMAL_ERROR
                    ));
        }

        return false;
    }

    @Test
    public void testDubletter(){
        System.out.println(List.of("E E", "E F", "A A", "A B", "B B", "C C", "D D", "D E").stream()
                .map(d -> Map.of("key", d.substring(0, 1), "val", d.substring(2)))
                        .collect(Collectors.groupingBy(i -> i.get("key"),
                                Collectors.mapping(k -> k. get("val"), Collectors.toList())))

                .entrySet()
                .stream()
                .filter(p -> p.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList())

        );
    }

    @Test
    public void test1(){
        List<Map<String , String>> as = new ArrayList<>();
        Map<String, String> a = new HashMap<>();
        a.put("key", "1");
        a.put("val", "ssd");
        Map<String, String> b = new HashMap<>();
        b.put("key", "1");
        b.put("val", "ssaad");
        Map<String, String> c = new HashMap<>();
        c.put("key", "2");
        c.put("val", "ssddad");
        Map<String, String> d = new HashMap<>();
        d.put("key", "2");
        d.put("val", "ssdfd");

        as.add(a);
        as.add(b);
        as.add(c);
        as.add(d);

        Map<String, List<String>> x = as.stream().collect(Collectors.groupingBy(i -> i.get("key"),
                Collectors.mapping(k -> k.get("val"), Collectors.toList())));

        System.out.println(x);
    }
}
