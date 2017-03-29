


package com.app.sportcity.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ACFTypeAdapter<T> extends TypeAdapter<List<T>> {
    private Class<T> adapterClass;

    public ACFTypeAdapter(Class<T> adapterClass){
        this.adapterClass = adapterClass;
    }

    @Override
    public void write(JsonWriter out, List<T> value) throws IOException {

    }

    @Override
    public List<T> read(JsonReader in) throws IOException {

        List<T> list =  new ArrayList<T>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ACFAdapterFactory())
                .create();

        if(in.peek()==JsonToken.BEGIN_OBJECT){
            T inning = (T) gson.fromJson(in, adapterClass);
            list.add(inning);
        }else if(in.peek()==JsonToken.BEGIN_ARRAY){
            in.beginArray();
            while (in.hasNext()){
                T inning = (T) gson.fromJson(in, adapterClass);
                list.add(inning);
            }
            in.endArray();
        }else {
            in.skipValue();
        }

        return list;
    }
}


/*TypeAdapterFactory {

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
}*/