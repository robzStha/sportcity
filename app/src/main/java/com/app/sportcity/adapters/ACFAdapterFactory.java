package com.app.sportcity.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by bugatti on 28/03/17.
 */

public class ACFAdapterFactory implements TypeAdapterFactory{

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        ACFTypeAdapter typeAdapter = null;
        try{
            if(type.getRawType() == List.class){
                typeAdapter = new ACFTypeAdapter((Class)((ParameterizedType)type.getType()).getActualTypeArguments()[0]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return typeAdapter;
    }
}
