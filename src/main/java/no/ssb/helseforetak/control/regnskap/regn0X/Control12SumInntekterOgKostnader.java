package no.ssb.helseforetak.control.regnskap.regn0X;


import no.ssb.kostra.control.Constants;

final class Control12SumInntekterOgKostnader extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT = "Kontroll 12, Sum inntekter og kostnader = 0:";
    private final int MAX_DIFF = 10;

    private int controlSum = 0;

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineContributedToSum = false;

        String kontoKode = RecordFields.getKontokode(line);
        int belop;

        if (ControlKontokoder.validKontokode(kontoKode)) {
            try {
                belop = RecordFields.getBelopIntValue(line);
                controlSum = controlSum + belop;
                lineContributedToSum = true;
            } catch (Exception e) {
            }
        }

        return lineContributedToSum;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + lf + lf;
        if (foundError()) {
            errorReport += "\tSum inntekter og kostnader er forskjellig fra 0. " +
                    "Differanser på opptil +/- 10 000 kroner godtas." + lf;
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return Math.abs(controlSum) >= MAX_DIFF;
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}