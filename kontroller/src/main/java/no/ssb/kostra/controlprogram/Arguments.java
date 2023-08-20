package no.ssb.kostra.controlprogram;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static no.ssb.kostra.controlprogram.ArgumentConstants.ATTACHMENT_ABBR;
import static no.ssb.kostra.controlprogram.ArgumentConstants.COMPANY_ORGNR_ABBR;
import static no.ssb.kostra.controlprogram.ArgumentConstants.EXTERNAL_PROCESS_ABBR;
import static no.ssb.kostra.controlprogram.ArgumentConstants.NAME_ABBR;
import static no.ssb.kostra.controlprogram.ArgumentConstants.QUARTER_ABBR;
import static no.ssb.kostra.controlprogram.ArgumentConstants.REGION_ABBR;
import static no.ssb.kostra.controlprogram.ArgumentConstants.SCHEMA_ABBR;
import static no.ssb.kostra.controlprogram.ArgumentConstants.UNIT_ORGNR_ABBR;
import static no.ssb.kostra.controlprogram.ArgumentConstants.YEAR_ABBR;

@SuppressWarnings("SpellCheckingInspection")
public final class Arguments {
    private String skjema = " ";
    private String aargang = "    ";
    private String kvartal = " ";
    private String region = "      ";
    private String navn = "Uoppgitt";
    private String orgnr = "         ";
    private String foretaknr = "         ";
    private boolean harVedlegg = true;
    private boolean isRunAsExternalProcess = false;

    private final InputStream inputFileStream = null;
    private List<String> inputFileContent = null;

    public Arguments(
            final String skjema, final String aargang, final String kvartal, final String region,
            final String navn, final String orgnr, final String foretaknr, final boolean harVedlegg,
            final boolean isRunAsExternalProcess, final List<String> inputFileContent) {

        this.setSkjema(skjema);
        this.aargang = aargang;
        this.kvartal = kvartal;
        this.region = region;
        this.navn = navn;
        this.orgnr = orgnr;
        this.foretaknr = foretaknr;
        this.harVedlegg = harVedlegg;
        this.isRunAsExternalProcess = isRunAsExternalProcess;
        this.inputFileContent = new ArrayList<>();

        for (var line : inputFileContent) {
            if (line != null && !line.trim().isEmpty()) {
                this.inputFileContent.add(line);
            }
        }
    }

    // for testing only
    public Arguments(final String[] argv) {

        final var parser = getGetOpt();
        @SuppressWarnings("rawtypes") final Map optionsFound = parser.parseArguments(argv);

        for (var o : optionsFound.keySet()) {
            final var key = (String) o;
            final var c = key.charAt(0);
            switch (c) {
                case SCHEMA_ABBR -> skjema = (String) optionsFound.get(key);
                case YEAR_ABBR -> aargang = (String) optionsFound.get(key);
                case QUARTER_ABBR -> kvartal = (String) optionsFound.get(key);
                case REGION_ABBR -> region = (String) optionsFound.get(key);
                case NAME_ABBR -> navn = (String) optionsFound.get(key);
                case UNIT_ORGNR_ABBR -> orgnr = (String) optionsFound.get(key);
                case COMPANY_ORGNR_ABBR -> foretaknr = (String) optionsFound.get(key);
                case ATTACHMENT_ABBR -> {
                    final var vedlegg = (String) optionsFound.get(key);
                    harVedlegg = vedlegg.equalsIgnoreCase("1");
                }
                case EXTERNAL_PROCESS_ABBR -> {
                    final var process = (String) optionsFound.get(key);
                    isRunAsExternalProcess = process.equalsIgnoreCase("1");
                }
                case '?' -> {
                }
//                    System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
                default -> {
                    System.err.println("Unexpected option character: " + c);
                    System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
                }
            }
        }

        if (skjema == null || skjema.trim().isEmpty()) {
            throw new IllegalArgumentException("parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A");
        }

        if (aargang == null || aargang.trim().isEmpty()) {
            throw new IllegalArgumentException("parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2020");
        }

        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100");
        }
    }

    @NotNull
    private static GetOpt getGetOpt() {
        GetOptDesc[] options = {
                new GetOptDesc(SCHEMA_ABBR, "schema", true),
                new GetOptDesc(YEAR_ABBR, "year", true),
                new GetOptDesc(QUARTER_ABBR, "quarter", true),
                new GetOptDesc(REGION_ABBR, "region", true),
                new GetOptDesc(NAME_ABBR, "name", true),
                new GetOptDesc(UNIT_ORGNR_ABBR, "unit-orgnr", true),
                new GetOptDesc(COMPANY_ORGNR_ABBR, "company-orgnr", true),
                new GetOptDesc(ATTACHMENT_ABBR, "attachment", true),
                new GetOptDesc(EXTERNAL_PROCESS_ABBR, "external-process", true)
        };

        return new GetOpt(options);
    }

    public String getSkjema() {
        return skjema;
    }

    public void setSkjema(final String skjema) {
        this.skjema = skjema;
    }

    public String getAargang() {
        return aargang;
    }

    public String getKvartal() {
        return kvartal;
    }

    public String getRegion() {
        return region;
    }

    public String getNavn() {
        return navn;
    }

    public String getOrgnr() {
        return orgnr;
    }

    public String getForetaknr() {
        return foretaknr;
    }

    public List<String> getInputContentAsStringList() {
        if (this.inputFileContent == null) {
            this.inputFileContent = new ArrayList<>();

            try (var reader = new BufferedReader(new InputStreamReader(inputFileStream))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    // utelater tomme linjer
                    if (!line.trim().isEmpty()) {
                        inputFileContent.add(line);
                    }
                }
            } catch (IOException ignored) {
                // Ignore exception
            }
        }
        return inputFileContent;
    }

    public void setInputFileContent(final List<String> inputFileContent) {
        if (this.inputFileContent == null) {
            this.inputFileContent = new ArrayList<>();
        }

        for (var line : inputFileContent) {
            if (line != null && !line.trim().isEmpty()) {
                this.inputFileContent.add(line);
            }
        }
    }

    public boolean harVedlegg() {
        return harVedlegg;
    }

    public boolean isRunAsExternalProcess() {
        return isRunAsExternalProcess;
    }

    public String getNewline() {
        return (isRunAsExternalProcess) ? "" : "\n";
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "skjema='" + skjema + '\'' +
                ", aargang='" + aargang + '\'' +
                ", kvartal='" + kvartal + '\'' +
                ", region='" + region + '\'' +
                ", navn='" + navn + '\'' +
                ", orgnr='" + orgnr + '\'' +
                ", foretaknr='" + foretaknr + '\'' +
                ", harVedlegg='" + harVedlegg + '\'' +
                ", inputFileContent=" + inputFileContent +
                ", externalProcess=" + isRunAsExternalProcess +
                '}';
    }
}
