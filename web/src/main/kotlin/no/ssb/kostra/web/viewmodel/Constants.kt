package no.ssb.kostra.web.viewmodel


object Constants {
    ////////////////////////////////////////////////////////////////////////////
    //
    // Konstanter som brukes ved System.exit.
    //
    ////////////////////////////////////////////////////////////////////////////

    // Resultat av kontroller.

    // Ingen feil er funnet.
    const val NO_ERROR = 0

    // "Ikke-kritiske" feil er funnet.
    const val NORMAL_ERROR = 1

    // "Kritiske" feil er funnet. Skal hindre innsending til SSB.
    const val CRITICAL_ERROR = 2


    // Hvis programmet ikke lar seg kjoere normalt.

    // Uspesifikk feil.
    const val SYSTEM_ERROR = 253

    // Feil antall eller type argumenter ved start av programmet.
    const val PARAMETER_ERROR = 254

}
