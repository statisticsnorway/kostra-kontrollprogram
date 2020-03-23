package no.ssb.kostra.controlprogram;

import no.ssb.kostra.control.ErrorReport;

public class CLI {
    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);

        ErrorReport er;
        switch (arguments.getSkjema()) {
            case "0AK1":
            case "0AK2":
            case "0AK3":
            case "0AK4":
                er = new no.ssb.kostra.control.regnskap.regn0AK.Main(arguments).doControls();
                break;
            default:
                er = new ErrorReport();
        };


    }
}
