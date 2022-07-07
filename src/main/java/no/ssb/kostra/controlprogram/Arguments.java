package no.ssb.kostra.controlprogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@SuppressWarnings("SpellCheckingInspection")
public class Arguments {
    private String skjema = " ";
    private String aargang = "    ";
    private String kvartal = " ";
    private String region = "      ";
    private String navn = "Uoppgitt";
    private String orgnr = "         ";
    private String foretaknr = "         ";
    private boolean harVedlegg = true;
    private boolean isRunAsExternalProcess = false;

    private List<String> inputFileContent = null;

    public Arguments() {
    }

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
            if (line != null && line.trim().length() != 0) {
                this.inputFileContent.add(line);
            }
        }
    }

    public Arguments(final String[] argv) {
        GetOptDesc[] options = {
                new GetOptDesc('s', "schema", true),
                new GetOptDesc('y', "year", true),
                new GetOptDesc('q', "quarter", true),
                new GetOptDesc('r', "region", true),
                new GetOptDesc('n', "name", true),
                new GetOptDesc('u', "unit-orgnr", true),
                new GetOptDesc('c', "company-orgnr", true),
                new GetOptDesc('a', "attachment", true),
                new GetOptDesc('e', "external-process", true)
        };

        final var parser = new GetOpt(options);
        @SuppressWarnings("rawtypes") final Map optionsFound = parser.parseArguments(argv);
        for (var o : optionsFound.keySet()) {
            final var key = (String) o;
            final var c = key.charAt(0);
            switch (c) {
                case 's':
                    skjema = (String) optionsFound.get(key);
                    break;
                case 'y':
                    aargang = (String) optionsFound.get(key);
                    break;
                case 'q':
                    kvartal = (String) optionsFound.get(key);
                    break;
                case 'r':
                    region = (String) optionsFound.get(key);
                    break;
                case 'n':
                    navn = (String) optionsFound.get(key);
                    break;
                case 'u':
                    orgnr = (String) optionsFound.get(key);
                    break;
                case 'c':
                    foretaknr = (String) optionsFound.get(key);
                    break;
                case 'a':
                    final var vedlegg = (String) optionsFound.get(key);
                    if (vedlegg.equalsIgnoreCase("0"))
                        harVedlegg = false;
                    break;
                case 'e':
                    final var process = (String) optionsFound.get(key);
                    if (process.equalsIgnoreCase("1"))
                        isRunAsExternalProcess = true;

                    break;
                case '?':
//                    System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
                    break;
                default:
                    System.err.println("Unexpected option character: " + c);
                    System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
            }
        }

        if (skjema == null || skjema.trim().length() == 0) {
            throw new IllegalArgumentException("parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A");
        }

        if (aargang == null || aargang.trim().length() == 0) {
            throw new IllegalArgumentException("parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2020");
        }

        if (region == null || region.trim().length() == 0) {
            throw new IllegalArgumentException("parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100");
        }
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

            try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    // utelater tomme linjer
                    if (line.trim().length() != 0) {
                        inputFileContent.add(line);
                    }
                }
            } catch (IOException ignored) {
                // Ignore exception
            }
        }
        return inputFileContent;
    }

    public InputStream getInputContentAsInputStream() {
        return System.in;
    }

    public void setInputFileContent(final List<String> inputFileContent) {
        if (this.inputFileContent == null) {
            this.inputFileContent = new ArrayList<>();
        }

        for (var line : inputFileContent) {
            if (line != null && line.trim().length() != 0) {
                this.inputFileContent.add(line);
            }
        }
    }

    public boolean hasInputContent() {
        return this.inputFileContent == null || 0 < this.inputFileContent.size();
    }

    public boolean harVedlegg() {
        return harVedlegg;
    }

    public boolean isRunAsExternalProcess() {
        return isRunAsExternalProcess;
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
