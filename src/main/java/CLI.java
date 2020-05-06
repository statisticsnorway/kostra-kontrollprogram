import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.controlprogram.Arguments;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CLI {
    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);
        ErrorReport er;

        if (List.of("0AK1", "0AK2", "0AK3", "0AK4",
                "0BK1", "0BK2", "0BK3", "0BK4",
                "0CK1", "0CK2", "0CK3", "0CK4",
                "0DK1", "0DK2", "0DK3", "0DK4").contains(arguments.getSkjema())){
            er = no.ssb.kostra.control.regnskap.kvartal.Main.doControls(arguments);

        } else if (List.of("0A", "0B", "0C", "0D",
                "0I", "0J", "0K", "0L",
                "0M", "0N", "0P", "0Q").contains(arguments.getSkjema())){
            er = no.ssb.kostra.control.regnskap.kostra.Main.doControls(arguments);

        } else if (List.of("0F", "0G").contains(arguments.getSkjema())){
            er = no.ssb.kostra.control.regnskap.kirkekostra.Main.doControls(arguments);

        } else if (List.of("0X", "0Y").contains(arguments.getSkjema())){
            er = no.ssb.kostra.control.regnskap.helseforetak.Main.doControls(arguments);

        } else if ("11".equalsIgnoreCase(arguments.getSkjema())){
            er = no.ssb.kostra.control.skjema.s11_sosialhjelp.Main.doControls(arguments);

        } else if ("11C".equalsIgnoreCase(arguments.getSkjema())){
            er = no.ssb.kostra.control.skjema.s11c_kvalifiseringsstonad.Main.doControls(arguments);

        } else {
            er = new ErrorReport();
        }

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(er.generateReport());
        //        System.out.print( er.generateReport());
    }
}
