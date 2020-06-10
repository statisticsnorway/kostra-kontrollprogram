package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.Map;
import java.util.TreeMap;

public final class Main {
	public static ErrorReport doControls(Arguments args) {
		ErrorReport er = new ErrorReport(args);
		String regionNumber = args.getRegion();
		Map<String, String> avgiver = new TreeMap<>();
		XMLReader r = new XMLReader();

		try {
			r.setRegion(regionNumber);
			// We can add several handlers which are triggered for a given node
			// name. The complete sub-dom of this node is then parsed and made
			// available as a StructuredNode
			r.addHandler("Avgiver", new AvgiverNodeHandler(er, regionNumber, avgiver));
			r.addHandler("Individ", new IndividNodeHandler(er, regionNumber, avgiver));
			r.parse(args.getInputContentAsInputStream());

		} catch (Exception e) {
			e.printStackTrace();
			// throw new UnreadableDataException();
		}

		IndividNodeHandler.reset();

		return er;
	}
}
