package no.ssb.kostra.controlprogram;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CLIArguments {
    private String schema = " ";
    private String year = "    ";
    private String quarter = " ";
    private String region = "      ";
    private String unitOrgnr = "         ";
    private String companyOrgnr = "         ";
    private String inputFilePath = "";
    private String outputFilePath = "";

    private File inputFile = null;
    private File outputFile = null;
    private String inputFileContent = "";
    private String outputFileContent = "";

    public CLIArguments(String[] argv) {
        boolean errs = false;
        GetOptDesc[] options = {
                new GetOptDesc('s', "schema", true),
                new GetOptDesc('y', "year", true),
                new GetOptDesc('q', "quarter", true),
                new GetOptDesc('r', "region", true),
                new GetOptDesc('u', "unit-orgnr", true),
                new GetOptDesc('c', "company-orgnr", true),
                new GetOptDesc('i', "input-file", true),
                new GetOptDesc('o', "output-file", true),
        };

        List<String> quarterCodes = Arrays.asList("K1","K2","K3","K4");
        GetOpt parser = new GetOpt(options);
        Map optionsFound = parser.parseArguments(argv);
        Iterator it = optionsFound.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            char c = key.charAt(0);
            switch (c) {
                case 's':
                    schema = (String) optionsFound.get(key);

                    // Kvartalsregnskap fra fylkene kommer inn med typeRegnskap=0AK*
                    // (for 0C-regnskap) eller typeRegnskap=0BK* (for 0D-regnskap).
                    // (P.g.a. lazy programming på Hubbus.)
                    // Retter dette her.
                    if (schema.length() == 4 && quarterCodes.contains(schema.substring(2))){
                        if (schema.substring(0,1).equalsIgnoreCase("0A")){
                            schema = "0C" + schema.substring(2);
                        } else if(schema.substring(0,1).equalsIgnoreCase("0B")){
                            schema = "0C" + schema.substring(2);
                        }
                    }
                    break;
                case 'y':
                    year = (String) optionsFound.get(key);
                    break;
                case 'q':
                    quarter = (String) optionsFound.get(key);
                    break;
                case 'r':
                    region = (String) optionsFound.get(key);
                    break;
                case 'u':
                    unitOrgnr = (String) optionsFound.get(key);
                    break;
                case 'c':
                    companyOrgnr = (String) optionsFound.get(key);
                    break;
                case 'i':
                    inputFilePath = (String) optionsFound.get(key);
                    break;
                case 'o':
                    outputFilePath = (String) optionsFound.get(key);
                    break;
                case '?':
                    errs = true;
                    break;
                default:
                    throw new IllegalStateException(
                            "Unexpected option character: " + c);
            }
        }
        if (errs) {
            System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
        }
    }

    public String getSchema() {
        return schema;
    }

    public String getYear() {
        return year;
    }

    public String getQuarter() {
        return quarter;
    }

    public String getRegion() {
        return region;
    }

    public String getUnitOrgnr() {
        return unitOrgnr;
    }

    public String getCompanyOrgnr() {
        return companyOrgnr;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public String getInputFileContent() {
        return inputFileContent;
    }

    public String getOutputFileContent() {
        return outputFileContent;
    }
}
