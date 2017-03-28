package com.app.sportcity.adapters;

import com.app.sportcity.objects.ACF;
import com.app.sportcity.objects.ACFList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by bugatti on 28/03/17.
 */

public class ACFDeserializer implements JsonDeserializer<ACF>{
    @Override
    public ACF deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ACFList acfList = new ACFList();
        if(json.isJsonArray()){
            for(JsonElement element: json.getAsJsonArray()){
//                acfLis
            }
        }else{
//            acfList.
        }

        return null;
    }
}
