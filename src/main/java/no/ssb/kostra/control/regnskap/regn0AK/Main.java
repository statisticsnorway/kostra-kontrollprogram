package no.ssb.kostra.control.regnskap.regn0AK;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.felles.ControlListeInneholderKodeFraKodeliste;
import no.ssb.kostra.control.regnskap.felles.Postering;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.List;

public class Main {
    Arguments args;
    ErrorReport er;
    List<String> list1;

    public Main(Arguments args) {
        this.args = args;
        er = new ErrorReport(this.args);
        list1 = this.args.getInputFileContent();
        List<Postering> regnskap = list1.stream()
                .map();
    }

    public ErrorReport doControls(){


        ControlListeInneholderKodeFraKodeliste.run(er, ere, );


        return er;
    }
}
