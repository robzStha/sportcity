package com.app.sportcity.server_protocols;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bugatti on 22/11/16.
 */

public class RetrofitRequest<T> {
    public ResponseInterface responseInterface = null;

    public void getResponse(Call<T> call, Class<T> type){
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.isSuccessful()){
                    responseInterface.onProcessFinish(response.body(), 1);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {

            }
        });
    }
}
