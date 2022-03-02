package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

public class ControlHarVedlegg {
    private ControlHarVedlegg() {
    }

    public static boolean doControl(ErrorReport errorReport) {
        errorReport.incrementCount();

        // sjekker om man krysset av i skjemaet om at vedlegg skal mangle
        // i så fall så er innsendingen ok
        Arguments arguments = errorReport.getArgs();

        if (arguments.harVedlegg()){
            if (arguments.hasInputContent()) {
                return false;
            } else {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                ""
                                , ""
                                , ""
                                , " "
                                , "Kontroll 0 Skal levere filuttrekk"
                                , "Det er krysset av i skjemaet at det finnes deltakere, men fil som kun inneholder et mellomrom er levert."
                                , Constants.CRITICAL_ERROR
                        )
                );

                return true;
            }

        } else {
            if (arguments.hasInputContent()) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                ""
                                , ""
                                , ""
                                , " "
                                , "Kontroll 0 Skal levere filuttrekk"
                                , "Det er krysset av i skjemaet at det ikke finnes deltakere, men filen som er levert har annet innhold enn ett mellomrom."
                                + " Kryptert fil uten innhold kan lastes ned fra https://www.ssb.no/innrapportering/kostra-innrapportering<br/>"
                                + " -> Kontrollprogram og programmer til fagsystem for kommuner og leverandører<br/>"
                                + " -> Kvalifiseringsstønad<br/>"
                                + " -> Tom, kryptert fil (for dem som ikke har noen mottakere av kvalifiseringsstønad i 2021)<br/>"
                                , Constants.CRITICAL_ERROR
                        )
                );

                return true;

            } else {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                ""
                                , ""
                                , ""
                                , " "
                                , "Kontroll 0 Skal levere filuttrekk"
                                , "Det er krysset av i skjemaet at det ikke finnes deltakere og fil som kun inneholder et mellomrom er levert."
                                , Constants.NO_ERROR
                        )
                );

                return false;
            }


        }
    }
}

