package no.ssb.kostra.control;

import no.ssb.kostra.utils.Regioner;
import no.ssb.kostra.control.regnskap.UnknownRegnskapException;
import no.ssb.kostra.utils.RegionerKvartal;

import java.io.*;


public final class ControlProgram {

  private String region_nr;
  private String kontor_nr;
  private String statistiskEnhet;
  private String regnskapstype;
  private File kildefil = null;
  private File rapportfil = null;
  private int type_filuttrekk;
  private static final int SOSIALHJELP = 0;
//  public static final int INTRODUKSJONSSTONAD = 1;
  private static final int BARNEVERN = 2;
  private static final int FAMILIEVERN = 3;
  private static final int REGNSKAP = 4;
  private static final int NO_CONTROL = 5;
  private static final int FAMILIEVERN_B = 6;
  private static final int KVALIFISERINGSSTONAD = 7;
  private static final int FAMILIEVERN_53 = 8;
  private static final int Meklinger_55 = 9;
  private static final int VAR_26 = 10;
  private final String lf = Constants.lineSeparator;
  private final String USAGE = "Bruk: ControlProgram <regionnummer> " +
                               "[<kildefil> <rapportfil>] <type filuttrekk> [<regnskapstype> || <kontornummer>] " +
                               "<orgnr>";


  public int doControl (String[] args) throws Exception {

    // Metoden parseArguments returnerer ikke (kaster IllegalArgumentException)
    // hvis  argumentene ikke er korrekte.
    parseArguments(args);
    int error_type_found;

    switch (type_filuttrekk) {
      case SOSIALHJELP:
        error_type_found = new no.ssb.kostra.control.sensitiv.soshjelp.Main
            (region_nr, kildefil, rapportfil).start();
        break;
//      case INTRODUKSJONSSTONAD:
//        error_type_found = new no.ssb.kostra.control.sensitiv.sosintro.Main
//            (region_nr, kildefil, rapportfil).start();
//        break;
      case KVALIFISERINGSSTONAD:
        error_type_found = new no.ssb.kostra.control.sensitiv.soskval.Main
            (region_nr, kildefil, rapportfil).start();
        break;
      case BARNEVERN:
        error_type_found = new no.ssb.kostra.control.sensitiv.barnevern.Main
            (region_nr, kildefil, rapportfil).start();
        break;
      case FAMILIEVERN:
    	error_type_found = new no.ssb.kostra.control.sensitiv.famvern.Main
            (region_nr, kildefil, rapportfil, kontor_nr).start();
        break;
      case FAMILIEVERN_B:
    	  error_type_found = new no.ssb.kostra.control.sensitiv.famvern_b.Main
            (region_nr, kildefil, rapportfil, kontor_nr).start();
        break;
      case FAMILIEVERN_53:
    	  error_type_found = new no.ssb.kostra.control.sensitiv.famvern_53.Main
              (region_nr, kildefil, rapportfil, kontor_nr).start();
          break;
      case Meklinger_55:
    	  error_type_found = new no.ssb.kostra.control.sensitiv.meklinger_55.Main
    		  (region_nr, kildefil, rapportfil).start();
          break;
      case REGNSKAP:
        error_type_found = new no.ssb.kostra.control.regnskap.ControlProgram
            (region_nr, kildefil, rapportfil, regnskapstype, statistiskEnhet).start();
        break;

 
      case NO_CONTROL:
    	  
    	  
        error_type_found = Constants.NO_ERROR;
        break;
      default:
        throw new Exception ("Intern feil!"); // Skal ikke kunne komme hit.
    }

    if (error_type_found == Constants.NO_ERROR && rapportfil == null)
      System.out.println(lf +"Lukk dette vinduet, velg filen på nytt" + lf +
       "og trykk på knappen 'Send inn' for å sende filuttrekket til SSB." + lf);

    return error_type_found;

  } // End method doControl




  private void parseArguments(String[] args)
      throws IOException, IllegalArgumentException {

    String type_filuttrekk_tmp;

//-----------------------------------------------------------------------------------------------------------------
//    ----- Argument |    0      |     1       |       2       |     3     |        4                  |    5      |
//    ----- Skjema   |  region   |  kildefil   |  rapportfil   |  1 - 10   |  regnskapstype / Kontornr.|  org.nr.  |
//    ----- 0A - 0D  |    X      |    -        |       -       |     4     |        X                  |    0      |
//    ----- 0AK-0DK  |    X      |    -        |       -       |     4     |        X                  |    0      |
//    ----- 0F-0G	 |    X      |    -        |       -       |     4     |        X                  |    X      |
//    ----- 0I-0L    |    X      |    -        |       -       |     4     |        X                  |    X      |
//    ----- 0X-0Y    |    X      |    -        |       -       |     4     |        X                  |    X      |
//    ----- 52A      |    X      |    -        |       -       |     3     |                    X      |    X      |
//    ----- 52B      |    X      |    -        |       -       |     6     |                    X      |    X      |
//    ----- 53       |    X      |    -        |       -       |     8     |                    X      |    X      |
//    ----- 55       |    X      |    -        |       -       |     9     |                    X      |    X      |
//-----------------------------------------------------------------------------------------------------------------

    if (args.length == 4 || args.length == 6) { // Versjon for les/skriv
      // fra/til standard in/out.
      region_nr = args[0]; // region

      if (args.length == 4) {
        type_filuttrekk_tmp = args[1];      //områdekode f.eks. 4 for regnskap
        kontor_nr =                         // kontornr. for FamVern
        regnskapstype = args[2];            // regnskapstypen f.eks. 0A eller 0AK3
        statistiskEnhet = args[3];          // org.nr

      } else {
        kildefil = new File(args[1]);
        rapportfil = new File (args[2]);
        type_filuttrekk_tmp = args[3];      //områdekode f.eks. 4 for regnskap
        kontor_nr =                         // kontornr. for FamVern
        regnskapstype = args[4];            // regnskapstypen f.eks. 0A eller 0AK3
        statistiskEnhet = args[5];          // org.nr
      }


//----------------------------------------------------------------------------------------
//----- Argument |    0      |     1       |       2       |     3     |        4         |
//----- Skjema   |  region   |  kildefil   |  rapportfil   |  1 - 10   |      org.nr.     |
//----- 11       |    X      |     -       |       -       |     0     |        X         |
//----- 11C      |    X      |     -       |       -       |     7     |        X         |
//----- 15       |    X      |     -       |       -       |     2     |        X         |
//----------------------------------------------------------------------------------------

    } else if (args.length == 3 || args.length == 5) { // Versjon for les/skriv
      // fra/til fil.
      region_nr = args[0];

      if (args.length == 3) {
        type_filuttrekk_tmp = args[1];      //områdekode f.eks. 4 for regnskap
        statistiskEnhet = args[2];          // org.nr

      } else {
        kildefil = new File(args[1]);
        rapportfil = new File (args[2]);
        type_filuttrekk_tmp = args[3];      //områdekode f.eks. 4 for regnskap
        statistiskEnhet = args[4];          // org.nr
      }


      // Sjekker om kildefil er lesbar.
      if (kildefil != null && !kildefil.canRead())
        throw new IOException
            ("Kan ikke lese kildefil: " + kildefil);

      // Sjekker om rapportfil kan skrives.
      boolean rapportfilIsWritable;
      try {
        rapportfilIsWritable = (rapportfil != null && (
                (rapportfil.exists() && rapportfil.canWrite())
                ||
                rapportfil.createNewFile())
        );
      } catch (Exception e) {
        rapportfilIsWritable = false;
      }

      if (rapportfil != null && !rapportfilIsWritable) {
        throw new IOException
                ("Kan ikke skrive rapportfil: " + rapportfil);
      }
    } else { // Feil antall argumenter.
      throw new IllegalArgumentException ("Feil antall argumenter!\n" + USAGE);
    }

    if (! Regioner.regionNrIsValid(region_nr) && ! RegionerKvartal.regionNrIsValid(region_nr))
      throw new IllegalArgumentException ("Ugyldig regionnr.: " + region_nr);

    if (! typeFiluttrekkIsValid(type_filuttrekk_tmp)) {
      throw new IllegalArgumentException
              ("Ugyldig type filuttrekk: " + type_filuttrekk_tmp);
    }
  } // End method parseArguments




  private boolean typeFiluttrekkIsValid (String argument) {

    boolean validArgument = false;
    try {
      type_filuttrekk = Integer.parseInt(argument);

      if (type_filuttrekk == SOSIALHJELP ||
//          type_filuttrekk == INTRODUKSJONSSTONAD ||
          type_filuttrekk == KVALIFISERINGSSTONAD ||
          type_filuttrekk == BARNEVERN ||
          type_filuttrekk == FAMILIEVERN ||
          type_filuttrekk == FAMILIEVERN_B ||
          type_filuttrekk == FAMILIEVERN_53 ||
          type_filuttrekk == Meklinger_55 ||
          type_filuttrekk == REGNSKAP ||
          type_filuttrekk == VAR_26 ||
          type_filuttrekk == NO_CONTROL) {

        validArgument = true;
      }
    } catch (NumberFormatException e) {
        System.out.println(e.getMessage());
    }

    return validArgument;
  } // End method typeFiluttrekkIsValid




  // Main fanger opp Exceptions og skriver feilmeldinger til System.out.
  // Returkoder settes ihht. feiltype, enten for aa angi resultat av kontroller
  // eller for aa signalisere feiltype ved Exceptions.
  public static void main (String[] args) {
	  
    ControlProgram cp = new ControlProgram();
    int error_type_found;
    try {
        error_type_found = cp.doControl(args);
      System.out.println(error_type_found);
    } catch (IllegalArgumentException e) {
        System.out.println(e.toString());
        error_type_found = Constants.PARAMETER_ERROR;
    } catch (IOException e) {
        System.out.println(e.toString());
        error_type_found = Constants.IO_ERROR;
    } catch (UnknownRegnskapException e) {
        System.out.println(e.getExceptionMessage());
        error_type_found = Constants.CRITICAL_ERROR;
    } catch (UnreadableDataException e) {
        System.out.println(e.getExceptionMessage());
        error_type_found = Constants.CRITICAL_ERROR;
    } catch (NullPointerException e){
        error_type_found = Constants.CRITICAL_ERROR;
    } catch (Exception e) {
      System.out.println(e.toString());
        error_type_found = Constants.SYSTEM_ERROR;
    }

    System.exit(error_type_found);

  } // End method main

} // End class ControlProgram
