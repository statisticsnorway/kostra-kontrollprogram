package no.ssb.kostra.felles;

public final class Constants {
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

    public static final boolean DEBUG = false;
}
