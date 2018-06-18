package no.ssb.kostra.control.sensitiv.famvern_53;

/*
*/

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.Control;
import no.ssb.kostra.control.SingleRecordErrorReport;
import no.ssb.kostra.utils.Regioner;

import java.io.*;
import java.util.Calendar;
import java.util.Vector;

public final class Main {
    private final String lf = Constants.lineSeparator;
    private final String VERSION = "53." + Constants.kostraYear + ".01";
    private String regionNumber;
    private String kontorNumber;
    private File sourceFile;
    private File reportFile;
    private Control[] controls;
    private Vector<String[]> errorLines = new Vector<String[]>();

    public Main
            (String regionNumber, File sourceFile, File reportFile, String kontorNumber) {
        this.regionNumber = regionNumber;
        this.kontorNumber = kontorNumber;
        this.sourceFile = sourceFile;
        this.reportFile = reportFile;
        initControls();
    }

    public int start() throws Exception {
        int lineNumber = 0;
        int error_type = Constants.NO_ERROR;

        try {
            BufferedReader reader;
            if (sourceFile == null)
                reader = new BufferedReader(new InputStreamReader(System.in));
            else
                reader = new BufferedReader(new FileReader(sourceFile));

            String line;
            String[] container;
            boolean printNewline;
            while ((line = reader.readLine()) != null) {
                if (!line.equalsIgnoreCase("")) {
                    lineNumber += 1;
                    //Sjekker recordlengde forst, fordi feil reccordlengde
                    //vil kunne odelegge mange andre kontroller
                    //(StringIndexOutOfBoundsException etc.)
                    if (!controls[0].doControl(line, lineNumber, regionNumber, "")) {
                        //Kontroll 4 er spesiell: krever kontornummer.
                        //((ControlKontornummer) controls[3]).setKontorNumber (kontorNumber);

                        printNewline = false;
                        for (int i = 1; i < controls.length; i++) {
                            if (controls[i].doControl(line, lineNumber, regionNumber, "")) {
                                container = new String[3];
                                container[0] = " " + lineNumber + "  ";
                                container[1] = line.substring(9, 18) + "  ";
                                container[2] = ((SingleRecordErrorReport) controls[i]).getErrorText();
                                errorLines.add(container);
                                printNewline = true;
                            }
                        }

                        if (printNewline) {
                            container = new String[3];
                            container[0] = "";
                            container[1] = "";
                            container[2] = "";
                            errorLines.add(container);
                        }
                    } else {
                        container = new String[3];
                        container[0] = " " + lineNumber + " ";
                        container[1] = " xxxxxxxxx ";
                        container[2] = ((SingleRecordErrorReport) controls[0]).getErrorText() + lf;
                        errorLines.add(container);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace(); //RAZ 20130703
            //throw new UnreadableDataException();
        }

        try {
            BufferedWriter writer = null;
            if (reportFile != null)
                writer = new BufferedWriter(new FileWriter(reportFile));

            StringBuffer report = new StringBuffer();
            report.append("-------------------------------------------------" + lf);
            report.append(lf);
            report.append(" Feilmeldingsrapport for " + regionNumber + " " + Regioner.getRegionName(regionNumber) + lf);
            report.append(lf);
            report.append(" Kontornummer " + kontorNumber + lf);
            report.append(lf);
            report.append("-------------------------------------------------" + lf + lf);
            report.append("Kontrollprogramversjon: " + VERSION + lf);
            report.append("Rapport generert: " + Calendar.getInstance().getTime() + lf);
            report.append("Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf);
            report.append("Type filuttrekk: Familievern 53 " + Constants.kostraYear + lf + lf);

            int numOfErrors = 0;
            int control_error_type;
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < controls.length; i++) {
                if (controls[i].foundError()) {
                    control_error_type = controls[i].getErrorType();
                    if (control_error_type > error_type)
                        error_type = control_error_type;

                    if (control_error_type == Constants.CRITICAL_ERROR)
                        buffer.append(Constants.CRITICAL_ERROR_TEXT_MSG + lf);

                    buffer.append(controls[i].getErrorReport(lineNumber));
                    numOfErrors += 1;
                }
            }

            if (numOfErrors > 0) {
                int errorLinesSize = errorLines.size();
                if (errorLinesSize > 0) {
                    String[] container;

                    report.append(lf + "Opplisting av feil pr. record (recordnr., journalnr., kontrolltekst):" + lf +
                            "---------------------------------------------------------------------" + lf + lf);

                    for (int i = 0; i < errorLinesSize; i++) {
                        container = (String[]) errorLines.elementAt(i);
                        report.append(container[0] + container[1] + container[2] + lf);
                    }
                }
                report.append(lf + "Oppsummering pr. kontroll:" + lf +
                        "-------------------------------------------------" + lf + lf);
                report.append("Følgende " + (numOfErrors == 1 ? "kontroll" : (numOfErrors + " kontroller")) + " har funnet feil eller advarsler:" + lf + lf);
                report.append(buffer);
            } else {
                report.append("Ingen feil funnet!");
            }

            if (reportFile != null) {
                writer.write(report.toString());
                writer.close();
            } else {
                System.out.println(report);
            }
        } catch (Exception e) {
            System.out.println("Can't write report file: ");
            System.out.println(e.toString());
            ;
            System.exit(Constants.IO_ERROR);
        }

        return error_type;
    }

    private void initControls() {
        controls = new Control[15];
        controls[0] = new ControlRecordlengde();
        controls[1] = new ControlNumericalFields();
        controls[2] = new ControlFylkesnummer();
        controls[3] = new ControlKontornummer();
        controls[4] = new ControlFylkeKontornummer();
        controls[5] = new ControlTiltak("K6", "Grupper for publikum", 31);
        controls[6] = new ControlTimer("K7", "Grupper for publikum", 31, 32);
        controls[7] = new ControlTiltak("K8", "Samlivskurs", 41);
        controls[8] = new ControlTimer("K9", "Samlivskurs", 41, 42);
        controls[9] = new ControlTiltak("K10", "Andre tiltak mot publikum", 51);
        controls[10] = new ControlTimer("K11", "Andre tiltak mot publikum", 51, 52);
        controls[11] = new ControlTiltak("K12", "Undervisning/veiledning studenter", 61);
        controls[12] = new ControlTimer("K13", "Undervisning/veiledning studenter", 61, 62);
        controls[13] = new ControlTiltak("K14", "Veiledning/konsultasjon for hjelpeapparatet", 71);
        controls[14] = new ControlTimer("K15", "Veiledning/konsultasjon for hjelpeapparatet", 71, 72);
        controls[15] = new ControlTiltak("K16", "Kurs/undervisning for hjelpeapparatet", 81);
        controls[16] = new ControlTimer("K17", "Kurs/undervisning for hjelpeapparatet", 81, 82);
        controls[17] = new ControlTiltak("K18", "Informasjon i media", 91);
        controls[18] = new ControlTimer("K19", "Informasjon i media", 91, 92);
        controls[19] = new ControlTiltak("K20", "Tilsyn", 101);
        controls[20] = new ControlTimer("K21", "Tilsyn", 101, 102);
        controls[21] = new ControlTiltak("K22", "Foreldreveiledning", 111);
        controls[22] = new ControlTimer("K23", "Foreldreveiledning", 111, 112);
        controls[23] = new ControlTiltak("K24", "Annet", 121);
        controls[24] = new ControlTimer("K25", "Annet", 121, 122);
    }
}
