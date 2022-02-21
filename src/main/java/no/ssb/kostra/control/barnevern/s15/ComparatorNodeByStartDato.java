package no.ssb.kostra.control.barnevern.s15;

import javax.xml.xpath.XPathExpressionException;
import java.time.LocalDate;
import java.util.Comparator;

public class ComparatorNodeByStartDato implements Comparator<StructuredNode> {

    @Override
    public int compare(StructuredNode o1, StructuredNode o2) {
        try {
            String datoFormatLangt = "yyyy-MM-dd";
            LocalDate d1 = o1.assignDateFromString(o1.queryString("StartDato"), datoFormatLangt);
            LocalDate d2 = o2.assignDateFromString(o2.queryString("StartDato"), datoFormatLangt);

            if (d1 != null && d2 != null) {
                if (d1.isBefore(d2)) {
                    return 1;

                } else if (d1.isAfter(d2)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        } catch (XPathExpressionException ignored) {
        }

        return 0;
    }
}
