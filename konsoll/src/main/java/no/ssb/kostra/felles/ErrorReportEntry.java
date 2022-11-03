package no.ssb.kostra.felles;

@SuppressWarnings("SpellCheckingInspection")
public class ErrorReportEntry {
    String saksbehandler = "";
    String journalnummer = "";
    String individId = "";
    String refNr = "";
    String kontrollNr = "Ikke Satt";
    String errorText = "";
    int errorType = Constants.NO_ERROR;

    public ErrorReportEntry() {
    }

    public ErrorReportEntry(
            final String saksbehandler,
            final String journalnummer,
            final String individId,
            final String refNr,
            final String kontrollNr,
            final String errorText,
            final int errorType) {

        this.saksbehandler = saksbehandler;
        this.journalnummer = journalnummer;
        this.individId = individId;
        this.refNr = refNr;
        this.kontrollNr = kontrollNr;
        this.errorText = errorText;
        this.errorType = errorType;
    }

    public String getSaksbehandler() {
        return saksbehandler.trim();
    }

    public void setSaksbehandler(final String saksbehandler) {
        this.saksbehandler = saksbehandler;
    }

    public String getJournalnummer() {
        return journalnummer.trim();
    }

    public void setJournalnummer(final String journalnummer) {
        this.journalnummer = journalnummer;
    }

    public String getIndividId() {
        return individId.trim();
    }

    public void setIndividId(final String individId) {
        this.individId = individId;
    }

    public String getRefNr() {
        return refNr.trim();
    }

    public void setRefNr(final String refNr) {
        this.refNr = refNr;
    }

    public String getKontrollNr() {
        return kontrollNr;
    }

    public void setKontrollNr(final String kontrollNr) {
        this.kontrollNr = kontrollNr;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(final String errorText) {
        this.errorText = errorText;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(final int errorType) {
        this.errorType = errorType;
    }
}
