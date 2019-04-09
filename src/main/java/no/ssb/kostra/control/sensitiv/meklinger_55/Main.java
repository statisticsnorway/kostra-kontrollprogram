package no.ssb.kostra.control.sensitiv.meklinger_55;


import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.Control;
import no.ssb.kostra.control.SingleRecordErrorReport;
import no.ssb.kostra.control.UnreadableDataException;
import no.ssb.kostra.utils.Regioner;

import java.io.*;
import java.util.Calendar;
import java.util.Vector;

public final class Main {
    private final String lf = Constants.lineSeparator;
    private final String VERSION = "55." + Constants.kostraYear + ".01";
    private String regionNumber;
    private File sourceFile;
    private File reportFile;
    private Control[] controls;
    private Vector<String[]> errorLines = new Vector<String[]>();

    public Main
            (String regionNumber, File sourceFile, File reportFile)
    {
        this.regionNumber = regionNumber;
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
            e.printStackTrace(); //RAZ 20140724
            throw new UnreadableDataException();
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
            report.append("-------------------------------------------------" + lf + lf);
            report.append("Kontrollprogramversjon: " + VERSION + lf);
            report.append("Rapport generert: " + Calendar.getInstance().getTime() + lf);
            report.append("Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf);
            report.append("Type filuttrekk: Meklinger  55 " + Constants.kostraYear + lf + lf);

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
        controls[3] = new ControlDubletter();
        controls[4] = new Control5();
        controls[5] = new Control6();
        controls[6] = new Control7();
        controls[7] = new Control8();
        controls[8] = new Control9();
        controls[9] = new Control10();
        controls[10] = new Control11();
        controls[11] = new Control12();
        controls[12] = new Control13();
        controls[13] = new Control14();
        controls[14] = new Control15();
    }
}
