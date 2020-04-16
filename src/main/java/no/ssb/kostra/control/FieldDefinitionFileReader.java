package no.ssb.kostra.control;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FieldDefinitionFileReader {
    public static List<FieldDefinition> getFieldDefinition(String path) {
        InputStream is = FieldDefinitionFileReader.class.getResourceAsStream(path);
        List<FieldDefinition> fieldDefinitions = new ArrayList<>();

        try (JsonReader rdr = Json.createReader(is)) {
            JsonObject obj = rdr.readObject();
            JsonArray fileDescription = obj.getJsonArray("filbeskrivelse");

            for (int i = 0; i < fileDescription.size(); i++) {
                JsonObject rowObject = fileDescription.get(i).asJsonObject();
                JsonArray codeListArray = rowObject.getJsonArray("codeList");
                List<Code> codeList = new ArrayList<>();
                for (int j = 0; j < codeListArray.size(); j++) {
                    codeList.add(
                            new Code(
                                    codeListArray.get(i).asJsonObject().getString("code")
                                    , codeListArray.get(i).asJsonObject().getString("value")
                            )
                    );
                }

                fieldDefinitions.add(
                        new FieldDefinition(
                                rowObject.getInt("number")
                                , rowObject.getJsonString("name").getString()
                                , rowObject.getJsonString("dataType").getString()
                                , rowObject.getJsonString("viewType").getString()
                                , rowObject.getInt("from")
                                , rowObject.getInt("to")
                                , codeList
                                , rowObject.getJsonString("datePattern").getString()
                        )
                );
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        return fieldDefinitions;
    }
}