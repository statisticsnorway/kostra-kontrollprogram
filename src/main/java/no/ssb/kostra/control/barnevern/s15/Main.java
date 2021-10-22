package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.controlprogram.Arguments;

public final class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        String regionNumber = args.getRegion();
        XMLReader r = new XMLReader();

        try {
            r.setRegion(regionNumber);
            // We can add several handlers which are triggered for a given node
            // name. The complete sub-dom of this node is then parsed and made
            // available as a StructuredNode
            r.addHandler("Avgiver", new AvgiverNodeHandler(er, args));
            r.addHandler("Individ", new IndividNodeHandler(er, args));
            r.parse(args.getInputContentAsInputStream());

        } catch (Exception e) {
            e.printStackTrace();
            // throw new UnreadableDataException();
            er.addEntry(
                    new ErrorReportEntry(
                            " ",
                            " ",
                            " ",
                            " ",
                            "Kontrollprogrammet",
                            "Klarer ikke å lese fil. Får feilmeldingen: " + e.getMessage(),
                            Constants.CRITICAL_ERROR)
            );
        }

        IndividNodeHandler.reset();

        return er;
    }
}
