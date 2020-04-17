package no.ssb.kostra.control.regnskap.regn0Dkvartal;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.Control;
import no.ssb.kostra.control.ControlRecord;
import no.ssb.kostra.utils.RegionerKvartal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public final class Main {
    private final String lf = Constants.lineSeparator;
    private final String VERSION = "0D-kvartal." + Constants.kvartalKostraYear + ".01";
    private int kvartal;
    private String regionNumber;
    private File sourceFile;
    private File reportFile;
    private String[] regnskap;
    private List<Control> controls;

    public Main(String regionNumber, File sourceFile, File reportFile, String[] regnskap, int kvartal) {
        this.regionNumber = regionNumber;
        this.sourceFile = sourceFile;
        this.reportFile = reportFile;
        this.regnskap = regnskap;
        this.kvartal = kvartal;
        initControls();
    }

    public int start() {
        int lineNumber = 0;
        int error_type = Constants.NO_ERROR;
        CharSequence z = "z";
        CharSequence Z = "Z";
        CharSequence tilde = "~";

        for (int i = 0; i < regnskap.length; i++) {
            if (!regnskap[i].equalsIgnoreCase("")) {
                lineNumber += 1;

                //Sjekker recordlengde forst, fordi feil reccordlengde
                //vil kunne odelegge mange andre kontroller
                //(StringIndexOutOfBoundsException etc.)
                // Legger på sjekk at linja inneholder z eller ~ så lar vi være å gjøre noe
                if (!controls.get(0).doControl(regnskap[i], lineNumber, regionNumber, "") &&
                        !(regnskap[i].contains(z) || regnskap[i].contains(Z) || regnskap[i].contains(tilde))
                        ) {

                    for (int j = 1; j < controls.size(); j++)
                        controls.get(j).doControl(regnskap[i], lineNumber, regionNumber, "");
                }
            }
        }


        try {
            BufferedWriter fileWriter = null;
            if (reportFile != null)
                fileWriter = new BufferedWriter(new FileWriter(reportFile));

            String report = "-------------------------------------------------" + lf;
            report += lf;
            report += " Feilmeldingsrapport for " + regionNumber + " " + RegionerKvartal.getRegionName(regionNumber) + lf;
            report += lf;
            report += "-------------------------------------------------" + lf + lf;

            report += "Kontrollprogramversjon: " + VERSION + lf;
            report += "Rapport generert: " + Calendar.getInstance().getTime() + lf;
            report += "Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf;
            report += "Type regnskap: 0D - Balanseregnskap fylkeskommune, kvartalsregnskap" + lf + lf;

            int numOfErrors = 0;
            int control_error_type;
            String buffer = "";

            for (int i = 0; i < controls.size(); i++) {
                if (controls.get(i).foundError()) {
                    control_error_type = controls.get(i).getErrorType();
                    if (control_error_type > error_type)
                        error_type = control_error_type;

                    if (control_error_type == Constants.CRITICAL_ERROR)
                        buffer += Constants.CRITICAL_ERROR_TEXT_MSG + lf;

                    buffer += controls.get(i).getErrorReport(lineNumber);
                    numOfErrors += 1;
                }
            }

            if (numOfErrors > 0) {
                report += "Følgende " + (numOfErrors == 1 ? "kontroll" : (numOfErrors + " kontroller")) + " har funnet feil eller advarsler:" + lf + lf;
                report += buffer;
            } else {
                if (lineNumber == 0) {
                    report += "Finner ingen data!";
                    error_type = Constants.CRITICAL_ERROR;
                } else {
                    report += "Ingen feil funnet!";
                }
            }

            if (fileWriter != null) {
                fileWriter.write(report);
                fileWriter.close();
            } else {
                System.out.println(report);
            }
        } catch (Exception e) {
            System.out.println("Can't write report file: ");
            System.out.println(e.toString());
            System.exit(Constants.IO_ERROR);
        }

        return error_type;
    }

    private void initControls() {
        controls = Arrays.asList(
                new ControlRecordlengde()
                , new ControlAargang()
                , new ControlFylkeskommunenummer()
                , new ControlKontoklasse()
                , new ControlOrgNummer()
                , new ControlKapitler()
                , new ControlSektorer()
                , new ControlDubletter()
                , new ControlKvartal(kvartal)
                , new ControlRecord()
                , new no.ssb.kostra.control.regnskap.ControlOrgNr()
        );
    }
}
