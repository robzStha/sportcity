package com.app.sportcity.server_protocols;

import android.app.Application;
import android.os.Environment;

import com.app.sportcity.applications.MyApplication;
import com.app.sportcity.utils.CommonMethods;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 120, 0))
                    .build();
        }
    };

    private RetrofitSingleton() {

        File httpCacheDirectory = new File(Environment.getDownloadCacheDirectory(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 120, 120))
                                .build();
                    }
                })
                .build();
//        okHttpClient.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);

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
