package no.ssb.kostra.control.barnevern.s15;

import org.joda.time.DateTime;

import javax.xml.xpath.XPathExpressionException;
import java.util.Comparator;

public class ComparatorNodeByStartDato implements Comparator<StructuredNode> {

	@Override
	public int compare(StructuredNode o1, StructuredNode o2) {
		try {
			DateTime d1 = o1.assignDateFromString(o1.queryString("StartDato"), no.ssb.kostra.control.Constants.datoFormatLangt);
			DateTime d2 = o2.assignDateFromString(o2.queryString("StartDato"), no.ssb.kostra.control.Constants.datoFormatLangt);

			if (d1 != null && d2 != null) {
				if (d1.isBefore(d2)) {
					return 1;

				} else if (d1.isAfter(d2)) {
					return -1;
				} else {
					return 0;
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
}
