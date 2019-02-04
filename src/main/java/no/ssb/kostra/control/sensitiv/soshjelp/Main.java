package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 *
 */

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.Control;
import no.ssb.kostra.control.SingleRecordErrorReport;
import no.ssb.kostra.control.UnreadableDataException;
import no.ssb.kostra.utils.Regioner;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public final class Main {
    private final String lf = Constants.lineSeparator;
    private final String VERSION = "11." + Constants.kostraYear + ".01";
    private String regionNumber;
    private File sourceFile;
    private File reportFile;
    private List<Control> controls;
    private Vector<String[]> errorLines = new Vector<String[]>();

    public Main(String regionNumber, File sourceFile, File reportFile) {
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
                    if (!controls.get(0).doControl(line, lineNumber, regionNumber, "")) {
                        printNewline = false;
                        for (int i = 1; i < controls.size(); i++) {
                            if (controls.get(i).doControl(line, lineNumber, regionNumber, "")) {
                                container = new String[4];
                                container[0] = " " + lineNumber + "  ";
                                container[1] = line.substring(18, 29) + "  ";
                                container[2] = ((SingleRecordErrorReport) controls.get(i)).getErrorText() +
                                        (controls.get(i).getErrorType() == Constants.CRITICAL_ERROR ? Constants.CRITICAL_ERROR_SHORT_TEXT_MSG : "") + "  ";
                                container[3] = line.substring(259, 269); // legger til SakNr
                                errorLines.add(container);
                                printNewline = true;
                            }
                        }

                        if (printNewline) {
                            container = new String[4];
                            container[0] = "";
                            container[1] = "";
                            container[2] = "";
                            container[3] = "";
                            errorLines.add(container);
                        }
                    } else {
                        container = new String[4];
                        container[0] = " " + lineNumber + " ";
                        container[1] = " xxxxxxxxx ";
                        container[2] = ((SingleRecordErrorReport) controls.get(0)).getErrorText() + (controls.get(0).getErrorType() == Constants.CRITICAL_ERROR ? Constants.CRITICAL_ERROR_SHORT_TEXT_MSG : "") + " ";
                        container[3] = line.substring(259, 269);
                        errorLines.add(container);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            report.append("Type filuttrekk: Sosialhjelp " + Constants.kostraYear + lf + lf);

            int numOfErrors = 0;
            int control_error_type;
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < controls.size(); i++) {
                if (controls.get(i).foundError()) {
                    control_error_type = controls.get(i).getErrorType();
                    if (control_error_type > error_type)
                        error_type = control_error_type;

                    if (control_error_type == Constants.CRITICAL_ERROR)
                        buffer.append(Constants.CRITICAL_ERROR_TEXT_MSG + lf);

                    buffer.append(controls.get(i).getErrorReport(lineNumber));
                    numOfErrors += 1;
                }
            }

            if (numOfErrors > 0) {
                int errorLinesSize = errorLines.size();
                if (errorLinesSize > 0) {
                    String[] container;

                    report.append(lf + "Opplisting av feil pr. record (recordnr., fødselsnr., kontrolltekst):" + lf +
                            "---------------------------------------------------------------------" + lf + lf);

                    for (int i = 0; i < errorLinesSize; i++) {
                        container = (String[]) errorLines.elementAt(i);
                        report.append(container[0] + container[1] + container[2] + container[3] + lf);
                    }

                }
                report.append(lf + "Oppsummering pr. kontroll:" + lf +
                        "-------------------------------------------------" + lf + lf);
                report.append("Følgende " + (numOfErrors == 1 ? "kontroll" : (numOfErrors + " kontroller")) + " har funnet feil eller advarsler:" + lf + lf);
                report.append(buffer);
                report.append(controls.get(controls.size() - 1).getErrorReport(lineNumber));
            } else {
                if (lineNumber == 0) {
                    report.append("Finner ingen data!");
                    error_type = Constants.CRITICAL_ERROR;
                } else {
                    report.append("Ingen feil funnet!");
                    report.append(controls.get(controls.size() - 1).getErrorReport(lineNumber));
                }
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
            System.exit(Constants.IO_ERROR);
        }

        return error_type;
    }

    private void initControls() {
        controls = new ArrayList<>();
        controls.add(new ControlRecordlengde());
        controls.add(new ControlNumericalFields());
        controls.add(new ControlKommunenummer());
        controls.add(new ControlBydelsnummer());
        controls.add(new ControlAargang());
        controls.add(new ControlFodselsnummer());
        controls.add(new ControlAlderUnder18());
        controls.add(new ControlAlderOver99());
        controls.add(new ControlKjonn());
        controls.add(new ControlSivilstatus());
        controls.add(new ControlBarnUnder18GyldigeVerdier());
        controls.add(new ControlBarnUnder18Antall());
        controls.add(new ControlBarnUnder18());
        controls.add(new ControlBarnUnder18Mange());
        controls.add(new ControlViktigsteKildeTilLivsopphold());
        controls.add(new ControlViktigsteKildeTilLivsoppholdArbeid_ArbInntekt());
        controls.add(new ControlViktigsteKildeTilLivsoppholdArbeid_KursLonn());
        controls.add(new ControlViktigsteKildeTilLivsoppholdArbeid_StipendLaan());
        controls.add(new ControlViktigsteKildeTilLivsoppholdArbeid_IntroStotte());
        controls.add(new ControlViktigsteKildeTilLivsoppholdArbeid_KvalStonad());
        controls.add(new ControlViktigsteKildeTilLivsoppholdTrygd());
        controls.add(new ControlTrygdesystemAlderOver66());
        controls.add(new ControlTrygdesystemAlderUnder66());
        controls.add(new ControlTrygdesystemBarn());
        controls.add(new ControlTrygdesystemArbeidssituasjon());
        controls.add(new Control24B());
        controls.add(new ControlArbeidssituasjon());
        controls.add(new ControlStonadsmaaneder());
        controls.add(new ControlStonadssum());
        controls.add(new ControlVarighetStonadssum());
        controls.add(new ControlStonadssumVarighet());
        controls.add(new ControlStonadssumStorrelseMax());
        controls.add(new ControlStonadssumStorrelseMin());
        controls.add(new ControlGjeldsRadgivning());
        controls.add(new ControlIndividuellPlan());
        controls.add(new ControlBoligsituasjon());
        controls.add(new Control36());
        controls.add(new Control37());
        controls.add(new ControlDUFnummer());
        controls.add(new Control39());
        controls.add(new Control40());
        controls.add(new Control41());
        controls.add(new Control42());
        controls.add(new Control43());
        controls.add(new Tilbakemeldingsliste());
    }
}
