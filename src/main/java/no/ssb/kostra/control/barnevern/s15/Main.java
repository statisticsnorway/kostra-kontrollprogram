package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.controlprogram.Arguments;

public final class Main {
    private Main() {
        throw new IllegalStateException("Static processing class");
    }

    public static ErrorReport doControls(final Arguments args) {
        final var errorReport = new ErrorReport(args);
        final var regionNumber = args.getRegion();
        final var xmlReader = new XMLReader();

        try {
            xmlReader.setRegion(regionNumber);
            // We can add several handlers which are triggered for a given node
            // name. The complete sub-dom of this node is then parsed and made
            // available as a StructuredNode
            xmlReader.addHandler("Avgiver", new AvgiverNodeHandler(errorReport, args));
            xmlReader.addHandler("Individ", new IndividNodeHandler(errorReport, args));
            xmlReader.parse(args.getInputContentAsInputStream());
        } catch (Exception e) {
            errorReport.addEntry(
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
        return errorReport;
    }
}
