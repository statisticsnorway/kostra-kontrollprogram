package no.ssb.kostra.control;

public final class Constants {
    
    // Rapporteringsaar.
    public static final String kostraYear = "2020";
    public static final String kvartalKostraYear = "2020";

    // Linjeskift til bruk ved skriving av kontrollrapporter.
    public static final String lineSeparator 
            = System.getProperty("line.separator");
    
    // Brukes i forbindelse med datohåndtering
    public static final String datoFormatKort = "ddMMyy";
    public static final String datoFormatLangt = "yyyy-MM-dd";

    
    ////////////////////////////////////////////////////////////////////////////
    //
    // Konstanter som brukes ved System.exit.
    //
    ////////////////////////////////////////////////////////////////////////////
    
    // Resultat av kontroller.
    
    // Ingen feil er funnet.
    public static final int NO_ERROR = 0;
    
    // "Ikke-kritiske" feil er funnet.
    public static final int NORMAL_ERROR = 1;
    
    // "Kritiske" feil er funnet. Skal hindre innsending til SSB.
    public static final int CRITICAL_ERROR = 2;

    
    // Hvis programmet ikke lar seg kjoere normalt.
    
    // Uspesifikk feil.
    public static final int SYSTEM_ERROR = 253;   

    // Feil antall eller type argumenter ved start av programmet.
    public static final int PARAMETER_ERROR = 254;

    public static int getRapporteringsAar() {
      
      int aar;
      
      try {
        aar = Integer.parseInt(kostraYear);
      } catch (NumberFormatException e) {
        aar = -1;
        System.exit (SYSTEM_ERROR);
      }
      
      return aar;
    }
    
    public static int getRapporteringsAarTwoDigits() {
      
      int aar;
      
      try {
        aar = Integer.parseInt(kostraYear.substring(2));
      } catch (NumberFormatException e) {
        aar = -1;
        System.exit (SYSTEM_ERROR);
      }
      
      return aar;
    }

    public static final boolean DEBUG = false;
} // End class Constants.
