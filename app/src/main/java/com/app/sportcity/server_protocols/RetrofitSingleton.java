package com.app.sportcity.server_protocols;

import com.app.sportcity.utils.CommonMethods;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bugatti on 22/11/16.
 */
public class RetrofitSingleton {


    private static Retrofit retrofit = null;
    private static RetrofitSingleton retrofitSingleton = null;
    private static ApiCalls apiCalls = null;

//    private static RetrofitSingleton ourInstance = new RetrofitSingleton();
//
//    public static RetrofitSingleton getInstance() {
//        return ourInstance;
//    }

    private RetrofitSingleton() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder().
                baseUrl(CommonMethods.UrlHelper.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(ApiCalls.class);

    }

    public static ApiCalls getApiCalls() {
        if (retrofitSingleton == null) {
            retrofitSingleton = new RetrofitSingleton();
        }
        return apiCalls;
    }

    public static Retrofit getRetrofit(){
        if(retrofitSingleton == null){
            retrofitSingleton = new RetrofitSingleton();
        }
        return retrofit;
    }

}
