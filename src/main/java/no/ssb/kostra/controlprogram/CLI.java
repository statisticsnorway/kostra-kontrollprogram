package no.ssb.kostra.controlprogram;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CLI {
    public static void main(String[] args) {
        ErrorReport er;
        int error_type_found;
        BufferedWriter fileWriter = null;

        try {
            Arguments arguments = new Arguments(args);

            if (List.of("0AK1", "0AK2", "0AK3", "0AK4",
                    "0BK1", "0BK2", "0BK3", "0BK4",
                    "0CK1", "0CK2", "0CK3", "0CK4",
                    "0DK1", "0DK2", "0DK3", "0DK4").contains(arguments.getSkjema())) {
                er = no.ssb.kostra.control.regnskap.kvartal.Main.doControls(arguments);

            } else if (List.of("0A", "0B", "0C", "0D",
                    "0I", "0J", "0K", "0L",
                    "0M", "0N", "0P", "0Q").contains(arguments.getSkjema())) {
                er = no.ssb.kostra.control.regnskap.kostra.Main.doControls(arguments);

            } else if (List.of("0F", "0G").contains(arguments.getSkjema())) {
                er = no.ssb.kostra.control.regnskap.kirkekostra.Main.doControls(arguments);

            } else if (List.of("0X", "0Y").contains(arguments.getSkjema())) {
                er = no.ssb.kostra.control.regnskap.helseforetak.Main.doControls(arguments);

            } else if ("11".equalsIgnoreCase(arguments.getSkjema())) {
                er = no.ssb.kostra.control.sosial.s11_sosialhjelp.Main.doControls(arguments);

            } else if ("11C".equalsIgnoreCase(arguments.getSkjema())) {
                er = no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.Main.doControls(arguments);

            } else if ("52AF".equalsIgnoreCase(arguments.getSkjema())) {
                er = no.ssb.kostra.control.famvern.s52a.Main.doControls(arguments);

            } else if ("52BF".equalsIgnoreCase(arguments.getSkjema())) {
                er = no.ssb.kostra.control.famvern.s52b.Main.doControls(arguments);

            } else if ("53F".equalsIgnoreCase(arguments.getSkjema())) {
                er = no.ssb.kostra.control.famvern.s53.Main.doControls(arguments);

            } else if ("55F".equalsIgnoreCase(arguments.getSkjema())) {
                er = no.ssb.kostra.control.famvern.s55.Main.doControls(arguments);

            } else {
                er = new ErrorReport();
            }

            error_type_found = er.getErrorType();

            if (arguments.getOutputFile() != null) {
                fileWriter = new BufferedWriter(new FileWriter(arguments.getOutputFile()));
            }

            if (fileWriter != null) {
                fileWriter.write(er.generateReport());
                fileWriter.close();
            } else {
                PrintStream out = new PrintStream(System.out, true, StandardCharsets.ISO_8859_1);
                out.println(er.generateReport());
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.PARAMETER_ERROR;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.IO_ERROR;

//        } catch (UnreadableDataException e) {
//            System.out.println(e.getExceptionMessage());
//            error_type_found = Constants.CRITICAL_ERROR;

        } catch (NullPointerException e) {
            error_type_found = Constants.CRITICAL_ERROR;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.SYSTEM_ERROR;
        }

        System.exit(error_type_found);
    }
}
