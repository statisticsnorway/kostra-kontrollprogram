package no.ssb.kostra.controlprogram;

import no.ssb.kostra.felles.Constants;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class CLI {

    public static void main(final String[] args) {

        int error_type_found;

        try {
            final var arguments = new Arguments(args, System.in);
            final var errorReport = ControlDispatcher.doControls(arguments);

            error_type_found = errorReport.getErrorType();

            final var printStream = new PrintStream(System.out, true, StandardCharsets.ISO_8859_1);
            printStream.print(errorReport.generateReport());

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.PARAMETER_ERROR;

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.CRITICAL_ERROR;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.SYSTEM_ERROR;
        }

        System.exit(error_type_found);
    }
}
