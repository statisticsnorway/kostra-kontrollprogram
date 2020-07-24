package no.ssb.kostra.controlprogram;

import java.io.*;
import java.util.*;

public class Arguments {
    private String skjema = " ";
    private String aargang = "    ";
    private String kvartal = " ";
    private String region = "      ";
    private String navn = "Uoppgitt";
    private String orgnr = "         ";
    private String foretaknr = "         ";

    private List<String> inputFileContent = new ArrayList<>();
    private List<String> outputFileContent = new ArrayList<>();

    public Arguments(String[] argv){
        GetOptDesc[] options = {
                new GetOptDesc('s', "schema", true),
                new GetOptDesc('y', "year", true),
                new GetOptDesc('q', "quarter", true),
                new GetOptDesc('r', "region", true),
                new GetOptDesc('n', "name", true),
                new GetOptDesc('u', "unit-orgnr", true),
                new GetOptDesc('c', "company-orgnr", true)
        };

        GetOpt parser = new GetOpt(options);
        Map optionsFound = parser.parseArguments(argv);
        Iterator it = optionsFound.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            char c = key.charAt(0);
            switch (c) {
                case 's':
                    skjema = (String) optionsFound.get(key);

                    // Kvartalsregnskap fra fylkene kommer inn med typeRegnskap=0AK*
                    // (for 0C-regnskap) eller typeRegnskap=0BK* (for 0D-regnskap).
                    // (P.g.a. lazy programming på Hubbus.)
                    // Retter dette her.
                    if (skjema.length() == 4 && Arrays.asList("K1", "K2", "K3", "K4").contains(skjema.substring(2))) {
                        if (skjema.substring(0, 1).equalsIgnoreCase("0A")) {
                            skjema = "0C" + skjema.substring(2);
                        } else if (skjema.substring(0, 1).equalsIgnoreCase("0B")) {
                            skjema = "0D" + skjema.substring(2);
                        }
                    }
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
                case '?':
//                    System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
                    break;
                default:
                    System.err.println("Unexpected option character: " + c);
                    System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
            }
        }

        if (skjema == null || skjema.trim().length() == 0){
            throw new IllegalArgumentException("parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A");
        }

        if (aargang == null || aargang.trim().length() == 0){
            throw new IllegalArgumentException("parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2020");
        }

        if (region == null || region.trim().length() == 0){
            throw new IllegalArgumentException("parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100");
        }
    }

    public String getSkjema() {
        return skjema;
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
        if (inputFileContent.isEmpty()){
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    // utelater tomme linjer
                    if (line.trim().length() != 0) {
                        inputFileContent.add(line);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return inputFileContent;
    }

    public InputStream getInputContentAsInputStream() {
        return System.in;
    }

    public void setInputFileContent(List<String> inputFileContent) {
        this.inputFileContent = inputFileContent;
    }

    public List<String> getOutputFileContent() {
        return outputFileContent;
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
                ", inputFileContent=" + inputFileContent +
                ", outputFileContent=" + outputFileContent +
                '}';
    }
}
