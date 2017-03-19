


package com.app.sportcity.adapters;

import com.app.sportcity.objects.ACF;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ACFTypeAdapter extends TypeAdapter<ACFTypeAdapter.ACFs> {

    private Gson gson = new Gson();

    @Override
    public void write(JsonWriter jsonWriter, ACFs acfs) throws IOException {
        gson.toJson(acfs, ACFs.class, jsonWriter);
    }

    @Override
    public ACFs read(JsonReader jsonReader) throws IOException {
        ACFs locations;

        jsonReader.beginObject();
        jsonReader.nextName();

        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            locations = new ACFs((ACF[]) gson.fromJson(jsonReader, ACF[].class));
        } else if(jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            locations = new ACFs((ACF) gson.fromJson(jsonReader, ACF.class));
        } else {
            throw new JsonParseException("Unexpected token " + jsonReader.peek());
        }

        jsonReader.endObject();
        return locations;
    }

    public class ACFs {
        public List<ACF> monuments;

        public ACFs(ACF ... ms) {
            monuments = Arrays.asList(ms);
        }
    }
}