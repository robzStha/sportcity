


package com.app.sportcity.adapters;

import com.app.sportcity.objects.ACF;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ACFTypeAdapter implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementTypeAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementTypeAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("acf")) {
                        jsonElement = jsonObject.get("acf");
                        if (jsonElement.isJsonArray()) {
                            String temp = "{\"show_in_store\": \"No\",\"price\": \"0\"}";
                            jsonElement = new JsonParser().parse(temp);
                        }
                    }
                }
                System.out.println("Json Element: " + jsonElement.toString());
                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }

    public class ACFs {
        public List<ACF> monuments;

        public ACFs(ACF... ms) {
            monuments = Arrays.asList(ms);
        }
    }
}