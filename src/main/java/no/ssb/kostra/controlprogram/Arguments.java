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

    private String inputFilePath = "";
    private String outputFilePath = "";
    private File inputFile = null;
    private File outputFile = null;
    private List<String> inputFileContent = new ArrayList<>();
    private List<String> outputFileContent = new ArrayList<>();

    public Arguments(String[] argv) {
        GetOptDesc[] options = {
                new GetOptDesc('s', "schema", true),
                new GetOptDesc('y', "year", true),
                new GetOptDesc('q', "quarter", true),
                new GetOptDesc('r', "region", true),
                new GetOptDesc('n', "name", true),
                new GetOptDesc('u', "unit-orgnr", true),
                new GetOptDesc('c', "company-orgnr", true),
                new GetOptDesc('i', "input-file", true),
                new GetOptDesc('o', "output-file", true),
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
                            skjema = "0C" + skjema.substring(2);
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
                case 'i':
                    inputFilePath = (String) optionsFound.get(key);
                    break;
                case 'o':
                    outputFilePath = (String) optionsFound.get(key);
                    break;
                case '?':
                    System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
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

        if (inputFilePath != null && 0 < inputFilePath.length()){
            readFileFromPath();
        } else {
            readFileFromStdin(System.in);
        }
    }

    public boolean readFile() {
        return (inputFilePath == null)? readFileFromStdin(System.in): readFileFromPath();
    }


    public boolean readFileFromStdin(InputStream inputStream){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = reader.readLine()) != null)
                inputFileContent.add(line);

            return true;
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean readFileFromPath(){
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;

            while (( line = reader.readLine()) != null)
                inputFileContent.add(line);

            return true;
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public List<String> getInputFileContent() {
        return inputFileContent;
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
                ", inputFilePath='" + inputFilePath + '\'' +
                ", outputFilePath='" + outputFilePath + '\'' +
                ", inputFile=" + inputFile +
                ", outputFile=" + outputFile +
                ", inputFileContent=" + inputFileContent +
                ", outputFileContent=" + outputFileContent +
                '}';
    }
}
