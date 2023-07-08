package no.ssb.kostra.felles;

public class ErrorReportEntry {
    final String saksbehandler;
    final String journalnummer;
    final String individId;
    final String refNr;
    String kontrollNr;
    final String errorText;
    final int errorType;

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

    public String getJournalnummer() {
        return journalnummer.trim();
    }

    public String getIndividId() {
        return individId.trim();
    }

    public String getRefNr() {
        return refNr.trim();
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

    public int getErrorType() {
        return errorType;
    }
}
