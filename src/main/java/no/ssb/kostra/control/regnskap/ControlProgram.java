package no.ssb.kostra.control.regnskap;

import no.ssb.kostra.control.Constants;
//import no.ssb.kostra.utils.RegionerKvartal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Vector;


public class ControlProgram {

    private final String lf = Constants.lineSeparator;
    private String regionNumber;
    private File sourceFile;
    private File reportFile;
    private String typeRegnskap;
    private String statistiskEnhet;


    public ControlProgram
            (String regionNumber, File sourceFile,
             File reportFile, String typeRegnskap, String statistiskEnhet) {

        this.regionNumber = regionNumber;
        this.sourceFile = sourceFile;
        this.reportFile = reportFile;
        this.typeRegnskap = typeRegnskap;
        this.statistiskEnhet = statistiskEnhet;
    }


    public int start() throws Exception {

        if (typeRegnskap == null) {
            throw new IllegalArgumentException("Type regnskap er ikke angitt.");
        }

        // Kvartalsregnskap fra fylkene kommer inn med typeRegnskap=0AK*
        // (for 0C-regnskap) eller typeRegnskap=0BK* (for 0D-regnskap).
        // (P.g.a. lazy programming på Hubbus.)
        // Retter dette her.
        if (regionNumber.substring(2, 3).equalsIgnoreCase("00")) {

            if (typeRegnskap.equalsIgnoreCase("0AK1") ||
                    typeRegnskap.equalsIgnoreCase("0AK2") ||
                    typeRegnskap.equalsIgnoreCase("0AK3") ||
                    typeRegnskap.equalsIgnoreCase("0AK4")) {

                typeRegnskap = "0C" + typeRegnskap.substring(2);

            } else if (typeRegnskap.equalsIgnoreCase("0BK1") ||
                    typeRegnskap.equalsIgnoreCase("0BK2") ||
                    typeRegnskap.equalsIgnoreCase("0BK3") ||
                    typeRegnskap.equalsIgnoreCase("0BK4")) {

                typeRegnskap = "0D" + typeRegnskap.substring(2);

            }

        }

        String[] regnskapRecords = getRegnskapRecords();
        String typeRegnskapInFile = getTypeRegnskap(regnskapRecords);

        // Det forventes at de to forste tegnene i typeRegnskap tilsvarer
        // type regnskap angitt i filuttrekket.

        // Vi sjekker om det er samsvar mellom angitt regnskapstype og faktisk
        // innhold i filuttrekket.
        if (!isUniform(regnskapRecords, typeRegnskap)) {

            String message;

            if (typeRegnskapInFile.equalsIgnoreCase("")) {

                message =
                        lf + "************************************************************************" +
                                lf +
                                lf + " Kontrollprogrammet er ikke i stand til å bestemme regnskapstype." +
                                lf +
                                lf + " Sjekk at første linje i kildefilen (filuttrekket) inneholder en record, " +
                                lf + " og at de to første posisjonene i denne angir en gyldig regnskapstype." +
                                lf +
                                lf + "************************************************************************" +
                                lf;

            } else {

                message =
                        lf + "************************************************************************" +
                                lf +
                                lf + " Innholdet i kildefilen (filuttrekket) stemmer ikke overens med " +
                                lf + " angitt type regnskap (\"" + typeRegnskap.substring(0, 2) + "\")." +
                                lf +
                                lf + " Sjekk at det er samsvar mellom innholdet i kildefilen (filuttrekket)" +
                                lf + " og den regnskapstype som er valgt." +
                                lf +
                                lf + "************************************************************************" +
                                lf;
            }
            System.out.println(message);
            return Constants.CRITICAL_ERROR;
        }

        return getControlResult(typeRegnskap, regnskapRecords);
    }


    private String getTypeRegnskap(String[] regnskap) {

        for (int i = 0; i < regnskap.length; i++)
            if (regnskap[i].length() >= 2)
                return regnskap[i].substring(0, 2);

        return "";
    }

    private boolean isUniform(String[] regnskap, String typeRegnskap) {

        for (int i = regnskap.length - 1; i >= 0; i--)
            if (regnskap[i].length() >= 2)
                if (!regnskap[i].substring(0, 2).equalsIgnoreCase(typeRegnskap.substring(0, 2)))
                    return false;

        return true;
    }


  
/*  
  private void writeErrorToReportFile (String errorMessage)
  {
    try
    {
      BufferedWriter fileWriter = 
          new BufferedWriter (new FileWriter (reportFile));
      fileWriter.write(errorMessage);
      fileWriter.close();
    }
    catch (Exception e)
    {
      System.out.println(e.toString());
      System.exit (Constants.IO_ERROR);
    }    
  }
*/


    private String[] getRegnskapRecords() throws Exception {

        String line;
        Vector<String> buffer = new Vector<String>();
        BufferedReader reader;

        if (sourceFile == null)
            reader = new BufferedReader(new InputStreamReader(System.in));
        else
            reader = new BufferedReader(new FileReader(sourceFile));

        while ((line = reader.readLine()) != null)
            buffer.add(line);

        reader.close();

//    int numberOfLines = buffer.size();
//    String[] regnskap = new String[numberOfLines];
//    
//    for (int i=0; i<numberOfLines; i++)
//      regnskap[i] = (String) buffer.elementAt(i);
//    
//    return regnskap;
        return buffer.toArray(new String[0]);
    } // End method getRegnskap.


    private int getControlResult(String typeRegnskap, String[] regnskap)
            throws UnknownRegnskapException {

        int error_type;

        if (typeRegnskap.equalsIgnoreCase("0AK1") ||
                typeRegnskap.equalsIgnoreCase("0AK2") ||
                typeRegnskap.equalsIgnoreCase("0AK3") ||
                typeRegnskap.equalsIgnoreCase("0AK4")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0Akvartal.Main
                    (regionNumber, sourceFile, reportFile, regnskap, Integer.valueOf(typeRegnskap.substring(3, 4))).start();

        } else if (typeRegnskap.equalsIgnoreCase("0BK1") ||
                typeRegnskap.equalsIgnoreCase("0BK2") ||
                typeRegnskap.equalsIgnoreCase("0BK3") ||
                typeRegnskap.equalsIgnoreCase("0BK4")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0Bkvartal.Main
                    (regionNumber, sourceFile, reportFile, regnskap, Integer.valueOf(typeRegnskap.substring(3, 4))).start();

        } else if (typeRegnskap.equalsIgnoreCase("0CK1") ||
                typeRegnskap.equalsIgnoreCase("0CK2") ||
                typeRegnskap.equalsIgnoreCase("0CK3") ||
                typeRegnskap.equalsIgnoreCase("0CK4")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0Ckvartal.Main
                    (regionNumber, sourceFile, reportFile, regnskap, Integer.valueOf(typeRegnskap.substring(3, 4))).start();

        } else if (typeRegnskap.equalsIgnoreCase("0DK1") ||
                typeRegnskap.equalsIgnoreCase("0DK2") ||
                typeRegnskap.equalsIgnoreCase("0DK3") ||
                typeRegnskap.equalsIgnoreCase("0DK4")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0Dkvartal.Main
                    (regionNumber, sourceFile, reportFile, regnskap, Integer.valueOf(typeRegnskap.substring(3, 4))).start();

        } else if (typeRegnskap.equalsIgnoreCase("0A")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0A.Main
                    (regionNumber, sourceFile, reportFile, regnskap).start();

        } else if (typeRegnskap.equalsIgnoreCase("0B")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0B.Main
                    (regionNumber, sourceFile, reportFile, regnskap).start();

        } else if (typeRegnskap.equalsIgnoreCase("0C")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0C.Main
                    (regionNumber, sourceFile, reportFile, regnskap).start();

        } else if (typeRegnskap.equalsIgnoreCase("0D")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0D.Main
                    (regionNumber, sourceFile, reportFile, regnskap).start();

        } else if (typeRegnskap.equalsIgnoreCase("0I")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0I.Main
                    (regionNumber, sourceFile, reportFile, regnskap, statistiskEnhet).start();

        } else if (typeRegnskap.equalsIgnoreCase("0J")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0J.Main
                    (regionNumber, sourceFile, reportFile, regnskap, statistiskEnhet).start();

        } else if (typeRegnskap.equalsIgnoreCase("0K")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0K.Main
                    (regionNumber, sourceFile, reportFile, regnskap, statistiskEnhet).start();

        } else if (typeRegnskap.equalsIgnoreCase("0L")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0L.Main
                    (regionNumber, sourceFile, reportFile, regnskap, statistiskEnhet).start();

        } else if (typeRegnskap.equalsIgnoreCase("0M")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0M.Main
                    (regionNumber, sourceFile, reportFile, regnskap).start();

        } else if (typeRegnskap.equalsIgnoreCase("0N")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0N.Main
                    (regionNumber, sourceFile, reportFile, regnskap).start();

        } else if (typeRegnskap.equalsIgnoreCase("0P")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0P.Main
                    (regionNumber, sourceFile, reportFile, regnskap).start();

        } else if (typeRegnskap.equalsIgnoreCase("0Q")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0Q.Main
                    (regionNumber, sourceFile, reportFile, regnskap).start();

        } else if (typeRegnskap.equalsIgnoreCase("0F")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0F.Main
                    (regionNumber, sourceFile, reportFile, regnskap, statistiskEnhet).start();

        } else if (typeRegnskap.equalsIgnoreCase("0G")) {

            error_type = new no.ssb.kostra.control.regnskap.regn0G.Main
                    (regionNumber, sourceFile, reportFile, regnskap, statistiskEnhet).start();

        } else if (typeRegnskap.equalsIgnoreCase("0X")) {

            error_type = new no.ssb.helseforetak.control.regnskap.regn0X.Main
                    (regionNumber, sourceFile, reportFile, regnskap, statistiskEnhet).start();

        } else if (typeRegnskap.equalsIgnoreCase("0Y")) {

            error_type = new no.ssb.helseforetak.control.regnskap.regn0Y.Main
                    (regionNumber, sourceFile, reportFile, regnskap, statistiskEnhet).start();

        } else {

            String message =
                    lf + "************************************************************************" +
                            lf +
                            lf + " De to første posisjonene i første linje i kildefilen (filuttrekket)" +
                            lf + " inneholder \"" + typeRegnskap.substring(0, 2) + "\", som ikke er en gyldig regnskapstype." +
                            lf +
                            lf + " Sjekk at første linje i kildefilen (filuttrekket) inneholder en record, " +
                            lf + " og at de to første posisjonene i denne angir en gyldig regnskapstype." +
                            lf +
                            lf + "************************************************************************" +
                            lf;

            throw new UnknownRegnskapException(message);
        }

        return error_type;
    }


} // End class ControlProgram.