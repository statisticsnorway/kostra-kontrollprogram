package no.ssb.kostra.control.regnskap.regn0I;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.Control;
import no.ssb.kostra.control.ControlRecord;
import no.ssb.kostra.utils.Regioner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

public final class Main {
    private final String lf = Constants.lineSeparator;
    private final String VERSION = "0I." + Constants.kostraYear + ".01";
    private String regionNumber;
    private File sourceFile;
    private File reportFile;
    private String[] regnskap;
    private Control[] controls;
    private String statistiskEnhet;


    public Main
            (String regionNumber, File sourceFile, File reportFile, String[] regnskap, String statistiskEnhet) {
        this.regionNumber = regionNumber;
        this.sourceFile = sourceFile;
        this.reportFile = reportFile;
        this.regnskap = regnskap;
        this.statistiskEnhet = statistiskEnhet;
    }

    public int start() {
        int lineNumber = 0;
        int error_type = Constants.NO_ERROR;
        int numOfControlsWithError = 0;

        for (int i = 0; i < regnskap.length; i++) {

            if (!regnskap[i].equalsIgnoreCase("")) {

                lineNumber += 1;

                //Sjekker recordlengde forst, fordi feil reccordlengde
                //vil kunne odelegge mange andre kontroller
                //(StringIndexOutOfBoundsException etc.)
                if (!controls[0].doControl(regnskap[i], lineNumber, regionNumber, statistiskEnhet)) {

                    for (int j = 1; j < controls.length; j++)
                        controls[j].doControl(regnskap[i], lineNumber, regionNumber, statistiskEnhet);

                }
            }
        }

        try {
            BufferedWriter fileWriter = null;
            if (reportFile != null)
                fileWriter = new BufferedWriter(new FileWriter(reportFile));

            String report = "-------------------------------------------------" + lf;
            report += lf;
            report += " Feilmeldingsrapport for " + regionNumber + " " + Regioner.getRegionName(regionNumber) + lf;
            report += lf;
            report += "-------------------------------------------------" + lf + lf;

            report += "Kontrollprogramversjon: " + VERSION + lf;
            report += "Rapport generert: " + Calendar.getInstance().getTime() + lf;
            report += "Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf;
            report += "Type regnskap: 0I - Bevilgningsregnskap kommunale sÃ¦rbedrifter." + lf + lf;

            int numOfErrors = 0;
            int control_error_type;
            String buffer = "";

            for (int i = 0; i < controls.length; i++) {
                if (controls[i].foundError()) {
                    numOfControlsWithError++;

                    control_error_type = controls[i].getErrorType();
                    if (control_error_type > error_type)
                        error_type = control_error_type;

                    if (control_error_type == Constants.CRITICAL_ERROR)
                        buffer += Constants.CRITICAL_ERROR_TEXT_MSG + lf;

                    buffer += controls[i].getErrorReport(lineNumber);
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
            ;
            System.exit(Constants.IO_ERROR);
        }

        if (numOfControlsWithError == 1 && controls[18].foundError())
            error_type = Constants.NO_ERROR;

        return error_type;
    }

    private void initControls() {
        controls = new Control[23];
        controls[0] = new ControlRecordlengde();
        controls[1] = new ControlAargang();
        controls[2] = new ControlKvartal();
        controls[3] = new ControlKommunenummer();
        controls[4] = new ControlOrgNummer();
        controls[5] = new ControlKontoklasse();
        controls[6] = new ControlFunksjoner();
        controls[7] = new ControlArter();
        controls[8] = new ControlKontoklasseOgFunksjon();
        controls[9] = new ControlKontoklasseOgArtInvestering();
        controls[10] = new ControlKontoklasseOgArtDrift();
        controls[11] = new ControlDubletter();
        controls[12] = new ControlSummering();
        controls[13] = new ControlOverforingDriftInvestering();
        controls[14] = new ControlAvskrivinger();
        controls[15] = new Control190();
        controls[16] = new ControlVersjon();
        controls[17] = new Control490();
        controls[18] = new ControlRecord();
        controls[19] = new ControlNumericalFields();
        controls[20] = new Control24a();
        controls[21] = new Control24b();
        controls[22] = new Control25a();

//    controls[13] = new ControlInterneOverforinger();
//    controls[17] = new Control290();
//    controls[18] = new ControlGyldigOrgNr();
//    controls[22] = new no.ssb.kostra.control.regnskap.ControlOrgNr();
    }
}
